package br.com.fatec.hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fatec.hotel.dtos.ReservaRequestDTO;
import br.com.fatec.hotel.dtos.ReservaResponseDTO;
import br.com.fatec.hotel.exceptions.ResourceNotFoundException;
import br.com.fatec.hotel.models.CanalReserva;
import br.com.fatec.hotel.models.Reserva;
import br.com.fatec.hotel.repositories.CanalReservaRepository;
import br.com.fatec.hotel.repositories.ReservaRepository;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository repository;

    @Autowired
    private CanalReservaRepository canalReservaRepository;

    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservaResponseDTO findById(Long id) {
        Reserva entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada. ID: " + id));
        return toDTO(entity);
    }

    @Transactional
    public ReservaResponseDTO insert(ReservaRequestDTO dto) {
        Reserva entity = new Reserva();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public ReservaResponseDTO update(Long id, ReservaRequestDTO dto) {
        Reserva entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada. ID: " + id));
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Reserva não encontrada. ID: " + id);
        }
        repository.deleteById(id);
    }

    private ReservaResponseDTO toDTO(Reserva entity) {
        return new ReservaResponseDTO(
                entity.getCodReserva(),
                entity.getDataCheckin(),
                entity.getDataCheckout(),
                entity.getValorTotal(),
                entity.getCanalReserva().getCodCanais());
    }

    private void copyDtoToEntity(ReservaRequestDTO dto, Reserva entity) {
        CanalReserva canalReserva = canalReservaRepository.findById(dto.getCodCanalFk()).orElseThrow(
                () -> new ResourceNotFoundException("Canal de Reserva não encontrado. ID: " + dto.getCodCanalFk()));

        entity.setDataCheckin(dto.getDataCheckin());
        entity.setDataCheckout(dto.getDataCheckout());
        entity.setValorTotal(dto.getValorTotal());
        entity.setCanalReserva(canalReserva);
    }
}
