package dev.erique.loja.integracao.asaas.webHooks;

import lombok.Data;

@Data
public class PaymentDto {

    private String object;
    private String id;
    private String dateCreated;
    private String customer;
    private String paymentLink;
    private Double value;
    private Double netValue;
    private Double originalValue;
    private Double interestValue;
    private String description;
    private String billingType;
    private String pixTransaction;
    private String status;
    private String dueDate;
    private String originalDueDate;
    private String paymentDate;
    private String clientPaymentDate;
    private Integer installmentNumber;
    private String invoiceUrl;
    private String invoiceNumber;
    private String externalReference;
    private Boolean deleted;
    private Boolean anticipated;
    private Boolean anticipable;
    private String creditDate;
    private String estimatedCreditDate;
    private String transactionReceiptUrl;
    private String nossoNumero;
    private String bankSlipUrl;
    private String lastInvoiceViewedDate;
    private String lastBankSlipViewedDate;

    private DiscountDto discount;

    private FineDto fine;

    private InterestDto interest;

    private Boolean postalService;
    private String custody;
    private String refunds;


}
