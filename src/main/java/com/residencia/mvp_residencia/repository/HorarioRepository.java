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

//findByLinhaId — filtra apenas os horários da linha informada
//AndParadaId — restringe aos horários daquela parada específica
//AndTipoDia — separa apenas os horários válidos para o tipo de dia passado (ex: dia útil, sábado ou domingo).
//OrderByHoraPartida — devolve a List<Horario> já ordenada cronologicamente, do mais cedo para o mais tarde (ex: 06:15, 06:45, 07:10)