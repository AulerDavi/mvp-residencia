package com.residencia.mvp_residencia.dto;

import com.residencia.mvp_residencia.model.StatusVeiculo;
import java.time.LocalDateTime;

public record ServicoVeiculoResponse(
    Long linhaId,
    String veiculo,
    StatusVeiculo status,
    String descricao,
    LocalDateTime consultadoEm
) {}
