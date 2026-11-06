package com.residencia.mvp_residencia.model;

import jakarta.persistence.*;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "horario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Long id;

    @Column(name = "hora_partida", nullable = false)
    private LocalTime horaPartida;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_dia", nullable = false)
    private TipoDia tipoDia;

    @Column(name = "adaptado_deficiente", nullable = false)
    private boolean adaptadoDeficiente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_linha", nullable = false)
    private Linha linha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_parada", nullable = false)
    private Parada parada;
}
