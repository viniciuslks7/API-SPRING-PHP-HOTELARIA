package br.com.fatec.hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fatec.hotel.dtos.NacionalidadeRequestDTO;
import br.com.fatec.hotel.dtos.NacionalidadeResponseDTO;
import br.com.fatec.hotel.exceptions.ResourceNotFoundException;
import br.com.fatec.hotel.models.Nacionalidade;
import br.com.fatec.hotel.repositories.NacionalidadeRepository;

@Service
public class NacionalidadeService {

    @Autowired
    private NacionalidadeRepository repository;

    @Transactional(readOnly = true)
    public List<NacionalidadeResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NacionalidadeResponseDTO findById(Long id) {
        Nacionalidade entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Nacionalidade não encontrada. ID: " + id));
        return toDTO(entity);
    }

    @Transactional
    public NacionalidadeResponseDTO insert(NacionalidadeRequestDTO dto) {
        Nacionalidade entity = new Nacionalidade();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public NacionalidadeResponseDTO update(Long id, NacionalidadeRequestDTO dto) {
        Nacionalidade entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Nacionalidade não encontrada. ID: " + id));
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Nacionalidade não encontrada. ID: " + id);
        }
        repository.deleteById(id);
    }

    private NacionalidadeResponseDTO toDTO(Nacionalidade entity) {
        return new NacionalidadeResponseDTO(
                entity.getCodNacionalidade(),
                entity.getNomePais());
    }

    private void copyDtoToEntity(NacionalidadeRequestDTO dto, Nacionalidade entity) {
        entity.setNomePais(dto.getNomePais());

    }

}
