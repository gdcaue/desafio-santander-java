package desafio.santander.springboot.cep.exception;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import desafio.santander.springboot.cep.controller.CepController;
import desafio.santander.springboot.cep.controller.ConsultaCepLogController;
import desafio.santander.springboot.cep.service.ConsultaCepLogService;
import desafio.santander.springboot.cep.service.ConsultaCepService;

@WebMvcTest(controllers = { CepController.class, ConsultaCepLogController.class })
class GlobalExceptionHandlerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private ConsultaCepService consultaCepService;

	@MockitoBean
	private ConsultaCepLogService consultaCepLogService;

	@Test
	void deveRetornarErroPadronizadoQuandoCepTemFormatoInvalido() throws Exception {
		mockMvc.perform(get("/api/ceps/abc"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.erro").value("Requisicao invalida"))
				.andExpect(jsonPath("$.mensagem").value(containsString("CEP deve conter exatamente 8 digitos numericos")))
				.andExpect(jsonPath("$.path").value("/api/ceps/abc"))
				.andExpect(jsonPath("$.trace").doesNotExist())
				.andExpect(jsonPath("$.exception").doesNotExist());
	}

	@Test
	void deveRetornarErroPadronizadoQuandoFiltroDePeriodoEstaInvalido() throws Exception {
		when(consultaCepLogService.listar(
				eq(null),
				eq(null),
				any(LocalDateTime.class),
				any(LocalDateTime.class),
				eq(0),
				eq(10)))
				.thenThrow(new FiltroConsultaInvalidoException(
						"Parametro inicio deve ser anterior ou igual ao parametro fim"));

		mockMvc.perform(get("/api/consultas")
				.param("inicio", "2026-07-03T00:00:00")
				.param("fim", "2026-07-02T00:00:00"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.erro").value("Requisicao invalida"))
				.andExpect(jsonPath("$.mensagem").value("Parametro inicio deve ser anterior ou igual ao parametro fim"))
				.andExpect(jsonPath("$.path").value("/api/consultas"))
				.andExpect(jsonPath("$.trace").doesNotExist())
				.andExpect(jsonPath("$.exception").doesNotExist());
	}

	@Test
	void deveRetornarErroPadronizadoQuandoEndpointNaoExiste() throws Exception {
		mockMvc.perform(get("/api/inexistente"))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.erro").value("Recurso nao encontrado"))
				.andExpect(jsonPath("$.mensagem").value("Endpoint nao encontrado"))
				.andExpect(jsonPath("$.path").value("/api/inexistente"))
				.andExpect(jsonPath("$.trace").doesNotExist())
				.andExpect(jsonPath("$.exception").doesNotExist());
	}

	@Test
	void deveRetornarErroPadronizadoQuandoMetodoHttpNaoEPermitido() throws Exception {
		mockMvc.perform(post("/api/ceps/01001000"))
				.andExpect(status().isMethodNotAllowed())
				.andExpect(jsonPath("$.status").value(405))
				.andExpect(jsonPath("$.erro").value("Metodo nao permitido"))
				.andExpect(jsonPath("$.mensagem").value("Metodo HTTP nao suportado para este endpoint"))
				.andExpect(jsonPath("$.path").value("/api/ceps/01001000"))
				.andExpect(jsonPath("$.trace").doesNotExist())
				.andExpect(jsonPath("$.exception").doesNotExist());
	}
}
