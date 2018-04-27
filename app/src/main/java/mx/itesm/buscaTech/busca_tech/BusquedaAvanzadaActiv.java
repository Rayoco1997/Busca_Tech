package mx.itesm.buscaTech.busca_tech;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BusquedaAvanzadaActiv extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strBusquedaAv);
        setContentView(R.layout.activity_busqueda_avanzada);
    }
}
