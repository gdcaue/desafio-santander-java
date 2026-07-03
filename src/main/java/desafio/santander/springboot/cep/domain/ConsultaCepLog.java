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

	@Column(name = "response_body", columnDefinition = "TEXT")
	private String responseBody;

	@Column(name = "http_status_code", nullable = false)
	private Short httpStatusCode;

	@Column(nullable = false)
	private Boolean success;

	@Column(name = "error_message", length = 255)
	private String errorMessage;

	@Column(name = "consulted_at", nullable = false)
	private LocalDateTime consultedAt;

	protected ConsultaCepLog() {
	}

	private ConsultaCepLog(
			String cep,
			String logradouro,
			String bairro,
			String localidade,
			String uf,
			String responseBody,
			int httpStatusCode,
			boolean success,
			String errorMessage,
			LocalDateTime consultedAt) {
		this.cep = cep;
		this.logradouro = logradouro;
		this.bairro = bairro;
		this.localidade = localidade;
		this.uf = uf;
		this.responseBody = responseBody;
		this.httpStatusCode = (short) httpStatusCode;
		this.success = success;
		this.errorMessage = errorMessage;
		this.consultedAt = consultedAt;
	}

	public static ConsultaCepLog success(CepResponse cepResponse, String responseBody, int httpStatusCode,
			LocalDateTime consultedAt) {
		return new ConsultaCepLog(
				cepResponse.cep(),
				cepResponse.logradouro(),
				cepResponse.bairro(),
				cepResponse.localidade(),
				cepResponse.uf(),
				responseBody,
				httpStatusCode,
				true,
				null,
				consultedAt);
	}

	public static ConsultaCepLog failure(String cep, String responseBody, int httpStatusCode, String errorMessage,
			LocalDateTime consultedAt) {
		return new ConsultaCepLog(
				cep,
				null,
				null,
				null,
				null,
				responseBody,
				httpStatusCode,
				false,
				errorMessage,
				consultedAt);
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

	public Short getHttpStatusCode() {
		return httpStatusCode;
	}

	public Boolean getSuccess() {
		return success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public LocalDateTime getConsultedAt() {
		return consultedAt;
	}
}
