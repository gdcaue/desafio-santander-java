CREATE TABLE consulta_cep_logs (
    id BIGSERIAL PRIMARY KEY,
    cep VARCHAR(8) NOT NULL,
    logradouro VARCHAR(255),
    bairro VARCHAR(255),
    localidade VARCHAR(255),
    uf VARCHAR(2),
    response_body TEXT NOT NULL,
    consulted_at TIMESTAMP NOT NULL
);
