package mx.itesm.buscaTech.busca_tech;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BuscarProductoActiv extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strBuscaTuProducto);
        setContentView(R.layout.activity_buscar_producto);
    }
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
