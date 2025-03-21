package com.tpdisenio.gSM3C.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BedelDTO {
    private String idUsuario;
    
    @Size(min = 1, message = "La longitud del nombre de usuario debe ser mayor a cero.")
    private String username;

    @NotNull(message = "El nombre es obligatorio.")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres.")
    private String nombre;
    
    @NotNull(message = "El apellido es obligatorio.")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres.")
    private String apellido;
    
    @Size(min = 8, max = 50, message = "La contraseña debe tener entre 8 y 50 caracteres.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[@#$%&*])(?=.*\\d).+$", message = "La contraseña debe contener al menos una letra mayúscula, un signo especial (@#$%&*) y un dígito.")
    private String password;
    
    @Size(min = 8, max = 50, message = "La contraseña repetida debe tener entre 8 y 50 caracteres.")
    private String repeatedPassword;

    @NotNull(message = "El turno es obligatorio.")
    @Size(min = 1, message = "La longitud del turno debe ser mayor a cero.")
    private String turno;

    @Size(min = 1, message = "La longitud del identificador de usuario de quien registra al bedel debe ser mayor a cero.")
    private String registradoPor;
   
}