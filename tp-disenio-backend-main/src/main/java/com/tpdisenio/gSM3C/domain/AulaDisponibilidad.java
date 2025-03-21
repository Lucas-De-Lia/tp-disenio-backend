package com.tpdisenio.gSM3C.domain;

import java.sql.Time;


import com.tpdisenio.gSM3C.dto.BloqueHorarioDTO;
import java.time.LocalDate;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa el estado de disponibilidad de una 
 * aula para el tipo, fecha y capacidad especificados.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AulaDisponibilidad {
    
    private Integer nroAula;
    private Integer capacidad;

    // Los siguientes campos serán nulos para aquellas aulas que no estén reservadas
    private LocalDate fecha;
    private Time horaInicio;
    private Integer duracion;
    private String idReserva;

    public AulaDisponibilidad(Integer nroAula, Integer capacidad, String idReserva, LocalDate fecha, Time horaInicio, Integer duracion) {
        this.nroAula = nroAula;
        this.capacidad = capacidad;
        this.idReserva = idReserva;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.duracion = duracion;
    }

    @Override
    public String toString() {
        String newline = "\n";
        return "AulaDisponibilidad {" + newline +
                "    nroAula = " + nroAula + ", " + newline +
                "    capacidad = " + capacidad + ", " + newline +
                "    idReserva = '" + idReserva + newline +
                "    fecha = " + fecha + ", " + newline +
                "    horaInicio = " + horaInicio + ", " + newline +
                "    duracion = " + duracion + ", " + newline +
                '}';
    }

    public Boolean tieneReservaAsociada() {
        return idReserva != null;
    }

    public Boolean haySolapamiento(LocalDate fecha, Time horaInicio, Integer duracion) {
        if (this.fecha == null || !this.fecha.equals(fecha)) {
            return false;
        }
        if (this.horaInicio == null || this.duracion == null) {
            return false;
        }

        // Convertir horas a milisegundos para la comparación (recordar que la duracion es en minutos)
        long thisStart = this.horaInicio.getTime();
        long thisEnd = thisStart + this.duracion * 60000;

        long otherStart = horaInicio.getTime();
        long otherEnd = otherStart + duracion * 60000;

        return thisStart < otherEnd && otherStart < thisEnd;
    }

    /**
     * Calcula el solapamiento en horas entre la fecha, hora de inicio y duración asociados a este objeto
     * y los valores pasados por parámetro.
     *
     * @param fecha La fecha de la reserva a comparar.
     * @param horaInicio La hora de inicio de la reserva a comparar.
     * @param duracion La duración de la reserva a comparar.
     * @return La cantidad de horas de solapamiento. Si no hay solapamiento, retorna 0.0.
     */
    public Double calcularSolapamiento(LocalDate fecha, Time horaInicio, Integer duracion) {
        if (this.fecha == null || !this.fecha.equals(fecha)) {
            return 0.0;
        }
        if (this.horaInicio == null || this.duracion == null) {
            return 0.0;
        }

        long thisStart = this.horaInicio.getTime();
        long thisEnd = thisStart + this.duracion * 60000;

        Time otherHoraInicio = horaInicio;
        Integer otherDuracion = duracion;

        long otherStart = otherHoraInicio.getTime();
        long otherEnd = otherStart + otherDuracion * 60000;

        long overlapStart = Math.max(thisStart, otherStart);
        long overlapEnd = Math.min(thisEnd, otherEnd);

        if (overlapStart < overlapEnd) {
            long overlapMillis = overlapEnd - overlapStart;
            return overlapMillis / (1000.0 * 60.0 * 60.0); 
        } else {
            return 0.0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AulaDisponibilidad that = (AulaDisponibilidad) o;
        return Objects.equals(nroAula, that.nroAula) &&
               Objects.equals(fecha, that.fecha) &&
               Objects.equals(horaInicio, that.horaInicio) &&
               Objects.equals(duracion, that.duracion) &&
               Objects.equals(idReserva, that.idReserva);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nroAula, fecha, horaInicio, duracion, idReserva);
    }
}