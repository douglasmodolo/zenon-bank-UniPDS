package br.com.zenon.fraud.transaction;

import java.math.BigDecimal;

public record TransactionCustomer(
    String name,
    BigDecimal oldBalance,
    BigDecimal newBalance
){}
