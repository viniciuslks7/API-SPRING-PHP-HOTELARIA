package br.com.fatec.hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fatec.hotel.dtos.ServicoExtraRequestDTO;
import br.com.fatec.hotel.dtos.ServicoExtraResponseDTO;
import br.com.fatec.hotel.exceptions.ResourceNotFoundException;
import br.com.fatec.hotel.models.ServicoExtra;
import br.com.fatec.hotel.repositories.ServicoExtraRepository;

@Service
public class ServicoExtraService {

    @Autowired
    private ServicoExtraRepository repository;

    @Transactional(readOnly = true)
    public List<ServicoExtraResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServicoExtraResponseDTO findById(Long id) {
        ServicoExtra entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Serviço Extra não encontrado. ID: " + id));
        return toDTO(entity);
    }

    @Transactional
    public ServicoExtraResponseDTO insert(ServicoExtraRequestDTO dto) {
        ServicoExtra entity = new ServicoExtra();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public ServicoExtraResponseDTO update(Long id, ServicoExtraRequestDTO dto) {
        ServicoExtra entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Serviço Extra não encontrado. ID: " + id));
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Serviço Extra não encontrado. ID: " + id);
        }
        repository.deleteById(id);
    }

    private ServicoExtraResponseDTO toDTO(ServicoExtra entity) {
        return new ServicoExtraResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getPreco());
    }

    private void copyDtoToEntity(ServicoExtraRequestDTO dto, ServicoExtra entity) {
        entity.setNome(dto.getNome());
        entity.setPreco(dto.getPreco());
    }

}
