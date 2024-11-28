package com.packge.manager.tosam.br.libraryApi.repository;

import com.packge.manager.tosam.br.libraryApi.dto.AutorDTO;
import com.packge.manager.tosam.br.libraryApi.model.Autor;
import com.packge.manager.tosam.br.libraryApi.model.GeneroLivro;
import com.packge.manager.tosam.br.libraryApi.model.Livro;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootTest
public class AutorRepositoryTeste {

     @Autowired
     private AutorRepository autorRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Test
    public void SaveTest() {


            Autor autor = new Autor();

            autor.setNome("Samuel");
            autor.setNacionalidade("Brasil");
            autor.setDataNascimento(LocalDate.of(2004 , 10 , 20));

            var autorSalvo = autorRepository.save(autor);

            System.out.println(autorSalvo);



    }

    @Test
    public  void AtualizarTeste() {

        var id  = UUID.fromString("8f792a23-b0b2-4a34-a022-320728850179");

        Optional<Autor> possivelAutor = autorRepository.findById(id);

        if(possivelAutor.isPresent()){

            System.out.println("Informações do Autor " + possivelAutor.get() );

            Autor autorEncontrado = possivelAutor.get();

            autorEncontrado.setDataNascimento(LocalDate.of(2005 , 10 , 5));

            autorRepository.save(autorEncontrado);


        }



    }

    @Test
    public  void ContagemTeste() {

        var quant = autorRepository.count();

        System.out.println("Quantidade de Autores casdastrados: " + quant);
        
    }

    @Test
    public  void ListAutoresTeste() {

        List<Autor> autors = autorRepository.findAll();

        autors.forEach(System.out::println);


    }

    @Test
    void SaveAutorLivroTeste() {


        Autor autor = new Autor();

        autor.setNacionalidade("US");
        autor.setNome("Josh");
        autor.setDataNascimento(LocalDate.of(1990 , 6 , 27));



        Livro livro = new Livro();

        livro.setIsbn("93467-34679");
        livro.setPreco(BigDecimal.valueOf(50));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("Nova Revolução");
        livro.setDataPublicacao(LocalDate.of(2000 , 5 , 28));
        livro.setAutor(autor);



        Livro livro2 = new Livro();

        livro2.setIsbn("93467-35675");
        livro2.setPreco(BigDecimal.valueOf(500));
        livro2.setGenero(GeneroLivro.MISTERIO);
        livro2.setTitulo("A Rua do Medo");
        livro2.setDataPublicacao(LocalDate.of(1980 , 5 , 28));
        livro2.setAutor(autor);


        autor.setLivros(new ArrayList<>());
        autor.getLivros().add(livro);
        autor.getLivros().add(livro2);


        autorRepository.save(autor);
        livroRepository.saveAll(autor.getLivros());



    }

    @Test
    void SaveAutorLivroCascadeTeste() {


        Autor autor = new Autor();

        autor.setNacionalidade("US");
        autor.setNome("Kane Whest");
        autor.setDataNascimento(LocalDate.of(1990 , 6 , 27));



        Livro livro = new Livro();

        livro.setIsbn("93467-34679");
        livro.setPreco(BigDecimal.valueOf(390));
        livro.setGenero(GeneroLivro.FICCAO);
        livro.setTitulo("Nova Revolução-Parte2");
        livro.setDataPublicacao(LocalDate.of(2010 , 9, 18));
        livro.setAutor(autor);



        Livro livro2 = new Livro();

        livro2.setIsbn("95151-52434");
        livro2.setPreco(BigDecimal.valueOf(590));
        livro2.setGenero(GeneroLivro.MISTERIO);
        livro2.setTitulo("A Rua do Medo-Parte4");
        livro2.setDataPublicacao(LocalDate.of(1990 , 8 , 14));
        livro2.setAutor(autor);


        autor.setLivros(new ArrayList<>());
        autor.getLivros().add(livro);
        autor.getLivros().add(livro2);


        autorRepository.save(autor);




    }


    @Test
    void pesquisarTeste() {

        var nome = "Autor 3";

        var nacionalidade = "US";


        List<Autor> autors = autorRepository.findByNomeAndNacionalidade(nome , nacionalidade);

        List<AutorDTO> autorDTOS = autors.stream().
                map( autor -> new
                        AutorDTO(autor.getId() ,
                        autor.getNome() ,
                        autor.getDataNascimento() ,
                        autor.getNacionalidade()))
                .collect(Collectors.toList());

        autorDTOS.forEach(System.out::println);


    }



    



}
