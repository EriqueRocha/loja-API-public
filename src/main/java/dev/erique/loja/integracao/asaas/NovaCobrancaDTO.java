package dev.erique.loja.integracao.asaas;

import dev.erique.loja.enums.BillingType;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;

@Data
public class NovaCobrancaDTO {

    private String customer;

    private BillingType billingType;

    private Float value;

    @Temporal(TemporalType.DATE)
    private String dueDate = String.valueOf(LocalDate.now());

    @Column(length = 500)
    private String description;

    //Dias após o vencimento para cancelamento do registro (somente para boleto bancário)
    private int daysAfterDueDateToCancellationRegistration;

    //Número de parcelas (somente no caso de cobrança parcelada)
    private int installmentCount;

    //Informe o valor total de uma cobrança que será parcelada (somente no caso de cobrança parcelada). Caso enviado este campo o installmentValue não é necessário, o cálculo por parcela será automático.
    private Float totalValue;

    //Valor de cada parcela (somente no caso de cobrança parcelada). Envie este campo em caso de querer definir o valor de cada parcela.
    private Float installmentValue;


}
