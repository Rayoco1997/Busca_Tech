package mx.itesm.buscaTech.busca_tech;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CambiarContrasenaActiv extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strConfiguración);
        setContentView(R.layout.activity_cambiar_contrasena);
    }
    public void cambiarAPantallaCambioUsuarioContra(View v){
        Intent intCambioUsuContra= new Intent(this, MiPerfilActiv.class);
        startActivity(intCambioUsuContra);
    }
}
