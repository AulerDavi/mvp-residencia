package com.residencia.mvp_residencia.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Indica ao Spring que esta é uma classe de configuração e ativa a segurança web na aplicação
@Configuration
@EnableWebSecurity
public class SegurancaConfig {

    // Lê a chave secreta definida no application.properties para assinar e validar os tokens JWT
    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * Filtro principal de segurança: define o comportamento da API, quais rotas são públicas
     * e quais exigem um token JWT válido para serem acessadas.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desativa a proteção CSRF (padrão para APIs REST que usam token no cabeçalho em vez de cookies)
            .csrf(csrf -> csrf.disable())

            // Permite que o navegador abra frames da própria aplicação (necessário para o console do banco H2 funcionar)
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))

            // Define a API como Stateless: o servidor não guarda sessão do usuário; cada requisição precisa enviar o token
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Configura as permissões de acesso por rota e método HTTP
            .authorizeHttpRequests(auth -> auth
                // Libera o cadastro de usuário e o login para qualquer pessoa sem token
                .requestMatchers(HttpMethod.POST, "/usuarios", "/login").permitAll()
                // Libera apenas a consulta (GET) de linhas, paradas e horários
                .requestMatchers(HttpMethod.GET, "/linhas/**", "/paradas/**", "/horarios/**").permitAll()
                // Libera o acesso ao painel do banco H2 em ambiente de desenvolvimento
                .requestMatchers("/h2-console/**").permitAll()
                // Bloqueia qualquer outra requisição (como POST, PUT, DELETE e /rotas-favoritas), exigindo login
                .anyRequest().authenticated()
            )

            // Ativa o suporte nativo do Spring para ler e validar o token JWT enviado no cabeçalho Authorization
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

    /**
     * Disponibiliza o algoritmo BCrypt para criptografar senhas no cadastro e conferi-las no login.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cria o gerador de tokens (usado no AuthService) para assinar o JWT com a chave secreta da API.
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        SecretKey key = new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
        return new NimbusJwtEncoder(new ImmutableSecret<>(key));
    }

    /**
     * Cria o leitor de tokens (usado automaticamente pelo Spring Security) para verificar se o JWT recebido é autêntico e não expirou.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey key = new SecretKeySpec(jwtSecret.getBytes(), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }
}