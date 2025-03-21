/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpdisenio.gSM3C.dao;

import com.tpdisenio.gSM3C.domain.Administrador;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 *
 * @author florh
 */
@Repository
public class AdministradorSQLDao extends DAO<Administrador> implements AdministradorDao {

    /**
     * Registra un nuevo administrador en la base de datos.
     *
     * @param admin El objeto Administrador que se desea registrar.
     * @return El objeto Administrador registrado.
     */
    @Override
    public Administrador registrarAdministrador(Administrador admin) {
        super.guardar(admin);
        return admin;
    }

    @Override
    public Boolean eliminarAdministrador(String idUsuario) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Boolean modificarAdministrador(Administrador admin) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
    /**
     * Busca un objeto Administrador por su id.
     *
     * @param id El identificador del administrador a buscar.
     * @return El objeto Administrador encontrado, o null si no se encuentra.
     */
    @Override
    public Administrador buscarPorId(String id) {
        Administrador admin = (Administrador) em.find(Administrador.class, id);
        return admin;
    }

    @Override
    public void cargarAdministradores(List<Administrador> admins) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Administrador> listarAdministradores() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Administrador buscarPorUsername(String username) {
        try {
            return (Administrador) em.createQuery("SELECT a FROM Administrador a WHERE a.username = :username")
                .setParameter("username", username)
                .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public Administrador buscarPorIDoUsername(String identificador) {
        try {
            return (Administrador) em.createQuery("SELECT a FROM Administrador a WHERE a.id = :id OR a.username = :username")
                .setParameter("id", identificador)
                .setParameter("username", identificador)
                .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
}
