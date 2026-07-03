package desafio.santander.springboot.cep.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta padronizada para erros da API.")
public record ErroResponse(
		@Schema(description = "Data e hora em que o erro foi gerado.", example = "2026-07-02T21:30:00")
		LocalDateTime timestamp,
		@Schema(description = "Status HTTP retornado.", example = "400")
		int status,
		@Schema(description = "Titulo resumido do erro.", example = "Requisicao invalida")
		String erro,
		@Schema(description = "Mensagem publica com a causa do erro.", example = "CEP deve conter exatamente 8 digitos numericos")
		String mensagem,
		@Schema(description = "Endpoint chamado pelo cliente.", example = "/api/ceps/abc")
		String path
) {
}
