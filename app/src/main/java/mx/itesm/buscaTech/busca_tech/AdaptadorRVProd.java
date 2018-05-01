package mx.itesm.buscaTech.busca_tech;

import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by rayoco on 20/02/18.
 */

public class AdaptadorRVProd extends RecyclerView.Adapter<AdaptadorRVProd.Vista>{

    private String[] arrNombreProd;
    private String[] arrPrecioProd;
    private Bitmap[] arrImagenProd;
    private String[] arrTiendaProd;

    public AdaptadorRVProd(String[] nombres, String[] precios, Bitmap[] imagenes, String[] tiendas){
        arrNombreProd = nombres;
        arrPrecioProd = precios;
        arrImagenProd = imagenes;
        arrTiendaProd = tiendas;
    }

    @Override
    public Vista onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tarjeta_producto,parent,false);
        return new Vista(cv);
    }

    @Override
    public void onBindViewHolder(Vista holder, int position) {
        CardView tarjeta = holder.tarjeta;
        //Poblar los datos de la tarjeta
        TextView tvNombreProd = tarjeta.findViewById(R.id.tvNombreProd);
        TextView tvPrecioProd = tarjeta.findViewById(R.id.tvPrecioProd);
        ImageView ivImgProd = tarjeta.findViewById(R.id.ivImgProd);
        TextView tvTiendaProd = tarjeta.findViewById(R.id.tvTiendaProd);
        tvNombreProd.setText(arrNombreProd[position]);
        tvPrecioProd.setText(arrPrecioProd[position]);
        ivImgProd.setImageBitmap(arrImagenProd[position]);
        tvTiendaProd.setText(arrTiendaProd[position]);

    }

    @Override
    public int getItemCount() {
        return arrNombreProd.length;
    }

    public class Vista extends RecyclerView.ViewHolder{
        private final CardView tarjeta;

        public Vista(CardView tarjeta){
            super(tarjeta);
            this.tarjeta = tarjeta;
        }
    }
}