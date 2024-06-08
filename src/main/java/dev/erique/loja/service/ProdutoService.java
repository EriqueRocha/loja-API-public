package dev.erique.loja.service;

import dev.erique.loja.infra.handler.ResponseFactory;
import dev.erique.loja.infra.handler.exception.NomeAlreadyExistsException;
import dev.erique.loja.model.produto.ProdutoEntity;
import dev.erique.loja.model.produto.ProdutoRequest;
import dev.erique.loja.repository.ProdutoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private static final String MESSAGE = "Id não encontrado";
    private static final String MESSAGE2 = "use outro id";

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Object save(ProdutoRequest request) {
        return this.persist(null, request);
    }

    private ProdutoEntity findById(Integer id){
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public Object persist(Integer id, ProdutoRequest request){
        ProdutoEntity entity;

        if (id!= null) {
            entity = this.findById(id);

            if (entity==null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFactory.error(
                        404,MESSAGE,MESSAGE2));
            }

            if (entity.getNome().equals(request.getNome())
                    || repository.existsByNome(request.getNome())) {

                Exception error = new NomeAlreadyExistsException(request.getNome());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseFactory.error(409,
                        error.getMessage(),"Use outro nome"));
            }
            BeanUtils.copyProperties(request,entity);
            return ResponseFactory.ok(repository.save(entity),
                    "Alteração salva com sucesso",
                    "As modificações já estão disponiveis no sistema");

        } else {

            if (repository.existsByNome(request.getNome())){
                Exception error = new NomeAlreadyExistsException(request.getNome());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseFactory.error(409,
                        error.getMessage(),"Use outro nome"));
            }

            //TODO: ajeitar os tipos de erro para Cliente

            entity = new ProdutoEntity();

            BeanUtils.copyProperties(request,entity);
            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.create(repository.save(entity),
                    "salvo com sucesso",
                    "Este Produto já pode ser gerenciada pelo sistema"));
        }
    }

    @Transactional
    public Object update(Integer id, ProdutoRequest request) {
        return this.persist(id,request);
    }

    public Object delete(Integer id) {
        Optional<ProdutoEntity> entity = repository.findById(id);
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
        Optional<ProdutoEntity> entity = repository.findById(id);
        if (entity.isPresent()){
            return ResponseFactory.ok(entity);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFactory.error(404,
                    MESSAGE,MESSAGE2));
        }
    }

    public Object addImage(Integer id, List<String> imagePaths) {
        ProdutoEntity entity = findById(id);
        if (entity!=null && repository.existsByNome(entity.getNome())){
            entity.setImagePaths(imagePaths);
            repository.save(entity);
            return ResponseFactory.ok(entity.getImagePaths(),
                    "imagens adicionadas com sucesso");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFactory.error(404,
                    MESSAGE,MESSAGE2));
        }
    }

}
