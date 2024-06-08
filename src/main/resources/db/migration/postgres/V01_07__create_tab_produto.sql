

CREATE TABLE public.produto_entity
(
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(255),
    detalhes VARCHAR(255),
    estoque VARCHAR(255),
    marca VARCHAR(255),
    nome VARCHAR(255),
    preco REAL
);