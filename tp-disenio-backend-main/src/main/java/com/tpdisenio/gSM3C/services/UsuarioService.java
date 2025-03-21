
package com.tpdisenio.gSM3C.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tpdisenio.gSM3C.dao.BedelDao;
import com.tpdisenio.gSM3C.dao.AdministradorDao;
import com.tpdisenio.gSM3C.domain.Administrador;
import com.tpdisenio.gSM3C.domain.Bedel;
import com.tpdisenio.gSM3C.domain.Usuario;
import com.tpdisenio.gSM3C.dto.BedelDTO;
import com.tpdisenio.gSM3C.exception.UsuarioNotFoundException;
import com.tpdisenio.gSM3C.exception.PasswordMismatchException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UsuarioService implements UserDetailsService{

    @Autowired
    @Qualifier("bedelSQLDao")
    private BedelDao bedelDao;

    @Autowired
    @Qualifier("administradorSQLDao")
    private AdministradorDao administradorDao;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Inicia sesión de un usuario con el nombre de usuario y contraseña proporcionados.
     *
     * @param username El nombre de usuario del usuario que intenta iniciar sesión.
     * @param password La contraseña sin cifrar del usuario que intenta iniciar sesión.
     * @return El objeto Usuario si el inicio de sesión es exitoso.
     * @throws UsuarioNotFoundException Si no se encuentra un usuario con el nombre de usuario proporcionado.
     * @throws PasswordMismatchException Si la contraseña proporcionada no coincide con la contraseña almacenada.
     */
    public Usuario autenticarUsuario(String username, String password) {
        // Buscar el usuario por nombre de usuario
        Usuario usuario = buscarPorUsername(username);
        if (usuario == null) {
            throw new UsuarioNotFoundException("Usuario no encontrado");
        }

        // Verificar la contraseña
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new PasswordMismatchException("Contraseña incorrecta");
        }

        // Retornar el usuario si la contraseña es correcta
        return usuario;
    }

    /**
     * Determinar si existe un usuario en cualquiera de las tablas.
     * @param username
     * @return True si existe el usuario. False en caso contrario.
     */
    public boolean usernameExists(String username) {
        return bedelDao.buscarPorUsername(username) != null || administradorDao.buscarPorUsername(username) != null;
    }

    /**
     * Buscar en ambas tablas (administradores y bedeles) el usuario de acuerdo al username.
     * @param username
     * @return
     */
    public Usuario buscarPorUsername(String username){
        Usuario usuario;
        
        usuario = bedelDao.buscarPorUsername(username);
        
        if (usuario == null) {
            usuario = administradorDao.buscarPorUsername(username);
        }

        return usuario;
    }
    public BedelDTO buscarPorUser(String username){
        Bedel bedel;
        Administrador admin;
        BedelDTO bedeldto=new BedelDTO();
        
        bedel = bedelDao.buscarPorUsername(username);
        
        if (bedel == null) {
            admin = administradorDao.buscarPorUsername(username);
            bedeldto.setTurno(null);
            bedeldto.setIdUsuario(admin.getIdUsuario());
        }else{
            bedeldto.setTurno(bedel.getTurno().toString());
            bedeldto.setIdUsuario(bedel.getIdUsuario());
        }

        
        
        

        return bedeldto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = buscarPorUsername(username);

    // Verificar si el usuario existe
    if (usuario == null) {
        throw new UsernameNotFoundException("Usuario no encontrado: " + username);
    }

    // Crear un objeto UserDetails usando los datos del usuario
    return User.builder()
            .username(usuario.getUsername())
            .password(usuario.getPassword())// Contraseña cifrada
            .build();
    }

}
