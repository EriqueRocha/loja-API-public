package dev.erique.loja.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.erique.loja.model.cliente.ClienteRequest;
import dev.erique.loja.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PostMapping("/saveNew")
    @Operation(summary = "cadastrar um Cliente")
    public Object save(@RequestBody ClienteRequest request) throws JsonProcessingException {
        return service.save(request);
    }

    //TODO fazer o update ocorrer tbm na plataforma de pagamento
    @PutMapping("/update")
    @Operation(summary = "modifica o cliente logado")
    public Object updateUserforUser(@RequestHeader("Authorization") String token, @RequestBody ClienteRequest request) {
        return service.updateUserForUser(token, request);
    }

    @PutMapping("/updateCliente/manager{id}")
    @Operation(summary = "modifica um Cliente especifico")
    public Object updateUserForManager(@PathVariable Integer id, @RequestBody ClienteRequest request) {
        return service.updateUserForManager(id, request);
    }

    @DeleteMapping("/deleteManager/{id}")
    @Operation(summary = "deleta um Cliente")
    public Object deleteManager(@PathVariable Integer id){
        return service.delete(id);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "o cliente apaga sua conta")
    public Object delete(@RequestHeader("Authorization") String token){
        return service.deleteUser(token);
    }

    @GetMapping("/getList")
    @Operation(summary = "retorna a lista de Cliente")
    public List<Object> getList(){
        return service.getList();
    }

    @GetMapping("/getOne")
    @Operation(summary = "retorna os dados do cliente logado")
    public Object getOne(@RequestHeader("Authorization") String token){
        return service.getOne(token);
    }

    @GetMapping("/getOne/manager/{id}")
    @Operation(summary = "retorna um Cliente especifico")
    public Object getOneManager(@PathVariable Integer id){
        return service.getOneManager(id);
    }

    @GetMapping("/compras")
    @Operation(summary = "retorna as compras do cliente logado")
    public Object getComprasUser(@RequestHeader("Authorization") String token){
        return service.getComprasUser(token);
    }

    @GetMapping("/compras/manager/{id}")
    @Operation(summary = "retorna as compras de um cliente especifico")
    public Object getComprasManager(@PathVariable Integer id){
        return service.getCobrancas(id);
    }

}
