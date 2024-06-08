package dev.erique.loja.infra.handler;

import dev.erique.loja.infra.handler.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


public class ResponseFactoryList {

    public ResponseFactoryList(LocalDateTime now, boolean b, String message, Serializable code, Object o, String observation) {
    }

    public static ResponseList<Object> delete(Object delete, String observation, String message) {
        return ok(null, observation,message);
    }

    public static ResponseList<Object> ok(List<Object> body) {
        return ok(body, "Consulta realizada com sucesso");
    }

    public static ResponseList<Object> ok(List<Object> body, String message) {
        return ResponseList(HttpStatus.OK.value(), body, message, "");
    }

    public static ResponseList<Object> ok(List<Object> body, String message, String observation) {
        return ResponseList(HttpStatus.OK.value(), body, message, observation);
    }

    public static ResponseList<Object> create(List<Object> body, String message, String observation) {
        return ResponseList(HttpStatus.CREATED.value(), body, message, observation);
    }

    private static ResponseList<Object> ResponseList(Serializable code, List<Object> body, String message, String observation) {
        return new ResponseList<>(LocalDateTime.now(), true, message, code, body, observation);
    }

    public static ResponseFactoryList error() {
        return error("Error", "Entre em contato com o suporte");
    }

    public static ResponseFactoryList exception(BusinessException be) {
        return error(be.getErrorCode(), be.getMessage(), be.getObservation());
    }

    public static ResponseFactoryList error(String message, String observation) {
        return error(500, message, observation);
    }

    public static ResponseFactoryList error(Serializable code, String message, String observation) {
        return new ResponseFactoryList(LocalDateTime.now(), false, message, code, null, observation);
    }
}
