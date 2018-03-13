package mx.itesm.buscaTech.busca_tech;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SugerirTiendaActiv extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strSugerencias);
        setContentView(R.layout.activity_sugerir_tienda);
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
