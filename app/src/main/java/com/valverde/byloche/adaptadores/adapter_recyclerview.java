package com.valverde.byloche.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.valverde.byloche.Datos.usu_producto;
import com.valverde.byloche.R;

import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class adapter_recyclerview extends RecyclerView.Adapter<adapter_recyclerview.MyViewHolder> implements  View.OnClickListener {
    //GETCONTEXT 
    private Context mCtx;
    List<usu_producto> product_list;
    private View.OnClickListener listener;

    @Override
    public void onClick(View view) {
        if(listener!=null){
            listener.onClick(view);
        }

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
         public TextView txtnombre, txtcategoria, txtprecio, txtdescripcion;
         public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtnombre = (TextView) itemView.findViewById(R.id.txt_nombre);
            txtcategoria = (TextView)itemView.findViewById(R.id.txt_categoria);
            txtprecio = (TextView)itemView.findViewById(R.id.txt_precio);
            txtdescripcion = (TextView)itemView.findViewById(R.id.txt_descripcion);
            imageView = (ImageView)itemView.findViewById(R.id.imageViewProducto);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)


    public adapter_recyclerview(List<usu_producto> product_list,Context mCtx) {
        this.mCtx = mCtx;
        this.product_list = product_list;
    }



    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater)mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.producto_list,parent,false);

        //SETONCLICKLISTENER
        view.setOnClickListener(this);

        return new MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.txtnombre.setText(product_list.get(position).getNombre());
        holder.txtcategoria.setText(String.valueOf(product_list.get(position).getCategoria()));
        holder.txtprecio.setText(String.valueOf(product_list.get(position).getPrecio()));
        holder.txtdescripcion.setText(product_list.get(position).getDescripcion());
        if (product_list.get(position).getRuta_image()!=null){
            cargarImagenWebService(product_list.get(position).getRuta_image(),holder);
        }else{
            holder.imageView.setImageResource(R.drawable.imagennodisponible);
            Toast.makeText(mCtx, "incorrecto", Toast.LENGTH_SHORT).show();
        }
    }
    private void cargarImagenWebService(String rutaImagen, final MyViewHolder holder) {

        String ip2 = mCtx.getString(R.string.rutaImagenes);

        Picasso.get().load(ip2+"/"+rutaImagen).into(holder.imageView);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return product_list.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

}
