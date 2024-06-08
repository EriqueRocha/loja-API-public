package dev.erique.loja.infra.handler.exception;


public class MobilePhoneAlreadyExistsException extends BusinessException{

    public MobilePhoneAlreadyExistsException(String campo) {
        super("O numero de celular '%s' já está cadastrado no sistema","409", "Utilize um numero diferente", campo);
    }
}
