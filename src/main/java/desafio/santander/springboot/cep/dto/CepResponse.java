package desafio.santander.springboot.cep.dto;

public record CepResponse(
		String cep,
		String logradouro,
		String bairro,
		String localidade,
		String uf
) {
}
