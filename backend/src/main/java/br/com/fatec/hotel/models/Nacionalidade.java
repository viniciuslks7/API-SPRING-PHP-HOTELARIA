package br.com.fatec.hotel.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nacionalidades")
public class Nacionalidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codnacionalidade")
    private Long codNacionalidade;

    @Column(nullable = false, length = 100)
    private String nomePais;

}
