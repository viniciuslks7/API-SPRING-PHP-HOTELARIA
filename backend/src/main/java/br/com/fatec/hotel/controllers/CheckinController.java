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

import br.com.fatec.hotel.dtos.CheckinRequestDTO;
import br.com.fatec.hotel.dtos.CheckinResponseDTO;
import br.com.fatec.hotel.services.CheckinService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/checkins")
public class CheckinController {

    @Autowired
    private CheckinService service;

    @GetMapping
    public ResponseEntity<List<CheckinResponseDTO>> findAll() {
        List<CheckinResponseDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CheckinResponseDTO> findById(@PathVariable Long id) {
        CheckinResponseDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<CheckinResponseDTO> insert(@Valid @RequestBody CheckinRequestDTO requestDTO) {
        CheckinResponseDTO responseDTO = service.insert(requestDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(responseDTO.getCodCheckin()).toUri();
        return ResponseEntity.created(uri).body(responseDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CheckinResponseDTO> update(@PathVariable Long id, @Valid @RequestBody CheckinRequestDTO requestDTO) {
        CheckinResponseDTO responseDTO = service.update(id, requestDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
