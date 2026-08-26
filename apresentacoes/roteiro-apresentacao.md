# Roteiro de apresentação — MecâniQA API OAT 1

## Orientações gerais

- **Duração máxima:** 7 minutos.
- **Apresentador:** um integrante, conforme a orientação da atividade.
- **Formato:** 7 slides, seguindo o modelo institucional da UNEX.
- **Nome do PDF final:** `mecaniQA_api_oat1_belo_horizonte.pdf`.
- **Local do PDF no repositório:** `apresentacoes/`.
- **Ritmo recomendado:** aproximadamente 125 palavras por minuto, com pequenas pausas.
- **Antes de apresentar:** substituir todos os campos entre colchetes pelos dados reais do grupo.

## Distribuição do tempo

| Slide | Assunto | Início | Duração |
|---|---|---:|---:|
| 1 | Capa e apresentação do projeto | 00:00 | 00:30 |
| 2 | Agenda, problema e objetivo | 00:30 | 00:40 |
| 3 | Escopo do MVP e arquitetura | 01:10 | 01:05 |
| 4 | Modelagem orientada a objetos | 02:15 | 01:25 |
| 5 | API REST, Singleton e demonstração | 03:40 | 01:45 |
| 6 | Testes, resultados e limitações | 05:25 | 01:05 |
| 7 | Conclusão e referências | 06:30 | 00:30 |
|  | **Total** |  | **07:00** |

---

## Slide 1 — Capa

### Conteúdo do slide

- Título: **MecâniQA API — Catálogo de Peças e Serviços**.
- Subtítulo: **OAT 1 — Desenvolvimento Web Orientado a Objetos**.
- Autores: `[NOME COMPLETO DOS INTEGRANTES]`.
- Curso: Sistemas de Informação.
- Local e ano: **Belo Horizonte, 2026**.

### Fala sugerida — 30 segundos

> Bom dia/boa noite. Somos [nomes dos integrantes] e vamos apresentar a MecâniQA API, backend de
> uma oficina mecânica fictícia desenvolvido para a OAT 1. A solução administra o catálogo de
> peças e serviços por uma API REST construída em Java 21 com Spring Boot, aplicando orientação
> a objetos, o padrão Singleton e armazenamento exclusivamente em memória.

### Orientação ao apresentador

- Não leia todos os nomes lentamente; apresente o grupo de forma natural.
- Confira se a capa diz **Belo Horizonte**, pois o modelo original contém “Feira de Santana”.

### Transição

> Primeiro, vamos entender o problema e o objetivo da solução.

---

## Slide 2 — Agenda, problema e objetivo

### Conteúdo do slide

**Objetivo:** criar uma API REST para gerenciar peças e serviços da oficina MecâniQA.

Agenda:

1. Problema e escopo do MVP;
2. Arquitetura da solução;
3. Modelagem orientada a objetos;
4. API REST e padrão Singleton;
5. Testes e resultados.

### Fala sugerida — 40 segundos

> A oficina precisa organizar suas peças e seus serviços. Sem uma estrutura centralizada,
> consultas de estoque, atualizações de preço e verificações de custos ficam sujeitas a
> inconsistências. O MVP criou uma API REST com o CRUD completo desses dois recursos. A
> apresentação aborda escopo, arquitetura, orientação a objetos, Singleton, funcionamento da API
> e resultados dos testes.

### Conceito de apoio

**CRUD** significa criar, consultar, atualizar e excluir dados. Em inglês: *Create, Read,
Update and Delete*.

### Transição

> Com esse objetivo definido, estruturamos um MVP com responsabilidades bem separadas.

---

## Slide 3 — Escopo do MVP e arquitetura

### Conteúdo do slide

Apresentar visualmente o fluxo:

```text
Cliente HTTP
    ↓
Controller REST
    ↓
Validação do DTO
    ↓
Repository Singleton
    ↓
Lista em memória
```

Ao lado do fluxo, destacar:

- CRUD de peças e serviços;
- 10 endpoints REST;
- códigos e datas gerados automaticamente;
- validação dos dados de entrada;
- respostas de erro padronizadas;
- sem banco de dados, Spring Data ou `@Autowired`.

### Fala sugerida — 1 minuto e 5 segundos

