package com.tpdisenio.gSM3C.dao;

import java.util.List;
import com.tpdisenio.gSM3C.domain.Bedel;


public interface BedelDao{

    public Bedel registrarBedel(Bedel bedel);

    public List<Bedel> buscarBedeles(String apellido, String turno);
    
    public Boolean eliminarBedel(String idUsuario);
    
    public Boolean modificarBedel(Bedel bedel);

    public Bedel buscarPorId(String idUsuario);

    public Bedel buscarPorUsername(String username);

    public void cargarBedeles(List<Bedel> bedeles);

    public List<Bedel> listarBedeles();

    public List<Bedel> listarBedelesHabilitados();
}