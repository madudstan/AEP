# Roteiro do vídeo — Segunda Entrega da AEP

**Duração planejada:** aproximadamente 5 minutos  
**Participantes:** três integrantes  
**PoC:** Recolhe+ — Gestão e Redistribuição de Alimentos Excedentes  
**ODS:** ODS 12 — Consumo e Produção Responsáveis

> O vídeo da Segunda Entrega deve ter de 3 a 5 minutos e apresentar: a evolução da solução, o projeto final, a demonstração da PoC, as tecnologias utilizadas, os testes automatizados, a cobertura obtida e os principais resultados.

## Preparação antes de gravar

- Deixar o Docker Desktop aberto e o container do MongoDB em execução.
- Iniciar a aplicação pelo IntelliJ com `AlimentosExcedentesApplication`.
- Abrir o sistema em `http://localhost:8080`.
- Deixar o Swagger disponível em `http://localhost:8080/docs`.
- Deixar o MongoDB Compass conectado em `localhost:27018`, no banco `alimentos`.
- Deixar aberta a pasta `src/test/java` ou uma execução anterior dos testes.
- Usar dados fictícios, sem CPF, CNPJ ou informações reais.
- Para não perder tempo, deixar prontos os dados de teste e excluir registros antigos antes da gravação.

---

## Parte 1 — Integrante 1: contexto, problema e evolução

**Tempo aproximado: 1 minuto e 10 segundos**

### Fala sugerida

> “Olá. Nós somos a equipe responsável pela PoC Recolhe+, desenvolvida para a AEP de Engenharia de Software. A nossa solução está relacionada ao ODS 12 da ONU, que trata de consumo e produção responsáveis.”

> “O problema que identificamos é o desperdício de alimentos que ainda estão próprios para consumo. Muitas empresas e estabelecimentos possuem excedentes, enquanto pessoas e instituições precisam de apoio alimentar. Sem organização, esses alimentos podem ser descartados e não chegar a quem precisa.”

> “Por isso, propusemos um sistema para organizar os alimentos excedentes, registrar quem disponibiliza esses alimentos, registrar as solicitações e acompanhar até a entrega ao destinatário.”

> “Na Primeira Entrega, tínhamos uma PoC inicial com uma coleção de alimentos e operações básicas de CRUD. Na Segunda Entrega, evoluímos a solução de forma incremental, sem descartar o que já funcionava.”

> “A evolução acrescentou múltiplas coleções no MongoDB, instituições relacionadas aos pedidos, solicitações com vários itens, documentos complexos com endereço, regras de negócio, alteração de status, histórico de pessoas atendidas, front-end, documentação técnica, testes automatizados e cobertura de código.”

> “Com isso, a solução passou de um cadastro simples de alimentos para um fluxo completo de redistribuição: alimento disponível, instituição doadora, solicitação, aprovação e atendimento.”

### O que mostrar na tela

- Mostrar rapidamente a tela inicial do Recolhe+.
- Mostrar o menu com **Alimentos**, **Instituições**, **Solicitações** e **Pessoas atendidas**.
- Não gastar tempo navegando ainda; a demonstração completa ficará com o terceiro integrante.

### Requisitos do PDF cobertos nesta parte

- Problema real e público/contexto identificável;
- alinhamento com o ODS;
- evolução da Primeira para a Segunda Entrega;
- funcionalidade final da PoC;
- principais resultados esperados.

---

## Parte 2 — Integrante 2: arquitetura, código e banco de dados

**Tempo aproximado: 1 minuto e 15 segundos**

### Fala sugerida

> “Tecnicamente, o projeto foi desenvolvido em Java 21 com Spring Boot, usando programação orientada a objetos e Spring Data MongoDB. Também utilizamos Swagger para documentar a API, JUnit e Mockito para os testes, JaCoCo para cobertura e Docker para executar o MongoDB.”

> “A aplicação foi organizada em camadas. O Controller recebe as requisições HTTP. Os DTOs representam os dados de entrada e saída. O Service concentra os casos de uso e as regras de negócio. O Repository acessa o MongoDB. Os Models representam os documentos, os Mappers convertem DTOs e modelos e o Exception Handler padroniza os erros.”

