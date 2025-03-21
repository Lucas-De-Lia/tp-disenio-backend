package com.tpdisenio.gSM3C.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.*;

import com.tpdisenio.gSM3C.domain.Reserva;
import com.tpdisenio.gSM3C.dto.ReservaDTO;
import com.tpdisenio.gSM3C.services.ReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

/**
 * Reserva Controller
 */
@RestController
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    // Crear nueva reserva
    @PostMapping
    @Operation(summary = "Crear reserva")
    public ResponseEntity<?> postReserva(@Valid @RequestBody ReservaDTO reservaDTO) {
        Reserva createdReserva = reservaService.registrarReserva(reservaDTO);

        // Creamos la uri para consumir la reserva creada
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{idReserva}")
                .buildAndExpand(createdReserva.getIdReserva())
                .toUri();

        return ResponseEntity.created(location).body(createdReserva);
    }

    // Obtener reservas por fecha
    @GetMapping("/search-by-fecha")
    @Operation(summary = "Buscar reservas por fecha")
    public ResponseEntity<List<Reserva>> getReservasByFecha(
        @Parameter(description = "Fecha en formato yyyy-MM-dd", example = "2024-01-21")
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fecha, 
        @RequestParam(required = false) String tipoAula, 
        @RequestParam(required = false) Integer numAula) {
        List<Reserva> reservas = reservaService.listarReservaPorDia(fecha, tipoAula, numAula);
        return ResponseEntity.ok(reservas);
    }

    // Obtener reservas por curso
    @GetMapping("/search-by-curso")
    @Operation(summary = "Buscar reservas por curso")
    public ResponseEntity<List<Reserva>> getReservasByCurso(@RequestParam(required = false) String curso) {
        List<Reserva> reservas = reservaService.listarReservaPorCurso(curso);
        return ResponseEntity.ok(reservas);
    }
}