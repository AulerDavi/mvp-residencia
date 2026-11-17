package com.residencia.mvp_residencia.dto;

import com.residencia.mvp_residencia.model.RotaFavorita;

public record RotaFavoritaResponse(
        Long id,
        Long linhaId,
        String nomeLinha,
        String sentidoLinha,
        Long paradaId,
        String nomeParada
) {
    public static RotaFavoritaResponse fromEntity(RotaFavorita favorita) {
        return new RotaFavoritaResponse(
                favorita.getId(),
                favorita.getLinha().getId(),
                favorita.getLinha().getNome(),
                favorita.getLinha().getSentido(),
                favorita.getParada().getId(),
                favorita.getParada().getNome()
        );
    }
}

// RotaFavoritaRequest recebe apenas os IDs da linha e da parada (o ID do usuário vem direto do token JWT).

// RotaFavoritaResponse devolve os dados prontos para exibir na tela (incluindo o id do favorito, útil na hora de excluir) e já traz o método fromEntity para converter a entidade do banco sem repetir código no serviço.