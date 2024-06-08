ALTER TABLE IF EXISTS public.carrinho_entity_produtos
    ADD CONSTRAINT fkhx81dk2dgppjg85i27qp4cf82 FOREIGN KEY (carrinho_entity_id)
        REFERENCES public.carrinho_entity (id) ON DELETE NO ACTION;

ALTER TABLE IF EXISTS public.compra_efetuada_entity
    ADD CONSTRAINT fkkhcvk0du4pssvs5i33etjuvx2 FOREIGN KEY (cliente_id)
        REFERENCES public.cliente_entity (id) ON DELETE NO ACTION;

ALTER TABLE IF EXISTS public.compra_efetuada_entity
    ADD CONSTRAINT fkkx6frr64km28g18i5qm3l8v8k FOREIGN KEY (carrinho_entity_id)
        REFERENCES public.carrinho_entity (id) ON DELETE NO ACTION;

ALTER TABLE IF EXISTS public.produto_entity_image_paths
    ADD CONSTRAINT fkqylrynx1ul4ixkq4px4wb3l6r FOREIGN KEY (produto_entity_id)
        REFERENCES public.produto_entity (id) ON DELETE NO ACTION;
