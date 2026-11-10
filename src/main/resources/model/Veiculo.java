package com.residencia.mvp_residencia.model;

public class Veiculo {
    private String id;
    private String nome;
    private StatusVeiculo status;

    public Veiculo(String id, String nome, StatusVeiculo status) {
        this.id = id;
        this.nome = nome;
        this.status = status;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public StatusVeiculo getStatus() { return status; }
    public void setStatus(StatusVeiculo status) { this.status = status; }
}
