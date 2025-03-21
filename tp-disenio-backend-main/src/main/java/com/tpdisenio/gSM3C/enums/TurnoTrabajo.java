package com.tpdisenio.gSM3C.enums;

import java.util.Arrays;

public enum TurnoTrabajo {
    
    MANIANA, TARDE, NOCHE;

    public static TurnoTrabajo fromString(String turnoStr) {
        try {
            return TurnoTrabajo.valueOf(turnoStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Turno de trabajo inválido. Los valores permitidos son: " + Arrays.toString(TurnoTrabajo.values()));
        }
    }

    /**
     * Valida si el string proporcionado corresponde a un valor válido del enum TurnoTrabajo.
     *
     * @param turnoStr representación en string del TurnoTrabajo a validar
     * @throws IllegalArgumentException si el string proporcionado no coincide con ningún valor del enum TurnoTrabajo
     */
    public static void validarTurno(String turnoStr) {
        if (!Arrays.stream(TurnoTrabajo.values()).anyMatch(turno -> turno.name().equals(turnoStr))) {
            throw new IllegalArgumentException("Turno de trabajo inválido. Los valores permitidos son: " + Arrays.toString(TurnoTrabajo.values()));
        }
    }
}