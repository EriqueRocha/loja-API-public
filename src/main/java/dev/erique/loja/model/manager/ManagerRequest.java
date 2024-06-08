package dev.erique.loja.model.manager;

public record ManagerRequest(

        String cpf,
        String nome,
        String email,
        String senha

) {
}
