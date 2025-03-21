package com.tpdisenio.gSM3C.dao;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.tpdisenio.gSM3C.domain.ReservaDia;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.tpdisenio.gSM3C.domain.Reserva;
import com.tpdisenio.gSM3C.enums.TipoReserva;

import jakarta.persistence.TypedQuery;

@Repository
public class ReservaSQLDao extends DAO<Reserva> implements ReservaDao {

    public ReservaSQLDao() {
    }


    @Override
    public Reserva registrarReserva(Reserva reserva) {
        super.guardar(reserva);
        return reserva;
    }

    @Override
    public List<Reserva> listarReservaPorDia(Date fecha, String tipoAula, Integer numAula) {
        // Activar el filtro
        Session session = em.unwrap(Session.class);
        // Esta línea de código activa el filtro y establece el valor del parámetro (fecha a buscar)
        session.enableFilter("fechaFilter").setParameter("fecha", fecha);

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT DISTINCT r FROM Reserva r ");
        queryBuilder.append("JOIN FETCH r.reservasDia rd ");
        queryBuilder.append("JOIN rd.aula a ");
        queryBuilder.append("LEFT JOIN AulaInformatica a1 ON a.nroAula = a1.nroAula ");
        queryBuilder.append("LEFT JOIN AulaMultimedios a2 ON a.nroAula = a2.nroAula ");
        queryBuilder.append("LEFT JOIN AulaSinRecursosAdicionales a3 ON a.nroAula = a3.nroAula ");
        queryBuilder.append("WHERE 1=1 ");
        // queryBuilder.append("WHERE FUNCTION('DATE', rd.fecha) = :fecha ");
    
        if (tipoAula != null) {
            queryBuilder.append("AND (");
            queryBuilder.append(":tipoAula IS NULL OR ");
            queryBuilder.append("(:tipoAula = 'INFORMATICA' AND a1.nroAula IS NOT NULL) OR ");
            queryBuilder.append("(:tipoAula = 'MULTIMEDIOS' AND a2.nroAula IS NOT NULL) OR ");
            queryBuilder.append("(:tipoAula = 'SIN_RECURSOS_ADICIONALES' AND a3.nroAula IS NOT NULL) ");
            queryBuilder.append(") ");
        }
        if (numAula != null) {
            queryBuilder.append("AND a.nroAula = :numAula ");
        }
    
        TypedQuery<Reserva> query = em.createQuery(queryBuilder.toString(), Reserva.class);
        // query.setParameter("fecha", fecha);
    
        if (tipoAula != null) {
            query.setParameter("tipoAula", tipoAula);
        }
        if (numAula != null) {
            query.setParameter("numAula", numAula);
        }
    
        List<Reserva> reservas = query.getResultList();

        // Desactivar el filtro después de ejecutar la consulta 
        session.disableFilter("fechaFilter");

        return reservas;
    }

    @Override
    public List<Reserva> listarReservaPorCurso(String curso) {
        TypedQuery<Reserva> query = em.createQuery(
            "SELECT r FROM Reserva r WHERE r.actividadAcademica = :curso", Reserva.class);
        query.setParameter("curso", curso);
        return query.getResultList();
    }
    
    /**
     * Obtiene una reserva por su ID. Obtiene solo la información general de la reserva.
     * @param id El ID de la reserva a obtener.
     * @return La reserva correspondiente al ID proporcionado.
     * @throws jakarta.persistence.NoResultException si no se encuentra ninguna reserva con el ID proporcionado.
     */
    @Override
    public Reserva obtenerReservaPorId(String id) {
        TypedQuery<Object[]> query = em.createQuery(
            "SELECT r.idReserva, r.tipoReserva, r.cantidadAlumnos, r.fechaSolicitud, r.apellidoDocente, r.nombreDocente, r.correoDocente, r.actividadAcademica " +
            "FROM Reserva r WHERE r.idReserva = :id", Object[].class);
        query.setParameter("id", id);
        Object[] result = query.getSingleResult();

        Reserva reserva = new Reserva();
        reserva.setIdReserva((String) result[0]);
        reserva.setTipoReserva((TipoReserva) result[1]);
        reserva.setCantidadAlumnos((Integer) result[2]);
        reserva.setFechaSolicitud((Date) result[3]);
        reserva.setApellidoDocente((String) result[4]);
        reserva.setNombreDocente((String) result[5]);
        reserva.setCorreoDocente((String) result[6]);
        reserva.setActividadAcademica((String) result[7]);

        return reserva;
    }

    @Override
    public List<ReservaDia> obtenerReservasDiaPorAulayFecha(int numAula, LocalDate fecha){
        TypedQuery<ReservaDia> query = em.createQuery(
            "SELECT rd FROM ReservaDia rd WHERE rd.aula.nroAula = :numAula AND rd.fecha = :fecha", ReservaDia.class);
        query.setParameter("numAula", numAula);
        query.setParameter("fecha", fecha);
        return query.getResultList();
    }
}
