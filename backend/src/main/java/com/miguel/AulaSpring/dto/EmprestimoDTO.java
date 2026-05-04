package com.miguel.AulaSpring.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmprestimoDTO {

    @NotNull
    private Long usuarioId;

    @NotNull
    private Long equipamentoId;

    @NotNull
    private LocalDate dataDevolucaoPrevista;

}
