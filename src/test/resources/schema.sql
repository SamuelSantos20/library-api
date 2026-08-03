create table if not exists usuario (
    id uuid primary key,
    email varchar(255),
    password varchar(255),
    username varchar(255),
    roles varchar array
);

create table if not exists autor (
    id uuid primary key,
    nome varchar(200) not null,
    data_nascimento date not null,
    nacionalidade varchar(100) not null,
    data_cadastro timestamp,
    data_atualizacao timestamp,
    id_usuario uuid
);

create table if not exists livro (
    id uuid primary key,
    isbn varchar(30) not null,
    titulo varchar(150) not null,
    data_publicacao date not null,
    genero varchar(30) not null,
    preco numeric(18, 2),
    data_cadastro timestamp,
    data_atualizacao timestamp,
    id_autor uuid,
    id_usuario uuid
);
