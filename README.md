# MecâniQA API - Belo Horizonte

API REST da oficina fictícia MecâniQA, desenvolvida em Java e Spring Boot. A persistência
das entidades é feita com JDBC puro e MySQL, sem ORM.

## Visão geral

O projeto mantém peças, serviços, ordens de serviço e pedidos de peças no MySQL. Os
repositórios JDBC implementam o contrato genérico `Repositorio<T, ID>` e compartilham o
ciclo de conexão da classe abstrata `RepositorioJdbcTemplate` (Template Method).

## Tecnologias

- Java 21
- Spring Boot 4.1.0
- Spring Web MVC
- Jakarta Bean Validation
- MySQL Connector/J e JDBC puro (sem JPA, Hibernate ou Spring Data)
- Gradle 9.5.1 com Kotlin DSL
- JUnit 5 e MockMvc

## Funcionalidades

- Cadastro, listagem, consulta, atualização e exclusão de peças.
- Cadastro, listagem, consulta, atualização e exclusão de serviços.
- Códigos gerados pelo `AUTO_INCREMENT` do MySQL.
- Datas de criação e atualização geradas automaticamente.
- Categoria obrigatória para peças.
- Tamanho e cor opcionais.
- Validação de corpos de requisição.
- Erros JSON padronizados.
- Status HTTP semânticos.
- Persistência MySQL de peças, serviços, ordens de serviço, pedidos e seus relacionamentos.
- Queries JDBC manuais e mappers de `ResultSet`; sem JPA, Hibernate ou Spring Data.
- Erros de domínio tratados explicitamente nas camadas Service e Controller, sem advice global.
- Criação de ordens de serviço pelo padrão Builder.
- Alteração dos cinco status obrigatórios da OS.
- Criação de pedidos e alteração dos quatro status obrigatórios.
- Associação pedido–peça por itens com quantidade.
- DTOs de entrada e saída e mappers em toda a fronteira HTTP.

## Pré-requisitos

- JDK 21
- Git
- Conexão com a internet na primeira execução do Gradle

Verifique o Java:

```bash
java -version
```

## Instalação

```bash
git clone https://github.com/Gitthe05/mecaniQA-api-belo_horizonte.git
cd mecaniQA-api-belo_horizonte
```

O Gradle Wrapper já faz parte do projeto; não é necessário instalar Gradle globalmente.

Execute `script_mecaniqa.sql` no MySQL para criar o banco e as tabelas. A conexão usa
`DB_URL`, `DB_USER` e `DB_PASSWORD`; os valores padrão apontam para
`localhost:3306/mecaniqa`, usuário `root` e senha vazia. Configure credenciais próprias
no ambiente antes de executar a aplicação.

No PowerShell, o script pode ser aplicado com:

```powershell
Get-Content .\script_mecaniqa.sql | mysql -u root -p
```

Exemplo PowerShell:

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/mecaniqa?useSSL=false&serverTimezone=America/Sao_Paulo"
$env:DB_USER = "root"
$env:DB_PASSWORD = "sua-senha"
```

A conexão é aberta sob demanda ao executar operação JDBC. Credenciais inválidas geram
mensagem clara de conectividade sem retornar a senha. Os recursos JDBC são fechados com
`try-with-resources`.

## Execução

Windows:

```powershell
.\gradlew.bat bootRun
```

Linux ou macOS:

```bash
./gradlew bootRun
```

A API ficará disponível em `http://localhost:8080`.

## Endpoints

### Peças

| Método | URL | Descrição | Corpo | Respostas |
|---|---|---|---|---|
| `POST` | `/api/pecas` | Cadastra uma peça | `CriarPecaRequest` | `201`, `400` |
| `GET` | `/api/pecas` | Lista todas as peças | Nenhum | `200` |
| `GET` | `/api/pecas/{codigo}` | Busca uma peça pelo código | Nenhum | `200`, `400`, `404` |
| `PUT` | `/api/pecas/{codigo}` | Atualiza estoque e preços | `AtualizarPecaRequest` | `200`, `400`, `404` |
| `DELETE` | `/api/pecas/{codigo}` | Exclui uma peça | Nenhum | `204`, `400`, `404` |

#### Cadastrar peça

```http
POST /api/pecas
Content-Type: application/json
```

```json
{
  "codigoBarras": "7891234567890",
  "fornecedorMarca": "Bosch",
  "quantidadeEstoque": 12,
  "precoCusto": 80.50,
  "precoVenda": 125.90,
  "tamanho": "M",
  "cor": "Preta",
  "categoria": "FREIOS"
}
```

