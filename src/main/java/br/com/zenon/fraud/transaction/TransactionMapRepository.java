package br.com.zenon.fraud.transaction;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionMapRepository implements TransactionRepository{

    private final Map<String, Transaction> transactionMap;

    public TransactionMapRepository(List<Transaction> transactionList) {

//        Map<String, Transaction> map = new HashMap<>();
//        for (Transaction t : transactionList) {
//            map.put(t.origin().name(), t);   // chave = nome de origem ; valor = a transação
//        }
//        this.transactionMap = map;

        this.transactionMap = transactionList.stream()
                .collect(Collectors.toMap(
                        t -> t.origin().name(),
                        t -> t
                ));
    }

    @Override
    public Optional<Transaction> findByOriginName(String name) {
        return Optional.ofNullable(transactionMap.get(name));
    }
}
