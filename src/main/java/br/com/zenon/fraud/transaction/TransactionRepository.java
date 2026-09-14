package br.com.zenon.fraud.transaction;

import java.util.Optional;

public interface TransactionRepository {
    Optional<Transaction> findByOriginName(String name);
}
