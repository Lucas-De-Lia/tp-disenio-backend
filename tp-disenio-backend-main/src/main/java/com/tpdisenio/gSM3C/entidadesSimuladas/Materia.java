/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpdisenio.gSM3C.entidadesSimuladas;

/**
 *
 * @author florh
 */
public class Materia {
    private String id;
    private String nombreString;

    public Materia(String id, String nombreString) {
        this.id = id;
        this.nombreString = nombreString;
    }

    public Materia() {
    }

    public String getId() {
        return id;
    }

    

    public String getNombreString() {
        return nombreString;
    }

    public void setNombreString(String nombreString) {
        this.nombreString = nombreString;
    }
    
}
