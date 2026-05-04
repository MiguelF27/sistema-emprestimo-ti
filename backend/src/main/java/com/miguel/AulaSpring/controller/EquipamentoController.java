package com.miguel.AulaSpring.controller;

import com.miguel.AulaSpring.dto.EquipamentoDTO;
import com.miguel.AulaSpring.entity.Equipamento;
import com.miguel.AulaSpring.entity.enums.StatusEquipamento;
import com.miguel.AulaSpring.service.EquipamentoService;
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
@RequestMapping("/equipamentos")
public class EquipamentoController {

    private final EquipamentoService service;

    public EquipamentoController(EquipamentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Equipamento> Listar(){
        return service.Listar();
    }

    @PostMapping
    public Equipamento salvar(@RequestBody @Valid EquipamentoDTO eqdto){
        return service.salvar(eqdto);
    }

    @GetMapping("/{id}")
    public Equipamento buscarPorId(@PathVariable Long id){
        return service.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void deletarPorID(@PathVariable Long id){
        service.deletar(id);
    }

    @PutMapping("/{id}")
    public Equipamento atualizar(@PathVariable Long id, @RequestBody EquipamentoDTO dto) {
        return service.atualizar(id, dto);
    }

    @GetMapping("/filtro")
    public List<Equipamento> filtrar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) StatusEquipamento status
    ) {
        return service.filtrar(nome, status);
    }
}