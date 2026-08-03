package com.packge.manager.tosam.br.libraryApi.validator;

import com.packge.manager.tosam.br.libraryApi.exceptions.RegistroDuplicadoExeption;
import com.packge.manager.tosam.br.libraryApi.model.Livro;
import com.packge.manager.tosam.br.libraryApi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LivroValidator {

    private final LivroRepository livroRepository;



    public  void validar(Livro livro) {

        if (existeLivroComIsbn(livro)) {
            throw new RegistroDuplicadoExeption("ISBN já Cadastrado!");
        }
    }

    

    private boolean existeLivroComIsbn(Livro livro){

        Optional<Livro> livroOptional = livroRepository.findByIsbn(livro.getIsbn());

        if (livro.getId() == null){

            return livroOptional.isPresent();
        }

        return livroOptional
                .map(Livro::getId)
                .stream()
                .anyMatch(id -> !id.equals(livro.getId()));

    }

}
