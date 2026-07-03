package desafio.santander.springboot.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI consultaCepOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("ConsultaCEP API")
						.version("v1")
						.description("API REST para consulta de CEP em uma API externa mockada e registro do historico em PostgreSQL."))
				.tags(List.of(
						new Tag()
								.name("CEPs")
								.description("Operacoes para consultar dados de endereco por CEP."),
						new Tag()
								.name("Historico")
								.description("Operacoes para consultar os logs das buscas realizadas.")));
	}
}
