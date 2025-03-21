/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpdisenio.gSM3C.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import com.tpdisenio.gSM3C.enums.TipoReserva;

import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.UniqueConstraint;

/**
 *
 * @author florh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FilterDef(name = "fechaFilter", parameters = @ParamDef(name = "fecha", type = java.util.Date.class))
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idReserva;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private TipoReserva tipoReserva;
    
    @Column(nullable=false)
    private Integer cantidadAlumnos;
    
    @Column(nullable=false)
    private Date fechaSolicitud;
    
    @Column(nullable=false)
    private String apellidoDocente;
    
    @Column(nullable=false)
    private String nombreDocente;
    
    @Column(nullable=false)
    private String correoDocente;
    
    @Column(nullable=false)
    private String actividadAcademica;

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    @Filter(name = "fechaFilter", condition = "DATE(fecha) = :fecha")
    private List<ReservaDia> reservasDia;

    @ManyToOne
    @JoinColumn(name = "realizada_por", nullable = false)
    private Bedel realizadaPor;

    @ManyToMany
    @JoinTable(
        name = "reserva_cuatrimestre",
        joinColumns = @JoinColumn(name = "id_reserva"),
        inverseJoinColumns = @JoinColumn(name = "id_cuatrimestre"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_reserva", "id_cuatrimestre"})
    )
    private List<Cuatrimestre> cuatrimestres;

}
