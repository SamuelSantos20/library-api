package com.packge.manager.tosam.br.libraryApi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Schema(name = "Usuario")
public record UsuarioDTO(
          @NotBlank(message = "campo obrigatório!")
          @Schema(name = "usuario")
          String username
        , @Email(message = "Email inválido!")
        @NotBlank(message = "campo obrigatório!")
          @Schema(name = "email")
          String email
        , @NotBlank(message = "campo obrigatório!")
          @Schema(name = "senha")
          String password,
          @Schema(name = "autorização")
          List<String> roles) {
}
