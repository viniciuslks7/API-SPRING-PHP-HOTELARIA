package br.com.fatec.hotel.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hoteis")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hotel")
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 18, unique = true)
    private String cnpj;

    @Column(name = "classificacao_estrelas")
    private Integer classificacaoEstrelas;

    @Column(length = 255)
    private String endereco;

    @Column(length = 20)
    private String telefone;
}
