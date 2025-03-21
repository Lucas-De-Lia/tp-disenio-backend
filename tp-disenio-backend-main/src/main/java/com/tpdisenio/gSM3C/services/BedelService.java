package com.tpdisenio.gSM3C.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.tpdisenio.gSM3C.dao.BedelDao;
import com.tpdisenio.gSM3C.domain.Administrador;
import com.tpdisenio.gSM3C.domain.Bedel;
import com.tpdisenio.gSM3C.dto.BedelDTO;
import com.tpdisenio.gSM3C.dto.BuscarBedelDTO;
import com.tpdisenio.gSM3C.enums.TurnoTrabajo;
import com.tpdisenio.gSM3C.exception.AdministradorNotFoundException;
import com.tpdisenio.gSM3C.exception.BedelNotFoundException;
import com.tpdisenio.gSM3C.exception.PasswordMismatchException;
import com.tpdisenio.gSM3C.exception.UsuarioAlreadyExistsException;


import org.springframework.transaction.annotation.Transactional;

@Service
public class BedelService {

    private final BedelDao dao;
    private final AdministradorService adminService;
    private final UsuarioService usuarioService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public BedelService(BedelDao dao, AdministradorService adminService, UsuarioService usuarioService) {
        this.dao = dao;
        this.adminService = adminService;
        this.usuarioService = usuarioService;
    }
    
    @Transactional
    public Bedel registrarBedel(BedelDTO bedelDTO) {
        // Validar los datos del DTO
        validarDatosRegistrarBedel(bedelDTO);

        // Validar si ya existe un usuario con el mismo nombre de usuario
        if (usuarioService.usernameExists(bedelDTO.getUsername())) {
            throw new UsuarioAlreadyExistsException(
                    "Ya existe un usuario con el nombre de usuario: " + bedelDTO.getUsername());
        }

        // Encriptar la contraseña
        String encryptedPassword = passwordEncoder.encode(bedelDTO.getPassword());

        // Crear Bedel a partir de los datos del DTO
        Bedel bedel = new Bedel(
                bedelDTO.getUsername(),
                bedelDTO.getNombre(),
                bedelDTO.getApellido(),
                encryptedPassword,
                true,
                TurnoTrabajo.fromString(bedelDTO.getTurno()));

        // Recuperar el administrador que registró al bedel
        String identificadorAdmin = bedelDTO.getRegistradoPor();
        Administrador admin = adminService.buscarAdministradorPorIDoUsername(identificadorAdmin);
        if (admin == null) {
            throw new AdministradorNotFoundException("El identificador de quien registra al bedel ('"
                    + identificadorAdmin + "') no corresponde a ningún administrador existente.");
        }

        // Asignar el administrador al bedel
        bedel.setRegistradoPor(admin);

        // Registrar Bedel en la base de datos
        return dao.registrarBedel(bedel);
    }

    public List<Bedel> buscarBedeles(BuscarBedelDTO buscarBedelDTO) {
        validarDatosBusqueda(buscarBedelDTO);
        return dao.buscarBedeles(buscarBedelDTO.getApellido(), buscarBedelDTO.getTurno());
    }

    @Transactional
    public boolean eliminarBedel(String idUsuario) {
        Bedel bedel = dao.buscarPorId(idUsuario);

        if (bedel == null || !bedel.isHabilitado())
            throw new BedelNotFoundException("El Bedel con ID " + idUsuario + " no existe.");

        bedel.setHabilitado(false);

        return dao.modificarBedel(bedel);
    }

    @Transactional
    public boolean modificarBedel(BedelDTO bedelDTO) {
        // Validar los datos del DTO
        validarDatosModificarBedel(bedelDTO);

        Bedel bedel = dao.buscarPorId(bedelDTO.getIdUsuario());

        // Verificar si el Bedel existe
        if (bedel == null)
            throw new BedelNotFoundException("El Bedel con ID " + bedelDTO.getIdUsuario() + " no existe.");

        // Actualizar los campos del Bedel existente
        bedel.setNombre(bedelDTO.getNombre());
        bedel.setApellido(bedelDTO.getApellido());
        bedel.setTurno(TurnoTrabajo.fromString(bedelDTO.getTurno()));
        if(bedelDTO.getPassword() != null) {
            String encryptedPassword = passwordEncoder.encode(bedelDTO.getPassword());
            bedel.setPassword(encryptedPassword);
        }

        // Modificar Bedel en la base de datos
        return dao.modificarBedel(bedel);
    }

    /**
     * Valida los datos del BedelDTO.
     * Nota: Las validaciones de los campos del DTO, se realizan con las anotaciones
     * de la clase BedelDTO.
     *
     * @param bedelDTO el objeto BedelDTO que contiene los datos a validar.
     * @throws PasswordMismatchException si las contraseñas no coinciden.
     */
    public boolean esUsernameValido(String username) {
        return username != null && username.matches("^[a-zA-Z0-9_]+$");
    }

