package br.com.fatec.hotel.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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

    @Column(nullable = false)
    private Double salarioBase;

}
