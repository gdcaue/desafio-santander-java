package desafio.santander.springboot.cep.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import desafio.santander.springboot.cep.dto.CepResponse;
import desafio.santander.springboot.cep.exception.CepNaoEncontradoException;
import desafio.santander.springboot.cep.exception.ErroIntegracaoCepException;
import tools.jackson.databind.ObjectMapper;

@Component
public class WireMockCepClient implements CepClient {

	private final RestClient restClient;
	private final ObjectMapper objectMapper;

	public WireMockCepClient(RestClient cepRestClient, ObjectMapper objectMapper) {
		this.restClient = cepRestClient;
		this.objectMapper = objectMapper;
	}

	@Override
	public CepClientResponse buscarPorCep(String cep) {
		try {
			ResponseEntity<String> response = restClient.get()
					.uri("/cep/{cep}", cep)
					.retrieve()
					.toEntity(String.class);

			String responseBody = response.getBody();
			CepResponse cepResponse = objectMapper.readValue(responseBody, CepResponse.class);
			return new CepClientResponse(cepResponse, responseBody, response.getStatusCode().value());
		} catch (HttpClientErrorException.NotFound exception) {
			throw new CepNaoEncontradoException(
					cep,
					exception.getStatusCode().value(),
					exception.getResponseBodyAsString());
		} catch (HttpClientErrorException.BadRequest exception) {
			throw new ErroIntegracaoCepException(
					cep,
					exception.getStatusCode().value(),
					exception.getResponseBodyAsString(),
					"A API externa recusou o formato do CEP informado.",
					exception);
		} catch (RestClientException exception) {
			throw new ErroIntegracaoCepException(
					cep,
					502,
					null,
					"Erro ao consultar a API externa de CEP.",
					exception);
		} catch (Exception exception) {
			throw new ErroIntegracaoCepException(
					cep,
					502,
					null,
					"Erro ao processar a resposta da API externa de CEP.",
					exception);
		}
	}
}
