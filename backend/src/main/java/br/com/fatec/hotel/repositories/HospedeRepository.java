package br.com.fatec.hotel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fatec.hotel.models.Hospede;

@Repository
public interface HospedeRepository extends JpaRepository<Hospede, Long> {
}
