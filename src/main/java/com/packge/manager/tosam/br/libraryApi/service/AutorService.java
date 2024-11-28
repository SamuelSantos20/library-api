package com.packge.manager.tosam.br.libraryApi.service;

import com.packge.manager.tosam.br.libraryApi.exceptions.OperacaoNaoPermitidaException;
import com.packge.manager.tosam.br.libraryApi.model.Autor;
import com.packge.manager.tosam.br.libraryApi.model.Usuario;
import com.packge.manager.tosam.br.libraryApi.repository.AutorRepository;
import com.packge.manager.tosam.br.libraryApi.repository.LivroRepository;
import com.packge.manager.tosam.br.libraryApi.security.SecurityService;
import com.packge.manager.tosam.br.libraryApi.validator.AutorValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;

    private final AutorValidator autorValidator;

    private final LivroRepository livroRepository;

    private final SecurityService securityService;


    public Autor salvar(Autor autor) {
        autorValidator.validar(autor);

        Usuario usuario = securityService.UsuarioLogado();
        autor.setUsuario(usuario);
        return autorRepository.save(autor);


    }


    public void atualizar(Autor autor) {

        if (autor.getId() == null) {
            throw new IllegalArgumentException("Para atualizar, é necessario que o autor já esteja salvo na base de dados");
        }

        autorValidator.validar(autor);

        autorRepository.save(autor);


    }

    public Optional<Autor> obterPorId(UUID uuid) {

        return autorRepository.findById(uuid);

    }


    public void deletar(Autor autor) {

        if (possuiLivro(autor)) {
            throw new OperacaoNaoPermitidaException("Não é permitido excluir um autor que possui livros cadastrados!");
        }

        autorRepository.delete(autor);
    }

    public List<Autor> pesquisaByExample(String nome, String nacionalidade) {

        var autor = new Autor();

        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);


        Example<Autor> autorExample = Example.of(autor, matcher);

        return autorRepository.findAll(autorExample);
    }


    public boolean possuiLivro(Autor autor) {

        return livroRepository.existsByAutor(autor);

    }


}
