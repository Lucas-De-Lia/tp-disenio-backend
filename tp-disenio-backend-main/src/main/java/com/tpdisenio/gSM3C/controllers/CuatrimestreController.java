package com.tpdisenio.gSM3C.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tpdisenio.gSM3C.domain.Cuatrimestre;
import com.tpdisenio.gSM3C.services.CuatrimestreService;

@RestController
@RequestMapping("/cuatrimestres")
public class CuatrimestreController {
    @Autowired
    private CuatrimestreService cuatrimestreService;
    
    @GetMapping("/{anioCicloLectivo}")
    public List<Cuatrimestre> getCuatrimestresByYear(@PathVariable Integer anioCicloLectivo) {
        return cuatrimestreService.buscarPorCicloLectivo(anioCicloLectivo);
    }
}
