package com.residencia.mvp_residencia.repository;

import com.residencia.mvp_residencia.model.Notificacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByUsuarioIdOrderByCriadaEmDesc(Long usuarioId);

    List<Notificacao> findByUsuarioIdAndLida(Long usuarioId, boolean lida);
}
