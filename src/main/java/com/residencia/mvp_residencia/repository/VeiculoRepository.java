package com.residencia.mvp_residencia.repository;

import com.residencia.mvp_residencia.model.Veiculo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    List<Veiculo> findByLinhaId(Long linhaId);

    boolean existsByPlaca(String placa);
}
