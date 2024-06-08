package dev.erique.loja.integracao.asaas.webHooks;

import lombok.Data;

@Data
public class DiscountDto {
    private Double value;
    private String limitDate;
    private Integer dueDateLimitDays;
    private String type;
}
