package desafio.santander.springboot.cep.exception;

public class ErroIntegracaoCepException extends RuntimeException {

	public ErroIntegracaoCepException(String message, Throwable cause) {
		super(message, cause);
	}
}
