package org.emiliano.parcialmutantes.services;

import org.emiliano.parcialmutantes.domain.entities.Adn;
import org.emiliano.parcialmutantes.repositories.AdnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service // Se indica que esta clase es un servicio
public class AdnService {
    private final AdnRepository adnRepository; // Declara el repositorio para el acceso a datos

    @Autowired // Permite la inyección automática del repositorio en el constructor
    public AdnService(AdnRepository adnRepository) {
        this.adnRepository = adnRepository; // Inicializa el repositorio
    }

    //Metodo para comprobar si el ADN es mutante o no
    public boolean isMutant(String[] dna) {
        int tam = dna.length;
        String dnaM = String.join(",", dna);  // Convertimos el ADN una sola vez
        int secuenciaMutante = 0;  // Contador para secuencias mutantes

        // FILA
        secuenciaMutante += contadorSecuenciasFilas(dna, tam);

        // COLUMNA
        secuenciaMutante += contadorSecuenciasColumnas(dna, tam);

        // DIAGONALES
        secuenciaMutante += contadorSecuenciasDiagonales(dna, tam);

        // Si hay al menos 2 secuencias mutantes
        if (secuenciaMutante >= 2) {
            // Si es mutante se guarda como mutante
            Adn adn = new Adn(null, dnaM, true);
            adnRepository.save(adn);
            return true;
        }

        // Si no es mutante se guarda como no mutante
        Adn adn = new Adn(null, dnaM, false);
        adnRepository.save(adn);
        return false;
    }

    // Metodo para contar secuencias mutantes en las filas
    public int contadorSecuenciasFilas(String[] dna, int tam) {
        int count = 0;
        for (int i = 0; i < tam; i++) {
            if (comprobarSecuencia(dna[i])) {
                count++;
            }
        }
        return count;
    }

    // Metodo para contar secuencias mutantes en las columnas
    public int contadorSecuenciasColumnas(String[] dna, int tam) {
        int count = 0;
        for (int j = 0; j < tam; j++) {
            StringBuilder columna = new StringBuilder();
            for (int i = 0; i < tam; i++) {
                columna.append(dna[i].charAt(j));
            }
            if (comprobarSecuencia(columna.toString())) {
                count++;
            }
        }
        return count;
    }

    // Metodo para contar secuencias mutantes en las diagonales
    public int contadorSecuenciasDiagonales(String[] dna, int tam) {
        int count = 0;

        // DIAGONALES PRINCIPALES (de arriba izquierda a abajo derecha)
        // Diagonales comenzando desde la primera fila
        for (int i = 0; i <= tam - 4; i++) {
            StringBuilder diagonal = new StringBuilder();
            for (int j = 0; j < tam - i; j++) {
                diagonal.append(dna[i + j].charAt(j));
            }
            if (comprobarSecuencia(diagonal.toString())) count++;
        }

        // Diagonales comenzando desde la primera columna (excepto la diagonal principal ya calculada)
        for (int i = 1; i <= tam - 4; i++) {
            StringBuilder diagonal = new StringBuilder();
            for (int j = 0; j < tam - i; j++) {
                diagonal.append(dna[j].charAt(i + j));
            }
            if (comprobarSecuencia(diagonal.toString())) count++;
        }

        // DIAGONALES INVERSAS (de arriba derecha a abajo izquierda)
        // Diagonales comenzando desde la primera fila
        for (int i = 0; i <= tam - 4; i++) {
            StringBuilder diagonalInv = new StringBuilder();
            for (int j = 0; j < tam - i; j++) {
                diagonalInv.append(dna[i + j].charAt(tam - 1 - j));
            }
            if (comprobarSecuencia(diagonalInv.toString())) count++;
        }

        // Diagonales comenzando desde la última columna (excepto la diagonal inversa principal ya calculada)
        for (int i = 1; i <= tam - 4; i++) {
            StringBuilder diagonalInv = new StringBuilder();
            for (int j = 0; j < tam - i; j++) {
                diagonalInv.append(dna[j].charAt(tam - 1 - (i + j)));
            }
            if (comprobarSecuencia(diagonalInv.toString())) count++;
        }

        return count;
    }

    // Metodo para comprobar si una secuencia es mutante
    public static boolean comprobarSecuencia(String secuencia) {
        return secuencia.matches(".*(AAAA|TTTT|CCCC|GGGG).*");
    }
}
