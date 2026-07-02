# Identidade e proposta da aplicação

## Nome da aplicação

ConsultaCEP

## Proposta

A ConsultaCEP é uma aplicação REST responsável por consultar informações de endereço a partir de um CEP e registrar cada consulta realizada em banco de dados, mantendo um histórico simples para auditoria.

## Problema que a aplicação resolve

Em muitos sistemas, consultas externas precisam ser rastreadas para fins de histórico, auditoria ou análise.

Esta aplicação simula esse cenário ao consultar dados de endereço por CEP em uma API externa mockada e armazenar o resultado de cada consulta realizada.

## Contexto de uso

A aplicação pode ser usada como base para sistemas que precisam validar ou complementar endereços de clientes, fornecedores ou usuários, mantendo o registro das consultas feitas a serviços externos.

## Escopo funcional inicial

A aplicação terá como função principal consultar dados de endereço a partir de um CEP informado.

A cada consulta realizada, o sistema deverá registrar no banco de dados:

* CEP consultado;
* dados retornados pela API externa;
* data e hora da consulta.

Também será possível consultar o histórico das buscas realizadas.
