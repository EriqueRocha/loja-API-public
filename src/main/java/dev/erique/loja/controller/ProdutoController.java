package dev.erique.loja.controller;

import dev.erique.loja.model.produto.ProdutoRequest;
import dev.erique.loja.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping("/saveNew")
    @Operation(summary = "cadastrar um Produto")
    public Object save(@RequestBody ProdutoRequest request){
        return service.save(request);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "modifica um Produto especifico")
    public Object update(@PathVariable Integer id, @RequestBody ProdutoRequest request){
        return service.update(id, request);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "deleta um Produto")
    public Object delete(@PathVariable Integer id){
        return service.delete(id);
    }

    @GetMapping("/getList")
    @Operation(summary = "retorna a lista de Produtos")
    public Object getList(){
        return service.getList();
    }

    @GetMapping("/getOne/{id}")
    @Operation(summary = "retorna um Produto especifico")
    public Object getOne(@PathVariable Integer id){
        return service.getOne(id);
    }

    @PatchMapping("/addImage/{id}")
    @Operation(summary = "Adiciona imagens a um Produto especifico")
    public Object addImage(@PathVariable Integer id, @RequestBody List<String> imagePaths){
        return service.addImage(id,imagePaths);
    }


}
