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
public class AulaMultimedios extends Aula {
    @Column
    private Boolean televisor;
    
    @Column
    private Boolean computadora; // Indica si tiene computadora

    @Column
    private Boolean proyector;

    public AulaMultimedios(Integer nroAula, Integer capacidad, Integer piso, 
            String ubicacion, String tipoPizarron, Boolean habilitada, 
            Boolean ventiladores, Boolean aireAcondicionado,
            Boolean televisor, Boolean computadora, Boolean proyector) {
        super(nroAula, capacidad, piso, ubicacion, tipoPizarron, habilitada, 
              ventiladores, aireAcondicionado);
        this.televisor = televisor;
        this.computadora = computadora;
        this.proyector = proyector;
    }

}
