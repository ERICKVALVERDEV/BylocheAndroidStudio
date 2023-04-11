package com.valverde.byloche.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;
import com.valverde.byloche.R;
import com.valverde.byloche.fragments.Online.VentasDetalleOnline;

import java.util.List;

public class adapter_recyclerCarroOnline extends RecyclerView.Adapter<adapter_recyclerCarroOnline.MyviewHolder>{
    public List<VentasDetalleOnline> listCarrito;
    public Context mcontext;
    private adapter_recyclerCarroOnline.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(adapter_recyclerCarroOnline.OnItemClickListener listener) {
        mListener = listener;
    }

    public adapter_recyclerCarroOnline(List<VentasDetalleOnline> listCarrito, Context mcontext) {
        this.listCarrito = listCarrito;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public adapter_recyclerCarroOnline.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.adapter_listview_carro, parent, false);
        return new adapter_recyclerCarroOnline.MyviewHolder(view, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull adapter_recyclerCarroOnline.MyviewHolder holder, final int position) {
        holder.plateName.setText(listCarrito.get(position).getMenu().getNombre());
        holder.platePrice.setText("$" + String.valueOf(listCarrito.get(position).getPrecioUnidad()));
        holder.plateQuantity.setText("Cantidad: " + String.valueOf(listCarrito.get(position).getCantidad()));
        holder.plateState.setText(listCarrito.get(position).getEstado().toString());
        // TODO: Replace plateDetails by plates that are excluded
        holder.plateDetails.setText(listCarrito.get(position).getDetalleProductosVenta().get(0).getNombreProducto());
        if (listCarrito.get(position).getMenu().getImagen() != null){
            loadImageWebService(listCarrito.get(position).getMenu().getImagen(), holder);
        }else{
            holder.plateImage.setImageResource(R.drawable.imagennodisponible);
            Toast.makeText(mcontext, "incorrecto", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return listCarrito.size();
    }

    private void loadImageWebService(String rutaImagen, final adapter_recyclerCarroOnline.MyviewHolder holder) {
        String ip = mcontext.getString(R.string.rutaImagenes);
        Picasso.get()
                .load(ip+rutaImagen)
                .error(R.drawable.imagennodisponible)
                .tag("Soy un TAG")
                .noFade()
                .into(holder.plateImage);

    }

    public class MyviewHolder extends RecyclerView.ViewHolder {

        TextView plateName, platePrice, plateDetails, plateState, plateQuantity;
        ImageView plateImage;

        ImageView btn_eliminar;
        MaterialCardView cardView;

        public MyviewHolder(@NonNull View itemView, final adapter_recyclerCarroOnline.OnItemClickListener listener) {
            super(itemView);

            plateName = itemView.findViewById(R.id.plateName);
            platePrice = itemView.findViewById(R.id.platePrice);
            plateDetails = itemView.findViewById(R.id.plateDetails);
            plateState = itemView.findViewById(R.id.plateState);
            plateQuantity = itemView.findViewById(R.id.plateQuantity);
            plateImage = itemView.findViewById(R.id.plateImage);

            btn_eliminar = itemView.findViewById(R.id.deletePlateButton);
            cardView = itemView.findViewById(R.id.cardViewCarrito);
            //EL ITEMVIEW ES TODO EL OBJETO DE MI LISTA RECYCLER
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            // ES SOLO EL BOTON
            btn_eliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
            //cardView.setOnCreateContextMenuListener(this);
        }
    }
}
