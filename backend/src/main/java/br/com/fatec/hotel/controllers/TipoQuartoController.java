package br.com.fatec.hotel.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.fatec.hotel.dtos.TipoQuartoRequestDTO;
import br.com.fatec.hotel.dtos.TipoQuartoResponseDTO;
import br.com.fatec.hotel.services.TipoQuartoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/tipos-quarto")
public class TipoQuartoController {

    @Autowired
    private TipoQuartoService service;

    @GetMapping
    public ResponseEntity<List<TipoQuartoResponseDTO>> findAll() {
        List<TipoQuartoResponseDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TipoQuartoResponseDTO> findById(@PathVariable Long id) {
        TipoQuartoResponseDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<TipoQuartoResponseDTO> insert(@Valid @RequestBody TipoQuartoRequestDTO requestDTO) {
        TipoQuartoResponseDTO responseDTO = service.insert(requestDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(responseDTO.getCodTipo()).toUri();
        return ResponseEntity.created(uri).body(responseDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<TipoQuartoResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody TipoQuartoRequestDTO requestDTO) {
        TipoQuartoResponseDTO responseDTO = service.update(id, requestDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
