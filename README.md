# RecicleGlobal API — GS CitizenTech / FIAP

Primeira versão acadêmica do back-end da **RecicleGlobal**. A plataforma permite cadastrar usuários, listar instituições fictícias em São Paulo, anunciar alimentos dentro da validade e roupas usadas e solicitar uma doação para retirada.

Esta etapa contempla a **API Java**. O site com o mapa interativo será construído depois, consumindo os endpoints desta API.

## 1. O que já está implementado?

| Recurso | Funcionalidades |
| --- | --- |
| Usuários | GET, POST, PUT, DELETE |
| Instituições | GET, POST, PUT, DELETE; latitude e longitude para o mapa |
| Doações | GET, POST, PUT, DELETE; filtros de categoria e status |
| Solicitações | GET, POST, aceitar, concluir, cancelar |
| Regras de negócio | Impede alimento vencido, retirada da própria doação e reservas conflitantes |
| Tratamento de erros | Respostas 400, 404 e 409 quando apropriado |

**Atenção:** Este é um protótipo acadêmico sem autenticação nem autorização. Qualquer pessoa que conheça o endereço da API poderia, por exemplo, aceitar solicitações. Não publique dados pessoais reais ou endereços residenciais. As coordenadas de doações são ilustrativas ou aproximadas. A versão real precisaria de login, controle de acesso e proteção de privacidade.

## 2. Estrutura e conceitos (para quem está começando)

```text
src/main/java/br/com/fiap/recicleglobal/
├── RecicleGlobalApplication.java  <- inicia o servidor
├── model/                        <- entidades e enums; representam tabelas
├── dao/                          <- consultas e persistência no Oracle
├── bo/                           <- validações e regras de negócio
├── dto/                          <- formato dos JSONs enviados e devolvidos
├── controller/                   <- rotas HTTP GET, POST, PUT e DELETE
├── exception/                    <- erros e mensagens da API
└── config/                       <- permissão de conexão com o futuro front-end
```

Um **POST /api/doacoes** passa pelo `DoacaoController` (recebe JSON), `DoacaoBO` (valida regras), `DoacaoDAO` (salva no Oracle) e `Doacao` (entidade que representa RG_DOACAO).

## 3. Ferramentas necessárias

1. **JDK 21**, não apenas JRE.
2. **IntelliJ IDEA** (Community já serve; ele consegue importar um projeto Maven).
3. **Oracle SQL Developer**, ou outro cliente com acesso ao servidor Oracle disponibilizado pela FIAP.
4. **Postman** para enviar requisições HTTP; também é possível usar Insomnia.
5. A conexão Oracle FIAP já liberada e as credenciais fornecidas pela faculdade.

> O arquivo `pom.xml` gerencia as dependências usando Maven. Dentro do IntelliJ, você pode usar a ferramenta Maven da própria IDE, sem instalar o comando `mvn` separadamente.

Também há três testes unitários de regras de negócio em `src/test/java/.../DoacaoBOTest.java`. Na aba Maven do IntelliJ, execute **Lifecycle > test** para rodá-los sem precisar de conexão ao Oracle.

## 4. Preparar o banco Oracle FIAP

1. Abra o SQL Developer e crie/abra a conexão da FIAP.
2. Confirme que a conexão funciona: execute `SELECT 1 FROM DUAL;`.
3. Abra `sql/schema-oracle.sql` e use **F5 (Run Script)**, uma única vez. Ele cria quatro tabelas, quatro sequences, índices e chaves estrangeiras.
4. Depois abra `sql/instituicoes-ficticias.sql` e use **F5**, uma única vez. O script cadastra três locais fictícios com pontos aproximados de São Paulo.
5. Confirme a criação executando:

```sql
SELECT * FROM RG_INSTITUICAO;
SELECT * FROM RG_USUARIO;
SELECT * FROM RG_DOACAO;
SELECT * FROM RG_SOLICITACAO;
```

Os scripts não são feitos para repetição; uma segunda execução causaria erro de objeto já existente ou registros duplicados. Não apague outras tabelas do banco da faculdade.

## 5. Abrir o código no IntelliJ (passo a passo)

1. Descompacte o ZIP em uma pasta simples, como `Documentos/RecicleGlobal`.
2. Abra o **IntelliJ IDEA** e clique em **Open**.
3. Escolha a pasta `recicleglobal-api`, aquela que contém `pom.xml`.
4. Confirme **Trust Project** se estiver confiante na origem do arquivo.
5. Espere a IDE indexar tudo e baixar as dependências Maven. O computador precisa de internet para o primeiro download.
6. No canto superior direito, configure o **Project SDK = Java 21**; se necessário, em *File > Project Structure > Project*.
7. Abra `RecicleGlobalApplication.java`.

### Informar acesso ao Oracle sem expor a senha no código

1. No IntelliJ, entre em **Run > Edit Configurations...**.
2. Se não existir uma configuração, adicione uma de tipo **Application** para a classe `br.com.fiap.recicleglobal.RecicleGlobalApplication`.
3. Procure o campo **Environment variables**.
4. Cadastre as três variáveis com os valores fornecidos pela FIAP:

