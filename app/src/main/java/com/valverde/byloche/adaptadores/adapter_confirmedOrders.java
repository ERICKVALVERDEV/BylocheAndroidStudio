package com.valverde.byloche.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.valverde.byloche.CarritoActivity;
import com.valverde.byloche.CarritoOnlineActivity;
import com.valverde.byloche.CuadroDialogoProCarritoOnline;
import com.valverde.byloche.DialogUsers;
import com.valverde.byloche.MainActivity;
import com.valverde.byloche.R;
import com.valverde.byloche.fragments.Online.Estado;
import com.valverde.byloche.fragments.Online.MesaOnline;
import com.valverde.byloche.fragments.Online.RetrofitCall;
import com.valverde.byloche.fragments.Online.VentasOnline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class adapter_confirmedOrders extends RecyclerView.Adapter<adapter_confirmedOrders.MyViewHolder>{
    private Context context;
    List<VentasOnline> ordersList;
    Map<Integer, String> tables = new HashMap<>();

    public adapter_confirmedOrders(List<VentasOnline> ordersList, Context context){
        this.ordersList = ordersList;
        this.context = context;
    }

    @NonNull
    @Override
    public adapter_confirmedOrders.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_order_card,parent,false);
        return new adapter_confirmedOrders.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_confirmedOrders.MyViewHolder holder, int position) {
        String orderTitleText = context.getString(R.string.orderTitle, ordersList.get(position).getIdVenta());
        String orderStateText = context.getString(R.string.orderState, ordersList.get(position).getEstado());
        holder.orderTitle.setText(orderTitleText);
        holder.orderTable.setText(tables.get(ordersList.get(holder.getAdapterPosition()).getIdMesa()));
        holder.orderPlate1.setText("");
        holder.orderPlate2.setText("");
        holder.orderState.setText(orderStateText);
        holder.editOrderButton.setImageResource(R.drawable.ic_order_card_edit);
        holder.sendOrderButton.setImageResource(R.drawable.ic_order_card_send);

        loadTables(holder);

        holder.sendOrderButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new DialogUsers(context, ordersList.get(holder.getAdapterPosition()).getIdVenta());
            }
        });

        if(ordersList.get(holder.getAdapterPosition()).getEstado().equals(Estado.ENCURSO.toString())){
            holder.editOrderButton.setImageResource(R.drawable.ic_order_card_edit_disabled);
            holder.editOrderButton.setEnabled(false);
            return;
        }

        holder.editOrderButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CarritoOnlineActivity.class);
                intent.putExtra("currentOrderId", (int) ordersList.get(holder.getAdapterPosition()).getIdVenta());
                intent.putExtra("currentTable", tables.get(ordersList.get(holder.getAdapterPosition()).getIdMesa()));
                intent.putExtra("currentTableId", ordersList.get(holder.getAdapterPosition()).getIdMesa());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    private void loadTables(adapter_confirmedOrders.MyViewHolder holder){
        Call<List<MesaOnline>> call = RetrofitCall.getApiService().getTablesByRestaurant(MainActivity.id_restaurante);

        call.enqueue(new Callback<List<MesaOnline>>() {
            @Override
            public void onResponse(Call<List<MesaOnline>> call, retrofit2.Response<List<MesaOnline>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(response.body() == null){
                    return;
                }
                List<MesaOnline> tablesList = response.body();
                for(MesaOnline table:tablesList){
                    tables.put(table.getIdMesa(), table.getNombre());
                }
                holder.orderTable.setText(tables.get(ordersList.get(holder.getAdapterPosition()).getIdMesa()));
            }

            @Override
            public void onFailure(Call<List<MesaOnline>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView orderTitle, orderTable, orderPlate1, orderPlate2, orderState;
        private ImageView editOrderButton, sendOrderButton;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            orderTitle =  itemView.findViewById(R.id.orderTitle);
            orderTable =  itemView.findViewById(R.id.orderTable);
            orderPlate1 =  itemView.findViewById(R.id.orderPlate1);
            orderPlate2 = itemView.findViewById(R.id.orderPlate2);
            orderState = itemView.findViewById(R.id.orderState);
            editOrderButton = itemView.findViewById(R.id.editOrderButton);
            sendOrderButton = itemView.findViewById(R.id.sendOrderButton);
        }
    }
}
