package com.tpdisenio.gSM3C.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for date-related operations.
 */
public class FechaUtils {

    /**
     * Obtiene una lista de fechas que corresponden a un día específico de la semana dentro de un rango de fechas.
     *
     * @param inicio La fecha de inicio del rango.
     * @param fin La fecha de fin del rango.
     * @param diaDeLaSemanaStr El día de la semana en formato de cadena (por ejemplo, "LUNES", "MARTES").
     * @return Una lista de objetos Date que representan las fechas que corresponden al día de la semana especificado dentro del rango.
     */
    public static List<LocalDate> obtenerDiasDeLaSemanaEnRango(Date inicio, Date fin, String diaDeLaSemanaStr) {
        DayOfWeek diaDeLaSemana = convertirDiaSemana(diaDeLaSemanaStr);
        List<LocalDate> dias = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        LocalDate localDate;
        calendar.setTime(inicio);

        while (calendar.getTime().before(fin) || calendar.getTime().equals(fin)) {
            if (calendar.get(Calendar.DAY_OF_WEEK) == diaDeLaSemana.getValue() % 7 + 1) {
                localDate = calendar.getTime().toInstant().atZone(calendar.getTimeZone().toZoneId()).toLocalDate();
                dias.add(localDate);
            }
            calendar.add(Calendar.DATE, 1);
        }

        return dias;
    }

    /**
     * Convierte una lista de nombres de días de la semana en formato de cadena a una lista de objetos DayOfWeek.
     *
     * @param diasSemanaStr Lista de nombres de días de la semana en formato de cadena.
     * @return Lista de objetos DayOfWeek correspondientes a los nombres de días de la semana proporcionados.
     */
    public static List<DayOfWeek> convertirDiasSemana(List<String> diasSemanaStr) {
        return diasSemanaStr.stream()
                .map(FechaUtils::convertirDiaSemana)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una cadena que representa un día de la semana en un objeto DayOfWeek.
     *
     * @param diaSemanaStr La cadena que representa el día de la semana. Debe ser uno de los siguientes valores:
     *                     "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO".
     * @return El objeto DayOfWeek correspondiente al día de la semana.
     * @throws IllegalArgumentException Si el día de la semana proporcionado no es válido.
     */
    public static DayOfWeek convertirDiaSemana(String diaSemanaStr) {
        switch (diaSemanaStr.toUpperCase()) {
            case "LUNES":
                return DayOfWeek.MONDAY;
            case "MARTES":
                return DayOfWeek.TUESDAY;
            case "MIERCOLES":
                return DayOfWeek.WEDNESDAY;
            case "JUEVES":
                return DayOfWeek.THURSDAY;
            case "VIERNES":
                return DayOfWeek.FRIDAY;
            case "SABADO":
                return DayOfWeek.SATURDAY;
            case "DOMINGO":
                return DayOfWeek.SUNDAY;
            default:
                throw new IllegalArgumentException("Día de la semana inválido: " + diaSemanaStr + ". Los valores permitidos son: LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO.");
        }
    }

    
    /**
     * Verifica si el día de la semana proporcionado está entre lunes y viernes.
     *
     * @param diaSemanaStr El día de la semana en formato de cadena (por ejemplo, "Lunes", "Martes").
     * @return true si el día de la semana está entre lunes y viernes, false en caso contrario.
     */
    public static Boolean inLunesAViernes(String diaSemanaStr) {
        return convertirDiaSemana(diaSemanaStr).getValue() >= 1 && convertirDiaSemana(diaSemanaStr).getValue() <= 5;
    }

}