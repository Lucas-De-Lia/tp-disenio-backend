package com.tpdisenio.gSM3C.dto;

import java.sql.Time;
import java.time.LocalDate;

import lombok.Data;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO para la reserva de un día específico de acuerdo a la fecha (reservas esporádicas)
 */
@Data
public class ReservaDiaDTO {
    @NotNull(message = "La fecha del día a reservar es obligatoria.")
    @Future(message = "La fecha del día a reservar debe ser en el futuro.")
    private LocalDate fecha;
    
    @NotNull(message = "La hora de inicio de la reserva es obligatoria.")
    private Time horaInicio;
    
    @NotNull(message = "La duración de la reserva es obligatoria.")
    @Positive(message = "La duración de la reserva debe ser mayor a cero.")
    private Integer duracion; // Duracion en minutos
    
    @NotNull(message = "El número de aula es obligatorio.")
    private Integer nroAula;
}
