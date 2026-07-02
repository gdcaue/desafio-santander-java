# Desafio Técnico - Santander

### Conceito: 

Criar uma aplicação em Java para consultar dados de CEP em API externa, gravar os logs das consultas em uma base de dados e aplicar conceitos básicos de SOLID.

### Definição:

Aplicação Java 17 com Spring Boot, expondo uma API REST para consulta de CEP.

A aplicação consumirá uma API externa simulada com Wiremock, persistirá os logs das consultas em PostgreSQL e utilizará Flyway para versionamento do banco de dados.

O ambiente externo será executado com Docker Compose, contemplando apenas o PostgreSQL e o Wiremock. A aplicação Java será executada localmente.

A API terá validação de entrada com o Validation, documentação com Swagger/OpenAPI e testes automatizados básicos com o Starter Test do Spring.

## Stack

* Java 17
* Spring Boot
* Maven
* Spring Web
* Spring DevTools
* Spring Data JPA
* PostgreSQL
* Flyway
* WireMock
* Docker Compose
* Spring Validation
* Swagger/OpenAPI
* Spring Boot Starter Test
