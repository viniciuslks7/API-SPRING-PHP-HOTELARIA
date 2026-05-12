package br.com.fatec.hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fatec.hotel.dtos.FuncionarioRequestDTO;
import br.com.fatec.hotel.dtos.FuncionarioResponseDTO;
import br.com.fatec.hotel.exceptions.ResourceNotFoundException;
import br.com.fatec.hotel.models.Cargo;
import br.com.fatec.hotel.models.Funcionario;
import br.com.fatec.hotel.repositories.CargoRepository;
import br.com.fatec.hotel.repositories.FuncionarioRepository;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository repository;

    @Autowired
    private CargoRepository cargoRepository;

    @Transactional(readOnly = true)
    public List<FuncionarioResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FuncionarioResponseDTO findById(Long id) {
        Funcionario entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado. ID: " + id));
        return toDTO(entity);
    }

    @Transactional
    public FuncionarioResponseDTO insert(FuncionarioRequestDTO dto) {
        Funcionario entity = new Funcionario();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public FuncionarioResponseDTO update(Long id, FuncionarioRequestDTO dto) {
        Funcionario entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionário não encontrado. ID: " + id));
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Funcionário não encontrado. ID: " + id);
        }
        repository.deleteById(id);
    }

    private FuncionarioResponseDTO toDTO(Funcionario entity) {
        return new FuncionarioResponseDTO(
                entity.getCodFuncionario(),
                entity.getNome(),
                entity.getCargo().getCodCargo());
    }

    private void copyDtoToEntity(FuncionarioRequestDTO dto, Funcionario entity) {
        Cargo cargo = cargoRepository.findById(dto.getCodCargoFk())
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado. ID: " + dto.getCodCargoFk()));

        entity.setNome(dto.getNome());
        entity.setCargo(cargo);
    }
}
