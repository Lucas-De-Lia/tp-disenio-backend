package com.tpdisenio.gSM3C.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.*;

import com.tpdisenio.gSM3C.domain.Bedel;
import com.tpdisenio.gSM3C.dto.BedelDTO;
import com.tpdisenio.gSM3C.dto.BuscarBedelDTO;
import com.tpdisenio.gSM3C.services.BedelService;

import io.swagger.v3.oas.annotations.Operation;

/**
 * BedelController
 */
@RestController
@RequestMapping()
public class BedelController {

    private final BedelService bedelService;

    public BedelController(BedelService bedelService) {
        this.bedelService = bedelService;
    }

    // Obtener todos los bedeles
    @GetMapping("/bedeles")
    @Operation(summary = "Listar todos los bedeles")
    public ResponseEntity<List<Bedel>> getBedeles() {
        List<Bedel> bedeles = bedelService.listarBedeles();
        return ResponseEntity.ok(bedeles);
    }

    //Obtener todos los bedeles habilitados
    @GetMapping("/bedeles/habilitados")
    @Operation(summary = "Listar todos los bedeles habilitados")
    public ResponseEntity<List<Bedel>> getBedelesHabilitados() {
        List<Bedel> bedeles = bedelService.listarBedelesHabilitados();
        return ResponseEntity.ok(bedeles);
    }

    // Obtener bedel por id de usuario
    @GetMapping("/bedeles/{id}")
    @Operation(summary = "Obtener bedel por ID de usuario")
    public ResponseEntity<Bedel> getBedel(@PathVariable String id) {
        Bedel bedel = bedelService.buscarBedelPorId(id);
        if (bedel != null) {
            return ResponseEntity.ok(bedel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Crear nuevo bedel
    @PostMapping("/bedeles")
    @Operation(summary = "Crear bedel")
    public ResponseEntity<?> postBedel(@Valid @RequestBody BedelDTO bedelDTO) {
        Bedel createdBedel = bedelService.registrarBedel(bedelDTO);

        // Creamos la uri para consumir el bedel creado
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{idBedel}")
                .buildAndExpand(bedelDTO.getIdUsuario())
                .toUri();

        return ResponseEntity.created(location).body(createdBedel);
    }

    // Actualizar bedel
    @PutMapping("/bedeles")
    @Operation(summary = "Modificar bedel")
    public ResponseEntity<?> putBedel(@Valid @RequestBody BedelDTO bedelDTO) {
        bedelService.modificarBedel(bedelDTO);
        return ResponseEntity.ok().build();
    }

    // Eliminar bedel por su id de usuario
    @DeleteMapping("/bedeles/{id}")
    @Operation(summary = "Eliminar bedel por ID de usuario")
    public ResponseEntity<Void> deleteBedel(@PathVariable String id) {
        return bedelService.eliminarBedel(id) 
            ? ResponseEntity.noContent().build() 
            : ResponseEntity.notFound().build();
    }

    // Obtener bedel por apellido o turno
    @GetMapping("/bedeles/search")
    @Operation(summary = "Buscar bedeles por apellido o turno")
    public ResponseEntity<List<Bedel>> getBedelesByApellidoOrTurno(@ModelAttribute BuscarBedelDTO buscarBedelDTO) {
        List<Bedel> bedeles = bedelService.buscarBedeles(buscarBedelDTO);
        return ResponseEntity.ok(bedeles);
    }

}