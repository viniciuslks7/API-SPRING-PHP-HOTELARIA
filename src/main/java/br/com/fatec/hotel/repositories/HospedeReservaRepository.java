package br.com.fatec.hotel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fatec.hotel.models.HospedeReserva;

public interface HospedeReservaRepository extends JpaRepository<HospedeReserva, Long> {
}
