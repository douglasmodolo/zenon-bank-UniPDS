package br.com.zenon.fraud.transaction;

import java.math.BigDecimal;
import java.util.Objects;

public record TransactionCustomer(
    String name,
    BigDecimal oldBalance,
    BigDecimal newBalance
){
    public TransactionCustomer {
        Objects.requireNonNull(name, "name should not be null");
        Objects.requireNonNull(oldBalance, "oldBalance should not be null");
        Objects.requireNonNull(newBalance, "newBalance should not be null");

        if (name.isBlank()) {
            throw new IllegalArgumentException("name should not be empty");
        }

        if (oldBalance.signum() < 0) {
            throw new IllegalArgumentException("oldBalance should be positive: " + oldBalance);
        }

        if (newBalance.signum() < 0) {
            throw new IllegalArgumentException("newBalance should be positive: " + newBalance);
        }
    }
}
