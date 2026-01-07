package com.packge.manager.tosam.br.libraryApi.controller;

import com.packge.manager.tosam.br.libraryApi.dto.UsuarioDTO; // Ou crie um LoginDTO simples
import com.packge.manager.tosam.br.libraryApi.security.JwtCustomAuthenticationFilter;
// Importe seu serviço de token se tiver, ou a lógica de geração
import com.packge.manager.tosam.br.libraryApi.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor// Sugestão: separar login de API do login de página
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private  final JwtService jwtService;
    // No método login do AuthController:
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO dadosLogin) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(dadosLogin.login(), dadosLogin.senha());
            Authentication auth = authenticationManager.authenticate(authToken);

            // Chama o método que acabamos de corrigir
            String token = jwtService.gerarToken(auth);

            return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // DTO Auxiliar para receber o JSON
    record LoginDTO(String login, String senha) {
    }

}