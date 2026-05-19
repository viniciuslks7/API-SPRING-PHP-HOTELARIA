package br.com.fatec.hotel.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaRequestDTO {

    @NotNull(message = "A data de check-in é obrigatória")
    private LocalDate dataCheckin;

    @NotNull(message = "A data de check-out é obrigatória")
    private LocalDate dataCheckout;

    @NotNull(message = "O valor total é obrigatório")
    @Positive(message = "O valor total deve ser positivo")
    private BigDecimal valorTotal;

    @NotNull(message = "O código do canal de reserva é obrigatório")
    private Long codCanalFk;
}
