package br.com.zenon.fraud;

import br.com.zenon.fraud.transaction.Transaction;
import br.com.zenon.fraud.transaction.TransactionType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FraudAnalyzer {

    public int countFrauds(List<Transaction> transactionList) {
        if (transactionList.isEmpty())
            return 0;

        List<Transaction> fraudList = transactionList.stream().filter(Transaction::isFraud).toList();
        return fraudList.size();
    }

    public List<BigDecimal> top3(List<Transaction> transactionList) {
        if (transactionList.isEmpty())
            return new ArrayList<>();

        List<Transaction> fraudList = transactionList.stream().filter(Transaction::isFraud).toList();

        List<Transaction> top3 = fraudList.stream()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .limit(3)
                .toList();

        return top3.stream().map(Transaction::amount).toList();
    }

    public List<String> suspects(List<Transaction> transactionList) {
        if (transactionList.isEmpty())
            return new ArrayList<>();

        List<Transaction> fraudList = transactionList.stream().filter(Transaction::isFraud).toList();

       return fraudList.stream()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .map(transaction -> transaction.origin().name())
                .distinct()
                .limit(5)
                .toList();
    }

    public BigDecimal totalLoss(List<Transaction> transactionList) {
        if (transactionList.isEmpty())
            return BigDecimal.ZERO;

        List<Transaction> fraudList = transactionList.stream().filter(Transaction::isFraud).toList();

        return fraudList.stream()
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long countFraudsByType(List<Transaction> transactionList, TransactionType transactionType) {
        if (transactionList.isEmpty())
            return 0;

        List<Transaction> fraudList = transactionList.stream().filter(Transaction::isFraud).toList();

        return fraudList.stream()
                .filter(transaction -> transaction.type() == transactionType)
                .count();
    }
}