Resposta `201 Created`:

```json
{
  "codigo": 1,
  "codigoBarras": "7891234567890",
  "fornecedorMarca": "Bosch",
  "quantidadeEstoque": 12,
  "precoCusto": 80.50,
  "precoVenda": 125.90,
  "dataCadastro": "2026-08-25T12:00:00",
  "dataUltimaAtualizacao": "2026-08-25T12:00:00",
  "tamanho": "M",
  "cor": "Preta",
  "categoria": "FREIOS"
}
```

O cabeçalho `Location` aponta para `/api/pecas/1`.

Categorias disponíveis: `MOTOR`, `SUSPENSAO`, `FREIOS`, `ELETRICA` e `ACESSORIOS`.

#### Listar peças

`GET /api/pecas` retorna `200 OK` e um array. Para um catálogo vazio, retorna `[]`.

#### Buscar peça

`GET /api/pecas/1` retorna `200 OK` com a peça. Código inexistente retorna `404`.

#### Atualizar peça

```http
PUT /api/pecas/1
Content-Type: application/json
```

```json
{
  "quantidadeEstoque": 20,
  "precoCusto": 90.00,
  "precoVenda": 140.00
}
```

Resposta `200 OK`: peça atualizada, preservando os demais campos.

#### Excluir peça

`DELETE /api/pecas/1` retorna `204 No Content`, sem corpo.

### Serviços

| Método | URL | Descrição | Corpo | Respostas |
|---|---|---|---|---|
| `POST` | `/api/servicos` | Cadastra um serviço | `CriarServicoRequest` | `201`, `400` |
| `GET` | `/api/servicos` | Lista todos os serviços | Nenhum | `200` |
| `GET` | `/api/servicos/{codigo}` | Busca um serviço pelo código | Nenhum | `200`, `400`, `404` |
| `PUT` | `/api/servicos/{codigo}` | Atualiza duração e custo | `AtualizarServicoRequest` | `200`, `400`, `404` |
| `DELETE` | `/api/servicos/{codigo}` | Exclui um serviço | Nenhum | `204`, `400`, `404` |

#### Cadastrar serviço

```http
POST /api/servicos
Content-Type: application/json
```

```json
{
  "nome": "Alinhamento",
  "duracaoEstimadaMinutos": 45,
  "custoTabelado": 100.00
}
```

Resposta `201 Created`:

```json
{
  "codigo": 1,
  "nome": "Alinhamento",
  "duracaoEstimadaMinutos": 45,
  "custoTabelado": 100.00,
  "dataCriacao": "2026-08-25T12:00:00",
  "dataUltimaAtualizacao": "2026-08-25T12:00:00"
}
```

#### Listar serviços

`GET /api/servicos` retorna `200 OK` e um array. Para um catálogo vazio, retorna `[]`.

#### Buscar serviço

`GET /api/servicos/1` retorna `200 OK` com o serviço. Código inexistente retorna `404`.

#### Atualizar serviço

```http
PUT /api/servicos/1
Content-Type: application/json
```

```json
{
  "duracaoEstimadaMinutos": 60,
  "custoTabelado": 130.00
}
```

Resposta `200 OK`: serviço atualizado, preservando código, nome e data de criação.

#### Excluir serviço

`DELETE /api/servicos/1` retorna `204 No Content`, sem corpo.

### Ordens de serviço

| Método | URL | Descrição |
|---|---|---|
| `POST` | `/api/ordens-servico` | Cria uma OS com peças e serviços opcionais |
| `GET` | `/api/ordens-servico` | Lista as ordens |
| `GET` | `/api/ordens-servico/{codigo}` | Consulta uma ordem |
| `PATCH` | `/api/ordens-servico/{codigo}/status` | Modifica o status |
| `DELETE` | `/api/ordens-servico/{codigo}` | Exclui uma ordem |

```json
{
  "cliente": "Ana Souza",
  "veiculo": "ABC-1234",
  "descricao": "Troca das pastilhas de freio",
  "codigosPecas": [1],
  "codigosServicos": [1]
}
```

Status: `ABERTO`, `PENDENTE_DE_PAGAMENTO`, `PAGO`, `EM_EXECUCAO` e `EXECUTADO`.

O `POST` responde com `201 Created` e `Location`; consultas e alteração de status respondem
com `200 OK`; exclusão responde com `204 No Content`. Recurso inexistente retorna `404`.

