package dev.erique.loja.model.cliente;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.erique.loja.model.compraEfetuada.CompraEfetuadaEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Data
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Integer id;

    private String customerId;

    private String name;

    @Pattern(regexp = "^[0-9]{11}$")
    private String cpfCnpj;

    private String email;

    private String mobilePhone;

    @JsonIgnore
    private String senha;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<CompraEfetuadaEntity> compraEfetuadaEntity;

    @JsonIgnore
    private String role = "USER";

}
