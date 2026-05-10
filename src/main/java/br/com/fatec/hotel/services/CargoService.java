package br.com.fatec.hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fatec.hotel.dtos.CargoRequestDTO;
import br.com.fatec.hotel.dtos.CargoResponseDTO;
import br.com.fatec.hotel.exceptions.ResourceNotFoundException;
import br.com.fatec.hotel.models.Cargo;
import br.com.fatec.hotel.repositories.CargoRepository;

@Service
public class CargoService {

    @Autowired
    private CargoRepository repository;

    @Transactional(readOnly = true)
    public List<CargoResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CargoResponseDTO findById(Long id) {
        Cargo entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cargo não encontrado. ID: " + id));
        return toDTO(entity);
    }

    @Transactional
    public CargoResponseDTO insert(CargoRequestDTO dto) {
        Cargo entity = new Cargo();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public CargoResponseDTO update(Long id, CargoRequestDTO dto) {
        Cargo entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cargo não encontrado. ID: " + id));
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Cargo não encontrado. ID: " + id);
        }
        repository.deleteById(id);
    }

    private CargoResponseDTO toDTO(Cargo entity) {
        return new CargoResponseDTO(
                entity.getId(),
                entity.getNomeCargo(),
                entity.getSalarioBase());
    }

    private void copyDtoToEntity(CargoRequestDTO dto, Cargo entity) {
        entity.setNomeCargo(dto.getNomeCargo());
        entity.setSalarioBase(dto.getSalarioBase());
    }

}
