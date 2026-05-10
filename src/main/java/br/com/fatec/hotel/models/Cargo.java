package br.com.fatec.hotel.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//   - `@Table(name = "cargos")`
//   - `Long id` (com `@Id` e `@Column(name = "id_cargo")`)
//   - `String nomeCargo` (`@Column(nullable = false)`)
//   - `Double salarioBase`

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cargos")
public class Cargo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo")
    private Long id;

    @Column(nullable = false, length = 150)
    private String nomeCargo;

    @Column(nullable = false)
    private Double salarioBase;

}
