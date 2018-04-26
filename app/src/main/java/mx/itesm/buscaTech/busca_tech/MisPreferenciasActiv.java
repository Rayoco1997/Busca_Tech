package mx.itesm.buscaTech.busca_tech;

import android.arch.persistence.room.Database;
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

import mx.itesm.buscaTech.busca_tech.Objetos.FirebaseReferences;

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
        agregarPreferencia();
        obtenerPreferencia();
    }


    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void agregarPreferencia(){
        String idUsuario = mAuth.getCurrentUser().getUid();
        String precio = mAuth.getCurrentUser().getUid();
        String nombre = mAuth.getCurrentUser().getUid();
        String tienda = mAuth.getCurrentUser().getUid();

        String idPreferencia = databasePreferences.push().getKey();
        Preferencias preferencias = new Preferencias(idPreferencia, idUsuario, precio, nombre, tienda);
        databasePreferences.child(idPreferencia).setValue(preferencias);
    }


    private void obtenerPreferencia(){
        databasePreferences.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot preferenciaSnapshot : dataSnapshot.getChildren()){
                    Preferencias preferencias = preferenciaSnapshot.getValue(Preferencias.class);
                    if (preferencias.getIdUsuario().equals(mAuth.getCurrentUser().getUid())){
                        Log.i("DATOS", preferencias.toString());
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
