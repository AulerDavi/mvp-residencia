package com.residencia.mvp_residencia.repository;

import com.residencia.mvp_residencia.model.RotaFavorita;
import java.util.List;
import java.util.Optional; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RotaFavoritaRepository extends JpaRepository<RotaFavorita, Long> {

    List<RotaFavorita> findByUsuarioId(Long usuarioId);

    List<RotaFavorita> findByLinhaId(Long linhaId);

    boolean existsByUsuarioIdAndLinhaIdAndParadaId(Long usuarioId, Long linhaId, Long paradaId);

    Optional<RotaFavorita> findByIdAndUsuarioId(Long id, Long usuarioId);
}

//existsByUsuarioIdAndLinhaIdAndParadaId: verifica se o usuário já salvou aquela mesma combinação de linha e parada antes.'

//findByIdAndUsuarioId: busca a rota favorita pelo ID dela, mas só se pertencer ao usuário logado.'

//O método findByIdAndUsuarioId(Long id, Long usuarioId) já garante esse filtro no banco.'