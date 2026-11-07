package com.residencia.mvp_residencia.dto;

import jakarta.validation.constraints.NotBlank;

public record LinhaRequest(
        @NotBlank(message = "O nome é obrigatório") String nome,
        @NotBlank(message = "O tipo é obrigatório") String tipo,
        String sentido,
        Boolean ativa) {
}
