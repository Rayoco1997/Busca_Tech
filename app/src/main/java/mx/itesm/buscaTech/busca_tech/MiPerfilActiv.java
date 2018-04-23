package mx.itesm.buscaTech.busca_tech;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class MiPerfilActiv extends AppCompatActivity {

    TextView tvUsuarioDatos;
    TextView tvCorreoDatos;
    TextView tvContrasenaDatos;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strMiPerfil);
        setContentView(R.layout.activity_cambiar_usu_contra);
        tvUsuarioDatos = findViewById(R.id.tvUsuarioDatos);
        tvCorreoDatos = findViewById(R.id.tvCorreoDatos);
        tvContrasenaDatos = findViewById(R.id.tvContrasenaDatos);
        mAuth = FirebaseAuth.getInstance();
        mostrarPerfil();
        // mostrarDatos();
        // Intent intLogin= new Intent(this,LoginActiv.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarPerfil();
        // mostrarDatos();
    }


    private void mostrarPerfil() {
        FirebaseUser user = mAuth.getCurrentUser();
        String nombre = "";
        String correo = "";
        if (user != null){
            if (user.getDisplayName() != null){
                nombre = user.getDisplayName();
            }
            if (user.getEmail() != null){
                correo = user.getEmail();
            }
        }
        // El usuario ingresó como invitado
        else {
            nombre = "Invitado";
            correo = "";
        }
        setText(tvUsuarioDatos, nombre);
        setText(tvCorreoDatos, correo);
        setText(tvContrasenaDatos, "******");

    }


    /*
    private void mostrarDatos() {
        // String yourFilePath = getApplicationContext().getFilesDir() + "/" + "DatosUsuario";
        // File yourFile = new File( yourFilePath );
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = null;
            fis = getApplicationContext().openFileInput("DatosUsuario");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // String correo = yourFile.toString();
        String correo = sb.toString();
        Log.i("Datos", "Los datos que tiene el archivo son "+ correo);
        new BDUsuario(correo).execute();
    }
    */


    public void cambiarAPantallaCambioContrasena(View v){
        Intent intCambioContrasena= new Intent(this, CambiarContrasenaActiv.class);
        startActivity(intCambioContrasena);
    }


    public void cambiarAPantallaCambioUsuario(View v){
        Intent intCambioUsuario= new Intent(this, CambiarUsuarioActiv.class);
        startActivity(intCambioUsuario);
    }


    public void cambiarPantallaPrincipal(View v){
        finish();
    }


    public void cambiarPantallaCambioCorreo(View v){
        Intent intCambioCorreo = new Intent(this, CambiarCorreoActiv.class);
        startActivity(intCambioCorreo);
    }


    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void setText(final TextView text,final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }


    /*
    public void obtenerDatos(String correo){
        Usuario usuario = new Usuario();
        UsuarioBD bd = UsuarioBD.getInstance(this);
        usuario = bd.usuarioDAO().buscarPorCorreo(correo);
        setText(tvCorreoDatos,usuario.getCorreo().toString());
        setText(tvUsuarioDatos, usuario.getNombreUsuario().toString());
        String asteristicos = cambiarAAsteristicos(usuario.getContrasena().toString());
        setText(tvContrasenaDatos,asteristicos);
    }


    public String cambiarAAsteristicos(String contrasena){
        String asteristicos = "";
        for (int i = 0; i < contrasena.length(); i++){
            asteristicos+= "*";
        }
        return asteristicos;
    }


    private class BDUsuario extends AsyncTask<Void, Void, Void> {
        String correo;
        public BDUsuario(String correo){
            this.correo = correo;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            obtenerDatos(correo);
            return null;
        }
    }
    */


}
