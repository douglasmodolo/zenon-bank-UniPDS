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
