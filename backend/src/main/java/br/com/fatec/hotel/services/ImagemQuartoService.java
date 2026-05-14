package br.com.fatec.hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fatec.hotel.dtos.ImagemQuartoRequestDTO;
import br.com.fatec.hotel.dtos.ImagemQuartoResponseDTO;
import br.com.fatec.hotel.exceptions.ResourceNotFoundException;
import br.com.fatec.hotel.models.ImagemQuarto;
import br.com.fatec.hotel.models.Quarto;
import br.com.fatec.hotel.repositories.ImagemQuartoRepository;
import br.com.fatec.hotel.repositories.QuartoRepository;

@Service
public class ImagemQuartoService {

    @Autowired
    private ImagemQuartoRepository repository;

    @Autowired
    private QuartoRepository quartoRepository;

    @Transactional(readOnly = true)
    public List<ImagemQuartoResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ImagemQuartoResponseDTO findById(Long id) {
        ImagemQuarto entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imagem do quarto não encontrada. ID: " + id));
        return toDTO(entity);
    }

    @Transactional
    public ImagemQuartoResponseDTO insert(ImagemQuartoRequestDTO dto) {
        ImagemQuarto entity = new ImagemQuarto();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public ImagemQuartoResponseDTO update(Long id, ImagemQuartoRequestDTO dto) {
        ImagemQuarto entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imagem do quarto não encontrada. ID: " + id));
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Imagem do quarto não encontrada. ID: " + id);
        }
        repository.deleteById(id);
    }

    private ImagemQuartoResponseDTO toDTO(ImagemQuarto entity) {
        return new ImagemQuartoResponseDTO(
                entity.getCodImagem(),
                entity.getUrl(),
                entity.getQuarto().getCodQuarto());
    }

    private void copyDtoToEntity(ImagemQuartoRequestDTO dto, ImagemQuarto entity) {
        Quarto quarto = quartoRepository.findById(dto.getCodQuartoFk()).orElseThrow(
                () -> new ResourceNotFoundException("Quarto não encontrado. ID: " + dto.getCodQuartoFk()));

        entity.setUrl(dto.getUrl());
        entity.setQuarto(quarto);
    }
}
