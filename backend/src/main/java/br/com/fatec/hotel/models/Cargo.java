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
@Table(name = "cargos")
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codcargo")
    private Long codCargo;

    @Column(nullable = false, length = 150)
    private String nomeCargo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salarioBase;

}
