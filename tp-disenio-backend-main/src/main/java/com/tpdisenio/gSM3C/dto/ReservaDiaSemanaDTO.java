package com.tpdisenio.gSM3C.dto;

import java.sql.Time;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * DTO para la reserva de un día específico de la semana (reservas por periodo)
 */
@Data
public class ReservaDiaSemanaDTO {
    @NotBlank(message = "El día de la semana a reservar es obligatorio.")
    private String diaSemana; // LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO
    
    @NotNull(message = "La hora de inicio de la reserva es obligatoria.")
    private Time horaInicio;
    
    @NotNull(message = "La duración de la reserva es obligatoria.")
    @Positive(message = "La duración de la reserva debe ser mayor a cero.")
    private Integer duracion; // Duracion en minutos
    
    @NotNull(message = "El número de aula es obligatorio.")
    private Integer nroAula;
}
