INSERT INTO produto_entity (nome, marca, preco, estoque, descricao, detalhes)
VALUES
    ('produto 1', 'marca 1', 10, 'DISPONIVEL', 'descrição do produto 1', 'detalhe do produto 1'),
    ('produto 2', 'marca 2', 20, 'DISPONIVEL', 'descrição do produto 2', 'detalhe do produto 2'),
    ('produto 3', 'marca 3', 25.5, 'DISPONIVEL', 'descrição do produto 3', 'detalhe do produto 3'),
    ('produto 4', 'marca 4', 3.4, 'DISPONIVEL', 'descrição do produto 4', 'detalhe do produto 4');


INSERT INTO manager_entity(
    cpf, email, nome, role, senha)
VALUES (78524937009, 'manager@email.com', 'teste manager', 'MANAGER', '$2a$10$mjVwAllGx8n4J.1iYRZ.auKou5n476kkpllX1Qp4UZfoUwq.Eakze');