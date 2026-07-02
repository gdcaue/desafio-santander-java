package desafio.santander.springboot.cep.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import desafio.santander.springboot.cep.domain.ConsultaCepLog;

public interface ConsultaCepLogRepository extends JpaRepository<ConsultaCepLog, Long> {
}
