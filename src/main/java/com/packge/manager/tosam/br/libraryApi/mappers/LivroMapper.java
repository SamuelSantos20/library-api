package com.packge.manager.tosam.br.libraryApi.mappers;

import com.packge.manager.tosam.br.libraryApi.dto.CadastroLivroDTO;
import com.packge.manager.tosam.br.libraryApi.dto.ResultadoPesquisaLivroDTO;
import com.packge.manager.tosam.br.libraryApi.model.Livro;
import com.packge.manager.tosam.br.libraryApi.repository.AutorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = AutorMapper.class)
public abstract class LivroMapper {

	@Autowired
	AutorRepository autorRepository;

	@Mapping(target = "autor", expression = "java( autorRepository.findById(dto.idAutor()).orElse(null))")
	public abstract Livro toEntity(CadastroLivroDTO dto);

	public abstract ResultadoPesquisaLivroDTO toDTO(Livro livro);

}
