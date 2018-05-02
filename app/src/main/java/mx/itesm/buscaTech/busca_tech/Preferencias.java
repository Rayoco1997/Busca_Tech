package mx.itesm.buscaTech.busca_tech;

/**
 * Created by jsamu on 24/04/2018.
 */

public class Preferencias {
    String idUsuario;
    String precio;
    String nombre;
    String tienda;
    String imagen;
    String direccion;


    public Preferencias(){

    }


    public Preferencias(String idUsuario, String precio, String nombre, String tienda, String imagen, String direccion) {
        this.idUsuario = idUsuario;
        this.precio = precio;
        this.nombre = nombre;
        this.tienda = tienda;
        this.imagen = imagen;
        this.direccion = direccion;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getPrecio() {return precio;}

    public String getNombre() {
        return nombre;
    }

    public String getTienda() {
        return tienda;
    }

    public String getDireccion() {return direccion;}

    public String getImagen() {return imagen;}

    @Override
    public String toString() {
        return "Id Usuario: " + getIdUsuario() + "\nPrecio: " + getPrecio() + "\nNombre: " + getNombre() + "\nTienda: " + getTienda();
    }
}
