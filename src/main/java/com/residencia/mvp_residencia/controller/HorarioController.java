package com.residencia.mvp_residencia.controller;

import com.residencia.mvp_residencia.dto.HorariosParadaResponse;
import com.residencia.mvp_residencia.model.TipoDia;
import com.residencia.mvp_residencia.service.HorarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/horarios")
@RequiredArgsConstructor
public class HorarioController {

    private final HorarioService service;

    @GetMapping
    public HorariosParadaResponse buscar(
            @RequestParam Long linhaId,
            @RequestParam Long paradaId,
            @RequestParam(required = false) TipoDia tipoDia,
            @RequestParam(defaultValue = "false") boolean adaptado) {
        return service.buscarPorLinhaEParada(linhaId, paradaId, tipoDia, adaptado);
    }
}
