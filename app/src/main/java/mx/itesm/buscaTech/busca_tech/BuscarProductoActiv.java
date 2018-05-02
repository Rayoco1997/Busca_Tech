package mx.itesm.buscaTech.busca_tech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

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
                    Document doc= Jsoup.connect("https://www.google.com.mx/search?tbm=shop&q="+busqueda).get();
                    Elements resultados = doc.select("div.sh-dlr__list-result");
                    Element resultadoPrimero = resultados.first();



                    ArrayList<String> nombreProductos= new ArrayList<String>();
                    ArrayList<String> precio = new ArrayList<String>();
                    ArrayList<String> tiendas = new ArrayList<String>();

                    ArrayList<Bitmap> imagenes = new ArrayList<Bitmap>();



                    Bitmap bm1 = BitmapFactory.decodeResource(getResources(),R.drawable.comp_1);



                    int count=0;

                    for(Element elemento:resultados){

                        System.out.println("AH NU MA; ELEMENTO CON CLAVE: "+elemento.attr("data-docid"));

                        Elements temp = elemento.select("div.JRlvE.XNeeld");
                        Element element = temp.first();
                        Element child = element.child(0);
                        nombreProductos.add(child.attr("alt"));
                        //nombreProductos[count]= child.attr("alt");
                        //Log.i("Nombre",child.attr("alt"));

                        //System.out.println("Nombre: "+child.attr("alt"));

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

                            imagenes.add(bm1);
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




    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
