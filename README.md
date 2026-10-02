# MecâniQA API - Belo Horizonte

API REST da oficina fictícia MecâniQA, desenvolvida em Java e Spring Boot para a OAT 2
da disciplina de Desenvolvimento Web Orientado a Objetos.

## Visão geral

O projeto mantém o catálogo de peças e serviços e implementa ordens de serviço e pedidos
de peças com persistência exclusivamente em memória. Cada tipo de recurso possui um
repository manual baseado no padrão Singleton.

Por requisito da atividade, o projeto não utiliza banco de dados, Spring Data ou injeção
de dependência (`@Autowired`). Os dados são perdidos quando a aplicação é encerrada.

## Tecnologias

- Java 21
- Spring Boot 4.1.0
- Spring Web MVC
- Jakarta Bean Validation
- Gradle 9.5.1 com Kotlin DSL
- JUnit 5 e MockMvc

## Funcionalidades

- Cadastro, listagem, consulta, atualização e exclusão de peças.
- Cadastro, listagem, consulta, atualização e exclusão de serviços.
- Códigos únicos sequenciais gerados pela aplicação.
- Datas de criação e atualização geradas automaticamente.
- Categoria obrigatória para peças.
- Tamanho e cor opcionais.
- Validação de corpos de requisição.
- Erros JSON padronizados.
- Status HTTP semânticos.
- Armazenamento em memória por repositories Singleton.
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

Não existem variáveis de ambiente obrigatórias nem credenciais para configurar.

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

### Pedidos de peças

| Método | URL | Descrição |
|---|---|---|
| `POST` | `/api/pedidos-pecas` | Cria um pedido, com itens opcionais |
| `GET` | `/api/pedidos-pecas` | Lista os pedidos |
| `GET` | `/api/pedidos-pecas/{codigo}` | Consulta um pedido |
| `POST` | `/api/pedidos-pecas/{codigo}/itens` | Adiciona uma peça e sua quantidade |
| `PATCH` | `/api/pedidos-pecas/{codigo}/status` | Modifica o status |

```json
{
  "fornecedor": "Distribuidora X",
  "itens": [{ "codigoPeca": 1, "quantidade": 4 }]
}
```

Status: `ORCANDO`, `PENDENTE_DE_PAGAMENTO`, `PAGO_FATURADO` e `ENTREGUE`. Se uma peça
já existente for adicionada novamente, a quantidade é somada ao item associativo.

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

A suíte atual possui 30 testes automatizados, sem falhas. O relatório HTML é gerado em
`build/reports/tests/test/index.html`.

Consulte a [matriz completa de cenários](docs/cenarios-de-teste.md).

## Collection Postman

Importe [`postman/MecaniQA-OAT2.postman_collection.json`](postman/MecaniQA-OAT2.postman_collection.json)
no Postman. Execute as requisições na ordem apresentada; os códigos criados são
armazenados automaticamente nas variáveis da collection. A collection da OAT 1 foi
preservada como histórico.

## Diagrama de classes

O UML completo está em [docs/diagrama-classes.md](docs/diagrama-classes.md). O diagrama
de atividade está em [docs/diagrama-atividade-criar-os.md](docs/diagrama-atividade-criar-os.md).
Os dois diagramas exigidos para a entrega também estão reunidos em
[apresentacoes/DIAGRAMAS.md](apresentacoes/DIAGRAMAS.md).

## Estrutura do projeto

```text
src/
├── main/
│   ├── java/br/com/mecaniqa/api/
│   │   ├── controller/   # Endpoints REST
    │   │   ├── dto/          # DTOs de entrada e saída
│   │   ├── exception/    # Respostas e tratamento de erros
    │   │   ├── mapper/       # Conversões DTO ↔ domínio
    │   │   ├── model/        # Peça, serviço, OS, pedido, item e enums
    │   │   ├── repository/   # Armazenamento Singleton em memória
    │   │   ├── service/      # Regras e orquestração das histórias da OAT 2
│   │   └── ApiApplication.java
│   └── resources/application.properties
└── test/java/br/com/mecaniqa/api/
    ├── controller/       # Testes HTTP com MockMvc
    └── repository/       # Testes dos Singletons

docs/                     # UML e matriz de testes
postman/                  # Collection executável da API
```

## Arquitetura

```text
Cliente HTTP
    ↓
Controller REST
    ↓
Validação do DTO
    ↓
Mapper DTO → domínio
    ↓
Service
    ↓
Repository Singleton
    ↓
Lista em memória
    ↓
Mapper domínio → DTO de resposta
```

Os controllers acessam os repositories exclusivamente por `getInstance()`, conforme a
restrição arquitetural da atividade.
