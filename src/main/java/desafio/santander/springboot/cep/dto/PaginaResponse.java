package desafio.santander.springboot.cep.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta paginada da API.")
public record PaginaResponse<T>(
		@Schema(description = "Itens da pagina atual.")
		List<T> content,
		@Schema(description = "Numero da pagina retornada. A primeira pagina e 0.", example = "0")
		int page,
		@Schema(description = "Quantidade de registros solicitada por pagina.", example = "10")
		int size,
		@Schema(description = "Quantidade total de registros encontrados para os filtros informados.", example = "25")
		long totalElements,
		@Schema(description = "Quantidade total de paginas disponiveis.", example = "3")
		int totalPages
) {

	public static <T> PaginaResponse<T> from(Page<T> page) {
		return new PaginaResponse<>(
				page.getContent(),
				page.getNumber(),
				page.getSize(),
				page.getTotalElements(),
				page.getTotalPages());
	}
}
