# Campo Minado

Projeto desktop do jogo **Campo Minado**, desenvolvido em Java com interface gráfica e conexão com banco de dados.

O sistema possui cadastro de usuários, login, registro de partidas, consulta de partidas, ranking e diferentes níveis de dificuldade no jogo.

---

## Sumário

- [Sobre o projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Bibliotecas para instalar](#bibliotecas-para-instalar)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Configuração do banco de dados](#configuração-do-banco-de-dados)
- [Como importar e rodar no Eclipse](#como-importar-e-rodar-no-eclipse)
- [Como jogar](#como-jogar)
- [Licença](#licença)

---

## Sobre o projeto

Este projeto é uma implementação do clássico jogo **Campo Minado**.

Além da lógica principal do jogo, a aplicação também possui recursos de cadastro, login, histórico de partidas e ranking dos jogadores.

O projeto está organizado em pacotes separados para facilitar a manutenção do código, separando as telas, os controles, as regras do jogo, o acesso ao banco de dados e classes auxiliares.

---

## Funcionalidades

- Cadastro de usuários.
- Login de usuários.
- Jogo Campo Minado com diferentes dificuldades.
- Registro de partidas.
- Consulta de partidas.
- Consulta de usuários.
- Ranking dos jogadores.
- Contador de tempo durante a partida.
- Controle de vitória e derrota.
- Reinício de partida.
- Interface gráfica desktop.

---

## Bibliotecas para instalar

O projeto precisa de duas bibliotecas externas para funcionar corretamente.

Baixe os arquivos `.jar` nos links abaixo:

- MySQL Connector/J  
  https://dev.mysql.com/downloads/connector/j/

- FlatLaf  
  https://www.formdev.com/flatlaf/

Depois de baixar, coloque os dois arquivos `.jar` dentro de uma pasta chamada `lib` na raiz do projeto.

A estrutura deve ficar assim:

```txt
Campo-minado/
├── lib/
│   ├── mysql-connector-j-8.0.33.jar
│   └── flatlaf-3.5.jar
├── src/
├── db.properties
└── ...