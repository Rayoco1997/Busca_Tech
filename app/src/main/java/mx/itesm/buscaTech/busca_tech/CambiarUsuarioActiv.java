package mx.itesm.buscaTech.busca_tech;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class CambiarUsuarioActiv extends AppCompatActivity {

    EditText etUsuarioNuevo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strConfiguración);
        setContentView(R.layout.activity_cambiar_usuario);
        etUsuarioNuevo = findViewById(R.id.etUsuarioNuevo);
    }
    public void cambiarAPantallaCambioUsuarioContra(View v){
        Intent intCambioUsuContra= new Intent(this, MiPerfilActiv.class);
        startActivity(intCambioUsuContra);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void cambiarNombre(View v) {
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
        new BDUsuario(correo, etUsuarioNuevo.getText().toString()).execute();
    }

    private class BDUsuario extends AsyncTask<Void, Void, Void> {
        String correo;
        String nombreUsuario;
        public BDUsuario(String correo, String nombreUsuario){
            this.correo = correo;
            this.nombreUsuario = nombreUsuario;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            cambiarUsuarioBD(correo, nombreUsuario);
            return null;
        }

    }

    public void cambiarUsuarioBD(String correo, String nombreUsuario){
        Usuario usuario = new Usuario();
        UsuarioBD bd = UsuarioBD.getInstance(this);
        bd.usuarioDAO().actualizarNombreUsuario(nombreUsuario, correo);
        Intent intCambioUsuContra= new Intent(this, MiPerfilActiv.class);
        startActivity(intCambioUsuContra);
    }
}
