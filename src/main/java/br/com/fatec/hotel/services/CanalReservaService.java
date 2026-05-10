package br.com.fatec.hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fatec.hotel.dtos.CanalReservaRequestDTO;
import br.com.fatec.hotel.dtos.CanalReservaResponseDTO;
import br.com.fatec.hotel.exceptions.ResourceNotFoundException;
import br.com.fatec.hotel.models.CanalReserva;
import br.com.fatec.hotel.repositories.CanalReservaRepository;

@Service
public class CanalReservaService {
    
    @Autowired
    private CanalReservaRepository repository;

    @Transactional(readOnly = true)
    public List<CanalReservaResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CanalReservaResponseDTO findById(Long id) {
        CanalReserva entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Canal de Reserva não encontrado. ID: " + id));
        return toDTO(entity);
    }

    @Transactional
    public CanalReservaResponseDTO insert(CanalReservaRequestDTO dto) {
        CanalReserva entity = new CanalReserva();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public CanalReservaResponseDTO update(Long id, CanalReservaRequestDTO dto) {
        CanalReserva entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Canal de Reserva não encontrado. ID: " + id));
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Canal de Reserva não encontrado. ID: " + id);
        }
        repository.deleteById(id);
    }

    private CanalReservaResponseDTO toDTO(CanalReserva entity) {
        return new CanalReservaResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getTaxaComissao());
    }

    private void copyDtoToEntity(CanalReservaRequestDTO dto, CanalReserva entity) {
        entity.setNome(dto.getNome());
        entity.setTaxaComissao(dto.getTaxaComissao());
    }

}
