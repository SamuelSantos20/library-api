package com.packge.manager.tosam.br.libraryApi.repository;

import com.packge.manager.tosam.br.libraryApi.model.Autor;
import com.packge.manager.tosam.br.libraryApi.model.GeneroLivro;
import com.packge.manager.tosam.br.libraryApi.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LivroRepository extends JpaRepository<Livro, UUID>, JpaSpecificationExecutor {


   
    @Query("select l from Livro as l order by l.titulo")
    List<Livro> ListarTodosOsLivros();

   
    @Query("select l from Livro as l order by l.titulo , l.preco")
    List<Livro> ListarTodosOrdenandoPorTituloAndPreco();



    @Query("select l from Livro l where l.titulo= :titulo and l.preco = :preco")
    List<Livro> ListarLivrosQueryComParametro(@Param("titulo") String titulo, @Param("preco") BigDecimal preco);


  
    @Query("select l from Livro l where l.titulo = ?1")
    List<Livro> ListarTodosPositionQuery(String titulo);

   
    
    @Modifying
    @Transactional
    @Query("delete from Livro where genero = ?1 ")
    void deleteByGenero(GeneroLivro generoLivro);


  

    @Modifying
    @Transactional
    @Query("update Livro set dataPublicacao = ?1 ")
    void updateDataPublicacao(LocalDate localDate);


    Optional<Livro> findByIsbn(String string);


    boolean existsByAutor(Autor autor);
}
