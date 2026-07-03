package desafio.santander.springboot.cep.exception;

public class ErroIntegracaoCepException extends RuntimeException {

	private final String cep;
	private final int httpStatusCode;
	private final String responseBody;

	public ErroIntegracaoCepException(String cep, int httpStatusCode, String responseBody, String message, Throwable cause) {
		super(message, cause);
		this.cep = cep;
		this.httpStatusCode = httpStatusCode;
		this.responseBody = responseBody;
	}

	public String getCep() {
		return cep;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public String getResponseBody() {
		return responseBody;
	}
}
