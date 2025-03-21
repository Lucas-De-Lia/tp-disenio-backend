
package com.tpdisenio.gSM3C.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.tpdisenio.gSM3C.dao.AulaDAO;
import com.tpdisenio.gSM3C.dao.ReservaDao;
import com.tpdisenio.gSM3C.domain.Aula;
import com.tpdisenio.gSM3C.domain.Reserva;

/**
 * El propósito de esta clase es servir de intermediario entre los servicios 
 * de Aula y Reserva para evitar la dependencia circular.
 */
@Service
public class AulaReservaService {
    @Autowired
    @Qualifier("aulaSQLDao")
    private AulaDAO<Aula> aulaDao;

    @Autowired
    @Qualifier("reservaSQLDao")
    private ReservaDao reservaDao;

    public Aula obtenerAulaPorNumero(Integer nroAula) {
        return aulaDao.buscarPorNroAula(nroAula);
    }

    public Reserva obtenerReservaPorId(String idReserva) {
        return reservaDao.obtenerReservaPorId(idReserva);
    }
}