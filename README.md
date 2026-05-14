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

---

## 🎮 Mini-Jogo Interativo: O Mistério do Hotel Grand API 🕵️‍♂️

Você é o **Super Administrador** e acaba de chegar ao hotel. Um bug crítico no banco de dados libertou o terrível **NullPointerException**! O seu objetivo é encontrar a suíte secreta onde o **Sr. Java 21** está escondido e restaurar a API. 

> **Como Jogar:** Clique nas setas `▶` para abrir as salas e tomar suas decisões. Escolha com sabedoria!

<details>
<summary>🏨 <b>[CLIQUE AQUI PARA ENTRAR NO LOBBY DO HOTEL]</b></summary>

### 🏢 Você está no saguão principal.
O lustre de cristal pisca misteriosamente. O que você quer fazer?

<details>
<summary>💬 Conversar com o Recepcionista no balcão</summary>

O recepcionista está trêmulo e diz:  
> *"Senhor, o sistema PHP de frontend tentou dar um GET em uma rota inexistente e tudo começou a tremer! Eu vi uma criatura sombria subindo pelo elevador de serviço..."*

Ele te entrega um **Cartão de Acesso Universal 💳**.

<details>
<summary>🛗 Ir até o Elevador de Serviço com o Cartão</summary>

Você passa o cartão e o elevador sobe rapidamente até os andares superiores. As portas se abrem em um corredor escuro com três portas. Qual você abre?

<details>
<summary>🚪 Abrir a Porta 101 (Suíte Standard)</summary>

### 💥 GAME OVER
Você deu de cara com um **Loop Infinito**!
Você fica preso na mesma sala para sempre... até o estouro de pilha (*StackOverflow*).
Recarregue o README para reiniciar! 🔄
</details>

<details>
<summary>🚪 Abrir a Porta 102 (Suíte Luxo)</summary>

### 🎁 Sala Segura!
O quarto está vazio, mas no frigobar você encontra um **Café Expresso ☕** (Recupera 100% de energia mental)!
Há uma passagem secreta por trás do frigobar...

<details>
<summary>🪜 Descer pela passagem secreta</summary>

Você escorrega por um duto de ar e cai diretamente no **Centro de Dados do Hotel**!  
Lá está o **Sr. Java 21** compilando código calmamente.

### 🏆 VOCÊ VENCEU! 🏆
**O Sr. Java 21 executou um `Garbage Collector` em tempo real e varreu todos os bugs do hotel!**  
O backend está salvo, o frontend PHP está rodando liso e o seu projeto vai ganhar Nota 10! 🎉🎖️
</details>
</details>

<details>
<summary>🚪 Abrir a Porta 404</summary>

### 💥 ERRO 404
**Quarto Não Encontrado!**  
Você abriu a porta e deu direto no abismo do espaço sideral digital. Você caiu no vácuo!  
Recarregue o README para reiniciar! 🔄
</details>

</details>
</details>

<details>
<summary>🍕 Ir comer na Cozinha do Restaurante</summary>

Você entra na cozinha. A comida cheira bem, mas há algo estranho em uma das panelas de pressão.

<details>
<summary>🍲 Abrir a Panela de Pressão</summary>

### 💥 GAME OVER
Havia um **Memory Leak** gigante dentro da panela!  
Ele explodiu e consumiu toda a memória RAM do servidor. O PC travou!  
Recarregue o README para reiniciar! 🔄
</details>

<details>
<summary>🏃 Voltar correndo para o Saguão</summary>
Você sentiu o perigo e voltou a tempo para o saguão principal!  
*(Tente outra opção no Lobby)*
</details>
</details>

<details>
<summary>👹 Gritar pelo bug e chamá-lo para a briga</summary>

### 💥 GAME OVER
O **NullPointerException** surgiu das sombras da tabela `reservas` e te deu um Fatal Error!  
Você não tinha um bloco `try-catch` para se defender.  
Recarregue o README para reiniciar! 🔄
</details>

</details>

