package com.valverde.byloche.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valverde.byloche.CarritoActivity;
import com.valverde.byloche.CarritoOnlineActivity;
import com.valverde.byloche.CuadroDialogoProCarritoOnline;
import com.valverde.byloche.DialogUsers;
import com.valverde.byloche.R;
import com.valverde.byloche.fragments.Online.VentasOnline;

import java.util.List;

public class adapter_confirmedOrders extends RecyclerView.Adapter<adapter_confirmedOrders.MyViewHolder>{
    private Context context;
    List<VentasOnline> ordersList;

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
        holder.orderTable.setText(ordersList.get(position).getMesa());
        holder.orderPlate1.setText("");
        holder.orderPlate2.setText("");
        holder.orderState.setText(orderStateText);
        holder.editOrderButton.setImageResource(R.drawable.ic_order_card_edit);
        holder.sendOrderButton.setImageResource(R.drawable.ic_order_card_send);

        holder.editOrderButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CarritoOnlineActivity.class);
                intent.putExtra("currentOrderId", (int) ordersList.get(holder.getAdapterPosition()).getIdVenta());
                intent.putExtra("currentTable", ordersList.get(holder.getAdapterPosition()).getMesa());
                context.startActivity(intent);
            }
        });

        holder.sendOrderButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new DialogUsers(context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
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
