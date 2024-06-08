package dev.erique.loja.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.erique.loja.infra.Converter;
import dev.erique.loja.infra.handler.*;
import dev.erique.loja.infra.security.jwt.JwtProperties;
import dev.erique.loja.integracao.asaas.*;
import dev.erique.loja.model.compraEfetuada.CompraEfetuadaEntity;
import dev.erique.loja.model.cliente.ClienteDto;
import dev.erique.loja.model.cliente.ClienteEntity;
import dev.erique.loja.model.cliente.ClienteRequest;
import dev.erique.loja.repository.ClienteRepository;
import dev.erique.loja.repository.CompraEfetuadaRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.BeanUtils;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ClienteService {

    private static final String MESSAGE = "Id não encontrado";
    private static final String MESSAGE2 = "use outro id";

    private final FieldType fieldType;

    private final AssasService assasService;

    private final ClienteRepository repository;

    private final CompraEfetuadaRepository compraEfetuadaRepository;

    private final JwtProperties jwtProperties;

    public ClienteService(ClienteRepository repository, CompraEfetuadaRepository compraEfetuadaRepository, JwtProperties jwtProperties, AssasService assasService, FieldType fieldType) {
        this.repository = repository;
        this.compraEfetuadaRepository = compraEfetuadaRepository;
        this.jwtProperties = jwtProperties;
        this.assasService = assasService;
        this.fieldType = fieldType;
    }

    private BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private ClienteEntity findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public Object save(ClienteRequest request) throws JsonProcessingException {

            int totalCount = assasService.getCustomerCount(request.cpfCnpj());
            ResponseEntity<Object> exception = fieldType.checkData(request, totalCount);

            if (exception != null){
                return exception;
            }

            String responseBody = assasService.createCustomer(request);

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            Customer customer = mapper.readValue(responseBody, Customer.class);

            String customerId = customer.getId();

            ClienteEntity entity = new ClienteEntity();

            BeanUtils.copyProperties(request, entity);

            entity.setCustomerId(customerId);
            entity.setSenha(passwordEncoder().encode(request.senha()));
            repository.save(entity);

            return ResponseEntity.status(HttpStatus.CREATED).body(ResponseFactory.create(customer,
                    "salvo com sucesso",
                    "Este Cliente já pode ser gerenciada pelo sistema"));
    }

    @Transactional
    public Object updateUserForManager(Integer id, ClienteRequest request){
        ResponseEntity<Object> exception = fieldType.checkData(request, id);

        if (exception != null){
            return exception;
        }

        ClienteEntity entity = this.findById(id);
        BeanUtils.copyProperties(request, entity);
        return ResponseFactory.ok(repository.save(entity),
                "Alteração salva com sucesso",
                "As modificações já estão disponiveis no sistema");
    }

    @Transactional
    public Object updateUserForUser(String token, ClienteRequest request){

        Integer id = findIdByLogin(extractSubjectFromToken(token));

        ClienteEntity entity = this.findById(id);

        ResponseEntity<Object> exception = fieldType.checkData(request, id);

        if (exception != null){
            return exception;
        }
        BeanUtils.copyProperties(request, entity);
        entity.setSenha(passwordEncoder().encode(request.senha()));
        return ResponseFactory.ok(repository.save(entity),
                 "Alteração salva com sucesso",
                 "As modificações já estão disponiveis no sistema");

    }

    public Object delete(Integer id) {

        boolean present = repository.existsById(id);
        if (present) {
            ClienteEntity entity = this.findById(id);
            ResponseEntity<String> responseEntity = assasService.deleteCustomer(entity);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                repository.deleteById(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFactory.error(404,
                        MESSAGE, MESSAGE2));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFactory.error(404,
                MESSAGE, MESSAGE2));
    }

    public Object deleteUser(String token) {

        Integer id = findIdByLogin(extractSubjectFromToken(token));
        boolean present = repository.existsById(id);
        if (present) {
            ClienteEntity clienteEntity = this.findById(id);

            ResponseEntity<String> responseEntity = assasService.deleteCustomer(clienteEntity);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {

                repository.deleteById(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFactory.error(404,
                        MESSAGE, MESSAGE2));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFactory.error(404,
                MESSAGE, MESSAGE2));
    }

    public List<Object> getList() {

        List<ClienteEntity> clienteList = repository.findAll();
        List<ClienteDto> clienteDtoList = new ArrayList<>();

        for (ClienteEntity cliente : clienteList) {
            ClienteDto clienteDto = new ClienteDto();

            BeanUtils.copyProperties(cliente,clienteDto);

            clienteDtoList.add(clienteDto);
        }
        return Collections.singletonList(ResponseFactory.ok(clienteDtoList));
    }

    private String extractSubjectFromToken(String authorizationHeader) {
        try {
            String token = authorizationHeader.replace(jwtProperties.getPrefix(), "");

            Claims claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getKey())
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public Object getOne(String token) {

        Integer id = findIdByLogin(extractSubjectFromToken(token));

        Optional<ClienteEntity> entity = repository.findById(id);
        if (entity.isPresent()){
            ClienteDto clienteDto = new ClienteDto();

            BeanUtils.copyProperties(entity.get(), clienteDto);

            return ResponseFactory.ok(clienteDto);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFactory.error(404,
                    MESSAGE,MESSAGE2));
        }
    }

    public Object getCobrancas(Integer id) {
        Optional<ClienteEntity> entity = repository.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseFactoryList.ok(Collections
                    .singletonList(compraEfetuadaRepository.findByCliente(entity)),
                    "Consulta realizada com sucesso","")).getBody();
    }

    public Object getComprasUser(String token) {

        Integer id = findIdByLogin(extractSubjectFromToken(token));

        if (id == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        Optional<ClienteEntity> entity = repository.findById(id);

        List<CompraEfetuadaEntity> compras = compraEfetuadaRepository.findByCliente(entity);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseFactoryList.ok(Collections.singletonList(compras.stream()
                        .map(Converter::convertCompraEfetuadaEntityToDto)
                        .toList())
        ))
        .getBody();
    }

    public Integer findIdByLogin(String login) {
        ClienteEntity cliente = findByLogin(login);

        if (cliente == null){
            return null;
        }

        return cliente.getId();
    }

    public ClienteEntity findByLogin(String login) {
        return repository.findByEmail(login);
    }

    public String findNomeByLogin(String email) {
        ClienteEntity usuario = findByLogin(email);
        return usuario.getName();
    }

    public Object getOneManager(Integer id) {
        Optional<ClienteEntity> entity = repository.findById(id);
        if (entity.isPresent()){
            ClienteDto clienteDto = new ClienteDto();

            BeanUtils.copyProperties(entity.get(), clienteDto);

            return ResponseFactory.ok(clienteDto);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFactory.error(404,
                    MESSAGE,MESSAGE2));
        }
    }
}
