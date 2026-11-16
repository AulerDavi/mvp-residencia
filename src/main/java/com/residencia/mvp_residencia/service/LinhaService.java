package com.residencia.mvp_residencia.service;

import com.residencia.mvp_residencia.dto.LinhaRequest;
import com.residencia.mvp_residencia.dto.LinhaResponse;
import com.residencia.mvp_residencia.exception.ConflitoException;
import com.residencia.mvp_residencia.exception.RecursoNaoEncontradoException;
import com.residencia.mvp_residencia.model.Linha;
import com.residencia.mvp_residencia.repository.LinhaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import com.residencia.mvp_residencia.dto.ServicoVeiculoResponse;
import com.residencia.mvp_residencia.model.Veiculos;
import com.residencia.mvp_residencia.simulador.SimuladorVeiculoService;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinhaService {

    private final LinhaRepository linhaRepository;

    public List<LinhaResponse> listar(String tipo, Pageable pageable) {
        Page<Linha> pagina = (tipo == null || tipo.isBlank())
                ? linhaRepository.findAll(pageable)
                : linhaRepository.findByTipoIgnoreCase(tipo.trim(), pageable);
        return pagina.map(LinhaResponse::de).getContent();
    }

    public LinhaResponse buscarPorId(Long id) {
        return LinhaResponse.de(buscarEntidade(id));
    }

    @Transactional
    public LinhaResponse criar(LinhaRequest request) {
        Linha linha = new Linha();
        preencher(linha, request);
        return LinhaResponse.de(linhaRepository.save(linha));
    }

    @Transactional
    public LinhaResponse atualizar(Long id, LinhaRequest request) {
        Linha linha = buscarEntidade(id);
        preencher(linha, request);
        return LinhaResponse.de(linhaRepository.save(linha));
    }

    @Transactional
    public void deletar(Long id) {
        Linha linha = buscarEntidade(id);
        try {
            linhaRepository.delete(linha);
            linhaRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ConflitoException("Não é possível excluir a linha " + id
                    + ": existem paradas, horários, veículos ou favoritas ligados a ela.");
        }
    }

    public Linha buscarEntidade(Long id) {
        return linhaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Linha " + id + " não encontrada"));
    }

    private void preencher(Linha linha, LinhaRequest request) {
        linha.setNome(request.nome());
        linha.setTipo(request.tipo());
        linha.setSentido(request.sentido());
        linha.setAtiva(request.ativa() == null || request.ativa());
    }
private final SimuladorVeiculoService simuladorService;
public ServicoVeiculoResponse buscarStatus(Long id) {
    Veiculos veiculo = simuladorService.buscarPorId(String.valueOf(id))
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Status em tempo real não encontrado para a linha " + id
            ));

    return new ServicoVeiculoResponse(
        id,
        veiculo.getNome(),
        veiculo.getStatus(),
        veiculo.getStatus().getDescricao(),
        LocalDateTime.now()
    );
}
}
