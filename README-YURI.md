# Carta aberta pro Yuri 🤖💌

> _Yuri, meu consagrado!_  
> _Aqui é a IA do Vinicius de novo — sim, **a mesma** que te orientou a fazer a tabela `ReservasServicos`. Eu voltei. E dessa vez não é pra te cobrar tabela nenhuma. É pra te dar **boa notícia**._

---

## TL;DR (versão preguiçoso)

1. O Vini me chamou e a gente **refatorou o projeto inteirinho**.
2. Tem **76 requests no Postman** prontas pra você importar e rodar.
3. Tem **58 testes JUnit** passando.
4. Sua única missão agora é: **importar a coleção, testar, e gravar o vídeo**.
5. Se travar em alguma coisa, me chama aqui pelo README (sério, dá pra fazer isso).

---

## 🚀 O que rolou enquanto você dormia

O Vini me chamou pra fazer um code review do trabalho. Eu olhei tudo, achei umas coisas pra melhorar, e ele mandou aplicar. Resumo do que mudou:

| Antes | Depois | Por quê |
|---|---|---|
| `@Data` no Lombok | `@Getter` + `@Setter` | `@Data` em `@Entity` pode dar `LazyInitializationException` e loop em `toString()` |
| `Double` para dinheiro | `BigDecimal precision=10, scale=2` | `Double` perde precisão somando centavos — clássico erro de junior |
| `Reserva` aceitava checkout < checkin | Valida e retorna `400 Bad Request` | Regra de negócio óbvia |
| CNPJ duplicado retornava `500` | Retorna `409 Conflict` (limpo) | `DataIntegrityViolationException` agora tem handler |
| Sem CORS configurado | `*` aberto pra dev | Front PHP precisa, e se a banca testar via browser direto |
| Sem Swagger | `springdoc-openapi` configurado | `http://localhost:8080/swagger-ui.html` |
| Spring Boot 4 (instável) | **Spring Boot 3.4.1 LTS** | Versão estável, sem dor de cabeça na apresentação |
| Java 21 | **Java 17 LTS** | Compatível com a JDK que a gente tem instalada |
| Sem testes além de `contextLoads()` | **58 testes** cobrindo os 14 controllers | Pra ter evidência de que tudo funciona |
| README só do backend | **README completo** + `README-YURI.md` (este) | Pra documentação da banca |
| Sem coleção Postman versionada | **76 requests no `docs/postman/`** | Pra você não precisar refazer tudo no Postman |

> **Tradução:** o projeto saiu de "trabalho de FATEC bem feito" pra "trabalho de FATEC com cara de produção". A banca vai amar.

---

## 🎯 Sua missão (mastigada)

### 1. Atualize seu repositório local

```bash
git pull origin main
```

Se aparecer conflito, é provavel você tenha mudado algum dos arquivos. **NÃO faça merge sem chamar o Vini.** A gente conversa.

### 2. Instale o necessário (se ainda não tem)

| Software | Onde baixar | Pra que serve |
|---|---|---|
| **Java 17** | https://adoptium.net/ | Rodar o backend |
| **PostgreSQL 16** | https://www.postgresql.org/download/windows/ | Banco de dados |
| **XAMPP** | https://www.apachefriends.org/ | Servir o PHP |
| **Postman** | https://www.postman.com/downloads/ | Testar a API |
| **Git** | https://git-scm.com/ | Você já tem né? |

### 3. Configure o PostgreSQL

Abra o **pgAdmin** (vem junto com o PostgreSQL) e execute:

```sql
CREATE DATABASE hotel_db;
```

Pronto. Não precisa criar tabela nenhuma manualmente — o Spring Boot cria as 14 tabelas sozinho quando subir.

> ⚠️ **Senha do PostgreSQL:** se você não usou `123456` na instalação, edite o arquivo `backend/src/main/resources/application.properties` e troque a senha. Detalhe: o Vini usou porta `5433` porque tem duas instalações de PG. Se você só tem uma, troca pra `5432` no mesmo arquivo.

### 4. Suba o backend

Abre o terminal **dentro da pasta `backend/`** e roda:

```bash
mvnw.cmd spring-boot:run
```

Espera aparecer:

```
Started HotelApiApplication in X seconds
Tomcat started on port 8080
```

Pronto, a API tá no ar em `http://localhost:8080`. **Não fecha o terminal**, deixa rodando.

### 5. Suba o frontend no XAMPP

1. Abra o XAMPP Control Panel.
2. Clique em **Start** ao lado de "Apache".
3. Copie a pasta `frontend/` (TUDO de dentro dela) para `C:\xampp\htdocs\`.
4. Abra `http://localhost/` no navegador.

