# Cenários de Teste - API MecâniQA

## Escopo

Esta matriz cobre o CRUD de peças e serviços, validações de entrada, respostas HTTP,
tratamento de erros e o comportamento dos repositories Singleton em memória.

Os testes de banco de dados não se aplicam a esta entrega: o armazenamento em memória é
um requisito explícito e o uso de banco, Spring Data e injeção de dependência é proibido.

## API de peças

| ID | Cenário | Tipo | Resultado esperado | Teste automatizado |
|---|---|---|---|---|
| PEC-01 | Cadastrar peça com todos os campos obrigatórios | Positivo | `201 Created`, `Location`, código e datas gerados | `deveCadastrarPecaValida` |
| PEC-02 | Cadastrar peça sem tamanho e cor, com estoque e preços iguais a zero | Borda | `201 Created` | `deveAceitarCamposOpcionaisAusentesEValoresLimite` |
| PEC-03 | Cadastrar peça com textos vazios, valores negativos e categoria ausente | Negativo | `400 Bad Request` com erros por campo | `deveRejeitarPecaComDadosInvalidos` |
| PEC-04 | Cadastrar peça com enum inexistente | Negativo | `400 Bad Request` controlado | `deveRetornarErrosControladosParaPecaInexistenteEParametrosInvalidos` |
| PEC-05 | Enviar corpo ausente ou JSON malformado | Negativo | `400 Bad Request` controlado | `deveRejeitarCorpoAusenteOuJsonMalformado` |
| PEC-06 | Listar catálogo vazio | Borda | `200 OK` com `[]` | `deveListarCatalogoVazio` |
| PEC-07 | Listar e buscar peça existente | Positivo | `200 OK` com os dados cadastrados | `deveListarBuscarAtualizarEExcluirPeca` |
| PEC-08 | Buscar peça inexistente | Negativo | `404 Not Found` padronizado | `deveRetornarErrosControladosParaPecaInexistenteEParametrosInvalidos` |
| PEC-09 | Consultar com código de tipo inválido | Negativo | `400 Bad Request` padronizado | `deveRetornarErrosControladosParaPecaInexistenteEParametrosInvalidos` |
| PEC-10 | Atualizar estoque e preços | Positivo | `200 OK`, dados e data de atualização alterados | `deveListarBuscarAtualizarEExcluirPeca` |
| PEC-11 | Atualizar com valores negativos | Negativo | `400 Bad Request`, registro original preservado | `deveRejeitarAtualizacaoInvalidaSemAlterarAPeca` |
| PEC-12 | Atualizar peça inexistente | Negativo | `404 Not Found` | `deveRetornar404AoAtualizarPecaInexistente` |
| PEC-13 | Excluir peça existente | Positivo | `204 No Content` sem corpo | `deveListarBuscarAtualizarEExcluirPeca` |
| PEC-14 | Excluir peça inexistente | Negativo | `404 Not Found` | `deveRetornarErrosControladosParaPecaInexistenteEParametrosInvalidos` |

## API de serviços

| ID | Cenário | Tipo | Resultado esperado | Teste automatizado |
|---|---|---|---|---|
| SER-01 | Cadastrar serviço válido | Positivo | `201 Created`, `Location`, código e datas gerados | `deveCadastrarServicoValido` |
| SER-02 | Cadastrar duração mínima e custo zero | Borda | `201 Created` | `deveAceitarDuracaoMinimaECustoZero` |
| SER-03 | Cadastrar nome vazio, duração zero e custo negativo | Negativo | `400 Bad Request` com erros por campo | `deveRejeitarServicoComDadosInvalidos` |
| SER-04 | Enviar corpo ausente ou tipo de dado incorreto | Negativo | `400 Bad Request` controlado | `deveRejeitarCorpoAusenteETipoIncorreto` |
| SER-05 | Listar catálogo vazio | Borda | `200 OK` com `[]` | `deveListarCatalogoVazio` |
| SER-06 | Listar e buscar serviço existente | Positivo | `200 OK` | `deveListarBuscarAtualizarEExcluirServico` |
| SER-07 | Buscar serviço inexistente | Negativo | `404 Not Found` padronizado | `deveRetornarErrosControladosParaServicoInexistente` |
| SER-08 | Consultar com código de tipo inválido | Negativo | `400 Bad Request` padronizado | `deveRetornarErrosControladosParaServicoInexistente` |
| SER-09 | Atualizar duração e custo | Positivo | `200 OK` com dados atualizados | `deveListarBuscarAtualizarEExcluirServico` |
| SER-10 | Atualizar com duração e custo inválidos | Negativo | `400 Bad Request`, registro original preservado | `deveRejeitarAtualizacaoInvalidaSemAlterarOServico` |
| SER-11 | Atualizar serviço inexistente | Negativo | `404 Not Found` | `deveRetornarErrosControladosParaServicoInexistente` |
| SER-12 | Excluir serviço existente | Positivo | `204 No Content` sem corpo | `deveListarBuscarAtualizarEExcluirServico` |
| SER-13 | Excluir serviço inexistente | Negativo | `404 Not Found` | `deveRetornarErrosControladosParaServicoInexistente` |

## Repositories em memória

| ID | Cenário | Tipo | Resultado esperado | Teste automatizado |
|---|---|---|---|---|
| REP-01 | Chamar `getInstance()` repetidamente | Estrutural | Mesma instância e construtor privado | `deveRetornarSempreAMesmaInstancia` |
| REP-02 | Salvar dois registros | Borda | Códigos positivos, crescentes e distintos | `deveGerarCodigosUnicosERecusarAtualizacaoInexistente` |
| REP-03 | Cadastrar um registro | Positivo | Código, criação e atualização preenchidos | `deveExecutarOCicloDeVidaDaPeca` / `deveExecutarOCicloDeVidaDoServico` |
| REP-04 | Atualizar registro existente | Positivo | Apenas campos permitidos e data de atualização mudam | `deveExecutarOCicloDeVidaDaPeca` / `deveExecutarOCicloDeVidaDoServico` |
| REP-05 | Atualizar código inexistente | Negativo | `Optional.empty()` | `deveGerarCodigosUnicosERecusarAtualizacaoInexistente` |
| REP-06 | Excluir duas vezes | Borda | Primeira exclusão `true`, segunda `false` | `deveExecutarOCicloDeVidaDaPeca` / `deveExecutarOCicloDeVidaDoServico` |
| REP-07 | Tentar incluir item na lista retornada | Negativo | `UnsupportedOperationException` | `deveProtegerAListaInternaContraInclusoesExternas` |
| REP-08 | Reiniciar a aplicação | Arquitetural | Dados anteriores deixam de existir | Validado pelo desenho em memória; não automatizado dentro da mesma JVM |

## Execução

```powershell
$env:JAVA_HOME = (Resolve-Path .\.tools\jdk-21).Path
.\gradlew.bat test
```

O relatório HTML é gerado em `build/reports/tests/test/index.html`.

## Resultado da execução

Execução realizada em 25/08/2026 com Java Temurin 21 e Gradle 9.5.1:

| Suíte | Testes | Falhas |
|---|---:|---:|
| Contexto Spring Boot | 1 | 0 |
| Controller de peças | 9 | 0 |
| Controller de serviços | 8 | 0 |
| Repository de peças | 4 | 0 |
| Repository de serviços | 4 | 0 |
| **Total** | **26** | **0** |

Resultado final: `BUILD SUCCESSFUL`.
