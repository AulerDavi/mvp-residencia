package com.residencia.mvp_residencia.controller;

import com.residencia.mvp_residencia.dto.LoginRequest;
import com.residencia.mvp_residencia.dto.LoginResponse;
import com.residencia.mvp_residencia.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.autenticar(request);
        return ResponseEntity.ok(response);
    }
}

//Junta o @RequestMapping("/login") com @PostMapping para expor a rota POST /login (a mesma que liberamos no SegurancaConfig).'

//Recebe o JSON com email e senha no @RequestBody, chama o AuthService para validar e devolve 200 OK com o token gerado.'