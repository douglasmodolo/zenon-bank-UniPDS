package br.com.zenon.fraud;

import br.com.zenon.fraud.transaction.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        //tarefa02();

//        try {
//            tarefa03();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

//        try {
//            tarefa04();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

//        try {
//            tarefa05();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        try {
            tarefa06();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void tarefa02() {
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

    private static void tarefa03() throws IOException {
        String filePath = "data/PS_20174392719_1491204439457_log.csv";
        TransactionIngestor ingestor = new TransactionIngestor();

        List<Transaction> transactionList = ingestor.execute(filePath, 1000);

        for (int i = 0; i < 10; i++) {
            System.out.println(transactionList.get(i));
        }
    }

    private static void tarefa04() throws IOException {
        String filePath = "data/paysim_with_bad_data.csv";
        TransactionIngestor ingestor = new TransactionIngestor();

        List<Transaction> transactionList = ingestor.execute(filePath, 16);

        System.out.println(transactionList.size());
        transactionList.forEach(System.out::println);
    }

    private static void tarefa05() throws IOException {
        String filePath = "data/PS_20174392719_1491204439457_log.csv";
        TransactionIngestor ingestor = new TransactionIngestor();

        List<Transaction> transactionList = ingestor.execute(filePath, 50000);

        FraudAnalyzer analyzer = new FraudAnalyzer();

        int fraudCount = analyzer.countFrauds(transactionList);
        System.out.println("1. Total de Fraudes: " + fraudCount);

        System.out.println("2. Top 3 Fraudes de Maior Valor:");
        List<BigDecimal> top3 = analyzer.top3(transactionList);
        top3.forEach(amount -> System.out.println(amount.setScale(2, RoundingMode.HALF_UP)));

        System.out.println("3. Clientes Suspeitos:");
        List<String> topSuspects = analyzer.suspects(transactionList);
        topSuspects.forEach(System.out::println);

        BigDecimal totalLoss = analyzer.totalLoss(transactionList);
        System.out.println("4. Prejuízo Total: " + totalLoss);

        System.out.println("5. Fraudes por Tipo:");
        long cashOutCount = analyzer.countFraudsByType(transactionList, TransactionType.CASH_OUT);
        System.out.println(" - CASH_OUT: " + cashOutCount);
        long transferCount = analyzer.countFraudsByType(transactionList, TransactionType.TRANSFER);
        System.out.println(" - TRANSFER: " + transferCount);
    }

    private static void tarefa06() throws IOException {
        String filePath = "data/PS_20174392719_1491204439457_log.csv";
        TransactionIngestor ingestor = new TransactionIngestor();

        List<Transaction> transactionList = ingestor.execute(filePath, 100000);

        TransactionRepository listRepository = new TransactionListRepository(transactionList);

        Optional<Transaction> transaction = Optional.empty();

        // 3.
        List<String> names = new ArrayList<>();
        names.add("C12345");
        names.add("C1231006815");

        for (var name : names) {
            transaction = listRepository.findByOriginName(name);

            if (transaction.isEmpty()) {
                System.out.println("Transação não encontrada para o cliente " + name);
            } else {
                System.out.println(transaction.get());
            }
        }

        // 4.
        long begin = System.nanoTime();
        transaction = listRepository.findByOriginName("C1868032458");
        long end = System.nanoTime();
        System.out.println("[List] Busca do pior caso: " + (end - begin) + "ns");

        // 6.
        TransactionRepository mapRepository = new TransactionMapRepository(transactionList);
        begin = System.nanoTime();
        transaction = mapRepository.findByOriginName("C1868032458");
        end = System.nanoTime();
        System.out.println("[Map] Busca do pior caso: " + (end - begin) + "ns");
    }
}
