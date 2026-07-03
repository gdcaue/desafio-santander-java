package desafio.santander.springboot.cep.exception;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import desafio.santander.springboot.cep.dto.ErroResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(CepNaoEncontradoException.class)
	public ResponseEntity<ErroResponse> handleCepNaoEncontrado(CepNaoEncontradoException exception,
			HttpServletRequest request) {
		return buildResponse(HttpStatus.NOT_FOUND, "CEP nao encontrado", exception.getMessage(), request);
	}

	@ExceptionHandler(FiltroConsultaInvalidoException.class)
	public ResponseEntity<ErroResponse> handleFiltroConsultaInvalido(FiltroConsultaInvalidoException exception,
			HttpServletRequest request) {
		return buildResponse(HttpStatus.BAD_REQUEST, "Requisicao invalida", exception.getMessage(), request);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErroResponse> handleConstraintViolation(ConstraintViolationException exception,
			HttpServletRequest request) {
		String mensagem = exception.getConstraintViolations()
				.stream()
				.map(violation -> violation.getMessage())
				.filter(Objects::nonNull)
				.distinct()
				.collect(Collectors.joining("; "));
		return buildResponse(HttpStatus.BAD_REQUEST, "Requisicao invalida", mensagem, request);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErroResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
			HttpServletRequest request) {
		String mensagem = exception.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(this::formatarCampoInvalido)
				.distinct()
				.collect(Collectors.joining("; "));
		return buildResponse(HttpStatus.BAD_REQUEST, "Requisicao invalida", mensagem, request);
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<ErroResponse> handleHandlerMethodValidation(HandlerMethodValidationException exception,
			HttpServletRequest request) {
		String mensagem = exception.getParameterValidationResults()
				.stream()
				.flatMap(result -> result.getResolvableErrors().stream())
				.map(MessageSourceResolvable::getDefaultMessage)
				.filter(Objects::nonNull)
				.distinct()
				.collect(Collectors.joining("; "));
		return buildResponse(HttpStatus.BAD_REQUEST, "Requisicao invalida", mensagem, request);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErroResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception,
			HttpServletRequest request) {
		String mensagem = "Parametro '" + exception.getName() + "' possui formato invalido";
		return buildResponse(HttpStatus.BAD_REQUEST, "Requisicao invalida", mensagem, request);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErroResponse> handleMissingServletRequestParameter(
			MissingServletRequestParameterException exception, HttpServletRequest request) {
		String mensagem = "Parametro obrigatorio ausente: " + exception.getParameterName();
		return buildResponse(HttpStatus.BAD_REQUEST, "Requisicao invalida", mensagem, request);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErroResponse> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {
		String mensagem = "Metodo HTTP nao suportado para este endpoint";
		return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, "Metodo nao permitido", mensagem, request);
	}

	@ExceptionHandler(ErroIntegracaoCepException.class)
	public ResponseEntity<ErroResponse> handleErroIntegracao(ErroIntegracaoCepException exception,
			HttpServletRequest request) {
		return buildResponse(HttpStatus.BAD_GATEWAY, "Erro de integracao", exception.getMessage(), request);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErroResponse> handleNoResourceFound(NoResourceFoundException exception,
			HttpServletRequest request) {
		return buildResponse(HttpStatus.NOT_FOUND, "Recurso nao encontrado", "Endpoint nao encontrado", request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErroResponse> handleErroInesperado(Exception exception, HttpServletRequest request) {
		LOGGER.error("Erro inesperado ao processar requisicao {}", request.getRequestURI(), exception);
		return buildResponse(
				HttpStatus.INTERNAL_SERVER_ERROR,
				"Erro interno",
				"Ocorreu um erro inesperado. Tente novamente mais tarde.",
				request);
	}

	private String formatarCampoInvalido(FieldError fieldError) {
		String mensagem = fieldError.getDefaultMessage();
		if (mensagem == null || mensagem.isBlank()) {
			mensagem = "valor invalido";
		}
		return fieldError.getField() + ": " + mensagem;
	}

	private ResponseEntity<ErroResponse> buildResponse(HttpStatus status, String erro, String mensagem,
			HttpServletRequest request) {
		ErroResponse response = new ErroResponse(
				LocalDateTime.now(),
				status.value(),
				erro,
				normalizarMensagem(mensagem),
				request.getRequestURI());
		return ResponseEntity.status(status).body(response);
	}

	private String normalizarMensagem(String mensagem) {
		if (mensagem == null || mensagem.isBlank()) {
			return "Nao foi possivel processar a requisicao.";
		}
		return mensagem;
	}
}
