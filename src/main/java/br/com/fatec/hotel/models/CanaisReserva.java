package br.com.fatec.hotel.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "canais_reserva")
public class CanaisReserva {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_canal")
    private long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(name = "taxa_comissao", nullable = false)
    private Double taxaComissao;

}
