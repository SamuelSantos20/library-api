package com.packge.manager.tosam.br.libraryApi.controller;

import com.packge.manager.tosam.br.libraryApi.dto.CadastroLivroDTO;
import com.packge.manager.tosam.br.libraryApi.dto.ResultadoPesquisaLivroDTO;
import com.packge.manager.tosam.br.libraryApi.mappers.LivroMapper;
import com.packge.manager.tosam.br.libraryApi.model.GeneroLivro;
import com.packge.manager.tosam.br.libraryApi.model.Livro;
import com.packge.manager.tosam.br.libraryApi.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
@Tag(name = "Livros")
public class LivroController implements GenericController {

    private final LivroService livroService;

    private final LivroMapper livroMapper;


    @PostMapping
    @PreAuthorize("hasAnyRole('OPERADOR' , 'GERENTE')")
    @Operation(summary = "Salvar", description = "Cadastrar novo Livro")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cadastrado com sucesso."),
            @ApiResponse(responseCode = "409", description = "Livro ou ISBN já cadastrado."),
            @ApiResponse(responseCode = "422", description = "Livro com data de publicação acima de 2020 e sem preço.")
    })
    public ResponseEntity<Object> saveLivro(@RequestBody @Valid CadastroLivroDTO cadastroLivroDTO) {


        Livro livro = livroMapper.toEntity(cadastroLivroDTO);

        livroService.salvar(livro);

        var location = gerarHaderLoccation(livro.getId());

        return ResponseEntity.created(location).build();


    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR' , 'GERENTE')")
    @Operation(summary = "Obter Detalhes", description = "Retorna os dados do livro pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro encontrado."),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado."),
    })
    public ResponseEntity<ResultadoPesquisaLivroDTO> ObterDetalhes(@PathVariable("id") String id) {


        return livroService.obterDetalhesPorId(UUID.fromString(id)).map(livro -> {


            var dto = livroMapper.toDTO(livro);

            return ResponseEntity.ok(dto);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR' , 'GERENTE')")
    @Operation(summary = "Deletar", description = "Cadastrar novo Livro")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deletado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado."),
    })
    public ResponseEntity<Object> Deletar(@PathVariable("id") String id) {


        return livroService.obterDetalhesPorId(UUID.fromString(id)).map(livro -> {
            livroService.deletar(livro);

            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());


    }


   

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERADOR' , 'GERENTE')")
    @Operation(summary = "Pesquisa paginada", description = "Realiza pesquisa de livros por parametros ou por numeros de paginas e elementos por pagina. ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sucesso."),
    })
    public ResponseEntity<Page<ResultadoPesquisaLivroDTO>> pesquisa(
            @RequestParam(value = "isbn", required = false)
            String isbn,
            @RequestParam(value = "titulo", required = false)
            String titulo,
            @RequestParam(value = "nome-autor", required = false)
            String nomeAutor,
            @RequestParam(value = "genero", required = false)
            GeneroLivro genero,
            @RequestParam(value = "ano-publicacao", required = false)
            Integer anoPublicacao,
            @RequestParam(value = "pagina", defaultValue = "0")
            Integer pagina,
            @RequestParam(value = "numero-elementos", defaultValue = "0")
            Integer numero_elementos

    ) {


        Page<Livro> Page = livroService.pesquisa(isbn, titulo, nomeAutor, genero, anoPublicacao, pagina, numero_elementos);

        Page<ResultadoPesquisaLivroDTO> lista = Page.map(livroMapper::toDTO);


        return ResponseEntity.ok(lista);


    }


    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('OPERADOR' , 'GERENTE')")
    @Operation(summary = "Atualizar", description = "Cadastrar novo Livro")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Atualizado com sucesso."),
            @ApiResponse(responseCode = "409", description = "Livro OU ISBN já cadastrado."),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado.")
    })
    public ResponseEntity<Object> atualizar(@PathVariable("id") @Valid String id, @RequestBody CadastroLivroDTO dto) {

        return livroService.obterDetalhesPorId(UUID.fromString(id)).map(livro -> {

            var livroEntity = livroMapper.toEntity(dto);


            livro.setTitulo(livroEntity.getTitulo());
            livro.setIsbn(livroEntity.getIsbn());
            livro.setDataPublicacao(livroEntity.getDataPublicacao());
            livro.setGenero(livroEntity.getGenero());
            livro.setPreco(livroEntity.getPreco());
            livro.setAutor(livroEntity.getAutor());

            livroService.atualizar(livro);

            return ResponseEntity.noContent().build();

        }).orElseGet(() -> ResponseEntity.notFound().build());

    }


}
