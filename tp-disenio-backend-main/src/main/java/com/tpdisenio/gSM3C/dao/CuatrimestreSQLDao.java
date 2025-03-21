package com.tpdisenio.gSM3C.dao;

import java.util.List;
import org.springframework.stereotype.Repository;

import com.tpdisenio.gSM3C.domain.CicloLectivo;
import com.tpdisenio.gSM3C.domain.Cuatrimestre;

@Repository
public class CuatrimestreSQLDao extends DAO<Cuatrimestre> implements CuatrimestreDao  {
    @Override
    public List<Cuatrimestre> buscarPorCicloLectivo(Integer cicloLectivo) {
        // Buscar ciclo lectivo
        CicloLectivo ciclo = em.find(CicloLectivo.class, cicloLectivo);

        // Buscar cuatrimestres asociados al ciclo lectivo
        List<Cuatrimestre> cuatrimestres = em
            .createQuery("SELECT c FROM Cuatrimestre c WHERE c.cicloLectivo = :cicloLectivo", Cuatrimestre.class)
            .setParameter("cicloLectivo", ciclo)
            .getResultList();

        return cuatrimestres;
    }

    @Override
    public Cuatrimestre buscarPorId(String idCuatrimestre) {
        return em.find(Cuatrimestre.class, idCuatrimestre);
    }
}
