package com.miguel.AulaSpring.entity;

import com.miguel.AulaSpring.entity.enums.StatusEquipamento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String patrimonio;

    @NotNull
    @Column(nullable = false)
    private String nome;

    private String tipo;

    private String categoria;

    @Enumerated(EnumType.STRING)
    private StatusEquipamento status = StatusEquipamento.DISPONIVEL;
}