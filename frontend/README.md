# Recolhe+ — Front-end

O Recolhe+ é a interface web do Sistema de Gestão e Redistribuição de Alimentos Excedentes. Ele mantém o backend Spring Boot como fonte de dados e oferece um painel operacional para acompanhar alimentos, instituições e solicitações.

## Executar localmente

O backend e o MongoDB continuam sendo executados da mesma forma:

```powershell
docker compose up -d
mvnw.cmd spring-boot:run
```

Em outro terminal, dentro desta pasta `frontend/`, instale as dependências e inicie a interface:

```powershell
npm install
npm run dev
```

Abra `http://localhost:5173`. O Vite encaminha automaticamente as chamadas `/api` para `http://localhost:8080`, portanto não é necessário alterar o backend ou configurar CORS para o desenvolvimento local.

## Fluxo de demonstração

Cadastre um alimento em **Alimentos**, uma instituição em **Instituições** e depois crie uma solicitação em **Solicitações**. O painel permite escolher itens reais, acompanhar o status e atualizar solicitações abertas.

## Build

```powershell
npm run build
```

O resultado de produção é gerado em `dist/`.
