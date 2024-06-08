package dev.erique.loja.model.produto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.erique.loja.enums.Estoque;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Data
public class ProdutoRequest{
        private String nome;

        private String marca;

        private Float preco;

        @Enumerated(EnumType.STRING)
        private Estoque estoque;

        private String descricao;

        private String detalhes;

        @JsonIgnore
        private List<String> imagePathe;
}





