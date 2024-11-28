package com.packge.manager.tosam.br.libraryApi.repository;

import com.packge.manager.tosam.br.libraryApi.model.Autor;
import com.packge.manager.tosam.br.libraryApi.model.GeneroLivro;
import com.packge.manager.tosam.br.libraryApi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
class LivroRepositoryTest {

    @Autowired
     LivroRepository livroRepository;

    @Autowired
    AutorRepository autorRepository;

    @Test
    void SaveTeste() {

        Livro livro = new Livro();

        livro.setIsbn("93156-23467");
        livro.setPreco(BigDecimal.valueOf(50));
        livro.setGenero(GeneroLivro.FANTASIA);
        livro.setTitulo("CRONICA");
        livro.setDataPublicacao(LocalDate.of(2000 , 5 , 28));

        var id =  UUID.fromString("f394114e-abc5-4d96-ac9f-e1bb2c61bbf1");
        Autor autor = autorRepository.findById(id).orElse(null);

        livro.setAutor(autor);

        livroRepository.save(livro);


    }


    //OBS: O CASCADE DEVE SER USADO COM MUITO CUIDADO E EM POCAS SITUAÇÕES
    @Test
    void SaveCasdeTeste() {

        Livro livro = new Livro();

        livro.setIsbn("93156-23467");
        livro.setPreco(BigDecimal.valueOf(50));
        livro.setGenero(GeneroLivro.FANTASIA);
        livro.setTitulo("CRONICA");
        livro.setDataPublicacao(LocalDate.of(2000 , 5 , 28));

        Autor autor = new Autor();

        autor.setNacionalidade("US");
        autor.setNome("Josh");
        autor.setDataNascimento(LocalDate.of(1990 , 6 , 27));




        livro.setAutor(autor);

        livroRepository.save(livro);


    }

    @Test
    void AtualizarTeste() {

        UUID id_livro = UUID.fromString("fbc1d0f4-f76b-4f7c-8d1a-08961b037463");

        Optional<Livro> livroOptional = livroRepository.findById(id_livro);

        UUID id = UUID.fromString("e0660237-4deb-4313-9ed2-95662fa20ea9");
        Optional<Autor> autorOptional = autorRepository.findById(id);

        boolean existe = autorOptional.isPresent() && livroOptional.isPresent();

        if(existe){

            Livro livro = livroOptional.get();

            livro.setAutor(autorOptional.get());

            livroRepository.save(livro);
        }

    }


    //OBS: NESSE CASO O DELETE DARIA ERRO  SE ESTIVESSE SEM O CASCADE POIS O CERTO SERIA ELE NÃO TER NEHUM AUTOR VINCULADO AO LIVRO
    // MAS COM O CASCADE ELE DELETA O LIVRO E O AUTOR JUNTO.
    @Test
    void DeleteCascade() {

        UUID id =  UUID.fromString("f5a1fede-1101-484b-9bd2-2f676fc9d0e1");

        livroRepository.deleteById(id);

    }

    @Test
    void ListaLivrosQueryTeste() {

        var livros = livroRepository.ListarTodosOsLivros();

        livros.forEach(System.out::println);




    }

    @Test
    void ListarTodosPorTituloAndPrecoTeste() {

        List<Livro> livros = livroRepository.ListarTodosOrdenandoPorTituloAndPreco();
        livros.forEach(System.out::println);

    }

    @Test
    void ListarComParametroTeste() {
        var titulo = "Nova Revolução-Parte2";

        var preco = BigDecimal.valueOf(390.00);

        List<Livro> livros = livroRepository.ListarLivrosQueryComParametro(titulo, preco);

        livros.forEach(System.out::println);


    }

    @Test
   void ListaPorPositionTeste() {
        var titulo = "Nova Revolução-Parte2";

        List<Livro> livros = livroRepository.ListarTodosPositionQuery(titulo);

        livros.forEach(System.out::println);


    }

    @Test
   void DeleteJpqlTeste() {

        //Delete utilizando o Sistema de CamioCase e o Sistema Jpql
        livroRepository.deleteByGenero(GeneroLivro.FANTASIA);

    }

    @Test
    void UpdateJpqlCamioCaseTeste() {

        //Update utilizando o Sistema de CamioCase e o Sistema Jpql
        livroRepository.updateDataPublicacao(LocalDate.of(2000 , 1 , 1));

    }





}