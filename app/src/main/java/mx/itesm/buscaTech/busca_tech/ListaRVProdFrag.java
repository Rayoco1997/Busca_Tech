package mx.itesm.buscaTech.busca_tech;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListaRVProdFrag extends Fragment {

    private RecyclerView rvProductos;

    public ListaRVProdFrag() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        rvProductos = getActivity().findViewById(R.id.rvProductos);
        String[] nombreProductos = {"Acer 15.6","Asus Vivobook","Acer Aspire 7","Asus 15.6","MSI GL62M"};
        String[] precio = {"$14,499.00","$22,900.00","$24,000.00","$18,800.00","$16,600.00"};
        Bitmap bm1 = BitmapFactory.decodeResource(getResources(),R.drawable.comp_1);
        Bitmap bm2 = BitmapFactory.decodeResource(getResources(),R.drawable.comp_2);
        Bitmap bm3 = BitmapFactory.decodeResource(getResources(),R.drawable.comp_3);
        Bitmap bm4 = BitmapFactory.decodeResource(getResources(),R.drawable.comp_4);
        Bitmap bm5 = BitmapFactory.decodeResource(getResources(),R.drawable.comp_5);
        Bitmap[] imagenes = {bm1,bm2,bm3,bm4,bm5};
        AdaptadorRVProd adaptador = new AdaptadorRVProd(nombreProductos,precio,imagenes);
        rvProductos.setAdapter(adaptador);
        rvProductos.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_rv_prod, container, false);
    }

}
