package org.emiliano.parcialmutantes.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.emiliano.parcialmutantes.domain.dto.StatsDto;
import org.emiliano.parcialmutantes.repositories.AdnRepository;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.dao.DataAccessException;

public class StatsServiceTest {

    @InjectMocks
    private StatsService statsService; // Inyecta las dependencias necesarias en el servicio

    @Mock
    private AdnRepository adnRepository; // Mock del repositorio de ADN

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks antes de cada prueba
    }

    @Test
    public void testObtenerEstadisticasConDatos() {
        // Simulamos que hay un ADN mutante y uno humano en el repositorio
        when(adnRepository.countByResultado(true)).thenReturn(40);
        when(adnRepository.countByResultado(false)).thenReturn(100);

        // Llamamos al metodo que estamos probando
        StatsDto result = statsService.obtenerEstadisticas();

        // Verificamos que los conteos de ADN sean correctos
        assertEquals(40, result.getCount_mutant_dna()); // Verifica el conteo de ADN mutante
        assertEquals(100, result.getCount_human_dna()); // Verifica el conteo de ADN humano
        assertEquals(0.4, result.getRatio()); // Verifica que el ratio sea correcto
    }

    @Test
    public void testObtenerEstadisticasSinMutantes() {
        // Simulamos que no hay ADN mutantes y que hay ADN humanos
        when(adnRepository.countByResultado(true)).thenReturn(0);
        when(adnRepository.countByResultado(false)).thenReturn(5);

        // Llamamos al metodo que estamos probando
        StatsDto result = statsService.obtenerEstadisticas();

        // Verificamos que los conteos sean correctos
        assertEquals(0, result.getCount_mutant_dna()); // Verifica que no haya ADN mutantes
        assertEquals(5, result.getCount_human_dna()); // Verifica que haya ADN humanos
        assertEquals(0.0, result.getRatio()); // Verifica que el ratio sea cero
    }

    @Test
    public void testObtenerEstadisticasSinHumanos() {
        // Simulamos que hay ADN mutantes pero no humanos
        when(adnRepository.countByResultado(true)).thenReturn(10);
        when(adnRepository.countByResultado(false)).thenReturn(0);

        // Llamamos al metodo que estamos probando
        StatsDto result = statsService.obtenerEstadisticas();

        // Verificamos que los conteos sean correctos
        assertEquals(10, result.getCount_mutant_dna()); // Verifica que haya 10 ADN mutantes
        assertEquals(0, result.getCount_human_dna()); // Verifica que no haya ADN humanos
        assertTrue(Double.isInfinite(result.getRatio())); // Verifica que el ratio sea infinito
    }

    @Test
    public void testObtenerEstadisticasSinDatos() {
        // Simulamos que no hay datos en el repositorio
        when(adnRepository.countByResultado(true)).thenReturn(0);
        when(adnRepository.countByResultado(false)).thenReturn(0);

        // Llamamos al metodo que estamos probando
        StatsDto result = statsService.obtenerEstadisticas();

        // Verificamos que los conteos sean cero
        assertEquals(0, result.getCount_mutant_dna()); // Verifica que no haya ADN mutantes
        assertEquals(0, result.getCount_human_dna()); // Verifica que no haya ADN humanos
        assertEquals(0.0, result.getRatio()); // Verifica que el ratio sea cero
    }

    @Test
    public void testObtenerEstadisticasConErrorDeBaseDeDatos() {
        // Simulamos que ocurre un error en la base de datos al contar los ADN mutantes
        when(adnRepository.countByResultado(true)).thenThrow(new DataAccessException("Error de acceso"){});

        // Verificamos que se lance la excepción esperada
        assertThrows(ServiceException.class, () -> {
            statsService.obtenerEstadisticas(); // Llamamos al metodo que estamos probando
        });
    }

    @Test
    public void testObtenerEstadisticasConErrorInesperado() {
        // Simulamos que ocurre un error inesperado al contar los ADN mutantes
        when(adnRepository.countByResultado(true)).thenThrow(new RuntimeException("Error inesperado"));

        // Verificamos que se lance la excepción esperada
        assertThrows(ServiceException.class, () -> {
            statsService.obtenerEstadisticas(); // Llamamos al metodo que estamos probando
        });
    }
}
