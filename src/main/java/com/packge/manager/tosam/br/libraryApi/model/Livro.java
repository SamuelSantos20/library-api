package com.packge.manager.tosam.br.libraryApi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "livro")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Livro {

    @Id
    @Column(name = "id" , nullable = false , unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "isbn" , nullable = false , length = 30)
    private String isbn;

    @Column(name = "titulo" , nullable = false , length = 150)
    private String titulo;

    @Column(name = "data_publicacao" , nullable = false)
    private LocalDate dataPublicacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero" , nullable = false , length = 30)
    private GeneroLivro genero;

    @Column(name = "preco" ,precision = 18 , scale = 2)
    private BigDecimal preco;

    
    @CreatedDate
    @Column(name = "data_cadastro")
    private LocalDateTime data_cadastro;

  
    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime data_atualizacao;

    @JoinColumn(name = "id_autor")
    @ManyToOne
    private Autor autor;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;



}
