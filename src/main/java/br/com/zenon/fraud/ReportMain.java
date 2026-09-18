package br.com.zenon.fraud;

import br.com.zenon.fraud.transaction.TransactionReport;

public class ReportMain {
    public static void main(String[] args) {
        TransactionReport report = new TransactionReport();
        report.execute("data/PS_20174392719_1491204439457_log.csv");
    }
}
