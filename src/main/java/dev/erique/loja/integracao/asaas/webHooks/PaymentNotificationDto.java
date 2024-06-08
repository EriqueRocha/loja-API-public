package dev.erique.loja.integracao.asaas.webHooks;

import lombok.Data;

@Data
public class PaymentNotificationDto {

    private String event;


    private PaymentDto payment;

}
