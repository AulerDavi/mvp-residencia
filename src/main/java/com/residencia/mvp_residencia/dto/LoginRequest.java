package com.residencia.mvp_residencia.dto;

public record LoginRequest(String email, String senha) {
}
//(o que o usuário envia no POST /login'