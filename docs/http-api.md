# API HTTP final

Base URL: `http://localhost:8080`. Swagger: `http://localhost:8080/docs`.

## Criar instituição

`POST /api/instituicoes`

```json
{
  "nome": "Instituto Esperança",
  "documento": "12345678000100",
  "email": "contato@esperanca.org",
  "telefone": "44999999999",
  "endereco": {"rua":"Rua A","numero":"100","cidade":"Maringá","estado":"PR","cep":"87000000"}
}
```

Retorna `201 Created` e o header `Location`.

## Criar solicitação

`POST /api/solicitacoes`

```json
{
  "instituicaoId": "ID_DA_INSTITUICAO",
  "observacao": "Doação para a próxima semana",
  "itens": [
    {"alimentoId":"ID_DO_ALIMENTO","quantidadeSolicitada":10,"unidade":"kg"}
  ]
}
```

Retorna `201 Created`. A solicitação começa com status `PENDENTE`.

## Alterar status

`PATCH /api/solicitacoes/{id}/status`

```json
{"status":"APROVADA"}
```

Retorna `200 OK`. Uma solicitação `ATENDIDA` ou `CANCELADA` não pode ser alterada.

## Demais endpoints

- `GET /api/instituicoes` e `GET /api/instituicoes/{id}`;
- `PUT` e `DELETE /api/instituicoes/{id}`;
- `GET /api/solicitacoes` e `GET /api/solicitacoes/{id}`;
- `GET /api/solicitacoes/instituicao/{instituicaoId}`;
- `DELETE /api/solicitacoes/{id}`;
- todos os endpoints CRUD de `/api/alimentos` da Primeira Entrega.

Erros de validação retornam `400`, recurso inexistente retorna `404` e regra de negócio violada retorna `422`, sempre no formato padronizado `ApiError`.
