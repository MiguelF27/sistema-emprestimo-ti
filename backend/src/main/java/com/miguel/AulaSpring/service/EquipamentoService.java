package com.miguel.AulaSpring.service;

import com.miguel.AulaSpring.dto.EquipamentoDTO;
import com.miguel.AulaSpring.entity.Equipamento;
import com.miguel.AulaSpring.entity.enums.StatusEquipamento;
import com.miguel.AulaSpring.exception.ResourceNotFoundException;
import com.miguel.AulaSpring.repository.EquipamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipamentoService {

    private final EquipamentoRepository repository;

    public EquipamentoService(EquipamentoRepository repository) {
        this.repository = repository;
    }
    public Equipamento salvar(EquipamentoDTO eqdto){

        Equipamento equipamento = new Equipamento();

        equipamento.setPatrimonio(eqdto.getPatrimonio());
        equipamento.setNome(eqdto.getNome());
        equipamento.setTipo(eqdto.getTipo());
        equipamento.setCategoria(eqdto.getCategoria());

        return repository.save(equipamento);
    }
    public Equipamento atualizar(Long id, EquipamentoDTO dto) {

        Equipamento equipamento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipamento não encontrado"));

        equipamento.setNome(dto.getNome());
        equipamento.setTipo(dto.getTipo());
        equipamento.setCategoria(dto.getCategoria());
        equipamento.setPatrimonio(dto.getPatrimonio());
        equipamento.setStatus(dto.getStatus());

        return repository.save(equipamento);
    }
    public List<Equipamento> Listar(){
        return repository.findAll();
    }

    public Equipamento buscarPorId(Long id){
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Equipamento não encontrado"));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public List<Equipamento> filtrar(String nome, StatusEquipamento status) {
        return repository.filtrar(nome, status);
    }
}