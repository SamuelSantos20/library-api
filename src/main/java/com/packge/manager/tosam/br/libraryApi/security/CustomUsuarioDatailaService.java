package com.packge.manager.tosam.br.libraryApi.security;

import com.packge.manager.tosam.br.libraryApi.model.Usuario;
import com.packge.manager.tosam.br.libraryApi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomUsuarioDatailaService implements UserDetailsService {

    private final UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Usuario> usuario = usuarioService.obterPorLogin(username);

        if (usuario.isEmpty()) {

            throw new UsernameNotFoundException("Usuario não encontrado!");
        }


        return User.builder()
                .username(usuario.get().getUsername())
                .password(usuario.get().getPassword())
                .roles(usuario.get().getRoles()
                        .toArray(new String[usuario.get().getRoles().size()])).build();
    }
}
