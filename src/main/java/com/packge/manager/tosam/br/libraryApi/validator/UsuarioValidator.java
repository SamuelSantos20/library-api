package com.packge.manager.tosam.br.libraryApi.validator;

import com.packge.manager.tosam.br.libraryApi.exceptions.RegistroDuplicadoExeption;
import com.packge.manager.tosam.br.libraryApi.model.Usuario;
import com.packge.manager.tosam.br.libraryApi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioValidator {

    private final UsuarioRepository usuarioRepository;

    public void validar(Usuario usuario) {

        if (existeUsuario(usuario)) {

            throw new RegistroDuplicadoExeption("Usuario já cadastrado!");

        }

    }

    private boolean existeUsuario(Usuario usuarioParam) {
        Optional<Usuario> usuario = usuarioRepository.findByUsername(usuarioParam.getUsername());
        if (usuarioParam.getId() == null) {

            return usuario.isPresent();

        }

        return !usuarioParam.getId().equals(usuario.get().getId()) && usuario.isPresent();


    }


}
