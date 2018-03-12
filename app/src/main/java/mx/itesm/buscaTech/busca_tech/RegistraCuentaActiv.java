package mx.itesm.buscaTech.busca_tech;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RegistraCuentaActiv extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strCreaTuCuenta);
        setContentView(R.layout.activity_registra_cuenta);
    }

    public void mandarALogin(View v){
        Intent intLogin= new Intent(this,LoginActiv.class);
        startActivity(intLogin);
    }
}
