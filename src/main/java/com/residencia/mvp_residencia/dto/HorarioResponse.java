package com.residencia.mvp_residencia.dto;

import com.residencia.mvp_residencia.model.Horario;
import com.residencia.mvp_residencia.model.TipoDia;
import java.time.format.DateTimeFormatter;

public record HorarioResponse(Long id, String horaPartida, TipoDia tipoDia, boolean adaptadoDeficiente) {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    public static HorarioResponse de(Horario horario) {
        return new HorarioResponse(
                horario.getId(),
                horario.getHoraPartida().format(FORMATO_HORA),
                horario.getTipoDia(),
                horario.isAdaptadoDeficiente());
    }
}
