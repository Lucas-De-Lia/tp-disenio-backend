/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpdisenio.gSM3C.dao;
import java.util.Date;
import java.util.List;

import com.tpdisenio.gSM3C.domain.Aula;
import com.tpdisenio.gSM3C.domain.AulaDisponibilidad;
import com.tpdisenio.gSM3C.enums.TipoAula;
import java.time.LocalDate;



/**
 *
 * @author florh
 * @param <T> Tipo de aula
 */
public interface AulaDAO<T>{

    public T buscarPorNroAula(Integer nroAula);
    
    public Boolean modificarAula(T aula);

    public Boolean eliminarAula(Integer numAula);

    public List<T> buscarAulas(Integer numAula, String tipoAula, Integer capacidad);
    
    public List<AulaDisponibilidad> obtenerDisponibilidadAulas(TipoAula tipoAula, LocalDate fecha, Integer capacidadMinima);

    public List<T> obtenerAulasHabilitadas();
}
