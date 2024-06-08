package dev.erique.loja.infra.handler;

import dev.erique.loja.infra.handler.exception.CpfAlreadyExistsException;
import dev.erique.loja.infra.handler.exception.EmailAlreadyExistsException;
import dev.erique.loja.infra.handler.exception.MobilePhoneAlreadyExistsException;
import dev.erique.loja.infra.handler.exception.RequiredFieldException;
import dev.erique.loja.model.cliente.ClienteEntity;
import dev.erique.loja.model.cliente.ClienteRequest;
import dev.erique.loja.model.produto.ProdutoRequest;
import dev.erique.loja.repository.ClienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FieldType {

    private static final String MESSAGE = "Id não encontrado";
    private static final String MESSAGE2 = "use outro id";

    private final ClienteRepository clienteRepository;

    public FieldType(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    private ClienteEntity findById(Integer id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public ResponseEntity checkData(ProdutoRequest request){

        int errorType = 0;

        if (request.getNome() == null || request.getNome().isEmpty()){
            errorType = 1;
            return errorType(errorType,null);
        }

        return errorType(errorType,null);
    }

    public ResponseEntity checkData(ClienteRequest request, Integer id){

        ClienteEntity entity = this.findById(id);

        int errorType = 0;

        if (entity == null) {
            errorType = 9;
            return errorType(errorType,null);
        }
        if (entity.getCpfCnpj().equals(request.cpfCnpj())
                || clienteRepository.existsByCpfCnpj(request.cpfCnpj())) {
            errorType = 10;
            return errorType(errorType,request);
        }
        if (entity.getMobilePhone().equals(request.mobilePhone())
                || clienteRepository.existsByMobilePhone(request.mobilePhone())) {
            errorType = 7;
            return errorType(errorType,request);
        }
        if (entity.getEmail().equals(request.email())
                || clienteRepository.existsByEmail(request.email())) {
            errorType = 8;
            return errorType(errorType,request);
        }

        return errorType(errorType,null);
    }

    public ResponseEntity checkData(ClienteRequest request, int totalCount){

        int errorType = 0;

        if (request.name() == null || request.name().isEmpty()){
            errorType = 1;
            return errorType(errorType,null);
        }
        if (request.email() == null || request.email().isEmpty()){
            errorType = 2;
            return errorType(errorType,null);
        }
        if (request.mobilePhone() == null || request.mobilePhone().isEmpty()){
            errorType = 3;
            return errorType(errorType,null);
        }
        if (request.cpfCnpj() == null || request.cpfCnpj().isEmpty()){
            errorType = 4;
            return errorType(errorType,null);
        }
        if (request.senha() == null || request.senha().isEmpty()){
            errorType = 5;
            return errorType(errorType,null);
        }
        if (totalCount >= 1) {
            errorType = 6;
            return errorType(errorType,request);
        }
        if (clienteRepository.existsByMobilePhone(request.mobilePhone())){
            errorType = 7;
            return errorType(errorType,request);
        }
        if (clienteRepository.existsByEmail(request.email())){
            errorType = 8;
            return errorType(errorType,request);
        }

        return errorType(errorType,request);
    }

    public ResponseEntity errorType(int errorType, ClienteRequest request){

        Exception error;
        switch (errorType) {
            case 1 -> {
                error = new RequiredFieldException("Nome");
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ResponseFactory.error(422,
                error.getMessage(), "Preencha o campo nome"));
            }
            case 2 -> {
                error = new RequiredFieldException("Email");
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ResponseFactory.error(422,
                error.getMessage(), "Preencha o campo email"));
            }
            case 3 -> {
                error = new RequiredFieldException("Mobile phone");
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ResponseFactory.error(422,
                error.getMessage(), "Preencha o campo número de celular"));
            }
            case 4 -> {
                error = new RequiredFieldException("CPF");
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ResponseFactory.error(422,
                error.getMessage(), "Preencha o campo CPF"));
            }
            case 5 -> {
                error = new RequiredFieldException("Senha");
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ResponseFactory.error(422,
                error.getMessage(), "Preencha o campo senha"));
            }
            case 6 -> {
                error = new CpfAlreadyExistsException(request.cpfCnpj());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseFactory.error(409,
                error.getMessage(), "Use outro CPF"));
            }
            case 7 -> {
                error = new MobilePhoneAlreadyExistsException(request.mobilePhone());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseFactory.error(409,
                error.getMessage(), "Use outro número de celular"));
            }
            case 8 -> {
                error = new EmailAlreadyExistsException(request.email());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseFactory.error(409,
                error.getMessage(), "Use outro email"));
            }
            case 9 -> {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFactory.error(
                        404, MESSAGE, MESSAGE2));
            }
            case 10 -> {
                error = new CpfAlreadyExistsException(request.cpfCnpj());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseFactory.error(409,
                        error.getMessage(), "o CPF está sendo usado em outra conta ou o CPF inserido é igual ao atual"));
            }
            case 0 -> {
                return null;
            }

            default -> throw new IllegalStateException("não foi encontrado erro para o valor: " + errorType);
        }
    }

}
