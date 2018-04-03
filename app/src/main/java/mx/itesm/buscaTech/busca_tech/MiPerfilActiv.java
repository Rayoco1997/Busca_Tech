package mx.itesm.buscaTech.busca_tech;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strMiPerfil);
        setContentView(R.layout.activity_cambiar_usu_contra);
        tvUsuarioDatos = findViewById(R.id.tvUsuarioDatos);
        tvCorreoDatos = findViewById(R.id.tvCorreoDatos);
        tvContrasenaDatos = findViewById(R.id.tvContrasenaDatos);
        mostrarDatos();
        Intent intLogin= new Intent(this,LoginActiv.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarDatos();


    }

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

    public void cambiarAPantallaCambioContrasena(View v){
        Intent intCambioContrasena= new Intent(this, CambiarContrasenaActiv.class);
        startActivity(intCambioContrasena);
    }
    public void cambiarAPantallaCambioUsuario(View v){
        Intent intCambioUsuario= new Intent(this, CambiarUsuarioActiv.class);
        startActivity(intCambioUsuario);
    }
    public void cambiarPantallaPrincipal(View v){
        /*Intent intPantallaPrincipal= new Intent(this, PantallaPrincipalActiv.class);
        startActivity(intPantallaPrincipal);*/
        finish();
    }
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void obtenerDatos(String correo){
        Usuario usuario = new Usuario();
        UsuarioBD bd = UsuarioBD.getInstance(this);
        usuario = bd.usuarioDAO().buscarPorCorreo(correo);
        setText(tvCorreoDatos,usuario.getCorreo().toString());
        setText(tvUsuarioDatos, usuario.getNombreUsuario().toString());
        String asteristicos = cambiarAAsteristicos(usuario.getContrasena().toString());
        setText(tvContrasenaDatos,asteristicos);
    }

    private void setText(final TextView text,final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
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
}
