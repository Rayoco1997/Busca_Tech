package mx.itesm.buscaTech.busca_tech;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistraCuentaActiv extends AppCompatActivity {

    EditText etNombreUsuario;
    EditText etCorreo;
    EditText etContrasena1;
    EditText etContrasena2;
    Boolean correcto=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strCreaTuCuenta);
        setContentView(R.layout.activity_registra_cuenta);
        etNombreUsuario = findViewById(R.id.etNombreUsuario);
        etCorreo = findViewById(R.id.etCorreo);
        etContrasena1 = findViewById(R.id.etContrasena1);
        etContrasena2 = findViewById(R.id.etContrasena2);
    }

    public void mandarALogin(View v){
        Intent intLogin= new Intent(this,LoginActiv.class);
        startActivity(intLogin);
        finish();
    }
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void registrarUsuario(View v) {
        if(etCorreo.getText().toString().equals("")){
            etCorreo.setError("El campo no puede estar vacío.");
        }else if(etContrasena1.getText().toString().equals("")){
            etContrasena1.setError("El campo no puede estar vacío.");
        }else if(etContrasena2.getText().toString().equals("")){
            etContrasena2.setError("El campo no puede estar vacío.");
        }else if(etNombreUsuario.getText().toString().equals("")){
            etNombreUsuario.setError("El campo no puede estar vacío.");
        }else if (!etCorreo.getText().toString().equals("")
                && !etNombreUsuario.getText().toString().equals("")
                && !etContrasena1.getText().toString().equals("")
                && !etContrasena2.getText().toString().equals("")){
            if (etContrasena1.getText().toString().equals(etContrasena2.getText().toString())) {
                new BDUsuario().execute();
                try {
                    Thread.sleep(1000);
                }catch (Exception e){

                }
                if(correcto){
                    Toast.makeText(this, "Se registró el usuario correctamente.", Toast.LENGTH_SHORT).show();
                    Intent intLogin= new Intent(this,LoginActiv.class);
                    startActivity(intLogin);
                    finish();
                }else{
                    Toast.makeText(this, "No se pudo regisrar el usuario, intente nuevamente.", Toast.LENGTH_SHORT).show();
                }

            }// En caso de que la constraseña no sea igual
            else {
                Toast.makeText(this, "La constraseña no coincide...", Toast.LENGTH_LONG).show();
            }
        }// Hay un dato vacío
        else {
                Toast.makeText(this, "Introduce todos los datos...", Toast.LENGTH_LONG).show();
        }
    }

    private void grabarRegistro(){
        try {
            Usuario usuario = new Usuario();
            usuario.setCorreo(etCorreo.getText().toString().toLowerCase());
            usuario.setNombreUsuario(etNombreUsuario.getText().toString());
            usuario.setContrasena(etContrasena1.getText().toString());
            //usuario.setImagen(null);
            UsuarioBD bd = UsuarioBD.getInstance(this);
            bd.usuarioDAO().insertar(usuario);
            Log.i("Grabar registro","Se grabó el registro del correo "+ etCorreo.getText().toString());
            correcto=true;

        } catch (Exception e) {
            Log.i("Grabar registro","Se detectó un error en el registro del usuario, verifique salu2");
            correcto=false;
        }
    }

    private class BDUsuario extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            grabarRegistro();
            return null;
        }

    }
}
