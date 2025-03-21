/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpdisenio.gSM3C.dto;

import com.tpdisenio.gSM3C.enums.TipoReserva;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author florh
 */
@Data
public class ObtenerDisponibilidadAulasDTO {
    @NotBlank(message = "El tipo de aula es obligatorio.")
    private String tipoAula;
    
    @NotBlank(message = "El tipo de reserva es obligatorio.")
    private String tipoReserva;
    
    @NotNull(message = "La capacidad del aula es obligatoria.")
    @Min(value = 1, message = "La capacidad del aula debe ser mayor a cero.")
    @Max(value = 1000, message = "La capacidad del aula no puede ser mayor a 1000.")
    private Integer capacidad;

    @Valid
    @NotEmpty(message = "La lista de días a reservar no puede estar vacía.")
    private List<BloqueHorarioDTO> diasReserva;

    /**
     * CAMPOS PARA RESERVAS POR PERIODO
     */
    
    private Integer anioCicloLectivo;

    private String periodoReserva;
}
