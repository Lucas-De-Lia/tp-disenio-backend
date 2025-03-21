/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tpdisenio.gSM3C.controllers;


import com.tpdisenio.gSM3C.dto.BedelDTO;
import com.tpdisenio.gSM3C.services.UsuarioService;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author florh
 */
@RestController
@RequestMapping()
public class Login {
     @Autowired
    private UsuarioService usuarioService;

@PostMapping("/login")
public ResponseEntity<Map<String, Object>> login(@RequestParam String username, @RequestParam String password, HttpSession session) {
    Map<String, Object> response = new HashMap<>();

    

    // Verifica las credenciales
    if (usuarioService.autenticarUsuario(username, password) == null) {
        response.put("status", "unauthorized");
        response.put("message", "Credenciales inválidas");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // Guarda el usuario en la sesión
    session.setAttribute("user", username);

    // Crear el DTO con la información del usuario
    BedelDTO dTO =usuarioService.buscarPorUser(username);

    // Agregar el DTO a la respuesta
    response.put("status", "accepted");
    response.put("message", "Credenciales válidas");
    response.put("user", dTO); // Agregar el DTO en la respuesta

    return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
}



    @GetMapping("/loginform")
    public String showLoginForm() {
        return "login"; // Nombre del template de login en resources/templates/login.html
    }

    @GetMapping("/home")
    public String home() {
        return "home"; // Nombre del template de home
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        if (session != null) {
            session.invalidate(); // Invalidar la sesión actual
            return ResponseEntity.status(HttpStatus.OK).body("Sesión cerrada correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No hay sesión activa");
        }
    }
}
