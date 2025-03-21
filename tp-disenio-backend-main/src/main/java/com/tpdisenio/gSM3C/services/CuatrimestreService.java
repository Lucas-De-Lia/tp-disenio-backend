package com.tpdisenio.gSM3C.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.tpdisenio.gSM3C.dao.CuatrimestreDao;
import com.tpdisenio.gSM3C.domain.Cuatrimestre;
import com.tpdisenio.gSM3C.exception.CuatrimestreNotFoundException;

import java.util.List;

@Service
public class CuatrimestreService {

    @Autowired
    @Qualifier("cuatrimestreSQLDao")
    private CuatrimestreDao cuatrimestreDao;

    public List<Cuatrimestre> buscarPorCicloLectivo(Integer anioCicloLectivo) {
        if(anioCicloLectivo == null) {
            throw new IllegalArgumentException("El año del ciclo lectivo no puede ser nulo.");
        }
        if(anioCicloLectivo <= 0) {
            throw new IllegalArgumentException("El año del ciclo lectivo debe ser mayor a cero.");
        }
        List<Cuatrimestre> cuatrimestres = cuatrimestreDao.buscarPorCicloLectivo(anioCicloLectivo);
        if (cuatrimestres.isEmpty()) {
            throw new CuatrimestreNotFoundException("No se encontraron cuatrimestres para el ciclo lectivo " + anioCicloLectivo + ".");
        }
        return cuatrimestres;
    }

    public Cuatrimestre buscarPorId(String idCuatrimestre) {
        Cuatrimestre cuatrimestre = cuatrimestreDao.buscarPorId(idCuatrimestre);
        if (cuatrimestre == null)
            throw new RuntimeException("Cuatrimestre not found with id: " + idCuatrimestre);

        return cuatrimestre;
    }

    public Cuatrimestre obtenerPrimerCuatrimestre(Integer cicloLectivo) {
        List<Cuatrimestre> cuatrimestres = buscarPorCicloLectivo(cicloLectivo);
        cuatrimestres.sort((c1, c2) -> c1.getFechaInicio().compareTo(c2.getFechaInicio()));
        if (!cuatrimestres.isEmpty()) {
            return cuatrimestres.get(0);
        } else {
            throw new RuntimeException("No se encontraron cuatrimestres para el ciclo lectivo: " + cicloLectivo);
        }
    }

    public Cuatrimestre obtenerSegundoCuatrimestre(Integer cicloLectivo) {
        List<Cuatrimestre> cuatrimestres = buscarPorCicloLectivo(cicloLectivo);
        cuatrimestres.sort((c1, c2) -> c1.getFechaInicio().compareTo(c2.getFechaInicio()));
        if (cuatrimestres.size() > 1) {
            return cuatrimestres.get(1);
        } else {
            throw new RuntimeException("No se encontraron cuatrimestres para el ciclo lectivo: " + cicloLectivo);
        }
    }
}
