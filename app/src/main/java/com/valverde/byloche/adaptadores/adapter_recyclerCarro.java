
package com.valverde.byloche.adaptadores;

import android.annotation.SuppressLint;
        import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.ContextMenu;
        import android.view.LayoutInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;
        import androidx.annotation.NonNull;
        import androidx.cardview.widget.CardView;
        import androidx.recyclerview.widget.RecyclerView;

        import com.valverde.byloche.Datos.Direccion_list;
import com.valverde.byloche.Datos.Sqlite_Detalle_Carrito;
import com.valverde.byloche.R;
import com.valverde.byloche.SQLite.ConexionSQLiteHelper;
import com.valverde.byloche.SQLite.Utilidades;

import java.util.ArrayList;
        import java.util.List;

public class adapter_recyclerCarro extends RecyclerView.Adapter<adapter_recyclerCarro.MyviewHolder>{
    public List<Sqlite_Detalle_Carrito> listCarrito;
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

    public class MyviewHolder extends RecyclerView.ViewHolder {

        TextView txt_datos_list;
        Button btn_eliminar;
        CardView cardView ;

        public MyviewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            txt_datos_list = itemView.findViewById(R.id.txt_datos_list);
            btn_eliminar = itemView.findViewById(R.id.btn_eliminarPro);
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


    public adapter_recyclerCarro(List<Sqlite_Detalle_Carrito> listCarrito, Context mcontext){
        this.listCarrito = listCarrito;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.adapter_listview_carro,parent,false);
        con = new ConexionSQLiteHelper(mcontext, "bd_registar_pro",null,1);
       // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listview_carro,parent,false);
        //view.setOnClickListener(this);
        return new MyviewHolder(view,mListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, final int position) {

        holder.txt_datos_list.setText(listCarrito.get(position).getCantidad_pro()+" - "+listCarrito.get(position).getNombre_pro()+" - Precio $"
                +listCarrito.get(position).getPrecio_pro());
    }


    @Override
    public int getItemCount() {
        return listCarrito.size();
    }





}