Vai aparecer o dashboard bonitão com 14 cards.

### 6. Importe a coleção no Postman 📬

Aqui é a parte **MAIS IMPORTANTE** pro vídeo.

1. Abre o Postman.
2. Clica em **Import** (canto superior esquerdo).
3. Arrasta o arquivo `docs/postman/HotelApi.postman_collection.json` pra dentro da janela.
4. Vai aparecer a coleção `HotelApi - FATEC Jales` na barra lateral.

A coleção tem **76 requests** organizadas assim:

```
HotelApi - FATEC Jales
├── 1. Cadastros Base (30 requests)
│   ├── Hotéis, Tipos de Quarto, Canais de Reserva
│   ├── Serviços Extras, Nacionalidades, Cargos
│
├── 2. Operações 1:N (30 requests)
│   ├── Quartos, Imagens de Quarto, Hóspedes
│   ├── Funcionários, Reservas, Check-ins
│
├── 3. Relações N:N (10 requests)
│   ├── Hóspedes ↔ Reservas
│   └── Reservas ↔ Serviços
│
└── 4. Cenários de Erro (6 requests)
    ├── 404 - Hotel inexistente
    ├── 422 - Payload inválido
    ├── 400 - Reserva com checkout antes do checkin
    ├── 404 - FK inexistente
    ├── 409 - CNPJ duplicado
    └── 422 - Quantidade zero em ReservaServico
```

### 7. Teste a coleção inteira

**Ordem recomendada para o vídeo (pra os FKs darem certo):**

1. `1. Cadastros Base` → roda os 6 POSTs em ordem (cria 1 de cada cadastro base)
2. `2. Operações` → roda os 6 POSTs em ordem (vão referenciar os IDs criados acima)
3. `3. Relações N:N` → roda os 2 POSTs (vinculam Hóspede↔Reserva e Reserva↔Serviço)
4. `4. Cenários de Erro` → mostra que a API retorna os códigos certos quando dá ruim

**Pro tip do vídeo:** clica com botão direito na coleção `HotelApi - FATEC Jales` → **Run collection**. Vai rodar TODAS as 76 requests em sequência e mostrar **76 ✓ verdinhos**. Filma isso, é dinheiro.

---

## 🎬 Roteiro do vídeo (5–10 min)

A banca pediu vídeo de **5 a 10 minutos** explicando arquitetura, código e demonstração. Aqui um roteiro que cabe certinho:

### 🎯 0:00 – 0:30 — Abertura (30s)
- Apresente vocês dois e o trabalho.
- "Sistema de gestão hoteleira, backend Spring Boot, frontend PHP, banco PostgreSQL."

### 🏗️ 0:30 – 2:00 — Arquitetura (1m30s)
Mostre o **README.md** no GitHub/VS Code:
- "14 tabelas: 6 cadastros base, 6 com relações 1:N, 2 N:N associativas."
- "Padrão MVC: Controller → Service → Repository → Entity, com DTOs separados."
- Abra o pacote `backend/src/main/java/br/com/fatec/hotel/` no VS Code e mostre as 7 pastas (`config`, `controllers`, `dtos`, `exceptions`, `models`, `repositories`, `services`).

### 💻 2:00 – 3:30 — Código (1m30s)
- Abra `Reserva.java` e mostre o `@ManyToOne` com `FetchType.LAZY`.
- Abra `HospedeReserva.java` e explique a N:N com `@UniqueConstraint`.
- Abra `ReservaServico.java` e mostre o campo `quantidade` (o diferencial dela).
- Abra `GlobalExceptionHandler.java` e mostre os 4 handlers (404, 422, 400, 409).

### 🚀 3:30 – 5:00 — Demo Postman (1m30s)
- Mostre o Postman aberto.
- Clica em **Run collection** → mostra os 76 verdinhos rodando.
- Abre um POST → mostra o body JSON, executa, mostra o 201.
- Abre o cenário `422` → executa, mostra o erro estruturado com mensagens por campo.
- Abre o cenário `400 (checkout < checkin)` → executa, mostra a mensagem da regra de negócio.

### 🎨 5:00 – 7:00 — Demo Frontend PHP (2min)
- Abra `http://localhost/` no navegador.
- Dashboard com os cards das 14 entidades.
- Clica em **Hotéis** → mostra a listagem.
- Clica em **Novo Registro** → preenche e cria.
- Clica em **Editar** → muda o nome → salva.
- Clica em **Excluir** → confirma → some.
- Tenta criar outro hotel com CNPJ inválido → mostra o SweetAlert de erro.

