
CREATE TABLE public.carrinho_entity_produtos
(
    carrinho_entity_id INTEGER NOT NULL,
    id INTEGER,
    quantidade INTEGER NOT NULL,
    FOREIGN KEY (carrinho_entity_id) REFERENCES public.carrinho_entity (id)
);