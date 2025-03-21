package com.tpdisenio.gSM3C.dto;

import java.util.Date;
import com.tpdisenio.gSM3C.enums.TipoReserva;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaResponseDTO {
    private String idReserva;
    private TipoReserva tipoReserva;
    private Integer cantidadAlumnos;
    private Date fechaSolicitud;
    private String apellidoDocente;
    private String nombreDocente;
    private String correoDocente;
    private String actividadAcademica;
}