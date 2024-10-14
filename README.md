# 🧬 API DETECCION DE MUTANTES

## 📝 Explicacion
Esta API permite detectar si un humano es mutante basándose en su secuencia de ADN. Implementa funcionalidades para validar secuencias de ADN y obtener estadísticas sobre los análisis realizados.

## 🛠️ Tecnologías Utilizadas
- Spring Boot
- Java 17
- Gradle
- Hibernate
- Open API
- H2 Database
- Render
- JUnit
  
## 🌐 URL de Despliegue en RENDER
- **URL:** [https://parcialmutantesapi.onrender.com](https://parcialmutantesapi.onrender.com)

## 📋 Funcionalidades
- Validación de secuencias de ADN para determinar si son mutantes o no.
- Obtención de estadísticas sobre las solicitudes de análisis de ADN, incluyendo la cantidad de mutantes y humanos analizados.
- Manejo de errores y validaciones adecuadas en las solicitudes.
- Persistencia de los mutantes y humanos en la base de datos.

## 🔧 Instrucciones de Ejecución
Para instalar y ejecutar el proyecto localmente:
  1. Clonar el repositorio de Git:
     ```bash
     git clone https://github.com/cachi001/ParcialMutantesAPI.git
     cd ParcialMutantesAPI
     ```
  2. Construir el proyecto:
      ```bash
      ./gradlew build
      ```
  3. Ejecutar la aplicación:
      ```bash
      ./gradlew bootRun
      ```
  4. Acceder a la API localmente: El puerto es el 8080 especificado en el archivo `application.properties`.
     - Puedes enviar requests a través de Postman.
       - **POST:** `http://localhost:8080/mutant`
       - **GET:** `http://localhost:8080/stats`
     - Puedes acceder a la base de datos H2 en `http://localhost:8080/h2-console/`
       - **URL de JDBC:** `jdbc:h2:mem:testdb`
       - **Usuario:** `sa`
       - **Contraseña:** ` ` **(vacia)**

## 🌐 Acceso a la API Hosteada
### 📋 Realizar Solicitudes
1. Se pueden enviar solicitudes a la API usando Postman:

  - **Ejemplo Request para verificar un ADN:**
    - **Endpoint:** `/mutant`
    - **Método:** POST
    - **URL:** `https://parcialmutantesapi.onrender.com/mutant`
    - **Ejemplo de Cuerpo (JSON):**
      ```json
      {
        "dna": ["CGGTG", "AAAA", "TTTT", "GGTC"]
      }
      ```
  
  - **Ejemplo Request para obtener las estadísticas:**
    - **Endpoint:** `/stats`
    - **Método:** GET
    - **URL:** `https://parcialmutantesapi.onrender.com/stats`

2. Se puede acceder a la base de datos en H2 de render aca `https://parcialmutantesapi.onrender.com/h2-console`
   - **Configuracion para acceder:**
       - **URL de JDBC:** `jdbc:h2:mem:testdb`
       - **Usuario:** `sa`
       - **Contraseña:** ` ` **(vacia)**
   
### 📊 Ejemplos de Respuestas

  #### 1. Comprobar ADN
  - **Endpoint**: `/mutant`
  - **Método**: `POST`
  - **Descripción**: Este endpoint recibe un ADN en formato JSON y verifica si pertenece a un mutante.
  - **Request Body**:
      ```json
      {
          "dna": [
              "ATGCGA",
              "CAGTGC",
              "TTTTGT",
              "AGAAGG",
              "CCCCTA",
              "TCACTG"
          ]
      }
      ```
  - **Respuestas**:
      - **200 OK**: El ADN es de un mutante.
          ```json
          {
              "codigo": "200 (OK)",
              "resultado": "Mutante"
          }
          ```
      - **403 FORBIDDEN**: El ADN no es de un mutante.
          ```json
          {
              "codigo": "403 (FORBIDDEN)",
              "resultado": "No mutante"
          }
          ```
      - **400 BAD REQUEST**: La secuencia es inválida (solo se permiten caracteres A, C, G y T).
          ```json
          {
              "codigo": "400 (BAD REQUEST)",
              "mensaje": "La secuencia es inválida, solo puede contener caracteres A, C y G."
          }
          ```
      - **500 INTERNAL SERVER ERROR**: Error inesperado en el servidor.
          ```json
          {
              "codigo": "500 (INTERNAL SERVER ERROR)",
              "mensaje": "Ocurrió un error interno en el servidor"
          }
          ```
  
  #### 2. Obtener estadísticas
  
  - **Endpoint**: `/stats`
  - **Método**: `GET`
  - **Descripción**: Este endpoint devuelve estadísticas sobre el ADN mutante y humano.
  - **Respuestas**:
      - **200 OK**: Se devuelve el conteo de ADN mutante, humano y la razón.
          ```json
          {
              "codigo": "200 (OK)",
              "estadisticas": {
                  "count_mutant_dna": 10,
                  "count_human_dna": 5,
                  "ratio": 2.0
              }
          }
          ```
      - **500 INTERNAL SERVER ERROR**: Error inesperado en el servidor.
          ```json
          {
              "codigo": "500 (INTERNAL SERVER ERROR)",
              "mensaje": "Error inesperado en el servidor"
          }
          ```

### ⚠️ Consideraciones Adicionales
- Asegúrate de enviar la secuencia de ADN en el formato correcto, ya que la validación se basa en la estructura de la matriz de ADN.
- La base de datos H2 se utiliza para almacenar los resultados de las solicitudes de ADN y se reinicia cada vez que se inicia la aplicación localmente.

## ⚗️ Pruebas
### Pruebas Unitarias
La API cuenta con pruebas unitarias que se pueden ejecutar para verificar el funcionamiento de la API sin necesidad de iniciar el servidor.

#### Pruebas de Servicios
- **Ubicación:** `src/test/java/org/emiliano/parcialmutantes/services`
- Esta clase se encarga de validar el manejo de errores en los servicios del ADN y las Stats.

#### Pruebas de Controladores
- **Ubicación:** `src/test/java/org/emiliano/parcialmutantes/controllers`
- Esta clase realiza las pruebas en los controladores de ADN y Stats para asegurarse de que las respuestas HTTP sean las esperadas.

### Ejecución de Pruebas
- Las pruebas se pueden ejecutar con el siguiente comando:
  ```bash
  ./gradlew test
  ```
## 📈 Diagrama de Secuencia
Se incluye un diagrama de secuencia sobre la arquitectura de la api

[Diagrama de Secuencia](docs/diagrama_de_secuencia.pdf)

![DiagramaDeSecuencia](https://github.com/user-attachments/assets/f853eeb4-5c20-4ce3-9076-f555f5c446fb)




