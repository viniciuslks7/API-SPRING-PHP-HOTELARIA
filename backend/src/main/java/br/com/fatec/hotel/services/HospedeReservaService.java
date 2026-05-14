package br.com.fatec.hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fatec.hotel.dtos.HospedeReservaRequestDTO;
import br.com.fatec.hotel.dtos.HospedeReservaResponseDTO;
import br.com.fatec.hotel.exceptions.ResourceNotFoundException;
import br.com.fatec.hotel.models.Hospede;
import br.com.fatec.hotel.models.HospedeReserva;
import br.com.fatec.hotel.models.Reserva;
import br.com.fatec.hotel.repositories.HospedeRepository;
import br.com.fatec.hotel.repositories.HospedeReservaRepository;
import br.com.fatec.hotel.repositories.ReservaRepository;

@Service
public class HospedeReservaService {

    @Autowired
    private HospedeReservaRepository repository;

    @Autowired
    private HospedeRepository hospedeRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Transactional(readOnly = true)
    public List<HospedeReservaResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HospedeReservaResponseDTO findById(Long id) {
        HospedeReserva entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Relação hóspede-reserva não encontrada. ID: " + id));
        return toDTO(entity);
    }

    @Transactional
    public HospedeReservaResponseDTO insert(HospedeReservaRequestDTO dto) {
        HospedeReserva entity = new HospedeReserva();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public HospedeReservaResponseDTO update(Long id, HospedeReservaRequestDTO dto) {
        HospedeReserva entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Relação hóspede-reserva não encontrada. ID: " + id));
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Relação hóspede-reserva não encontrada. ID: " + id);
        }
        repository.deleteById(id);
    }

    private HospedeReservaResponseDTO toDTO(HospedeReserva entity) {
        return new HospedeReservaResponseDTO(
                entity.getCodHospedeReserva(),
                entity.getHospede().getCodHospede(),
                entity.getReserva().getCodReserva());
    }

    private void copyDtoToEntity(HospedeReservaRequestDTO dto, HospedeReserva entity) {
        Hospede hospede = hospedeRepository.findById(dto.getCodHospedeFk())
                .orElseThrow(() -> new ResourceNotFoundException("Hóspede não encontrado. ID: " + dto.getCodHospedeFk()));

        Reserva reserva = reservaRepository.findById(dto.getCodReservaFk())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada. ID: " + dto.getCodReservaFk()));

        entity.setHospede(hospede);
        entity.setReserva(reserva);
    }
}
