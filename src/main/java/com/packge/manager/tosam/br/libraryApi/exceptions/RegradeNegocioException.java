package com.packge.manager.tosam.br.libraryApi.exceptions;


import lombok.Getter;

public class RegradeNegocioException extends RuntimeException{

    @Getter
    private String campo;


    public RegradeNegocioException(String message, String campo) {
        super(message);
        this.campo = campo;
    }
}
