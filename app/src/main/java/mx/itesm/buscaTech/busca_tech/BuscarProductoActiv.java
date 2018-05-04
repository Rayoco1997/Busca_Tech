package mx.itesm.buscaTech.busca_tech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class BuscarProductoActiv extends AppCompatActivity {

    TextInputEditText tiBuscarProducto;
    ProgressDialog progressDialog;
    //String dirImagen;
    // ArrayList<Bitmap> imagenes;
    ArrayList<String> imagenesLink;
    Bitmap bmLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strBuscaTuProducto);
        setContentView(R.layout.activity_buscar_producto);

        tiBuscarProducto = findViewById(R.id.tiBuscarProducto);

        progressDialog= new ProgressDialog(this);
        bmLogo = BitmapFactory.decodeResource(getResources(),R.drawable.logobuscatech);


        Intent intent = getIntent();
        String busquedaAvz = intent.getStringExtra("busqueda");

        if(busquedaAvz!=null){
            buscarProducto(this.findViewById(android.R.id.content));
            //Log.i("BUSQUEDA AVANZA: ",busquedaAvz);
        }
    }

    public void mandarABusquedaAvanzada(View v){
        Intent intBusquedaAvanzada= new Intent(this,BusquedaAvanzadaActiv.class);
        startActivity(intBusquedaAvanzada);
    }


    public void buscarProducto(View v){
        //String url= tiBuscarProducto.getText().toString();
        progressDialog.setMessage("Buscando...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //INICIO DEL METODO PARA LIMPIAR LISTA DE FRAGMENTS

                    String[] listaVacia= new String[0];
                    ListaRVProdFrag fragLista = new ListaRVProdFrag(listaVacia, listaVacia, listaVacia, listaVacia, 0, listaVacia);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.layoutProductos,fragLista);
                    transaction.commit();
                    //progressDialog.dismiss();
                    //FIN DEL METODO

                    //Llamar al algoritmo de búsqueda
                    Intent intent = getIntent();
                    String busquedaAvz=intent.getStringExtra("busqueda");
                    String url;

                    //Captura de texto desde el text field para buscar
                    if(busquedaAvz==null) {
                        url = tiBuscarProducto.getText().toString();

                    }else{
                        url = busquedaAvz;
                    }


                    String busqueda = URLEncoder.encode(url, "utf-8");
                    //Log.i("AQUI TA BUSQUEDA", busqueda);
                    Document doc= Jsoup.connect("https://www.google.com.mx/search?tbm=shop&q="+busqueda).get();
                    //Document doc= Jsoup.connect("https://www.google.com.mx/search?tbm=shop&q="+busqueda).userAgent("Mozilla/5.0 (X11; CrOS x86_64 8172.45.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.64 Safari/537.36").referrer("http://www.google.com").get();
                    Elements resultados = doc.select("div.sh-dlr__list-result");
                    Element resultadoPrimero = resultados.first();

                    ArrayList<String> nombreProductos= new ArrayList<String>();
                    ArrayList<String> precio = new ArrayList<String>();
                    ArrayList<String> tiendas = new ArrayList<String>();

                    imagenesLink = new ArrayList<String>();


                    int count=0;
                    int j = 0;

                    String busquedaImagen;

                    for(Element elemento:resultados){
                        if(j>=5){
                            break;
                        }
                        System.out.println("AH NU MA; ELEMENTO CON CLAVE: "+elemento.attr("data-docid"));

                        Elements temp = elemento.select("div.JRlvE.XNeeld");
                        Element element = temp.first();
                        Element child = element.child(0);
                        nombreProductos.add(child.attr("alt"));
                        //nombreProductos[count]= child.attr("alt");
                        //Log.i("Nombre",child.attr("alt"));


                        // can only grab first 100 results
                        String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";
                        busquedaImagen= URLEncoder.encode(child.attr("alt"), "utf-8");
                        String url1 = "https://www.googleapis.com/customsearch/v1?q="+busquedaImagen+"&cx=013957929780137382896%3Aevgtatruacs&num=1&searchType=image&key=AIzaSyBf8R40a48_oa4DGjtzVQvhavgH8K1ndgk";

                        //List<String> resultUrls = new ArrayList<String>();

                        new DescargaTextoTarea().execute(url1);

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //new DescargaTextoTarea().execute(url1);
                        /*Thread.sleep(10000);
                        Log.i("URL",dirImagen);
                        URL urlImagen = new URL(dirImagen);
                        HttpURLConnection connection = (HttpURLConnection) urlImagen.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        imagenes.add(redimensionarImagenMaximo(myBitmap,400,400));*/


                        //IMAGEN
                        System.out.println("Nombre: "+child.attr("alt"));

                        Elements tempo = elemento.select("span.O8U6h");
                        Element element1 = tempo.first();

                        precio.add(element1.text());
                        //precio[count]= element1.text();
                        //Log.i("Precio",element1.text());
                        //System.out.println("Precio: "+element1.text());
                        Elements lugar = elemento.select("div.mQ35Be");
                        Element primerLugar = lugar.first();
                        Element childLugar = primerLugar.child(0);
                        String lug = childLugar.text();
                        lug = lug.replace(element1.text()+" en ","");
                        //Log.i("Lugar", lug);
                        if (lug.contains("tiendas")){
                            nombreProductos.remove(count);
                            precio.remove(count);
                        }else{
                            tiendas.add(lug);
                            //tiendas[count]= lug;

                            //System.out.println("Lugar: "+lug);

                            //imagenes.add(bm1);
                            //imagenes[count]=bm1;
                            count++;
                            j+=1;
                        }






                    }

                    String[] nombreProductosArray= new String[count];
                    String[] precioArray= new String[count];

                    //Bitmap[] imagenesArray = new Bitmap[count];

                    String[] tiendasArray= new String[count];
                    String[] idPreferenciasArray= new String[count];
                    for (int i = 0; i < idPreferenciasArray.length; i++){
                        idPreferenciasArray[i] = "TIENDA";
                    }

                    String[] strImagenesArray = new String[count];



                    fragLista = new ListaRVProdFrag(nombreProductos.toArray(nombreProductosArray), precio.toArray(precioArray), tiendas.toArray(tiendasArray), idPreferenciasArray, 0, imagenesLink.toArray(strImagenesArray));
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.layoutProductos,fragLista);
                    transaction.commit();


                } catch (IOException e) {
                    Log.i("ERROR JSOUP", e.getLocalizedMessage());
                }

                progressDialog.dismiss();


            }
        }).start();
        //progressDialog.dismiss();

    }

    /*
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
    */



    /*public void procesarImagen(String dirImagen){
        try {
            Log.i("URL",dirImagen);
            URL urlImagen = new URL(dirImagen);
            HttpURLConnection connection = (HttpURLConnection) urlImagen.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            imagenes.add(redimensionarImagenMaximo(myBitmap,400,400));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/



    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private InputStream abrirConexion(String direccionRecurso) throws IOException {
        InputStream flujoEntrada = null;
        URL url = new URL(direccionRecurso);
        // Crea el enlace de comunicaciÃ³n entre la app y el url
        URLConnection conexion = url.openConnection();

        if (!(conexion instanceof HttpURLConnection)) {
            throw new IOException("No es una conexiÃ³n HTTP");
        }

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conexion;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect(); // Abre el enlace de comunicaciÃ³n
            int respuesta = httpConn.getResponseCode(); // Respuesta

            if (respuesta == HttpURLConnection.HTTP_OK) {
                // Exito en la conexiÃ³n
                flujoEntrada = httpConn.getInputStream(); // Obtenemos el flujo para leer los datos
            }
        } catch (Exception e) {
            Log.d("Networking", e.getLocalizedMessage());
            throw new IOException("Error conectando a: " + direccionRecurso);
        }

        return flujoEntrada;    // Entrega el flujo que ya se puede leer
    }

    // Descarga un recurso de texto desde la red
    private String descargarTexto(String direccion) {
        int tamBuffer = 2000;   // Paquetes de texto
        InputStream flujoEntrada = null;
        try {
            flujoEntrada = abrirConexion(direccion);  // Estable y abre la conexiÃ³n
        } catch (IOException e) {
            return "Error en la descarga de " + direccion;
        }

        // Lectura 'normal', como cualquier flujo de entrada
        InputStreamReader isr = new InputStreamReader(flujoEntrada);
        int numCharLeidos;
        StringBuffer contenido = new StringBuffer();
        char[] buffer = new char[tamBuffer];
        try {
            while ((numCharLeidos = isr.read(buffer)) > 0) {    // Mientras lee caracteres
                //convierte el arreglo de caracteres en cadena
                String cadena =
                        String.copyValueOf(buffer, 0, numCharLeidos);
                contenido.append(cadena);
                buffer = new char[tamBuffer];
            }
            flujoEntrada.close();
        } catch (IOException e) {
            return "Error leyendo los datos";
        }
        return contenido.toString();
    }


    // Clase para ejecutar cÃ³digo en segundo plano (un thread diferente al de UI)
    private class DescargaTextoTarea extends AsyncTask<String, Void, String>
    {
        protected String doInBackground(String... urls) {
            return descargarTexto(urls[0]); // Descarga el contenido
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                org.json.JSONObject diccionario = new org.json.JSONObject(result);
                org.json.JSONArray dArrayItems = diccionario.getJSONArray("items");
                Log.i("onPostExecute",dArrayItems.toString());
                org.json.JSONObject objZero = dArrayItems.getJSONObject(0);
                String dirImg = objZero.getString("link");
                Log.i("onPostExecuteLink img:",dirImg);
                imagenesLink.add(dirImg);

                //new DescargaImagenTarea().execute(dirImg);

                //procesarImagen(dirImg);
                /*try {
                    Log.i("URL",dirImg);
                    URL urlImagen = new URL(dirImg);
                    HttpURLConnection connection = (HttpURLConnection) urlImagen.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    imagenes.add(redimensionarImagenMaximo(myBitmap,400,400));
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            } catch (JSONException e) {
                //Toast.makeText(BuscaLibrosActiv.this, "No se encontró un libro con el ISBN "+ISBN+", o no se encontraron suficientes datos, intenta nuevamente.", Toast.LENGTH_SHORT).show();
                //BuscaLibrosActiv.this.finish();
                e.printStackTrace();
                /*
                imagenes.add(redimensionarImagenMaximo(bmLogo,400,400));
                imagenesLink.add("Logo");
                Log.i("OPEDTT:","Hubo un error al encontrar imagen, añadí un placeholder.");
                */
            }

        }
    }


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
    */

    /*
    private class DescargaImagenTarea extends AsyncTask<String, Void, Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... urls) {
            return descargarImagen(urls[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(imagenes != null){
                imagenes.add(redimensionarImagenMaximo(result,400,400));
                Log.i("OPEDIT:","Ya añadí al arreglo imagenes");
            }else{
                Log.i("OPEDIT:","Hubo un error al añadir al arreglo.");
            }

        }
    }

    */
}
