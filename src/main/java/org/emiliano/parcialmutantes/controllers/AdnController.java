package org.emiliano.parcialmutantes.controllers;

import jakarta.validation.Valid;
import org.emiliano.parcialmutantes.domain.dto.AdnDto;
import org.emiliano.parcialmutantes.services.AdnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController // Se indica que esta clase es un controlador REST
@RequestMapping("/mutants") // Define la ruta base para las solicitudes
public class AdnController {
    private final AdnService adnService; // Declara el servicio que se inyectará en el constructor

    @Autowired // Permite que Spring inyecte la dependencia en el constructor
    public AdnController(AdnService adnService) {
        this.adnService = adnService;
    }

    @PostMapping("") // Define el metodo que manejará las solicitudes POST a la ruta "/mutants"
    public ResponseEntity<?> validationMutant(@Valid @RequestBody AdnDto adn) {
        try {
            // Verifica si la secuencia de ADN es válida
            if (!adn.isValidDna()) {
                // Crea un mapa para la respuesta de error
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("codigo", HttpStatus.BAD_REQUEST.value() + " (BAD REQUEST)");
                errorResponse.put("mensaje", "La secuencia es inválida, solo puede contener caracteres A, C, G y T");

                // Retorna una respuesta 400 BAD REQUEST con el contenido en JSON
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON) // Especifica el tipo de contenido como JSON
                        .body(errorResponse); // Devuelve el cuerpo de la respuesta
            }

            // Llama al servicio para determinar si la secuencia es mutante
            boolean resultado = adnService.isMutant(adn.getDna());

            // Crea un mapa para la respuesta que contiene el resultado
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("codigo", resultado ? HttpStatus.OK.value() + " (OK)" : HttpStatus.FORBIDDEN.value() + " (FORBIDDEN)"); // Código de estado según el resultado
            respuesta.put("resultado", resultado ? "Mutante" : "No mutante"); // Mensaje de resultado

            // Retorna la respuesta correspondiente con el estado adecuado
            return ResponseEntity.status(resultado ? HttpStatus.OK : HttpStatus.FORBIDDEN) // Establece el código de estado
                    .contentType(MediaType.APPLICATION_JSON) // Especifica el tipo de contenido como JSON
                    .body(respuesta); // Devuelve el cuerpo de la respuesta

        } catch (Exception e) {
            // Si ocurre un error, retorna un mensaje de error en JSON
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("codigo", HttpStatus.INTERNAL_SERVER_ERROR.value() + " (INTERNAL SERVER ERROR)");
            errorResponse.put("mensaje", "Ocurrió un error interno en el servidor");

            // Retorna una respuesta 500 INTERNAL SERVER ERROR con el contenido en JSON
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // Establece el código de estado
                    .contentType(MediaType.APPLICATION_JSON) // Especifica el tipo de contenido como JSON
                    .body(errorResponse); // Devuelve el cuerpo de la respuesta
        }
    }
}
