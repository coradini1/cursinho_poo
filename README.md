
# Projeto de Gerenciamento de Aulas Escolares

Este projeto foi desenvolvido como parte do curso de Programação Orientada a Objetos da Web I, com o objetivo de criar um sistema de gerenciamento de materiais, aulas, cursos e usuários para auxiliar uma escola.




## Tecnologias Utilizadas

 - Java
 - Spring Framework
 - MySQL


## Funcionalidades

- Registro de Materiais: Os usuários podem cadastrar e gerenciar diferentes materiais, fornecendo informações como nome, descrição e um arquivo para ser disponibilizado para download.
- Registro de Aulas: O sistema permite o registro e gerenciamento de aulas, incluindo informações como título, data, curso associado e material cadastrado (permite o download do material).
- Registro de Cursos: Os cursos podem ser cadastrados e gerenciados, contendo informações como nome, descrição.
- Gerenciamento de Usuários: O sistema permite o cadastro de usuários, incluindo diferentes tipos de permissões e controle de acesso.


## Configuração do Banco de Dados
O projeto utiliza o banco de dados MySQL. Para configurar o banco de dados, siga as etapas abaixo:

Execute os seguintes comandos SQL para criar as tabelas necessárias:

```bash
 CREATE TABLE Usuarios (
    id INT AUTO_INCREMENT,
    nome VARCHAR(255),
    idade INT,
    tipoUsuario ENUM('admin', 'professor', 'aluno'),
    senha VARCHAR(255),
    PRIMARY KEY(id)
);

CREATE TABLE Cursos (
    id INT AUTO_INCREMENT,
    nome VARCHAR(255),
    descricao TEXT,
    professor_id INT,
    PRIMARY KEY(id),
    FOREIGN KEY(professor_id) REFERENCES Usuarios(id)
);


CREATE TABLE Materiais (
    id INT AUTO_INCREMENT,
    nome VARCHAR(255),
    descricao TEXT,
curso_id INT,
caminho_arquivo VARCHAR(255),
    PRIMARY KEY(id)
);

CREATE TABLE Aulas (
  id INT PRIMARY KEY AUTO_INCREMENT,
  titulo VARCHAR(100) NOT NULL,
  data DATE NOT NULL,
  curso_id INT NOT NULL,
  material_id INT NOT NULL,
  FOREIGN KEY (curso_id) REFERENCES Cursos(id),
  FOREIGN KEY (material_id) REFERENCES Materiais(id)
);
```


## Relacionados

Configure as informações de conexão com o banco de dados no arquivo application.properties no diretório src/main/resources do projeto.
Executando o Projeto
Clone o repositório do projeto em sua máquina local.

Abra o projeto em sua IDE

Certifique-se de que as dependências do projeto sejam baixadas e atualizadas.

Execute a aplicação Spring Boot.

Acesse o site através do endereço http://localhost:8080 em seu navegador.




## Autores

- [@coradini1](https://github.com/coradini1)

