package com.packge.manager.tosam.br.libraryApi.repository;

import com.packge.manager.tosam.br.libraryApi.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AutorRepository extends JpaRepository<Autor , UUID> {

    List<Autor> findByNomeAndNacionalidade(String nome , String nacionalidade);

    List<Autor> findByNome(String nome);

    List<Autor> findByNacionalidade(String nacionalidade);

    Optional<Autor> findByNomeAndDataNascimentoAndNacionalidade(String nome, LocalDate date  , String nacionalidade);
}
