
package com.tpdisenio.gSM3C.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import jakarta.persistence.TypedQuery;
import com.tpdisenio.gSM3C.domain.CicloLectivo;

@Repository
public class CicloLectivoSQLDao extends DAO<CicloLectivo>implements CicloLectivoDao {

    @Override
    public CicloLectivo obtenerPorAnio(int anio) {
        try {
            String jpql = "SELECT c FROM CicloLectivo c WHERE c.anio = :anio";
            TypedQuery<CicloLectivo> query = em.createQuery(jpql, CicloLectivo.class);
            query.setParameter("anio", anio);
            return query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<CicloLectivo> obtenerTodos() {
        String jpql = "SELECT c FROM CicloLectivo c";
        TypedQuery<CicloLectivo> query = em.createQuery(jpql, CicloLectivo.class);
        return query.getResultList();
    }
}