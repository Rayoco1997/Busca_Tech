package mx.itesm.buscaTech.busca_tech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegistraCuentaActiv extends AppCompatActivity {

    EditText etNombreUsuario;
    EditText etCorreo;
    EditText etContrasena1;
    EditText etContrasena2;
    // Boolean correcto=false;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        this.setTitle(R.string.strCreaTuCuenta);
        setContentView(R.layout.activity_registra_cuenta);
        etNombreUsuario = findViewById(R.id.etNombreUsuario);
        etCorreo = findViewById(R.id.etCorreo);
        etContrasena1 = findViewById(R.id.etContrasena1);
        etContrasena2 = findViewById(R.id.etContrasena2);
    }

    /*
    public void mandarALogin(View v){
        Intent intLogin= new Intent(this,LoginActiv.class);
        startActivity(intLogin);
        finish();
    }
    */

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void registrarUsuario(View v) {
        final String nombreUsuario = etNombreUsuario.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String contrasena1 = etContrasena1.getText().toString().trim();
        String contrasena2 = etContrasena2.getText().toString().trim();

        if (correo.contains(" ") || contrasena1.contains(" ") || contrasena2.contains(" ")){
            Toast.makeText(getApplicationContext(), "El correo y/o contraseña no pueden contener espacios en blanco.", Toast.LENGTH_LONG).show();

        }else if (nombreUsuario.equals("")){
            etNombreUsuario.setError("El campo no puede estar vacío.");
            etNombreUsuario.requestFocus();

        }else if (correo.equals("")){
            etCorreo.setError("El campo no puede estar vacío.");
            etCorreo.requestFocus();

        }else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            etCorreo.setError("Introduce un correo electrónico válido.");
            etCorreo.requestFocus();

        }else if(contrasena1.equals("")){
            etContrasena1.setError("El campo no puede estar vacío.");
            etContrasena1.requestFocus();

        }else if(contrasena2.equals("")){
            etContrasena2.setError("El campo no puede estar vacío.");
            etContrasena2.requestFocus();

        }else if (contrasena1.length() < 6){
            etContrasena1.setError("La contraseña debe de contener al menos 6 caracteres.");
            etContrasena1.requestFocus();

        }else if (contrasena2.length() < 6){
            etContrasena2.setError("La contraseña debe de contener al menos 6 caracteres.");
            etContrasena2.requestFocus();

        }
        /*
        else if(etNombreUsuario.getText().toString().equals("")){
            etNombreUsuario.setError("El campo no puede estar vacío.");
            etNombreUsuario.requestFocus();

        }
        */
        else if (contrasena1.equals(contrasena2)) {
            progressDialog.setMessage("Registrando usuario...");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(correo, contrasena1)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Se registró el usuario correctamente.", Toast.LENGTH_SHORT).show();
                                guardarNombreUsuario(nombreUsuario);
                                Intent intLogin= new Intent(getApplicationContext(), LoginActiv.class);
                                startActivity(intLogin);
                                finish();
                            }else {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                    Toast.makeText(getApplicationContext(), "Este correo ya está registrado.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "No se pudo registrar el usuario.", Toast.LENGTH_SHORT).show();
                                    Log.e("ERROR", "onComplete: Failed=" + task.getException().getMessage());
                                }
                                progressDialog.dismiss();
                            }
                        }
                    });
                /*
                new BDUsuario().execute();
                try {
                    Thread.sleep(1000);
                }catch (Exception e){

                }
                */
            }// En caso de que la constraseña no sea igual
            else {
                Toast.makeText(this, "La constraseña no coincide...", Toast.LENGTH_LONG).show();
            }

    }


    private void guardarNombreUsuario(String nombre){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user!=null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nombre)
                    .build();
            user.updateProfile(profile).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Se guardó el nombre de usuario correctamente.", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getApplicationContext(), "No se guardó el nombre de usuario.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }


    /*
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
    */
}
