# Ciclo Certo – Sistema de Acompanhamento Menstrual

Projeto acadêmico (Universidade Católica de Brasília – Ciências da Computação) que reúne
o trabalho de **Banco de Dados** (Etapa 1) e o de **Teste de Software**.

Integrantes: Anna Luisa Ferreira Guimarães, Lethicia Hikary Borges Hoshi,
Giovanna Hermida de Araújo Vieira Siqueira e Erik Matheus Rodrigues Magalhães.

## Estrutura das pastas

```
annalu/
├── frontend/            Site estático "Ciclo Certo" (HTML + CSS + JS puro)
│   └── tests/           Testes da calculadora e do cadastro (Node)
├── backend/
│   └── acompanhamento-api/   API REST em Spring Boot (dados em memória)
├── console/             Versão de terminal em Java puro (menus com Scanner)
├── database/
│   ├── schema/          01_ddl.sql, 02_carga.sql, 03_consultas.sql (versão da Etapa 1)
│   └── backup/          Dump completo do MySQL (versão mais recente do banco)
├── tests/
│   └── postman/         Coleção Postman + prints de evidência dos testes de API
└── docs/                Relatório da Etapa 1, apresentação e documento de Teste de Software
```

## Como rodar cada parte

### Front-end
Abra `frontend/index.html` no navegador (ou use a extensão *Live Server* do VS Code).
A calculadora funciona sozinha. O formulário **Criar conta** envia o cadastro para a
API em `http://localhost:8080/usuarios`, então inicie o backend antes de testá-lo.

Testes (precisa do Node 20+):

```bash
npm test
# ou: node --test frontend/tests/calc.test.js
```

### Backend (API Spring Boot)
Requisitos: JDK 21+ e Maven (ou use o `mvnw` que está na pasta).

```bash
cd backend/acompanhamento-api
mvn spring-boot:run      # sobe em http://localhost:8080
mvn test                 # roda os testes unitários e de integração
```

Endpoints: `GET/POST /usuarios`, `GET/PUT/DELETE /usuarios/{id}`,
`GET/POST /ciclos`, `PUT/DELETE /ciclos/{id}`, `GET/POST /sintomas`, `PUT/DELETE /sintomas/{id}`.
Os dados ficam em memória e somem quando a API é desligada.

### Console (Java puro)

```bash
javac -encoding UTF-8 -d out console/src/*.java
java -cp out Main                  # menus interativos
java -cp out TesteValidadorCiclo   # teste manual do validador de datas
```

### Banco de dados (MySQL 8)

```bash
# Opção A – recriar do zero com os scripts da Etapa 1
mysql -u root -p < database/schema/01_ddl.sql
mysql -u root -p < database/schema/02_carga.sql
mysql -u root -p acompanhamento_menstrual < database/schema/03_consultas.sql

# Opção B – restaurar o dump completo (inclui tabelas extras: fase, fluxo_menstrual,
# medicamento, uso_medicamento, auditoria_registro_sintoma e a view vw_ciclos_em_andamento)
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS acompanhamento_menstrual"
mysql -u root -p acompanhamento_menstrual < database/backup/backup_acompanhamento_menstrual.sql
```

> Atenção: o dump contém a tabela `auditoria_registro_sintoma` já com registros, mas **não**
> contém os triggers que a alimentam (o `mysqldump` foi gerado sem `--triggers`/`--routines`).
> Se quiser preservá-los, gere o dump novamente com `mysqldump --routines --triggers ...`.

### Testes de API no Postman
Importe `tests/postman/colecao_postman.json` com a API rodando. Os mesmos casos
estão automatizados em `backend/.../ApiIntegrationTest.java`, então `mvn test` já os cobre.

## O que foi corrigido na organização (26/10/2026)

- **Front chamava uma rota inexistente** (`/api/usuarios`) com campos que a API não aceitava;
  agora chama `/usuarios` enviando `nome`, `email` e `dataNascimento` (campo novo no formulário)
  e lê a mensagem de erro no formato que a API devolve (`erro`).
- **API não tinha CORS**: o navegador bloqueava o `fetch` do front. Adicionado `CorsConfig`.
- **API exigia `idUsuario` do cliente**: agora o ID é gerado automaticamente quando não é
  informado (continua aceitando ID explícito, como nos testes do Postman). E-mail repetido
  retorna 409 (regra RN02 do relatório).
- **Calculadora** mostrava "próxima menstruação" no passado quando a última menstruação
  informada era antiga; agora avança ciclos completos até a data atual. A duração da
  menstruação, que era lida e ignorada, passou a ser usada.
- **`pom.xml`** declarava Java 21 mas forçava o compilador para 25; removido o conflito.
- **`UsuarioController`** guardava os dados dentro do controller; extraído `UsuarioService`
  no mesmo padrão de `CicloService`/`SintomasService`. `Usuario` ganhou os getters/setters
  que faltavam (`duracaoCiclo`, `duracaoMenstruacao`) e o campo `email`.
- **Console Java** quebrava com `InputMismatchException` ao digitar texto onde esperava
  número, e com `DateTimeParseException` em datas erradas; agora repete a pergunta.
  Menus ganharam `default` e "Excluído!" só aparece quando algo foi removido.
- Removidos `target/`, `out/`, `.iml` e `HELP.md` do backend; `gitignore` renomeado para
  `.gitignore` e ampliado.
- Testes automatizados adicionados: 8 no front (Node) e 27 no backend (JUnit 5 + MockMvc).
