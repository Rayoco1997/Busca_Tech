package mx.itesm.buscaTech.busca_tech;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
        this.setTitle(R.string.strRestaurarContrasena);

        mAuth = FirebaseAuth.getInstance();
        etCorreoParaContra = findViewById(R.id.etCorreoParaContra);
    }

    private void mostrarDialogo(String mensaje) {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
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
                mostrarDialogo( "Se envió correctamente el correo.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mostrarDialogo("No se pudo enviar la contraseña.");
            }
        });
        etCorreoParaContra.setText("");
    }


}