| Nome | Exemplo (substitua pelo valor da FIAP) |
| --- | --- |
| `DB_URL` | `jdbc:oracle:thin:@//servidor:1521/servico` |
| `DB_USERNAME` | `usuario_oracle` |
| `DB_PASSWORD` | `sua_senha` |

**Importante:** A URL JDBC varia de acordo com a conexão da FIAP. Alguns ambientes usam `@host:porta:SID` em vez de `@//host:porta/servico`. Consulte os campos de sua conexão no SQL Developer ou o material do professor. Não envie a senha no chat nem coloque no GitHub.

5. Salve a configuração, clique no botão verde **Run** da classe principal e aguarde a mensagem `Started RecicleGlobalApplication`.
6. Abra `http://localhost:8080/api/status` no navegador. Resposta esperada:

```json
{"aplicacao":"RecicleGlobal","status":"online"}
```

Se o aplicativo não iniciar, verifique no final deste guia os erros mais comuns.

## 6. Testar com Postman (requisições em ordem)

No Postman, para cada requisição escolha o método, informe a URL `http://localhost:8080/...`, abra **Body > raw > JSON** (para POST/PUT), cole o JSON e clique em **Send**. A API utiliza IDs gerados pelo Oracle; use os IDs reais que retornarem no Postman.

### Teste A — cadastrar o doador

**POST** `http://localhost:8080/api/usuarios`

```json
{"nome":"Ana Exemplo","email":"ana.exemplo@teste.com"}
```

Retorno esperado: **201 Created** com um `id`, por exemplo, `1`.

### Teste B — cadastrar alguém que vai receber

**POST** `http://localhost:8080/api/usuarios`

```json
{"nome":"Bruno Exemplo","email":"bruno.exemplo@teste.com"}
```

Guarde o ID de Bruno (por exemplo, `2`). Os emails aqui são fictícios.

### Teste C — listar instituições cadastradas

**GET** `http://localhost:8080/api/instituicoes`

Retorno esperado: **200 OK** com três instituições fictícias, cada uma com `latitude` e `longitude`.

### Teste D — criar anúncio de roupa

**POST** `http://localhost:8080/api/doacoes`

Substitua `usuarioId` pelo ID verdadeiro do Teste A.

```json
{
  "usuarioId": 1,
  "categoria": "ROUPA",
  "titulo": "Casacos em bom estado",
  "descricao": "3 casacos adultos limpos para doação",
  "quantidade": 3,
  "dataValidade": null,
  "bairro": "Vila Mariana",
  "latitude": -23.5863000,
  "longitude": -46.6325000
}
```

Retorno esperado: **201 Created** com o ID da doação e `status: "DISPONIVEL"`.

### Teste E — listar roupas disponíveis

**GET** `http://localhost:8080/api/doacoes?categoria=ROUPA&status=DISPONIVEL`

### Teste F — solicitar uma doação

**POST** `http://localhost:8080/api/solicitacoes`

Substitua pelos IDs reais da doação (D) e do interessado (B).

```json
{"doacaoId":1,"interessadoId":2}
```

Retorno esperado: **201 Created** e `status: "PENDENTE"`.

### Teste G — aceitar a solicitação

**PUT** `http://localhost:8080/api/solicitacoes/1/aceitar`

Troque o `1` pelo ID da solicitação do Teste F. Não envie Body.

Depois use **GET** `http://localhost:8080/api/doacoes/1` (ID da doação) e confirme `status: "RESERVADA"`.

### Teste H — concluir a doação

**PUT** `http://localhost:8080/api/solicitacoes/1/concluir` (ID da solicitação). Não envie Body.

O anúncio passará para `CONCLUIDA` e não poderá mais receber solicitações.

### Teste I — testar validação (alimento vencido)

**POST** `http://localhost:8080/api/doacoes`

```json
{
  "usuarioId": 1,
  "categoria": "ALIMENTO",
  "titulo": "Arroz fora da validade",
  "descricao": "Teste proposital de regra de negócio",
  "quantidade": 1,
  "dataValidade": "2020-01-01",
  "bairro": "Centro",
  "latitude": -23.5509000,
  "longitude": -46.6333000
}
```

Retorno esperado: **400 Bad Request** com a mensagem de que alimentos vencidos não podem ser doados.

## 7. Lista de endpoints

