package org.emiliano.parcialmutantes.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.emiliano.parcialmutantes.domain.entities.Adn;
import org.emiliano.parcialmutantes.repositories.AdnRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

public class AdnServiceTest {

    @Mock
    private AdnRepository adnRepository; // Mock del repositorio que se usará para almacenar ADN

    @InjectMocks
    private AdnService adnService; // Inyecta las dependencias necesarias en el servicio

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks antes de cada prueba
    }

    @Test
    public void testEsMutanteConSecuenciasEnFilas() {
        // Definimos un ADN mutante que tiene secuencias en las filas
        String[] dnaMutante = {
                "AAAAAA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"};

        // Llamamos al metodo que estamos probando
        boolean result = adnService.isMutant(dnaMutante);

        // Verificamos que el resultado sea verdadero, indicando que es un ADN mutante
        assertTrue(result);

        // Verificamos que el metodo save del repositorio fue llamado una vez
        verify(adnRepository, times(1)).save(any(Adn.class));
    }

    @Test
    public void testEsMutanteConSecuenciasEnColumnas() {
        // Definimos un ADN mutante que tiene secuencias en las columnas
        String[] dnaMutante = {
                "ATGCGA",
                "CAGTGC",
                "TTGTGT",
                "AGGAGG",
                "CCCTAA",
                "TCACTG"};

        // Llamamos al metodo que estamos probando
        boolean result = adnService.isMutant(dnaMutante);

        // Verificamos que el resultado sea verdadero
        assertTrue(result);

        // Verificamos que el metodo save del repositorio fue llamado una vez
        verify(adnRepository, times(1)).save(any(Adn.class));
    }

    @Test
    public void testEsMutanteConSecuenciasDiagonalesPrincipales() {
        // Definimos un ADN mutante que tiene secuencias en las diagonales principales
        String[] dnaMutante = {
                "ATGCGA",
                "TAGTTC",
                "TTATGT",
                "AGTAAG",
                "CCCTTA",
                "TCACTG"};

        // Llamamos al metodo que estamos probando
        boolean result = adnService.isMutant(dnaMutante);

        // Verificamos que el resultado sea verdadero
        assertTrue(result);

        // Verificamos que el metodo save del repositorio fue llamado una vez
        verify(adnRepository, times(1)).save(any(Adn.class));
    }

    @Test
    public void testEsMutanteConSecuenciasDiagonalesInversas() {
        // Definimos un ADN mutante que tiene secuencias en las diagonales inversas
        String[] dnaMutante = {
                "ATGCGA",
                "CAGTAC",
                "TTAACT",
                "AGACAG",
                "CCCTTA",
                "TCACTG"};

        // Llamamos al metodo que estamos probando
        boolean result = adnService.isMutant(dnaMutante);

        // Verificamos que el resultado sea verdadero
        assertTrue(result);

        // Verificamos que el metodo save del repositorio fue llamado una vez
        verify(adnRepository, times(1)).save(any(Adn.class));
    }

    @Test
    public void testNoEsMutanteSinSecuencias() {
        // Definimos un ADN humano que no tiene secuencias mutantes
        String[] dnaHumano = {
                "ATGCGT",
                "CAGTAC",
                "TTAGGT",
                "AGCAAA",
                "CACTTG",
                "TCACTG"};

        // Llamamos al metodo que estamos probando
        boolean result = adnService.isMutant(dnaHumano);

        // Verificamos que el resultado sea falso, indicando que no es un ADN mutante
        assertFalse(result);

        // Verificamos que el metodo save del repositorio fue llamado una vez
        verify(adnRepository, times(1)).save(any(Adn.class));
    }

    @Test
    public void testComprobarSecuenciaConSecuenciaMutante() {
        // Secuencia mutante para probar
        String secuenciaMutante = "AAAA";

        // Llamamos al metodo de comprobación
        boolean result = AdnService.comprobarSecuencia(secuenciaMutante);

        // Verificamos que el resultado sea verdadero
        assertTrue(result);
    }

    @Test
    public void testComprobarSecuenciaConSecuenciaHumana() {
        // Secuencia humana para probar
        String secuenciaHumana = "ATCG";

        // Llamamos al metodo de comprobación
        boolean result = AdnService.comprobarSecuencia(secuenciaHumana);

        // Verificamos que el resultado sea falso
        assertFalse(result);
    }
}
