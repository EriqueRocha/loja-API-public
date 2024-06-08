package dev.erique.loja.model.carrinho;

import dev.erique.loja.model.produto.ProdutoEntity;
import lombok.Data;

import java.util.List;

@Data
public class CarrinhoRequest {

    private List<ProdutoEntity> produtos;

}
