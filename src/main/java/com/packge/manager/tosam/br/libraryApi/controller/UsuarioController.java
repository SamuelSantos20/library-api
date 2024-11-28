package com.packge.manager.tosam.br.libraryApi.controller;

import com.packge.manager.tosam.br.libraryApi.dto.UsuarioDTO;
import com.packge.manager.tosam.br.libraryApi.mappers.UsuarioMapper;
import com.packge.manager.tosam.br.libraryApi.service.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuarios")
@Tag(name = "Usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    private final UsuarioMapper usuarioMapper;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void salvar(@RequestBody @Valid UsuarioDTO usuarioDTO) {

        var usuario = usuarioMapper.toEntity(usuarioDTO);


        usuarioService.salvar(usuario);

    }


}
