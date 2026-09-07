package br.com.zenon.fraud;

import br.com.zenon.fraud.transaction.Transaction;
import br.com.zenon.fraud.transaction.TransactionCustomer;
import br.com.zenon.fraud.transaction.TransactionType;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        var transaction1 = new Transaction(
                1,
                TransactionType.PAYMENT,
                new BigDecimal("9839.64"),
                new TransactionCustomer("C1231006815", new BigDecimal("170136.00"), new BigDecimal("160296.36")),
                new TransactionCustomer("M1979787155", new BigDecimal("0"), new BigDecimal("0")),
                true,
                false);

        System.out.println(transaction1);

        var transaction2 = new Transaction(
                743,
                TransactionType.CASH_OUT,
                new BigDecimal("850002.52"),
                new TransactionCustomer("C1280323807", new BigDecimal("850002.52"), new BigDecimal("0")),
                new TransactionCustomer("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63")),
                true,
                false);

        System.out.println(transaction2);
    }
}
