package com.tpdisenio.gSM3C.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDTO {

    @NotBlank(message = "El tipo de reserva es obligatorio.")
    private String tipoReserva;

    @NotNull(message = "La cantidad de alumnos es obligatoria.")
    @Min(value = 1, message = "La cantidad de alumnos debe mayor a cero.")
    private Integer cantidadAlumnos;

    @NotBlank(message = "El apellido del docente es obligatorio.")
    private String apellidoDocente;

    @NotBlank(message = "El nombre del docente es obligatorio.")
    private String nombreDocente;

    @NotBlank(message = "El correo del docente es obligatorio.")
    @Email(message = "El correo del docente debe ser una dirección de correo electrónico válida.")
    private String correoDocente;

    @NotBlank(message = "La actividad académica es obligatoria.")
    private String actividadAcademica;

    @NotBlank(message = "El ID del bedel que realiza la reserva es obligatorio.")
    private String realizadaPor;
    
    /**
     * CAMPOS PARA RESERVAS ESPORADICAS
     */

    @Valid
    private List<ReservaDiaDTO> reservasDia;

    /**
     * CAMPOS PARA RESERVAS POR PERIODO
     */
    
    private Integer anioCicloLectivo;

    private String periodoReserva;

    @Valid
    private List<ReservaDiaSemanaDTO> reservasDiasSemana;

}
