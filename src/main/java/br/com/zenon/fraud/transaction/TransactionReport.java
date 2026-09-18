package br.com.zenon.fraud.transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class TransactionReport {
    public void execute(String filePath) {
        Path path = Path.of(filePath);
        // Total de linhas
        try(Stream<String> lines = Files.lines(path)) {
            long linesCount = lines.skip(1).count();
            System.out.println("Total de linhas: " + linesCount);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Total de fraudes
        try(Stream<String> lines = Files.lines(path)) {
            long fraudTotal = lines.skip(1)
                    .filter(line -> Integer.parseInt(line.split(",")[9]) > 0)
                    .count();
            System.out.println("Total de fraudes: " + fraudTotal);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Valor total transacionado
        try(Stream<String> lines = Files.lines(path)) {
            BigDecimal total = lines.skip(1)
                    .map(line -> new BigDecimal(line.split(",")[2]))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            System.out.println("Valor total transacionado: " + total);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
