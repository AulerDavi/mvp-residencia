package com.residencia.mvp_residencia.controller;

import com.residencia.mvp_residencia.model.Usuario;
import com.residencia.mvp_residencia.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;
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

    public record CadastroRequest(
            @NotBlank(message = "O nome é obrigatório") String nome,
            @NotBlank(message = "O e-mail é obrigatório")
            @Email(message = "E-mail inválido") String email,
            @NotBlank(message = "A senha é obrigatória")
            @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres") String senha) {
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> cadastrar(@Valid @RequestBody CadastroRequest request) {
        Usuario usuario = service.cadastrar(request.nome(), request.email(), request.senha());
        return Map.of("id", usuario.getId(), "nome", usuario.getNome());
    }
}