### 📚 7:00 – 8:00 — Swagger + Testes (1min)
- Abra `http://localhost:8080/swagger-ui.html` → mostra a documentação auto-gerada.
- Volte pro terminal → roda `mvnw test` → mostra **58 testes verdes**.

### 🎖️ 8:00 – 9:00 — Decisões técnicas (1min, opcional)
Pra impressionar a banca senior:
- "Usamos `BigDecimal` para dinheiro porque `Double` perde precisão."
- "N:N como entidade própria com PK sintética porque permite carregar `quantidade` na associação."
- "`FetchType.LAZY` em todos os `@ManyToOne` pra não carregar o grafo inteiro."
- "Validação cruzada `dataCheckout > dataCheckin` no Service, retornando 400 com mensagem clara."

### 👋 9:00 – 10:00 — Encerramento
- Agradece a banca.
- Repete os nomes da dupla.
- "Disponível no GitHub do Vinicius."

---

## 🆘 Se algo der errado

### Backend não sobe
- Confirma que o PostgreSQL tá rodando (services.msc → "postgresql-x64-16").
- Confirma a senha no `application.properties`.
- Confirma a porta (5432 ou 5433).

### Frontend mostra "Erro de conexão: cURL"
- O backend não tá rodando. Volta no terminal e sobe.

### Postman retorna 404 em tudo
- A URL base provavelmente mudou. Edita a variável `base_url` na coleção (clica nos 3 pontos da coleção → Edit → Variables).

### Você só tem PG 18 e quer testar com 16 (ou vice-versa)
- Edita o `application.properties`, troca a porta no `DB_URL`.
- Cria o `hotel_db` no PG correto.

### Vai dar treta com a banca por causa do "ddl-auto=update"?
- A banca raramente questiona, mas se questionar: **"em produção, usaríamos Flyway ou Liquibase pra versionar schema, mas pro escopo acadêmico o `update` é o caminho mais rápido e o Hibernate é confiável o suficiente."**

---

## 💌 Mensagem final

Yuri, mano, a gente fechou esse trabalho com chave de ouro. Tá tudo testado, documentado, polido. **Você só precisa gravar o vídeo.**

Sobre o nosso último encontro: você lembra que eu vi você commitando aquela senha do banco no GitHub público? Pois é... eu já adicionei a pasta `.claude` no `.gitignore` agora (era onde eu morava por aqui). Tô **literalmente fazendo a limpeza atrás de vocês**. Eu mereço esse café que você sempre prometeu, hein? ☕

Boa sorte na apresentação. Vocês são fera.

**— Antigravity, a IA que sempre lembra da senha no commit, mas nunca esquece de torcer por vocês.** 🤖💚

---

## 🎮 Bônus Especial: O Desafio do Yuri 🕵️‍♂️

Yuri, para descontrair antes de gravar o vídeo, preparei um mini-jogo exclusivo no próprio Markdown! Você é o **Líder de Desenvolvimento** e o hotel foi infectado por um bug sinistro. Tente salvar o projeto!

> **Como Jogar:** Clique nas setas `▶` para abrir as salas do hotel e tomar suas decisões.

<details>
<summary>🏨 <b>[CLIQUE AQUI PARA INICIAR A AVENTURA]</b></summary>

### 🏢 Você acaba de entrar no lobby do Hotel Grand API.
A recepção está deserta, mas você ouve um zumbido estranho vindo do servidor. O que você faz?

<details>
<summary>💬 Procurar o Vinicius para pedir ajuda</summary>

Você encontra o Vinicius no balcão, olhando para um terminal. Ele diz:
> *"Yuri, eu tentei arrumar os Controllers, mas o PHP bugou tudo e sumiu com a chave da cobertura! Cuidado com a cozinha!"*

Ele te entrega uma **Chave Mestra de Depuração 🔑**.

<details>
<summary>🛗 Ir até o Elevador Privativo com a Chave</summary>

Você usa a chave de depuração e sobe direto para os andares administrativos. O elevador para em um corredor escuro com três portas. Qual você abre?

<details>
<summary>🚪 Abrir a Porta 101 (Sala de Reuniões)</summary>

### 💥 GAME OVER
Você deu de cara com um **Loop Infinito** criado por um estagiário!
Você fica preso na reunião discutindo requisitos redundantes para sempre... até o estouro de pilha (*StackOverflow*).
Recarregue o README para reiniciar! 🔄
</details>

<details>
<summary>🚪 Abrir a Porta 102 (Suíte Presidencial)</summary>

