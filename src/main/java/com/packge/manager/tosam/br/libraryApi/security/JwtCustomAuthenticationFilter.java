package com.packge.manager.tosam.br.libraryApi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.packge.manager.tosam.br.libraryApi.model.Usuario;
import com.packge.manager.tosam.br.libraryApi.service.UsuarioService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtCustomAuthenticationFilter extends OncePerRequestFilter {

    private final UsuarioService usuarioService;
    private String secret = "minha_senha_super_secreta_123456";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = recuperarToken(request);

        // LOG 1: Saber se o filtro foi chamado
        if (token != null) {
            System.out.println(">>> FILTRO: Token encontrado! Tentando validar...");
            try {
                Algorithm algorithm = Algorithm.HMAC256(secret);
                DecodedJWT decodedJWT = JWT.require(algorithm)
                        .withIssuer("Library-API")
                        .build()
                        .verify(token);

                String login = decodedJWT.getSubject();
                // LOG 2: Saber o que tinha dentro do token
                System.out.println(">>> FILTRO: Token Válido! Login no token: " + login);

                Optional<Usuario> usuario = usuarioService.obterPorLogin(login);

                if (usuario.isPresent()) {
                    // LOG 3: Sucesso total
                    System.out.println(">>> FILTRO: Usuário encontrado no banco. Autenticando...");
                    CustomAuthentication authentication = new CustomAuthentication(usuario.get());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    // LOG 4: Problema no Banco de Dados
                    System.out.println(">>> FILTRO ERRO: Usuário '" + login + "' não encontrado no banco de dados!");
                }

            } catch (JWTVerificationException e) {
                // LOG 5: Problema na Assinatura (Senha diferente)
                System.out.println(">>> FILTRO ERRO: Token inválido ou expirado: " + e.getMessage());
            }
        } else {
            // Apenas para debug, descomente se quiser ver todas as requisições
            // System.out.println(">>> FILTRO: Requisição sem token.");
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}