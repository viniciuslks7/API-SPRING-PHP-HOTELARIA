package br.com.fatec.hotel.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hospedes_reservas", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"codhospedefk", "codreservafk"})
})
public class HospedeReserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codhospedereserva")
    private Long codHospedeReserva;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codhospedefk", nullable = false)
    private Hospede hospede;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codreservafk", nullable = false)
    private Reserva reserva;
}
