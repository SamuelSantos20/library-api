package com.packge.manager.tosam.br.libraryApi.security;

import com.packge.manager.tosam.br.libraryApi.model.Usuario;
import com.packge.manager.tosam.br.libraryApi.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginSocialSuccesHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UsuarioService usuarioService;

    private  static final String SENHA_PADRAO = "1234";

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();

        String email = oAuth2User.getAttribute("email");

        Optional<Usuario> usuarioEncontrado = usuarioService.obterPorEmail(email);

        if (usuarioEncontrado.isEmpty()) {

            usuarioEncontrado = Optional.of(obterNovoUsuario(email));

        }

        Usuario usuario = usuarioEncontrado.get();

        CustomAuthentication customAuthentication = new CustomAuthentication(usuario);

        SecurityContextHolder.getContext().setAuthentication(customAuthentication);

        super.onAuthenticationSuccess(request  , response , customAuthentication);

     
    }

    private Usuario obterNovoUsuario(String email) {

            Usuario usuario = new Usuario();

            usuario.setPassword(SENHA_PADRAO);
            usuario.setRoles(List.of("OPERADOR"));
            usuario.setEmail(email);
            usuario.setUsername(ObterUserName(email));

            usuarioService.salvar(usuario);


            return usuario;

    }

    private String ObterUserName(String email) {

        return email.substring(0 , email.indexOf("@"));

    }
}
