package mx.itesm.buscaTech.busca_tech;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by jsamu on 27/03/2018.
 */

@Database(entities = {Usuario.class}, version = 1)
public abstract class UsuarioBD extends RoomDatabase{

    private static UsuarioBD INSTANCE;

    public abstract UsuarioDAO usuarioDAO();

    public static UsuarioBD getInstance(Context contexto){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(contexto.getApplicationContext(),
                    UsuarioBD.class,
                    "baseDatosUsuario")
                    .build();
        }
        return INSTANCE;
    }
    public static void destroyInstance(){
        INSTANCE = null;
    }
}
