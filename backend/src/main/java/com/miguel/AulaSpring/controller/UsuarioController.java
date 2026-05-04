package com.miguel.AulaSpring.controller;

import com.miguel.AulaSpring.dto.UsuarioDTO;
import com.miguel.AulaSpring.entity.Usuario;
import com.miguel.AulaSpring.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Usuario> Listar(){
        return service.Listar();
    }

    @PostMapping
    public Usuario salvar(@RequestBody @Valid UsuarioDTO dto){
        return service.salvar(dto);
    }

    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable Long id){
        return service.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void deletarPorID(@PathVariable Long id){
        service.deletar(id);
    }

    @PutMapping("/{id}")
    public Usuario atualizar(@PathVariable Long id, @RequestBody UsuarioDTO dto) {
        return service.atualizar(id, dto);
    }

    @GetMapping("/filtro")
    public List<Usuario> filtrar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String email
    ) {
        return service.filtrar(nome, email);
    }
}
