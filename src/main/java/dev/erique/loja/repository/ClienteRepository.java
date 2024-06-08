package dev.erique.loja.repository;

import dev.erique.loja.model.cliente.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Integer> {
    boolean existsByCpfCnpj(String cpf);

    boolean existsById(Integer id);

    @Query("SELECT c FROM ClienteEntity c WHERE c.id = :id")
    Optional<ClienteEntity>findById(@Param("id") Integer id);

    ClienteEntity findByEmail(String email);

    boolean existsByMobilePhone(String mobilePhone);

    boolean existsByEmail(String email);
}
