

CREATE TABLE public.produto_entity_image_paths
(
    produto_entity_id INTEGER NOT NULL,
    image_paths VARCHAR(255),
    FOREIGN KEY (produto_entity_id) REFERENCES public.produto_entity (id)
);