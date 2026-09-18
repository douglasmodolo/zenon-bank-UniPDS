# Decisões Técnicas

Este documento registra **por que** o projeto foi feito de um certo jeito — não *o quê*
(isso está no código) nem *a lista de tarefas* (isso está no [roadmap](roadmap.md)), mas as
**escolhas de design** e o raciocínio por trás delas.

É o documento que mostra senioridade num portfólio: qualquer um cumpre a tarefa; poucos
explicam por que escolheram `BigDecimal` em vez de `double`, ou por que o parsing rejeita a
linha inteira em vez de adivinhar o campo faltante.

> **Como preenchemos isto:** uma entrada por decisão relevante, escrita **na hora em que a
> decisão é tomada** (normalmente durante uma tarefa). Não precisa ser longo — precisa ser
> honesto. Se uma decisão se provar errada depois, não apague: registre o que mudou e por
> quê. Um erro documentado vale mais que um acerto silencioso.

## Formato de cada decisão (ADR-lite)

```
### DT-NN — <título curto da decisão>
- **Tarefa:** em qual tarefa surgiu (# do roadmap).
- **Decisão:** o que foi escolhido, em uma frase.
- **Por quê:** o motivo — o trade-off que estamos aceitando.
- **Alternativas descartadas:** o que consideramos e não usamos, e por quê.
- **Status:** aceita / revisada / substituída por DT-XX.
```

---

## Decisões em aberto (a resolver junto, conforme as tarefas chegam)

Pontos que já identificamos como escolhas importantes, mas que ainda **não** foram
decididos. Viram uma entrada `DT-NN` abaixo quando batermos o martelo.

- **Estratégia de parsing e tratamento de linha inválida.** Rejeitar a linha inteira ao
  primeiro campo inválido, ou tentar recuperar? → decisão na **Tarefa 04**.

---

## Decisões tomadas

<!-- Mais novas embaixo. Uma entrada DT-NN por decisão, no formato acima. -->

### DT-01 — Java 21 (LTS)
- **Tarefa:** 01 (fundação, confirmado no início da 02).
- **Decisão:** o projeto usa Java 21.
- **Por quê:** é a versão configurada no `pom.xml`, é LTS e cobre todos os recursos que o
  projeto exige (Records, Stream API, Virtual Threads na Tarefa 10).
- **Alternativas descartadas:** Java 25 — o README original do professor citava 25, mas não
  há necessidade que justifique subir de versão; alinhamos tudo em 21.
- **Status:** aceita.

### DT-02 — Estrutura do modelo: records aninhados + enum
- **Tarefa:** 02.
- **Decisão:** modelar em 3 peças — `enum TransactionType` (PAYMENT, TRANSFER, CASH_OUT,
  CASH_IN, DEBIT), `record TransactionCustomer(name, oldBalance, newBalance)` e
  `record Transaction(step, type, amount, origin, recipient, isFraud, isFlaggedFraud)`,
  onde `origin` e `recipient` são ambos `TransactionCustomer`.
- **Por quê:** os 3 campos de cada cliente (nome + 2 saldos) são coesos — descrevem *um
  cliente na transação* e andam sempre juntos; agrupá-los num record próprio deixa o modelo
  legível e evita 11 campos soltos. Origem e destino têm forma idêntica, então o mesmo tipo
  serve aos dois papéis (sem duplicação). O enum dá segurança de tipo e gera de graça a
  validação da Tarefa 04. Além disso, esse desenho reproduz exatamente o `toString` padrão
  de record mostrado nos outputs esperados.
- **Alternativas descartadas:** record "achatado" com 11 campos (baixa coesão, ilegível);
  `type` como `String` (sem segurança de tipo, sem a mensagem de erro pronta do enum).
- **Status:** aceita.

### DT-03 — Valores monetários com BigDecimal (construído a partir de String)
- **Tarefa:** 02.
- **Decisão:** `amount` e os saldos são `BigDecimal`, sempre construídos a partir da String
  lida do CSV (`new BigDecimal(campo)`), nunca a partir de `double`.
- **Por quê:** `double`/`float` são binários e não representam decimais exatamente
  (`0.1 + 0.2 != 0.3`), o que gera erro de arredondamento inaceitável em dinheiro e se
  acumula nas somas (ex.: prejuízo total da Tarefa 05). `BigDecimal` é exato em base 10 e
  dá controle de arredondamento. Construir a partir da String preserva a escala original do
  arquivo — é o que faz o output bater (`170136.0` com 1 casa, `5000.00` com 2 casas), algo
  que `double` perderia (`5000.00` viraria `5000.0`).
- **Alternativas descartadas:** `double` (erro de ponto flutuante, quebra o output);
  `new BigDecimal(double)` (reintroduz a sujeira binária do double).
- **Status:** aceita.

### DT-04 — TransactionIngestor como service de instância (não static)
- **Tarefa:** 03.
- **Decisão:** o `TransactionIngestor` é uma classe `public` com método de **instância**
  (`new TransactionIngestor().ingest(arquivo)`), não métodos `static`.
- **Por quê:** é a forma idiomática de um "service" em Java — services são objetos, não
  funções soltas; facilita testes e evolução (ex.: futura configuração de separador ou
  limite de linhas encaixa no objeto). Alinha com o modelo que aparece adiante no curso
  (Spring/JDBC, onde tudo é instância).
- **Alternativas descartadas:** método `static` — funcionaria por a classe ser stateless,
  mas "tudo static" tende a virar código procedural vestido de OO e é menos testável.
