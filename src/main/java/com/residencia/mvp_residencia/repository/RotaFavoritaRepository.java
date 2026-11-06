package com.residencia.mvp_residencia.repository;

import com.residencia.mvp_residencia.model.RotaFavorita;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RotaFavoritaRepository extends JpaRepository<RotaFavorita, Long> {

    List<RotaFavorita> findByUsuarioId(Long usuarioId);

    List<RotaFavorita> findByLinhaId(Long linhaId);
}
