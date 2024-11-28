package com.packge.manager.tosam.br.libraryApi.mappers;

import com.packge.manager.tosam.br.libraryApi.dto.UsuarioDTO;
import com.packge.manager.tosam.br.libraryApi.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {


    Usuario toEntity(UsuarioDTO usuarioDTO);

}
                   