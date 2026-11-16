package com.residencia.mvp_residencia.model;

public enum StatusVeiculo {
    SAIU_DO_PONTO("Saiu do ponto"),
    A_CAMINHO("A caminho"),
    CHEGANDO("Chegando");

    private final String descricao;

    StatusVeiculo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public StatusVeiculo proximo() {
        switch (this) {
            case SAIU_DO_PONTO: return A_CAMINHO;
            case A_CAMINHO: return CHEGANDO;
            default: return SAIU_DO_PONTO;
        }
    }
}
