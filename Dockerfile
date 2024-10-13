# Etapa de construcción
FROM alpine:latest AS build

# Actualizar el sistema y agregar OpenJDK
RUN apk update && apk add openjdk17 gradle

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar los archivos del proyecto al contenedor
COPY . .

# Dar permisos de ejecución al script de Gradle
RUN chmod +x ./gradlew

# Ejecutar el comando para construir el JAR
RUN ./gradlew bootJar --no-daemon

# Etapa de ejecución
FROM openjdk:17-alpine

# Exponer el puerto que la aplicación utilizará
EXPOSE 8080

# Copiar el archivo JAR generado desde la etapa de construcción
COPY --from=build /app/build/libs/ProyectoParcialMutantes-0.0.1-SNAPSHOT.jar ./api.jar

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "api.jar"]
