package dev.erique.loja.controller;

import dev.erique.loja.infra.security.Login;
import dev.erique.loja.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")

public class LoginResource {

    private final LoginService loginService;

    public LoginResource(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    @Operation(summary = "login cliente")
    public Object userLogin(@RequestBody Login login){
        return loginService.userLogin(login);
    }

    @PostMapping("/loginManager")
    @Operation(summary = "login administrador")
    public Object managerLogin(@RequestBody Login login){
        return loginService.managerLogin(login);
    }

}
