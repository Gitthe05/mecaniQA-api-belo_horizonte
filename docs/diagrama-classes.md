# Diagrama de Classes - API MecâniQA OAT 3

O diagrama destaca a camada de serviço, o contrato genérico dos repositórios JDBC e o
Template Method que controla o ciclo de vida das conexões.

```mermaid
classDiagram
    direction TB

    class Controller
    class Service
    class Repositorio~T_ID~ {
        <<interface>>
        +salvar(entidade): T
        +buscarPorCodigo(id): Optional~T~
        +listarTodos(): List~T~
        +atualizar(id, entidade): Optional~T~
        +excluir(id): boolean
    }
    class RepositorioJdbcTemplate {
        <<abstract>>
        #executar(operacao, rotina): T
        #buscarPorCodigo(id, tabela, mapper): Optional~T~
    }
    class PecaJdbcRepository
    class ServicoRepository
    class OrdemServicoRepository
    class PedidoPecasRepository
    class ConexaoMySQL
    class PecaResultSetMapper
    class ServicoResultSetMapper
    class OrdemServicoResultSetMapper
    class PedidoPecasResultSetMapper

    Controller --> Service
    Service --> PecaJdbcRepository
    Service --> ServicoRepository
    Service --> OrdemServicoRepository
    Service --> PedidoPecasRepository
    PecaJdbcRepository ..|> Repositorio
    ServicoRepository ..|> Repositorio
    OrdemServicoRepository ..|> Repositorio
    PedidoPecasRepository ..|> Repositorio
    PecaJdbcRepository --|> RepositorioJdbcTemplate
    ServicoRepository --|> RepositorioJdbcTemplate
    OrdemServicoRepository --|> RepositorioJdbcTemplate
    PedidoPecasRepository --|> RepositorioJdbcTemplate
    RepositorioJdbcTemplate --> ConexaoMySQL
    PecaJdbcRepository ..> PecaResultSetMapper
    ServicoRepository ..> ServicoResultSetMapper
    OrdemServicoRepository ..> OrdemServicoResultSetMapper
    PedidoPecasRepository ..> PedidoPecasResultSetMapper
```

Os controllers não chamam repositories. O `RepositorioJdbcTemplate` centraliza a abertura
e o fechamento da conexão, e cada repositório implementa suas queries SQL e mapeamento.
