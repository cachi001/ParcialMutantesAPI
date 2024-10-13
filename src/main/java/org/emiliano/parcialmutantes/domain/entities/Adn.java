package org.emiliano.parcialmutantes.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // Identificador único para cada entrada de ADN
    @Column(name = "dna")
    @Basic
    private String dna; // Cadena que representa el ADN
    @Column(name = "resultado")
    @Basic
    private Boolean resultado; // Indica si el ADN es mutante o no
}
