package desafio.santander.springboot.cep.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados de endereco retornados para um CEP encontrado.")
public record CepResponse(
		@Schema(description = "CEP consultado, sem mascara.", example = "01001000")
		String cep,
		@Schema(description = "Nome do logradouro retornado pela API externa.", example = "Praca da Se")
		String logradouro,
		@Schema(description = "Bairro retornado pela API externa.", example = "Se")
		String bairro,
		@Schema(description = "Cidade ou localidade retornada pela API externa.", example = "Sao Paulo")
		String localidade,
		@Schema(description = "Unidade federativa retornada pela API externa.", example = "SP")
		String uf
) {
}
