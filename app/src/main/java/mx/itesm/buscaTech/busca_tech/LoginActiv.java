package mx.itesm.buscaTech.busca_tech;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;

public class LoginActiv extends AppCompatActivity {
    Boolean coincide = false;
    EditText etCorreo;
    EditText etContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strBienvenido);
        setContentView(R.layout.activity_login);
        etCorreo = findViewById(R.id.etCorreoLogin);
        etContrasena = findViewById(R.id.etContrasenaLogin);
    }

    public void mandarAPantallaPrincipal(View v){
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

    public void iniciarSesion(View v){
        String correo = etCorreo.getText().toString();
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
                Intent intPantallaPrincipal= new Intent(this,PantallaPrincipalActiv.class);
                startActivity(intPantallaPrincipal);
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
