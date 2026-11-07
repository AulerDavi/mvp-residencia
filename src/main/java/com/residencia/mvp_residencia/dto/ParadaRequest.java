package com.residencia.mvp_residencia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParadaRequest(
        @NotBlank(message = "O nome é obrigatório") String nome,
        String tipo,
        @NotNull(message = "A linha é obrigatória") Long linhaId) {
}
