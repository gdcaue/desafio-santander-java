package desafio.santander.springboot.cep.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import desafio.santander.springboot.cep.client.CepClient;
import desafio.santander.springboot.cep.client.CepClientResponse;
import desafio.santander.springboot.cep.domain.ConsultaCepLog;
import desafio.santander.springboot.cep.dto.CepResponse;
import desafio.santander.springboot.cep.repository.ConsultaCepLogRepository;

@Service
public class ConsultaCepService {

	private final CepClient cepClient;
	private final ConsultaCepLogRepository consultaCepLogRepository;

	public ConsultaCepService(CepClient cepClient, ConsultaCepLogRepository consultaCepLogRepository) {
		this.cepClient = cepClient;
		this.consultaCepLogRepository = consultaCepLogRepository;
	}

	@Transactional
	public CepResponse consultar(String cep) {
		CepClientResponse clientResponse = cepClient.buscarPorCep(cep);

		ConsultaCepLog log = ConsultaCepLog.from(
				clientResponse.cepResponse(),
				clientResponse.responseBody(),
				LocalDateTime.now());

		consultaCepLogRepository.save(log);
		return clientResponse.cepResponse();
	}
}
