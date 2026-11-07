package com.residencia.mvp_residencia.service;

import com.residencia.mvp_residencia.dto.HorarioResponse;
import com.residencia.mvp_residencia.dto.HorariosParadaResponse;
import com.residencia.mvp_residencia.exception.RecursoNaoEncontradoException;
import com.residencia.mvp_residencia.model.Horario;
import com.residencia.mvp_residencia.model.Linha;
import com.residencia.mvp_residencia.model.Parada;
import com.residencia.mvp_residencia.model.TipoDia;
import com.residencia.mvp_residencia.repository.HorarioRepository;
import com.residencia.mvp_residencia.repository.ParadaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HorarioService {

    private final HorarioRepository horarioRepository;
    private final ParadaRepository paradaRepository;
    private final LinhaService linhaService;

    public HorariosParadaResponse buscarPorLinhaEParada(
            Long linhaId, Long paradaId, TipoDia tipoDia, boolean somenteAdaptados) {

        Linha linha = linhaService.buscarEntidade(linhaId);
        Parada parada = paradaRepository.findById(paradaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Parada " + paradaId + " não encontrada"));

        List<Horario> horarios = (tipoDia == null)
                ? horarioRepository.findByLinhaIdAndParadaIdOrderByHoraPartida(linhaId, paradaId)
                : horarioRepository.findByLinhaIdAndParadaIdAndTipoDiaOrderByHoraPartida(
                        linhaId, paradaId, tipoDia);

        List<HorarioResponse> resposta = horarios.stream()
                .filter(h -> !somenteAdaptados || h.isAdaptadoDeficiente())
                .map(HorarioResponse::de)
                .toList();

        return new HorariosParadaResponse(
                linha.getId(), linha.getNome(), linha.getSentido(),
                parada.getId(), parada.getNome(), resposta);
    }
}
