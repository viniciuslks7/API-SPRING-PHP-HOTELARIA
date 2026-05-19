# Sistema de Gestão Hoteleira — FATEC Jales 2026

Trabalho final da disciplina **Arquitetura Orientada a Serviços**. Sistema completo de gestão hoteleira:

- **Backend** REST em Java + Spring Boot (14 tabelas, relações 1:N e N:N)
- **Frontend** PHP consumindo a API via cURL
- **Documentação** Postman (`docs/postman/HotelApi.postman_collection.json`) + Swagger/OpenAPI auto-gerado

Autores: **Vinicius Siqueira** & **Yuri**

---

## Sumário

1. [Stack](#stack)
2. [Estrutura do repositório](#estrutura-do-repositório)
3. [Modelo de dados](#modelo-de-dados)
4. [Pré-requisitos](#pré-requisitos)
5. [Setup do PostgreSQL](#setup-do-postgresql)
6. [Variáveis de ambiente](#variáveis-de-ambiente)
7. [Como rodar o backend](#como-rodar-o-backend)
8. [Como rodar o frontend (XAMPP)](#como-rodar-o-frontend-xampp)
9. [Endpoints e exemplos](#endpoints-e-exemplos)
10. [Tratamento de erros](#tratamento-de-erros)
11. [Swagger / OpenAPI](#swagger--openapi)
12. [Coleção Postman](#coleção-postman)
13. [Como rodar os testes](#como-rodar-os-testes)
14. [Decisões de arquitetura](#decisões-de-arquitetura)
15. [Troubleshooting](#troubleshooting)
16. [Limitações conhecidas](#limitações-conhecidas)

---

## Stack

| Camada | Tecnologia | Versão |
|---|---|---|
| Backend | Java + Spring Boot | 17 / 3.4.1 |
| Persistência | Spring Data JPA + Hibernate | 6.6.x |
| Banco (produção) | PostgreSQL | 16+ |
| Banco (testes) | H2 in-memory | 2.x |
| Validação | Jakarta Bean Validation | 3.x |
| Documentação | springdoc-openapi (Swagger UI) | 2.7.0 |
| Frontend | PHP vanilla + cURL | 8+ |
| UI | HTML5 + CSS + Font Awesome + SweetAlert2 | — |
| Build | Maven Wrapper | 3.13 |

---

## Estrutura do repositório

```
.
├── backend/                          # API Spring Boot
│   ├── mvnw / mvnw.cmd               # Maven Wrapper (não precisa Maven instalado)
│   ├── pom.xml                       # Dependências
│   └── src/
│       ├── main/
│       │   ├── java/br/com/fatec/hotel/
│       │   │   ├── config/           # CORS, OpenAPI
│       │   │   ├── controllers/      # 14 controllers REST
│       │   │   ├── dtos/             # 28 DTOs (Request / Response)
│       │   │   ├── exceptions/       # ResourceNotFoundException + GlobalExceptionHandler
│       │   │   ├── models/           # 14 @Entity
│       │   │   ├── repositories/     # 14 JpaRepository
│       │   │   ├── services/         # 14 @Service
│       │   │   └── HotelApiApplication.java
│       │   └── resources/application.properties
│       └── test/                     # 58 testes (JUnit 5 + MockMvc + H2)
│
├── frontend/                         # Aplicação PHP
│   ├── index.php                     # Roteador principal
│   ├── config/api.php                # Mapeamento das 14 entidades + BASE_URL
│   ├── services/ApiClient.php        # Wrapper cURL
│   ├── controllers/CrudController.php # CRUD genérico
│   ├── views/                        # Páginas + sidebar + dashboard
│   └── assets/                       # CSS, JS
│
├── docs/
│   └── postman/
│       └── HotelApi.postman_collection.json   # Coleção pronta para importar
│
├── README.md                         # Este arquivo
└── README-YURI.md                    # Documento histórico (não usar para apresentação)
```

---

## Modelo de dados

### Cadastros base (6 tabelas — sem FK)

| Tabela | PK | Campos |
|---|---|---|
| `hoteis` | `codhotel` | nome, cnpj (unique), estrelas |
| `tipos_quarto` | `codtipo` | nome, precobase |
| `canais_reserva` | `codcanais` | nome |
| `servicos_extras` | `codservicos` | nome, preco |
| `nacionalidades` | `codnacionalidade` | nome_pais |
| `cargos` | `codcargo` | nome_cargo, salario_base |

### Operações (6 tabelas — relações 1:N)

| Tabela | PK | FKs |
|---|---|---|
| `quartos` | `codquarto` | codhotelfk → hoteis, codtipofk → tipos_quarto |
| `imagens_quarto` | `codimagem` | codquartofk → quartos |
| `hospedes` | `codhospede` | codnacionalidadefk → nacionalidades |
| `funcionarios` | `codfuncionario` | codcargofk → cargos |
| `reservas` | `codreserva` | codcanalfk → canais_reserva |
| `checkins` | `codcheckin` | codreservafk → reservas, codfuncionariofk → funcionarios |

### Relações N:N (2 tabelas associativas)

| Tabela | PK sintética | FKs (par único) | Campo extra |
|---|---|---|---|
| `hospedes_reservas` | `codhospedereserva` | codhospedefk + codreservafk | — |
| `reservas_servicos` | `codreservaservico` | codreservafk + codservicofk | quantidade |

**Total: 14 tabelas, 13 chaves estrangeiras, 4 UNIQUE constraints.**

---

## Pré-requisitos

| Software | Versão mínima | Obrigatório? |
|---|---|---|
| **Java JDK** | 17 | Sim |
| **PostgreSQL** | 12+ (testado em 16 e 18) | Sim |
| **PHP** | 8.0 com extensão cURL | Sim (para o frontend) |
| **XAMPP** ou Apache | qualquer | Para servir o PHP |
| Maven | — | Não (já tem `mvnw.cmd` no repo) |

> O projeto **não exige Maven instalado** — use `./mvnw` (Linux/macOS) ou `mvnw.cmd` (Windows).

---

## Setup do PostgreSQL

### 1. Criar o banco

Abra o `pgAdmin` ou o `psql` e execute:

```sql
CREATE DATABASE hotel_db;
```

### 2. Identificar a porta do PG

Verifique a porta do seu PostgreSQL:

- **PG instalação única:** porta `5432`.
- **Múltiplas instalações:** geralmente `5432` (primeira) e `5433` (segunda). Você pode confirmar via `services.msc` (Windows) procurando "postgresql".

### 3. Não precisa criar tabelas manualmente

O Hibernate cria todas as 14 tabelas, FKs e UNIQUE constraints **automaticamente** ao subir o backend, graças a `spring.jpa.hibernate.ddl-auto=update` em `application.properties`.

---

## Variáveis de ambiente

Os valores padrão estão em `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5433/hotel_db}
spring.datasource.username=${DB_USER:postgres}
spring.datasource.password=${DB_PASSWORD:123456}
```

Você pode **sobrescrever** sem editar o arquivo, usando variáveis de ambiente:

| Variável | Padrão | Descrição |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5433/hotel_db` | URL JDBC completa |
| `DB_USER` | `postgres` | Usuário do banco |
| `DB_PASSWORD` | `123456` | Senha do banco |

### Como setar (Windows PowerShell)

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/hotel_db"
$env:DB_USER="postgres"
$env:DB_PASSWORD="minha_senha"
```

### Como setar (Linux/macOS)

```bash
export DB_URL=jdbc:postgresql://localhost:5432/hotel_db
export DB_USER=postgres
export DB_PASSWORD=minha_senha
```

---

## Como rodar o backend

### Opção 1: Maven Wrapper (recomendado)

```bash
cd backend

# Windows
mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

Aguarde até ver no console:

```
Started HotelApiApplication in X seconds
Tomcat started on port 8080 (http) with context path '/'
```

A API estará disponível em **http://localhost:8080**.

### Opção 2: Build do JAR

```bash
cd backend
./mvnw clean package -DskipTests
java -jar target/api-0.0.1-SNAPSHOT.jar
```

### Opção 3: IDE (IntelliJ / VS Code / Eclipse)

Abra a pasta `backend/` como projeto Maven e rode a classe `HotelApiApplication.java`.

### Parar o backend

`Ctrl+C` no terminal onde rodou o `mvnw`.

Se o processo Java ficou "fantasma" segurando a porta 8080:

```powershell
# Windows
Get-NetTCPConnection -LocalPort 8080 -State Listen | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force }

# Linux / macOS
lsof -ti :8080 | xargs kill -9
```

---

## Como rodar o frontend (XAMPP)

### 1. Copiar arquivos

Copie o conteúdo da pasta `frontend/` para o `htdocs` do XAMPP. Duas opções:

**Opção A — pasta dedicada:**
```
C:\xampp\htdocs\hotel\   ← copiar tudo de frontend/ para cá
URL: http://localhost/hotel/
```

**Opção B — direto na raiz do htdocs:**
```
C:\xampp\htdocs\         ← copiar tudo de frontend/ para cá
URL: http://localhost/
```

### 2. Iniciar Apache

Abra o XAMPP Control Panel e clique em **Start** ao lado de `Apache`. MySQL não é necessário.

### 3. Configurar a URL da API (opcional)

Se você mudou a porta do backend, edite `frontend/config/api.php`:

```php
define('API_BASE_URL', 'http://localhost:8080');
```

### 4. Acessar

Abra o navegador em `http://localhost/` (ou `http://localhost/hotel/` se usou pasta dedicada).

> **Importante:** o backend Spring Boot precisa estar **rodando** antes de acessar o frontend, senão o dashboard mostra erros de conexão.

---

## Endpoints e exemplos

Todos os recursos possuem CRUD completo: `GET /`, `GET /{id}`, `POST /`, `PUT /{id}`, `DELETE /{id}`.

| Recurso | Path |
|---|---|
| Hotéis | `/hoteis` |
| Tipos de Quarto | `/tipos-quarto` |
| Canais de Reserva | `/canais-reserva` |
| Serviços Extras | `/servicos-extras` |
| Nacionalidades | `/nacionalidades` |
| Cargos | `/cargos` |
| Quartos | `/quartos` |
| Imagens de Quarto | `/imagens-quarto` |
| Hóspedes | `/hospedes` |
| Funcionários | `/funcionarios` |
| Reservas | `/reservas` |
| Check-ins | `/checkins` |
| Hóspedes ↔ Reservas | `/hospedes-reservas` |
| Reservas ↔ Serviços | `/reservas-servicos` |

### Exemplo: criar um Hotel

```bash
curl -X POST http://localhost:8080/hoteis \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pousada Lua Cheia",
    "cnpj": "12.345.678/0001-90",
    "estrelas": 5
  }'
```

**Resposta `201 Created`** (com header `Location: /hoteis/1`):

```json
{
  "codHotel": 1,
  "nome": "Pousada Lua Cheia",
  "cnpj": "12.345.678/0001-90",
  "estrelas": 5
}
```

### Exemplo: criar uma Reserva (depende de um CanalReserva)

```bash
# 1) Criar o canal primeiro
curl -X POST http://localhost:8080/canais-reserva \
  -H "Content-Type: application/json" \
  -d '{"nome":"Booking"}'

# 2) Criar a reserva referenciando o canal
curl -X POST http://localhost:8080/reservas \
  -H "Content-Type: application/json" \
  -d '{
    "dataCheckin": "2026-07-01",
    "dataCheckout": "2026-07-05",
    "valorTotal": 1200.00,
    "codCanalFk": 1
  }'
```

### Exemplo: relação N:N — vincular Hóspede a Reserva

```bash
curl -X POST http://localhost:8080/hospedes-reservas \
  -H "Content-Type: application/json" \
  -d '{
    "codHospedeFk": 1,
    "codReservaFk": 1
  }'
```

### Exemplo: relação N:N com campo extra — `quantidade`

```bash
curl -X POST http://localhost:8080/reservas-servicos \
  -H "Content-Type: application/json" \
  -d '{
    "codReservaFk": 1,
    "codServicoFk": 1,
    "quantidade": 3
  }'
```

---

## Tratamento de erros

| Cenário | Status | Quem dispara |
|---|---|---|
| Recurso inexistente (GET/PUT/DELETE por ID) | `404 Not Found` | `ResourceNotFoundException` |
| FK referencia ID inexistente | `404 Not Found` | `ResourceNotFoundException` no Service |
| Campo obrigatório ausente, valor fora do range, etc. | `422 Unprocessable Entity` | `@NotBlank`, `@Min`, `@Positive`, etc. |
| Regra de negócio violada (ex.: dataCheckout < dataCheckin) | `400 Bad Request` | `IllegalArgumentException` |
| CNPJ duplicado, documento duplicado, par N:N duplicado | `409 Conflict` | `DataIntegrityViolationException` |
| Criação OK | `201 Created` + header `Location` | — |
| Atualização OK | `200 OK` | — |
| Exclusão OK | `204 No Content` | — |

### Formato dos erros

**404:**
```json
{
  "timestamp": "2026-05-18T22:10:33.123Z",
  "status": 404,
  "error": "Resource not found",
  "message": "Hotel não encontrado. ID: 99"
}
```

**422:**
```json
{
  "timestamp": "2026-05-18T22:10:33.123Z",
  "status": 422,
  "error": "Validation exception",
  "errors": {
    "nome": "O nome é obrigatório",
    "estrelas": "A classificação deve ser no máximo 5 estrelas"
  }
}
```

**400 (regra de negócio):**
```json
{
  "timestamp": "2026-05-18T22:10:33.123Z",
  "status": 400,
  "error": "Bad request",
  "message": "A data de check-out deve ser posterior à data de check-in."
}
```

**409:**
```json
{
  "timestamp": "2026-05-18T22:10:33.123Z",
  "status": 409,
  "error": "Data integrity violation",
  "message": "Violação de integridade: registro duplicado ou referência inválida (FK)."
}
```

Implementação em `backend/src/main/java/br/com/fatec/hotel/exceptions/GlobalExceptionHandler.java`.

---

## Swagger / OpenAPI

Com o backend rodando:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

A documentação é gerada automaticamente a partir dos controllers. Você pode testar todas as requisições direto pela UI do Swagger.

---

## Coleção Postman

A coleção completa está em `docs/postman/HotelApi.postman_collection.json`.

### Como importar

1. Abra o Postman.
2. Clique em **Import** → selecione o arquivo.
3. A coleção `HotelApi - FATEC Jales` aparece na barra lateral.

### Estrutura da coleção

```
HotelApi - FATEC Jales
├── 1. Cadastros Base
│   ├── Hotéis (5 requests)
│   ├── Tipos de Quarto (5 requests)
│   ├── Canais de Reserva (5 requests)
│   ├── Serviços Extras (5 requests)
│   ├── Nacionalidades (5 requests)
│   └── Cargos (5 requests)
├── 2. Operações (1:N)
│   ├── Quartos (5 requests)
│   ├── Imagens de Quarto (5 requests)
│   ├── Hóspedes (5 requests)
│   ├── Funcionários (5 requests)
│   ├── Reservas (5 requests)
│   └── Check-ins (5 requests)
├── 3. Relações N:N
│   ├── Hóspedes ↔ Reservas (5 requests)
│   └── Reservas ↔ Serviços (5 requests)
└── 4. Cenários de Erro
    ├── 404 - Recurso inexistente
    ├── 422 - Payload inválido
    ├── 400 - Regra de negócio violada
    └── 409 - Conflito (CNPJ duplicado)
```

**Variável de ambiente:** `base_url` (padrão: `http://localhost:8080`). Se mudou a porta do backend, atualize a variável na coleção.

Cada request tem:
- Headers `Content-Type: application/json` e `Accept: application/json`
- Body de exemplo realista
- Test script validando o status HTTP esperado

---

## Como rodar os testes

```bash
cd backend
./mvnw test
```

Os testes usam **H2 in-memory** (profile `test`) — não precisa do PostgreSQL local. Cobertura atual: **58 testes** distribuídos em 14 classes:

| Classe | Testes |
|---|---|
| `HotelApiApplicationTests` | Smoke test do contexto Spring |
| `HotelControllerTests` | CRUD do Hotel + validação + 404 |
| `TipoQuartoControllerTests`, `CanalReservaControllerTests`, etc. | CRUD por entidade |
| `QuartoControllerTests`, `ImagemQuartoControllerTests`, etc. | CRUD + resolução de FK |
| `ReservaValidationTests` | Regra de negócio: check-out > check-in |
| `HospedeReservaControllerTests`, `ReservaServicoControllerTests` | N:N com unique constraint |

Para rodar uma classe específica:

```bash
./mvnw test -Dtest=HotelControllerTests
```

---

## Decisões de arquitetura

### Camadas
```
Controller (REST) → Service (@Transactional) → Repository (JpaRepository) → @Entity
                       ↑
                  DTO Request → entity (via copyDtoToEntity)
                  entity → DTO Response (via toDTO)
```

### Por que `@Getter`/`@Setter` em vez de `@Data`
`@Data` gera `equals/hashCode/toString` que percorrem **todas as relações**, incluindo `@ManyToOne` LAZY. Isso pode causar `LazyInitializationException` ou loops infinitos. `@Getter`/`@Setter` resolvem só o que precisamos.

### Por que `BigDecimal` em vez de `Double` para dinheiro
`Double` é ponto flutuante binário — perde precisão em somas de centavos. `BigDecimal` com `precision=10, scale=2` é exato. Aplicado em `precoBase`, `valorTotal`, `salarioBase`, `preco`.

### Por que N:N como entidade própria (com PK sintética)
Em vez de `@ManyToMany` com `@JoinTable` cego, modelamos `HospedeReserva` e `ReservaServico` como `@Entity` próprias. Isso permite:
- Carregar campos extras na associação (ex.: `quantidade` em `reservas_servicos`)
- Garantir unicidade do par via `@UniqueConstraint`
- Operar CRUD direto na tabela associativa

### Por que `FetchType.LAZY` em todos os `@ManyToOne`
Evita carregar o grafo inteiro de entidades em cada `findAll()`. O Service só converte o que precisa via `toDTO`.

### Por que DTOs Request/Response separados
- Não vaza entidade JPA pelo HTTP (evita ciclos serialização)
- Request pode validar com `@NotNull`/`@Size`; Response não precisa
- Permite evoluir o contrato HTTP sem mexer na entidade

---

## Troubleshooting

### "Port 8080 was already in use"

Algum processo Java fantasma ficou segurando a porta. No PowerShell:

```powershell
Get-NetTCPConnection -LocalPort 8080 -State Listen | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force }
```

### "Connection refused" / "FATAL: password authentication failed"

Sua senha/usuário do PostgreSQL não bate com o `application.properties`. Sobrescreva via env var:

```powershell
$env:DB_PASSWORD="sua_senha_real"
./mvnw spring-boot:run
```

### "FATAL: database hotel_db does not exist"

Crie o banco no pgAdmin ou no psql:
```sql
CREATE DATABASE hotel_db;
```

### Frontend mostra "Erro de conexão: cURL ..."

O backend não está rodando, ou está em porta diferente. Confirme com:
```
http://localhost:8080/hoteis
```
deve retornar uma lista JSON. Se mudou a porta, edite `frontend/config/api.php`.

### Múltiplas instalações de PostgreSQL na mesma máquina

Cada uma usa uma porta diferente (normalmente 5432 e 5433). Verifique no `services.msc` qual porta cada serviço configura, e ajuste a env var `DB_URL` para apontar para a versão correta.

### Hibernate falha ao aplicar `ALTER TABLE`

Provavelmente tabelas pré-existentes têm tipos incompatíveis com o schema atual. **Em desenvolvimento**, dropar o banco e recriar é o caminho mais rápido:

```sql
DROP DATABASE hotel_db;
CREATE DATABASE hotel_db;
```

### Encoding bagunçado nas respostas (acentos errados)

Garanta que seu PostgreSQL foi criado com `ENCODING 'UTF8'` (padrão). Para checar:

```sql
SELECT datname, pg_encoding_to_char(encoding) FROM pg_database WHERE datname = 'hotel_db';
```

---

## Limitações conhecidas

- `spring.jpa.hibernate.ddl-auto=update` — adequado para desenvolvimento; em produção, usaríamos Flyway/Liquibase para versionar schema.
- Sem paginação nos `GET /` — retorna `List<T>` completa. Para o escopo acadêmico, OK.
- Sem autenticação/autorização (Spring Security está fora do escopo).
- DTOs de Response retornam apenas IDs das FKs (não embarcam objetos relacionados).
- CORS configurado como `*` — em produção, restringir aos domínios reais.

---

## Créditos

Trabalho realizado por **Vinicius Siqueira** e **Yuri** para a FATEC Jales — 2026.
