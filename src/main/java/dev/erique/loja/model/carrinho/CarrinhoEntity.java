package dev.erique.loja.model.carrinho;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class CarrinhoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //TODO: ajustar para ser produto e quantidade
    @ElementCollection
    private List<ProdutoQuantidade> produtos;

    @Column(length = 500)
    private String description;

}