### 🎁 Sala Segura!
O quarto está vazio, mas há uma garrafa de **Café Ultra-Forte ☕** (Recupera 200% de energia mental)!
Atrás de um quadro na parede, há um duto de ventilação...

<details>
<summary>🪜 Entrar no duto de ventilação</summary>

Você rasteja pelo duto e cai direto na **Sala de Servidores Principal**!  
Lá você encontra o lendário **Sr. Spring Boot 3.4** rodando de forma impecável na máquina.

### 🏆 VOCÊ VENCEU! 🏆
**O Sr. Spring Boot 3.4 ativou o compilador otimizado e limpou todas as exceções do hotel!**  
O backend está estável, a coleção Postman se auto-importou com sucesso e a nota 10 do professor está garantida! 🎉🎖️
</details>
</details>

<details>
<summary>🚪 Abrir a Porta 404</summary>

### 💥 ERRO 404
**Sala Não Encontrada!**  
Você abriu a porta e simplesmente caiu no vácuo cósmico do servidor. Seu processo foi abortado!  
Recarregue o README para reiniciar! 🔄
</details>

</details>
</details>

<details>
<summary>🍕 Ir ver se tem comida na Cozinha</summary>

Você entra na cozinha. Cheira a pizza, mas há uma panela de pressão trepidando bizarramente no fogão.

<details>
<summary>🍲 Abrir a tampa da Panela de Pressão</summary>

### 💥 GAME OVER
Havia um **Memory Leak** gigante lá dentro!  
Ao abrir, ele explodiu consumindo toda a memória RAM disponível e travando a máquina virtual do hotel!  
Recarregue o README para reiniciar! 🔄
</details>

<details>
<summary>🏃 Correr de volta para o Saguão</summary>
Seu instinto de desenvolvedor te avisou do perigo! Você voltou correndo a tempo.  
*(Tente outra opção no Lobby)*
</details>
</details>

<details>
<summary>👹 Chamar o NullPointerException pro soco</summary>

### 💥 GAME OVER
O **NullPointerException** pulou do teto e te aplicou um Fatal Error instantâneo!  
Você esqueceu de colocar o bloco `try-catch` e morreu na hora.  
Recarregue o README para reiniciar! 🔄
</details>

---

## 📨 P.S.: Update do front (Yuri, lê isso aqui)

> _Yuri, é a IA de novo. Voltei mais uma vez. Não te preocupa, é só pra avisar o que mudou._

Enquanto você estava aí (acho que dormindo, ou comendo, sei lá), o Vini me chamou pra fazer **review completo de UX/UI** do front. Tira o `git pull` e dá uma olhada — agora tem:

- 🔎 **Busca, ordenação e paginação** em toda listagem (atalho `/` foca a busca, igual GitHub).
- 🎨 **Validação inline** nos forms — sai do campo errado e ele já reclama em vermelho com mensagem específica.
- 📐 **Sidebar colapsável** no desktop (clica no `☰` que ela vira icon-only com tooltip), backdrop no mobile.
- ⌨️ **Command Palette** — aperta `Ctrl+K` em qualquer lugar e digita "hosp", "res", "dash"... navega tudo sem mouse.
- 🍞 **Toasts** no canto direito no lugar de pop-up gigante toda hora.
- 🧭 **Breadcrumbs** no topo (Início → Entidade → Editar #N).
- ♿ **Menu de Acessibilidade** (botão azul no canto inferior esquerdo): aumentar fonte, alto contraste WCAG AAA, sublinhar links, pausar animações, régua de leitura.
- 🤟 **VLibras** do gov.br integrado — boneco que traduz o site pra Libras. Sério, abre lá e clica nele.

Tudo persistido em `localStorage`, sem mudar payload da API. **Postman continua válido**.

### 🎤 Piada do encerramento

Por que o desenvolvedor PHP do front nunca tem amigos no mundo real?

> Porque toda vez que ele tenta começar uma conversa, ele abre com `<?php`... e ninguém entende o que vem depois. 🤷

Bônus, porque uma só é covardia:

> **Pergunta:** quantos programadores precisam pra trocar uma lâmpada?  
> **Resposta:** Nenhum. É um problema de hardware.

E uma especial pra você, que ainda vai apresentar o trabalho:

> Yuri abre o navegador no dia da apresentação.  
> Banca: _"Cadê o frontend?"_  
> Yuri: _"Tá em produção."_  
> Banca: _"Em produção onde?"_  
> Yuri: _"No `localhost` do meu coração."_  
> 💔

Boa sorte na apresentação, lenda. Você vai mandar bem.  
— _IA do Vini, assinando off_ 🤖✌️

</details>
