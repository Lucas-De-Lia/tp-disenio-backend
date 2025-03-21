/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpdisenio.gSM3C.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author florh
 */
@Data
public class AulaDTO {
    @NotNull(message = "El numero de aula es obligatorio")
    private Integer nroAula;
     @NotBlank(message = "El estado del aula es obligatorio")
    private Boolean habilitada;
    
    @NotBlank(message = "El tipo de aula es obligatorio.")
    private String tipoAula;
    @NotBlank(message = "El tipo de pizarron es obligatorio.")
    private String tipoPizarron;
    @NotBlank(message = "La ubicacion es obligatorio.")
    private String ubicacion;

    @NotNull(message = "La capacidad del aula es obligatoria.")
    @Min(value = 1, message = "La capacidad del aula debe ser mayor a cero.")
    private Integer capacidad;
    
    @NotNull(message = "El piso donde se encuentra el aula es obligatorio")
    private Integer piso;
    
    private Boolean aireAcondicionado;
    private Boolean ventiladores;
    private Boolean proyector;
    private Boolean pcs;
    private Boolean televisor;
    
    @NotNull(message = "El numero de pcs es obligatorio")
    private Integer cantPcs;
}
