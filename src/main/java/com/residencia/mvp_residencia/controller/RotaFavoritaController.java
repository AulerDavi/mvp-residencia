package com.residencia.mvp_residencia.controller;

import com.residencia.mvp_residencia.dto.RotaFavoritaRequest;
import com.residencia.mvp_residencia.dto.RotaFavoritaResponse;
import com.residencia.mvp_residencia.service.RotaFavoritaService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rotas-favoritas")
public class RotaFavoritaController {

    private final RotaFavoritaService rotaFavoritaService;

    public RotaFavoritaController(RotaFavoritaService rotaFavoritaService) {
        this.rotaFavoritaService = rotaFavoritaService;
    }

    @PostMapping
    public ResponseEntity<RotaFavoritaResponse> salvar(
            @RequestBody RotaFavoritaRequest request,
            Authentication authentication) {
        RotaFavoritaResponse response = rotaFavoritaService.salvar(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<RotaFavoritaResponse>> listar(Authentication authentication) {
        List<RotaFavoritaResponse> favoritas = rotaFavoritaService.listarDoUsuario(authentication.getName());
        return ResponseEntity.ok(favoritas);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long id,
            Authentication authentication) {
        rotaFavoritaService.remover(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}

//Lê o usuário direto do token: O parâmetro Authentication authentication é preenchido automaticamente pelo Spring Security a partir do JWT enviado na chamada. O método authentication.getName() entrega o e-mail que gravamos no token na hora do login.'
//O método @DeleteMapping("/{id}") já expõe DELETE /rotas-favoritas/{id}, pega o e-mail do usuário logado pelo token (authentication.getName()) e devolve 204 No Content quando exclui com sucesso.'