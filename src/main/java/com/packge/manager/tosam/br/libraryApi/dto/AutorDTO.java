package com.packge.manager.tosam.br.libraryApi.dto;

import com.packge.manager.tosam.br.libraryApi.model.Autor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;




@Schema(name = "Autor")
public record AutorDTO(

        UUID id ,
        @NotBlank(message = "O campo é obrigatorio")
        @Size( max = 200,  min = 2,  message = "Campo fora do tamanho padrão!")
        @Schema(name = "nome")
        String nome ,
        @NotNull(message = "O campo é obrigatorio")
        @Past(message = "A data de nasciomento não pode ser futura!")
        @Schema(name = "dataNascimento")
        LocalDate dataNascimento ,
        @NotBlank(message ="O campo é obrigatório" )
        @Size(max = 100 , min = 2 , message = "Campo fora do tamanho padrão!")
        @Schema(name = "nacionalidade")
        String nacionalidade) {


}
