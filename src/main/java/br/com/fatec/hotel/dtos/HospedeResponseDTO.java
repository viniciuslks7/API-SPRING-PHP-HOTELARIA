package br.com.fatec.hotel.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospedeResponseDTO {
    private Long codHospede;
    private String nome;
    private String documento;
    private Long codNacionalidadeFk;
}
