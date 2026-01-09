package com.packge.manager.tosam.br.libraryApi.security;

import com.packge.manager.tosam.br.libraryApi.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomAuthentication implements Authentication {

    private final Usuario usuario;
    private boolean authenticated; // Controle interno

    public CustomAuthentication(Usuario usuario) {
        this.usuario = usuario;
        // 👇 O SEGREDO: Dizemos que se chegou aqui, o login é VÁLIDO!
        this.authenticated = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return usuario.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return usuario;
    }

    @Override
    public Object getPrincipal() {
        return usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return usuario.getUsername();
    }
}