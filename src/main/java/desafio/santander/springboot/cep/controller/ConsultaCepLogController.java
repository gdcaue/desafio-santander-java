package desafio.santander.springboot.cep.controller;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import desafio.santander.springboot.cep.dto.ConsultaCepLogResponse;
import desafio.santander.springboot.cep.dto.PaginaResponse;
import desafio.santander.springboot.cep.service.ConsultaCepLogService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/api/consultas")
public class ConsultaCepLogController {

	private final ConsultaCepLogService consultaCepLogService;

	public ConsultaCepLogController(ConsultaCepLogService consultaCepLogService) {
		this.consultaCepLogService = consultaCepLogService;
	}

	@GetMapping
	public ResponseEntity<PaginaResponse<ConsultaCepLogResponse>> listar(
			@RequestParam(required = false)
			@Pattern(regexp = "\\d{8}", message = "CEP deve conter exatamente 8 digitos numericos")
			String cep,
			@RequestParam(required = false)
			Boolean success,
			@RequestParam(required = false)
			@DateTimeFormat(iso = ISO.DATE_TIME)
			LocalDateTime inicio,
			@RequestParam(required = false)
			@DateTimeFormat(iso = ISO.DATE_TIME)
			LocalDateTime fim,
			@RequestParam(defaultValue = "0")
			@Min(value = 0, message = "Pagina deve ser maior ou igual a zero")
			int page,
			@RequestParam(defaultValue = "10")
			@Min(value = 1, message = "Tamanho da pagina deve ser maior que zero")
			@Max(value = 100, message = "Tamanho da pagina deve ser menor ou igual a 100")
			int size) {
		return ResponseEntity.ok(PaginaResponse.from(consultaCepLogService.listar(cep, success, inicio, fim, page, size)));
	}
}
