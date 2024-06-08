package dev.erique.loja.infra.handler.exception;


public class CpfAlreadyExistsException extends BusinessException{

    public CpfAlreadyExistsException(String campo) {
        super("O cpf '%s' já está cadastrado no sistema","409", "Utilize um cpf diferente", campo);
    }
}
