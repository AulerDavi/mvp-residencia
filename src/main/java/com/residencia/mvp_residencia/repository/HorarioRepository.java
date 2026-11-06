package com.residencia.mvp_residencia.repository;

import com.residencia.mvp_residencia.model.Horario;
import com.residencia.mvp_residencia.model.TipoDia;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {

    List<Horario> findByLinhaIdAndParadaIdOrderByHoraPartida(Long linhaId, Long paradaId);

    List<Horario> findByLinhaIdAndParadaIdAndTipoDiaOrderByHoraPartida(
            Long linhaId, Long paradaId, TipoDia tipoDia);

    List<Horario> findByLinhaIdAndAdaptadoDeficienteTrue(Long linhaId);
}
