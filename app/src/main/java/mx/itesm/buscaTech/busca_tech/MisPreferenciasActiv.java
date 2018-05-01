package mx.itesm.buscaTech.busca_tech;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MisPreferenciasActiv extends AppCompatActivity {

    DatabaseReference databasePreferences;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strMisPreferencias);
        setContentView(R.layout.activity_mis_preferencias);
        mAuth = FirebaseAuth.getInstance();
        databasePreferences = FirebaseDatabase.getInstance().getReference("preferencias");


        // agregarPreferencia();
        obtenerFavoritos();

        // crearLista();

    }


    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void crearLista(String[] precios, String[] nombres, String[] tiendas, String[] imagenes, String[] direcciones) {
        ListaRVProdFrag fragLista = new ListaRVProdFrag();
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
        Preferencias preferencias = new Preferencias(idPreferencia, idUsuario, precio, nombre, tienda, imagen, direccion);
        databasePreferences.child(idPreferencia).setValue(preferencias);

    }


    private String[][] obtenerFavoritos(){
        final String [][] matriz = new String[10][5];
        final int counter = 0;
        databasePreferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot preferenciaSnapshot : dataSnapshot.getChildren()){
                    Preferencias preferencias = preferenciaSnapshot.getValue(Preferencias.class);
                    if (preferencias.getIdUsuario().equals(mAuth.getCurrentUser().getUid())){
                        matriz[counter][0] = preferencias.precio;
                        matriz[counter][0] = preferencias.precio;
                        matriz[counter][0] = preferencias.precio;
                        matriz[counter][0] = preferencias.precio;
                        matriz[counter][0] = preferencias.precio;

                        Log.i("DATOS", preferencias.toString());
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        String [][] d = new String[1][1];
        return d;
    }



}
