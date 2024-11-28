package com.packge.manager.tosam.br.libraryApi.service;

import com.packge.manager.tosam.br.libraryApi.model.GeneroLivro;
import com.packge.manager.tosam.br.libraryApi.model.Livro;
import com.packge.manager.tosam.br.libraryApi.model.Usuario;
import com.packge.manager.tosam.br.libraryApi.repository.LivroRepository;

import static com.packge.manager.tosam.br.libraryApi.repository.specifications.LivroSpecification.*;

import com.packge.manager.tosam.br.libraryApi.security.SecurityService;
import com.packge.manager.tosam.br.libraryApi.validator.LivroValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;

    private final LivroValidator livroValidator;

    private final SecurityService securityService;

    public Livro salvar(Livro livro) {

        livroValidator.validar(livro);
        Usuario usuario = securityService.UsuarioLogado();

        livro.setUsuario(usuario);
        return livroRepository.save(livro);


    }

    public Optional<Livro> obterDetalhesPorId(UUID uuid) {

        return livroRepository.findById(uuid);

    }

    public void deletar(Livro livro) {

        livroRepository.delete(livro);

    }

    
    public Page<Livro> pesquisa(String isbn, String titulo, String nomeAutor, GeneroLivro genero, Integer anoPublicacao, Integer page, Integer elements) {


        Specification<Livro> specs = Specification.where((root, query, cb) -> cb.conjunction());


        if (isbn != null) {

            specs = specs.and(isbnEqual(isbn));

        }

        if (titulo != null) {

            specs = specs.and(tituloLike(titulo));

        }

        if (genero != null) {

            specs = specs.and(generoEqual(genero));

        }


        if (anoPublicacao != null) {

            specs = specs.and(dataPublicacaoEqual(anoPublicacao));
        }


        if (nomeAutor != null) {

            specs = specs.and(nomeAutorLike(nomeAutor));

        }

        Pageable pagina = PageRequest.of(page, elements);


        return livroRepository.findAll(specs, pagina);

    }


    public void atualizar(Livro livro) {

        if (livro.getId() == null) {

            throw new IllegalArgumentException("Para atualizar, é necessario que o livro já esteja salvo na base de dados");

        }
        livroValidator.validar(livro);
        livroRepository.save(livro);

    }
}
