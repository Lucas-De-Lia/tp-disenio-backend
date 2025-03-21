package com.tpdisenio.gSM3C.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tpdisenio.gSM3C.dto.BloqueHorarioDTO;
import com.tpdisenio.gSM3C.dto.ObtenerDisponibilidadAulasDTO;
import com.tpdisenio.gSM3C.services.AulaService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/aulas")
public class ObtenerDisponibilidadAulasController {

    @Autowired
    private AulaService aulaService;

    @PostMapping("/disponibilidad")
    public ResponseEntity<?> obtenerDisponibilidad(@Valid @RequestBody ObtenerDisponibilidadAulasDTO dto) {
        List<BloqueHorarioDTO> disponibilidad = aulaService.obtenerDisponibilidadAulas(dto);
        return ResponseEntity.ok(disponibilidad);
    }
}
