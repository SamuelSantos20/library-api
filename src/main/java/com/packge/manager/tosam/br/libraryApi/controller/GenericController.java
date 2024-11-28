package com.packge.manager.tosam.br.libraryApi.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;


public interface GenericController {

            default URI gerarHaderLoccation(UUID id){

                return  ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();

            }

}
