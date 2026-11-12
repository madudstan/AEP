# Arquitetura final da PoC

A aplicação utiliza Spring Boot, programação orientada a objetos e separação em camadas:

```text
Controller -> Request DTO -> Service -> Repository -> MongoDB
MongoDB -> Model -> Service -> Mapper -> Response DTO -> Controller
```

- `controller`: recebe requisições HTTP e devolve códigos de status;
- `dto`: define contratos de entrada e saída, sem expor os documentos persistidos;
- `service`: concentra casos de uso e regras de negócio;
- `repository`: acessa as coleções MongoDB;
- `model`: representa documentos e subdocumentos;
- `mapper`: converte DTOs e modelos;
- `exception`: padroniza respostas de erro;
- `configuration`: configura o OpenAPI.

## Fluxos principais

1. `AlimentoController` mantém o CRUD da Primeira Entrega.
2. `InstituicaoController` cadastra instituições receptoras e seu endereço aninhado.
3. `SolicitacaoController` cria solicitações, consulta por instituição e altera status.
4. `SolicitacaoService` valida a instituição, verifica alimentos disponíveis e preserva os itens como lista de subdocumentos.

Não há acesso direto ao repository pelos controllers. As respostas são convertidas por mappers antes de serem retornadas.
