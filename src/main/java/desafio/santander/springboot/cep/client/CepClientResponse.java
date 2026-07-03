package desafio.santander.springboot.cep.client;

import desafio.santander.springboot.cep.dto.CepResponse;

public record CepClientResponse(CepResponse cepResponse, String responseBody, int httpStatusCode) {
}
