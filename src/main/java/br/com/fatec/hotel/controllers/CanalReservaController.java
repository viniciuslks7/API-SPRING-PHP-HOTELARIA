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

import br.com.fatec.hotel.dtos.CanalReservaRequestDTO;
import br.com.fatec.hotel.dtos.CanalReservaResponseDTO;
import br.com.fatec.hotel.services.CanalReservaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/canais-reserva")
public class CanalReservaController {
    
    @Autowired
    private CanalReservaService service;

    @GetMapping
    public ResponseEntity<List<CanalReservaResponseDTO>> findAll() {
        List<CanalReservaResponseDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CanalReservaResponseDTO> findById(@PathVariable Long id) {
        CanalReservaResponseDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<CanalReservaResponseDTO> insert(@Valid @RequestBody CanalReservaRequestDTO requestDTO) {
        CanalReservaResponseDTO responseDTO = service.insert(requestDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(responseDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(responseDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CanalReservaResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody CanalReservaRequestDTO requestDTO) {
        CanalReservaResponseDTO responseDTO = service.update(id, requestDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
