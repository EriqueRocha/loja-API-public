package dev.erique.loja.integracao.asaas;

import lombok.Data;

import java.util.List;

@Data
public class CustomerSearch {
    private  String id;
    private String object;
    private boolean hasMore;
    private int totalCount;
    private int limit;
    private int offset;
    private List<Customer> data;
}
