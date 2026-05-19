package br.com.fatec.hotel.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResponseDTO {
    private Long codReserva;
    private LocalDate dataCheckin;
    private LocalDate dataCheckout;
    private BigDecimal valorTotal;
    private Long codCanalFk;
}