> O MVP possui dois módulos principais: peças e serviços. Para cada módulo implementamos as
> operações de cadastro, listagem, consulta por código, atualização e exclusão, totalizando dez
> endpoints. O cliente envia uma requisição HTTP para o controller. O Spring transforma o JSON
> recebido em um DTO e executa as validações. Quando os dados são válidos, o controller acessa o
> repository Singleton, que realiza a operação em uma lista na memória. Por fim, a API devolve
> uma resposta JSON com o status adequado.
>
> Essa arquitetura respeita as restrições da atividade. Não utilizamos banco de dados,
> Spring Data nem injeção de dependência com `@Autowired`. Por isso, os dados existem apenas
> enquanto a aplicação está em execução e são perdidos quando ela é reiniciada. Utilizamos DTOs
> distintos para cadastro e atualização, impedindo que o cliente controle campos internos como
> código e datas. A aplicação foi construída com Java 21, Spring Boot 4.1.0 e Gradle 9.5.1.

### Orientação visual

- Use o fluxo como elemento principal do slide.
- Evite colocar a árvore completa de pastas; cite somente `controller`, `dto`, `model`,
  `repository` e `exception`.

### Transição

> A separação dessas responsabilidades começa nas classes que representam o domínio.

---

## Slide 4 — Modelagem orientada a objetos

### Conteúdo do slide

Inserir uma versão legível do diagrama contendo:

- `Peca`;
- `Servico`;
- `CategoriaPeca`;
- `PecaRepository` e `ServicoRepository`;
- `PecaController` e `ServicoController`.

Destacar os conceitos:

- atributos privados e encapsulamento;
- construtores;
- getters e setters;
- enumeração;
- membros estáticos;
- associação entre controller, repository e entidade.

### Fala sugerida — 1 minuto e 25 segundos

> Na modelagem orientada a objetos, a classe `Peca` representa um item do estoque. Ela possui
> código único, código de barras, fornecedor ou marca, quantidade, preços de custo e venda,
> datas de cadastro e atualização, além de tamanho e cor opcionais. Cada peça também possui uma
> categoria obrigatória. Essa categoria é representada pelo enum `CategoriaPeca`, com os valores
> motor, suspensão, freios, elétrica e acessórios. O enum evita valores livres e inconsistentes.
>
> A classe `Servico` possui código, nome, duração estimada em minutos, custo tabelado e datas de
> criação e atualização. Nas duas entidades, os atributos são privados. O acesso controlado por
> getters e setters aplica encapsulamento e permite que a classe preserve seu próprio estado.
> Os construtores inicializam os objetos, enquanto os repositories completam os campos gerados
> pela aplicação, como código e datas.
>
> Os controllers dependem dos repositories para executar o CRUD. Os repositories mantêm as
> entidades em listas na memória. A palavra-chave `static` aparece na instância única de cada
> repository e no método `getInstance()`, permitindo o acesso compartilhado sem criar vários
> repositórios. O diagrama mostra os dados do domínio e as responsabilidades entre as classes.

### Pontos para indicar no diagrama

1. A seta de `Peca` para `CategoriaPeca` representa a categoria obrigatória.
2. A agregação dos repositories indica que eles mantêm objetos em memória.
3. A dependência dos controllers mostra o acesso por `getInstance()`.
4. O modificador `-` indica membro privado; `+` indica membro público.

### Transição

> A partir dessa modelagem, expusemos as operações por meio dos controllers REST.

---

## Slide 5 — API REST, Singleton e demonstração

### Conteúdo do slide

Mostrar uma tabela compacta:

| Operação | Peças | Serviços | Resposta de sucesso |
|---|---|---|---|
| Criar | `POST /api/pecas` | `POST /api/servicos` | `201 Created` |
| Listar | `GET /api/pecas` | `GET /api/servicos` | `200 OK` |
| Buscar | `GET /api/pecas/{codigo}` | `GET /api/servicos/{codigo}` | `200 OK` |
| Atualizar | `PUT /api/pecas/{codigo}` | `PUT /api/servicos/{codigo}` | `200 OK` |
| Excluir | `DELETE /api/pecas/{codigo}` | `DELETE /api/servicos/{codigo}` | `204 No Content` |

Também mostrar um recorte pequeno do Singleton:

```java
private static final PecaRepository INSTANCE = new PecaRepository();

private PecaRepository() {}

public static PecaRepository getInstance() {
    return INSTANCE;
}
```