> “O fluxo principal é: JSON, Controller, DTO, Service, Repository e MongoDB. Na resposta, o caminho retorna pelo Model, Service, Mapper, Response DTO e Controller.”

> “No banco NoSQL temos três coleções principais: `alimentos`, `instituicoes` e `solicitacoes`. A coleção de instituições possui um endereço embutido. A coleção de solicitações possui uma lista de itens como subdocumentos, e cada item se relaciona a um alimento por meio do `alimentoId`.”

> “As regras de negócio verificam se a instituição existe, se o alimento está disponível e se a unidade informada é compatível. A solicitação também possui estados como pendente, aprovada, recusada e atendida.”

> “O Docker executa o MongoDB em container. A aplicação se conecta ao banco `alimentos` pela porta local 27018, enquanto o backend disponibiliza a API e o front-end em `localhost:8080`.”

### O que mostrar na tela

- Mostrar rapidamente no IntelliJ as pastas:
  - `controller`;
  - `dto`;
  - `service`;
  - `repository`;
  - `model`;
  - `mapper`;
  - `exception`.
- Mostrar o arquivo `compose.yaml` ou o Docker Desktop por alguns segundos.
- Mostrar no MongoDB Compass as três coleções, sem abrir muitos documentos ainda.

### Requisitos do PDF cobertos nesta parte

- Uso efetivo de banco NoSQL;
- programação orientada a objetos;
- organização e qualidade do código;
- relacionamento entre coleções;
- documentos complexos, endereço embutido e lista de subdocumentos;
- tecnologias utilizadas;
- execução e arquitetura da solução.

---

## Parte 3 — Integrante 3: demonstração da PoC, banco e testes

**Tempo aproximado: 2 minutos e 35 segundos**

> Esta parte é maior porque demonstra o fluxo funcional completo solicitado no vídeo.

### 3.1 Cadastrar a instituição doadora — aproximadamente 25 segundos

**Ação:** abrir **Instituições > Nova instituição**.

**Dados fictícios sugeridos:**

```text
Nome: Supermercado Esperança
Documento: 12.345.678/0001-00
Telefone: (44) 99999-9999
E-mail: contato@esperanca.com
Rua: Avenida Brasil
Número: 1000
Cidade: Maringá
Estado: PR
```

**Fala sugerida:**

> “Agora vamos demonstrar o fluxo principal. Primeiro cadastramos a instituição doadora, que representa a empresa ou estabelecimento que disponibiliza o alimento. O endereço é armazenado como um documento embutido no MongoDB. O ID é gerado automaticamente pelo sistema.”

Clicar em **Salvar instituição** e mostrar que ela aparece na lista.

### 3.2 Cadastrar um alimento pelo Swagger — aproximadamente 25 segundos

**Ação:** abrir `http://localhost:8080/docs`, acessar `POST /api/alimentos`, clicar em **Try it out** e usar um corpo semelhante a:

```json
{
  "nome": "Arroz",
  "quantidade": 20,
  "unidade": "kg",
  "dataValidade": "2026-12-30",
  "origem": "Supermercado Esperança",
  "status": "DISPONIVEL"
}
```

**Fala sugerida:**

> “Neste momento, vamos cadastrar um alimento diretamente pela API documentada no Swagger. O ID não é digitado: o sistema gera automaticamente um identificador curto e devolve esse ID na resposta. Como o front-end e o Swagger usam a mesma API e o mesmo MongoDB, esse alimento também aparece no site depois da atualização.”

Abrir `GET /api/alimentos`, executar e mostrar o alimento e o ID retornado.

### 3.3 Criar a solicitação no front-end — aproximadamente 45 segundos

**Ação:** voltar ao site, abrir **Solicitações > Nova solicitação**.

Preencher:

```text
Instituição doadora: Supermercado Esperança
Nome da pessoa/instituição atendida: Casa de Apoio Vida Nova
CPF/CNPJ: documento fictício
Rua e número: Rua das Flores, 200
Cidade: Maringá
Estado: PR
Motivo: Apoio alimentar
Produto: Arroz
Quantidade: 5 kg
```

**Fala sugerida:**

