package dev.erique.loja.integracao.asaas;

import lombok.Data;

@Data
public class RetornoPagamento {
    String id;
    String customer;
    Float value;
    String description;
    String billingType;
    String invoiceUrl;
}
