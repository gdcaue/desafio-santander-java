package desafio.santander.springboot.cep.client;

public interface CepClient {

	CepClientResponse buscarPorCep(String cep);
}
