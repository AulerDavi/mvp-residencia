package com.residencia.mvp_residencia.controller;

import com.residencia.mvp_residencia.dto.LinhaRequest;
import com.residencia.mvp_residencia.dto.LinhaResponse;
import com.residencia.mvp_residencia.service.LinhaService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/linhas")
@RequiredArgsConstructor
public class LinhaController {

    private final LinhaService service;

    @GetMapping
    public List<LinhaResponse> listar(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return service.listar(pageable);
    }

    @GetMapping("/{id}")
    public LinhaResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<LinhaResponse> criar(@Valid @RequestBody LinhaRequest request) {
        LinhaResponse criado = service.criar(request);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(local).body(criado);
    }

    @PutMapping("/{id}")
    public LinhaResponse atualizar(@PathVariable Long id, @Valid @RequestBody LinhaRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
