package com.tpdisenio.gSM3C.dao;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.tpdisenio.gSM3C.domain.Aula;
import com.tpdisenio.gSM3C.domain.AulaDisponibilidad;
import com.tpdisenio.gSM3C.domain.AulaInformatica;
import com.tpdisenio.gSM3C.domain.AulaMultimedios;
import com.tpdisenio.gSM3C.domain.AulaSinRecursosAdicionales;
import com.tpdisenio.gSM3C.enums.TipoAula;

import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.time.LocalDate;

@Repository
public class AulaSQLDao extends DAO<Aula> implements AulaDAO<Aula> {

    /**
     * Obtiene la disponibilidad de aulas según el tipo de aula, capacidad y la
     * fecha especificada.
     *
     * @param tipoAula El tipo de aula (INFORMATICA, MULTIMEDIOS,
     *                 SIN_RECURSOS_ADICIONALES).
     * @param fecha    La fecha para la cual se desea obtener la disponibilidad.
     * @return Una lista de objetos AulaDisponibilidad que representan la
     *         disponibilidad de las aulas.
     */
    @Transactional
    public List<AulaDisponibilidad> obtenerDisponibilidadAulas(TipoAula tipoAula, LocalDate fecha, Integer capacidadMinima) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT a.nro_aula, a.capacidad, rd.id_reserva, rd.hora_inicio, rd.duracion ");
        queryBuilder.append("FROM aula a ");

        if (TipoAula.INFORMATICA.equals(tipoAula))
            queryBuilder.append("JOIN aula_informatica a1 ON a.nro_aula = a1.nro_aula ");
        else if (TipoAula.MULTIMEDIOS.equals(tipoAula))
            queryBuilder.append("JOIN aula_multimedios a1 ON a.nro_aula = a1.nro_aula ");
        else if (TipoAula.SIN_RECURSOS_ADICIONALES.equals(tipoAula))
            queryBuilder.append("JOIN aula_sin_recursos_adicionales a1 ON a.nro_aula = a1.nro_aula ");

        queryBuilder.append("LEFT JOIN reserva_dia rd ON a.nro_aula = rd.nro_aula  ");
        queryBuilder.append("AND DATE(rd.fecha) = :fecha ");
        queryBuilder.append("WHERE a.habilitada = 1 ");
        queryBuilder.append("AND a.capacidad >= :capacidad ");

        Query query = em.createNativeQuery(queryBuilder.toString(), Object[].class);
        query.setParameter("fecha", fecha);
        query.setParameter("capacidad", capacidadMinima);

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();

        // Realizar el mapeo de los datos obtenidos
        List<AulaDisponibilidad> listaDTO = new ArrayList<>();
        for (Object[] row : results) {
            Integer nroAula = ((Number) row[0]).intValue();
            Integer capacidad = ((Number) row[1]).intValue();
            String idReserva = (String) row[2];
            Time horaInicio = (Time) row[3];
            Integer duracion = row[4] != null ? ((Number) row[4]).intValue() : null;

            AulaDisponibilidad dto = new AulaDisponibilidad(nroAula, capacidad, idReserva, fecha, horaInicio, duracion);

            listaDTO.add(dto);
        }

        return listaDTO;
    }

    @Override
    public Aula buscarPorNroAula(Integer nroAula) {
        return em.find(Aula.class, nroAula);
    }

    @Override
    public Boolean modificarAula(Aula aula) {
        try {
            Aula aulaExistente = em.find(Aula.class, aula.getNroAula());
            if (aulaExistente != null) {
                // Actualizar atributos comunes
                aulaExistente.setCapacidad(aula.getCapacidad());
                aulaExistente.setPiso(aula.getPiso());
                aulaExistente.setUbicacion(aula.getUbicacion());
                aulaExistente.setTipoPizarron(aula.getTipoPizarron());
                aulaExistente.setHabilitada(aula.getHabilitada());
                aulaExistente.setVentiladores(aula.getVentiladores());
                aulaExistente.setAireAcondicionado(aula.getAireAcondicionado());

                // Actualizar atributos específicos
                if (aula instanceof AulaInformatica) {
                    AulaInformatica aulaInfo = (AulaInformatica) aula;
                    ((AulaInformatica) aulaExistente).setCantidadPCs(aulaInfo.getCantidadPCs());
                    ((AulaInformatica) aulaExistente).setProyector(aulaInfo.getProyector());
                } else if (aula instanceof AulaMultimedios) {
                    AulaMultimedios aulaMulti = (AulaMultimedios) aula;
                    ((AulaMultimedios) aulaExistente).setTelevisor(aulaMulti.getTelevisor());
                    ((AulaMultimedios) aulaExistente).setComputadora(aulaMulti.getComputadora());
                    ((AulaMultimedios) aulaExistente).setProyector(aulaMulti.getProyector());
                }

                em.merge(aulaExistente);
                return true;
            }
            return false; // Aula no encontrada
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean eliminarAula(Integer numAula) {
        try {
            Aula aula = em.find(Aula.class, numAula);
            if (aula != null) {
                aula.setHabilitada(false); // Deshabilitar aula
                em.merge(aula);
                return true;
            }
            return false; // Aula no encontrada
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Aula> buscarAulas(Integer numAula, String tipoAula, Integer capacidad) {
        try {
            StringBuilder jpql = new StringBuilder("SELECT a FROM Aula a WHERE 1=1");

            // Filtrar por número de aula
            if (numAula != null) {
                jpql.append(" AND a.nroAula = :numAula");
            }

            // Filtrar por tipo de aula
            if (tipoAula != null) {
                jpql.append(" AND TYPE(a) = :tipoAula");
            }

            // Filtrar por capacidad mínima
            if (capacidad != null) {
                jpql.append(" AND a.capacidad >= :capacidad");
            }

            TypedQuery<Aula> query = em.createQuery(jpql.toString(), Aula.class);

            // Asignar parámetros condicionalmente
            if (numAula != null) {
                query.setParameter("numAula", numAula);
            }

            if (tipoAula != null) {
                Class<? extends Aula> aulaClass = switch (tipoAula) {
                    case "AulaInformatica" -> AulaInformatica.class;
                    case "AulaMultimedios" -> AulaMultimedios.class;
                    default -> AulaSinRecursosAdicionales.class;
                };
                query.setParameter("tipoAula", aulaClass);
            }

            if (capacidad != null) {
                query.setParameter("capacidad", capacidad);
            }

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Aula> obtenerAulasHabilitadas() {
        try {
            TypedQuery<Aula> query = em.createQuery("SELECT a FROM Aula a WHERE a.habilitada = true", Aula.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
