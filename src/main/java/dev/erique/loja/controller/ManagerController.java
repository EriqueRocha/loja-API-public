package dev.erique.loja.controller;

import dev.erique.loja.model.manager.ManagerRequest;
import dev.erique.loja.service.ManagerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService service;

    public ManagerController(ManagerService service) {
        this.service = service;
    }

    @PostMapping("/saveNew")
    @Operation(summary = "cadastrar um Manager")
    public Object save(@RequestBody ManagerRequest request){
        return service.save(request);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "modifica um Manager especifico")
    public Object update(@PathVariable Integer id, @RequestBody ManagerRequest request){
        return service.update(id, request);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "deleta um Manager")
    public Object delete(@PathVariable Integer id){
        return service.delete(id);
    }

    @GetMapping("/getList")
    @Operation(summary = "retorna a lista de Manager")
    public Object getList(){
        return service.getList();
    }

    @GetMapping("/getOne/{id}")
    @Operation(summary = "retorna um Manager especifico")
    public Object getOne(@PathVariable Integer id){
        return service.getOne(id);
    }

}
