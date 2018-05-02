package mx.itesm.buscaTech.busca_tech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class BuscarProductoActiv extends AppCompatActivity {

    TextInputEditText tiBuscarProducto;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strBuscaTuProducto);
        setContentView(R.layout.activity_buscar_producto);

        tiBuscarProducto = findViewById(R.id.tiBuscarProducto);

        progressDialog= new ProgressDialog(this);

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
        progressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //INICIO DEL METODO PARA LIMPIAR LISTA DE FRAGMENTS

                    String[] listaVacia= new String[0];
                    Bitmap[] listaVaciaBitmap= new Bitmap[0];
                    ListaRVProdFrag fragLista = new ListaRVProdFrag(listaVacia, listaVacia, listaVaciaBitmap, listaVacia, listaVacia, true);
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

                    ArrayList<Bitmap> imagenes = new ArrayList<Bitmap>();



                    Bitmap bm1 = BitmapFactory.decodeResource(getResources(),R.drawable.comp_1);



                    int count=0;

                    String busquedaImagen;

                    for(Element elemento:resultados){

                        System.out.println("AH NU MA; ELEMENTO CON CLAVE: "+elemento.attr("data-docid"));

                        Elements temp = elemento.select("div.JRlvE.XNeeld");
                        Element element = temp.first();
                        Element child = element.child(0);
                        nombreProductos.add(child.attr("alt"));
                        //nombreProductos[count]= child.attr("alt");
                        //Log.i("Nombre",child.attr("alt"));

                        /*
                        byte[] decodedString = Base64.decode(child.attr("src").getBytes(), Base64.URL_SAFE);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imagenes.add(decodedByte);
                        */



                        /*Log.i("CONTENIDO",child.toString());
                        String base64str= child.attr("src");
                        //String base64str= "data:image/jpeg;base64,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";

                        Log.i("AQUI TA EL STRING",base64str);
                        String base64Image = base64str.split(",")[1];
                        byte[] decodedString= android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT);
                        //byte[] decodedString = Base64.getDecoder().decode(child.attr("src").getBytes("UTF-8"));
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        imagenes.add(decodedByte);*/

                        /*
                        try {
                            URL urlImagen = new URL("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQceqJA8Sljx0R_RbNMA8LTLq3gwkOBW7cJPwdH4dPYNGGlHRHb");
                            HttpURLConnection connection = (HttpURLConnection) urlImagen.openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            InputStream input = connection.getInputStream();
                            Bitmap myBitmap = BitmapFactory.decodeStream(input);
                            imagenes.add(myBitmap);
                        } catch (IOException e) {
                            // Log exceptio
                        }*/

                        // can only grab first 100 results
                        String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.116 Safari/537.36";
                        busquedaImagen= URLEncoder.encode(child.attr("alt"), "utf-8");
                        String url1 = "https://www.google.com/search?site=imghp&tbm=isch&source=hp&q="+busquedaImagen;

                        List<String> resultUrls = new ArrayList<String>();

                        try {
                            Document doc1 = Jsoup.connect(url1).timeout(1000).userAgent(userAgent).referrer("https://www.google.com/").get();

                            Elements elements = doc1.select("div.rg_meta");

                            JSONObject jsonObject;
                            for (int i=0; i<1;i++) {
                                Element element1= elements.get(i);
                                if (element1.childNodeSize() > 0) {
                                    jsonObject = (JSONObject) new JSONParser().parse(element1.childNode(0).toString());
                                    resultUrls.add((String) jsonObject.get("ou"));
                                }
                            }

                            System.out.println("number of results: " + resultUrls.size());

                            for (String imageUrl : resultUrls) {
                                Log.i("URL",imageUrl);
                                URL urlImagen = new URL(imageUrl);
                                HttpURLConnection connection = (HttpURLConnection) urlImagen.openConnection();
                                connection.setDoInput(true);
                                connection.connect();
                                InputStream input = connection.getInputStream();
                                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                                imagenes.add(redimensionarImagenMaximo(myBitmap,400,400));
                            }

                        } catch (org.json.simple.parser.ParseException e) {
                            e.printStackTrace();
                        }


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
                        }






                    }

                    String[] nombreProductosArray= new String[count];
                    String[] precioArray= new String[count];

                    Bitmap[] imagenesArray = new Bitmap[count];

                    String[] tiendasArray= new String[count];
                    String[] idPreferenciasArray= new String[count];
                    for (int i = 0; i < idPreferenciasArray.length; i++){
                        idPreferenciasArray[i] = "TIENDA";
                    }

                    fragLista = new ListaRVProdFrag(nombreProductos.toArray(nombreProductosArray), precio.toArray(precioArray), imagenes.toArray(imagenesArray), tiendas.toArray(tiendasArray), idPreferenciasArray, true);
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




    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
