package com.residencia.mvp_residencia.repository;

import com.residencia.mvp_residencia.model.Linha;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface LinhaRepository extends JpaRepository<Linha, Long> {

    List<Linha> findByTipoIgnoreCase(String tipo);

    Page<Linha> findByTipoIgnoreCase(String tipo, Pageable pageable);

    List<Linha> findByAtivaTrue();

    List<Linha> findByNomeContainingIgnoreCase(String nome);
}
