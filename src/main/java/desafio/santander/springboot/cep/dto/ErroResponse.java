package desafio.santander.springboot.cep.dto;

import java.time.LocalDateTime;

public record ErroResponse(
		LocalDateTime timestamp,
		int status,
		String erro,
		String mensagem,
		String path
) {
}
