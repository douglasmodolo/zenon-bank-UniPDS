# Visão & Escopo

## Visão em uma linha

Um detector de fraude sobre transações financeiras simuladas (**PaySim**), usado como
laboratório para exercitar os **fundamentos do Java** — da modelagem de dados ao
processamento eficiente de grandes volumes.

## O contexto: Zenón Bank

O **Zenón Bank** é uma fintech fictícia. Ela processa transações financeiras móveis e
precisa identificar quais delas são fraudulentas. O projeto assume o papel do time de
engenharia que constrói, do zero, a ferramenta de análise dessas transações.

## O dataset: PaySim

O [PaySim](https://www.kaggle.com/datasets/ealaxi/paysim1) é um simulador que reproduz o
comportamento de transações financeiras móveis a partir de dados reais agregados. Cada
linha do CSV representa uma transação, com campos como:

| Campo | Significado |
|---|---|
| `step` | Unidade de tempo (1 step = 1 hora simulada). |
| `type` | Tipo da transação: `PAYMENT`, `TRANSFER`, `CASH_OUT`, `CASH_IN`, `DEBIT`. |
| `amount` | Valor da transação. |
| `nameOrig` / `oldbalanceOrg` / `newbalanceOrig` | Cliente de origem e seus saldos antes/depois. |
| `nameDest` / `oldbalanceDest` / `newbalanceDest` | Cliente de destino e seus saldos antes/depois. |
| `isFraud` | Se a transação é, de fato, fraude (rótulo). |
| `isFlaggedFraud` | Se o sistema sinalizou a transação como suspeita. |

A fraude, no PaySim, concentra-se em transações do tipo `TRANSFER` e `CASH_OUT` — o padrão
de esvaziar uma conta e sacar o dinheiro.

## Dentro do escopo

- Carregar, validar e modelar as transações do PaySim como objetos Java.
- Processar os dados com Collections, Streams e estruturas de dados adequadas.
- Ler arquivos grandes de forma eficiente (sem estourar a memória).
- Persistir dados em um banco relacional (MySQL) via JDBC.
- Gerar relatórios de fraude, inclusive internacionalizados (i18n).
- Processar o dataset completo com concorrência e paralelismo.

## Fora do escopo (de propósito)

- **Machine Learning / modelos preditivos.** Este é um projeto de *fundamentos Java*. A
  "detecção" usa os rótulos (`isFraud`) e regras simples já presentes no dataset — não
  treina modelos.
- **Interface gráfica / API web.** A entrega é uma aplicação de console (`Main`) que
  demonstra cada capacidade.
- **Dados reais / PII.** O PaySim é totalmente sintético.

## A medida de sucesso

1. **Cada tarefa entregue** cumpre o comportamento e o output esperado descritos no
   enunciado.
2. **O código é limpo e justificado** — as escolhas de design estão registradas em
   [`decisoes-tecnicas.md`](decisoes-tecnicas.md).
3. **O histórico de commits conta a evolução** — uma branch por tarefa, commits pequenos e
   descritivos.
