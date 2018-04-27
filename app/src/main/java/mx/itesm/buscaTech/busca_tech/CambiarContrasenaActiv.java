package mx.itesm.buscaTech.busca_tech;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class CambiarContrasenaActiv extends AppCompatActivity {

    EditText etContrasenaAntigua;
    EditText etContrasenaNueva1;
    EditText etContrasenaNueva2;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    // Boolean correcto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strConfiguración);
        setContentView(R.layout.activity_cambiar_contrasena);
        progressDialog = new ProgressDialog(this);
        etContrasenaAntigua = findViewById(R.id.etContrasenaAntigua);
        etContrasenaNueva1 = findViewById(R.id.etContrasenaNueva1);
        etContrasenaNueva2 = findViewById(R.id.etContrasenaNueva2);
        mAuth = FirebaseAuth.getInstance();
    }
    public void cambiarAPantallaCambioUsuarioContra(View v){
        finish();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

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


    public void cambiarContrasena(View v){

        String contrasenaActual = etContrasenaAntigua.getText().toString().trim();
        final String contrasenaNueva1 = etContrasenaNueva1.getText().toString().trim();
        String contrasenaNueva2 = etContrasenaNueva2.getText().toString().trim();
        String correoActual = mAuth.getCurrentUser().getEmail();

        if (contrasenaActual.equals("")){
            etContrasenaAntigua.setError("Ingresa tu contraseña actual.");
            etContrasenaAntigua.requestFocus();

        }
        else if (contrasenaActual.length() < 6){
            etContrasenaAntigua.setError("Tu contraseña actual no contiene menos de 6 caracteres.");
            etContrasenaAntigua.requestFocus();

        }
        else if (contrasenaNueva1.equals("")){
            etContrasenaNueva1.setError("Ingresa tu nueva contraseña.");
            etContrasenaNueva1.requestFocus();

        }
        else if (contrasenaNueva1.length() < 6){
            etContrasenaNueva1.setError("Tu nueva contraseña no puede tener menos de 6 caracteres.");
            etContrasenaNueva1.requestFocus();

        }
        else if (contrasenaNueva2.equals("")){
            etContrasenaNueva2.setError("Confirma tu nueva contraseña.");
            etContrasenaNueva2.requestFocus();

        }
        else if (contrasenaNueva2.length() < 6){
            etContrasenaNueva2.setError("Tu nueva contraseña no puede tener menos de 6 caracteres.");
            etContrasenaNueva2.requestFocus();

        }
        else if (!contrasenaNueva1.equals(contrasenaNueva2)){
            mostrarDialogo("La contraseña nueva no coincide.");

        }
        else {
            progressDialog.setMessage("Realizando cambios...");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(correoActual, contrasenaActual).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        mAuth.getCurrentUser().updatePassword(contrasenaNueva1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    mostrarDialogo("Se cambió la contraseña.");
                                    finish();

                                } else {
                                    progressDialog.dismiss();
                                    mostrarDialogo("No se cambió la contraseña.");
                                    Log.i("Cambiar contraseña", task.getException().getMessage());

                                }
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        etContrasenaAntigua.setError("La contraseña es incorrecta.");
                        etContrasenaAntigua.requestFocus();
                        Log.i("Cambiar contraseña Acc", task.getException().getMessage());

                    }
                }
            });
        }
    }


}
