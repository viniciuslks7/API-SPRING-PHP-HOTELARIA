package br.com.fatec.hotel.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioResponseDTO {
    private Long codFuncionario;
    private String nome;
    private Long codCargoFk;
}
