package mx.itesm.buscaTech.busca_tech;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

/**
 * Created by jsamu on 27/03/2018.
 */

@Dao
public interface UsuarioDAO {
    @Query("SELECT * FROM usuario WHERE correo = :correo")
    Usuario buscarPorCorreo(String correo);

    @Insert
    void insertar(Usuario... usuarios);

    @Query("DELETE FROM usuario")
    void BorrarTodo();

    @Query("UPDATE usuario SET nombreUsuario = :nombreUsuarioNuevo WHERE correo = :correo")
    void actualizarNombreUsuario(String nombreUsuarioNuevo, String correo);

    @Query("UPDATE usuario SET contrasena = :contrasenaNueva WHERE correo = :correo")
    void actualizarContrasena(String contrasenaNueva, String correo);
}
