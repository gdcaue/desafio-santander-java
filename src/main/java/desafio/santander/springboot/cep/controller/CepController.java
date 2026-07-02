package desafio.santander.springboot.cep.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import desafio.santander.springboot.cep.dto.CepResponse;
import desafio.santander.springboot.cep.service.ConsultaCepService;
import jakarta.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping("/api/ceps")
public class CepController {

	private final ConsultaCepService consultaCepService;

	public CepController(ConsultaCepService consultaCepService) {
		this.consultaCepService = consultaCepService;
	}

	@GetMapping("/{cep}")
	public ResponseEntity<CepResponse> consultar(
			@PathVariable
			@Pattern(regexp = "\\d{8}", message = "CEP deve conter exatamente 8 digitos numericos")
			String cep) {
		return ResponseEntity.ok(consultaCepService.consultar(cep));
	}
}
