package com.residencia.mvp_residencia.dto;

import java.util.List;

public record HorariosParadaResponse(
        Long linhaId,
        String linhaNome,
        String linhaSentido,
        Long paradaId,
        String paradaNome,
        List<HorarioResponse> horarios) {
}
