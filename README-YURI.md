# 🚀 Guia de Desenvolvimento: Módulos de Apoio (Fase 2)

Fala, Yuri! Tudo certo?  
Antes de mais nada: mandou bem demais no módulo de `TipoQuarto`! O código ficou limpo, as validações bateram certinho e o padrão MVC foi seguido à risca. 👏

**Ah, e um recado importante do Vinicius:**  
> "MEU AMIGO YURI, HAHAHA, NAO FAÇA O COMMIT DO USER E SENHA DO BANCO, HAHAHA" 🤣

---

## 🏗️ Sua nova missão: As 4 Tabelas de Apoio

Agora que já temos a estrutura base, precisamos criar as demais tabelas de "Cadastro Único" (aquelas que não possuem chave estrangeira e servem de base para o sistema). O fluxo que você fez para o `TipoQuarto` vai se repetir aqui para os seguintes domínios:

1. **`CanaisReserva`** (Ex: Booking, Site, Balcão)
2. **`ServicosExtras`** (Ex: Spa, Frigobar, Lavanderia)
3. **`Nacionalidades`** (Ex: Brasil, Argentina, etc.)
4. **`Cargos`** (Ex: Recepcionista, Gerente)

Abaixo, detalho exatamente os campos que cada Entidade deve ter. Lembre-se: para cada uma delas, você deverá criar **Entidade**, **Repository**, **RequestDTO**, **ResponseDTO**, **Service** e **Controller**!

---

### 1. Módulo `CanaisReserva`
Esta tabela indicará de onde a reserva veio.
- **Entidade (`models/CanalReserva.java`):**
  - `@Table(name = "canais_reserva")`
  - `Long id` (com `@Id` e `@Column(name = "id_canal")`)
  - `String nome` (`@Column(nullable = false)`)
  - `Double taxaComissao`

### 2. Módulo `ServicosExtras`
Esta tabela guardará os serviços que um hóspede pode pedir na pousada.
- **Entidade (`models/ServicoExtra.java`):**
  - `@Table(name = "servicos_extras")`
  - `Long id` (com `@Id` e `@Column(name = "id_servico")`)
  - `String nome` (`@Column(nullable = false)`)
  - `Double preco` (`@Column(nullable = false)`)

### 3. Módulo `Nacionalidades`
Para separar o país do hóspede e mantermos o banco normalizado.
- **Entidade (`models/Nacionalidade.java`):**
  - `@Table(name = "nacionalidades")`
  - `Long id` (com `@Id` e `@Column(name = "id_nacionalidade")`)
  - `String nomePais` (`@Column(nullable = false)`)

### 4. Módulo `Cargos`
Cargos dos nossos funcionários.
- **Entidade (`models/Cargo.java`):**
  - `@Table(name = "cargos")`
  - `Long id` (com `@Id` e `@Column(name = "id_cargo")`)
  - `String nomeCargo` (`@Column(nullable = false)`)
  - `Double salarioBase`

---

## 🛠️ Checklist de Desenvolvimento para CADA Módulo

Para você não se perder, aqui vai o checklist do que precisa ser feito em **cada um** dos 4 domínios acima:

1. [ ] **Criar a classe em `models`:** Coloque as anotações do Lombok (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`) e as do JPA (`@Entity`, `@Table`, `@Id`).
2. [ ] **Criar a interface em `repositories`:** `public interface NomeRepository extends JpaRepository<Entidade, Long>`
3. [ ] **Criar os DTOs em `dtos`:** Um `RequestDTO` (com anotações de `@NotBlank` e `@NotNull` do *Jakarta Validation*) e um `ResponseDTO`.
4. [ ] **Criar a classe em `services`:** Injete o Repository com `@Autowired`. Crie os 5 métodos do CRUD (`findAll`, `findById`, `insert`, `update`, `delete`). Não esqueça de tratar o Not Found usando o `ResourceNotFoundException`.
5. [ ] **Criar a classe em `controllers`:** Anote com `@RestController` e `@RequestMapping`. Crie os endpoints (`@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`), lembrando de passar o `@Valid` nos métodos de inserção e atualização.

---

## 🏃 Como testar?

1. Rode a aplicação na sua IDE. O Hibernate vai criar as 4 novas tabelas automaticamente.
2. Abra o Postman e teste enviar um POST para cada uma delas para garantir que as validações e as inserções no banco estão OK.
3. Se certificou que tudo está redondo? Só dar aquele combo clássico:
   ```bash
   git add .
   git commit -m "feat: Criando tabelas de apoio (Canais, Servicos, Nacionalidades e Cargos)"
   git push origin main
   ```

Boa sorte! Depois que você fechar essas 4, o Vini vai assumir para fazer as tabelas de Relacionamento (1:N) que vão amarrar tudo isso! 🚀
