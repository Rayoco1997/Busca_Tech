package mx.itesm.buscaTech.busca_tech;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class BusquedaAvanzadaActiv extends AppCompatActivity {

    String busqueda= " ";

    ArrayList<String> DispositivoSpinner = new ArrayList<String>(
            Arrays.asList("","Computadora", "Tablet", "Celular"));


    ArrayList<String> ComputadoraSpinner = new ArrayList<String>(
            Arrays.asList("","Laptop", "De Escritorio"));

    ArrayList<String> CategoriaSpinner = new ArrayList<String>(
            Arrays.asList("","Programación", "Gaming", "Diseño", "Uso general"));


    ArrayList<String> TabletSpinner = new ArrayList<String>(
            Arrays.asList("","Android", "iOS"));

    ArrayList<String> TamagnoSpinner = new ArrayList<String>(
            Arrays.asList("","7\"+","8\"+","9\"+", "10\"+"));


    ArrayList<String> CelularSpinner = new ArrayList<String>(
            Arrays.asList("","Gama Alta", "Gama Media", "Económico"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strBusquedaAv);
        setContentView(R.layout.activity_busqueda_avanzada);

        //Conexiones con View
        TextView tvDispositivo = findViewById(R.id.tvDispositivo);
        final TextView tvAttr2 = findViewById(R.id.tvAttr2);
        final TextView tvAttr3 = findViewById(R.id.tvAttr3);
        final Spinner spDispositivo = findViewById(R.id.spDispositivo);
        final Spinner spAttr2 = findViewById(R.id.spAttr2);
        final Spinner spAttr3 = findViewById(R.id.spAttr3);

        //Asignar Array al spinner
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, DispositivoSpinner);
        final ArrayAdapter<String> spAttr2Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, DispositivoSpinner);
        final ArrayAdapter<String> spAttr3Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, DispositivoSpinner);

        //Colocar elementos en la clase
        tvDispositivo.setText("Dispositivo: ");

        spDispositivo.setAdapter(adapter);
        spAttr2.setVisibility(View.GONE);
        spAttr3.setVisibility(View.GONE);


        //Revisando al seleccionar un dispositivo
        spDispositivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleccion;
                seleccion = parent.getItemAtPosition(position).toString();
                //spDispositivo.setEnabled(false);
                //spDispositivo.setClickable(false);
                spAttr2.setAdapter(spAttr2Adapter);
                switch (seleccion){
                    case "Computadora":
                        //configuración del atributo 2
                        tvAttr2.setText("Tipo: ");
                        spAttr2Adapter.clear();
                        spAttr2Adapter.addAll(ComputadoraSpinner);
                        spAttr2.setVisibility(View.VISIBLE);

                        /*
                        //Configuración del atributo 3
                        tvAttr3.setText("Categoría: ");
                        spAttr3Adapter.clear();
                        spAttr3Adapter.addAll(CategoriaSpinner);
                        spAttr3.setVisibility(View.VISIBLE);
                        */

                        break;

                    case "Tablet":
                        //atributo 2
                        tvAttr2.setText("Sistema Operativo: ");
                        spAttr2Adapter.clear();
                        spAttr2Adapter.addAll(TabletSpinner);
                        spAttr2.setVisibility(View.VISIBLE);

                        /*
                        //atributo 3
                        tvAttr3.setText("Tamaño: ");
                        spAttr3Adapter.clear();
                        spAttr3Adapter.addAll(ComputadoraSpinner);
                        spAttr3.setVisibility(View.VISIBLE);
                        */
                        break;

                    case "Celular":
                        //Único atributo
                        tvAttr2.setText("Gama: ");
                        spAttr2Adapter.clear();
                        spAttr2Adapter.addAll(CelularSpinner);
                        spAttr2.setVisibility(View.VISIBLE);
                        break;

                    default:
                        break;
                }
                busqueda+=seleccion+" ";
                //Toast.makeText(BusquedaAvanzadaActiv.this, busqueda, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });

        //Revisando al seleccionar un atributo 2
        spAttr2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleccion;
                seleccion = parent.getItemAtPosition(position).toString();
                //spAttr2.setEnabled(false);
                //spAttr2.setClickable(false);
                spAttr3.setAdapter(spAttr3Adapter);
                switch (seleccion){
                    case "Laptop":
                        tvAttr3.setText("Categoría: ");
                        spAttr3Adapter.clear();
                        spAttr3Adapter.addAll(CategoriaSpinner);
                        spAttr3.setVisibility(View.VISIBLE);
                        break;
                    case "De Escritorio":
                        tvAttr3.setText("Categoría: ");
                        spAttr3Adapter.clear();
                        spAttr3Adapter.addAll(CategoriaSpinner);
                        spAttr3.setVisibility(View.VISIBLE);
                        break;
                    case "Android":
                        tvAttr3.setText("Tamaño: ");
                        spAttr3Adapter.clear();
                        spAttr3Adapter.addAll(TamagnoSpinner);
                        spAttr3.setVisibility(View.VISIBLE);
                        break;
                    case "iOS":
                        tvAttr3.setText("Tamaño: ");
                        spAttr3Adapter.clear();
                        spAttr3Adapter.addAll(TamagnoSpinner);
                        spAttr3.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                busqueda+=seleccion+" ";
                //Toast.makeText(BusquedaAvanzadaActiv.this, busqueda, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spAttr3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleccion;
                seleccion = parent.getItemAtPosition(position).toString();
                //spAttr2.setEnabled(false);
                //spAttr2.setClickable(false);
                //spAttr3.setAdapter(spAttr3Adapter);
                busqueda+=seleccion+" ";
                //Toast.makeText(BusquedaAvanzadaActiv.this, busqueda, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void buscar(View v){
        Log.i("busqueda vacia", busqueda);
        if(busqueda.equals("   ") || busqueda.equals("  ")){
            Log.i("ALERTA ROJA","ANTRE UNO ANTES DEL DIALOGO");
            mostrarDialogo("Elige un dispositivo.");
        }else {
            Intent intent = new Intent(this, BuscarProductoActiv.class);
            intent.putExtra("busqueda", busqueda);
            startActivity(intent);

        }
    }

    private void mostrarDialogo(String mensaje) {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("ERROR");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Click
                //Info que tiene que pasar
            }
        });
        builder.show();

    }


}
