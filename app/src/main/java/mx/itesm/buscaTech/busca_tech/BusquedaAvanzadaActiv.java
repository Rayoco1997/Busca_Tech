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
            Arrays.asList("","7\"","8\"","9\"", "10\""));


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

        //Asignar cada array a su adapter
        final ArrayAdapter<String> dispositivoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, DispositivoSpinner);
        final ArrayAdapter<String> computadoraAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ComputadoraSpinner);
        final ArrayAdapter<String> categoriaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CategoriaSpinner);
        final ArrayAdapter<String> tabletAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, TabletSpinner);
        final ArrayAdapter<String> tamagnoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, TamagnoSpinner);
        final ArrayAdapter<String> celularAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CelularSpinner);

        //Colocar elementos en la clase
        tvDispositivo.setText("Dispositivo: ");

        spDispositivo.setAdapter(dispositivoAdapter);
        spAttr2.setEnabled(false);
        spAttr3.setEnabled(false);

        spAttr2.setVisibility(View.GONE);
        spAttr3.setVisibility(View.GONE);


        //Revisando al seleccionar un dispositivo
        spDispositivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvAttr2.setText("");
                spAttr2.setVisibility(View.GONE);
                tvAttr3.setText("");
                spAttr3.setVisibility(View.GONE);
                String seleccion;
                seleccion = parent.getItemAtPosition(position).toString();
                switch (seleccion){
                    case "Computadora":
                        //configuración del atributo 2
                        tvAttr2.setText("Tipo: ");
                        spAttr2.setAdapter(computadoraAdapter);
                        spAttr2.setEnabled(true);
                        spAttr2.setVisibility(View.VISIBLE);
                        busqueda+=seleccion+" ";
                        break;

                    case "Tablet":
                        //atributo 2
                        tvAttr2.setText("Sistema Operativo: ");
                        spAttr2.setAdapter(tabletAdapter);
                        spAttr2.setEnabled(true);
                        spAttr2.setVisibility(View.VISIBLE);
                        busqueda+=seleccion+" ";
                        break;

                    case "Celular":
                        //Único atributo
                        tvAttr2.setText("Gama: ");
                        spAttr2.setAdapter(celularAdapter);
                        spAttr2.setEnabled(true);
                        spAttr2.setVisibility(View.VISIBLE);
                        busqueda+=seleccion+" ";
                        break;

                    default:
                        break;
                }

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
                tvAttr3.setText("");
                spAttr3.setVisibility(View.GONE);
                String seleccion;
                seleccion = parent.getItemAtPosition(position).toString();
                switch (seleccion){
                    case "Laptop":
                        tvAttr3.setText("Categoría: ");
                        spAttr3.setAdapter(categoriaAdapter);
                        spAttr3.setEnabled(true);
                        spAttr3.setVisibility(View.VISIBLE);
                        busqueda+=seleccion+" ";
                        break;
                    case "De Escritorio":
                        tvAttr3.setText("Categoría: ");
                        spAttr3.setAdapter(categoriaAdapter);
                        spAttr3.setEnabled(true);
                        spAttr3.setVisibility(View.VISIBLE);
                        busqueda+="Desktop ";
                        break;
                    case "Android":
                        tvAttr3.setText("Tamaño: ");
                        spAttr3.setAdapter(tamagnoAdapter);
                        spAttr3.setEnabled(true);
                        spAttr3.setVisibility(View.VISIBLE);
                        busqueda+=seleccion+" ";
                        break;
                    case "iOS":
                        busqueda+="iPad ";
                        break;
                    case "Gama Alta":
                        busqueda+="6gb ram 64gb octa core ";
                        break;
                    case "Gama Media":
                        busqueda+="3gb ram 32gb ";
                        break;
                    case "Económico":
                        busqueda+="1gb ram 8gb ";
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spAttr3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleccion=parent.getItemAtPosition(position).toString();
                switch (seleccion){
                    case "Programación":
                        busqueda+="8gb ram 1tb i5";
                        break;
                    case "Gaming":
                        busqueda+="16gb ram 1tb gtx 1050ti i7";
                        break;
                    case "Diseño":
                        busqueda+="8gb ram 1tb i7";
                        break;
                    case "Uso General":
                        busqueda+="4gb ram i3";
                        break;
                    default:
                        busqueda+=seleccion+" ";
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentReturn= new Intent(this,BuscarProductoActiv.class);
        startActivity(intentReturn);
        finish();
    }

    public void buscar(View v){
        Log.i("busqueda vacia", busqueda);
        if(busqueda.equals("  ") || busqueda.equals(" ")){
            mostrarDialogo("Elige un dispositivo.");
        }else {
            //mostrarDialogo("Entré a la busqueda con el string: "+ busqueda);
            Intent intent = new Intent(this, BuscarProductoActiv.class);
            intent.putExtra("busqueda", busqueda);
            startActivity(intent);
            finish();

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
