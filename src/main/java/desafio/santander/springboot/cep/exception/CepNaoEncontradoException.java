package desafio.santander.springboot.cep.exception;

public class CepNaoEncontradoException extends RuntimeException {

	public CepNaoEncontradoException(String cep) {
		super("CEP nao encontrado: " + cep);
	}
}
