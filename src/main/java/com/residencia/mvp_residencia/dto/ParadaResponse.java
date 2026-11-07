package com.residencia.mvp_residencia.dto;

import com.residencia.mvp_residencia.model.Parada;

public record ParadaResponse(Long id, String nome, String tipo, Long linhaId) {

    public static ParadaResponse de(Parada parada) {
        return new ParadaResponse(
                parada.getId(), parada.getNome(), parada.getTipo(), parada.getLinha().getId());
    }
}
