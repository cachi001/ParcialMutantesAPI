package org.emiliano.parcialmutantes.repositories;

import org.emiliano.parcialmutantes.domain.entities.Adn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdnRepository extends JpaRepository<Adn,Long> {
    int countByResultado(boolean resultado); // Metodo para contar ADNs según su estado mutante
}
