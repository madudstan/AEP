# Sistema de Gestão e Redistribuição de Alimentos Excedentes

PoC desenvolvida para a AEP de Engenharia de Software, alinhada ao **ODS 12 — Consumo e Produção Responsáveis**. A solução organiza alimentos excedentes e permite que instituições receptoras registrem solicitações com vários itens.

## Equipe

Heloísa Sayuri Silva Saito (24062631-2), Maria Eduarda de Castro Lachimia (24055202-2) e Matheus Costa E Silva (2400729-2).

## Evolução da Primeira para a Segunda Entrega

A Primeira Entrega utilizava uma coleção `alimentos`, documentos simples e CRUD básico. Esta versão preserva esse fluxo e evolui a PoC com:

- coleção `instituicoes`, com endereço embutido;
- coleção `solicitacoes`, relacionada a uma instituição por `instituicaoId`;
- lista de subdocumentos `itens` dentro de cada solicitação;
- relacionamento de cada item com um alimento por `alimentoId`;
- regras de negócio para instituição existente, alimento disponível e unidade compatível;
- alteração de status da solicitação;
- testes unitários, HTTP e de integração com Testcontainers;
- documentação técnica e evidência de cobertura com JaCoCo.

## Tecnologias Utilizadas

- Java 21;
- Maven Wrapper;
- JaCoco;
- Spring Boot 3.5.16;
- Spring Web;
- Spring Data MongoDB;
- Jakarta Validation;
- Springdoc OpenAPI;
- Testcontainers;
- Docker e Docker Compose;
- JUnit 5;
- Mockito e MockMvc.

## Arquitetura

A aplicação mantém separação por responsabilidades:

```text
JSON -> Controller -> Request DTO -> Service -> Repository -> MongoDB
MongoDB -> Model -> Service -> Mapper -> Response DTO -> JSON
```

As camadas são `controller`, `dto`, `service`, `repository`, `model`, `mapper` e `exception`. Consulte `docs/architecture.md` para o fluxo completo.

## Modelagem NoSQL

O MongoDB possui três coleções:

- `alimentos`: excedentes cadastrados, mantendo o CRUD da Primeira Entrega;
- `instituicoes`: instituições receptoras com o documento embutido `endereco`;
- `solicitacoes`: pedidos relacionados a uma instituição e contendo a lista aninhada `itens`.

Exemplo resumido de solicitação:

```json
{
  "instituicaoId": "instituicao-1",
  "status": "PENDENTE",
  "itens": [
    { "alimentoId": "alimento-1", "quantidadeSolicitada": 10, "unidade": "kg" }
  ]
}
```

## Pré-requisitos

- JDK 21;
- Docker e Docker Compose;
- Git;
- IDE compatível, opcionalmente IntelliJ IDEA.

## Execução local

Na pasta `AepMongoJava2026-main`, inicie o MongoDB:

```bash
docker compose up -d
```

A aplicação usa por padrão `mongodb://root:Mongo@localhost:27018/alimentos?authSource=admin`, conforme `compose.yaml`. Para outra instalação, defina a variável `MONGODB_URI`.

Execute a aplicação:

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
mvnw.cmd spring-boot:run
```

A API fica em `http://localhost:8080` e a documentação Swagger em `http://localhost:8080/docs`.

### Front-end Recolhe+

O front-end funcional está incorporado aos recursos do próprio Spring Boot. Portanto, para usar o sistema normalmente, não é necessário iniciar Node, Vite ou outro terminal.

O fluxo simples é:

1. abrir o Docker Desktop e iniciar o container do MongoDB;
2. executar `AlimentosExcedentesApplication` pelo botão **Run** do IntelliJ;
3. abrir `http://localhost:8080`.

O Swagger continua disponível em `http://localhost:8080/docs`. A pasta `frontend/` permanece no projeto para futuras alterações visuais; nesse caso, depois de alterar o front-end, gere um novo build com `npm install` e `npm run build` e copie o conteúdo de `frontend/dist/` para `src/main/resources/static/`.

## Principais endpoints

### Alimentos

```text
POST   /api/alimentos
GET    /api/alimentos
GET    /api/alimentos/{id}
PUT    /api/alimentos/{id}
DELETE /api/alimentos/{id}
```

### Instituições

```text
POST   /api/instituicoes
GET    /api/instituicoes
GET    /api/instituicoes/{id}
PUT    /api/instituicoes/{id}
DELETE /api/instituicoes/{id}
```

### Solicitações

```text
POST   /api/solicitacoes
GET    /api/solicitacoes
GET    /api/solicitacoes/{id}
GET    /api/solicitacoes/instituicao/{instituicaoId}
PATCH  /api/solicitacoes/{id}/status
DELETE /api/solicitacoes/{id}
```

O fluxo recomendado para a demonstração é: cadastrar alimento, cadastrar instituição com endereço, criar solicitação com um ou mais itens e atualizar o status para `APROVADA` ou `ATENDIDA`.

## Testes e cobertura

Execute os testes unitários e de controller:

```bash
./mvnw test
```

Para executar também a verificação de cobertura e os testes de integração com MongoDB em Testcontainers:

```bash
./mvnw clean verify
```

O JaCoCo gera o relatório em `target/site/jacoco/index.html` e bloqueia o build quando a cobertura de linhas fica abaixo de 70%. Na validação local desta versão, foram executados 31 testes unitários/HTTP, com cobertura de linhas de 85,45%. Os testes de integração exigem Docker ativo e devem ser executados no ambiente final com `./mvnw clean verify`.

## Documentação e evidências

- `docs/architecture.md`: arquitetura e responsabilidades;
- `docs/database.md`: coleções, documentos e relacionamentos;
- `docs/http-api.md`: contratos HTTP e exemplos;
- `docs/decisions.md`: decisões técnicas da evolução;
- `docs/kanban.md`: quadro de tarefas e evidências de metodologia;
- `docs/demo-roteiro.md`: roteiro do vídeo de 3 a 5 minutos;
- `docs/versionamento.md`: identificação da versão final no GitHub.

## ODS 12

O sistema contribui para o consumo e a produção responsáveis ao organizar excedentes que ainda podem ser aproveitados e facilitar sua solicitação por instituições receptoras, reduzindo a chance de descarte.
