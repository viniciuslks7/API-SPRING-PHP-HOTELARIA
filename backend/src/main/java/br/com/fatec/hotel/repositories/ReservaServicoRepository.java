package br.com.fatec.hotel.repositories;

import br.com.fatec.hotel.models.ReservaServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaServicoRepository extends JpaRepository<ReservaServico, Long> {

}
