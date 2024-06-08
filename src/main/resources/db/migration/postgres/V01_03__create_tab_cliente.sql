

CREATE TABLE public.cliente_entity
(
    id SERIAL PRIMARY KEY,
    cpf_cnpj VARCHAR(255),
    customer_id VARCHAR(255),
    email VARCHAR(255),
    mobile_phone VARCHAR(255),
    name VARCHAR(255),
    role VARCHAR(255),
    senha VARCHAR(255)
);