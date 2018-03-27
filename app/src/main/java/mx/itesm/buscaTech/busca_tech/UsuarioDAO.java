package mx.itesm.buscaTech.busca_tech;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

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
}
