-- auto-generated definition
create database infohome
    with owner postgres;

create schema public;

comment on schema public is 'standard public schema';

alter schema public owner to postgres;

grant create, usage on schema public to public;

create table application_user
(
    id       serial
        constraint application_user_pkey
            primary key,
    email    varchar(255) not null,
    name     varchar(100) not null,
    password varchar(255) not null
);

alter table application_user
    owner to postgres;

create table imovel
(
    id               serial
        constraint imovel_pkey
            primary key,
    cidade           varchar(255) not null,
    descricao        varchar(255),
    estado           varchar(255) not null,
    telefone         varchar(14)  not null,
    titulo           varchar(255) not null,
    url              varchar(255) not null,
    application_user integer      not null
        constraint fkd5v1n81qrsb5yspse2h74360n
            references application_user
);

alter table imovel
    owner to postgres;