### Fala sugerida — 1 minuto e 45 segundos

> Os controllers publicam dez endpoints agrupados em `/api/pecas` e `/api/servicos`. O método
> POST cadastra um recurso e retorna `201 Created`, incluindo o cabeçalho `Location`. Os métodos
> GET retornam `200 OK` para listagem ou consulta por código. O PUT atualiza somente os campos
> permitidos e também retorna `200 OK`. O DELETE retorna `204 No Content`, sem corpo. Quando o
> código não existe, a resposta é `404 Not Found`. Dados inválidos, JSON malformado ou código em
> formato incorreto produzem `400 Bad Request`.
>
> Antes de chegar ao repository, o corpo da requisição é validado. Textos obrigatórios não podem
> estar vazios, estoque e valores monetários não podem ser negativos, a duração deve ser maior
> que zero e a categoria da peça é obrigatória. Um tratamento global mantém os erros em JSON
> padronizado, com data, status, mensagem, caminho e erros de campo.
>
> O armazenamento utiliza o padrão Singleton. Cada repository possui um construtor privado, uma
> instância única estática e o método público `getInstance()`. Dessa forma, os controllers usam o
> mesmo objeto e, consequentemente, a mesma lista em memória. Os códigos são gerados por um
> `AtomicLong`, garantindo valores positivos, crescentes e únicos durante a execução. A coleção
> Postman contém as dez requisições e salva automaticamente os códigos criados para reutilizá-los
> nas operações seguintes.

### Demonstração opcional no Postman

Caso a demonstração ao vivo faça parte dos 7 minutos, limite-a a aproximadamente 35 segundos:

1. Executar **Cadastrar peça** e apontar o `201`, o `Location`, o código e as datas.
2. Executar **Atualizar peça** e mostrar o `200` e os novos valores.
3. Executar **Excluir peça** e mostrar o `204` sem corpo.
4. Executar **Buscar peça excluída** e mostrar o `404` padronizado.

Para manter o tempo total, reduza a explicação oral deste slide durante a demonstração.

### Transição

> Depois da implementação, validamos tanto o comportamento HTTP quanto a estrutura dos
> repositories.

---

## Slide 6 — Testes, resultados e limitações

### Conteúdo do slide

Indicadores principais:

- **26 testes automatizados**;
- **0 falhas**;
- **BUILD SUCCESSFUL**;
- testes de controller com MockMvc;
- testes unitários dos repositories;
- casos positivos, negativos e de borda.

Distribuição:

| Suíte | Testes |
|---|---:|
| Contexto Spring Boot | 1 |
| Controller de peças | 9 |
| Controller de serviços | 8 |
| Repository de peças | 4 |
| Repository de serviços | 4 |
| **Total** | **26** |

### Fala sugerida — 1 minuto e 5 segundos

> A solução foi validada por 26 testes automatizados, todos concluídos sem falhas. Os testes de
> controller utilizam MockMvc para verificar os status HTTP, os corpos JSON, as validações e o
> tratamento de erros. Os testes dos repositories verificam o ciclo completo de cadastro,
> consulta, atualização e exclusão, além da geração de códigos, da proteção da lista interna e
> da característica Singleton, confirmando que chamadas repetidas a `getInstance()` devolvem a
> mesma instância.
>
> Também foram testados casos de borda e negativos, como catálogo vazio, valores iguais a zero,
> campos opcionais ausentes, enum inválido, JSON malformado e recurso inexistente. O resultado
> final foi `BUILD SUCCESSFUL`. A principal limitação é intencional: como o armazenamento é em
> memória, não existe persistência após reiniciar a aplicação. Em uma evolução futura, um banco
> de dados poderia ser incorporado, desde que as restrições desta atividade fossem removidas.

### Transição

> Com isso, concluímos que o MVP atende ao escopo funcional e aos requisitos arquiteturais.

---

## Slide 7 — Conclusão e referências

### Conteúdo do slide

Conclusão:

- CRUD completo de peças e serviços;
- requisitos de POO e Singleton atendidos;
- API validada por testes e coleção Postman;
- documentação e UML disponíveis no repositório.

Referências:

- Repositório GitHub do projeto;
- documentação oficial do Java;
- documentação oficial do Spring Boot;
- materiais da OAT 1;
- collection Postman e documentação interna do projeto.

