# Diagrama de Atividade — POST /api/ordens-servico

O diagrama apresenta exclusivamente a visão lógica do `OrdemServicoController`. As
operações internas do Service, Mapper e Repository ficam fora do escopo.

```mermaid
flowchart TD
    A([Receber POST /api/ordens-servico]) --> B[Desserializar corpo em CriarOrdemServicoRequest]
    B --> C{DTO válido?}
    C -- Não --> D[Encaminhar erro de validação ao tratamento global]
    D --> E[Responder 400 Bad Request]
    C -- Sim --> F[Chamar service.criar com o DTO]
    F --> G{Service retornou com sucesso?}
    G -- Não --> H[Propagar exceção ao tratamento global]
    H --> I[Responder com o status de erro correspondente]
    G -- Sim --> J[Receber OrdemServicoResponse]
    J --> K[Montar URI do recurso criado]
    K --> L[Responder 201 Created com Location e DTO]
    E --> M([Fim])
    I --> M
    L --> M
```
