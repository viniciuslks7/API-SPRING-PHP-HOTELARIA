package br.com.fatec.hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fatec.hotel.dtos.TipoQuartoRequestDTO;
import br.com.fatec.hotel.dtos.TipoQuartoResponseDTO;
import br.com.fatec.hotel.exceptions.ResourceNotFoundException;
import br.com.fatec.hotel.models.TipoQuarto;
import br.com.fatec.hotel.repositories.TipoQuartoRepository;

@Service
public class TipoQuartoService {

    @Autowired
    private TipoQuartoRepository repository;

    @Transactional(readOnly = true)
    public List<TipoQuartoResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TipoQuartoResponseDTO findById(Long id) {
        TipoQuarto entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Tipo de Quarto não encontrado. ID: " + id));
        return toDTO(entity);
    }

    @Transactional
    public TipoQuartoResponseDTO insert(TipoQuartoRequestDTO dto) {
        TipoQuarto entity = new TipoQuarto();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public TipoQuartoResponseDTO update(Long id, TipoQuartoRequestDTO dto) {
        TipoQuarto entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Tipo de Quarto não encontrado. ID: " + id));
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Tipo de Quarto não encontrado. ID: " + id);
        }
        repository.deleteById(id);
    }

    private TipoQuartoResponseDTO toDTO(TipoQuarto entity) {
        return new TipoQuartoResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getPrecoBase(),
                entity.getCapacidadePessoas());
    }

    private void copyDtoToEntity(TipoQuartoRequestDTO dto, TipoQuarto entity) {
        entity.setNome(dto.getNome());
        entity.setPrecoBase(dto.getPrecoBase());
        entity.setCapacidadePessoas(dto.getCapacidadePessoas());
    }

}
