package mx.itesm.buscaTech.busca_tech;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MisPreferenciasActiv extends AppCompatActivity {

    DatabaseReference databasePreferences;
    FirebaseAuth mAuth;

    ArrayList<ArrayList<String>> matriz = new ArrayList<ArrayList<String>>();

    Bitmap bmLogo;
    Boolean enEjecucion = true;

    Bitmap[] imagenesBm;
    ArrayList<Bitmap> imagenesAL;
    int posicionLista;
    ProgressDialog pD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strMisPreferencias);
        setContentView(R.layout.activity_mis_preferencias);
        mAuth = FirebaseAuth.getInstance();
        databasePreferences = FirebaseDatabase.getInstance().getReference("preferencias");

        bmLogo = BitmapFactory.decodeResource(getResources(),R.drawable.logobuscatech);
        bmLogo=redimensionarImagenMaximo(bmLogo,400,400);

        crearMatriz();
        // progressDialog
        // agregarPreferencia();
        pD = new ProgressDialog(this);
        pD.setMessage("Cargando preferencias");
        pD.setCanceledOnTouchOutside(false);
        pD.show();
        imagenesAL = new ArrayList<Bitmap>();
        obtenerFavoritos();

        // crearLista(matriz[0], matriz[1], matriz[2], matriz[3], matriz[4]);
        Log.i("Tamaño", matriz.get(0).size() + "");
        for (int i = 0; i < matriz.get(0).size(); i++){
            Log.i("Lista", matriz.get(0).get(i) +
                    "\n" + matriz.get(1).get(i) +
                    "\n" + matriz.get(2).get(i) +
                    "\n" + matriz.get(3).get(i) +
                    "\n" + matriz.get(4).get(i));
            }

    }


    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void crearLista(ArrayList<String> preciosArr,
                             ArrayList<String> nombresArr,
                             ArrayList<String> tiendasArr,
                             ArrayList<String> imagenesArr,
                             ArrayList<String> direccionesArr,
                             ArrayList<String> preferenciasArr) {

        if(matriz.get(0).size() == 0){
            mostrarDialogo("No tienes guardado ningún producto.");
        }


        String[] precios = new String[preciosArr.size()];
        for (int i = 0; i < preciosArr.size(); i++){
            precios[i] = preciosArr.get(i);
        }

        String[] nombres = new String[nombresArr.size()];
        for (int i = 0; i < nombresArr.size(); i++){
            nombres[i] = nombresArr.get(i);
        }

        String[] tiendas = new String[tiendasArr.size()];
        for (int i = 0; i < tiendasArr.size(); i++){
            tiendas[i] = tiendasArr.get(i);
        }

        Bitmap[] imagenesBitmap = new Bitmap[imagenesArr.size()];
        String[] imagenes = new String[imagenesArr.size()];

        for (int i = 0; i < imagenesArr.size(); i++){
            imagenes[i] = imagenesArr.get(i);
        }



        String[] direcciones = new String[direccionesArr.size()];
        for (int i = 0; i < direccionesArr.size(); i++){
            direcciones[i] = direccionesArr.get(i);
        }

        String[] idPreferencias = new String[preferenciasArr.size()];
        for (int i = 0; i < preferenciasArr.size(); i++){
            idPreferencias[i] = preferenciasArr.get(i);
        }



        imagenesBm = new Bitmap[imagenesArr.size()];
        Bitmap bm;
        /*for(int i= 0; i<imagenes.length;i++){
            Log.i("LISTA CONT:",imagenes[i]);
        }*/
        //pD.show();
        /*for (int i = 0; i < imagenesArr.size(); i++){
            posicionLista=i;

            new DescargaImagenTarea().execute(imagenes[i]);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try{

                while(enEjecucion) {

                    Thread.sleep(500);
                    Log.i("ciclo","Sigo acá"+i);
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }*/
            /*if(!imagenes[i].equals("Logo")) {
                try {
                    URL urlImagen = new URL(imagenes[i]);
                    HttpURLConnection connection = null;
                    connection = (HttpURLConnection) urlImagen.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bm = BitmapFactory.decodeStream(input);
                    imagenesBm[i]=redimensionarImagenMaximo(bm,400,400);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                imagenesBm[i] = bmLogo;
            }*/

        //}
        imagenesAL.toArray(imagenesBm);
        pD.dismiss();
        Log.i("SIZE",""+imagenesAL.size());
        for(int i=0; i<imagenesAL.size();i++){
            Log.i("Elemento: ",""+imagenesBm[i]);
        }

        ListaRVProdFrag fragLista = new ListaRVProdFrag(nombres, precios, tiendas, idPreferencias, 2, imagenes, direcciones);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layoutFavoritos, fragLista);
        transaction.commit();
    }
    public Bitmap redimensionarImagenMaximo(Bitmap mBitmap, float newWidth, float newHeigth){
        //Redimensionamos
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeigth) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }


    private void agregarPreferencia(){
        String idUsuario = mAuth.getCurrentUser().getUid();
        String precio = mAuth.getCurrentUser().getUid();
        String nombre = mAuth.getCurrentUser().getUid();
        String tienda = mAuth.getCurrentUser().getUid();
        String imagen = "http://icons.iconarchive.com/icons/marcus-roberto/google-play/512/Google-Chrome-icon.png";
        String direccion = "https://www.google.com/";

        String idPreferencia = databasePreferences.push().getKey();
        Preferencias preferencias = new Preferencias(idUsuario, precio, nombre, tienda, imagen, direccion);
        databasePreferences.child(idPreferencia).setValue(preferencias);

    }

    private void obtenerFavoritos() {
        databasePreferences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot preferenciaSnapshot : dataSnapshot.getChildren()){
                    Preferencias preferencias = preferenciaSnapshot.getValue(Preferencias.class);
                    if (preferencias.getIdUsuario().equals(mAuth.getCurrentUser().getUid())){
                        agregarMatriz(preferencias.precio, preferencias.nombre, preferencias.tienda, preferencias.imagen, preferencias.direccion, preferenciaSnapshot.getKey());
                        Log.i("KEY", preferenciaSnapshot.getKey());
                        Log.i("DATOS", preferencias.toString());
                    }
                }
                Log.i("Tamaño", matriz.get(0).size() + "");
                crearLista(matriz.get(0), matriz.get(1), matriz.get(2), matriz.get(3), matriz.get(4), matriz.get(5));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Log.i("TamLista", matriz.get(0).size() + "");
        for (int i = 0; i < matriz.get(0).size(); i++){
            Log.i("Lista", matriz.get(0).get(i) +
                    "\n" + matriz.get(1).get(i) +
                    "\n" + matriz.get(2).get(i) +
                    "\n" + matriz.get(3).get(i) +
                    "\n" + matriz.get(4).get(i));
        }



    }


    private void mostrarDialogo(String mensaje) {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("AVISO");
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


    public void agregarMatriz(String precio, String nombre, String tienda, String imagen, String direccion, String idPreferencia){
        matriz.get(0).add(precio);
        matriz.get(1).add(nombre);
        matriz.get(2).add(tienda);
        matriz.get(3).add(imagen);
        matriz.get(4).add(direccion);
        matriz.get(5).add(idPreferencia);


        // Log.i("Inner", inner.get(0) + "\n" + inner.get(1) + "\n" + inner.get(2) + "\n" + inner.get(3) + "\n" + inner.get(4));
        Log.i("AgregarM", matriz.get(0).get(0));

    }


    public void crearMatriz() {
        ArrayList<String> precio = new ArrayList<String>();
        ArrayList<String> nombre = new ArrayList<String>();
        ArrayList<String> tienda = new ArrayList<String>();
        ArrayList<String> imagen = new ArrayList<String>();
        ArrayList<String> direccion = new ArrayList<String>();
        ArrayList<String> idPreferencia = new ArrayList<String>();
        matriz.add(precio);
        matriz.add(nombre);
        matriz.add(tienda);
        matriz.add(imagen);
        matriz.add(direccion);
        matriz.add(idPreferencia);
    }

    /*private Bitmap descargarImagen(String direccion) {
        Bitmap bm = null;

        if(!direccion.equals("Logo")) {
            try {
                URL urlImagen = new URL(direccion);
                HttpURLConnection connection = (HttpURLConnection) urlImagen.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bm = BitmapFactory.decodeStream(input);
                //imagenesBm[i]=redimensionarImagenMaximo(bm,400,400);
                return bm;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            return bmLogo;
        }
        return bm;
    }*/


    /*private class DescargaImagenTarea extends AsyncTask<String, Void, Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... urls) {
            enEjecucion=true;
            Log.i("ESTO ES URL 0",urls[0]);
            return descargarImagen(urls[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(imagenesBm != null){
                imagenesBm[posicionLista]=(redimensionarImagenMaximo(result,400,400));
                Log.i("OPEDIT:","Ya añadí al arreglo imagenes");
                Log.i("IMAGENES:","POSICIÓN: "+posicionLista);
            }else{
                Log.i("OPEDIT:","Hubo un error al añadir al arreglo.");
            }
            enEjecucion=false;

        }
    }*/

    /*
    public static Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(x);
    }
    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }

    */


    /*
    private Bitmap descargarImagen(String direccion) {
        Bitmap bitmap = null;

        try {
            Log.i("URL",direccion);
            URL urlImagen = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) urlImagen.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            Log.i("descargarImagen", "EXCEPCION: " + e);
        }
        return bitmap;
    }

    private class DescargaImagenTarea extends AsyncTask<String, Void, Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... urls) {
            return getBitmapFromURL(urls[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(imagenesAL != null){
                imagenesAL.add(redimensionarImagenMaximo(result,400,400));
                Log.i("OPEDIT:","Ya añadí al arreglo imagenesAL");
            }else{
                Log.i("OPEDIT:","Hubo un error al añadir al arreglo.");
            }

        }
    }

    */



    /*
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
    */




}
