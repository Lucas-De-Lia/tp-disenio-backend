/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpdisenio.gSM3C.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 *
 * @author florh
 */
@Entity
@Table(name = "administradores")
public class Administrador extends Usuario{
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private String idUsuario;

    public Administrador() {
    }
    
    public Administrador(String username, String nombre, String apellido, String password, boolean habilitado) {
        super.setUsername(username);
        super.setNombre(nombre);
        super.setApellido(apellido);
        super.setPassword(password);
        super.setHabilitado(habilitado);
    }

    public String getIdUsuario() {
        return idUsuario;
    }
    
    
    
}
