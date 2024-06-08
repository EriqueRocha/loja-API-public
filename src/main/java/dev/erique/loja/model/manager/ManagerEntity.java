package dev.erique.loja.model.manager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;

@Entity
@Data
public class ManagerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    @Pattern(regexp = "^[0-9]{11}$")
    private String cpf;

    private String nome;

    private String email;

    @JsonIgnore
    private String senha;

    @JsonIgnore
    private String role = "MANAGER";

}
