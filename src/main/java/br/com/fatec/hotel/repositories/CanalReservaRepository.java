package br.com.fatec.hotel.repositories;

import br.com.fatec.hotel.models.CanalReserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CanalReservaRepository extends JpaRepository<CanalReserva, Long> {
    
}
