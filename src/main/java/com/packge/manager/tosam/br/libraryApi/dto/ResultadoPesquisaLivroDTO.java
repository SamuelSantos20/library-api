package com.packge.manager.tosam.br.libraryApi.dto;

import com.packge.manager.tosam.br.libraryApi.model.GeneroLivro;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "Pesquisa Livro")
public record ResultadoPesquisaLivroDTO (

        @Schema(name = "isbn")
        String isbn ,
        @Schema(name = "titulo")
        String titulo,
        @Schema(name = "dataPublicacao")
        LocalDate dataPublicacao ,
        @Schema(name = "genero")
        GeneroLivro genero ,
        @Schema(name = "preco")
        BigDecimal preco,
        @Schema(name = "autor")
        AutorDTO autor){
}
