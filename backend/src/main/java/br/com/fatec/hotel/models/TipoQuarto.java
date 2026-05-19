package br.com.fatec.hotel.models;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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

    @Column(name = "precobase", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoBase;
}
