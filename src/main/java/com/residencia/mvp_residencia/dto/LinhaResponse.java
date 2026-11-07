package com.residencia.mvp_residencia.dto;

import com.residencia.mvp_residencia.model.Linha;

public record LinhaResponse(Long id, String nome, String tipo, String sentido, boolean ativa) {

    public static LinhaResponse de(Linha linha) {
        return new LinhaResponse(
                linha.getId(), linha.getNome(), linha.getTipo(), linha.getSentido(), linha.isAtiva());
    }
}