| Método | Endpoint | Função |
| --- | --- | --- |
| GET | `/api/status` | Estado do servidor |
| GET | `/api/usuarios` | Lista usuários |
| GET | `/api/usuarios/{id}` | Detalha usuário |
| POST | `/api/usuarios` | Cria usuário |
| PUT | `/api/usuarios/{id}` | Altera usuário |
| DELETE | `/api/usuarios/{id}` | Exclui se não houver vínculos |
| GET | `/api/instituicoes` | Lista pontos para o mapa |
| GET | `/api/instituicoes/{id}` | Detalha instituição |
| POST | `/api/instituicoes` | Cria instituição |
| PUT | `/api/instituicoes/{id}` | Altera instituição |
| DELETE | `/api/instituicoes/{id}` | Exclui instituição |
| GET | `/api/doacoes` | Lista; filtros opcionais `categoria` e `status` |
| GET | `/api/doacoes/{id}` | Detalha anúncio |
| POST | `/api/doacoes` | Cria anúncio |
| PUT | `/api/doacoes/{id}` | Atualiza anúncio disponível |
| DELETE | `/api/doacoes/{id}` | Exclui anúncio sem solicitações |
| GET | `/api/solicitacoes` | Lista; filtro `doacaoId` opcional |
| POST | `/api/solicitacoes` | Pede retirada de uma doação |
| PUT | `/api/solicitacoes/{id}/aceitar` | Reserva a doação |
| PUT | `/api/solicitacoes/{id}/concluir` | Finaliza entrega |
| PUT | `/api/solicitacoes/{id}/cancelar` | Cancela pedido |

## 8. Fluxo e regras

```text
Usuário cadastra doação -> DISPONIVEL
                           |
                   Solicitação PENDENTE
                           |
                     Doador aceita
                           |
                        RESERVADA
                           |
                    Entrega concluída
                           |
                        CONCLUIDA
```

- Apenas alimentos com validade atual ou futura são anunciados. A validade é reavaliada na solicitação, no aceite e na conclusão; anúncios de alimentos que venceram deixam de aparecer nas listagens quando disponíveis.
- Roupas não têm `dataValidade`.
- Um usuário não pode solicitar o próprio anúncio.
- A aceitação de um pedido recusa os outros pedidos pendentes da mesma doação.
- Se uma solicitação aceita for cancelada, a doação volta a estar disponível.
- O BO valida as regras; o DAO faz a persistência; o Controller expõe a API.

## 9. Deploy no Render (etapa posterior)

O projeto inclui um `Dockerfile` para futura publicação. O Render permite executar aplicações Java por Docker. Para publicar:

1. Crie um repositório GitHub e envie o projeto **sem** `.env`, senhas ou credenciais.
2. Em Render, crie **New > Web Service**, conecte o repositório e selecione **Language: Docker**.
3. Em **Environment**, configure `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` (segredos) e, se houver site externo, `FRONTEND_ORIGIN`.
4. Verifique se o **Oracle FIAP aceita conexões originadas do Render**. Se o servidor for acessível apenas pela rede/VPN da faculdade ou por IPs autorizados, o deploy não conectará ao banco até que a instituição permita o acesso. Não altere a segurança do Oracle por conta própria.
5. Após o deploy, teste `https://SEU-SERVICO.onrender.com/api/status` e `.../api/instituicoes`.

O código e o Dockerfile ficam preparados para essa fase, mas é necessário testar a conectividade com o servidor FIAP antes de considerar o deploy concluído.

## 10. Solução de erros comuns

| Sintoma | O que conferir |
| --- | --- |
| `DB_URL` ou `DB_USERNAME` não encontrado | Variáveis de ambiente não cadastradas em *Run > Edit Configurations* |
| `ORA-01017` | Usuário ou senha do Oracle incorretos |
| `ORA-12514` / `ORA-12505` | Service Name ou SID da URL incorreto |
| `ORA-12154` / falha de conexão | URL, rede/VPN FIAP, firewall e DNS |
| `Schema-validation: missing table` | Executar `sql/schema-oracle.sql` na **mesma conta** que a API usa |
| `ORA-00955` | Tentativa de executar o script de criação de tabelas pela segunda vez |
| `409 Conflict` ao cadastrar email | O email já existe |
| `400 Bad Request` | Verificar campos obrigatórios, categoria, validade e formato JSON |
| `404 Not Found` | ID não existe |
| `Port 8080 already in use` | Fechar programa que usa a porta ou mudar `PORT` |

## 11. Próximas etapas do trabalho

1. **Validar localmente** no Oracle FIAP: cadastros, consulta, edição, exclusão e fluxo de retirada.
2. **Criar o frontend** com telas de listagem e formulário, consumindo os endpoints; mapa usa `latitude`/`longitude` de `/api/instituicoes` e `/api/doacoes`.
3. **Modelar eventos beneficentes** e o recebimento por ONG, se houver tempo, sem comprometer o CRUD essencial.
4. **Desenhar os diagramas de classes e DER** a partir do código e do script SQL.
5. **Publicar no GitHub**, testar deploy, documentar capturas de tela e montar **um PDF** conforme a rubrica da disciplina.

### Observação sobre a entrega da GS

O projeto fornece um começo alinhado às camadas **Model, DAO, BO, Controller, tratamento de exceções** e suporte ao **Oracle FIAP**. Os testes reais no Oracle, o deploy funcional, os protótipos e os diagramas **ainda não estão concluídos**. Não considere o ZIP, sozinho, como trabalho final da GS.
