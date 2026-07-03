package desafio.santander.springboot.cep.controller;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import desafio.santander.springboot.cep.client.CepClient;
import desafio.santander.springboot.cep.client.CepClientResponse;
import desafio.santander.springboot.cep.domain.ConsultaCepLog;
import desafio.santander.springboot.cep.dto.CepResponse;
import desafio.santander.springboot.cep.exception.CepNaoEncontradoException;
import desafio.santander.springboot.cep.repository.ConsultaCepLogRepository;

@SpringBootTest
@AutoConfigureMockMvc
class ConsultaCepApiIntegrationTest {

	private static final String TEST_MARKER = "teste-automatizado-consulta-cep";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ConsultaCepLogRepository consultaCepLogRepository;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@MockitoBean
	private CepClient cepClient;

	@BeforeEach
	void setUp() {
		limparLogsDeTeste();
		reset(cepClient);
	}

	@AfterEach
	void tearDown() {
		limparLogsDeTeste();
	}

	@Test
	void deveConsultarCepComSucessoESalvarLog() throws Exception {
		CepResponse cepResponse = new CepResponse(
				"01001000",
				"Praca da Se",
				"Se",
				"Sao Paulo",
				"SP");
		String responseBody = "{\"cep\":\"01001000\",\"logradouro\":\"Praca da Se\",\"origem\":\""
				+ TEST_MARKER + "\"}";

		when(cepClient.buscarPorCep("01001000"))
				.thenReturn(new CepClientResponse(cepResponse, responseBody, 200));

		mockMvc.perform(get("/api/ceps/01001000"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.cep").value("01001000"))
				.andExpect(jsonPath("$.logradouro").value("Praca da Se"))
				.andExpect(jsonPath("$.bairro").value("Se"))
				.andExpect(jsonPath("$.localidade").value("Sao Paulo"))
				.andExpect(jsonPath("$.uf").value("SP"));

		List<ConsultaCepLog> logs = buscarLogsDeTeste();
		assertEquals(1, logs.size());

		ConsultaCepLog log = logs.get(0);
		assertEquals("01001000", log.getCep());
		assertEquals("Praca da Se", log.getLogradouro());
		assertEquals(Short.valueOf((short) 200), log.getHttpStatusCode());
		assertTrue(log.getSuccess());
		assertTrue(log.getResponseBody().contains(TEST_MARKER));
		assertNotNull(log.getConsultedAt());

		verify(cepClient).buscarPorCep("01001000");
		verifyNoMoreInteractions(cepClient);
	}

	@Test
	void deveRetornarNotFoundESalvarLogDeFalhaQuandoCepNaoExiste() throws Exception {
		String responseBody = "{\"erro\":\"CEP nao encontrado\",\"origem\":\"" + TEST_MARKER + "\"}";

		when(cepClient.buscarPorCep("99999999"))
				.thenThrow(new CepNaoEncontradoException("99999999", 404, responseBody));

		mockMvc.perform(get("/api/ceps/99999999"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.erro").value("CEP nao encontrado"))
				.andExpect(jsonPath("$.path").value("/api/ceps/99999999"));

		List<ConsultaCepLog> logs = buscarLogsDeTeste();
		assertEquals(1, logs.size());

		ConsultaCepLog log = logs.get(0);
		assertEquals("99999999", log.getCep());
		assertEquals(Short.valueOf((short) 404), log.getHttpStatusCode());
		assertFalse(log.getSuccess());
		assertTrue(log.getResponseBody().contains(TEST_MARKER));
		assertEquals("CEP nao encontrado: 99999999", log.getErrorMessage());
		assertNotNull(log.getConsultedAt());

		verify(cepClient).buscarPorCep("99999999");
		verifyNoMoreInteractions(cepClient);
	}

	@Test
	void deveRetornarBadRequestENaoConsultarApiExternaQuandoCepTemFormatoInvalido() throws Exception {
		long totalLogsAntes = consultaCepLogRepository.count();

		mockMvc.perform(get("/api/ceps/abc"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.erro").value("Requisicao invalida"))
				.andExpect(jsonPath("$.path").value("/api/ceps/abc"));

		assertEquals(totalLogsAntes, consultaCepLogRepository.count());
		verifyNoInteractions(cepClient);
	}

	@Test
	void deveFiltrarHistoricoPorConsultasComFalha() throws Exception {
		ConsultaCepLog logSucesso = ConsultaCepLog.success(
				new CepResponse("77777777", "Rua Teste", "Centro", "Sao Paulo", "SP"),
				"{\"origem\":\"" + TEST_MARKER + "\",\"tipo\":\"sucesso\"}",
				200,
				LocalDateTime.of(2099, 1, 4, 10, 0));
		ConsultaCepLog logFalha = ConsultaCepLog.failure(
				"88888888",
				"{\"origem\":\"" + TEST_MARKER + "\",\"tipo\":\"falha\"}",
				404,
				"CEP nao encontrado: 88888888",
				LocalDateTime.of(2099, 1, 3, 10, 0));

		consultaCepLogRepository.save(logSucesso);
		consultaCepLogRepository.save(logFalha);

		mockMvc.perform(get("/api/consultas")
				.param("success", "false")
				.param("size", "10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[*].success", everyItem(is(false))))
				.andExpect(jsonPath("$.content[*].cep", hasItem("88888888")))
				.andExpect(jsonPath("$.content[*].cep", not(hasItem("77777777"))));

		verifyNoInteractions(cepClient);
	}

	private List<ConsultaCepLog> buscarLogsDeTeste() {
		return consultaCepLogRepository.findAll()
				.stream()
				.filter(log -> contemMarcador(log.getResponseBody()))
				.toList();
	}

	private boolean contemMarcador(String value) {
		return value != null && value.contains(TEST_MARKER);
	}

	private void limparLogsDeTeste() {
		jdbcTemplate.update("delete from consulta_cep_logs where response_body like ?", "%" + TEST_MARKER + "%");
	}
}
