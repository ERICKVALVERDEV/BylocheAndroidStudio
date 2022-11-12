package com.valverde.byloche.adaptadores;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.valverde.byloche.Datos.Sqlite_Detalle_Carrito;
import com.valverde.byloche.R;

import java.util.ArrayList;

public class adapter_ListViewCarrito extends BaseAdapter {

    public Activity contetx;
    public ArrayList<Sqlite_Detalle_Carrito> listCarrito;
    private static LayoutInflater inflater = null;

    public adapter_ListViewCarrito(Activity contetx, ArrayList<Sqlite_Detalle_Carrito> listCarrito){
        this.contetx = contetx;
        this.listCarrito = listCarrito;
        inflater = (LayoutInflater) contetx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return listCarrito.size();
    }

    @Override
    public Object getItem(int i) {
        return listCarrito.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setItems(ArrayList<Sqlite_Detalle_Carrito> listCarrito) {
        //VERIFICAR
        //https://stackoverrun.com/es/q/4071878
        this.listCarrito.clear();
        this.listCarrito.addAll(listCarrito);
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        itemView = (itemView == null) ? inflater.inflate(R.layout.adapter_listview_carro, null): itemView;
        TextView txt_datos_list = itemView.findViewById(R.id.txt_datos_list);
        Sqlite_Detalle_Carrito selectlist = listCarrito.get(position);
        txt_datos_list.setText(selectlist.getCantidad_pro()+" - "+selectlist.getNombre_pro()+" - Precio $"
                +selectlist.getPrecio_pro());

        return itemView;
    }


}
