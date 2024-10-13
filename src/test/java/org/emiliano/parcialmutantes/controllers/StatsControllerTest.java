package org.emiliano.parcialmutantes.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.emiliano.parcialmutantes.domain.dto.StatsDto;
import org.emiliano.parcialmutantes.services.StatsService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class StatsControllerTest {

    @InjectMocks // Inyecta las dependencias necesarias en el controlador
    private StatsController statsController;

    @Mock // Crea un mock de la clase StatsService
    private StatsService statsService;

    private MockMvc mockMvc; // Objeto MockMvc para realizar pruebas de los endpoints

    @BeforeEach
    public void setUp() {
        // Inicializa los mocks antes de cada prueba
        MockitoAnnotations.openMocks(this);
        // Configura MockMvc con el controlador a probar
        mockMvc = MockMvcBuilders.standaloneSetup(statsController).build();
    }

    @Test
    public void testObtenerEstadisticas() throws Exception {
        // Simula los datos que devolverá el servicio
        StatsDto statsDto = new StatsDto(10, 5, 2.0);
        when(statsService.obtenerEstadisticas()).thenReturn(statsDto);

        // Realiza la solicitud GET al endpoint /stats y verifica la respuesta
        mockMvc.perform(get("/stats")
                        .contentType(MediaType.APPLICATION_JSON)) // Especifica el tipo de contenido
                .andExpect(status().isOk()) // Verifica que el estado de la respuesta sea 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // // Verifica el tipo de contenido de la respuesta
                .andExpect(jsonPath("$.codigo").value("200 (OK)"))  // Verifica el código de respuesta
                .andExpect(jsonPath("$.estadisticas.count_mutant_dna").value(10)) // Verifica el conteo de ADN mutante
                .andExpect(jsonPath("$.estadisticas.count_human_dna").value(5)) // Verifica el conteo de ADN humano
                .andExpect(jsonPath("$.estadisticas.ratio").value(2.0)); // Verifica la razón

        // Verifica que el metodo del servicio fue llamado una vez
        verify(statsService, times(1)).obtenerEstadisticas();
    }

    @Test
    public void testObtenerEstadisticasConError() throws Exception {
        // Simula un error en el servicio al obtener estadísticas
        when(statsService.obtenerEstadisticas()).thenThrow(new RuntimeException("Error inesperado en el servidor"));

        // Realiza la solicitud GET al endpoint /stats y verifica la respuesta
        mockMvc.perform(get("/stats")
                        .contentType(MediaType.APPLICATION_JSON)) // Especifica el tipo de contenido
                .andExpect(status().isInternalServerError()) // Verifica que el estado de la respuesta sea 500 Internal Server Error
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica el tipo de contenido de la respuesta
                .andExpect(jsonPath("$.codigo").value("500 (INTERNAL SERVER ERROR)"))  // Verifica el código de error
                .andExpect(jsonPath("$.mensaje").value("Error inesperado en el servidor")); // Verifica el mensaje de error
    }
}
