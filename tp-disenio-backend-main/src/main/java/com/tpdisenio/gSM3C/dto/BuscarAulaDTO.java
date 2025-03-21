/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpdisenio.gSM3C.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 * @author florh
 */
@Data
public class BuscarAulaDTO {
   @Schema(description = "Número de Aula a buscar", example = "3")
    private Integer nroAula;
   @Schema(description = "Tipo de Aula a buscar", example = "Informática")
    private String tipoAula;
   @Schema(description = "Capacidad de Aula a buscar", example = "35")
    private Integer capacidadAula;
   @Schema(description = "Ubicación del Aula a buscar", example = "Ala Sur")
    private String ubicacionAula;
   @Schema(description = "Piso en que se encuentra el aula", example = "2")
    private Integer piso;
   
       

   
   

    
    
}