> “Aqui é importante separar os papéis: a instituição doadora é quem fornece o alimento. Já a pessoa ou instituição atendida é quem receberá a entrega, por isso seus dados são preenchidos separadamente. A solicitação pode conter um ou mais itens, que são armazenados como uma lista de subdocumentos.”

Clicar em **Enviar solicitação** e mostrar a solicitação pendente.

### 3.4 Aprovar e concluir a entrega — aproximadamente 25 segundos

**Ação:** na solicitação:

1. clicar em **Aprovar**;
2. clicar em **Marcar atendida**;
3. abrir **Pessoas atendidas**.

**Fala sugerida:**

> “Depois de criada, a solicitação começa como pendente. Podemos aprová-la e, após a entrega, marcá-la como atendida. Ao concluir o fluxo, o sistema registra a data da entrega e cria o histórico do destinatário.”

Mostrar o card contendo:

- pessoa ou instituição atendida;
- documento;
- produto;
- endereço;
- motivo;
- data da entrega.

### 3.5 Mostrar o MongoDB — aproximadamente 20 segundos

**Ação:** abrir o MongoDB Compass conectado em:

```text
mongodb://root:Mongo@localhost:27018/alimentos?authSource=admin
```

Abrir rapidamente:

- `alimentos`;
- `instituicoes`;
- `solicitacoes`.

**Fala sugerida:**

> “No MongoDB podemos confirmar a persistência dos dados. Temos uma coleção de alimentos, uma de instituições e uma de solicitações. A instituição possui o endereço embutido e a solicitação possui a lista de itens e os dados do destinatário.”

### 3.6 Mostrar testes e cobertura — aproximadamente 15 segundos

**Ação:** mostrar a pasta `src/test/java` no IntelliJ ou executar no terminal:

```powershell
.\mvnw.cmd test
```

Em seguida, mostrar o relatório do JaCoCo, se já estiver gerado, em:

```text
target/site/jacoco/index.html
```

**Fala sugerida:**

> “Por fim, temos testes automatizados para controllers, services, mappers e regras de negócio. A suíte possui 31 testes executados sem falhas. A cobertura é verificada pelo JaCoCo e atende ao mínimo obrigatório de 70% solicitado pela AEP.”

### 3.7 Encerramento — aproximadamente 10 segundos

> “Assim, a solução final atende ao fluxo proposto: organiza alimentos excedentes, registra a instituição doadora, cria solicitações, acompanha os status, registra as entregas e mantém os dados persistidos em um banco NoSQL. Obrigado.”

### Requisitos do PDF cobertos nesta parte

- demonstração da PoC funcional;
- cadastro e consulta de dados;
- uso do MongoDB;
- tecnologias utilizadas;
- testes automatizados;
- cobertura mínima de 70%;
- principais resultados alcançados.

---

## Divisão final de tempo

| Parte | Integrante | Tempo |
|---|---|---:|
| Contexto, ODS, problema e evolução | 1 | 1min10s |
| Código, arquitetura e banco | 2 | 1min15s |
| Demonstração, MongoDB e testes | 3 | 2min35s |
| **Total** | **3 pessoas** | **5min00s** |

## Checklist de conformidade com a Segunda Entrega

- [x] Evolução da solução em relação à Primeira Entrega;
- [x] Projeto final funcional;
- [x] MongoDB como banco NoSQL;
- [x] Múltiplas coleções;
- [x] Relacionamento entre documentos;
- [x] Documento complexo com endereço embutido;
- [x] Lista de itens como subdocumentos;
- [x] Programação orientada a objetos e arquitetura em camadas;
- [x] Front-end e API REST demonstrados;
- [x] Testes automatizados demonstrados;
- [x] Cobertura mínima obrigatória de 70% demonstrada;
- [x] Docker e MongoDB explicados;
- [x] Principais resultados apresentados;
- [x] Vídeo planejado entre 3 e 5 minutos.

## Observação sobre o envio

Além do vídeo, conferir no repositório ou no pacote final os itens exigidos pelo PDF: `README.md`, código-fonte atualizado, documentação técnica, testes automatizados, instrução ou evidência reproduzível da cobertura e identificação clara da versão da Segunda Entrega no GitHub.
