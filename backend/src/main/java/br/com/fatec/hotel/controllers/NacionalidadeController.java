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

import br.com.fatec.hotel.dtos.NacionalidadeRequestDTO;
import br.com.fatec.hotel.dtos.NacionalidadeResponseDTO;
import br.com.fatec.hotel.services.NacionalidadeService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/nacionalidades")
public class NacionalidadeController {

    @Autowired
    private NacionalidadeService service;

    @GetMapping
    public ResponseEntity<List<NacionalidadeResponseDTO>> findAll() {
        List<NacionalidadeResponseDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<NacionalidadeResponseDTO> findById(@PathVariable Long id) {
        NacionalidadeResponseDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<NacionalidadeResponseDTO> insert(@Valid @RequestBody NacionalidadeRequestDTO requestDTO) {
        NacionalidadeResponseDTO responseDTO = service.insert(requestDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(responseDTO.getCodNacionalidade()).toUri();
        return ResponseEntity.created(uri).body(responseDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<NacionalidadeResponseDTO> update(@PathVariable Long id,
            @Valid @RequestBody NacionalidadeRequestDTO requestDTO) {
        NacionalidadeResponseDTO responseDTO = service.update(id, requestDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
