package com.miguel.AulaSpring.service;

import com.miguel.AulaSpring.entity.Emprestimo;
import com.miguel.AulaSpring.entity.Equipamento;
import com.miguel.AulaSpring.entity.Usuario;
import com.miguel.AulaSpring.entity.enums.StatusEmprestimo;
import com.miguel.AulaSpring.entity.enums.StatusEquipamento;
import com.miguel.AulaSpring.exception.BusinessException;
import com.miguel.AulaSpring.exception.ResourceNotFoundException;
import com.miguel.AulaSpring.repository.EmprestimoRepository;
import com.miguel.AulaSpring.repository.EquipamentoRepository;
import com.miguel.AulaSpring.repository.UsuarioRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Setter
@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EquipamentoRepository equipamentoRepository;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, UsuarioRepository usuarioRepository, EquipamentoRepository equipamentoRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.usuarioRepository = usuarioRepository;
        this.equipamentoRepository = equipamentoRepository;
    }

    public List<Emprestimo> listar() {
        return emprestimoRepository.findAll();
    }

    public Emprestimo emprestar(Long usuarioId, Long equipamentoId, LocalDate dataPrevista) {

        Equipamento equipamento = equipamentoRepository.findById(equipamentoId).orElseThrow(() -> new ResourceNotFoundException("Equipamento não encontrado"));

        if (equipamento.getStatus() == StatusEquipamento.EM_USO) {
            throw new BusinessException("Equipamento não disponível");
        }

        if (equipamento.getStatus() == StatusEquipamento.MANUTENCAO) {
            throw new BusinessException("Equipamento em manutenção");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Emprestimo emp = new Emprestimo();
        emp.setUsuario(usuario);
        emp.setEquipamento(equipamento);
        emp.setDataDevolucaoPrevista(dataPrevista);

        equipamento.setStatus(StatusEquipamento.EM_USO);

        emp.setStatus(StatusEmprestimo.ATIVO);

        equipamentoRepository.save(equipamento);

        return emprestimoRepository.save(emp);

    }

    public void devolver(Long emprestimoId, StatusEquipamento proximoStatus) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new ResourceNotFoundException("Emprestimo não encontrado"));

        Equipamento eq = emprestimo.getEquipamento();
        eq.setStatus(proximoStatus);
        equipamentoRepository.save(eq);

        LocalDateTime agora = LocalDateTime.now();
        emprestimo.setDataDevolucaoReal(agora);

        LocalDate dataHoje = agora.toLocalDate();
        LocalDate dataPrevista = emprestimo.getDataDevolucaoPrevista();

        if (dataHoje.isAfter(dataPrevista)) {
            long diasAtraso = ChronoUnit.DAYS.between(dataPrevista, dataHoje);
            emprestimo.setDiasAtraso(diasAtraso);
            emprestimo.setStatus(StatusEmprestimo.FINALIZADO);
        } else {
            emprestimo.setDiasAtraso(0L);
            emprestimo.setStatus(StatusEmprestimo.FINALIZADO);
        }

        emprestimoRepository.save(emprestimo);
    }


    public Emprestimo buscarPorId(Long id){
        return emprestimoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Emprestimo não encontrado"));
    }
    public void deletar(Long id) {
        emprestimoRepository.deleteById(id);
    }

    public List<Emprestimo> filtrar(String nome, StatusEmprestimo status) {
        return emprestimoRepository.filtrar(nome, status);
    }

}