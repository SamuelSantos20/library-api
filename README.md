# Library API

API REST para gerenciar autores, livros e usuários, com autenticação JWT, login social via Google e controle de acesso por perfil.

## Funcionalidades

- CRUD de autores e livros.
- Pesquisa paginada de livros por ISBN, título, autor, gênero e ano de publicação.
- Cadastro de usuários com senha criptografada.
- Autenticação por JWT e autorização por perfis (`OPERADOR`, `GERENTE` e `ADMIN`).
- Login social com Google OAuth2.
- Validação de dados e tratamento centralizado de exceções.
- Documentação interativa com OpenAPI e Swagger UI.

## Tecnologias

- Java 21
- Spring Boot 3.3.5
- Spring Web, Spring Data JPA e Spring Security
- JWT e OAuth2
- PostgreSQL
- MapStruct e Lombok
- OpenAPI/Swagger
- JUnit 5
- Maven Wrapper

## Pré-requisitos

- JDK 21
- PostgreSQL com um banco chamado `library`
- Credenciais OAuth2 do Google para utilizar o login social

## Configuração

A aplicação aceita as seguintes variáveis de ambiente:

| Variável | Descrição | Valor padrão |
| --- | --- | --- |
| `DB_URL` | URL JDBC do PostgreSQL | `jdbc:postgresql://localhost:5432/library` |
| `DB_USERNAME` | Usuário do PostgreSQL | `postgres` |
| `DB_PASSWORD` | Senha do PostgreSQL | `admin` |
| `JWT_SECRET` | Chave usada para assinar os tokens JWT | chave local de desenvolvimento |
| `GOOGLE_CLIENT_ID` | Client ID do Google OAuth2 | obrigatório |
| `GOOGLE_CLIENT_SECRET` | Client secret do Google OAuth2 | obrigatório |
| `CORS_ALLOWED_ORIGINS` | Origens permitidas, separadas por vírgula | URLs locais usadas no desenvolvimento |

Use valores próprios e seguros fora do ambiente de desenvolvimento.

## Como executar

1. Clone o repositório:

   ```bash
   git clone https://github.com/SamuelSantos20/library-api.git
   cd library-api
   ```

2. Configure o PostgreSQL e as variáveis de ambiente necessárias.

3. Inicie a aplicação:

   ```bash
   ./mvnw spring-boot:run
   ```

   No Windows, use `mvnw.cmd spring-boot:run`.

A API ficará disponível em `http://localhost:8081`.

## Autenticação

Cadastre um usuário em `POST /usuarios` e autentique-se em `POST /auth/login`:

```json
{
  "login": "seu-usuario",
  "senha": "sua-senha"
}
```

Envie o token retornado nas rotas protegidas:

```http
Authorization: Bearer <token>
```

## Endpoints principais

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `POST` | `/usuarios` | Cadastra um usuário |
| `POST` | `/auth/login` | Gera um token JWT |
| `POST` | `/livros` | Cadastra um livro |
| `GET` | `/livros` | Pesquisa livros com filtros e paginação |
| `GET` | `/livros/{id}` | Consulta um livro |
| `PUT` | `/livros/{id}` | Atualiza um livro |
| `DELETE` | `/livros/{id}` | Exclui um livro |
| `POST` | `/autores` | Cadastra um autor |
| `GET` | `/autores` | Pesquisa autores |
| `GET` | `/autores/{id}` | Consulta um autor |
| `PUT` | `/autores/{id}` | Atualiza um autor |
| `DELETE` | `/autores/{id}` | Exclui um autor |

## Documentação e testes

Com a aplicação em execução, acesse o Swagger UI em:

```text
http://localhost:8081/swagger-ui.html
```

Para executar a suíte de 18 testes:

```bash
./mvnw test
```

No Windows, use `mvnw.cmd test`. O perfil `test` usa H2 em memória e credenciais OAuth fictícias, portanto a suíte não depende de PostgreSQL nem de chaves externas.

## Licença

Este projeto está licenciado sob os termos do arquivo [LICENSE](LICENSE).
