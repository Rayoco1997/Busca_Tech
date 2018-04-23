package mx.itesm.buscaTech.busca_tech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class CambiarContrasenaActiv extends AppCompatActivity {

    EditText etContrasenaAntigua;
    EditText etContrasenaNueva1;
    EditText etContrasenaNueva2;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    // Boolean correcto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strConfiguración);
        setContentView(R.layout.activity_cambiar_contrasena);
        progressDialog = new ProgressDialog(this);
        etContrasenaAntigua = findViewById(R.id.etContrasenaAntigua);
        etContrasenaNueva1 = findViewById(R.id.etContrasenaNueva1);
        etContrasenaNueva2 = findViewById(R.id.etContrasenaNueva2);
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


    public void cambiarContrasena(View v){

        String contrasenaActual = etContrasenaAntigua.getText().toString().trim();
        final String contrasenaNueva1 = etContrasenaNueva1.getText().toString().trim();
        String contrasenaNueva2 = etContrasenaNueva2.getText().toString().trim();
        String correoActual = mAuth.getCurrentUser().getEmail();

        if (contrasenaActual.equals("")){
            etContrasenaAntigua.setError("Ingresa tu contraseña actual.");
            etContrasenaAntigua.requestFocus();

        }
        else if (contrasenaActual.length() < 6){
            etContrasenaAntigua.setError("Tu contraseña actual no contiene menos de 6 caracteres.");
            etContrasenaAntigua.requestFocus();

        }
        else if (contrasenaNueva1.equals("")){
            etContrasenaNueva1.setError("Ingresa tu nueva contraseña.");
            etContrasenaNueva1.requestFocus();

        }
        else if (contrasenaNueva1.length() < 6){
            etContrasenaNueva1.setError("Tu nueva contraseña no puede tener menos de 6 caracteres.");
            etContrasenaNueva1.requestFocus();

        }
        else if (contrasenaNueva2.equals("")){
            etContrasenaNueva2.setError("Confirma tu nueva contraseña.");
            etContrasenaNueva2.requestFocus();

        }
        else if (contrasenaNueva2.length() < 6){
            etContrasenaNueva2.setError("Tu nueva contraseña no puede tener menos de 6 caracteres.");
            etContrasenaNueva2.requestFocus();

        }
        else if (!contrasenaNueva1.equals(contrasenaNueva2)){
            Toast.makeText(getApplicationContext(), "La contraseña nueva no coincide.", Toast.LENGTH_LONG).show();

        }
        else {
            progressDialog.setMessage("Realizando cambios...");
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(correoActual, contrasenaActual).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        mAuth.getCurrentUser().updatePassword(contrasenaNueva1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Se cambió la contraseña.", Toast.LENGTH_LONG).show();
                                    finish();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "No se cambió la contraseña.", Toast.LENGTH_LONG).show();
                                    Log.i("Cambiar contraseña", task.getException().getMessage());

                                }
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        etContrasenaAntigua.setError("La contraseña es incorrecta.");
                        etContrasenaAntigua.requestFocus();
                        Log.i("Cambiar contraseña Acc", task.getException().getMessage());

                    }
                }
            });
        }
    }


    /*
    public void cambiarContrasena(View v) {
        if(etContrasenaNueva1.getText().toString().equals("")){
            etContrasenaNueva1.setError("El campo no puede estar vacío.");
        }else if(etContrasenaNueva2.getText().toString().equals("")){
            etContrasenaNueva2.setError("El campo no puede estar vacío.");
        }else if(etContrasenaAntigua.getText().toString().equals("")){
            etContrasenaAntigua.setError("El campo no puede estar vacío.");
        }
        else if (etContrasenaNueva1.getText().toString().equals(etContrasenaNueva2.getText().toString()) && !etContrasenaAntigua.getText().toString().equals("")){
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
            try {
                Thread.sleep(1000);
            }catch (Exception e){

            }
            if(correcto){
                Toast.makeText(this, "Se actualizó la contraseña correctamente.", Toast.LENGTH_SHORT).show();
                //Intent intCambioUsuContra= new Intent(this, MiPerfilActiv.class);
                //startActivity(intCambioUsuContra);
                finish();
            }else{
                Toast.makeText(this, "Hubo un error al actualizar la contraseña, intente de nuevo.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Las contraseñas nuevas no son iguales.", Toast.LENGTH_LONG).show();
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
            correcto=true;
            bd.usuarioDAO().actualizarContrasena(contrasenaNueva, correo);
            //Intent intCambioUsuContra= new Intent(this, MiPerfilActiv.class);
            //startActivity(intCambioUsuContra);
        }else {
            Log.i("Cambiar Contraseña BD", "La contraseña antigua no era igual a la BD");
            correcto=false;
        }
    }
    */


}
