package br.com.fatec.hotel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.fatec.hotel.models.ServicoExtra;

@Repository
public interface ServicoExtraRepository extends JpaRepository<ServicoExtra, Long> {
    
}
