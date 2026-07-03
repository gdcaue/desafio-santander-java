package desafio.santander.springboot.cep.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import desafio.santander.springboot.cep.client.CepClient;
import desafio.santander.springboot.cep.client.CepClientResponse;
import desafio.santander.springboot.cep.domain.ConsultaCepLog;
import desafio.santander.springboot.cep.dto.CepResponse;
import desafio.santander.springboot.cep.exception.CepNaoEncontradoException;
import desafio.santander.springboot.cep.exception.ErroIntegracaoCepException;
import desafio.santander.springboot.cep.repository.ConsultaCepLogRepository;

@Service
public class ConsultaCepService {

	private final CepClient cepClient;
	private final ConsultaCepLogRepository consultaCepLogRepository;

	public ConsultaCepService(CepClient cepClient, ConsultaCepLogRepository consultaCepLogRepository) {
		this.cepClient = cepClient;
		this.consultaCepLogRepository = consultaCepLogRepository;
	}

	public CepResponse consultar(String cep) {
		try {
			CepClientResponse clientResponse = cepClient.buscarPorCep(cep);

			ConsultaCepLog log = ConsultaCepLog.success(
					clientResponse.cepResponse(),
					clientResponse.responseBody(),
					clientResponse.httpStatusCode(),
					LocalDateTime.now());

			consultaCepLogRepository.save(log);
			return clientResponse.cepResponse();
		} catch (CepNaoEncontradoException exception) {
			registrarFalha(
					exception.getCep(),
					exception.getResponseBody(),
					exception.getHttpStatusCode(),
					exception.getMessage());
			throw exception;
		} catch (ErroIntegracaoCepException exception) {
			registrarFalha(
					exception.getCep(),
					exception.getResponseBody(),
					exception.getHttpStatusCode(),
					exception.getMessage());
			throw exception;
		}
	}

	private void registrarFalha(String cep, String responseBody, int httpStatusCode, String errorMessage) {
		ConsultaCepLog log = ConsultaCepLog.failure(
				cep,
				responseBody,
				httpStatusCode,
				errorMessage,
				LocalDateTime.now());

		consultaCepLogRepository.save(log);
	}
}
