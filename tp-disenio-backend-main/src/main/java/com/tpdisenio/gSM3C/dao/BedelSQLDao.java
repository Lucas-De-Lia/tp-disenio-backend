package com.tpdisenio.gSM3C.dao;

import com.tpdisenio.gSM3C.domain.Bedel;
import com.tpdisenio.gSM3C.enums.TurnoTrabajo;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository
public class BedelSQLDao extends DAO<Bedel> implements BedelDao {

    @PersistenceContext
    private EntityManager em;

    /**
     * Carga una lista de bedeles en la base de datos.
     * 
     * @param bedeles Lista de objetos Bedel a cargar.
     * 
     * Recorre la lista de bedeles proporcionada y guarda
     * cada bedel en la base de datos verificando primero si no existe.
     */
    @Override
    @Transactional
    public void cargarBedeles(List<Bedel> bedeles) {
        for (Bedel bedel : bedeles) {
            if(buscarPorUsername(bedel.getUsername()) == null){
                guardar(bedel);
                System.out.println("Bedel con nombre de usuario \"" + bedel.getUsername() + "\" cargado exitosamente.");
            }else {
                System.out.println("Bedel con nombre de usuario \"" + bedel.getUsername() + "\" ya existente (se omite).");
            }
        }
    }

    /**
     * Registra un nuevo bedel en la base de datos.
     *
     * @param bedel El objeto Bedel que se desea registrar.
     * @return El objeto Bedel registrado.
     */
    @Override
    public Bedel registrarBedel(Bedel bedel) {
        super.guardar(bedel);
        return bedel;
    }

    /**
     * Busca una lista de bedeles que coincidan con el apellido y turno
     * especificados.
     *
     * @param apellido El apellido del bedel a buscar.
     * @param turno    El turno del bedel a buscar.
     * @return Una lista de objetos Bedel que coincidan con el criterio de búsqueda.
     */
    @Override
    public List<Bedel> buscarBedeles(String apellido, String turno) {
        TurnoTrabajo turnoConvertido = null;
        if(turno != null) turnoConvertido = TurnoTrabajo.fromString(turno);
        
        List<Bedel> bedeles = em
                .createQuery(
                        "SELECT b FROM Bedel b WHERE b.habilitado = true AND (:apellido IS NULL OR b.apellido LIKE CONCAT(:apellido, '%')) AND (:turno IS NULL OR b.turno = :turno)",
                        Bedel.class)
                .setParameter("apellido", apellido)
                .setParameter("turno", turnoConvertido)
                .getResultList();
        
        return bedeles;
    }

    /**
     * Elimina un Bedel de la base de datos basado en su idUsuario.
     *
     * @param idUsuario El identificador del usuario del Bedel a eliminar.
     * @return true si el Bedel fue eliminado exitosamente, false en caso contrario.
     */
    @Override
    public Boolean eliminarBedel(String idUsuario) {
        boolean bandera = false;

        Bedel bedel = (Bedel) em.createQuery("SELECT b FROM Bedel b WHERE b.idUsuario = :idUsuario ")
                .setParameter("idUsuario", idUsuario).getSingleResult();

        super.eliminar(bedel);

        if (bedel != null) {
            bandera = true;
        }
        return bandera;
    }

    /**
     * Busca un objeto Bedel por su identificador.
     *
     * @param idUsuario El identificador del Bedel a buscar.
     * @return El objeto Bedel encontrado, o null si no se encuentra.
     */
    @Override
    public Bedel buscarPorId(String idUsuario) {
        Bedel bedel = (Bedel) em.find(Bedel.class, idUsuario);
        return bedel;
    }

    /**
     * Busca un objeto Bedel por su username.
     *
     * @param username El username del Bedel a buscar.
     * @return El objeto Bedel encontrado, o null si no se encuentra.
     */
    @Override
    public Bedel buscarPorUsername(String username) {
        try {
            return (Bedel) em.createQuery("SELECT b FROM Bedel b WHERE b.username = :username")
                .setParameter("username", username)
                .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Modifica la información de un Bedel existente en la base de datos.
     *
     * @param bedel El objeto Bedel con la información actualizada.
     * @return true si el Bedel fue modificado correctamente, false si no se
     *         encontró el Bedel.
     */
    @Override
    public Boolean modificarBedel(Bedel bedel) {
        em.merge(bedel); // Guardar el Bedel modificado
        return true;
    }

    @Override
    public List<Bedel> listarBedeles() {
        List<Bedel> bedeles = em
                .createQuery("SELECT b FROM Bedel b", Bedel.class).getResultList();
        return bedeles;
    }

    @Override
    public List<Bedel> listarBedelesHabilitados(){
        List<Bedel> bedeles = em.createQuery("SELECT b FROM Bedel b WHERE b.habilitado = true", Bedel.class).getResultList();
        return bedeles;
    }

}