package com.packge.manager.tosam.br.libraryApi.validator;

import com.packge.manager.tosam.br.libraryApi.exceptions.RegistroDuplicadoExeption;
import com.packge.manager.tosam.br.libraryApi.model.Autor;
import com.packge.manager.tosam.br.libraryApi.repository.AutorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class AutorValidator {

    private AutorRepository autorRepository;

    public void validar(Autor autor) {

        if (existeAutorCadastardo(autor)) {

            throw new RegistroDuplicadoExeption("Autor já Cadastrado!");
        }


    }

    private boolean existeAutorCadastardo(Autor autor) {

        Optional<Autor> autorEncontrado = autorRepository.findByNomeAndDataNascimentoAndNacionalidade(autor.getNome(), autor.getDataNascimento(), autor.getNacionalidade());

        if (autor.getId() == null) {
            return autorEncontrado.isPresent();
        }

        return !autor.getId().equals(autorEncontrado.get().getId()) && autorEncontrado.isPresent();


    }

}
