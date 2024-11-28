package com.packge.manager.tosam.br.libraryApi.service;

import com.packge.manager.tosam.br.libraryApi.model.Usuario;
import com.packge.manager.tosam.br.libraryApi.repository.UsuarioRepository;
import com.packge.manager.tosam.br.libraryApi.validator.UsuarioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final UsuarioValidator usuarioValidator;

    public void salvar(Usuario usuario) {

        usuarioValidator.validar(usuario);

        var senha = usuario.getPassword();

        usuario.setPassword(passwordEncoder.encode(senha));

        repository.save(usuario);


    }

    public Optional<Usuario> obterPorLogin(String username) {

        return repository.findByUsername(username);

    }

    public Optional<Usuario> obterPorEmail(String email){

       return repository.findByEmail(email);
    }



}
