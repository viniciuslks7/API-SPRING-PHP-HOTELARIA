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

import br.com.fatec.hotel.dtos.ImagemQuartoRequestDTO;
import br.com.fatec.hotel.dtos.ImagemQuartoResponseDTO;
import br.com.fatec.hotel.services.ImagemQuartoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/imagens-quarto")
public class ImagemQuartoController {

    @Autowired
    private ImagemQuartoService service;

    @GetMapping
    public ResponseEntity<List<ImagemQuartoResponseDTO>> findAll() {
        List<ImagemQuartoResponseDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ImagemQuartoResponseDTO> findById(@PathVariable Long id) {
        ImagemQuartoResponseDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<ImagemQuartoResponseDTO> insert(@Valid @RequestBody ImagemQuartoRequestDTO requestDTO) {
        ImagemQuartoResponseDTO responseDTO = service.insert(requestDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(responseDTO.getCodImagem()).toUri();
        return ResponseEntity.created(uri).body(responseDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ImagemQuartoResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody ImagemQuartoRequestDTO requestDTO) {
        ImagemQuartoResponseDTO responseDTO = service.update(id, requestDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
