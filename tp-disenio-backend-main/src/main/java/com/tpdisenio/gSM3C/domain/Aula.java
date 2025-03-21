/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpdisenio.gSM3C.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tpdisenio.gSM3C.enums.TipoAula;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author florh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "tipoAula"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = AulaInformatica.class, name = "INFORMATICA"),
    @JsonSubTypes.Type(value = AulaMultimedios.class, name = "MULTIMEDIOS"),
    @JsonSubTypes.Type(value = AulaSinRecursosAdicionales.class, name = "SIN_RECURSOS_ADICIONALES")
})
@Table(name = "aula")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Aula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer nroAula;

    @Column(nullable = false)
    private Integer capacidad;
    
    @Column
    private Integer piso;
    
    @Column
    private String ubicacion;
    
    @Column
    private String tipoPizarron;
    
    @Column
    private Boolean habilitada;
    
    @Column
    private Boolean ventiladores;
    
    @Column
    private Boolean aireAcondicionado;

}
