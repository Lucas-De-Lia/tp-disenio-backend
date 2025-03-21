/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpdisenio.gSM3C.entidadesSimuladas;

import java.util.List;

/**
 *
 * @author florh
 */
class Profesor {
    private String id;
    private String nombreString;
    private String apellidoString;
    private List<Materia> materias;
    private String emailString;

    public Profesor(String id, String nombreString,String emailString, String apellidoString, List<Materia> materias) {
        this.id = id;
        this.emailString=emailString;
        this.nombreString = nombreString;
        this.apellidoString = apellidoString;
        this.materias=materias;
    }

    public Profesor() {
    }

    public String getEmailString() {
        return emailString;
    }

    public void setEmailString(String emailString) {
        this.emailString = emailString;
    }
    
    

    public List<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
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

    public String getApellidoString() {
        return apellidoString;
    }

    public void setApellidoString(String apellidoString) {
        this.apellidoString = apellidoString;
    }
    
    
}
