/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpdisenio.gSM3C.entidadesSimuladas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author florh
 */
@RestController
@RequestMapping()
public class ProfesorController {
    private static  List<Profesor> profesores;
    

    static{
        Materia materia1 = new Materia("abc", "Matemáticas");
        Materia materia2 = new Materia("bcd", "Física");
        Materia materia32 = new Materia("cde", "Quimica");
        Materia materia33 = new Materia("cde", "Paradigmas");
        Materia materia321 = new Materia("cde", "Diseño");
        Materia materia323 = new Materia("cde", "Economía");
        Materia materia35 = new Materia("cde", "Informática");
        Materia materia36 = new Materia("cde", "Probabilidad");
        

        
       Profesor p1= new Profesor("1L", "Carlos","carlos@gmail.com", "Gómez", Arrays.asList(materia1, materia2));
       Profesor p2= new Profesor("2L", "María","mariaperez@gmail.com", "Pérez", Arrays.asList(materia32,materia36));
       Profesor p3= new Profesor("3L", "Ana","anafer@hotmail.com", "Fernández", Arrays.asList(materia1, materia321));
       Profesor p22= new Profesor("4L", "Carolina","carolina_27@gmail.com", "Gonzales", Arrays.asList(materia321, materia33));
       Profesor p23= new Profesor("5L", "Marianela","marianelap@gmail.com", "Paoloni", Arrays.asList(materia323,materia36));
       Profesor p34= new Profesor("6L", "Analia","analia23@hotmail.com", "Frene", Arrays.asList(materia35, materia1));
       profesores = new ArrayList<>();
       profesores.add(p3);
       profesores.add(p2);
       profesores.add(p1);
       profesores.add(p22);
       profesores.add(p23);
       profesores.add(p34);
       
    }
    
    
 
    @GetMapping("/profesores")
    public ResponseEntity<List<Profesor>> getProfesores() {
        if (profesores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
        }
        return ResponseEntity.ok(profesores); // 200 OK
    }

    // Endpoint para obtener un profesor por ID
    @GetMapping("/profesores/{id}")
    public ResponseEntity<Profesor> getProfesorById(@PathVariable Long id) {
        Optional<Profesor> profesor = profesores.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (profesor.isPresent()) {
            return ResponseEntity.ok(profesor.get()); // 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(null); // 404 Not Found
        }
    }

    public boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @PostMapping
    public ResponseEntity<?> addProfesor(@RequestBody Profesor profesor) {
    if (isValidEmail(profesor.getEmailString()) &&
        profesor.getApellidoString() != null && !profesor.getApellidoString().isEmpty() &&
        profesor.getNombreString() != null && !profesor.getNombreString().isEmpty()) {
        
        
        profesores.add(profesor);
        
        
        return ResponseEntity.status(HttpStatus.CREATED).body(profesor);
    } else {
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body("Los datos proporcionados no son válidos.");
    }
    }
}
