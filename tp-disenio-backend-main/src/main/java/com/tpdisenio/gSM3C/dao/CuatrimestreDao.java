package com.tpdisenio.gSM3C.dao;

import java.util.List;

import com.tpdisenio.gSM3C.domain.Cuatrimestre;

public interface CuatrimestreDao {

    public List<Cuatrimestre> buscarPorCicloLectivo(Integer cicloLectivo);

    public Cuatrimestre buscarPorId(String idCuatrimestre);
    
}
