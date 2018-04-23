package mx.itesm.buscaTech.busca_tech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

public class CambiarCorreoActiv extends AppCompatActivity {

    EditText etCorreoNuevo;
    EditText etCambiarCorreoContra;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_correo);
        this.setTitle(R.string.strConfiguración);
        progressDialog = new ProgressDialog(this);
        etCorreoNuevo = findViewById(R.id.etCorreoNuevo);
        etCambiarCorreoContra = findViewById(R.id.etCambiarCorreoContra);
        mAuth = FirebaseAuth.getInstance();
    }


    public void cambiarAPantallaMiPerfil(View v){
        finish();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }


    public void cambiarCorreo(View v) {

        final String correo = etCorreoNuevo.getText().toString().trim();
        String contrasena = etCambiarCorreoContra.getText().toString().trim();
        String correoActual = mAuth.getCurrentUser().getEmail();

        if(correo.equals("")){
            etCorreoNuevo.setError("El correo no puede estar vacío.");
            etCorreoNuevo.requestFocus();

        }
        else if (correo.contains(" ")){
            etCorreoNuevo.setError("El correo no puede contener espacios.");
            etCorreoNuevo.requestFocus();

        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            etCorreoNuevo.setError("Introduce un correo electrónico válido.");
            etCorreoNuevo.requestFocus();

        } else if (contrasena.length() < 6){
            etCambiarCorreoContra.setError("La contraseña contiene al menos 6 caracteres.");
            etCambiarCorreoContra.requestFocus();

        }
        else {
            progressDialog.setMessage("Realizando cambios...");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(correoActual, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        mAuth.getCurrentUser().updateEmail(correo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Se cambió el correo.", Toast.LENGTH_LONG).show();
                                    finish();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "No se cambió el correo.", Toast.LENGTH_LONG).show();
                                    Log.i("Cambiar correo", task.getException().getMessage());

                                }
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        etCambiarCorreoContra.setError("La contraseña es incorrecta.");
                        etCambiarCorreoContra.requestFocus();
                        Log.i("Cambiar correo accesp", task.getException().getMessage());

                    }
                }
            });

            }
        }


    }
