package mx.itesm.buscaTech.busca_tech;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActiv extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void mandarAPantallaPrincipal(View v){
        Intent intPantallaPrincipal= new Intent(this,PantallaPrincipalActiv.class);
        startActivity(intPantallaPrincipal);
    }

    public void mandarARegistrarUsuario(View v){
        Intent intRegistroUsuario= new Intent(this,RegistraCuentaActiv.class);
        startActivity(intRegistroUsuario);
    }
}
