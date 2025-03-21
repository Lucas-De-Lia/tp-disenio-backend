package com.tpdisenio.gSM3C.utils;

import com.tpdisenio.gSM3C.domain.Aula;
import com.tpdisenio.gSM3C.domain.AulaInformatica;
import com.tpdisenio.gSM3C.domain.AulaMultimedios;
import com.tpdisenio.gSM3C.domain.Reserva;
import com.tpdisenio.gSM3C.dto.AulaResponseDTO;
import com.tpdisenio.gSM3C.dto.ReservaResponseDTO;

public class DTOConverter {
    public static AulaResponseDTO convertirAulaResponseDTO(Aula aula) {
        AulaResponseDTO aulaResponseDTO = new AulaResponseDTO();
        aulaResponseDTO.setNroAula(aula.getNroAula());
        aulaResponseDTO.setCapacidad(aula.getCapacidad());
        aulaResponseDTO.setPiso(aula.getPiso());
        aulaResponseDTO.setUbicacion(aula.getUbicacion());
        aulaResponseDTO.setTipoPizarron(aula.getTipoPizarron());
        aulaResponseDTO.setHabilitada(aula.getHabilitada());
        aulaResponseDTO.setVentiladores(aula.getVentiladores());
        aulaResponseDTO.setAireAcondicionado(aula.getAireAcondicionado());
        if (aula instanceof AulaInformatica aulaInformatica) {
            aulaResponseDTO.setCantidadPCs(aulaInformatica.getCantidadPCs());
            aulaResponseDTO.setProyector(aulaInformatica.getProyector());
        } else if (aula instanceof AulaMultimedios aulaMultimedia) {
            aulaResponseDTO.setTelevisor(aulaMultimedia.getTelevisor());
            aulaResponseDTO.setComputadora(aulaMultimedia.getComputadora());
            aulaResponseDTO.setProyector(aulaMultimedia.getProyector());
        }
        return aulaResponseDTO;
    }

    public static ReservaResponseDTO convertirReservaResponseDTO(Reserva reserva) {
        ReservaResponseDTO reservaResponseDTO = new ReservaResponseDTO();
        reservaResponseDTO.setIdReserva(reserva.getIdReserva());
        reservaResponseDTO.setTipoReserva(reserva.getTipoReserva());
        reservaResponseDTO.setCantidadAlumnos(reserva.getCantidadAlumnos());
        reservaResponseDTO.setFechaSolicitud(reserva.getFechaSolicitud());
        reservaResponseDTO.setApellidoDocente(reserva.getApellidoDocente());
        reservaResponseDTO.setNombreDocente(reserva.getNombreDocente());
        reservaResponseDTO.setCorreoDocente(reserva.getCorreoDocente());
        reservaResponseDTO.setActividadAcademica(reserva.getActividadAcademica());
        return reservaResponseDTO;
    }
}