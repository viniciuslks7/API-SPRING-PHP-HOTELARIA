package br.com.fatec.hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fatec.hotel.dtos.HospedeRequestDTO;
import br.com.fatec.hotel.dtos.HospedeResponseDTO;
import br.com.fatec.hotel.exceptions.ResourceNotFoundException;
import br.com.fatec.hotel.models.Hospede;
import br.com.fatec.hotel.models.Nacionalidade;
import br.com.fatec.hotel.repositories.HospedeRepository;
import br.com.fatec.hotel.repositories.NacionalidadeRepository;

@Service
public class HospedeService {

    @Autowired
    private HospedeRepository repository;

    @Autowired
    private NacionalidadeRepository nacionalidadeRepository;

    @Transactional(readOnly = true)
    public List<HospedeResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HospedeResponseDTO findById(Long id) {
        Hospede entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hóspede não encontrado. ID: " + id));
        return toDTO(entity);
    }

    @Transactional
    public HospedeResponseDTO insert(HospedeRequestDTO dto) {
        Hospede entity = new Hospede();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public HospedeResponseDTO update(Long id, HospedeRequestDTO dto) {
        Hospede entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hóspede não encontrado. ID: " + id));
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Hóspede não encontrado. ID: " + id);
        }
        repository.deleteById(id);
    }

    private HospedeResponseDTO toDTO(Hospede entity) {
        return new HospedeResponseDTO(
                entity.getCodHospede(),
                entity.getNome(),
                entity.getDocumento(),
                entity.getNacionalidade().getCodNacionalidade());
    }

    private void copyDtoToEntity(HospedeRequestDTO dto, Hospede entity) {
        Nacionalidade nacionalidade = nacionalidadeRepository.findById(dto.getCodNacionalidadeFk()).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Nacionalidade não encontrada. ID: " + dto.getCodNacionalidadeFk()));

        entity.setNome(dto.getNome());
        entity.setDocumento(dto.getDocumento());
        entity.setNacionalidade(nacionalidade);
    }
}
