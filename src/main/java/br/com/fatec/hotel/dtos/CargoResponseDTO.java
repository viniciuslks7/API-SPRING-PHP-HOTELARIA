package br.com.fatec.hotel.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CargoResponseDTO {
    
    private Long codCargo;
    private String nomeCargo;
    private Double salarioBase;

}
