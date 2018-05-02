package mx.itesm.buscaTech.busca_tech;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public ListaRVProdFrag() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public ListaRVProdFrag(String[] nombreProductos, String[] precio, Bitmap[] imagenes, String[] tiendas, String[] idPreferencias) {
        this.nombreProductos = nombreProductos;
        this.precio = precio;
        this.imagenes = imagenes;
        this.tiendas = tiendas;
        this.idPreferencias = idPreferencias;
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

        /*
        rvProductos = getActivity().findViewById(R.id.rvProductos);
        String[] nombreProductos = {"Acer 15.6","Asus Vivobook","Acer Aspire 7","Asus 15.6","MSI GL62M"};
        String[] precio = {"$14,499.00","$22,900.00","$24,000.00","$18,800.00","$16,600.00"};
        Bitmap bm1 = BitmapFactory.decodeResource(getResources(),R.drawable.comp_1);
        Bitmap bm2 = BitmapFactory.decodeResource(getResources(),R.drawable.comp_2);
        Bitmap bm3 = BitmapFactory.decodeResource(getResources(),R.drawable.comp_3);
        Bitmap bm4 = BitmapFactory.decodeResource(getResources(),R.drawable.comp_4);
        Bitmap bm5 = BitmapFactory.decodeResource(getResources(),R.drawable.comp_5);
        Bitmap[] imagenes = {bm1,bm2,bm3,bm4,bm5};
        String[] tiendas = {"Tienda A", "Tienda B", "Tienda C", "Tienda D" ,"Tienda E"};
        */

        AdaptadorRVProd adaptador = new AdaptadorRVProd(nombreProductos, precio, imagenes, tiendas, new ClickHandler() {
            @Override
            public void onMyButtonClicked(int position) {
                Log.i("WALUIGI",position+"");
                Toast.makeText(getContext(), "ANUMA", Toast.LENGTH_LONG).show();
                agregarFavorito(precio[position], nombreProductos[position], tiendas[position], "NO HAY IMAGEN CHAVO", "NO HAY DIRECCION CHAVO");
            }
            //public void agregarFavorito(String precio, String nombre, String tienda, String imagen, String direccion) {
        }
        , idPreferencias);
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

        if (idUsuario != "uNSCzUet0ZaCprSZrs2wXfDhnX22"){
            // No es invitado, no está en la cuenta de buscatechoficial
            Preferencias preferencias = new Preferencias(idPreferencia, idUsuario, precio, nombre, tienda, imagen, direccion);
            databasePreferences.child(idPreferencia).setValue(preferencias);

        }
        else {
            Toast.makeText(getContext(), "No puedes guardar favoritos como invitado.", Toast.LENGTH_LONG);

        }

    }


    public void eliminarFavorito(String idPreferencia) {
        databasePreferences.child(idPreferencia).removeValue();
        obtenerFavoritos();
    }


    private void obtenerFavoritos() {
        databasePreferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot preferenciaSnapshot : dataSnapshot.getChildren()){
                    Preferencias preferencias = preferenciaSnapshot.getValue(Preferencias.class);
                    if (preferencias.getIdUsuario().equals(mAuth.getCurrentUser().getUid())){
                        agregarMatriz(preferencias.precio, preferencias.nombre, preferencias.tienda, preferencias.imagen, preferencias.direccion, preferencias.idPreferencia);
                        Log.i("DATOS", preferencias.toString());
                        /*
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
                Log.i("Tamaño", matriz.get(0).size() + "");
                crearLista(matriz.get(0), matriz.get(1), matriz.get(2), matriz.get(3), matriz.get(4), matriz.get(5));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.i("TamLista", matriz.get(0).size() + "");
        for (int i = 0; i < matriz.get(0).size(); i++){
            Log.i("Lista", matriz.get(0).get(i) +
                    "\n" + matriz.get(1).get(i) +
                    "\n" + matriz.get(2).get(i) +
                    "\n" + matriz.get(3).get(i) +
                    "\n" + matriz.get(4).get(i));
        }

    }


    public void agregarMatriz(String precio, String nombre, String tienda, String imagen, String direccion, String idPreferencia){
        matriz.get(0).add(precio + " p " + matriz.get(0).size());
        matriz.get(1).add(nombre + " n " + matriz.get(0).size());
        matriz.get(2).add(tienda + " t " +  matriz.get(0).size());
        matriz.get(3).add(imagen + " i " +  matriz.get(0).size());
        matriz.get(4).add(direccion + " d " +  matriz.get(0).size());
        matriz.get(5).add(idPreferencia);

        /*
        matriz.get(0).add("Precio " + matriz.get(0).size());
        matriz.get(1).add("nombre " + matriz.get(0).size());
        matriz.get(2).add("tienda " + matriz.get(0).size());
        matriz.get(3).add("imagen " + matriz.get(0).size());
        matriz.get(4).add("direccion " + matriz.get(0).size());
        */


        // Log.i("Inner", inner.get(0) + "\n" + inner.get(1) + "\n" + inner.get(2) + "\n" + inner.get(3) + "\n" + inner.get(4));
        Log.i("AgregarM", matriz.get(0).get(0));

    }


    private void crearLista(ArrayList<String> preciosArr,
                            ArrayList<String> nombresArr,
                            ArrayList<String> tiendasArr,
                            ArrayList<String> imagenesArr,
                            ArrayList<String> direccionesArr,
                            ArrayList<String> preferenciasArr) {

        String[] precios = new String[preciosArr.size()];
        for (int i = 0; i < preciosArr.size(); i++){
            precios[i] = preciosArr.get(i);
        }

        String[] nombres = new String[nombresArr.size()];
        for (int i = 0; i < nombresArr.size(); i++){
            precios[i] = nombresArr.get(i);
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


        Bitmap bm1 = BitmapFactory.decodeResource(getResources(),R.drawable.comp_1);
        Bitmap[] imagenesBm = new Bitmap[imagenesArr.size()];
        for (int i = 0; i < imagenesArr.size(); i++){
            imagenesBm[i] = bm1;
        }

        ListaRVProdFrag fragLista = new ListaRVProdFrag(nombres, precios, imagenesBm, tiendas, idPreferencias);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutFavoritos, fragLista);
        transaction.commit();
    }



}
