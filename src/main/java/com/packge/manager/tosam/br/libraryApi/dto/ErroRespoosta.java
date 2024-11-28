package com.packge.manager.tosam.br.libraryApi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

import java.util.List;
@Schema(name = "Resposta de Erro")
public record ErroRespoosta(
        @Schema(name = "status")
        int status,
        @Schema(name = "mensagem")
        String mensagem,
        @Schema(name = "erroCampos")
        List<ErroCampo> erroCampos) {


    public static ErroRespoosta respostaPadrao(String mensagem) {

        return new ErroRespoosta(HttpStatus.BAD_REQUEST.value() , mensagem , List.of() );


    }

    public static ErroRespoosta conflito(String mensagem) {

        return new ErroRespoosta(HttpStatus.CONFLICT.value(), mensagem , List.of());

    }

}
