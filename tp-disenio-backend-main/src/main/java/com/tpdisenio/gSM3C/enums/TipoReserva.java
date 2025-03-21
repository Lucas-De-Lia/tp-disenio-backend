
package com.tpdisenio.gSM3C.enums;

import java.util.Arrays;

public enum TipoReserva {
    
    ESPORADICA, POR_PERIODO;

    public static TipoReserva fromString(String tipoReservaStr) {
        try {
            return TipoReserva.valueOf(tipoReservaStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de reserva inválido. Los valores permitidos son: " + Arrays.toString(TipoReserva.values()));
        }
    }
    /**
     * Validates if the provided string corresponds to a valid TipoReserva enum value.
     *
     * @param tipoReservaStr the string representation of the TipoReserva to be validated
     * @throws IllegalArgumentException if the provided string does not match any TipoReserva enum value
     */
    public static void validateTipoReserva(String tipoReservaStr) {
        if (!Arrays.stream(TipoReserva.values()).anyMatch(tipo -> tipo.name().equals(tipoReservaStr))) {
            throw new IllegalArgumentException("Tipo de reserva inválido. Los valores permitidos son: " + Arrays.toString(TipoReserva.values()));
        }
    }
}