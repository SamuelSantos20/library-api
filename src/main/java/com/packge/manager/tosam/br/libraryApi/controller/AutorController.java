package com.packge.manager.tosam.br.libraryApi.controller;

import com.packge.manager.tosam.br.libraryApi.dto.AutorDTO;
import com.packge.manager.tosam.br.libraryApi.mappers.AutorMapper;
import com.packge.manager.tosam.br.libraryApi.model.Autor;
import com.packge.manager.tosam.br.libraryApi.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

 

@RestController
@RequestMapping("/autores")
@RequiredArgsConstructor
@Tag(name = "Autores")
public class AutorController implements GenericController {

    private final AutorService autorService;

    private final AutorMapper mapper;


   
    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Salvar", description = "Cadastrar novo autor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso."),
            @ApiResponse(responseCode = "422", description = "Erro  de validação."),
            @ApiResponse(responseCode = "409", description = "Autor já cadastrado.")
    })
    public ResponseEntity<Object> save(@RequestBody @Valid AutorDTO dto) {


        Autor autor = mapper.toEntity(dto);

        autorService.salvar(autor);

        var location = gerarHaderLoccation(autor.getId());
        //O metodo ResponseEntity.created() faz com que retorne um crated passando para o metodo uma URI
        return ResponseEntity.created(location).build();


    }


    @PutMapping("{id}")
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Atualizar", description = "Atualiza um autor existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado."),
            @ApiResponse(responseCode = "409", description = "Autor já cadastrado.")
    })
    public ResponseEntity<Object> Update(@PathVariable("id") String id, @RequestBody AutorDTO autorDTO) {


        UUID uuid = UUID.fromString(id);

        Optional<Autor> autorOptional = autorService.obterPorId(uuid);

        if (autorOptional.isEmpty()) {
            return ResponseEntity.noContent().build();
        }


        var autor = autorOptional.get();

        autor.setNome(autorDTO.nome());
        autor.setDataNascimento(autorDTO.dataNascimento());
        autor.setNacionalidade(autorDTO.nacionalidade());


        autorService.atualizar(autor);


        return ResponseEntity.notFound().build();

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Obter Detalhes", description = "Retorna os dados do autor pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autor encontrado."),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado."),
    })
    public ResponseEntity<AutorDTO> obterDetalhes(@PathVariable("id") String id) {

        System.out.println(id);


        var idAutor = UUID.fromString(id);

        Optional<Autor> autorOptional = autorService.obterPorId(idAutor);

        return autorService
                .obterPorId(idAutor)
                .map(autor -> {

                    AutorDTO dto = mapper.toDTO(autor);
                    return ResponseEntity.ok(dto);

                })
                .orElseGet(() -> ResponseEntity.notFound().build());


    }


    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Deletar", description = "Deleta um autor existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Autor não encontardo."),
            @ApiResponse(responseCode = "400", description = "Autor possui livro cadastrado.")
    })
    public ResponseEntity<Object> Deletar(@PathVariable("id") String id) {


        UUID uuid = UUID.fromString(id);

        Optional<Autor> autorOptional = autorService.obterPorId(uuid);

        if (autorOptional.isEmpty()) {

            return ResponseEntity.notFound().build();


        }

        autorService.deletar(autorOptional.get());

        return ResponseEntity.noContent().build();


    }

    @GetMapping
    @PreAuthorize("hasRole('GERENTE')")
    @Operation(summary = "Pesquisar ", description = "Realiza pesquisa de autores por parametros")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucesso."),
    })
    public ResponseEntity<List<AutorDTO>> pesquisar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "nacionalidade", required = false) String nacionalidade) {

        List<Autor> autors = autorService.pesquisaByExample(nome, nacionalidade);

        List<AutorDTO> autorDTOS = autors.stream().
                map(mapper::toDTO)
                .collect(Collectors.toList());

        autorDTOS.forEach(System.out::println);

        return ResponseEntity.ok(autorDTOS);


    }


}
