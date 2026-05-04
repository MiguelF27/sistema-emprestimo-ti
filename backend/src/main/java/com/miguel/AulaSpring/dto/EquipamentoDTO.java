package com.miguel.AulaSpring.dto;

import com.miguel.AulaSpring.entity.enums.StatusEquipamento;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipamentoDTO {

    @NotBlank
    private String patrimonio;

    @NotBlank
    private String nome;

    @NotBlank
    private String tipo;

    @NotBlank
    private String categoria;

    private StatusEquipamento status;
}
