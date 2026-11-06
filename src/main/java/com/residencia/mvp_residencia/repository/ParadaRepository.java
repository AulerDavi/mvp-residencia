package com.residencia.mvp_residencia.repository;

import com.residencia.mvp_residencia.model.Parada;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParadaRepository extends JpaRepository<Parada, Long> {

    List<Parada> findByLinhaId(Long linhaId);
}
