package desafio.santander.springboot.cep.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import desafio.santander.springboot.cep.dto.ErroResponse;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CepNaoEncontradoException.class)
	public ResponseEntity<ErroResponse> handleCepNaoEncontrado(CepNaoEncontradoException exception) {
		return buildResponse(HttpStatus.NOT_FOUND, "CEP nao encontrado", exception.getMessage());
	}

	@ExceptionHandler({
			ConstraintViolationException.class,
			MethodArgumentNotValidException.class,
			HandlerMethodValidationException.class,
			MethodArgumentTypeMismatchException.class,
			FiltroConsultaInvalidoException.class
	})
	public ResponseEntity<ErroResponse> handleValidacao(Exception exception) {
		String mensagem = exception instanceof FiltroConsultaInvalidoException
				? exception.getMessage()
				: "Parametros da requisicao invalidos";
		return buildResponse(HttpStatus.BAD_REQUEST, "Requisicao invalida", mensagem);
	}

	@ExceptionHandler(ErroIntegracaoCepException.class)
	public ResponseEntity<ErroResponse> handleErroIntegracao(ErroIntegracaoCepException exception) {
		return buildResponse(HttpStatus.BAD_GATEWAY, "Erro de integracao", exception.getMessage());
	}

	private ResponseEntity<ErroResponse> buildResponse(HttpStatus status, String erro, String mensagem) {
		ErroResponse response = new ErroResponse(LocalDateTime.now(), status.value(), erro, mensagem);
		return ResponseEntity.status(status).body(response);
	}
}
