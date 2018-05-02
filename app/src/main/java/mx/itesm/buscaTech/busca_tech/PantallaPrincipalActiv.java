package mx.itesm.buscaTech.busca_tech;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class PantallaPrincipalActiv extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView tvNavNombre;
    TextView tvNavCorreo;
    FirebaseAuth mAuth;

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTitle(R.string.strBuscaTech);
        setContentView(R.layout.activity_pantalla_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();

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
        View headerView = navigationView.getHeaderView(0);
        tvNavNombre = (TextView) headerView.findViewById(R.id.tvNavNombre);
        tvNavCorreo = (TextView) headerView.findViewById(R.id.tvNavCorreo);

        menu = navigationView.getMenu();
        Log.i("AVISO", "entre al on create de pantalla principal");
        cargaInformacionUsuario();
        /*
        File file = new File(getApplicationContext().getFilesDir(),"DatosUsuario");
        if(file.exists()){
            Log.i("InicioSesion:","ARCHIVO ENCONTRADO, ENTRA AL IF");
            mostrarDatos();
        }
        else{
            Log.i("InicioSesion:","ARCHIVO NO ENCONTRADO, ENTRA AL ELSE");
            tvNavNombre.setText("Usuario Invitado");
            tvNavCorreo.setText("");
        }
        */

        String[] nombreProductos = {"Acer 15.6","Asus Vivobook","Acer Aspire 7","Asus 15.6","MSI GL62M"};
        String[] precio = {"$14,499.00","$22,900.00","$24,000.00","$18,800.00","$16,600.00"};
        Bitmap bm1 = BitmapFactory.decodeResource(getResources(),R.drawable.comp_1);
        Bitmap bm2 = BitmapFactory.decodeResource(getResources(),R.drawable.comp_2);
        Bitmap bm3 = BitmapFactory.decodeResource(getResources(),R.drawable.comp_3);
        Bitmap bm4 = BitmapFactory.decodeResource(getResources(),R.drawable.comp_4);
        Bitmap bm5 = BitmapFactory.decodeResource(getResources(),R.drawable.comp_5);
        Bitmap[] imagenes = {bm1,bm2,bm3,bm4,bm5};
        String[] tiendas = {"Tienda A", "Tienda B", "Tienda C", "Tienda D" ,"Tienda E"};
        String[] idPreferencias = {"TIENDA", "TIENDA", "TIENDA", "TIENDA" ,"TIENDA"};


        ListaRVProdFrag fragLista = new ListaRVProdFrag(nombreProductos, precio, imagenes, tiendas, idPreferencias, 1);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutProductos,fragLista);
        transaction.commit();

        cambiarBotonesSiEsInvitado(menu);

    }



    private void cambiarBotonesSiEsInvitado(Menu menu) {
        if(!esUsuario()) {
            MenuItem menuItemFavoritos = menu.findItem(R.id.nav_Preferencias).setEnabled(false);
            MenuItem menuItemSugerirTienda = menu.findItem(R.id.nav_SugerirTienda).setEnabled(false);
            MenuItem menuItemMiPerfil = menu.findItem(R.id.nav_MiPerfil).setEnabled(false);

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Si desea tener acceso a las demás funcionalidades, inicie sesión como usuario");
            builder1.setCancelable(false);

            builder1.setPositiveButton(
                    "Aceptar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });


            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }


    /*
    @Override
    protected void onResume() {
        super.onResume();
        File file = new File(getApplicationContext().getFilesDir(),"DatosUsuario");
        if(file.exists()){
            Log.i("InicioSesion:","ARCHIVO ENCONTRADO, ENTRA AL IF");
            mostrarDatos();
        }
        else{
            Log.i("InicioSesion:","ARCHIVO NO ENCONTRADO, ENTRA AL ELSE");
            tvNavNombre.setText("Usuario Invitado");
            tvNavCorreo.setText("");
        }

        ListaRVProdFrag fragLista = new ListaRVProdFrag();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutProductos,fragLista);
        transaction.commit();
    }
    */

    @Override
    protected void onResume() {
        super.onResume();
        cargaInformacionUsuario();
    }

    private void cargaInformacionUsuario(){
        FirebaseUser user = mAuth.getCurrentUser();
        String nombre = "Invitado";
        String correo = "";
        if (user != null){
            if (user.getDisplayName() != null){
                nombre = user.getDisplayName();
            }
            if (user.getEmail() != null){
                if (!user.getEmail().equals("buscatechoficial@gmail.com")){
                    correo = user.getEmail();
                }
            }
        }
        // El usuario ingresó como invitado
        else {
            nombre = "Invitado";
            correo = "";
        }
        setText(tvNavNombre, nombre);
        setText(tvNavCorreo, correo);
        //tvNavNombre.setText(nombre);
        //tvNavCorreo.setText(correo);
    }


    private boolean esUsuario(){
        Boolean esUsuario = true;
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            if (user.getEmail().equals("buscatechoficial@gmail.com")){
                Log.i("SUPER MEGA AVISO", "SOY UN INVITADO CHIDO");
                esUsuario = false;
            }
        }
        return esUsuario;
    }

    private void setText(final TextView text,final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pantalla_principal, menu);
        return true;
    }*/

    /*@Override
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
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Logout) {
            // BORRA EL ARCHIVO
            /*
            File file = new File(getApplicationContext().getFilesDir(),"DatosUsuario");
            boolean deleted = file.delete();
            */
            mAuth.signOut();
            Toast.makeText(this, "Sesión cerrada correectamente.", Toast.LENGTH_SHORT).show();
            Intent intLogin= new Intent(this,LoginActiv.class);
            finish();
            startActivity(intLogin);


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






}
