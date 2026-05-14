# API Hotelaria — Documentação das Alterações

API REST em Java com Spring Boot para gestão de hotelaria.

## Stack

- Java 21
- Spring Boot 4.0.5
- Spring Data JPA
- Spring Validation
- PostgreSQL

## O que foi implementado nesta entrega

### 1. Padronização de contrato (`cod*`) nas entidades base

Foram ajustadas as entidades já existentes para o padrão de chaves com prefixo `cod`:

- `tipos_quarto`: `codtipo`, `nome`, `precobase`
- `canais_reserva`: `codcanais`, `nome`
- `hoteis`: `codhotel`, `nome`, `cnpj`, `estrelas`
- `nacionalidades`: `codnacionalidade`, `nomePais`
- `cargos`: `codcargo`, `nomeCargo`, `salarioBase`
- `servicos_extras`: `codservicos`, `nome`, `preco`

Esses ajustes também foram refletidos em DTOs, Services e Controllers.

### 2. Módulos novos com relacionamento 1:N e N:N

Foram criados módulos completos (**Model, Repository, DTO, Service, Controller**) para:

- `quartos` (`codquarto`, `numero`, `andar`, `codhotelfk`, `codtipofk`)
- `imagens_quarto` (`codimagem`, `url`, `codquartofk`)
- `hospedes` (`codhospede`, `nome`, `documento`, `codnacionalidadefk`)
- `funcionarios` (`codfuncionario`, `nome`, `codcargofk`)
- `reservas` (`codreserva`, `datacheckin`, `datacheckout`, `valortotal`, `codcanalfk`)
- `checkins` (`codcheckin`, `datahorareal`, `codreservafk`, `codfuncionariofk`)
- `hospedes_reservas` (`codhospedereserva`, `codhospedefk`, `codreservafk`)

### 3. Relacionamentos implementados

- `Hotel (1) -> (N) Quarto`
- `TipoQuarto (1) -> (N) Quarto`
- `Quarto (1) -> (N) ImagemQuarto`
- `Nacionalidade (1) -> (N) Hospede`
- `Cargo (1) -> (N) Funcionario`
- `CanalReserva (1) -> (N) Reserva`
- `Reserva (1) -> (N) Checkin` (modelado conforme escopo atual)
- `Funcionario (1) -> (N) Checkin`
- `Hospede (N) <-> (N) Reserva` via `hospedes_reservas`

## Endpoints disponíveis

Todos os recursos abaixo possuem CRUD completo (`GET` lista, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}`):

- `/tipos-quarto`
- `/canais-reserva`
- `/hoteis`
- `/nacionalidades`
- `/cargos`
- `/servicos-extras`
- `/quartos`
- `/imagens-quarto`
- `/hospedes`
- `/funcionarios`
- `/reservas`
- `/checkins`
- `/hospedes-reservas`

## Validação e tratamento de erros

- Validações de entrada com `jakarta.validation` nos DTOs de request.
- Retorno `422` para payload inválido.
- Retorno `404` para registros/FKs inexistentes via `ResourceNotFoundException`.

## Escopo explicitamente fora desta entrega

Tabela N:N ainda pendente nesta etapa:

- `ReservasServicos`

## Como executar

1. Configure o PostgreSQL em `src/main/resources/application.properties`.
2. Execute:

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

## Observações

- O projeto atualmente não possui Swagger/OpenAPI configurado.
- O `ddl-auto=update` está habilitado para criação/atualização automática das tabelas.
