package desafio.santander.springboot.cep.dto;

import java.time.LocalDateTime;

import desafio.santander.springboot.cep.domain.ConsultaCepLog;

public record ConsultaCepLogResponse(
		Long id,
		String cep,
		String logradouro,
		String bairro,
		String localidade,
		String uf,
		Short httpStatusCode,
		Boolean success,
		String responseBody,
		String errorMessage,
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
