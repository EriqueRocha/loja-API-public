package dev.erique.loja.repository;

import dev.erique.loja.model.compraEfetuada.CompraEfetuadaEntity;
import dev.erique.loja.model.cliente.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompraEfetuadaRepository extends JpaRepository<CompraEfetuadaEntity, Integer> {

    @Query("SELECT ce FROM CompraEfetuadaEntity ce WHERE ce.cliente = :cliente")
    List<CompraEfetuadaEntity> findByCliente(Optional<ClienteEntity> cliente);

}
