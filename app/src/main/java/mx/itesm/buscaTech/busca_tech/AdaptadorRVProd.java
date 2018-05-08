package mx.itesm.buscaTech.busca_tech;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by rayoco on 20/02/18.
 */

public class AdaptadorRVProd extends RecyclerView.Adapter<AdaptadorRVProd.Vista> {
    private final ClickHandler clickHandler;
    private String[] arrNombreProd;
    private String[] arrPrecioProd;
    private String[] arrTiendaProd;
    private String[] arrIdPreferenciaProd;
    private String[] arrImagenLinkProd;
    private String[] arrDirecciones;
    Context thisContext;
    int agregar;

    public AdaptadorRVProd(String[] nombres, String[] precios, String[] tiendas, final ClickHandler clickHandler, String[] idPreferenciaProd, String[] imagenesLink, int agregar, String[] direcciones){
        arrNombreProd = nombres;
        arrPrecioProd = precios;
        arrTiendaProd = tiendas;
        this.clickHandler = clickHandler;
        arrIdPreferenciaProd = idPreferenciaProd;
        arrImagenLinkProd = imagenesLink;
        this.agregar = agregar;
        arrDirecciones = direcciones;
    }

    @Override
    public Vista onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tarjeta_producto,parent,false);
        thisContext = parent.getContext();
        return new Vista(cv);
    }

    @Override
    public void onBindViewHolder(Vista holder, final int position) {
        CardView tarjeta = holder.tarjeta;
        //Poblar los datos de la tarjeta
        TextView tvNombreProd = tarjeta.findViewById(R.id.tvNombreProd);
        TextView tvPrecioProd = tarjeta.findViewById(R.id.tvPrecioProd);
        ImageView ivImgProd = tarjeta.findViewById(R.id.ivImgProd);
        TextView tvTiendaProd = tarjeta.findViewById(R.id.tvTiendaProd);
        tvTiendaProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("dando click", arrDirecciones[position]+"");
                //String url = arrDirecciones[position];
                //Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                //startActivity(i);
            }
        });
        TextView tvIdPreferenciasProd = tarjeta.findViewById(R.id.tvIdPreferenciaProd);
        TextView tvImagenLink = tarjeta.findViewById(R.id.tvImagenLink);
        ImageButton imbtn = tarjeta.findViewById(R.id.imbtnGuardar);
        tvNombreProd.setText(arrNombreProd[position]);
        tvPrecioProd.setText(arrPrecioProd[position]);
        // Dirección de la imagen en la cardView
        Log.i("ImagenLink", arrImagenLinkProd[position]+"");
        if (arrImagenLinkProd[position] == null){
            Picasso.with(thisContext).load("https://blog.sqlauthority.com/i/a/errorstop.png").into(ivImgProd);

        } else {
            Picasso.with(thisContext).load(arrImagenLinkProd[position]).into(ivImgProd);

        }
        // 0 es buscarProducto
        // 1 es pantallaPrincipal
        // 2 es misPreferencias
        if (agregar == 2){
           imbtn.setImageResource(android.R.drawable.ic_delete);
        }
        tvTiendaProd.setText(arrTiendaProd[position]);
        holder.clickHandler = this.clickHandler;
        tvIdPreferenciasProd.setText(arrIdPreferenciaProd[position]);
        tvImagenLink.setText(arrImagenLinkProd[position]);

    }

    @Override
    public int getItemCount() {
        return arrNombreProd.length;
    }

    public class Vista extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final CardView tarjeta;
        private ClickHandler clickHandler;

        public Vista(CardView tarjeta){
            super(tarjeta);
            this.tarjeta = tarjeta;
            ImageButton myButton = (ImageButton) itemView.findViewById(R.id.imbtnGuardar);
            myButton.setOnClickListener(this);

        }

        @Override
        public void onClick(final View v) {
            if (clickHandler != null) {
                clickHandler.onMyButtonClicked(getAdapterPosition());
            }
        }
    }
}