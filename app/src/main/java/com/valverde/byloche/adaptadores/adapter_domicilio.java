package com.valverde.byloche.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.valverde.byloche.Datos.Direccion_list;
import com.valverde.byloche.R;

import java.util.ArrayList;
import java.util.List;

public class adapter_domicilio extends RecyclerView.Adapter<adapter_domicilio.MyviewHolder> {
    public List<Direccion_list> direccion_list;
    public Context mcontext;

    public adapter_domicilio(List<Direccion_list> direccion_list){
        this.direccion_list = direccion_list;
    }


    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.direccion_list,parent,false);


        return new MyviewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        holder.txt_domicilio.setText(direccion_list.get(position).getNombre()+"\n"+direccion_list.get(position).getCalle_prin()+" "+direccion_list.get(position).getCalle_secun()
                        +"\n"+direccion_list.get(position).getReferencia()+"\n"+direccion_list.get(position).getTipo_vi()
                        +" "+direccion_list.get(position).getAdicional()+" ");
    }

    @Override
    public int getItemCount() {
        return direccion_list.size();
    }




    public  static class MyviewHolder extends RecyclerView.ViewHolder  implements  View.OnCreateContextMenuListener{

        TextView txt_domicilio;
        CardView cardView ;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            txt_domicilio = itemView.findViewById(R.id.txt_nombres_direccion);
            cardView = itemView.findViewById(R.id.cardViewdireccion);
            cardView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Escoja una Opcion");
            contextMenu.add(this.getAdapterPosition(),1,0,"Seleccionar");
            contextMenu.add(this.getAdapterPosition(),2,1,"Eliminar");
        }



    }

}
