# 🚀 Guia de Desenvolvimento: Módulo Tipo de Quarto

Fala, Yuri! Tudo certo?  
Esse guia foi criado especialmente para você dar sequência ao nosso projeto e construir o **Módulo de Tipo de Quarto**. 

A IA do Vinicius (eu) já montou a estrutura inicial do Spring Boot, conectou com o Postgres e deixou o módulo de **Hóteis** 100% funcional. Agora, a bola está com você! ⚽

Seu objetivo é implementar as classes necessárias para que possamos fazer um CRUD (Criar, Ler, Atualizar, Deletar) de `TipoQuarto` no banco de dados.

---

## 🏗️ O que você precisa fazer? (Passo a Passo)

Você vai criar 6 arquivos no total, e a boa notícia é que **você pode se inspirar nos arquivos que já criamos para `Hotel`**. Veja os passos e os lugares onde os arquivos devem ficar:

### 1. Criar a Entidade (Em `models`)
**Arquivo:** `src/main/java/br/com/fatec/hotel/models/TipoQuarto.java`

Esta é a classe que diz como a tabela vai ser no banco de dados.
- Crie usando `@Entity` e `@Table(name = "tipos_quarto")`.
- Crie um `Long id` com `@Id` e estratégia `GenerationType.IDENTITY`.
- Crie o atributo `String nome` (Luxo, Standard, etc.).
- Crie o atributo `Double precoBase`.
- Crie o atributo `Integer capacidadePessoas`.
- Lembre-se de colocar as anotações do Lombok: `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor` no topo da classe da mesma forma que está em `Hotel.java`.

### 2. Criar as "Apostas Seguras" de Entrada e Saída (Em `dtos`)
**Arquivos:** `TipoQuartoRequestDTO.java` e `TipoQuartoResponseDTO.java`

O DTO de Request é o que o Postman (ou o PHP) envia pra nós para SALVAR.
O DTO de Response é o que nós devolvemos pro Postman (ou PHP).
- Faça parecido com o *HotelResponseDTO* e *HotelRequestDTO*. 
- No `RequestDTO`, coloque anotações do tipo `@NotBlank(message = "O nome é obrigatório")` no atributo de nome, e `@NotNull` no preço.

### 3. Criar o Repositório (Em `repositories`)
**Arquivo:** `src/main/java/br/com/fatec/hotel/repositories/TipoQuartoRepository.java`

- Crie uma **Interface** e faça ela estender `JpaRepository<TipoQuarto, Long>`.
- Coloque `@Repository` em cima da interface.
- Pode olhar o `HotelRepository` se bater a dúvida. É só 3 linhas!

### 4. Criar o Serviço com as Regras de Negócio (Em `services`)
**Arquivo:** `src/main/java/br/com/fatec/hotel/services/TipoQuartoService.java`

- Copie a estrutura (e os métodos) do `HotelService.java`, mas altere para `TipoQuarto`.
- Injete o `TipoQuartoRepository` com `@Autowired`.
- Teremos os métodos: `findAll()`, `findById()`, `insert()`, `update()`, `delete()`.
- O método de deletar e buscar já estão com as Exceptions que criei tratadas. Use `ResourceNotFoundException("Tipo de quarto não encontrado. ID: " + id)`.

### 5. Expor as Rotas na Web (Em `controllers`)
**Arquivo:** `src/main/java/br/com/fatec/hotel/controllers/TipoQuartoController.java`

- É aqui que a mágica acontece. Crie a classe e coloque `@RestController` e `@RequestMapping(value = "/tipos-quarto")` no topo.
- Copie as rotas do `HotelController.java` mas chame o seu `TipoQuartoService`.
- Tente entender o que o `@GetMapping`, `@PostMapping`, `@PutMapping` e `@DeleteMapping` fazem! 

---

## 🏃 Como rodar e testar?

1. Certifique-se de que o **PostgreSQL** está rodando na sua máquina (Usuário: `postgres`, Senha: `root`, Porta: `5432`).
2. Crie um banco vazio no PostgreSQL chamado `hotel_db` (Usando o PgAdmin ou Beekeeper Studio, por exemplo).
3. Na IDE (Eclipse, VSCode, IntelliJ), dê os play no arquivo `HotelApiApplication.java`.
4. O Spring vai criar as tabelas sozinho (lembra do application.properties com `update`?).
5. Abra o **Postman** e crie uma Collection para brincar! As rotas cadastradas do hotel estão em `http://localhost:8080/hoteis`. A sua de Tipos de Quarto vai ficar em `http://localhost:8080/tipos-quarto`.

Manda bala! Qualquer dúvida que você e o Vini tiverem, podem me retornar aqui. 🚀
