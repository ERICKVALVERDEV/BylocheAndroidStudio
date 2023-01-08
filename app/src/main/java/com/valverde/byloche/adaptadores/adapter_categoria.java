package com.valverde.byloche.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.valverde.byloche.Datos.usu_categoria;
import com.valverde.byloche.R;

import java.io.File;
import java.util.List;

public class adapter_categoria
        extends RecyclerView.Adapter<adapter_categoria.MyHolderView> implements  View.OnClickListener{

    private Context context;
    List<usu_categoria> categorialist;
    private View.OnClickListener listener;



    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }

    }

    public class MyHolderView extends RecyclerView.ViewHolder {
        private TextView txt_nombre;
        private ImageView imgcat;


        public MyHolderView(@NonNull View itemView) {
            super(itemView);
            txt_nombre = (TextView) itemView.findViewById(R.id.txt_categoria_nombre);
            imgcat = (ImageView) itemView.findViewById(R.id.imgview_categoria);

        }
    }
    public adapter_categoria(List<usu_categoria> categorialist, Context context){
        this.categorialist = categorialist;
        this.context = context;

    }

    @NonNull
    @Override
    public MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.categoria_list,parent,false);

        //SETONCLICKLISTENER
        view.setOnClickListener(this);

        return new MyHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolderView holder, int position) {
        holder.txt_nombre.setText(categorialist.get(position).getNombre());


        if (categorialist.get(position).getRuta_imagen()!=null){
            //
            cargarImagenWebService(categorialist.get(position).getRuta_imagen(),holder);
        }else{
            holder.imgcat.setImageResource(R.drawable.imagennodisponible);
            Toast.makeText(context, "incorrecto", Toast.LENGTH_SHORT).show();
        }

    }
    private void cargarImagenWebService(String rutaImagen, final adapter_categoria.MyHolderView holder) {
        String ip = context.getString(R.string.rutaImagenes);
        Picasso.get()
                .load(ip+rutaImagen)
                .error(R.drawable.imagennodisponible)
                .tag("Soy un TAG")
                .noFade()
                .into(holder.imgcat);

    }



    @Override
    public int getItemCount() {
        return categorialist.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }


}
