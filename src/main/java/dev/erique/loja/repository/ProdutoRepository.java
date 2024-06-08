package dev.erique.loja.repository;

import dev.erique.loja.model.cliente.ClienteEntity;
import dev.erique.loja.model.produto.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Integer> {
    boolean existsByNome(String nome);

    Optional<ProdutoEntity> findById(Integer id);

}
