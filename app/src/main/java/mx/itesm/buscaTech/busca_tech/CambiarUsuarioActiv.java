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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class CambiarUsuarioActiv extends AppCompatActivity {

    EditText etUsuarioNuevo;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    // Boolean correcto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strConfiguración);
        progressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_cambiar_usuario);
        etUsuarioNuevo = findViewById(R.id.etUsuarioNuevo);
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


    public void cambiarNombre(View v) {
        String nombre = etUsuarioNuevo.getText().toString();
        if(nombre.equals("")){
            etUsuarioNuevo.setError("El campo no puede estar vacío.");
            etUsuarioNuevo.requestFocus();
        }else {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null){
                progressDialog.setMessage("Realizando cambios...");
                progressDialog.show();
                UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                        .setDisplayName(nombre)
                        .build();
                user.updateProfile(profile).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    mostrarDialogo("Se guardó el nombre de usuario correctamente.");
                                    finish();
                                }else {
                                    progressDialog.dismiss();
                                    mostrarDialogo("No se guardó el nombre de usuario.");
                                }
                            }
                        });
            }
        }

    }

}
