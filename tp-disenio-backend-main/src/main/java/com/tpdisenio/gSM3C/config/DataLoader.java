package com.tpdisenio.gSM3C.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.tpdisenio.gSM3C.dao.BedelDao;
import com.tpdisenio.gSM3C.domain.Administrador;
import com.tpdisenio.gSM3C.domain.Bedel;
import com.tpdisenio.gSM3C.enums.TurnoTrabajo;
import com.tpdisenio.gSM3C.exception.AdministradorAlreadyExistsException;
import com.tpdisenio.gSM3C.services.AdministradorService;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Component
public class DataLoader {

    private final BedelDao bedelDao;
    private final AdministradorService adminService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JdbcTemplate jdbcTemplate;

    public DataLoader(BedelDao bedelDao, AdministradorService adminService, JdbcTemplate jdbcTemplate) {
        this.bedelDao = bedelDao;
        this.adminService = adminService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    @Transactional
    public void cargarDatos() {
        cargarAdministradores();
        cargarBedeles();
        ejecutarScriptSQL();
    }

    private void cargarAdministradores() {
        Administrador admin1 = new Administrador("ADMIN", "El admin 😎", "Zapata", passwordEncoder.encode("Pass!22word!"), true);
        Administrador admin2 = new Administrador("otroAdmin", "Admin alternativo", "Zapata", passwordEncoder.encode("Pass!22word!"), true);
        try {
            adminService.registrarAdministrador(admin1);
            System.out.println("Administrador de prueba cargado exitosamente.");
        } catch (AdministradorAlreadyExistsException e) {
            System.out.println("Administrador de prueba no cargado: " + e.getMessage());
        }

        try {
            adminService.registrarAdministrador(admin2);
            System.out.println("Administrador alternativo cargado exitosamente.");
        } catch (AdministradorAlreadyExistsException e) {
            System.out.println("Administrador alternativo no cargado: " + e.getMessage());
        }
    }

    private void cargarBedeles() {
        Administrador admin = adminService.buscarAdministradorPorUsername("ADMIN");

        if (admin == null) {
            System.out.println("No se pudo recuperar el administrador creado.");
            return;
        }

        List<Bedel> bedeles = Arrays.asList(
            new Bedel("Juan123", "Juan", "Perez", passwordEncoder.encode("password1"), true, TurnoTrabajo.MANIANA, admin),
            new Bedel("Maria123", "Maria", "Gomez", passwordEncoder.encode("password2"), true, TurnoTrabajo.TARDE, admin),
            new Bedel("Carlos123", "Carlos", "Lopez", passwordEncoder.encode("password3"), true, TurnoTrabajo.NOCHE, admin),
            new Bedel("Ana123", "Ana", "Martinez", passwordEncoder.encode("password4"), true, TurnoTrabajo.MANIANA, admin)
        );

        bedelDao.cargarBedeles(bedeles);
        
    }

    private void ejecutarScriptSQL() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("datos_prueba.sql");
            if (inputStream == null) {
                throw new RuntimeException("No se pudo encontrar el archivo datos_prueba.sql");
            }

            String sql;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                sql = reader.lines().collect(Collectors.joining("\n"));
            }

            String[] sqlStatements = sql.split(";");
            for (String statement : sqlStatements) {
                if (!statement.trim().isEmpty()) {
                    jdbcTemplate.execute(statement);
                }
            }

            System.out.println("Script SQL ejecutado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al ejecutar el script SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}