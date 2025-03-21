
package com.tpdisenio.gSM3C.dao;

import java.util.List;
import com.tpdisenio.gSM3C.domain.CicloLectivo;

public interface CicloLectivoDao {
    public CicloLectivo obtenerPorAnio(int anio);
    public List<CicloLectivo> obtenerTodos();
}