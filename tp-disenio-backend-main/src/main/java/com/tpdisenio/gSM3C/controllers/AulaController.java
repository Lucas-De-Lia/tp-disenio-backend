/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpdisenio.gSM3C.controllers;

import com.tpdisenio.gSM3C.dto.AulaDTO;
import com.tpdisenio.gSM3C.services.AulaService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author florh
 */
@RestController
@RequestMapping()
public class AulaController {

    @Autowired
    private AulaService aulaService;

    @GetMapping("/aulas")
    @Operation(summary = "Listar todas las aulas habilitadas")
    public ResponseEntity<?> obtenerAulasHabilitadas() {
        return ResponseEntity.ok(aulaService.obtenerAulasHabilitadas());
    }
    
}
