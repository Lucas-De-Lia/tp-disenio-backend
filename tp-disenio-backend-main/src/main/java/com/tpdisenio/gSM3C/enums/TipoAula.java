
package com.tpdisenio.gSM3C.enums;

import java.util.Arrays;

public enum TipoAula {
    
    INFORMATICA, MULTIMEDIOS, SIN_RECURSOS_ADICIONALES;

    public static TipoAula fromString(String tipoAulaStr) {
        try {
            return TipoAula.valueOf(tipoAulaStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de aula inválido. Los valores permitidos son: " + Arrays.toString(TipoAula.values()));
        }
    }

    /**
     * Validates if the provided string corresponds to a valid TipoAula enum value.
     *
     * @param tipoAulaStr the string representation of the TipoAula to be validated
     * @throws IllegalArgumentException if the provided string does not match any TipoAula enum value
     */
    public static void validateTipoAula(String tipoAulaStr) {
        if (!Arrays.stream(TipoAula.values()).anyMatch(tipo -> tipo.name().equals(tipoAulaStr))) {
            throw new IllegalArgumentException("Tipo de aula inválido. Los valores permitidos son: " + Arrays.toString(TipoAula.values()));
        }
    }
}
