# Usar la imagen base de Amazon Corretto 17 con Alpine
FROM amazoncorretto:17-alpine-jdk

# Copiar los archivos del proyecto al contenedor
COPY . .

# Dar permisos de ejecución al Maven Wrapper
RUN chmod +x ./mvnw

# Ejecutar el comando Maven para limpiar y empaquetar el proyecto
RUN ./mvnw clean package

# Definir el punto de entrada para ejecutar la aplicación
ENTRYPOINT [ "java", "-jar", "./target/gSM3C-0.0.1-SNAPSHOT.jar" ]
