package com.residencia.mvp_residencia.controller;

import com.residencia.mvp_residencia.dto.ParadaRequest;
import com.residencia.mvp_residencia.dto.ParadaResponse;
import com.residencia.mvp_residencia.service.ParadaService;
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
@RequestMapping("/paradas")
@RequiredArgsConstructor
public class ParadaController {

    private final ParadaService service;

    @GetMapping
    public List<ParadaResponse> listar(@RequestParam(required = false) Long linhaId,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return service.listar(linhaId, pageable);
    }

    @GetMapping("/{id}")
    public ParadaResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ParadaResponse> criar(@Valid @RequestBody ParadaRequest request) {
        ParadaResponse criado = service.criar(request);
        URI local = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(local).body(criado);
    }

    @PutMapping("/{id}")
    public ParadaResponse atualizar(@PathVariable Long id, @Valid @RequestBody ParadaRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