- **Status:** aceita.

### DT-05 — Parsing de linha com Optional<Transaction>
- **Tarefa:** 04.
- **Decisão:** o parsing de uma linha fica num método `parseLine(String)` que devolve
  `Optional<Transaction>` — `Optional.of(...)` quando a linha é válida e, no `catch`, loga o
  erro em `System.err` (`Erro: <linha> | <exceção>`) e devolve `Optional.empty()`. O loop
  consome com `parseLine(line).ifPresent(transactionList::add)`.
- **Por quê:** separa "parsear uma linha (pode falhar)" de "coletar os resultados"; a
  assinatura `Optional<Transaction>` documenta que o parse pode não produzir nada (melhor que
  devolver `null` escondido) e prepara o terreno para o estilo funcional da Tarefa 05
  (Streams).
- **Alternativas descartadas:** try/catch inline no loop — também correto e mais direto, mas
  mistura as duas responsabilidades e não compõe tão bem.
- **Status:** aceita.

### DT-06 — Limite de linhas obrigatório no TransactionIngestor
- **Tarefa:** 05.
- **Decisão:** `execute(String filePath, int limit)` — o limite de linhas a ler é um
  parâmetro **obrigatório**; não há sobrecarga "ler tudo". Cada tarefa passa quanto quer
  (03 → 1.000; 05 → 50.000).
- **Por quê:** exigir o limite é uma escolha defensiva e intencional — o CSV do PaySim tem
  ~6,3 milhões de linhas (471 MB), e um "ler tudo" descuidado carregaria tudo numa `List` e
  estouraria a memória (`OutOfMemoryError`). Obrigar o limite força quem chama a decidir
  conscientemente quanto carrega, que é o cerne das lições de I/O eficiente das Tarefas 07
  (NIO) e 10 (concorrência/streaming).
- **Alternativas descartadas:** sobrecarga `execute(filePath)` lendo o arquivo inteiro —
  elegante para arquivos pequenos, mas vira um footgun de memória no arquivo grande.
- **Status:** aceita.

### DT-07 — FraudAnalyzer com um método por análise (calcula, não imprime)
- **Tarefa:** 05.
- **Decisão:** o `FraudAnalyzer` tem **um método por análise**, cada um **retornando** o
  resultado (ex.: contagem, lista das top 3, prejuízo total, mapa por tipo). A `Main`
  orquestra as chamadas e cuida da **impressão** (a formatação com `1.`, `2.`, ...).
- **Por quê:** separa cálculo (analyzer) de apresentação (Main) — mesma linha do
  `TransactionIngestor`, que retorna e deixa a `Main` imprimir; alinha com o enunciado
  ("invoque cada um dos métodos"); e deixa cada análise testável isoladamente.
- **Alternativas descartadas:** um único `execute` que roda as 5 análises e imprime tudo —
  mais simples, mas mistura cálculo com apresentação e não bate com o "cada um dos métodos".
- **Status:** aceita.

### DT-08 — Busca abstraída por interface TransactionRepository (List e Map)
- **Tarefa:** 06.
- **Decisão:** a busca por transação é definida na interface `TransactionRepository`
  (`Optional<Transaction> findByOriginName(String name)`), com duas implementações: uma
  baseada em `List` (busca linear, O(n)) e outra em `Map<String, Transaction>` indexado pelo
  nome de origem (busca O(1)). O repositório **é dono dos dados** (recebe a `List` no
  construtor); a implementação com `Map` constrói o mapa a partir da lista no construtor. A
  `Main` declara a variável pelo tipo da interface e troca de implementação mudando uma
  única linha.
- **Por quê:** exercita "programar para uma interface, não para uma implementação" — o
  código chamador não muda ao trocar a estrutura de dados; e permite comparar na prática
  O(n) (List) vs O(1) (Map). Benchmark: busca do pior caso (`C1868032458`, último dos 100k)
  ~6,7 ms na List vs ~0,012 ms no Map (~567×).
- **Alternativas descartadas:** passar a `List` como parâmetro do método de busca — impediria
  a implementação com `Map` (que precisa da sua própria estrutura), quebrando a abstração.
- **Status:** aceita.

### DT-09 — Relatório do arquivo completo com Files.lines (lazy), em 3 passadas
- **Tarefa:** 07.
- **Decisão:** o `TransactionReport` processa o arquivo completo (~493MB) com
  `java.nio.file.Files.lines(Path)` (stream preguiçoso, linha a linha) dentro de
  try-with-resources, calculando 3 agregados (total de linhas, de fraudes, valor total) com
  operações terminais puras (`count`, `filter+count`, `map+reduce`) — sem montar
  `List<Transaction>`. Escolhida a abordagem de **3 passadas** (uma por métrica).
- **Por quê:** ler para uma `List` estouraria a memória (6,3M objetos); o stream lazy mantém
  só uma linha por vez na RAM (roda em `-Xmx128m`). As 3 passadas são mais simples e legíveis;
  o custo é reler o arquivo 3× (aceitável para o exercício). Operações terminais puras
  (count/reduce) evitam o anti-padrão de `forEach` com estado mutável externo e dispensam
  `Atomic*` (que só fariam sentido em stream paralelo).
- **Alternativas descartadas:** carregar tudo em `List` (OutOfMemoryError); uma única passada
  acumulando os 3 valores (mais eficiente, lê o arquivo 1×, mas exige acumuladores mutáveis e
  mais complexidade — não compensou aqui).
- **Status:** aceita.
