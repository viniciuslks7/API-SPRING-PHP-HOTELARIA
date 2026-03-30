package br.com.fatec.hotel.controllers;

import br.com.fatec.hotel.dtos.HotelRequestDTO;
import br.com.fatec.hotel.dtos.HotelResponseDTO;
import br.com.fatec.hotel.services.HotelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/hoteis")
public class HotelController {

    @Autowired
    private HotelService service;

    @GetMapping
    public ResponseEntity<List<HotelResponseDTO>> findAll() {
        List<HotelResponseDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<HotelResponseDTO> findById(@PathVariable Long id) {
        HotelResponseDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<HotelResponseDTO> insert(@Valid @RequestBody HotelRequestDTO requestDTO) {
        HotelResponseDTO responseDTO = service.insert(requestDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(responseDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(responseDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<HotelResponseDTO> update(@PathVariable Long id, @Valid @RequestBody HotelRequestDTO requestDTO) {
        HotelResponseDTO responseDTO = service.update(id, requestDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
