package com.tpdisenio.gSM3C.dto;

import java.sql.Time;
import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import lombok.Data;
/**
 * BloqueHorarioDTO representa un bloque de horario para la reserva de aulas.
 * 
 * Contiene la fecha, hora de inicio y duración de la reserva, así como información adicional
 * sobre la disponibilidad de aulas y posibles reservas solapadas.
 **/
@Data
public class BloqueHorarioDTO {
    private LocalDate fecha;

    private String diaSemana; // En caso de ser reserva por periodo
    
    @NotNull(message = "La hora de inicio de la reserva es obligatoria.")
    private Time horaInicio;
    
    @NotNull(message = "La duración de la reserva es obligatoria.")
    @Positive(message = "La duración de la reserva debe ser mayor a cero.")
    @Max(value = 300, message = "La duración de la reserva no puede ser mayor a 5 horas.")
    private Integer duracion; // Duracion en minutos

    /**
     * Atributos adicionales para la validación de la disponibilidad de aulas.
     * Se devuelven como respuesta a la solicitud de disponibilidad de aulas.
     */
    private Boolean disponible; // Indica si hay aulas disponibles para el día y horario solicitado

    private List<AulaResponseDTO> aulasDisponibles; // Lista de aulas disponibles (en caso de haber)
 
    private List<ReservaSolapamientoDTO> reservasSolapadas; // Mapa de aulas con sus reservas solapadas (en caso de haber)

}
