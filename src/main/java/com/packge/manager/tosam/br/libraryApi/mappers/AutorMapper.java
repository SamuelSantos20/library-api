package com.packge.manager.tosam.br.libraryApi.mappers;

import com.packge.manager.tosam.br.libraryApi.dto.AutorDTO;
import com.packge.manager.tosam.br.libraryApi.model.Autor;
import org.mapstruct.Mapper;




@Mapper(componentModel = "spring")
public interface AutorMapper {

Autor toEntity(AutorDTO autorDTO);

AutorDTO toDTO(Autor autor);

}
         