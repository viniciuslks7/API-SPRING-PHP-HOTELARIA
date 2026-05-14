package br.com.fatec.hotel.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tipos_quarto")
public class TipoQuarto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codtipo")
    private Long codTipo;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(name = "precobase", nullable = false)
    private Double precoBase;
}
