package dev.erique.loja.service;

import dev.erique.loja.infra.handler.ResponseFactory;
import dev.erique.loja.infra.handler.exception.CpfAlreadyExistsException;
import dev.erique.loja.infra.handler.exception.RecordNotFoundException;
import dev.erique.loja.model.manager.ManagerEntity;
import dev.erique.loja.model.manager.ManagerRequest;
import dev.erique.loja.repository.ManagerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ManagerService {

    private static final String MESSAGE = "Id não encontrado";
    private static final String MESSAGE2 = "use outro id";

    private final ManagerRepository repository;

    public ManagerService(ManagerRepository repository) {
        this.repository = repository;
    }

    private BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Transactional
    public Object save(ManagerRequest request) {
        return this.persist(null, request);
    }

    private ManagerEntity findById(Integer id){
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public Object persist(Integer id, ManagerRequest request){
        ManagerEntity entity;

        if (id!= null) {
            entity = this.findById(id);

            if (entity==null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFactory.error(
                        404,MESSAGE,MESSAGE2));
            }

            if (entity.getCpf().equals(request.cpf())
                    || repository.existsByCpf(request.cpf())) {

                Exception error = new CpfAlreadyExistsException(request.cpf());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseFactory.error(409,
                        error.getMessage(),"Use outro CPF"));
            }
            BeanUtils.copyProperties(request,entity);
            return ResponseFactory.ok(repository.save(entity),
                    "Alteração salva com sucesso",
                    "As modificações já estão disponiveis no sistema");

        } else {

            if (repository.existsByCpf(request.cpf())){
                Exception error = new CpfAlreadyExistsException(request.cpf());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseFactory.error(409,
                        error.getMessage(),"Use outro CPF"));
            }

            //TODO: ajeitar os tipos de erro para manager

            entity = new ManagerEntity();

            BeanUtils.copyProperties(request,entity);

            entity.setSenha(passwordEncoder().encode(request.senha()));
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.create(repository.save(entity),
                    "salvo com sucesso",
                    "Este Manager já pode ser gerenciada pelo sistema"));
        }
    }

    @Transactional
    public Object update(Integer id, ManagerRequest request) {
        return this.persist(id,request);
    }

    public Object delete(Integer id) {
        Optional<ManagerEntity> entity = repository.findById(id);
        if(entity.isPresent()) {
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFactory.error(404,
                    MESSAGE,MESSAGE2));
        }
    }

    public Object getList() {
        return ResponseFactory.ok(repository.findAll());
    }

    public Object getOne(Integer id) {
        Optional<ManagerEntity> entity = repository.findById(id);
        if (entity.isPresent()){
            return ResponseFactory.ok(entity);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFactory.error(404,
                    MESSAGE,MESSAGE2));
        }
    }

    public ManagerEntity findByLogin(String login) {
        Optional<ManagerEntity> optionalManager = Optional.ofNullable(repository.findByEmail(login));
        if (optionalManager.isPresent()) {
            return optionalManager.get();
        } else {
            throw new RecordNotFoundException("Manager", "Login");
        }
    }

    public Integer findIdByLogin(String login) {
        ManagerEntity manager = findByLogin(login);
        return manager.getId();
    }

    public String findNomeByLogin(String email) {
        ManagerEntity manager = findByLogin(email);
        return manager.getNome();
    }
}
