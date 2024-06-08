package dev.erique.loja.model.cliente;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class ClienteDto {

    private String customerId;

    private String name;

    @Pattern(regexp = "^[0-9]{11}$")
    private String cpfCnpj;

    private String email;

    private String mobilePhone;

}
