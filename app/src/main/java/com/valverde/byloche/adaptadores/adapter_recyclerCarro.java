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
import com.valverde.byloche.SQLite.ConexionSQLiteHelper;
import com.valverde.byloche.SQLite.cart.Cart;

import java.util.List;

public class adapter_recyclerCarro extends RecyclerView.Adapter<adapter_recyclerCarro.MyviewHolder> {
    public List<Cart> listCarrito;
    public Context mcontext;
    public ConexionSQLiteHelper con;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public adapter_recyclerCarro(List<Cart> listCarrito, Context mcontext) {
        this.listCarrito = listCarrito;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.adapter_listview_carro, parent, false);
        con = new ConexionSQLiteHelper(mcontext, "bd_registar_pro", null, 1);
        return new MyviewHolder(view, mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, final int position) {
        holder.plateName.setText(listCarrito.get(position).getProductName());
        holder.platePrice.setText("$" + String.valueOf(listCarrito.get(position).getProductPrice()));
        holder.plateQuantity.setText("Cantidad: " + String.valueOf(listCarrito.get(position).getProductQuantity()));
        holder.plateState.setText(listCarrito.get(position).getProductState());
        holder.plateDetails.setText(listCarrito.get(position).getProductDetails());
        if (listCarrito.get(position).getProductImagePath() != null){
            loadImageWebService(listCarrito.get(position).getProductImagePath(), holder);
        }else{
            holder.plateImage.setImageResource(R.drawable.imagennodisponible);
            Toast.makeText(mcontext, "incorrecto", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return listCarrito.size();
    }

    private void loadImageWebService(String rutaImagen, final adapter_recyclerCarro.MyviewHolder holder) {
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

        public MyviewHolder(@NonNull View itemView, final OnItemClickListener listener) {
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
