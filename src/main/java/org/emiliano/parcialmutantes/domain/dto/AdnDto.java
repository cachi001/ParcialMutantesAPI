package org.emiliano.parcialmutantes.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AdnDto {
    @Valid // Indica que la validación debe aplicarse al campo 'dna'
    @NotNull(
            message = "Debe haber al menos una secuencia de ADN"
    )
    private String [] dna; // Array que almacena las secuencias de ADN

    //Metodo para VALIDAR la secuencia
    public boolean isValidDna() {
        for (String secuencia : dna) {
            if (!secuencia.matches("^[ACGT]+$")) { // Asegura que solo contenga A, C, G, T
                return false; // Retorna falso si alguna secuencia es inválida
            }
        }
        return true; // Si todas las secuencias son válidas
    }
}
