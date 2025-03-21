/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpdisenio.gSM3C.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.tpdisenio.gSM3C.dao.AdministradorDao;
import com.tpdisenio.gSM3C.domain.Administrador;
import com.tpdisenio.gSM3C.exception.AdministradorAlreadyExistsException;

import jakarta.transaction.Transactional;
/**
 *
 * @author florh
 */
@Service
public class AdministradorService {

    @Autowired
    @Qualifier("administradorSQLDao")
    private AdministradorDao dao;

    public Administrador buscarAdministradorPorId(String id) {
        return dao.buscarPorId(id);
    }

    @Transactional
    public Administrador buscarAdministradorPorUsername(String username) {
        return dao.buscarPorUsername(username);
    }

    /**
     * Searches for an administrator by their ID or username.
     * 
     * This method attempts to find an administrator using the provided identifier.
     * It first searches by ID, and if no administrator is found, it then searches by username.
     * 
     * @param identificador the ID or username of the administrator to search for
     * @return the found administrator, or null if no administrator is found with the given identifier
     */
    @Transactional
    public Administrador buscarAdministradorPorIDoUsername(String identificador) {
        return dao.buscarPorIDoUsername(identificador);
    }
    public boolean esUsernameValido(String username) {
        return username != null && username.matches("^[a-zA-Z0-9_]+$");
    }
    @Transactional
    public Administrador registrarAdministrador(Administrador admin) {
        if(!esUsernameValido(admin.getUsername())){
             throw new IllegalArgumentException("El nombre de usuario no puede contener caracteres especiales.");
        }
        // Crear Administrador a partir de los datos del DTO
        Administrador administrador = new Administrador();
        administrador.setUsername(admin.getUsername());
        administrador.setNombre(admin.getNombre());
        administrador.setApellido(admin.getApellido());
        administrador.setPassword(admin.getPassword());
        administrador.setHabilitado(true);

        // Validar si el Admin ya existe
        if (dao.buscarPorUsername(admin.getUsername()) != null) {
            throw new AdministradorAlreadyExistsException("Administrador con nombre de usuario \"" + admin.getUsername() + "\" ya existente.");
        }

        // Registrar Administrador en la base de datos
        return dao.registrarAdministrador(administrador);
    }
    
}
