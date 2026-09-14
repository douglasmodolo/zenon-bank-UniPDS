package br.com.zenon.fraud.transaction;

import java.util.List;
import java.util.Optional;

public class TransactionListRepository implements TransactionRepository {

    private final List<Transaction> transactionList;

    public TransactionListRepository(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @Override
    public Optional<Transaction> findByOriginName(String name) {
        return transactionList.stream()
                .filter(transaction -> transaction.origin().name().equals(name))
                .findFirst();
    }
}
