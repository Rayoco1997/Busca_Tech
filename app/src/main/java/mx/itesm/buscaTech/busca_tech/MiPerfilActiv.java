package mx.itesm.buscaTech.busca_tech;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MiPerfilActiv extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strMiPerfil);
        setContentView(R.layout.activity_cambiar_usu_contra);
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
        Intent intPantallaPrincipal= new Intent(this, PantallaPrincipalActiv.class);
        startActivity(intPantallaPrincipal);
        finish();
    }
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
