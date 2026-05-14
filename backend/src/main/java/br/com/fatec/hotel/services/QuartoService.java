package br.com.fatec.hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fatec.hotel.dtos.QuartoRequestDTO;
import br.com.fatec.hotel.dtos.QuartoResponseDTO;
import br.com.fatec.hotel.exceptions.ResourceNotFoundException;
import br.com.fatec.hotel.models.Hotel;
import br.com.fatec.hotel.models.Quarto;
import br.com.fatec.hotel.models.TipoQuarto;
import br.com.fatec.hotel.repositories.HotelRepository;
import br.com.fatec.hotel.repositories.QuartoRepository;
import br.com.fatec.hotel.repositories.TipoQuartoRepository;

@Service
public class QuartoService {

    @Autowired
    private QuartoRepository repository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private TipoQuartoRepository tipoQuartoRepository;

    @Transactional(readOnly = true)
    public List<QuartoResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public QuartoResponseDTO findById(Long id) {
        Quarto entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quarto não encontrado. ID: " + id));
        return toDTO(entity);
    }

    @Transactional
    public QuartoResponseDTO insert(QuartoRequestDTO dto) {
        Quarto entity = new Quarto();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public QuartoResponseDTO update(Long id, QuartoRequestDTO dto) {
        Quarto entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quarto não encontrado. ID: " + id));
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Quarto não encontrado. ID: " + id);
        }
        repository.deleteById(id);
    }

    private QuartoResponseDTO toDTO(Quarto entity) {
        return new QuartoResponseDTO(
                entity.getCodQuarto(),
                entity.getNumero(),
                entity.getAndar(),
                entity.getHotel().getCodHotel(),
                entity.getTipoQuarto().getCodTipo());
    }

    private void copyDtoToEntity(QuartoRequestDTO dto, Quarto entity) {
        Hotel hotel = hotelRepository.findById(dto.getCodHotelFk())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel não encontrado. ID: " + dto.getCodHotelFk()));
        TipoQuarto tipoQuarto = tipoQuartoRepository.findById(dto.getCodTipoFk()).orElseThrow(
                () -> new ResourceNotFoundException("Tipo de Quarto não encontrado. ID: " + dto.getCodTipoFk()));

        entity.setNumero(dto.getNumero());
        entity.setAndar(dto.getAndar());
        entity.setHotel(hotel);
        entity.setTipoQuarto(tipoQuarto);
    }
}
