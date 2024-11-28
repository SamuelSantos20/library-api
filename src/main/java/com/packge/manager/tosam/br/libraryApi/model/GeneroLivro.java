package com.packge.manager.tosam.br.libraryApi.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum GeneroLivro {

    FICCAO,
    FANTASIA,
    MISTERIO,
    ROMANCE,
    CIENCIA;


    private String genero;

    GeneroLivro() {

    }


    public String getGenero() {
        return genero;
    }


    GeneroLivro(String genero) {
        this.genero = genero;
    }
}
