package br.com.fatec.hotel.repositories;

import br.com.fatec.hotel.models.TipoQuarto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoQuartoRepository extends JpaRepository<TipoQuarto, Long> {
    
}
