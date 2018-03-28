package mx.itesm.buscaTech.busca_tech;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class SugerirTiendaActiv extends AppCompatActivity {
    EditText etNombreTienda;
    EditText etDireccionTienda;
    EditText etDescripcionTienda;
    Spinner spCategoriaTienda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strSugerencias);
        setContentView(R.layout.activity_sugerir_tienda);
        etNombreTienda = findViewById(R.id.etNombreTienda);
        etDireccionTienda = findViewById(R.id.etDireccionTienda);
        etDescripcionTienda = findViewById(R.id.etDescripcionTienda);
        spCategoriaTienda = findViewById(R.id.spCategoriaTienda);
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

    public void sugerirTienda(View v){
        String nombreTienda = etNombreTienda.getText().toString();
        String direccionTienda = etDireccionTienda.getText().toString();
        String descripcionTienda = etDescripcionTienda.getText().toString();
        String categoriaTienda = spCategoriaTienda.getSelectedItem().toString();

        String soriana = "Nombre: "+nombreTienda+"\nDireccion: "+direccionTienda+"\nDescripción: "+descripcionTienda+"\nCategoria: "+categoriaTienda;

        composeEmail(soriana);
        Log.i("Sugerir Tienda", "Tienda sugerida: "+nombreTienda);
        //Intent intPantallaPrincipal= new Intent(this, PantallaPrincipalActiv.class);
        //startActivity(intPantallaPrincipal);
    }

    public void composeEmail(String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        String[] direcciones = new String[1];
        direcciones[0]= "buscatechoficial@gmail.com";
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, direcciones);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Sugerencia de tienda");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        Log.i("Sugerir Tienda", "Texto a mandar "+text);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
