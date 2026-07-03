package desafio.santander.springboot.cep.dto;

import java.time.LocalDateTime;

import desafio.santander.springboot.cep.domain.ConsultaCepLog;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Registro de log de uma consulta de CEP.")
public record ConsultaCepLogResponse(
		@Schema(description = "Identificador do log.", example = "1")
		Long id,
		@Schema(description = "CEP consultado.", example = "01001000")
		String cep,
		@Schema(description = "Logradouro retornado quando a consulta foi concluida com sucesso.", example = "Praca da Se")
		String logradouro,
		@Schema(description = "Bairro retornado quando a consulta foi concluida com sucesso.", example = "Se")
		String bairro,
		@Schema(description = "Cidade/localidade retornada quando a consulta foi concluida com sucesso.", example = "Sao Paulo")
		String localidade,
		@Schema(description = "Unidade federativa retornada quando a consulta foi concluida com sucesso.", example = "SP")
		String uf,
		@Schema(description = "Status HTTP retornado pela API externa ou status tecnico da integracao.", example = "200")
		Short httpStatusCode,
		@Schema(description = "Indica se a consulta externa foi concluida com sucesso.", example = "true")
		Boolean success,
		@Schema(description = "Corpo completo retornado pela API externa.", example = "{\"cep\":\"01001000\",\"logradouro\":\"Praca da Se\"}")
		String responseBody,
		@Schema(description = "Mensagem de erro registrada quando a consulta externa falha.", example = "CEP nao encontrado: 99999999")
		String errorMessage,
		@Schema(description = "Data e hora em que a consulta foi realizada.", example = "2026-07-02T21:30:00")
		LocalDateTime consultedAt
) {

	public static ConsultaCepLogResponse from(ConsultaCepLog log) {
		return new ConsultaCepLogResponse(
				log.getId(),
				log.getCep(),
				log.getLogradouro(),
				log.getBairro(),
				log.getLocalidade(),
				log.getUf(),
				log.getHttpStatusCode(),
				log.getSuccess(),
				log.getResponseBody(),
				log.getErrorMessage(),
				log.getConsultedAt());
	}
}
