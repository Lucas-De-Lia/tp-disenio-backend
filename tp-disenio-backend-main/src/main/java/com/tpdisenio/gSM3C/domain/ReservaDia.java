/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpdisenio.gSM3C.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author florh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ReservaDia {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String idReservaDia;
    
    @Column(nullable=false)
    private LocalDate fecha;
    
    @Column(nullable=false)
    private Time horaInicio;
    
    @Column(nullable=false)
    private Integer duracion; // Duracion en minutos
    
    @ManyToOne
    @JoinColumn(name = "nroAula", nullable = false)
    private Aula aula;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idReserva", nullable = false)
    private Reserva reserva;
    
}
