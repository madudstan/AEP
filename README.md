# Recolhe+

PoC desenvolvida para a AEP de Engenharia de Software, alinhada ao **ODS 12 — Consumo e Produção Responsáveis**.

A solução organiza alimentos excedentes e permite que instituições receptoras registrem solicitações com vários itens, facilitando o controle das doações e das entregas.

---

## Equipe

Heloísa Sayuri Silva Saito (24062631-2), Maria Eduarda de Castro Lachimia (24055202-2) e Matheus Costa E Silva (2400729-2).

---

## Evolução da Primeira para a Segunda Entrega

A Primeira Entrega utilizava uma coleção `alimentos`, documentos simples e operações básicas de CRUD.

Na Segunda Entrega, a solução foi evoluída com:

- Coleção `instituicoes`, com endereço embutido;
- Coleção `solicitacoes`, relacionada a uma instituição por `instituicaoId`;
- Lista de subdocumentos `itens` dentro de cada solicitação;
- Relacionamento de cada item com um alimento por `alimentoId`;
- Regras de negócio para verificar instituição existente;
- Verificação de alimento disponível;
- Verificação de unidade compatível;
- Alteração do status das solicitações;
- Edição e exclusão de alimentos e instituições;
- Front-end para utilização da aplicação;
- Testes unitários, HTTP e de integração;
- Documentação técnica;
- Relatório de cobertura com JaCoCo

---

# Tecnologias Utilizadas

## Linguagens de programação

- Java 21;
- JavaScript.

## Estrutura e bibliotecas

- Spring Boot 3.5.16;
- Spring Web;
- Spring Data MongoDB;
- Jakarta Validation;
- Springdoc OpenAPI;
- React;
- Vite;
- JUnit 5;
- Mockito;
- MockMvc;
- Testcontainers;
- JaCoCo.

## Banco de dados

- MongoDB;
- Docker;
- Docker Compose.

## Projeto e versionamento

- Apache Maven;
- Maven Wrapper;
- Git;
- GitHub.

---

# Arquitetura

A aplicação foi organizada em camadas, com responsabilidades separadas:

```text
JSON -> Controller -> Request DTO -> Service -> Repository -> MongoDB
MongoDB -> Model -> Service -> Mapper -> Response DTO -> JSON
```

As principais camadas são:

- `controller`: recebe as requisições HTTP;
- `dto`: define os dados de entrada e saída;
- `service`: concentra as regras de negócio;
- `repository`: realiza o acesso ao MongoDB;
- `model`: representa os documentos armazenados;
- `mapper`: converte modelos em DTOs e DTOs em modelos;
- `exception`: trata os erros da aplicação.

---

# Modelagem NoSQL

O MongoDB possui três coleções principais:

- `alimentos`: alimentos excedentes cadastrados;
- `instituicoes`: instituições com endereço embutido;
- `solicitacoes`: solicitações relacionadas a instituições e contendo uma lista de itens.

Exemplo resumido de uma solicitação:

```json
{
  "instituicaoId": "instituicao-1",
  "status": "PENDENTE",
  "itens": [
    {
      "alimentoId": "alimento-1",
      "quantidadeSolicitada": 10,
      "unidade": "kg"
    }
  ]
}
```

---

# Pré-requisitos

Para executar o projeto, é necessário ter instalado:

- JDK 21;
- Docker Desktop;
- Docker Compose;
- Git;
- IntelliJ IDEA ou outra IDE compatível.

---

# Execução

Primeiro, abra o Docker Desktop e a IDE de sua preferência, como o IntelliJ IDEA.  Depois, abra o projeto na pasta principal, aquela que contém o arquivo: ```compose.yaml ```

No terminal da IDE, execute:

```powershell
docker compose up -d
```

Esse comando inicia o MongoDB e o Mongo Express em containers. Depois que o container estiver em execução, abra o MongoDB Compass e conecte-se utilizando:

```text
mongodb://root:Mongo@localhost:27018/alimentos?authSource=admin
```

A aplicação utiliza essa conexão por padrão, conforme definido no arquivo `compose.yaml`.  Em seguida, no IntelliJ IDEA, execute a classe principal:

```text
AlimentosExcedentesApplication
```

Após a inicialização, a aplicação ficará disponível em:

```text
http://localhost:8080
```

A documentação da API pelo Swagger está disponível em:

```text
http://localhost:8080/docs
```

---

# Principais endpoints

## Alimentos

```text
POST   /api/alimentos
GET    /api/alimentos
GET    /api/alimentos/{id}
PUT    /api/alimentos/{id}
DELETE /api/alimentos/{id}
```

## Instituições

```text
POST   /api/instituicoes
GET    /api/instituicoes
GET    /api/instituicoes/{id}
PUT    /api/instituicoes/{id}
DELETE /api/instituicoes/{id}
```

## Solicitações

```text
POST   /api/solicitacoes
GET    /api/solicitacoes
GET    /api/solicitacoes/{id}
GET    /api/solicitacoes/instituicao/{instituicaoId}
PATCH  /api/solicitacoes/{id}/status
DELETE /api/solicitacoes/{id}
```

---

# Testes e cobertura

### A cobertura geral obtida ficou em aproximadamente: 89%

Para executar os testes automatizados no Windows:

```powershell
.\mvnw.cmd test
```

Para executar os testes e gerar o relatório de cobertura:

```powershell
.\mvnw.cmd clean verify
```

O JaCoCo gera o relatório em:

```text
target/site/jacoco/index.html
```
---

# Documentação

A pasta `docs/` contém os documentos complementares do projeto:

- `architecture.md`: arquitetura e responsabilidades;
- `database.md`: coleções, documentos e relacionamentos;
- `http-api.md`: endpoints e contratos da API;
- `decisions.md`: decisões técnicas da evolução;
- `kanban.md`: quadro de tarefas da equipe;
- `demo-roteiro.md`: roteiro de demonstração;
- `versionamento.md`: informações sobre a versão do projeto;
- `roteiro-video-segunda-entrega.md`: roteiro do vídeo da Segunda Entrega.

---

# ODS 12

O projeto está relacionado ao **ODS 12 — Consumo e Produção Responsáveis**.

A aplicação contribui para esse objetivo ao organizar alimentos excedentes que ainda podem ser aproveitados e facilitar sua redistribuição para instituições e pessoas atendidas, reduzindo o desperdício de alimentos.
