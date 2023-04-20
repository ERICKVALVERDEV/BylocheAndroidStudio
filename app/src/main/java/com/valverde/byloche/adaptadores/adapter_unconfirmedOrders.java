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
import com.valverde.byloche.R;
import com.valverde.byloche.SQLite.order.Order;

import java.util.List;

public class adapter_unconfirmedOrders extends RecyclerView.Adapter<adapter_unconfirmedOrders.MyViewHolder> {
    private Context context;
    List<Order> ordersList;

    public adapter_unconfirmedOrders(List<Order> ordersList, Context context){
        this.ordersList = ordersList;
        this.context = context;
    }


    @NonNull
    @Override
    public adapter_unconfirmedOrders.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_order_card,parent,false);
        return new adapter_unconfirmedOrders.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_unconfirmedOrders.MyViewHolder holder, int position) {
        String orderTitleText = context.getString(R.string.orderTitle, ordersList.get(position).getId());
        String orderStateText = context.getString(R.string.orderState, ordersList.get(position).getState());
        holder.orderTitle.setText(orderTitleText);
        holder.orderTable.setText(ordersList.get(position).getTable());
        holder.orderPlate1.setText("");
        holder.orderPlate2.setText("");
        holder.orderState.setText(orderStateText);
        holder.editOrderButton.setImageResource(R.drawable.ic_order_card_edit);
        holder.sendOrderButton.setImageResource(R.drawable.ic_order_card_send);
        holder.sendOrderButton.setVisibility(View.INVISIBLE);

        holder.editOrderButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CarritoActivity.class);
                intent.putExtra("currentOrderId", (int) ordersList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("currentTable", ordersList.get(holder.getAdapterPosition()).getTable());
                intent.putExtra("currentTableId", ordersList.get(holder.getAdapterPosition()).getTableId());
                context.startActivity(intent);
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
            orderTitle = itemView.findViewById(R.id.orderTitle);
            orderTable = itemView.findViewById(R.id.orderTable);
            orderPlate1 = itemView.findViewById(R.id.orderPlate1);
            orderPlate2 = itemView.findViewById(R.id.orderPlate2);
            orderState = itemView.findViewById(R.id.orderState);
            editOrderButton = itemView.findViewById(R.id.editOrderButton);
            sendOrderButton = itemView.findViewById(R.id.sendOrderButton);
        }


    }
}
