package desafio.santander.springboot.cep.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import desafio.santander.springboot.cep.domain.ConsultaCepLog;
import desafio.santander.springboot.cep.dto.ConsultaCepLogResponse;
import desafio.santander.springboot.cep.exception.FiltroConsultaInvalidoException;
import desafio.santander.springboot.cep.repository.ConsultaCepLogRepository;
import jakarta.persistence.criteria.Predicate;

@Service
public class ConsultaCepLogService {

	private final ConsultaCepLogRepository consultaCepLogRepository;

	public ConsultaCepLogService(ConsultaCepLogRepository consultaCepLogRepository) {
		this.consultaCepLogRepository = consultaCepLogRepository;
	}

	@Transactional(readOnly = true)
	public List<ConsultaCepLogResponse> listar(String cep, Boolean success, LocalDateTime inicio, LocalDateTime fim) {
		validarPeriodo(inicio, fim);

		Sort ordenacao = Sort.by(Sort.Direction.DESC, "consultedAt")
				.and(Sort.by(Sort.Direction.DESC, "id"));

		return consultaCepLogRepository.findAll(filtrarPor(cep, success, inicio, fim), ordenacao)
				.stream()
				.map(ConsultaCepLogResponse::from)
				.toList();
	}

	private Specification<ConsultaCepLog> filtrarPor(String cep, Boolean success, LocalDateTime inicio,
			LocalDateTime fim) {
		return (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();

			if (cep != null) {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("cep"), cep));
			}

			if (success != null) {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("success"), success));
			}

			if (inicio != null) {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("consultedAt"), inicio));
			}

			if (fim != null) {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("consultedAt"), fim));
			}

			return predicate;
		};
	}

	private void validarPeriodo(LocalDateTime inicio, LocalDateTime fim) {
		if (inicio != null && fim != null && inicio.isAfter(fim)) {
			throw new FiltroConsultaInvalidoException("Parametro inicio deve ser anterior ou igual ao parametro fim");
		}
	}
}
