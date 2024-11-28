# Library API

Uma API para gerenciar autores e livros, utilizando autenticação e segurança com **Spring Security**. A API suporta autenticação via **Basic Auth**, **JWT** e **OAuth2**.

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- 
- **Spring Boot**
- 
- **Spring Security**
- 
- **JWT (JSON Web Token)**
- 
- **OAuth2**
- 
- **PostgreSQL**

## 🔑 Funcionalidades

- **Cadastro de Autores**: Permite criar, listar, atualizar e excluir autores.
- 
- **Cadastro de Livros**: Permite criar, listar, atualizar e excluir livros.
- 
- **Autenticação e Autorização**:
- 
  - **Basic Auth** para autenticação simples.
  - 
  - **JWT** para autenticação baseada em tokens.
  - 
  - **OAuth2** para autenticação com provedores externos, como Google.

## 🚀 Como Executar

1. Clone o repositório:
2. 
   ```bash
   git clone https://github.com/seu-usuario/library-api.git
   
   cd library-api
   
Configure o banco de dados no arquivo application.yml ou application.properties:

yaml

Copiar código

spring:

  datasource:
  
    url: jdbc:postgresql://localhost:5432/library
    
    username: seu-usuario
    
    password: sua-senha
    
Compile e execute o projeto:

bash

Copiar código

./mvnw spring-boot:run

Acesse a API em: http://localhost:8080.

📄 Endpoints Principais

Livros

POST /livros - Cadastra um novo livro.

GET /livros - Lista todos os livros.

PUT /livros/{id} - Atualiza um livro existente.

DELETE /livros/{id} - Exclui um livro.

Autores

POST /autores - Cadastra um novo autor.

GET /autores - Lista todos os autores.

PUT /autores/{id} - Atualiza um autor existente.

DELETE /autores/{id} - Exclui um autor.

🛡️ Segurança
Basic Auth: Use username e password diretamente no cabeçalho da requisição.

JWT: Gere um token ao autenticar-se e use-o para acessar recursos protegidos.

OAuth2: Integração com provedores externos para autenticação, como Google.

📚 Documentação

Acesse a documentação da API no Swagger:

URL: http://localhost:8080/swagger-ui

🧑‍💻 Contribuições

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou pull requests.
