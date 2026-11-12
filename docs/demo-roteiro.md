# Roteiro do vídeo — 3 a 5 minutos

1. **Problema e ODS (30s):** desperdício de alimentos, instituições receptoras e ODS 12.
2. **Evolução (40s):** explicar que a Primeira Entrega tinha uma coleção e CRUD de alimentos; a versão final acrescenta instituições, solicitações, relacionamentos e documentos complexos.
3. **Arquitetura (30s):** mostrar as camadas Controller, DTO, Service, Repository, Model e Mapper.
4. **Demonstração (90s):** abrir Swagger, cadastrar alimento, cadastrar instituição com endereço, criar solicitação com dois itens, consultar por instituição e aprovar a solicitação.
5. **MongoDB (20s):** mostrar as coleções `alimentos`, `instituicoes` e `solicitacoes`, destacando endereço e lista de itens.
6. **Testes e cobertura (30s):** executar `./mvnw clean verify`, mostrar os testes e abrir `target/site/jacoco/index.html`, evidenciando pelo menos 70%.
7. **Resultado final (20s):** resumir que a PoC permite organizar excedentes e conectar instituições a alimentos disponíveis.
