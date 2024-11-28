package com.packge.manager.tosam.br.libraryApi.validator;

import com.packge.manager.tosam.br.libraryApi.exceptions.RegistroDuplicadoExeption;
import com.packge.manager.tosam.br.libraryApi.exceptions.RegradeNegocioException;
import com.packge.manager.tosam.br.libraryApi.model.Livro;
import com.packge.manager.tosam.br.libraryApi.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LivroValidator {

    private static final int ANO_PUBLICACAO_EXIGENCIA_PRECO=2020;

    private final LivroRepository livroRepository;



    public  void validar(Livro livro) {

        if (existeLivroComIsbn(livro)) {
            throw new RegistroDuplicadoExeption("ISBN já Cadastrado!");
        }

        if (isPrecoObrigatorioNulo(livro)){

            throw  new RegradeNegocioException("Para livros com o ano de publicção de 2020 em diante, o campo preco é obrigatório! " , "preco");
        }




    }

    private boolean isPrecoObrigatorioNulo(Livro livro) {
        return livro.getPreco() == null && livro.getDataPublicacao().getYear()>=ANO_PUBLICACAO_EXIGENCIA_PRECO;
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
