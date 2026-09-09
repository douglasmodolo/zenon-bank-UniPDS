package br.com.zenon.fraud.transaction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionIngestor {

    private static final int LIMIT_LINES = 1000;

    public List<Transaction> execute(String filePath) throws IOException {
        List<Transaction> transactionList = new ArrayList<>();

        if (filePath.isBlank())
            return transactionList;

        var file = new FileReader(filePath);

        try (BufferedReader reader = new BufferedReader(file)) {
            String line;

            line = reader.readLine();

            int count = 0;
            while(((line = reader.readLine()) != null) && (count < LIMIT_LINES)) {
                parseLine(line).ifPresent(transactionList::add);
                count++;
            }
        }

        return transactionList;
    }

    private Transaction createTransaction(String[] values) {
        return new Transaction(
                Integer.parseInt(values[0]),
                TransactionType.valueOf(values[1]),
                new BigDecimal(values[2]),
                new TransactionCustomer(values[3], new BigDecimal(values[4]), new BigDecimal(values[5])),
                new TransactionCustomer(values[6], new BigDecimal(values[7]), new BigDecimal(values[8])),
                Integer.parseInt(values[9]) > 0,
                Integer.parseInt(values[10]) > 0
        );
    }

    private Optional<Transaction> parseLine(String line) {
        try {
            String[] values = line.split(",");
            return Optional.of(createTransaction(values));
        } catch (Exception e) {
            System.err.println("Erro: " + line + " | " + e);
            return Optional.empty();
        }
    }
}
