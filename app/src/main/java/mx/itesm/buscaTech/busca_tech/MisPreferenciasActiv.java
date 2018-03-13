package mx.itesm.buscaTech.busca_tech;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MisPreferenciasActiv extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strMisPreferencias);
        setContentView(R.layout.activity_mis_preferencias);
    }
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