### Fala sugerida — 30 segundos

> Em conclusão, a MecâniQA API entrega o CRUD completo de peças e serviços, utiliza os conceitos
> de orientação a objetos solicitados, aplica o Singleton nos repositories e respeita a
> restrição de armazenamento em memória. A API possui validações, erros padronizados, dez
> endpoints, documentação, diagrama UML, coleção Postman e 26 testes aprovados. As referências e
> o código-fonte estão disponíveis no repositório do projeto. Obrigado pela atenção. Estamos à
> disposição para perguntas.

---

## Preparação antes da apresentação

### Checklist técnico

1. Confirmar que o JDK 21 está disponível.
2. Executar os testes:

   ```powershell
   .\gradlew.bat test
   ```

3. Iniciar a API:

   ```powershell
   .\gradlew.bat bootRun
   ```

4. Confirmar a aplicação em `http://localhost:8080`.
5. Importar `postman/MecaniQA-OAT1.postman_collection.json`.
6. Executar previamente a collection para confirmar as dez requisições.
7. Fechar notificações e programas que possam aparecer durante a apresentação.
8. Manter uma imagem dos resultados dos testes como alternativa à demonstração ao vivo.
9. Exportar os slides em PDF e revisar todas as páginas.
10. Salvar o arquivo como `apresentacoes/mecaniQA_api_oat1_belo_horizonte.pdf`.

### Checklist visual

- Remover todos os textos de exemplo do modelo.
- Alterar “Feira de Santana” para “Belo Horizonte”.
- Conferir título, autores e ano.
- Usar fontes grandes e pouco texto por slide.
- Garantir que o diagrama seja legível quando projetado.
- Manter a numeração das páginas.
- Usar capturas reais do projeto, sem dados sensíveis.
- Conferir se links e referências estão corretos.

## Perguntas prováveis e respostas sugeridas

### Por que não foi utilizado banco de dados?

> O armazenamento exclusivamente em memória era um requisito da atividade. Por isso, não
> utilizamos banco de dados nem Spring Data. Os dados são mantidos em listas e deixam de existir
> quando a aplicação é encerrada.

### Por que os repositories não usam `@Autowired`?

> A injeção de dependência também foi proibida pelo enunciado. Os controllers obtêm o repository
> pelo método estático `getInstance()`, conforme o padrão Singleton solicitado.

### Como o Singleton é garantido?

> O construtor é privado, a classe mantém uma única instância estática e o único acesso público
> ocorre pelo método `getInstance()`. Os testes confirmam que chamadas repetidas retornam o mesmo
> objeto.

### Como os códigos são gerados?

> Cada repository possui um `AtomicLong`. A cada cadastro, o contador gera um código positivo,
> crescente e único durante aquela execução da aplicação.

### Qual é a diferença entre entidade e DTO?

> A entidade representa o objeto completo armazenado pela aplicação. O DTO representa somente
> os dados aceitos em uma requisição. Isso impede que o cliente defina campos internos, como
> código e datas, e permite aplicar validações específicas para cadastro e atualização.

### Por que foi utilizado um enum para a categoria?

> O enum restringe a categoria a um conjunto conhecido de valores. Assim, a API evita grafias
> diferentes ou categorias inválidas e torna o contrato mais previsível.

### O que acontece quando um recurso não existe?

> Consultas, atualizações e exclusões de códigos inexistentes retornam `404 Not Found` em um JSON
> de erro padronizado. Dados inválidos retornam `400 Bad Request`.

### Quais campos podem ser atualizados?

> Em peças, podem ser atualizados estoque, preço de custo e preço de venda. Em serviços, duração
> estimada e custo tabelado. Código, nome, identificação e data de criação são preservados.

### A aplicação está pronta para produção?

> Ela está pronta para o escopo acadêmico do MVP. Para produção seriam necessários persistência
> em banco, autenticação, autorização, observabilidade, configuração por ambiente e uma análise
> mais ampla de concorrência e segurança.

## Plano de contingência

Se a API ou o Postman não funcionar durante a apresentação:

1. Não interromper a explicação para depurar.
2. Mostrar uma captura do `BUILD SUCCESSFUL` e das respostas `201`, `200`, `204` e `404`.
3. Explicar que a collection completa está versionada no repositório.
4. Prosseguir para os resultados e a conclusão, preservando o limite de 7 minutos.
