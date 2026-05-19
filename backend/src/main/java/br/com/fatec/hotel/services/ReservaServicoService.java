package br.com.fatec.hotel.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fatec.hotel.dtos.ReservaServicoRequestDTO;
import br.com.fatec.hotel.dtos.ReservaServicoResponseDTO;
import br.com.fatec.hotel.exceptions.ResourceNotFoundException;
import br.com.fatec.hotel.models.ServicoExtra;
import br.com.fatec.hotel.models.ReservaServico;
import br.com.fatec.hotel.models.Reserva;
import br.com.fatec.hotel.repositories.ServicoExtraRepository;
import br.com.fatec.hotel.repositories.ReservaServicoRepository;
import br.com.fatec.hotel.repositories.ReservaRepository;

@Service
public class ReservaServicoService {

    @Autowired
    private ReservaServicoRepository repository;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ServicoExtraRepository servicoExtraRepository;

    @Transactional(readOnly = true)
    public List<ReservaServicoResponseDTO> findAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReservaServicoResponseDTO findById(Long id) {
        ReservaServico entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Relação reserva-serviço não encontrada. ID: " + id));
        return toDTO(entity);
    }

    @Transactional
    public ReservaServicoResponseDTO insert(ReservaServicoRequestDTO dto) {
        ReservaServico entity = new ReservaServico();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public ReservaServicoResponseDTO update(Long id, ReservaServicoRequestDTO dto) {
        ReservaServico entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Relação reserva-serviço não encontrada. ID: " + id));
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return toDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Relação reserva-serviço não encontrada. ID: " + id);
        }
        repository.deleteById(id);
    }

    private ReservaServicoResponseDTO toDTO(ReservaServico entity) {
        return new ReservaServicoResponseDTO(
                entity.getCodReservaServico(),
                entity.getReserva().getCodReserva(),
                entity.getServicoExtra().getCodServicos(),
                entity.getQuantidade());
    }

    private void copyDtoToEntity(ReservaServicoRequestDTO dto, ReservaServico entity) {
        Reserva reserva = reservaRepository.findById(dto.getCodReservaFk())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Reserva não encontrada. ID: " + dto.getCodReservaFk()));

        ServicoExtra servicoExtra = servicoExtraRepository.findById(dto.getCodServicoFk())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Serviço Extra não encontrado. ID: " + dto.getCodServicoFk()));

        entity.setReserva(reserva);
        entity.setServicoExtra(servicoExtra);
        entity.setQuantidade(dto.getQuantidade());
    }

}
