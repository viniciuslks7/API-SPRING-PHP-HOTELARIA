package br.com.fatec.hotel.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CargoRequestDTO {

    @NotBlank(message = "O nome do cargo é obrigatório")
    @Size(max = 150, message = "O nome do cargo deve ter no máximo 150 caracteres")
    private String nomeCargo;

    @NotNull(message = "O salário base é obrigatório")
    @Positive(message = "O salário base deve ser positivo!")
    private BigDecimal salarioBase;

}
