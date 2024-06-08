package dev.erique.loja.model.compraEfetuada;

import dev.erique.loja.enums.BillingType;
import dev.erique.loja.enums.StatusPayment;
import dev.erique.loja.model.carrinho.CarrinhoDto;
import dev.erique.loja.model.carrinho.CarrinhoEntity;
import dev.erique.loja.model.cliente.ClienteEntity;
import lombok.Data;

import javax.persistence.*;

@Data
public class CompraEfetuadaDto {

    private String idPay;

    private Float value;

    private String status;

    @Enumerated(EnumType.STRING)
    private BillingType billingType;

    @Enumerated(EnumType.STRING)
    private StatusPayment statusPayment;

    private String linkPayment;

    private CarrinhoDto carrinhoEntity;

}
