ALTER TABLE consulta_cep_logs
    ADD COLUMN http_status_code SMALLINT,
    ADD COLUMN success BOOLEAN,
    ADD COLUMN error_message VARCHAR(255);

UPDATE consulta_cep_logs
SET http_status_code = 200,
    success = TRUE
WHERE http_status_code IS NULL
  AND success IS NULL;

ALTER TABLE consulta_cep_logs
    ALTER COLUMN http_status_code SET NOT NULL,
    ALTER COLUMN success SET NOT NULL,
    ALTER COLUMN response_body DROP NOT NULL;
