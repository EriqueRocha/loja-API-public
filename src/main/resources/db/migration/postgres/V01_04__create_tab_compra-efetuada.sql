

CREATE TABLE public.compra_efetuada_entity
(
    id SERIAL PRIMARY KEY,
    billing_type VARCHAR(255),
    id_pay VARCHAR(255),
    link_payment VARCHAR(255),
    status VARCHAR(255),
    status_payment VARCHAR(255),
    value REAL,
    carrinho_entity_id INTEGER,
    cliente_id INTEGER,
    FOREIGN KEY (carrinho_entity_id) REFERENCES public.carrinho_entity (id),
    FOREIGN KEY (cliente_id) REFERENCES public.cliente_entity (id)
);