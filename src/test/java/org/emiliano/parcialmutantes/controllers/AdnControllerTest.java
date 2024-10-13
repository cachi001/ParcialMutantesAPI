package org.emiliano.parcialmutantes.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.emiliano.parcialmutantes.services.AdnService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class AdnControllerTest {

    @InjectMocks // Inyecta los mocks en el objeto del controlador
    private AdnController adnController; // Controlador a probar

    @Mock // Indica que este es un objeto simulado
    private AdnService adnService; // Servicio simulado

    private MockMvc mockMvc; // Objeto MockMvc para realizar las pruebas

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
        mockMvc = MockMvcBuilders.standaloneSetup(adnController).build(); // Configura MockMvc con el controlador
    }

    @Test
    public void testEsMutante() throws Exception {
        // Simular los datos que devolverá el servicio
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTTTGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        when(adnService.isMutant(dna)).thenReturn(true); // Simula que el servicio devuelve verdadero

        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(post("/mutants") // Realiza una solicitud POST a /mutants
                        .contentType(MediaType.APPLICATION_JSON) // Especifica el tipo de contenido
                        .content("{\"dna\":[\"ATGCGA\",\"CAGTGC\",\"TTTTGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]}")) // Contenido de la solicitud
                .andExpect(status().isOk()) // Verifica que el estado de la respuesta sea 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica el tipo de contenido de la respuesta
                .andExpect(jsonPath("$.codigo").value("200 (OK)")) // Verifica el código en la respuesta
                .andExpect(jsonPath("$.resultado").value("Mutante")); // Verifica el resultado en la respuesta
    }

    @Test
    public void testNoEsMutante() throws Exception {
        // Simular los datos que devolverá el servicio
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTCTGT",
                "AGAAAG",
                "CCCTCA",
                "TCACTG"
        };
        when(adnService.isMutant(dna)).thenReturn(false); // Simula que el servicio devuelve falso

        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(post("/mutants") // Realiza una solicitud POST a /mutants
                        .contentType(MediaType.APPLICATION_JSON) // Especifica el tipo de contenido
                        .content("{\"dna\":[\"ATGCGA\",\"CAGTGC\",\"TTCTGT\",\"AGAAAG\",\"CCCTCA\",\"TCACTG\"]}")) // Contenido de la solicitud
                .andExpect(status().isForbidden()) // Verifica que el estado de la respuesta sea 403 Forbidden
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica el tipo de contenido de la respuesta
                .andExpect(jsonPath("$.codigo").value("403 (FORBIDDEN)")) // Verifica el código en la respuesta
                .andExpect(jsonPath("$.resultado").value("No mutante")); // Verifica el resultado en la respuesta
    }

    @Test
    public void testSecuenciaInvalida() throws Exception {
        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(post("/mutants") // Realiza una solicitud POST a /mutants
                        .contentType(MediaType.APPLICATION_JSON) // Especifica el tipo de contenido
                        .content("{\"dna\":[\"ATGCGA\",\"CAGTGC\",\"TTCTGT\",\"AGAAAG\",\"CCCTCZ\",\"TCACTG\"]}")) // Contenido de la solicitud con una secuencia inválida
                .andExpect(status().isBadRequest()) // Verifica que el estado de la respuesta sea 400 Bad Request
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica el tipo de contenido de la respuesta
                .andExpect(jsonPath("$.codigo").value("400 (BAD REQUEST)")) // Verifica el código en la respuesta
                .andExpect(jsonPath("$.mensaje").value("La secuencia es inválida, solo puede contener caracteres A, C, G y T")); // Verifica el mensaje de error en la respuesta
    }

    @Test
    public void testEsMutanteConError() throws Exception {
        // Simular un error en el servicio
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        when(adnService.isMutant(dna)).thenThrow(new RuntimeException("Error inesperado")); // Simula que el servicio lanza una excepción

        // Realizar la solicitud y verificar la respuesta
        mockMvc.perform(post("/mutants") // Realiza una solicitud POST a /mutants
                        .contentType(MediaType.APPLICATION_JSON) // Especifica el tipo de contenido
                        .content("{\"dna\":[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]}")) // Contenido de la solicitud
                .andExpect(status().isInternalServerError()) // Verifica que el estado de la respuesta sea 500 Internal Server Error
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica el tipo de contenido de la respuesta
                .andExpect(jsonPath("$.codigo").value("500 (INTERNAL SERVER ERROR)")) // Verifica el código en la respuesta
                .andExpect(jsonPath("$.mensaje").value("Ocurrió un error interno en el servidor")); // Verifica el mensaje de error en la respuesta
    }
}
