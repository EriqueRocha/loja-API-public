package dev.erique.loja.model.carrinho;

import lombok.Data;

import javax.persistence.Column;
import java.util.List;

@Data
public class CarrinhoDto {

    private List<ProdutoQuantidade> produtos;

    @Column(length = 500)
    private String description;

}
