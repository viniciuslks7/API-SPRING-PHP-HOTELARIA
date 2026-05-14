package br.com.fatec.hotel.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuartoRequestDTO {

    @NotNull(message = "O número do quarto é obrigatório")
    @Min(value = 1, message = "O número do quarto deve ser maior que zero")
    private Integer numero;

    @NotNull(message = "O andar é obrigatório")
    @Min(value = 0, message = "O andar não pode ser negativo")
    private Integer andar;

    @NotNull(message = "O código do hotel é obrigatório")
    private Long codHotelFk;

    @NotNull(message = "O código do tipo de quarto é obrigatório")
    private Long codTipoFk;
}
