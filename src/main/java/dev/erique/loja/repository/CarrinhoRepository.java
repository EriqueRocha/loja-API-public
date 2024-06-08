package dev.erique.loja.repository;

import dev.erique.loja.model.carrinho.CarrinhoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrinhoRepository extends JpaRepository<CarrinhoEntity, Integer> {
}
