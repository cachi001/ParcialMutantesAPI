package org.emiliano.parcialmutantes.controllers;

import org.emiliano.parcialmutantes.domain.dto.StatsDto;
import org.emiliano.parcialmutantes.services.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController // Se indica que esta clase es un controlador REST
@RequestMapping("/stats") // Define la ruta base para las solicitudes
public class StatsController {
    private final StatsService statsService; // Declara el servicio que se inyectará en el constructor

    @Autowired // Permite que Spring inyecte la dependencia en el constructor
    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("") // Define el metodo que manejará las solicitudes GET a la ruta "/stats"
    public ResponseEntity<?> obtenerEstadisticas() {
        try {
            StatsDto statsDto = statsService.obtenerEstadisticas(); // Llama al servicio para obtener las estadísticas
            // Crea un mapa para la respuesta
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("codigo", HttpStatus.OK.value() + " (OK)"); // Código de estado 200
            respuesta.put("estadisticas", statsDto); // Agrega las estadísticas al mapa de respuesta

            return ResponseEntity.ok(respuesta); // Retorna la respuesta con estado 200 OK
        } catch (Exception e) {
            // Crea un mapa para la respuesta de error
            Map<String, Object> error = new HashMap<>();
            error.put("codigo", HttpStatus.INTERNAL_SERVER_ERROR.value() + " (INTERNAL SERVER ERROR)");
            error.put("mensaje", "Error inesperado en el servidor");

            // Retorna una respuesta con estado 500 INTERNAL SERVER ERROR
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON) // Especifica el tipo de contenido como JSON
                    .body(error); // Devuelve el cuerpo de la respuesta con el error
        }
    }
}
