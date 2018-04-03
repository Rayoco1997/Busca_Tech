package mx.itesm.buscaTech.busca_tech;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class PantallaPrincipalActiv extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView tvNavNombre;
    TextView tvNavCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strBuscaTech);
        setContentView(R.layout.activity_pantalla_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        File file = new File(getApplicationContext().getFilesDir(),"DatosUsuario");
        View headerView = navigationView.getHeaderView(0);
        tvNavNombre = (TextView) headerView.findViewById(R.id.tvNavNombre);
        tvNavCorreo = (TextView) headerView.findViewById(R.id.tvNavCorreo);
        if(file.exists()){
            mostrarDatos();
        }
        else{
            tvNavNombre.setText("Usuario Invitado");
            tvNavCorreo.setText("");
        }

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pantalla_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Logout) {
            // BORRA EL ARCHIVO
            File file = new File(getApplicationContext().getFilesDir(),"DatosUsuario");
            boolean deleted = file.delete();
            Intent intLogin= new Intent(this,LoginActiv.class);
            startActivity(intLogin);
            finish();
        } else if (id == R.id.nav_BuscarProducto) {
            Intent intBuscarProducto=new Intent(this, BuscarProductoActiv.class);
            startActivity(intBuscarProducto);
        } else if (id == R.id.nav_MiPerfil) {
            Intent intMiPerfil= new Intent(this,MiPerfilActiv.class);
            startActivity(intMiPerfil);

        } else if (id == R.id.nav_SugerirTienda) {
            Intent intSugerirTienda= new Intent(this,SugerirTiendaActiv.class);
            startActivity(intSugerirTienda);
        } else if (id == R.id.nav_Preferencias) {
            Intent intMisPreferencias= new Intent(this, MisPreferenciasActiv.class);
            startActivity(intMisPreferencias);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void mostrarDatos() {
        // String yourFilePath = getApplicationContext().getFilesDir() + "/" + "DatosUsuario";
        // File yourFile = new File( yourFilePath );
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = null;
            fis = getApplicationContext().openFileInput("DatosUsuario");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // String correo = yourFile.toString();
        String correo = sb.toString();
        Log.i("Datos", "Los datos que tiene el archivo son "+ correo);
        new BDUsuario(correo).execute();
    }

    public void obtenerDatos(String correo){
        Usuario usuario = new Usuario();
        UsuarioBD bd = UsuarioBD.getInstance(this);
        usuario = bd.usuarioDAO().buscarPorCorreo(correo);
        tvNavCorreo.setText(usuario.getCorreo().toString());
        tvNavNombre.setText(usuario.getNombreUsuario().toString());
    }

    private class BDUsuario extends AsyncTask<Void, Void, Void> {
        String correo;
        public BDUsuario(String correo){
            this.correo = correo;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            obtenerDatos(correo);
            return null;
        }

    }
}
