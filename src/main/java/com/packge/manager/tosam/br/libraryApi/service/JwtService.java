package com.packge.manager.tosam.br.libraryApi.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.packge.manager.tosam.br.libraryApi.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtService {

    @Value("${api.security.token.secret:minha_senha_super_secreta_123456}")
    private String secret;

    public String gerarToken(Authentication authentication) {
        try {
            Object principal = authentication.getPrincipal();
            String username;

            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            }
            // 👇 AQUI ESTÁ A CORREÇÃO: Tratamos sua classe específica
            else if (principal instanceof Usuario) {
                username = ((Usuario) principal).getUsername(); // Pega só o "admin"
            }
            else {
                username = principal.toString(); // Fallback
            }

            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("Library-API")
                    .withSubject(username) // Agora vai gravar apenas "admin"
                    .withExpiresAt(dataExpiracao())
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}