package mx.itesm.buscaTech.busca_tech;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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
        File file = new File(getApplicationContext().getFilesDir(),"DatosUsuario");
        if(file.exists()){
            Intent intPantallaPrincipal= new Intent(this,PantallaPrincipalActiv.class);
            startActivity(intPantallaPrincipal);
            finish();
        }
        else{
            //Nada GEGE
        }
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
                                               Toast.makeText(getApplicationContext(), "No se pudo ingresar.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    public void iniciarSesion(View v){
        String correo = etCorreo.getText().toString().toLowerCase();
        String contrasena = etContrasena.getText().toString();
        if(correo.equals("")){
            etCorreo.setError("El campo no puede estar vacío.");
        }else if(contrasena.equals("")){
            etContrasena.setError("El campo no puede estar vacío.");
        }else if (!correo.equals("") &&!contrasena.equals("")){
            new BDUsuario(correo, contrasena).execute();
            try {
                Thread.sleep(1000);
            }catch (Exception e){

            }
            if (coincide){
                Toast.makeText(this, "Bienvenido "+correo, Toast.LENGTH_SHORT).show();
                Intent intPantallaPrincipal= new Intent(this,PantallaPrincipalActiv.class);
                startActivity(intPantallaPrincipal);
                finish();
            }else {
                Toast.makeText(this, "La contraseña y/o correo son incorrectos", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, "Introduce tu correo y contraseña", Toast.LENGTH_LONG).show();
        }
    }


    public void verificarInicio(String correo, String contrasena){
        try {
            Usuario usuario = new Usuario();
            UsuarioBD bd = UsuarioBD.getInstance(this);
            usuario = bd.usuarioDAO().buscarPorCorreo(correo);
            Log.i("Verificar Usuario", "Se obtuvo el correo");
            String contrasenaReal = usuario.getContrasena();
            if (contrasena.equals(contrasenaReal)){
                coincide = true;
                String filename = "DatosUsuario";
                String string = usuario.getCorreo()+"";
                FileOutputStream outputStream;
                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(string.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                coincide = false;
            }
        }catch (Exception e){
            Log.i("Verificar Usuario", "No se pudo reconocer ese correo");
        }
    }



    private class BDUsuario extends AsyncTask<Void, Void, Void> {
        String correo;
        String contrasena;
        public BDUsuario(String correo, String contrasena){
            this.correo = correo;
            this.contrasena = contrasena;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            verificarInicio(correo, contrasena);
            return null;
        }

    }
}
