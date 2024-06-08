package dev.erique.loja.service;

import dev.erique.loja.infra.handler.ResponseFactory;
import dev.erique.loja.infra.security.Login;
import dev.erique.loja.infra.security.Session;
import dev.erique.loja.infra.security.jwt.JwtFactory;
import dev.erique.loja.infra.security.jwt.JwtObject;
import dev.erique.loja.infra.security.jwt.JwtProperties;
import dev.erique.loja.model.cliente.ClienteEntity;
import dev.erique.loja.model.manager.ManagerEntity;
import dev.erique.loja.repository.ClienteRepository;
import dev.erique.loja.repository.ManagerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final ClienteService clienteService;

    private final ClienteRepository clienteRepository;

    private final ManagerRepository managerRepository;

    private final ManagerService managerService;

    private Session session;

    private final PasswordEncoder encoder;

    public LoginService(ClienteRepository clienteRepository, ClienteService clienteService, PasswordEncoder encoder, ManagerRepository managerRepository, ManagerService managerService) {
        this.clienteRepository = clienteRepository;
        this.clienteService = clienteService;
        this.encoder = encoder;
        this.managerRepository = managerRepository;
        this.managerService = managerService;
    }

    public Object userLogin(Login login){

        ClienteEntity entity = clienteRepository.findByEmail(login.getEmail());
        if(entity!=null ){
            this.session = new Session();
            session.setNome(clienteService.findNomeByLogin(login.getEmail()));

            boolean senhaValida = encoder.matches(login.getPassword(), entity.getSenha());

            if(!senhaValida){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFactory.error(
                        401, "esta senha não petence a este login", "verifique sua senha e tente novamente"));
            }

            JwtObject jwtObject = JwtObject.builder()
                    .subject(login.getEmail())
                    .issuedAt()
                    .expirationHours(4)
                    .roles(entity.getRole()==null?"USER":entity.getRole());

            session.setToken(JwtFactory.create(JwtProperties.PREFIX, JwtProperties.KEY, jwtObject));
            return ResponseFactory.ok(session,"Login realizado com sucesso");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFactory.error(
                    404, "Login não encontrado", "Crie uma conta antes de logar"));
        }


    }

    public Object managerLogin(Login login){

        ManagerEntity entity = managerRepository.findByEmail(login.getEmail());
        if(entity!=null ){
            this.session = new Session();
            session.setNome(managerService.findNomeByLogin(login.getEmail()));

            boolean senhaValida = encoder.matches(login.getPassword(), entity.getSenha());

            if(!senhaValida){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFactory.error(
                        401, "esta senha não petence a este login", "verifique sua senha e tente novamente"));
            }

            JwtObject jwtObject = JwtObject.builder()
                    .subject(login.getEmail())
                    .issuedAt()
                    .expirationHours(4)
                    .roles(entity.getRole()==null?"MANAGER":entity.getRole());

            session.setToken(JwtFactory.create(JwtProperties.PREFIX, JwtProperties.KEY, jwtObject));
            return ResponseFactory.ok(session,"Login realizado com sucesso");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseFactory.error(
                    404, "Login não encontrado", "Sua conta deve ser criada por outro administrador antes de logar"));
        }

    }

}
