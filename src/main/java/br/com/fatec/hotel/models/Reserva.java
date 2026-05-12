package br.com.fatec.hotel.models;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codreserva")
    private Long codReserva;

    @Column(name = "datacheckin", nullable = false)
    private LocalDate dataCheckin;

    @Column(name = "datacheckout", nullable = false)
    private LocalDate dataCheckout;

    @Column(name = "valortotal", nullable = false)
    private Double valorTotal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codcanalfk", nullable = false)
    private CanalReserva canalReserva;
}
