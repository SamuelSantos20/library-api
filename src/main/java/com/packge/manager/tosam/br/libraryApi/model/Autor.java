package com.packge.manager.tosam.br.libraryApi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "autor" , schema = "public")
@Data
@ToString(exclude = {"livros"})
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Autor {

    @Id
    @Column(name = "id" , nullable = false , unique = true , updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome" , length = 200 ,  nullable = false)
    private String nome;

    @Column(name = "data_nascimento" , nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "nacionalidade" , length = 100 ,nullable = false)
    private String nacionalidade;

    
    @CreatedDate
    @Column(name = "data_cadastro")
    private LocalDateTime data_cadastro;

  
    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime data_atualizacao;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;


    @OneToMany(mappedBy = "autor" ,
             fetch = FetchType.LAZY)
    private List<Livro> livros;


}

