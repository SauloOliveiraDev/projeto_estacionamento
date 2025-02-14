CREATE DATABASE estacionamento_saulo;
USE estacionamento_saulo;

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL
);

CREATE TABLE veiculos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    modelo VARCHAR(100) NOT NULL,
    placa VARCHAR(10) NOT NULL UNIQUE,
    cor VARCHAR(50) NOT NULL,
    data_entrada DATETIME NOT NULL
);

insert into usuarios (email, senha) values ("usuario@gmail", "senha123");