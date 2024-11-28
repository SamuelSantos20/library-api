package com.packge.manager.tosam.br.libraryApi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name ="Campo Invalido" )
public record ErroCampo(@Schema(name = "campo") String campo  , @Schema(name = "mensagem") String mensagem) {



}
