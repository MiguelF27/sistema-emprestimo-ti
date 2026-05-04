package com.miguel.AulaSpring.entity;

import com.miguel.AulaSpring.entity.enums.StatusEmprestimo;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "equipamento_id")
    private Equipamento equipamento;

    @CreationTimestamp
    private LocalDateTime dataEmprestimo;

    private LocalDate dataDevolucaoPrevista;

    private LocalDateTime dataDevolucaoReal;

    @Enumerated(EnumType.STRING)
    private StatusEmprestimo status = StatusEmprestimo.ATIVO;

    private long diasAtraso;
}