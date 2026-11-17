package com.residencia.mvp_residencia.service;

import com.residencia.mvp_residencia.dto.RotaFavoritaRequest;
import com.residencia.mvp_residencia.dto.RotaFavoritaResponse;
import com.residencia.mvp_residencia.model.Linha;
import com.residencia.mvp_residencia.model.Parada;
import com.residencia.mvp_residencia.model.RotaFavorita;
import com.residencia.mvp_residencia.model.Usuario;
import com.residencia.mvp_residencia.repository.LinhaRepository;
import com.residencia.mvp_residencia.repository.ParadaRepository;
import com.residencia.mvp_residencia.repository.RotaFavoritaRepository;
import com.residencia.mvp_residencia.repository.UsuarioRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RotaFavoritaService {

    private final RotaFavoritaRepository rotaFavoritaRepository;
    private final UsuarioRepository usuarioRepository;
    private final LinhaRepository linhaRepository;
    private final ParadaRepository paradaRepository;

    public RotaFavoritaService(
            RotaFavoritaRepository rotaFavoritaRepository,
            UsuarioRepository usuarioRepository,
            LinhaRepository linhaRepository,
            ParadaRepository paradaRepository) {
        this.rotaFavoritaRepository = rotaFavoritaRepository;
        this.usuarioRepository = usuarioRepository;
        this.linhaRepository = linhaRepository;
        this.paradaRepository = paradaRepository;
    }

    @Transactional
    public RotaFavoritaResponse salvar(String emailUsuario, RotaFavoritaRequest request) {
        if (request.linhaId() == null || request.paradaId() == null) {
            throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "linhaId e paradaId são obrigatórios");
        }
        
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);

        Linha linha = linhaRepository.findById(request.linhaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Linha não encontrada"));

        Parada parada = paradaRepository.findById(request.paradaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Parada não encontrada"));

        if (!parada.getLinha().getId().equals(linha.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "A parada informada não pertence a esta linha");
        }

        if (rotaFavoritaRepository.existsByUsuarioIdAndLinhaIdAndParadaId(
                usuario.getId(), linha.getId(), parada.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Esta rota já está salva nos seus favoritos");
        }

        RotaFavorita novaFavorita = new RotaFavorita();
        novaFavorita.setUsuario(usuario);
        novaFavorita.setLinha(linha);
        novaFavorita.setParada(parada);

        RotaFavorita salva = rotaFavoritaRepository.save(novaFavorita);
        return RotaFavoritaResponse.fromEntity(salva);
    }

    @Transactional(readOnly = true)
    public List<RotaFavoritaResponse> listarDoUsuario(String emailUsuario) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);

        return rotaFavoritaRepository.findByUsuarioId(usuario.getId())
                .stream()
                .map(RotaFavoritaResponse::fromEntity)
                .toList();
    }

    @Transactional
    public void remover(String emailUsuario, Long favoritoId) {
        Usuario usuario = buscarUsuarioPorEmail(emailUsuario);

        RotaFavorita favorita = rotaFavoritaRepository.findByIdAndUsuarioId(favoritoId, usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Rota favorita não encontrada"));

        rotaFavoritaRepository.delete(favorita);
    }

    private Usuario buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));
    }
}

//Identificação segura: Recebe o e-mail que o Spring extraiu do token JWT (emailUsuario) e busca o usuário correspondente no banco.'

//Consistência dos dados: Confere se a Linha e a Parada existem, se a parada realmente pertence àquela linha e bloqueia com erro 409 Conflict caso o usuário tente salvar o mesmo favorito duas vezes.'

//Isolamento por usuário: Tanto na listagem (listarDoUsuario) quanto na exclusão (remover), cada usuário só enxerga e só apaga os próprios favoritos.'

//O método remover busca o favorito cruzando o ID da rota favorita com o ID do usuário logado. Se o favorito não existir ou pertencer a outra pessoa, ele barra com 404 Not Found, impedindo que um usuário apague o favorito de outro.'