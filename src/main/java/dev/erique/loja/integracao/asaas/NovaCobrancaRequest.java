package dev.erique.loja.integracao.asaas;

import dev.erique.loja.enums.BillingType;
import dev.erique.loja.model.carrinho.ProdutoQuantidade;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

@Data
public class NovaCobrancaRequest {

    private BillingType billingType;

    @Column(length = 500)
    private String description;

    //Dias após o vencimento para cancelamento do registro (somente para boleto bancário)
    private int daysAfterDueDateToCancellationRegistration;

    //Número de parcelas (somente no caso de cobrança parcelada)
    private int installmentCount;

    private List<ProdutoQuantidade> produtos;

}
