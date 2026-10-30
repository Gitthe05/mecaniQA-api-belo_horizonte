# Cenários de teste - OAT 3

## JDBC e Template Method

| ID | Cenário | Resultado esperado | Cobertura |
|---|---|---|---|
| JDBC-01 | Query conclui com sucesso | Conexão encerrada | `RepositorioJdbcTemplateTests` |
| JDBC-02 | Query lança `SQLException` | Exceção de persistência e conexão encerrada | `RepositorioJdbcTemplateTests` |
| JDBC-03 | Repositórios de Peça, Serviço, OS e Pedido | Implementam contrato genérico e herdam o template JDBC | `ServicoRepositoryTests` |

## HTTP e regra de negócio

| ID | Cenário | Resultado esperado | Cobertura |
|---|---|---|---|
| API-01 | `GET /api/pecas/99999` sem registro | Service propaga recurso inexistente; controller devolve HTTP 404 | `PecaControllerTests` |
| API-02 | `GET /api/servicos/99999` sem registro | HTTP 404 | `ServicoControllerTests` |
| API-03 | `DELETE /api/ordens-servico/99999` | HTTP 404 | `OrdemServicoControllerTests` |
| API-04 | `DELETE /api/pedidos-pecas/99999` | HTTP 404 | `PedidoPecasControllerTests` |

## Validação ponta a ponta com MySQL

O ambiente desta execução não tem servidor MySQL ativo na porta local padrão. Para validar persistência e relacionamentos, execute `script_mecaniqa.sql`, configure `DB_URL`, `DB_USER` e `DB_PASSWORD`, inicie a API e rode a coleção Postman. Confirme as linhas nas tabelas `pecas`, `servicos`, `ordens_servico`, `ordem_servico_pecas`, `ordem_servico_servicos`, `pedidos_pecas` e `itens_pedido_pecas`.

```powershell
.\gradlew.bat test
```
