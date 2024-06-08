package dev.erique.loja.model.compraEfetuada;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.erique.loja.enums.BillingType;
import dev.erique.loja.enums.StatusPayment;
import dev.erique.loja.model.carrinho.CarrinhoEntity;
import dev.erique.loja.model.cliente.ClienteEntity;
import lombok.Data;

import javax.persistence.*;


@Data
@Entity
public class CompraEfetuadaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String idPay;

    private Float value;

    private String status;

    @Enumerated(EnumType.STRING)
    private BillingType billingType;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @JsonIgnore
    private ClienteEntity cliente;

    @Enumerated(EnumType.STRING)
    private StatusPayment statusPayment;

    private String linkPayment;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonManagedReference
    private CarrinhoEntity carrinhoEntity;

    public CompraEfetuadaEntity() {

    }

}
