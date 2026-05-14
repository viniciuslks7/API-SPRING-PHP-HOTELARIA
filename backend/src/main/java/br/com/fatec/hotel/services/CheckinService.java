package br.com.fatec.hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fatec.hotel.dtos.CheckinRequestDTO;
import br.com.fatec.hotel.dtos.CheckinResponseDTO;
import br.com.fatec.hotel.exceptions.ResourceNotFoundException;
import br.com.fatec.hotel.models.Checkin;
import br.com.fatec.hotel.models.Funcionario;
import br.com.fatec.hotel.models.Reserva;
import br.com.fatec.hotel.repositories.CheckinRepository;
import br.com.fatec.hotel.repositories.FuncionarioRepository;
import br.com.fatec.hotel.repositories.ReservaRepository;

@Service
public class CheckinService {

    @Autowired
    private CheckinRepository repository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Transactional(readOnly = true)
    public List<CheckinResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CheckinResponseDTO findById(Long id) {
        Checkin entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Check-in não encontrado. ID: " + id));
        return toDTO(entity);
    }

    @Transactional
    public CheckinResponseDTO insert(CheckinRequestDTO dto) {
        Checkin entity = new Checkin();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public CheckinResponseDTO update(Long id, CheckinRequestDTO dto) {
        Checkin entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Check-in não encontrado. ID: " + id));
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Check-in não encontrado. ID: " + id);
        }
        repository.deleteById(id);
    }

    private CheckinResponseDTO toDTO(Checkin entity) {
        return new CheckinResponseDTO(
                entity.getCodCheckin(),
                entity.getDataHoraReal(),
                entity.getReserva().getCodReserva(),
                entity.getFuncionario().getCodFuncionario());
    }

    private void copyDtoToEntity(CheckinRequestDTO dto, Checkin entity) {
        Reserva reserva = reservaRepository.findById(dto.getCodReservaFk()).orElseThrow(
                () -> new ResourceNotFoundException("Reserva não encontrada. ID: " + dto.getCodReservaFk()));
        Funcionario funcionario = funcionarioRepository.findById(dto.getCodFuncionarioFk()).orElseThrow(
                () -> new ResourceNotFoundException("Funcionário não encontrado. ID: " + dto.getCodFuncionarioFk()));

        entity.setDataHoraReal(dto.getDataHoraReal());
        entity.setReserva(reserva);
        entity.setFuncionario(funcionario);
    }
}
