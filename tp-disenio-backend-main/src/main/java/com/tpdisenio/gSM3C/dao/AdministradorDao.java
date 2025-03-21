/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpdisenio.gSM3C.dao;

import com.tpdisenio.gSM3C.domain.Administrador;
import java.util.List;

/**
 *
 * @author florh
 */
public interface AdministradorDao {
    public Administrador registrarAdministrador(Administrador admin);
    
    public Boolean eliminarAdministrador(String idUsuario);
    
    public Boolean modificarAdministrador(Administrador admin);

    public Administrador buscarPorId(String id);

    public void cargarAdministradores(List<Administrador> admins);

    public List<Administrador> listarAdministradores();

    public Administrador buscarPorUsername(String username);

    public Administrador buscarPorIDoUsername(String identificador);
}
