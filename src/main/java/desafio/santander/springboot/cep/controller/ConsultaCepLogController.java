package desafio.santander.springboot.cep.controller;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import desafio.santander.springboot.cep.dto.ConsultaCepLogResponse;
import desafio.santander.springboot.cep.dto.ErroResponse;
import desafio.santander.springboot.cep.dto.PaginaResponse;
import desafio.santander.springboot.cep.service.ConsultaCepLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/api/consultas")
@Tag(name = "Historico")
public class ConsultaCepLogController {

	private final ConsultaCepLogService consultaCepLogService;

	public ConsultaCepLogController(ConsultaCepLogService consultaCepLogService) {
		this.consultaCepLogService = consultaCepLogService;
	}

	@GetMapping
	@Operation(
			summary = "Consultar historico de consultas",
			description = "Lista os logs de consultas de CEP com filtros opcionais por CEP, resultado da consulta, periodo e paginacao.")
	@ApiResponses({
			@ApiResponse(
					responseCode = "200",
					description = "Historico retornado com sucesso",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = PaginaResponse.class),
							examples = @ExampleObject(
									name = "Historico paginado",
									value = """
											{
											  "content": [
											    {
											      "id": 1,
											      "cep": "01001000",
											      "logradouro": "Praca da Se",
											      "bairro": "Se",
											      "localidade": "Sao Paulo",
											      "uf": "SP",
											      "httpStatusCode": 200,
											      "success": true,
											      "responseBody": "{\\"cep\\":\\"01001000\\",\\"logradouro\\":\\"Praca da Se\\"}",
											      "errorMessage": null,
											      "consultedAt": "2026-07-02T21:30:00"
											    },
											    {
											      "id": 2,
											      "cep": "99999999",
											      "logradouro": null,
											      "bairro": null,
											      "localidade": null,
											      "uf": null,
											      "httpStatusCode": 404,
											      "success": false,
											      "responseBody": "{\\"erro\\":\\"CEP nao encontrado\\"}",
											      "errorMessage": "CEP nao encontrado: 99999999",
											      "consultedAt": "2026-07-02T21:31:00"
											    }
											  ],
											  "page": 0,
											  "size": 10,
											  "totalElements": 2,
											  "totalPages": 1
											}
											"""))),
			@ApiResponse(
					responseCode = "400",
					description = "Filtros informados em formato invalido",
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = ErroResponse.class),
							examples = @ExampleObject(
									name = "Periodo invalido",
									value = """
											{
											  "timestamp": "2026-07-02T21:30:00",
											  "status": 400,
											  "erro": "Requisicao invalida",
											  "mensagem": "Parametro inicio deve ser anterior ou igual ao parametro fim",
											  "path": "/api/consultas"
											}
											"""))),
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
											  "path": "/api/consultas"
											}
											""")))
	})
	public ResponseEntity<PaginaResponse<ConsultaCepLogResponse>> listar(
			@Parameter(description = "Filtra pelo CEP consultado. Deve conter exatamente 8 digitos numericos.", example = "01001000")
			@RequestParam(required = false)
			@Pattern(regexp = "\\d{8}", message = "CEP deve conter exatamente 8 digitos numericos")
			String cep,
			@Parameter(description = "Filtra por resultado da consulta externa. Use true para sucesso e false para falha.", example = "false")
			@RequestParam(required = false)
			Boolean success,
			@Parameter(description = "Data e hora inicial do periodo consultado, no formato ISO-8601.", example = "2026-07-02T00:00:00")
			@RequestParam(required = false)
			@DateTimeFormat(iso = ISO.DATE_TIME)
			LocalDateTime inicio,
			@Parameter(description = "Data e hora final do periodo consultado, no formato ISO-8601.", example = "2026-07-02T23:59:59")
			@RequestParam(required = false)
			@DateTimeFormat(iso = ISO.DATE_TIME)
			LocalDateTime fim,
			@Parameter(description = "Numero da pagina. A primeira pagina e 0.", example = "0")
			@RequestParam(defaultValue = "0")
			@Min(value = 0, message = "Pagina deve ser maior ou igual a zero")
			int page,
			@Parameter(description = "Quantidade de registros por pagina. Valor permitido entre 1 e 100.", example = "10")
			@RequestParam(defaultValue = "10")
			@Min(value = 1, message = "Tamanho da pagina deve ser maior que zero")
			@Max(value = 100, message = "Tamanho da pagina deve ser menor ou igual a 100")
			int size) {
		return ResponseEntity.ok(PaginaResponse.from(consultaCepLogService.listar(cep, success, inicio, fim, page, size)));
	}
}
