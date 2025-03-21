package com.tpdisenio.gSM3C.dao;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.tpdisenio.gSM3C.domain.Reserva;
import com.tpdisenio.gSM3C.domain.ReservaDia;

public interface ReservaDao {
    public Reserva registrarReserva(Reserva reserva);
    
    public List<Reserva> listarReservaPorDia(Date fecha, String tipoAula, Integer numAula);
    
    public List<Reserva> listarReservaPorCurso(String curso);

    public Reserva obtenerReservaPorId(String idReserva);

    public List<ReservaDia> obtenerReservasDiaPorAulayFecha(int numAula, LocalDate fecha);
}
