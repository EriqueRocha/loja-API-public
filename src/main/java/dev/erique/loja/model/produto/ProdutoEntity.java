package dev.erique.loja.model.produto;

import dev.erique.loja.enums.Estoque;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Embeddable
@Data
public class ProdutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private String marca;

    private Float preco;

    @Enumerated(EnumType.STRING)
    private Estoque estoque;

    private String descricao;

    private String detalhes;

    @Column
    @ElementCollection
    private List<String> imagePaths;

}
