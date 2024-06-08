package dev.erique.loja.repository;

import dev.erique.loja.model.manager.ManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<ManagerEntity, Integer> {


    boolean existsByCpf(String cpf);

    ManagerEntity findByEmail(String email);
}
