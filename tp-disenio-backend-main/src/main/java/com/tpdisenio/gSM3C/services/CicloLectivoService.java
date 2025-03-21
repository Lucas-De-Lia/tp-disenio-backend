
package com.tpdisenio.gSM3C.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.tpdisenio.gSM3C.dao.CicloLectivoDao;
import com.tpdisenio.gSM3C.domain.CicloLectivo;

@Service
public class CicloLectivoService {

    @Autowired
    @Qualifier("cicloLectivoSQLDao")
    private CicloLectivoDao cicloLectivoDao;

    public CicloLectivo obtenerCicloLectivoPorAnio(int anio) {
        return cicloLectivoDao.obtenerPorAnio(anio);
    }

    public List<CicloLectivo> obtenerCiclosLectivos() {
        return cicloLectivoDao.obtenerTodos();
    }
}