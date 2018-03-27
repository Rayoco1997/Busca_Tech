package mx.itesm.buscaTech.busca_tech;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by jsamu on 27/03/2018.
 */

@Entity(tableName = "Usuario")
public class Usuario {
    @PrimaryKey
    @NonNull
    private String correo;

    @ColumnInfo(name = "nombreUsuario")
    private String nombreUsuario;

    @ColumnInfo(name = "contrasena")
    private String contrasena;

    @ColumnInfo(name = "imagen")
    private byte[] imagen;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}
