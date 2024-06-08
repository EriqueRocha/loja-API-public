package dev.erique.loja.model.cliente;

import javax.validation.constraints.Pattern;

public record ClienteRequest(

        String name,
        String email,
        String mobilePhone,
        @Pattern(regexp = "^[0-9]{11}$")
        String cpfCnpj,
        String senha

) {
}
