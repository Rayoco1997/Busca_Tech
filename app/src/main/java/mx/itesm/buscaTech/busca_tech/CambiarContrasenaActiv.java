package mx.itesm.buscaTech.busca_tech;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class CambiarContrasenaActiv extends AppCompatActivity {

    EditText etContrasenaAntigua;
    EditText etContrasenaNueva1;
    EditText etContrasenaNueva2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strConfiguración);
        setContentView(R.layout.activity_cambiar_contrasena);
        etContrasenaAntigua = findViewById(R.id.etContrasenaAntigua);
        etContrasenaNueva1 = findViewById(R.id.etContrasenaNueva1);
        etContrasenaNueva2 = findViewById(R.id.etContrasenaNueva2);
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

    public void cambiarContrasena(View v) {
        if (etContrasenaNueva1.getText().toString().equals(etContrasenaNueva2.getText().toString())){
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
            new BDUsuario(correo, etContrasenaAntigua.getText().toString(), etContrasenaNueva1.getText().toString()).execute();
        }else{
            Toast.makeText(this, "Las contraseñas nuevas no son iguales", Toast.LENGTH_LONG).show();
        }
    }

    private class BDUsuario extends AsyncTask<Void, Void, Void> {
        String correo;
        String contresenaAntigua;
        String contrasenaNueva;
        public BDUsuario(String correo, String contrasenaAntigua, String contrasenaNueva){
            this.correo = correo;
            this.contresenaAntigua = contrasenaAntigua;
            this.contrasenaNueva = contrasenaNueva;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            cambiarContrasenaBD(correo, contresenaAntigua, contrasenaNueva);
            return null;
        }

    }

    public void cambiarContrasenaBD(String correo, String contrasenaAntigua, String contrasenaNueva){
        Usuario usuario = new Usuario();
        UsuarioBD bd = UsuarioBD.getInstance(this);
        usuario = bd.usuarioDAO().buscarPorCorreo(correo);
        if (usuario.getContrasena().equals(contrasenaAntigua)){
            bd.usuarioDAO().actualizarContrasena(contrasenaNueva, correo);
            Intent intCambioUsuContra= new Intent(this, MiPerfilActiv.class);
            startActivity(intCambioUsuContra);
        }else {
            Log.i("Cambiar Contraseña BD", "La contraseña antigua no era igual a la BD");
        }
    }
}
