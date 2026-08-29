# Diagrama de Classes - API MecâniQA

## Núcleo da aplicação

```mermaid
classDiagram
    direction LR

    class CategoriaPeca {
        <<enumeration>>
        MOTOR
        SUSPENSAO
        FREIOS
        ELETRICA
        ACESSORIOS
    }

    class Peca {
        -codigo: Long
        -codigoBarras: String
        -fornecedorMarca: String
        -quantidadeEstoque: Integer
        -precoCusto: BigDecimal
        -precoVenda: BigDecimal
        -dataCadastro: LocalDateTime
        -dataUltimaAtualizacao: LocalDateTime
        -tamanho: String
        -cor: String
        -categoria: CategoriaPeca
        +getCodigo(): Long
        +setCodigo(codigo: Long): void
        +getQuantidadeEstoque(): Integer
        +setQuantidadeEstoque(quantidade: Integer): void
        +getPrecoCusto(): BigDecimal
        +setPrecoCusto(preco: BigDecimal): void
        +getPrecoVenda(): BigDecimal
        +setPrecoVenda(preco: BigDecimal): void
    }

    class Servico {
        -codigo: Long
        -nome: String
        -duracaoEstimadaMinutos: Integer
        -custoTabelado: BigDecimal
        -dataCriacao: LocalDateTime
        -dataUltimaAtualizacao: LocalDateTime
        +getCodigo(): Long
        +setCodigo(codigo: Long): void
        +getDuracaoEstimadaMinutos(): Integer
        +setDuracaoEstimadaMinutos(minutos: Integer): void
        +getCustoTabelado(): BigDecimal
        +setCustoTabelado(custo: BigDecimal): void
    }

    class PecaRepository {
        <<Singleton>>
        -INSTANCE: PecaRepository
        -pecas: List~Peca~
        -proximoCodigo: AtomicLong
        -PecaRepository()
        +getInstance(): PecaRepository
        +salvar(peca: Peca): Peca
        +listarTodos(): List~Peca~
        +buscarPorCodigo(codigo: Long): Optional~Peca~
        +atualizar(codigo: Long, quantidade: Integer, custo: BigDecimal, venda: BigDecimal): Optional~Peca~
        +excluir(codigo: Long): boolean
    }

    class ServicoRepository {
        <<Singleton>>
        -INSTANCE: ServicoRepository
        -servicos: List~Servico~
        -proximoCodigo: AtomicLong
        -ServicoRepository()
        +getInstance(): ServicoRepository
        +salvar(servico: Servico): Servico
        +listarTodos(): List~Servico~
        +buscarPorCodigo(codigo: Long): Optional~Servico~
        +atualizar(codigo: Long, duracao: Integer, custo: BigDecimal): Optional~Servico~
        +excluir(codigo: Long): boolean
    }

    class PecaController {
        -repository: PecaRepository
        +criar(request: CriarPecaRequest): ResponseEntity~Peca~
        +listar(): ResponseEntity~List~Peca~~
        +buscar(codigo: Long): ResponseEntity~Peca~
        +atualizar(codigo: Long, request: AtualizarPecaRequest): ResponseEntity~Peca~
        +excluir(codigo: Long): ResponseEntity~Void~
    }

    class ServicoController {
        -repository: ServicoRepository
        +criar(request: CriarServicoRequest): ResponseEntity~Servico~
        +listar(): ResponseEntity~List~Servico~~
        +buscar(codigo: Long): ResponseEntity~Servico~
        +atualizar(codigo: Long, request: AtualizarServicoRequest): ResponseEntity~Servico~
        +excluir(codigo: Long): ResponseEntity~Void~
    }

    Peca --> CategoriaPeca : categoria obrigatória
    PecaRepository o-- Peca : mantém em memória
    ServicoRepository o-- Servico : mantém em memória
    PecaController --> PecaRepository : getInstance()
    ServicoController --> ServicoRepository : getInstance()
```

Os repositories possuem construtor privado, instância estática única e método público
`getInstance()`. Não existe injeção de dependência entre controllers e repositories.

## DTOs e tratamento de erros

```mermaid
classDiagram
    direction LR

    class CriarPecaRequest {
        -codigoBarras: String
        -fornecedorMarca: String
        -quantidadeEstoque: Integer
        -precoCusto: BigDecimal
        -precoVenda: BigDecimal
        -tamanho: String
        -cor: String
        -categoria: CategoriaPeca
    }

    class AtualizarPecaRequest {
        -quantidadeEstoque: Integer
        -precoCusto: BigDecimal
        -precoVenda: BigDecimal
    }

    class CriarServicoRequest {
        -nome: String
        -duracaoEstimadaMinutos: Integer
        -custoTabelado: BigDecimal
    }

    class AtualizarServicoRequest {
        -duracaoEstimadaMinutos: Integer
        -custoTabelado: BigDecimal
    }

    class ApiError {
        <<record>>
        +timestamp: LocalDateTime
        +status: int
        +error: String
        +message: String
        +path: String
        +campos: Map~String_String~
    }

    class RecursoNaoEncontradoException {
        +RecursoNaoEncontradoException(message: String)
    }

    class GlobalExceptionHandler {
        +tratarRecursoNaoEncontrado(ex: RecursoNaoEncontradoException, request: HttpServletRequest): ResponseEntity~ApiError~
        +tratarValidacao(ex: MethodArgumentNotValidException, request: HttpServletRequest): ResponseEntity~ApiError~
        +tratarCorpoInvalido(ex: HttpMessageNotReadableException, request: HttpServletRequest): ResponseEntity~ApiError~
        +tratarParametroInvalido(ex: MethodArgumentTypeMismatchException, request: HttpServletRequest): ResponseEntity~ApiError~
    }

    class PecaController
    class ServicoController

    PecaController ..> CriarPecaRequest
    PecaController ..> AtualizarPecaRequest
    ServicoController ..> CriarServicoRequest
    ServicoController ..> AtualizarServicoRequest
    GlobalExceptionHandler --> ApiError : produz
    GlobalExceptionHandler ..> RecursoNaoEncontradoException : trata
```

## Fluxo principal

```mermaid
sequenceDiagram
    participant Cliente
    participant Controller
    participant DTO
    participant Repository
    participant Memoria as Lista em memória

    Cliente->>Controller: Requisição HTTP + JSON
    Controller->>DTO: Desserializa e valida
    DTO-->>Controller: Dados válidos
    Controller->>Repository: getInstance() + operação
    Repository->>Memoria: CRUD
    Memoria-->>Repository: Resultado
    Repository-->>Controller: Entidade ou Optional vazio
    Controller-->>Cliente: ResponseEntity + status HTTP
```
