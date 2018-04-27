package mx.itesm.buscaTech.busca_tech;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutionException;

public class LoginActiv extends AppCompatActivity {
    Boolean coincide = false;
    EditText etCorreo;
    EditText etContrasena;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strBienvenido);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        setContentView(R.layout.activity_login);
        etCorreo = findViewById(R.id.etCorreoLogin);
        etContrasena = findViewById(R.id.etContrasenaLogin);
        /*File file = new File(getApplicationContext().getFilesDir(),"DatosUsuario");
        if(file.exists()){
            Intent intPantallaPrincipal= new Intent(this,PantallaPrincipalActiv.class);
            startActivity(intPantallaPrincipal);
            finish();
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, PantallaPrincipalActiv.class));
        }
    }

    public void mandarAPantallaPrincipal(View v){
        finish();
        Intent intPantallaPrincipal= new Intent(this,PantallaPrincipalActiv.class);
        startActivity(intPantallaPrincipal);
    }

    public void mandarARegistrarUsuario(View v){
        Intent intRegistroUsuario= new Intent(this,RegistraCuentaActiv.class);
        startActivity(intRegistroUsuario);
    }

    public void mandarABusqueda(View v){
        Intent intMandarABusqueda = new Intent(this, BuscarProductoActiv.class);
        startActivity(intMandarABusqueda);
    }

    public void iniciarAnonimo(View v){
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intPantallaPrincipal= new Intent(getApplicationContext(), PantallaPrincipalActiv.class);
                            startActivity(intPantallaPrincipal);
                            finish();

                        } else {
                            mostrarDialogo("No se pudo iniciar como Invitado");
                        }

                    }
                });
    }




    public void iniciarSesionFire(View v){
        final String correo = etCorreo.getText().toString();
        String contrasena = etContrasena.getText().toString();
        if (correo.equals("")){
            etCorreo.setError("El campo no puede estar vacío.");
            etCorreo.requestFocus();
        }else if (contrasena.equals("")){
            etContrasena.setError("El campo no puede estar vacío.");
            etContrasena.requestFocus();
        }else if (contrasena.length() < 6){
            etContrasena.setError("La contraseña contiene al menos 6 caracteres.");
            etContrasena.requestFocus();
        }else{
            progressDialog.setMessage("Iniciando sesión...");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(correo, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        // Toast.makeText(getApplicationContext(), "Bienvenido "+correo, Toast.LENGTH_SHORT).show();
                        Intent intPantallaPrincipal= new Intent(getApplicationContext(), PantallaPrincipalActiv.class);
                        startActivity(intPantallaPrincipal);
                        finish();
                    }else{
                        mostrarDialogo("No se pudo ingresar.");
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    public void mandarOlvidarContrasena(View v){
        Intent intOlvidarC= new Intent(this, OlvidarContrasenaActiv.class);
        startActivity(intOlvidarC);
    }

    private void mostrarDialogo(String mensaje) {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("ERROR");
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

}
