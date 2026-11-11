package com.residencia.mvp_residencia.controller;

import com.residencia.mvp_residencia.model.Usuario;
import com.residencia.mvp_residencia.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    public record CadastroRequest(String nome, String email, String senha) {
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String cadastrar(@RequestBody CadastroRequest request) {
        Usuario usuario = service.cadastrar(request.nome(), request.email(), request.senha());
        return "Usuário " + usuario.getNome() + " cadastrado com id " + usuario.getId();
    }
}
