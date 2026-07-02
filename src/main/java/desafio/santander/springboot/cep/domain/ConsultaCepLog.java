package desafio.santander.springboot.cep.domain;

import java.time.LocalDateTime;

import desafio.santander.springboot.cep.dto.CepResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "consulta_cep_logs")
public class ConsultaCepLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 8)
	private String cep;

	@Column(length = 255)
	private String logradouro;

	@Column(length = 255)
	private String bairro;

	@Column(length = 255)
	private String localidade;

	@Column(length = 2)
	private String uf;

	@Column(name = "response_body", nullable = false, columnDefinition = "TEXT")
	private String responseBody;

	@Column(name = "consulted_at", nullable = false)
	private LocalDateTime consultedAt;

	protected ConsultaCepLog() {
	}

	private ConsultaCepLog(CepResponse cepResponse, String responseBody, LocalDateTime consultedAt) {
		this.cep = cepResponse.cep();
		this.logradouro = cepResponse.logradouro();
		this.bairro = cepResponse.bairro();
		this.localidade = cepResponse.localidade();
		this.uf = cepResponse.uf();
		this.responseBody = responseBody;
		this.consultedAt = consultedAt;
	}

	public static ConsultaCepLog from(CepResponse cepResponse, String responseBody, LocalDateTime consultedAt) {
		return new ConsultaCepLog(cepResponse, responseBody, consultedAt);
	}

	public Long getId() {
		return id;
	}

	public String getCep() {
		return cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public String getLocalidade() {
		return localidade;
	}

	public String getUf() {
		return uf;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public LocalDateTime getConsultedAt() {
		return consultedAt;
	}
}
