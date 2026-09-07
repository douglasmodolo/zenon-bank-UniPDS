# 🏦 Zenón Bank - Projeto Prático: Fundamentos Java

Bem-vindo ao repositório base do projeto prático da disciplina de Fundamentos Java. Este projeto simula o backend de uma fintech fictícia, o **Zenón Bank**, onde você aplicará conceitos desde manipulação de tipos básicos até processamento de alta performance e concorrência.

## 📋 Sobre o Projeto

O objetivo é consolidar os conhecimentos adquiridos em aula através de um cenário realista. Você lidará com:
* Modelagem de dados com **Records**.
* API de Collections e Streams.
* Manipulação de I/O de arquivos grandes.
* Tratamento de Exceções.
* Integração com Banco de Dados.
* Concorrência e Paralelismo.

Siga o passo a passo do quadro de atividades (Trello) do projeto prático dessa disciplina, disponível nos vídeos de projeto prático da disciplina.

## 💾 Sobre o Dataset (PaySim)

Os dados usados no projeto vêm do [PaySim](https://www.kaggle.com/datasets/ealaxi/paysim1), um simulador que reproduz transações financeiras móveis a partir de dados reais agregados. Cada linha do CSV representa uma transação, com campos como `step` (tempo), `type` (`PAYMENT`, `TRANSFER`, `CASH_OUT`, `CASH_IN`, `DEBIT`), `amount`, os clientes de origem/destino e seus saldos, além dos rótulos `isFraud` e `isFlaggedFraud`. As fraudes se concentram em transações do tipo `TRANSFER` e `CASH_OUT`.

## 📦 Requisitos

* **Git e GitHub**: para controle de versões e portifólio de repositórios.
* **Java JDK**: 21 (LTS).
* **IDE**: IntelliJ IDEA ou Eclipse.
* **Docker e Docker Compose**: Para a tarefa de Banco de Dados.
