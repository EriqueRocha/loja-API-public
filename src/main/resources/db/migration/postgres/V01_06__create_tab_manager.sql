

CREATE TABLE public.manager_entity
(
    id SERIAL PRIMARY KEY,
    cpf VARCHAR(255),
    email VARCHAR(255),
    nome VARCHAR(255),
    role VARCHAR(255),
    senha VARCHAR(255)
);