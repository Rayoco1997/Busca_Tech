package mx.itesm.buscaTech.busca_tech;

/**
 * Created by jsamu on 24/04/2018.
 */

public class Preferencias {
    String idPreferencia;
    String idUsuario;
    String precio;
    String nombre;
    String tienda;


    public Preferencias(){

    }


    public Preferencias(String idPreferencia, String idUsuario, String precio, String nombre, String tienda) {
        this.idPreferencia= idPreferencia;
        this.idUsuario = idUsuario;
        this.precio = precio;
        this.nombre = nombre;
        this.tienda = tienda;
    }


    public String getIdPreferencia() {
        return idPreferencia;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getPrecio() {
        return precio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTienda() {
        return tienda;
    }

    @Override
    public String toString() {
        return "Id Usuario: " + getIdUsuario() + "\nPrecio: " + getPrecio() + "\nNombre: " + getNombre() + "\nTienda: " + getTienda();
    }
}
