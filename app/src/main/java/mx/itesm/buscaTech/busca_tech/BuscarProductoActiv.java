package mx.itesm.buscaTech.busca_tech;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class BuscarProductoActiv extends AppCompatActivity {

    TextInputEditText tiBuscarProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(R.string.strBuscaTuProducto);
        setContentView(R.layout.activity_buscar_producto);

        tiBuscarProducto = findViewById(R.id.tiBuscarProducto);

    }

    public void mandarABusquedaAvanzada(View v){
        Intent intBusquedaAvanzada= new Intent(this,BusquedaAvanzadaActiv.class);
        startActivity(intBusquedaAvanzada);
    }

    public void buscarProducto(View v){
        //String url= tiBuscarProducto.getText().toString();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String url= tiBuscarProducto.getText().toString();
                    Document doc= Jsoup.connect("https://www.google.com.mx/search?tbm=shop&q="+url).get();
                    Elements resultados = doc.select("div.sh-dlr__list-result");
                    Element resultadoPrimero = resultados.first();

                    for(Element elemento:resultados){
                        System.out.println("AH NU MA; ELEMENTO CON CLAVE: "+elemento.attr("data-docid"));

                        Elements temp = elemento.select("div.JRlvE.XNeeld");
                        Element element = temp.first();
                        Element child = element.child(0);
                        Log.i("Nombre",child.attr("alt"));
                        //System.out.println("Nombre: "+child.attr("alt"));

                        Elements tempo = elemento.select("span.O8U6h");
                        Element element1 = tempo.first();

                        Log.i("Precio",element.text());
                        //System.out.println("Precio: "+element1.text());
                        Elements lugar = elemento.select("div.mQ35Be");
                        Element primerLugar = lugar.first();
                        Element childLugar = primerLugar.child(0);
                        String lug = childLugar.text();
                        lug = lug.replace(element1.text()+" en ","");
                        Log.i("Lugar", lug);
                        //System.out.println("Lugar: "+lug);

                    }
                } catch (IOException e) {
                    Log.i("ERROR", "ENTRE AL PUTO ERROR DE SOPITA");
                }

            }
        }).start();

/*
        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.google.com.mx/search?tbm=shop&q="+url).get();

        } catch (IOException e) {
            Log.i("ERROR", "ENTRE AL ERROR DE SOPITA");
            e.printStackTrace();
        }
        Elements resultados = doc.select("div.sh-dlr__list-result");
        Element resultadoPrimero = resultados.first();

        for(Element elemento:resultados){
            System.out.println("AH NU MA; ELEMENTO CON CLAVE: "+elemento.attr("data-docid"));

            Elements temp = elemento.select("div.JRlvE.XNeeld");
            Element element = temp.first();
            Element child = element.child(0);
            Log.i("Nombre",child.attr("alt"));
            //System.out.println("Nombre: "+child.attr("alt"));

            Elements tempo = elemento.select("span.O8U6h");
            Element element1 = tempo.first();

            Log.i("Precio",element.text());
            //System.out.println("Precio: "+element1.text());
            Elements lugar = elemento.select("div.mQ35Be");
            Element primerLugar = lugar.first();
            Element childLugar = primerLugar.child(0);
            String lug = childLugar.text();
            lug = lug.replace(element1.text()+" en ","");
            Log.i("Lugar", lug);
            //System.out.println("Lugar: "+lug);

        }*/


    }




    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
