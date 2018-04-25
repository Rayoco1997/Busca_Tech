package mx.itesm.buscaTech.busca_tech;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class OlvidarContrasenaActiv extends AppCompatActivity {

    EditText etCorreoParaContra;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_olvidar_contrasena);
        this.setTitle("Restaurar contraseña");
        mAuth = FirebaseAuth.getInstance();
        etCorreoParaContra = findViewById(R.id.etCorreoParaContra);
    }


    public void regresar(View v){
        finish();
    }


    public void mandarCorreo(View v){
        final String correo = etCorreoParaContra.getText().toString();
        if (correo.equals("")){
            etCorreoParaContra.setError("Escribe tu correo electrónico.");
            etCorreoParaContra.requestFocus();
        }
        mAuth.sendPasswordResetEmail(correo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Se envió correctamente el correo.", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "No se pudo enviar la contraseña.", Toast.LENGTH_LONG).show();
            }
        });
        etCorreoParaContra.setText("");
    }


}
