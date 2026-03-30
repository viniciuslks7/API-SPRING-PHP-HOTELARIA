package br.com.fatec.hotel.services;

import br.com.fatec.hotel.dtos.HotelRequestDTO;
import br.com.fatec.hotel.dtos.HotelResponseDTO;
import br.com.fatec.hotel.exceptions.ResourceNotFoundException;
import br.com.fatec.hotel.models.Hotel;
import br.com.fatec.hotel.repositories.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelService {

    @Autowired
    private HotelRepository repository;

    @Transactional(readOnly = true)
    public List<HotelResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HotelResponseDTO findById(Long id) {
        Hotel entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Hotel não encontrado. ID: " + id));
        return toDTO(entity);
    }

    @Transactional
    public HotelResponseDTO insert(HotelRequestDTO dto) {
        Hotel entity = new Hotel();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public HotelResponseDTO update(Long id, HotelRequestDTO dto) {
        Hotel entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Hotel não encontrado. ID: " + id));
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Hotel não encontrado. ID: " + id);
        }
        repository.deleteById(id);
    }

    private HotelResponseDTO toDTO(Hotel entity) {
        return new HotelResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getCnpj(),
                entity.getClassificacaoEstrelas(),
                entity.getEndereco(),
                entity.getTelefone()
        );
    }

    private void copyDtoToEntity(HotelRequestDTO dto, Hotel entity) {
        entity.setNome(dto.getNome());
        entity.setCnpj(dto.getCnpj());
        entity.setClassificacaoEstrelas(dto.getClassificacaoEstrelas());
        entity.setEndereco(dto.getEndereco());
        entity.setTelefone(dto.getTelefone());
    }
}
