package com.tpdisenio.gSM3C.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AulaResponseDTO {
    private Integer nroAula;
    private Integer capacidad;
    private Integer piso;
    private String ubicacion;
    private String tipoPizarron;
    private Boolean habilitada;
    private Boolean ventiladores;
    private Boolean aireAcondicionado;
    private Integer cantidadPCs;
    private Boolean proyector;
    private Boolean televisor;
    private Boolean computadora;
}