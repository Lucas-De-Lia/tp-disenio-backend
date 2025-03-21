/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpdisenio.gSM3C.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Date;

/**
 *
 * @author florh
 */
@Entity
public class Cuatrimestre {
    @Id
    @Column(nullable=false)
    private String idCuatrimestre;
    @ManyToOne
    @JoinColumn(name="anio", nullable=false)
    private CicloLectivo cicloLectivo;
    @Column(nullable = false)
    private Date fechaInicio;
    @Column(nullable = false)
    private Date fechaFinal;

    public Cuatrimestre() {
    }

    public Cuatrimestre(String idCuatrimestre, CicloLectivo cicloLectivo, Date fechaInicio, Date fechaFinal) {
        this.idCuatrimestre = idCuatrimestre;
        this.cicloLectivo = cicloLectivo;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
    }

    public String getIdCuatrimestre() {
        return idCuatrimestre;
    }

    public void setIdCuatrimestre(String idCuatrimestre) {
        this.idCuatrimestre = idCuatrimestre;
    }

    public CicloLectivo getCicloLectivo() {
        return cicloLectivo;
    }

    public void setCicloLectivo(CicloLectivo cicloLectivo) {
        this.cicloLectivo = cicloLectivo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
    
    
     
     
}
