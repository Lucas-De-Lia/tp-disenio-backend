package com.tpdisenio.gSM3C.domain;

import com.tpdisenio.gSM3C.enums.TurnoTrabajo;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bedeles")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Bedel extends Usuario {
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private String idUsuario;
    
    @Enumerated(EnumType.STRING)
    private TurnoTrabajo turno;

    @ManyToOne
    @JoinColumn(name = "registrado_por", referencedColumnName = "idUsuario")
    private Administrador registradoPor;

    // Constructor sin administrador
    public Bedel(String username, String nombre, String apellido, String password, boolean habilitado, TurnoTrabajo turno) {
        super.setUsername(username);
        super.setNombre(nombre);
        super.setApellido(apellido);
        super.setPassword(password);
        super.setHabilitado(habilitado);
        this.turno = turno;
    }

    // Constructor con administrador
    public Bedel(String username, String nombre, String apellido, String password, boolean habilitado, TurnoTrabajo turno, Administrador registradoPor) {
        super.setUsername(username);
        super.setNombre(nombre);
        super.setApellido(apellido);
        super.setPassword(password);
        super.setHabilitado(habilitado);
        this.turno = turno;
        this.registradoPor = registradoPor;
    }

    public TurnoTrabajo getTurno() {
        return turno;
    }

    public void setTurno(TurnoTrabajo turno) {
        this.turno = turno;
    }

    public Administrador getRegistradoPor() {
        return registradoPor;
    }

    public void setRegistradoPor(Administrador registradoPor) {
        this.registradoPor = registradoPor;
    }

    public String getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(String id) {
        this.idUsuario = id;
    }
}