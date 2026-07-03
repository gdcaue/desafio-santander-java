package desafio.santander.springboot.cep.exception;

public class CepNaoEncontradoException extends RuntimeException {

	private final String cep;
	private final int httpStatusCode;
	private final String responseBody;

	public CepNaoEncontradoException(String cep, int httpStatusCode, String responseBody) {
		super("CEP nao encontrado: " + cep);
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
