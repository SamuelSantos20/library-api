package com.packge.manager.tosam.br.libraryApi.repository.specifications;

import com.packge.manager.tosam.br.libraryApi.model.GeneroLivro;
import com.packge.manager.tosam.br.libraryApi.model.Livro;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;



public class LivroSpecification {


    public static Specification<Livro> isbnEqual(String isbn) {

        return (root, query, cb) -> cb.equal(root.get("isbn"), isbn);

    }

    public static Specification<Livro> tituloLike(String titulo) {

        return (root, query, cb) -> cb.like(cb.upper(root.get("titulo")), "%" + titulo.toUpperCase() + "%");

    }

    public static Specification<Livro> generoEqual(GeneroLivro genero) {

        return (root, qury, cb) -> cb.equal(root.get("genero"), genero);


    }


    public static Specification<Livro> dataPublicacaoEqual(Integer anoPublicacao) {


        return (root, query, cb) -> cb.equal(cb.function("to_char", String.class, root.get("dataPublicacao"), cb.literal("YYYY")), anoPublicacao.toString());

    }

    public static Specification<Livro> nomeAutorLike(String nomeAutor) {


        return (root, query, cb) -> {
            Join<Object, Object> autorJoin = root.join("autor", JoinType.INNER);


            return cb.like(cb.upper(autorJoin.get("nome")), "%" + nomeAutor.toUpperCase() + "%");
        };

    }


}
