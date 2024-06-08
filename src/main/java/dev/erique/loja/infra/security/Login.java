package dev.erique.loja.infra.security;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name="Login", description="Representacao dos dados de login")

public class Login {

    @Schema(description="E-mail do usuário cadastrado",example = "teste@email.com")
    private String email;
    @Schema(description="Senha do usuário cadastrado",example = "1234")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
