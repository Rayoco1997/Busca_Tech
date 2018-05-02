package mx.itesm.buscaTech.busca_tech;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MisPreferenciasActiv extends AppCompatActivity {

    DatabaseReference databasePreferences;
    FirebaseAuth mAuth;

    ArrayList<ArrayList<String>> matriz = new ArrayList<ArrayList<String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strMisPreferencias);
        setContentView(R.layout.activity_mis_preferencias);
        mAuth = FirebaseAuth.getInstance();
        databasePreferences = FirebaseDatabase.getInstance().getReference("preferencias");

        crearMatriz();
        // progressDialog
        // agregarPreferencia();
        obtenerFavoritos();

        // crearLista(matriz[0], matriz[1], matriz[2], matriz[3], matriz[4]);
        Log.i("Tamaño", matriz.get(0).size() + "");
        for (int i = 0; i < matriz.get(0).size(); i++){
            Log.i("Lista", matriz.get(0).get(i) +
                    "\n" + matriz.get(1).get(i) +
                    "\n" + matriz.get(2).get(i) +
                    "\n" + matriz.get(3).get(i) +
                    "\n" + matriz.get(4).get(i));
            }

    }


    public void onBackPressed() {
        super.onBackPressed();
        finish();
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


        Bitmap bm1 = BitmapFactory.decodeResource(getResources(),R.drawable.comp_1);
        Bitmap[] imagenesBm = new Bitmap[imagenesArr.size()];
        for (int i = 0; i < imagenesArr.size(); i++){
            imagenesBm[i] = bm1;
        }

        ListaRVProdFrag fragLista = new ListaRVProdFrag(nombres, precios, imagenesBm, tiendas, idPreferencias, 2, imagenes);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutFavoritos, fragLista);
        transaction.commit();
    }


    private void agregarPreferencia(){
        String idUsuario = mAuth.getCurrentUser().getUid();
        String precio = mAuth.getCurrentUser().getUid();
        String nombre = mAuth.getCurrentUser().getUid();
        String tienda = mAuth.getCurrentUser().getUid();
        String imagen = "http://icons.iconarchive.com/icons/marcus-roberto/google-play/512/Google-Chrome-icon.png";
        String direccion = "https://www.google.com/";

        String idPreferencia = databasePreferences.push().getKey();
        Preferencias preferencias = new Preferencias(idUsuario, precio, nombre, tienda, imagen, direccion);
        databasePreferences.child(idPreferencia).setValue(preferencias);

    }

    private void obtenerFavoritos() {
        databasePreferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot preferenciaSnapshot : dataSnapshot.getChildren()){
                    Preferencias preferencias = preferenciaSnapshot.getValue(Preferencias.class);
                    if (preferencias.getIdUsuario().equals(mAuth.getCurrentUser().getUid())){
                        agregarMatriz(preferencias.precio, preferencias.nombre, preferencias.tienda, preferencias.imagen, preferencias.direccion, preferenciaSnapshot.getKey());
                        Log.i("KEY", preferenciaSnapshot.getKey());
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
        matriz.get(0).add(precio);
        matriz.get(1).add(nombre);
        matriz.get(2).add(tienda);
        matriz.get(3).add(imagen);
        matriz.get(4).add(direccion);
        matriz.get(5).add(idPreferencia);


        // Log.i("Inner", inner.get(0) + "\n" + inner.get(1) + "\n" + inner.get(2) + "\n" + inner.get(3) + "\n" + inner.get(4));
        Log.i("AgregarM", matriz.get(0).get(0));

    }


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


}
