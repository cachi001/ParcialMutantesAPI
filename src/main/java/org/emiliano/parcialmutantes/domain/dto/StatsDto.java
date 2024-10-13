package org.emiliano.parcialmutantes.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsDto {
    private int count_mutant_dna; //Contador para la cantidad de mutantes
    private int count_human_dna; //Contador para la cantidad de humanos
    private double ratio; //Promedio para la cantidad de mutantes
}
