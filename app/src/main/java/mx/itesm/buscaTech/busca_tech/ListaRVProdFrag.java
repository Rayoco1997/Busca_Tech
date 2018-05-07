package mx.itesm.buscaTech.busca_tech;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListaRVProdFrag extends Fragment {

    // Base de datos
    DatabaseReference databasePreferences;
    FirebaseAuth mAuth;
    ArrayList<ArrayList<String>> matriz = new ArrayList<ArrayList<String>>();

    private RecyclerView rvProductos;
    String[] nombreProductos;
    String[] precio;
    Bitmap[] imagenes;
    String[] tiendas;
    String[] idPreferencias;
    // 0 es buscarProducto
    // 1 es pantallaPrincipal
    // 2 es misPreferencias
    int agregar;
    String[] strImagenes;

    public ListaRVProdFrag() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ListaRVProdFrag(String[] nombreProductos, String[] precio, String[] tiendas, String[] idPreferencias, int agregar, String[] strImagenes) {
        this.nombreProductos = nombreProductos;
        this.precio = precio;
        this.imagenes = imagenes;
        this.tiendas = tiendas;
        this.idPreferencias = idPreferencias;
        this.agregar = agregar;
        this.strImagenes = strImagenes;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        databasePreferences = FirebaseDatabase.getInstance().getReference("preferencias");
        crearMatriz();

    }

    @Override
    public void onStart() {
        super.onStart();

        rvProductos = getActivity().findViewById(R.id.rvProductos);

        AdaptadorRVProd adaptador = new AdaptadorRVProd(nombreProductos, precio, tiendas, new ClickHandler() {
            @Override
            public void onMyButtonClicked(int position) {
                if (agregar == 0){
                    // agregarFavorito(precio[position], nombreProductos[position], tiendas[position], "NO HAY IMAGEN CHAVO", "NO HAY DIRECCION CHAVO");
                    yaExisteBusqueda("No hay direccion", strImagenes[position], nombreProductos[position], precio[position], tiendas[position]);

                }
                else if (agregar == 1){
                    yaExiste("No hay direccion", strImagenes[position], nombreProductos[position], precio[position], tiendas[position]);
                }
                else if (agregar == 2){
                    eliminarFavorito(idPreferencias[position]);

                }

            }
        }
        , idPreferencias, strImagenes, agregar);
        rvProductos.setAdapter(adaptador);
        rvProductos.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_rv_prod, container, false);
    }




    // BASE DE DATOS
    public void crearMatriz() {
        ArrayList<String> precio = new ArrayList<String>();
        ArrayList<String> nombre = new ArrayList<String>();
        ArrayList<String> tienda = new ArrayList<String>();
        ArrayList<String> imagen = new ArrayList<String>();
        ArrayList<String> direccion = new ArrayList<String>();
        ArrayList<String> idPreferencia = new ArrayList<String>();
        matriz.add(precio);
        matriz.add(nombre);
        matriz.add(tienda);
        matriz.add(imagen);
        matriz.add(direccion);
        matriz.add(idPreferencia);
    }







    public void agregarFavorito(String precio, String nombre, String tienda, String imagen, String direccion) {

        String idUsuario = mAuth.getCurrentUser().getUid();
        String idPreferencia = databasePreferences.push().getKey();

        if (!idUsuario.equals("uNSCzUet0ZaCprSZrs2wXfDhnX22")){
            if (nombre.equals("NO SE ENCONTRARON RESULTADOS")) {

            } else {
                // No es invitado, no está en la cuenta de buscatechoficial
                Preferencias preferencias = new Preferencias(idUsuario, precio, nombre, tienda, imagen, direccion);
                databasePreferences.child(idPreferencia).setValue(preferencias);
                Toast.makeText(getContext(), "Guardado a favoritos", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Toast.makeText(getContext(), "No puedes guardar favoritos como invitado.", Toast.LENGTH_LONG).show();

        }

    }


    private void mostrarDialogo(String mensaje) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
        builder.setTitle("AVISO");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Click
                //Info que tiene que pasar
            }
        });
        builder.show();

    }


    public void eliminarFavorito(String idPreferencia) {
        databasePreferences.child(idPreferencia).removeValue();
        Toast.makeText(getContext(), "Eliminado de favoritos", Toast.LENGTH_LONG).show();
        obtenerFavoritos();
    }


    private void obtenerFavoritos() {
        databasePreferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot preferenciaSnapshot : dataSnapshot.getChildren()){
                    Preferencias preferencias = preferenciaSnapshot.getValue(Preferencias.class);
                    if (preferencias.getIdUsuario().equals(mAuth.getCurrentUser().getUid())){
                        agregarMatriz(preferencias.precio, preferencias.nombre, preferencias.tienda, preferencias.imagen, preferencias.direccion, preferenciaSnapshot.getKey());
                        /*
                        // Checar datos que de la matriz
                        Log.i("KEY", preferenciaSnapshot.getKey());
                        Log.i("DATOS", preferencias.toString());
                        Log.i("Length", matriz.size() + "");
                        for (int i = 0; i < matriz.size(); i++){
                            Log.i("Lista", matriz.get(i).get(0) +
                                    "\n" + matriz.get(i).get(1) +
                                    "\n" + matriz.get(i).get(2) +
                                    "\n" + matriz.get(i).get(3) +
                                    "\n" + matriz.get(i).get(4));
                        }
                        */
                    }
                }
                /*
                // Checar dirección de la imagen
                Log.i("Tamaño", matriz.get(0).size() + "");
                for (int i = 0; i < matriz.get(3).size(); i++){
                    Log.i("DirIma", matriz.get(3).get(i)+"");
                }
                */
                crearLista(matriz.get(0), matriz.get(1), matriz.get(2), matriz.get(3), matriz.get(4), matriz.get(5));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*
        // Tamaño total de la lista
        Log.i("TamLista", matriz.get(0).size() + "");
        for (int i = 0; i < matriz.get(0).size(); i++){
            Log.i("Lista", matriz.get(0).get(i) +
                    "\n" + matriz.get(1).get(i) +
                    "\n" + matriz.get(2).get(i) +
                    "\n" + matriz.get(3).get(i) +
                    "\n" + matriz.get(4).get(i));
        }
        */
    }


    public void agregarMatriz(String precio, String nombre, String tienda, String imagen, String direccion, String idPreferencia){
        matriz.get(0).add(precio);
        matriz.get(1).add(nombre);
        matriz.get(2).add(tienda);
        matriz.get(3).add(imagen);
        matriz.get(4).add(direccion);
        matriz.get(5).add(idPreferencia);
    }


    private void crearLista(ArrayList<String> preciosArr,
                            ArrayList<String> nombresArr,
                            ArrayList<String> tiendasArr,
                            ArrayList<String> imagenesArr,
                            ArrayList<String> direccionesArr,
                            ArrayList<String> preferenciasArr) {

        if(matriz.get(0).size() == 0){
            mostrarDialogo("No tienes guardado ningún producto.");
        }

        String[] precios = new String[preciosArr.size()];
        for (int i = 0; i < preciosArr.size(); i++){
            precios[i] = preciosArr.get(i);
        }

        String[] nombres = new String[nombresArr.size()];
        for (int i = 0; i < nombresArr.size(); i++){
            nombres[i] = nombresArr.get(i);
        }

        String[] tiendas = new String[tiendasArr.size()];
        for (int i = 0; i < tiendasArr.size(); i++){
            tiendas[i] = tiendasArr.get(i);
        }

        String[] imagenes = new String[imagenesArr.size()];
        for (int i = 0; i < imagenesArr.size(); i++){
            imagenes[i] = imagenesArr.get(i);
        }

        String[] direcciones = new String[direccionesArr.size()];
        for (int i = 0; i < direccionesArr.size(); i++){
            direcciones[i] = direccionesArr.get(i);
        }

        String[] idPreferencias = new String[preferenciasArr.size()];
        for (int i = 0; i < preferenciasArr.size(); i++){
            idPreferencias[i] = preferenciasArr.get(i);
        }


        Bitmap[] imagenesBm = new Bitmap[imagenesArr.size()];

        ListaRVProdFrag fragLista = new ListaRVProdFrag(nombres, precios, tiendas, idPreferencias, 2, imagenes);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutFavoritos, fragLista);
        transaction.commit();
    }



    private void yaExiste(final String direccion, final String imagen, final String nombre, final String precio, final String tienda) {
        final String idUsuario = mAuth.getCurrentUser().getUid();
        final TextView tvBoolean = (TextView) getActivity().findViewById(R.id.tvBoolean);
        tvBoolean.setText("false");
        databasePreferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvBoolean.setText("false");
                for (DataSnapshot preferenciaSnapshot : dataSnapshot.getChildren()){
                    Preferencias preferencias = preferenciaSnapshot.getValue(Preferencias.class);
                    if (preferencias.direccion.equals(direccion)
                            && preferencias.idUsuario.equals(idUsuario)
                            && preferencias.imagen.equals(imagen)
                            && preferencias.nombre.equals(nombre)
                            && preferencias.precio.equals(precio)
                            && preferencias.tienda.equals(tienda)){
                        tvBoolean.setText("true");
                    }
                }

                if (tvBoolean.getText().toString().equals("true")){
                    Toast.makeText(getContext(), "Ya guardaste este producto", Toast.LENGTH_SHORT).show();
                } else {
                    agregarFavorito(precio, nombre, tienda, imagen, direccion);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void yaExisteBusqueda(final String direccion, final String imagen, final String nombre, final String precio, final String tienda) {
        final String idUsuario = mAuth.getCurrentUser().getUid();
        final TextView tvBoolean = (TextView) getActivity().findViewById(R.id.tvBooleanProd);
        tvBoolean.setText("false");
        databasePreferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvBoolean.setText("false");
                for (DataSnapshot preferenciaSnapshot : dataSnapshot.getChildren()){
                    Preferencias preferencias = preferenciaSnapshot.getValue(Preferencias.class);
                    if (preferencias.direccion.equals(direccion)
                            && preferencias.idUsuario.equals(idUsuario)
                            && preferencias.imagen.equals(imagen)
                            && preferencias.nombre.equals(nombre)
                            && preferencias.precio.equals(precio)
                            && preferencias.tienda.equals(tienda)){
                        tvBoolean.setText("true");
                    }
                }

                if (tvBoolean.getText().toString().equals("true")){
                    Toast.makeText(getContext(), "Ya guardaste este producto", Toast.LENGTH_SHORT).show();
                } else {
                    agregarFavorito(precio, nombre, tienda, imagen, direccion);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
