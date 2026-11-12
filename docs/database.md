# Modelagem do banco NoSQL

O MongoDB é utilizado de forma efetiva pela aplicação e possui três coleções.

## Coleções

### `alimentos`

Mantém os documentos simples da Primeira Entrega: nome, quantidade, unidade, validade, origem e status.

### `instituicoes`

Armazena instituições receptoras. O campo `endereco` é um documento embutido:

```json
{
  "nome": "Instituto Esperança",
  "documento": "12345678000100",
  "email": "contato@esperanca.org",
  "endereco": {
    "rua": "Rua A",
    "numero": "100",
    "cidade": "Maringá",
    "estado": "PR",
    "cep": "87000000"
  }
}
```

### `solicitacoes`

Relaciona a instituição pelo campo `instituicaoId` e guarda uma lista de subdocumentos `itens`:

```json
{
  "instituicaoId": "instituicao-1",
  "dataSolicitacao": "2026-11-08",
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

## Relacionamentos

A aplicação usa referências lógicas por identificador, uma abordagem adequada para a flexibilidade do MongoDB:

- `solicitacoes.instituicaoId` aponta para `instituicoes._id`;
- `solicitacoes.itens[].alimentoId` aponta para `alimentos._id`.

Na criação, o service valida a existência da instituição, a existência de cada alimento, o status `DISPONIVEL` e a compatibilidade da unidade.
