# RecicleGlobal API

API REST para gerenciamento de doações de roupas e alimentos, conectando doadores a pessoas interessadas na retirada dos itens. A aplicação também disponibiliza instituições de apoio com coordenadas geográficas para consulta em mapa.

**API em produção:** https://recicleglobal.onrender.com  
**Health check:** https://recicleglobal.onrender.com/api/status

## Visão geral

A RecicleGlobal centraliza anúncios de doação e o processo de solicitação, reserva e conclusão de entregas. Cada anúncio possui categoria, descrição, quantidade, localização aproximada e um estado que representa sua disponibilidade.

O fluxo de retirada é controlado por regras de negócio que impedem solicitações da própria doação, reservas conflitantes e a disponibilização de alimentos vencidos.

### Funcionalidades

- Cadastro e gerenciamento de usuários.
- Cadastro de instituições com bairro, descrição, latitude e longitude.
- Publicação, consulta, atualização e exclusão de anúncios de doação.
- Busca de doações por categoria e status.
- Solicitação, aceite, cancelamento e conclusão de retiradas.
- Validações de dados, regras de negócio e respostas de erro padronizadas.

## Stack

| Componente | Tecnologia |
| --- | --- |
| Linguagem | Java 21 |
| Framework | Spring Boot 3.5.16 |
| API | Spring Web / REST |
| Persistência | Spring Data JPA / Hibernate |
| Banco de dados | Oracle Database |
| Validação | Jakarta Bean Validation |
| Build | Maven |
| Containerização | Docker |
| Hospedagem | Render |

## Arquitetura

O código separa a interface HTTP, as regras de negócio e a persistência de dados:

```text
src/main/java/br/com/fiap/recicleglobal/
├── controller/   Endpoints REST
├── dto/          Contratos de entrada e saída
├── bo/           Regras de negócio e transações
├── dao/          Repositórios Spring Data JPA
├── model/        Entidades e enumerações
├── exception/    Tratamento global de erros
└── config/       Configuração de CORS
```

O modelo de dados utiliza quatro tabelas principais:

- `RG_USUARIO`: dados de identificação dos usuários.
- `RG_INSTITUICAO`: instituições e suas coordenadas geográficas.
- `RG_DOACAO`: anúncios associados ao usuário doador.
- `RG_SOLICITACAO`: pedidos associados à doação e ao usuário interessado.

As tabelas, sequences, restrições e índices estão definidos em [`sql/schema-oracle.sql`](sql/schema-oracle.sql).

## Endpoints

O prefixo das rotas de negócio é `/api`. Os endpoints recebem e retornam JSON.

### Serviço

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/` | Informações e rotas disponíveis da API |
| `GET` | `/api/status` | Estado do serviço |

### Usuários

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/api/usuarios` | Lista os usuários |
| `GET` | `/api/usuarios/{id}` | Consulta um usuário |
| `POST` | `/api/usuarios` | Cadastra um usuário |
| `PUT` | `/api/usuarios/{id}` | Atualiza um usuário |
| `DELETE` | `/api/usuarios/{id}` | Exclui um usuário sem registros vinculados |

### Instituições

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/api/instituicoes` | Lista as instituições |
| `GET` | `/api/instituicoes/{id}` | Consulta uma instituição |
| `POST` | `/api/instituicoes` | Cadastra uma instituição |
| `PUT` | `/api/instituicoes/{id}` | Atualiza uma instituição |
| `DELETE` | `/api/instituicoes/{id}` | Exclui uma instituição |

### Doações

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/api/doacoes` | Lista as doações; aceita filtros opcionais `categoria` e `status` |
| `GET` | `/api/doacoes/{id}` | Consulta uma doação |
| `POST` | `/api/doacoes` | Publica uma doação |
| `PUT` | `/api/doacoes/{id}` | Atualiza uma doação disponível |
| `DELETE` | `/api/doacoes/{id}` | Exclui uma doação sem solicitações vinculadas |

### Solicitações

| Método | Rota | Descrição |
| --- | --- | --- |
| `GET` | `/api/solicitacoes` | Lista as solicitações; aceita o filtro opcional `doacaoId` |
| `POST` | `/api/solicitacoes` | Solicita a retirada de uma doação |
| `PUT` | `/api/solicitacoes/{id}/aceitar` | Aceita a solicitação e reserva a doação |
| `PUT` | `/api/solicitacoes/{id}/concluir` | Conclui a entrega |
| `PUT` | `/api/solicitacoes/{id}/cancelar` | Cancela uma solicitação pendente ou aceita |

**Valores aceitos nos filtros:**