### Pedidos de peças

| Método | URL | Descrição |
|---|---|---|
| `POST` | `/api/pedidos-pecas` | Cria um pedido, com itens opcionais |
| `GET` | `/api/pedidos-pecas` | Lista os pedidos |
| `GET` | `/api/pedidos-pecas/{codigo}` | Consulta um pedido |
| `POST` | `/api/pedidos-pecas/{codigo}/itens` | Adiciona uma peça e sua quantidade |
| `PATCH` | `/api/pedidos-pecas/{codigo}/status` | Modifica o status |
| `DELETE` | `/api/pedidos-pecas/{codigo}` | Exclui um pedido |

```json
{
  "fornecedor": "Distribuidora X",
  "itens": [{ "codigoPeca": 1, "quantidade": 4 }]
}
```

Status: `ORCANDO`, `PENDENTE_DE_PAGAMENTO`, `PAGO_FATURADO` e `ENTREGUE`. Se uma peça
já existente for adicionada novamente, a quantidade é somada ao item associativo.

O `POST` de criação responde com `201 Created` e `Location`; consultas, alteração de status
e inclusão de item respondem com `200 OK`; exclusão responde com `204 No Content`. Recurso
inexistente retorna `404`.

## Validações

- Textos obrigatórios não podem estar vazios.
- Quantidade em estoque deve ser maior ou igual a zero.
- Preços e custos devem ser maiores ou iguais a zero.
- Duração estimada deve ser maior que zero.
- Categoria da peça é obrigatória e deve pertencer ao enum.
- Tamanho e cor são opcionais.
- Código e datas enviados pelo cliente não fazem parte dos DTOs de entrada.

## Formato de erro

```json
{
  "timestamp": "2026-08-25T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Dados de entrada inválidos",
  "path": "/api/pecas",
  "campos": {
    "categoria": "é obrigatória"
  }
}
```

Erros de recurso inexistente seguem o mesmo formato com status `404`.

## Testes

```powershell
.\gradlew.bat test
```

A suíte possui 19 testes automatizados de controllers, serviços e persistência JDBC, todos
aprovados. O relatório HTML é gerado em `build/reports/tests/test/index.html`.

Consulte a [matriz completa de cenários](docs/cenarios-de-teste.md).

## Collection Postman

Importe [`postman/MecaniQA-OAT3.postman_collection.json`](postman/MecaniQA-OAT3.postman_collection.json)
no Postman. Execute as requisições na ordem apresentada; os códigos criados são
armazenados automaticamente nas variáveis da collection. A collection da OAT 1 foi
preservada como histórico.

## Requisitos OAT 3

O resumo dos requisitos de persistência, Template Method e versionamento está em
[docs/oat3-requisitos.md](docs/oat3-requisitos.md).

A branch de avaliação é `main`. A apresentação da OAT 3 deve ser versionada em
`apresentacoes/mecaniQA_api_oat3_belo_horizonte.pdf` e também entregue no Blackboard.

## Estrutura do projeto

```text
src/
├── main/
│   ├── java/br/com/mecaniqa/api/
│   │   ├── config/       # Conexão MySQL
│   │   ├── controller/   # Endpoints REST
│   │   ├── dto/          # DTOs de entrada e saída
│   │   ├── exception/    # Exceções e erros da API
│   │   ├── mapper/       # Conversões DTO ↔ domínio e ResultSet
│   │   ├── model/        # Entidades e enums
│   │   ├── repository/   # JDBC manual + Template Method
│   │   └── service/      # Regras e orquestração
│   └── resources/application.properties
└── test/java/br/com/mecaniqa/api/
    ├── controller/       # Testes HTTP
    ├── repository/       # Testes de contrato e ciclo de vida JDBC
    └── service/          # Testes de regras de negócio

docs/                     # Cenários e requisitos OAT 3
postman/                  # Coleção executável da API
script_mecaniqa.sql       # DDL do banco MySQL
```
## Arquitetura

```text
Cliente HTTP
    ↓
Controller REST
    ↓
Service
    ↓
Mapper de entrada → domínio
    ↓
Repository JDBC
    ↓
MySQL via PreparedStatement
    ↓
Mapper ResultSet → domínio → DTO de resposta
```

Os controllers dependem dos serviços. Os serviços aplicam as regras de negócio e usam
repositories JDBC, que implementam o contrato genérico `Repositorio<T, ID>`.
