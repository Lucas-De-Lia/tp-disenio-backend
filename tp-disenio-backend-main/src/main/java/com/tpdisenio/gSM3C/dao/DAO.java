/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpdisenio.gSM3C.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

/**
 * DAO genérico para operaciones CRUD.
 *
 * @param <T> Tipo de entidad.
 */
@Component
public class DAO<T> {

    @PersistenceContext
    protected EntityManager em;

    // OBJETIVO: persistir un objeto en la base de datos.
    // Toma como parámetro el objeto a persistir, genérico, 
    // lo que puede aceptar cualquier tipo de objeto (Dirección, Mascota, Persona)
    public void guardar(T objeto) {
        em.persist(objeto);
    }

    // OBJETIVO: modificar una tupla de una base de datos.
    // Recibe como parámetro el objeto con los datos cambiados (debe mantener
    // la misma llave primaria) y lo reemplaza por el anterior.
    public void editar(T objeto) {
        em.merge(objeto);
    }

    // OBJETIVO: eliminar un registro de la base de datos.
    // Como parámetro se pasa el objeto a eliminar de la base de datos.
    // Se busca en la base de datos el registro que contenga la misma información
    // que el parámetro recibido, y se elimina.
    public void eliminar(T objeto) {
        em.remove(objeto);
    }
}