- `categoria`: `ROUPA`, `ALIMENTO`.
- `status` da doação: `DISPONIVEL`, `RESERVADA`, `CONCLUIDA`.

## Exemplos de uso

### Criar usuário

`POST /api/usuarios`

```json
{
  "nome": "Ana Silva",
  "email": "ana.silva@example.com"
}
```

### Publicar uma doação

`POST /api/doacoes`

```json
{
  "usuarioId": 1,
  "categoria": "ROUPA",
  "titulo": "Casacos em bom estado",
  "descricao": "Três casacos adultos disponíveis para doação",
  "quantidade": 3,
  "dataValidade": null,
  "bairro": "Vila Mariana",
  "latitude": -23.5863,
  "longitude": -46.6325
}
```

### Consultar doações disponíveis

`GET /api/doacoes?categoria=ROUPA&status=DISPONIVEL`

### Solicitar a retirada

`POST /api/solicitacoes`

```json
{
  "doacaoId": 1,
  "interessadoId": 2
}
```

Os identificadores são gerados pelo banco de dados. Nos exemplos, devem ser substituídos pelos IDs retornados pela API.

## Regras de negócio

O ciclo de uma doação é `DISPONIVEL` → `RESERVADA` → `CONCLUIDA`.

- Alimentos exigem data de validade igual ou posterior à data atual. A validade também é verificada durante a solicitação, o aceite e a conclusão.
- Roupas não possuem data de validade.
- O doador não pode solicitar o próprio anúncio.
- Uma solicitação aceita reserva o anúncio e recusa as demais solicitações pendentes da mesma doação.
- O cancelamento de uma solicitação aceita devolve o anúncio ao estado `DISPONIVEL`.
- A edição de anúncios é permitida somente enquanto estiverem disponíveis.

Erros são retornados em JSON. A API utiliza `400` para entrada inválida ou violação de regra de negócio, `404` para recursos inexistentes e `409` para conflitos de integridade no banco de dados.

## Execução local

### Requisitos

- JDK 21.
- Maven.
- Acesso a uma instância do Oracle Database.

### Banco de dados

Execute os scripts na conta Oracle utilizada pela aplicação, nesta ordem:

1. [`sql/schema-oracle.sql`](sql/schema-oracle.sql) — estrutura do banco.
2. [`sql/instituicoes-ficticias.sql`](sql/instituicoes-ficticias.sql) — carga opcional de instituições de exemplo.

Os scripts foram preparados para execução única, não para reaplicação automática.

### Variáveis de ambiente

| Variável | Descrição |
| --- | --- |
| `DB_URL` | URL JDBC de conexão com o Oracle |
| `DB_USERNAME` | Usuário do banco |
| `DB_PASSWORD` | Senha do banco |
| `PORT` | Porta HTTP; padrão `8080` |
| `FRONTEND_ORIGIN` | Origem permitida pelo CORS; padrão `http://localhost:5500` |

Exemplo de URL com Service Name:

```text
jdbc:oracle:thin:@//servidor:1521/servico
```

Um modelo das variáveis está disponível em [`.env.example`](.env.example). Configure os valores no ambiente de execução; não inclua credenciais reais no repositório.

### Inicialização

Com as variáveis de ambiente configuradas:

```bash
mvn spring-boot:run
```

A API estará disponível em `http://localhost:8080`, com verificação de status em `http://localhost:8080/api/status`.

### Testes

```bash
mvn test
```

Os testes de regras de negócio estão em `src/test/java/br/com/fiap/recicleglobal/bo/DoacaoBOTest.java`.

## Postman

A collection [`RecicleGlobal.postman_collection.json`](postman/RecicleGlobal.postman_collection.json) reúne as operações de CRUD, o fluxo de solicitações e os testes de validação. Importe o arquivo no Postman e ajuste a variável `baseUrl` para o ambiente desejado (o padrão é `http://localhost:8080`).

As pastas estão organizadas na ordem de execução. Os testes de criação armazenam os IDs retornados pela API nas variáveis da collection; as operações de escrita modificam os registros do banco de dados.

## Docker e deploy

O [`Dockerfile`](Dockerfile) utiliza build em múltiplos estágios com Maven e Java 21. Para gerar e executar a imagem:

```bash
docker build -t recicleglobal-api .
docker run --rm -p 8080:8080 --env-file .env recicleglobal-api
```

O arquivo `.env` deve conter as variáveis de conexão e permanecer fora do controle de versão. O serviço também pode ser implantado em provedores compatíveis com Docker, como o Render, configurando essas variáveis no ambiente de hospedagem.

**Instância pública:** https://recicleglobal.onrender.com

**Autenticação:** não implementada nas rotas atuais.
