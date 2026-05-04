package com.miguel.AulaSpring.controller;

import com.miguel.AulaSpring.dto.EmprestimoDTO;
import com.miguel.AulaSpring.entity.Emprestimo;
import com.miguel.AulaSpring.entity.enums.StatusEmprestimo;
import com.miguel.AulaSpring.entity.enums.StatusEquipamento;
import com.miguel.AulaSpring.service.EmprestimoService;
import com.miguel.AulaSpring.service.EquipamentoService;
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
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService, UsuarioService usuarioService, EquipamentoService equipamentoService) {
        this.emprestimoService = emprestimoService;

    }

    @GetMapping
    public List<Emprestimo> listar() {
        return emprestimoService.listar();
    }

    @PostMapping
    public Emprestimo emprestar(@RequestBody @Valid EmprestimoDTO dto) {
        return emprestimoService.emprestar(
                dto.getUsuarioId(),
                dto.getEquipamentoId(),
                dto.getDataDevolucaoPrevista()
        );
    }

    @PutMapping("/devolver/{id}")
    public void devolver(@PathVariable Long id, @RequestParam StatusEquipamento status){
        emprestimoService.devolver(id, status);
    }

    @GetMapping("/{id}")
    public Emprestimo buscarPorId(@PathVariable Long id){
        return emprestimoService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void deletarPorID(@PathVariable Long id){
        emprestimoService.deletar(id);
    }

    @GetMapping("/filtro")
    public List<Emprestimo> filtrar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) StatusEmprestimo status
    ) {
        return emprestimoService.filtrar(nome, status);
    }
}
