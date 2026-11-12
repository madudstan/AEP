# Decisões técnicas

## Preservação do CRUD de alimentos

O CRUD original foi mantido para evidenciar a evolução incremental. Não foram removidas classes nem endpoints da Primeira Entrega.

## Novas coleções

Foram adicionadas `instituicoes` e `solicitacoes` para representar o fluxo real de redistribuição sem criar autenticação ou funcionalidades fora do escopo da AEP.

## Referências por ID

As solicitações usam `instituicaoId` e `alimentoId`. O service valida essas referências antes de salvar, mantendo baixo acoplamento entre documentos e permitindo evolução independente das coleções.

## Documentos complexos

O endereço da instituição é aninhado e a solicitação contém a lista de subdocumentos `itens`. Assim, o requisito do segundo semestre é demonstrado diretamente na persistência.

## Status e regras

Solicitações começam como `PENDENTE`. O status pode evoluir para `APROVADA`, `RECUSADA`, `ATENDIDA` ou `CANCELADA`, mas solicitações encerradas não podem ser alteradas novamente.
