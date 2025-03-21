/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpdisenio.gSM3C.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.PrimaryKeyJoinColumn;

/**
 *
 * @author florh
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "nroAula")
public class AulaInformatica extends Aula {
    @Column
    private Integer cantidadPCs;

    @Column
    private Boolean proyector;

    public AulaInformatica(Integer nroAula, Integer capacidad, Integer piso, String ubicacion, String tipoPizarron, Boolean habilitada, Boolean ventiladores, Boolean aireAcondicionado, Integer cantidadPCs, Boolean proyector) {
        super(nroAula, capacidad, piso, ubicacion, tipoPizarron, habilitada, ventiladores, aireAcondicionado);
        this.cantidadPCs = cantidadPCs;
        this.proyector = proyector;
    }
    
}
