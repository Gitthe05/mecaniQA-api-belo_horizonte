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
        -Long codigo
        -String codigoBarras
        -String fornecedorMarca
        -Integer quantidadeEstoque
        -BigDecimal precoCusto
        -BigDecimal precoVenda
        -LocalDateTime dataCadastro
        -LocalDateTime dataUltimaAtualizacao
        -String tamanho
        -String cor
        -CategoriaPeca categoria
        +getCodigo() Long
        +setCodigo(Long codigo) void
        +getQuantidadeEstoque() Integer
        +setQuantidadeEstoque(Integer quantidade) void
        +getPrecoCusto() BigDecimal
        +setPrecoCusto(BigDecimal preco) void
        +getPrecoVenda() BigDecimal
        +setPrecoVenda(BigDecimal preco) void
    }

    class Servico {
        -Long codigo
        -String nome
        -Integer duracaoEstimadaMinutos
        -BigDecimal custoTabelado
        -LocalDateTime dataCriacao
        -LocalDateTime dataUltimaAtualizacao
        +getCodigo() Long
        +setCodigo(Long codigo) void
        +getDuracaoEstimadaMinutos() Integer
        +setDuracaoEstimadaMinutos(Integer minutos) void
        +getCustoTabelado() BigDecimal
        +setCustoTabelado(BigDecimal custo) void
    }

    class PecaRepository {
        <<Singleton>>
        -PecaRepository INSTANCE
        -List~Peca~ pecas
        -AtomicLong proximoCodigo
        -PecaRepository()
        +getInstance() PecaRepository
        +salvar(Peca peca) Peca
        +listarTodos() List~Peca~
        +buscarPorCodigo(Long codigo) Optional~Peca~
        +atualizar(Long codigo, Integer quantidade, BigDecimal custo, BigDecimal venda) Optional~Peca~
        +excluir(Long codigo) boolean
    }

    class ServicoRepository {
        <<Singleton>>
        -ServicoRepository INSTANCE
        -List~Servico~ servicos
        -AtomicLong proximoCodigo
        -ServicoRepository()
        +getInstance() ServicoRepository
        +salvar(Servico servico) Servico
        +listarTodos() List~Servico~
        +buscarPorCodigo(Long codigo) Optional~Servico~
        +atualizar(Long codigo, Integer duracao, BigDecimal custo) Optional~Servico~
        +excluir(Long codigo) boolean
    }

    class PecaController {
        -PecaRepository repository
        +criar(CriarPecaRequest request) ResponseEntity~Peca~
        +listar() ResponseEntity~List~Peca~~
        +buscar(Long codigo) ResponseEntity~Peca~
        +atualizar(Long codigo, AtualizarPecaRequest request) ResponseEntity~Peca~
        +excluir(Long codigo) ResponseEntity~Void~
    }

    class ServicoController {
        -ServicoRepository repository
        +criar(CriarServicoRequest request) ResponseEntity~Servico~
        +listar() ResponseEntity~List~Servico~~
        +buscar(Long codigo) ResponseEntity~Servico~
        +atualizar(Long codigo, AtualizarServicoRequest request) ResponseEntity~Servico~
        +excluir(Long codigo) ResponseEntity~Void~
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
        -String codigoBarras
        -String fornecedorMarca
        -Integer quantidadeEstoque
        -BigDecimal precoCusto
        -BigDecimal precoVenda
        -String tamanho
        -String cor
        -CategoriaPeca categoria
    }

    class AtualizarPecaRequest {
        -Integer quantidadeEstoque
        -BigDecimal precoCusto
        -BigDecimal precoVenda
    }

    class CriarServicoRequest {
        -String nome
        -Integer duracaoEstimadaMinutos
        -BigDecimal custoTabelado
    }

    class AtualizarServicoRequest {
        -Integer duracaoEstimadaMinutos
        -BigDecimal custoTabelado
    }

    class ApiError {
        <<record>>
        +LocalDateTime timestamp
        +int status
        +String error
        +String message
        +String path
        +Map~String_String~ campos
    }

    class RecursoNaoEncontradoException {
        +RecursoNaoEncontradoException(String message)
    }

    class GlobalExceptionHandler {
        +tratarRecursoNaoEncontrado(RecursoNaoEncontradoException, HttpServletRequest) ResponseEntity~ApiError~
        +tratarValidacao(MethodArgumentNotValidException, HttpServletRequest) ResponseEntity~ApiError~
        +tratarCorpoInvalido(HttpMessageNotReadableException, HttpServletRequest) ResponseEntity~ApiError~
        +tratarParametroInvalido(MethodArgumentTypeMismatchException, HttpServletRequest) ResponseEntity~ApiError~
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
