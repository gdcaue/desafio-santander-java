package desafio.santander.springboot.cep.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import desafio.santander.springboot.cep.dto.CepResponse;
import desafio.santander.springboot.cep.dto.ErroResponse;
import desafio.santander.springboot.cep.service.ConsultaCepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/api/ceps")
@Tag(name = "CEPs")
public class CepController {

	private final ConsultaCepService consultaCepService;

	public CepController(ConsultaCepService consultaCepService) {
		this.consultaCepService = consultaCepService;
	}

	@GetMapping("/{cep}")
	@Operation(
			summary = "Consultar CEP",
			description = "Consulta dados de endereco para um CEP com 8 digitos numericos, registra o log da consulta e retorna os dados encontrados.")
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "CEP encontrado",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = CepResponse.class),
							examples = @ExampleObject(
									name = "CEP encontrado",
									value = """
											{
											  "cep": "01001000",
											  "logradouro": "Praca da Se",
											  "bairro": "Se",
											  "localidade": "Sao Paulo",
											  "uf": "SP"
											}
											"""))),
			@ApiResponse(
					responseCode = "400",
					description = "CEP com formato invalido",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ErroResponse.class),
							examples = @ExampleObject(
									name = "CEP invalido",
									value = """
											{
											  "timestamp": "2026-07-02T21:30:00",
											  "status": 400,
											  "erro": "Requisicao invalida",
											  "mensagem": "CEP deve conter exatamente 8 digitos numericos",
											  "path": "/api/ceps/abc"
											}
											"""))),
			@ApiResponse(
					responseCode = "404",
					description = "CEP nao encontrado na API externa",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ErroResponse.class),
							examples = @ExampleObject(
									name = "CEP nao encontrado",
									value = """
											{
											  "timestamp": "2026-07-02T21:30:00",
											  "status": 404,
											  "erro": "CEP nao encontrado",
											  "mensagem": "CEP nao encontrado: 99999999",
											  "path": "/api/ceps/99999999"
											}
											"""))),
			@ApiResponse(
					responseCode = "502",
					description = "Falha ao consultar a API externa de CEP",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ErroResponse.class))),
			@ApiResponse(
					responseCode = "500",
					description = "Erro inesperado na aplicacao",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ErroResponse.class),
							examples = @ExampleObject(
									name = "Erro interno",
									value = """
											{
											  "timestamp": "2026-07-02T21:30:00",
											  "status": 500,
											  "erro": "Erro interno",
											  "mensagem": "Ocorreu um erro inesperado. Tente novamente mais tarde.",
											  "path": "/api/ceps/01001000"
											}
											""")))
	})
	public ResponseEntity<CepResponse> consultar(
			@Parameter(description = "CEP com exatamente 8 digitos numericos.", example = "01001000", required = true)
			@PathVariable
			@Pattern(regexp = "\\d{8}", message = "CEP deve conter exatamente 8 digitos numericos")
			String cep) {
		return ResponseEntity.ok(consultaCepService.consultar(cep));
	}
}
