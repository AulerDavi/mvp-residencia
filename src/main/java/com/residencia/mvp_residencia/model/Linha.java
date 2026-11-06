package com.residencia.mvp_residencia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "linha")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Linha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_linha")
    private Long id;

    @Column(nullable = false)
    private String nome;

    /** Categoria da linha: expressa, circular, alimentadora, madrugadão... */
    @Column(nullable = false)
    private String tipo;

    private String sentido;

    @Column(nullable = false)
    private boolean ativa = true;
}
