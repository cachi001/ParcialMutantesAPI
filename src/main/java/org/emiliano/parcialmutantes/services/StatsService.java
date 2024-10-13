package org.emiliano.parcialmutantes.services;

import org.emiliano.parcialmutantes.domain.dto.StatsDto;
import org.emiliano.parcialmutantes.repositories.AdnRepository;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service // Se indica que esta clase es un servicio
public class StatsService {
    private final AdnRepository adnRepository; // Declara el repositorio para el acceso a datos

    @Autowired // Permite la inyección automática del repositorio en el constructor
    public StatsService(AdnRepository adnRepository) {
        this.adnRepository = adnRepository; // Inicializa el repositorio
    }

    // Metodo para obtener las estadísticas y setearlas en el DTO
    public StatsDto obtenerEstadisticas() {
        try {
            int count_mutants_dna = adnRepository.countByResultado(true); // Cuenta los ADN mutantes
            int count_human_dna = adnRepository.countByResultado(false); // Cuenta los ADN humanos

            // Calcular el ratio
            double ratio = (count_human_dna == 0) ? (count_mutants_dna > 0 ? Double.POSITIVE_INFINITY : 0) :
                    (double) count_mutants_dna / count_human_dna;

            // Crea y devuelve el objeto StatsDto con los resultados
            StatsDto statsDto = new StatsDto(count_mutants_dna, count_human_dna, ratio);

            return statsDto; // Retorna el objeto StatsDto
        } catch (DataAccessException e) {
            // Lanza una excepción de servicio en caso de error en el acceso a la base de datos
            throw new ServiceException("Error al acceder a la base de datos", e);
        } catch (Exception e) {
            // Lanza una excepción de servicio en caso de un error inesperado
            throw new ServiceException("Error inesperado", e);
        }
    }
}
