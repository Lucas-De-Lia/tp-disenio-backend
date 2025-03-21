package com.tpdisenio.gSM3C.dto;

import java.sql.Time;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaSolapamientoDTO {
    private AulaResponseDTO aula;
    private ReservaResponseDTO reserva;
    private LocalDate fecha;
    private Time horaInicio;
    private Integer duracion; // Duracion en minutos
    private Double tiempoSolapamiento; // en horas
}