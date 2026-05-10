package br.com.fatec.hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fatec.hotel.dtos.CanaisReservaRequestDTO;
import br.com.fatec.hotel.dtos.CanaisReservaResponseDTO;
import br.com.fatec.hotel.exceptions.ResourceNotFoundException;
import br.com.fatec.hotel.models.CanaisReserva;
import br.com.fatec.hotel.repositories.CanaisReservaRepository;

@Service
public class CanaisReservaService {
    
    @Autowired
    private CanaisReservaRepository repository;

    @Transactional(readOnly = true)
    public List<CanaisReservaResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CanaisReservaResponseDTO findById(Long id) {
        CanaisReserva entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Canal de Reserva não encontrado. ID: " + id));
        return toDTO(entity);
    }

    @Transactional
    public CanaisReservaResponseDTO insert(CanaisReservaRequestDTO dto) {
        CanaisReserva entity = new CanaisReserva();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public CanaisReservaResponseDTO update(Long id, CanaisReservaRequestDTO dto) {
        CanaisReserva entity = repository.findById(id).orElseThrow(
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

    private CanaisReservaResponseDTO toDTO(CanaisReserva entity) {
        return new CanaisReservaResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getTaxaComissao());
    }

    private void copyDtoToEntity(CanaisReservaRequestDTO dto, CanaisReserva entity) {
        entity.setNome(dto.getNome());
        entity.setTaxaComissao(dto.getTaxaComissao());
    }

}
