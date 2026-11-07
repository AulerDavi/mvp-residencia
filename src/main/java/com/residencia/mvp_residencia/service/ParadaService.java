package com.residencia.mvp_residencia.service;

import com.residencia.mvp_residencia.dto.ParadaRequest;
import com.residencia.mvp_residencia.dto.ParadaResponse;
import com.residencia.mvp_residencia.exception.ConflitoException;
import com.residencia.mvp_residencia.exception.RecursoNaoEncontradoException;
import com.residencia.mvp_residencia.model.Parada;
import com.residencia.mvp_residencia.repository.ParadaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParadaService {

    private final ParadaRepository paradaRepository;
    private final LinhaService linhaService;

    public List<ParadaResponse> listar(Long linhaId, Pageable pageable) {
        Page<Parada> pagina = (linhaId == null)
                ? paradaRepository.findAll(pageable)
                : paradaRepository.findByLinhaId(linhaId, pageable);
        return pagina.map(ParadaResponse::de).getContent();
    }

    public ParadaResponse buscarPorId(Long id) {
        return ParadaResponse.de(buscarEntidade(id));
    }

    @Transactional
    public ParadaResponse criar(ParadaRequest request) {
        Parada parada = new Parada();
        preencher(parada, request);
        return ParadaResponse.de(paradaRepository.save(parada));
    }

    @Transactional
    public ParadaResponse atualizar(Long id, ParadaRequest request) {
        Parada parada = buscarEntidade(id);
        preencher(parada, request);
        return ParadaResponse.de(paradaRepository.save(parada));
    }

    @Transactional
    public void deletar(Long id) {
        Parada parada = buscarEntidade(id);
        try {
            paradaRepository.delete(parada);
            paradaRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ConflitoException("Não é possível excluir a parada " + id
                    + ": existem horários ou favoritas ligados a ela.");
        }
    }

    private Parada buscarEntidade(Long id) {
        return paradaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Parada " + id + " não encontrada"));
    }

    private void preencher(Parada parada, ParadaRequest request) {
        parada.setNome(request.nome());
        parada.setTipo(request.tipo());
        parada.setLinha(linhaService.buscarEntidade(request.linhaId()));
    }
}
