package mx.itesm.buscaTech.busca_tech;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class BusquedaAvanzadaActiv extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strBusquedaAv);
        setContentView(R.layout.activity_busqueda_avanzada);

        TextView tvDispositivo = findViewById(R.id.tvDispositivo);
        TextView tvAttr2 = findViewById(R.id.tvAttr2);
        TextView tvAttr3 = findViewById(R.id.tvAttr3);

        Spinner spDispositivo = findViewById(R.id.spDispositivo);
        Spinner spAttr2 = findViewById(R.id.spAttr2);
        Spinner spAttr3 = findViewById(R.id.spAttr3);

        ArrayList<String> DispositivoSpinner = new ArrayList<String>(
                Arrays.asList("","Laptop", "Tablet", "Celular"));

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, DispositivoSpinner);
        spDispositivo.setAdapter(spinnerArrayAdapter);
        tvDispositivo.setText("Dispositivo: ");





    }


}
