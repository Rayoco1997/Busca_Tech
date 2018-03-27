package mx.itesm.buscaTech.busca_tech;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

/**
 * Created by jsamu on 27/03/2018.
 */

@Entity(tableName = "Usuario")
public class Usuario {
    @PrimaryKey
    private String correo;

    @ColumnInfo(name = "nombreUsuario")
    private String nombreUsuario;

    @ColumnInfo(name = "contrasena")
    private String contrasena;

    @ColumnInfo(name = "imagen")
    private Bitmap imagen;
}
