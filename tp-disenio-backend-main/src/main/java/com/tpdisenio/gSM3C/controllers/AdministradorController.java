package com.tpdisenio.gSM3C.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tpdisenio.gSM3C.domain.Administrador;
import com.tpdisenio.gSM3C.services.AdministradorService;

@RestController
@RequestMapping()
public class AdministradorController {

    private final AdministradorService administradorService;

    public AdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    // Obtener administrador por id de usuario o username
    @GetMapping("/get-admin/{identificador}")
    public ResponseEntity<Administrador> getAdministrador(@PathVariable String identificador){
        Administrador administrador = administradorService.buscarAdministradorPorIDoUsername(identificador);
        if (administrador != null) {
            return ResponseEntity.ok(administrador);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
