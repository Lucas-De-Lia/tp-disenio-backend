# Trabajo Práctico de Diseño de Sistemas (Backend)

## Introducción

Este proyecto corresponde al backend del Trabajo Práctico de la materia de Diseño de Sistemas de la UTN. La aplicación está desarrollada utilizando Spring Boot y proporciona una API REST para gestionar aulas, reservas y usuarios (administradores y bedeles). La base de datos utilizada es MySQL, y se incluyen scripts SQL para poblar la base de datos con datos de prueba.

## Instrucciones para Inicializar la Aplicación

Sigue estos pasos poner en marcha la aplicación:

1. **Clona el repositorio en tu máquina local**:
    ```bash
    git clone https://github.com/gonzalo-aguero/tp-disenio-backend.git
    cd tp-disenio-backend
    ```

2. **Configura la Base de Datos**

    Asegúrate de tener MySQL instalado y en ejecución. Crea una base de datos para la aplicación:
    ```sql
    CREATE DATABASE tpdisenio;
    ```

    Actualiza el archivo [application.properties](application.properties) con las credenciales de tu base de datos o deja los valores predeterminados:
    ```properties
    # Configuración de la base de datos MySQL
    spring.datasource.url=jdbc:mysql://localhost:3306/tpdisenio
    spring.datasource.username=root
    spring.datasource.password=
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    ```

3. **Compila el Proyecto**

    Compila el proyecto para asegurarte de que todos los archivos se compilen correctamente.

    ##### Con Maven:
    ```bash
    ./mvnw clean install
    ```

4. **Ejecuta la Aplicación**

    Una vez que el proyecto esté compilado, puedes ejecutar la aplicación.

    ##### Con Maven:
    ```bash
    ./mvnw spring-boot:run
    ```

5. **Poblar la Base de Datos con Datos de Prueba**

    El proyecto incluye un script SQL (`datos_prueba.sql`) que se ejecutará automáticamente al iniciar la aplicación para poblar la base de datos con datos de prueba. Asegúrate de que el archivo `datos_prueba.sql` esté en el directorio [resources](./src/main/resources/).

6. **Verifica la Ejecución**

    Una vez que la aplicación esté en ejecución, verifica la consola para asegurarte de que no haya errores y que la aplicación se haya iniciado correctamente. La API REST estará disponible en `http://localhost:8080`.

