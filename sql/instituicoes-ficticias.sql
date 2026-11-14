-- DADOS ILUSTRATIVOS EM SAO PAULO. AS ORGANIZACOES NAO EXISTEM.
-- Executar depois de sql/schema-oracle.sql. Apenas UMA vez.

INSERT INTO RG_INSTITUICAO (ID, NOME, DESCRICAO, BAIRRO, LATITUDE, LONGITUDE)
VALUES (RG_INSTITUICAO_SEQ.NEXTVAL, 'Instituto Ponte Solidaria',
        'Organizacao ficticia para a demonstracao da RecicleGlobal.',
        'Bela Vista', -23.5595000, -46.6489000);

INSERT INTO RG_INSTITUICAO (ID, NOME, DESCRICAO, BAIRRO, LATITUDE, LONGITUDE)
VALUES (RG_INSTITUICAO_SEQ.NEXTVAL, 'Casa do Bem SP',
        'Organizacao ficticia que recebe roupas e alimentos dentro da validade.',
        'Vila Mariana', -23.5863000, -46.6325000);

INSERT INTO RG_INSTITUICAO (ID, NOME, DESCRICAO, BAIRRO, LATITUDE, LONGITUDE)
VALUES (RG_INSTITUICAO_SEQ.NEXTVAL, 'Centro Comunitario Novo Horizonte',
        'Organizacao ficticia representada apenas para fins academicos.',
        'Se', -23.5509000, -46.6333000);

COMMIT;
