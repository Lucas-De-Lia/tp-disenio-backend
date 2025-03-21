
package com.tpdisenio.gSM3C.enums;

import java.util.Arrays;

public enum PeriodoReserva {
    
    PRIMER_CUATRIMESTRE, SEGUNDO_CUATRIMESTRE, ANUAL;

    public static PeriodoReserva fromString(String periodoReservaStr) {
        try {
            return PeriodoReserva.valueOf(periodoReservaStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Periodo de reserva inválido. Los valores permitidos son: " + Arrays.toString(PeriodoReserva.values()));
        }
    }

    /**
     * Validates if the provided string corresponds to a valid PeriodoReserva enum value.
     *
     * @param periodoReservaStr the string representation of the PeriodoReserva to be validated
     * @throws IllegalArgumentException if the provided string does not match any PeriodoReserva enum value
     */
    public static void validatePeriodoReserva(String periodoReservaStr) {
        if (!Arrays.stream(PeriodoReserva.values()).anyMatch(periodo -> periodo.name().equals(periodoReservaStr))) {
            throw new IllegalArgumentException("Periodo de reserva inválido. Los valores permitidos son: " + Arrays.toString(PeriodoReserva.values()));
        }
    }
}