    public boolean esValido(String nombre) {
        return nombre.matches("^[a-zA-Z]+$");
    }
    private void validarDatosRegistrarBedel(BedelDTO bedelDTO) {
        if(!esUsernameValido(bedelDTO.getUsername())){
             throw new IllegalArgumentException("El nombre de usuario no puede contener caracteres especiales.");
        }
        if(!esValido(bedelDTO.getApellido())){
             throw new IllegalArgumentException("El apellido del usuario sólo puede contener letras mayúsculas y minúsculas.");
        }
        if(!esValido(bedelDTO.getNombre())){
            throw new IllegalArgumentException("El nombre del usuario sólo puede contener letras mayúsculas y minúsculas.");
        }
        
        if (bedelDTO.getUsername() == null) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }
        
        // Validar que las contraseñas hayan sido ingresadas
        if(bedelDTO.getPassword() == null || bedelDTO.getRepeatedPassword() == null) {
            throw new IllegalArgumentException("Tanto la contraseña como la contraseña repetida son obligatorias.");
        }
        
        // Validar que las contraseñas coincidan
        if (!bedelDTO.getPassword().equals(bedelDTO.getRepeatedPassword())) {
            throw new PasswordMismatchException("Las contraseñas ingresadas no coinciden.");
        }
        
        // Validar Turno
        TurnoTrabajo.validarTurno(bedelDTO.getTurno());

        // Validar que el identificiador de quien registra al bedel haya sido ingresado
        if (bedelDTO.getRegistradoPor() == null) {
            throw new IllegalArgumentException("El identificador de quien registra al bedel es obligatorio.");
        }
    }

    /**
     * Valida los datos del BedelDTO.
     * Nota: Las validaciones de los campos del DTO, se realizan con las anotaciones
     * de la clase BedelDTO.
     *
     * @param bedelDTO el objeto BedelDTO que contiene los datos a validar.
     * @throws PasswordMismatchException si las contraseñas no coinciden.
     */
    private void validarDatosModificarBedel(BedelDTO bedelDTO) {
        if(!esUsernameValido(bedelDTO.getUsername())){
             throw new IllegalArgumentException("El nombre de usuario no puede contener caracteres especiales.");
        }
        if(!esValido(bedelDTO.getApellido())){
             throw new IllegalArgumentException("El apellido del usuario sólo puede contener letras mayúsculas y minúsculas.");
        }
        if(!esValido(bedelDTO.getNombre())){
            throw new IllegalArgumentException("El nombre del usuario sólo puede contener letras mayúsculas y minúsculas.");
        }
        // Validar que el ID de usuario haya sido ingresado
        if (bedelDTO.getIdUsuario() == null) {
            throw new IllegalArgumentException("El ID del usuario a modificar es obligatorio.");
        }
        
        

        // Validar contraseñas (en caso de ser ingresadas)
        if (bedelDTO.getPassword() != null || bedelDTO.getRepeatedPassword() != null) {
            // Validar que ambas contraseñas hayan sido ingresadas
            if(bedelDTO.getPassword() == null || bedelDTO.getRepeatedPassword() == null) {
                throw new IllegalArgumentException("Si desea modificar la contraseña, tanto la contraseña como la contraseña repetida deben ser ingresadas.");
            }

            // Validar que las contraseñas coincidan
            if (!bedelDTO.getPassword().equals(bedelDTO.getRepeatedPassword())) {
                throw new PasswordMismatchException("Las contraseñas ingresadas no coinciden.");
            }
        }

        // Validar Turno
        TurnoTrabajo.validarTurno(bedelDTO.getTurno());
    }

    
    
    /**
     * Valida si los datos de búsqueda son válidos.
     * Lanza excepciones si no se especifica un criterio de búsqueda o si se buscan
     * por ambos criterios a la vez. También lanza excepción si el turno no es
     * válido.
     * 
     * @param buscarBedelDTO DTO con los datos de búsqueda del Bedel
     */
    private void validarDatosBusqueda(BuscarBedelDTO buscarBedelDTO) {
        String apellido = buscarBedelDTO.getApellido();
        String turno = buscarBedelDTO.getTurno();

        // Validamos haya almenos un criterio de busqueda
        if (apellido == null && turno == null)
            throw new IllegalArgumentException("Debe especificar un criterio de búsqueda (apellido o turno).");

        // Validamos que no se busque por ambos criterios a la vez
        if (apellido != null && turno != null)
            throw new IllegalArgumentException("No se puede buscar por ambos criterios a la vez.");

        // Validamos que turno sea válido
        if (turno != null)
            TurnoTrabajo.validarTurno(turno);
    }

    public Bedel buscarBedelPorId(String id) {
        Bedel bedel = dao.buscarPorId(id);
        if (bedel == null){
            throw new BedelNotFoundException("El Bedel con ID " + id + " no existe.");
        }
        return bedel;
    }

    public List<Bedel> listarBedeles() {
        return dao.listarBedeles();
    }

    public List<Bedel> listarBedelesHabilitados(){return dao.listarBedelesHabilitados();}

}
