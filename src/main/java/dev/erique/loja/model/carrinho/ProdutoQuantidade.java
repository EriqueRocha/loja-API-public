package dev.erique.loja.model.carrinho;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class ProdutoQuantidade {

    private Integer id;

    private int quantidade;

}
