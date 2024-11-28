package com.packge.manager.tosam.br.libraryApi.security;

import com.packge.manager.tosam.br.libraryApi.model.Usuario;
import com.packge.manager.tosam.br.libraryApi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UsuarioService usuarioService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = authentication.getName();

        var password = (String) authentication.getCredentials();

        Optional<Usuario> usuarioEncontrado = usuarioService.obterPorLogin(username);

        if (usuarioEncontrado.isEmpty()){

            throw ErroUsuarioNãoEncontradoException();

        }

        String senhaCriptografada = usuarioEncontrado.get().getPassword();


        boolean isenhavalida = passwordEncoder.matches(password , senhaCriptografada);

        if(isenhavalida){

            Usuario usuario = usuarioEncontrado.get();

          return new CustomAuthentication(usuario);
        }

        throw  ErroUsuarioNãoEncontradoException();


    }

    private  UsernameNotFoundException ErroUsuarioNãoEncontradoException() {
        return new UsernameNotFoundException("Usuário e/ou senha incorretos!");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
