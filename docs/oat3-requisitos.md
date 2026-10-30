# Requisitos da OAT 3

Resumo técnico conferido com o PDF da atividade.

## Persistência

- MySQL como banco oficial.
- JDBC nativo com SQL manual; JPA, Hibernate e Spring Data não são utilizados.
- Script versionado em `script_mecaniqa.sql`.
- Repositórios implementam o contrato genérico `Repositorio<T, ID>`.
- `RepositorioJdbcTemplate` padroniza abertura e fechamento dos recursos JDBC.
- Mappers convertem linhas de `ResultSet` para os modelos.

## Arquitetura e erros

- Controllers recebem DTOs, chamam Services e devolvem respostas HTTP.
- Services concentram regras de negócio, validação e chamadas aos repositórios.
- Exceções de domínio são próprias e tratadas explicitamente nos controllers.
- Não se utiliza `@ExceptionHandler` nem advice global.
- Os DTOs e as rotas mantêm o formato REST já utilizado pelo projeto.

## Entrega

- A branch de avaliação é `main`.
- A apresentação da OAT 3 deve ser versionada em `apresentacoes` com o padrão
  `mecaniQA_api_oat3_<nome_da_equipe>.pdf` e também entregue no Blackboard.
