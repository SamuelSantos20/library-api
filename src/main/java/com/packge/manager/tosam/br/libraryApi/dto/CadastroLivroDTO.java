package com.packge.manager.tosam.br.libraryApi.dto;

import com.packge.manager.tosam.br.libraryApi.model.GeneroLivro;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "Livro")
public record CadastroLivroDTO(
        @ISBN
        @NotBlank(message = "O camo é obrigatorio!")
        @Schema(name = "isbn")
        String isbn ,

        @NotBlank(message = "O campo é obrigatorio!")
        @Schema(name = "titulo")
        String titulo,

        @NotNull(message = "O campo é obrigatorio!")
        @Past(message = "A data de publicação não pode ser uma data futura!")
        @Schema(name = "dataPublicacao")
        LocalDate dataPublicacao ,

        @Schema(name = "genero")
        GeneroLivro genero ,
        @Schema(name = "preco")
        BigDecimal preco,

        @NotNull(message = "O campo é obrigatorio!")
        @Schema(name = "idAutor")
        UUID idAutor){



}
