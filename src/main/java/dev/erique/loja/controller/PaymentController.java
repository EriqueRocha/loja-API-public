package dev.erique.loja.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.erique.loja.integracao.asaas.NovaCobrancaRequest;
import dev.erique.loja.integracao.asaas.webHooks.PaymentNotificationDto;
import dev.erique.loja.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/webhook")
    public Object pointWebHook(@RequestBody PaymentNotificationDto paymentNotificationDto, @RequestHeader("Authorization") String token){
        return paymentService.webHook(paymentNotificationDto, token);
    }

    @PostMapping
    @Operation(summary = "gerar cobrança")
    public Object createPayment(@RequestBody NovaCobrancaRequest novaCobrancaRequest, @RequestHeader("Authorization") String token) throws JsonProcessingException {
        return paymentService.createCobranca(novaCobrancaRequest, token);
    }

}
