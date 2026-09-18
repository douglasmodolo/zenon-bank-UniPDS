# Roadmap

As 10 tarefas do projeto, em ordem crescente de dificuldade. Cada tarefa é desenvolvida em
uma branch própria (`tarefa/NN-nome`) e, ao final, mergeada na `main`.

**Níveis de dificuldade:**
🟢 Essencial · 🟡 Intermediária · ⚫ Desafio

**Status:** ✅ Concluída · 🚧 Em desenvolvimento · ⬜ A fazer

| # | Tarefa | Nível | Status | Foco técnico |
|---|--------|:-----:|:------:|--------------|
| 01 | Configuração do ambiente e dados (PaySim) | 🟢 | ✅ | IDE, Maven, download do dataset |
| 02 | Modelagem de transações com Records | 🟢 | ✅ | Imutabilidade, tipos corretos, encapsulamento |
| 03 | Ingestão de dados e I/O básico | 🟢 | ✅ | `java.io`, parsing manual, `List<Transaction>` |
| 04 | Tratamento de erros e Optional | 🟢 | ✅ | Exceções, validação no record, dados sujos |
| 05 | Análise de fraudes com Stream API | 🟢 | ✅ | filter / map / sorted / reduce / groupingBy |
| 06 | Benchmark de busca: List vs Map | 🟢 | ✅ | Complexidade O(n) vs O(1) |
| 07 | Relatório eficiente com Java NIO | 🟡 | ✅ | I/O sem estourar memória, lazy loading |
| 08 | Internacionalização do relatório | 🟡 | ⬜ | i18n, `Locale`, `ResourceBundle`, moeda/data |
| 09 | Persistência em banco com JDBC | 🟡 | ⬜ | MySQL, batch insert, Docker |
| 10 | Ingestão de dados completa e eficiente | ⚫ | ⬜ | Streams + concorrência / Virtual Threads |

> As tarefas 🟡 Intermediárias e a ⚫ Desafio são **opcionais** no enunciado — entregues por
> escolha, para ir além do essencial.

## Detalhe das tarefas

### 01 — Configuração do ambiente e dados (PaySim) 🟢 ✅
Configurar a IDE (IntelliJ/Eclipse) e o build (Maven), e baixar o dataset do PaySim para
`data/`. Fundação para todas as tarefas seguintes.

### 02 — Modelagem de transações com Records 🟢 ✅
Modelar a transação como um `record` imutável, escolhendo os tipos de dados corretos.
Espinha dorsal do projeto: todas as tarefas seguintes consomem esse tipo.

### 03 — Ingestão de dados e I/O básico 🟢 ✅
Criar a classe `TransactionIngestor`, que recebe o nome de um arquivo e devolve uma
`List<Transaction>`. Ler as primeiras 1.000 linhas do CSV com o `java.io` clássico e fazer
o parsing manual de cada linha.

### 04 — Tratamento de erros e Optional 🟢 ✅
Validar o record `Transaction` no momento da construção (nenhum valor nulo, `step >= 1`,
valores não negativos, `type` válido) e processar um CSV com linhas corrompidas — logando
os erros em `System.err` e seguindo a leitura sem abortar.

### 05 — Análise de fraudes com Stream API 🟢 ✅
Criar a classe `FraudAnalyzer` que, sobre 50.000 transações, usa a Stream API para:
contar fraudes, achar as 3 de maior valor, listar clientes suspeitos distintos, somar o
prejuízo total e contar fraudes por tipo.

### 06 — Benchmark de busca: List vs Map 🟢 ✅
Comparar, na prática, o desempenho de uma busca linear em `List` (O(n)) contra a busca em
`Map` (O(1)) sobre um grande volume de dados. Ver a complexidade algorítmica acontecer.

### 07 — Relatório eficiente com Java NIO 🟡 ✅
Gerar um relatório processando o arquivo grande com Java NIO, evitando carregar tudo na
RAM (`OutOfMemoryError`). Sentir a diferença entre carregar tudo vs. processar em stream.

### 08 — Internacionalização do relatório 🟡
Formatar dados sensíveis à cultura (moeda, data) e traduzir textos conforme o `Locale` do
usuário, usando `ResourceBundle`.

### 09 — Persistência em banco com JDBC 🟡
Conectar a aplicação a um MySQL real (via Docker) usando JDBC padrão, e ver a importância
do batch insert para ingestão massiva de dados.

### 10 — Ingestão de dados completa e eficiente ⚫
Processar o dataset completo limitando o uso de memória (Streams) e maximizando o hardware
com programação concorrente/paralela (incl. Virtual Threads).
