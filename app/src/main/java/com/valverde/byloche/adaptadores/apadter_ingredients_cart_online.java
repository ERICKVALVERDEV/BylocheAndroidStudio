package com.valverde.byloche.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valverde.byloche.CuadroDialogoProCarrito;
import com.valverde.byloche.CuadroDialogoProCarritoOnline;
import com.valverde.byloche.R;
import com.valverde.byloche.SQLite.ingredient.Ingredient;
import com.valverde.byloche.fragments.Online.VentaDetalleProductoOnline;

import java.util.List;

public class apadter_ingredients_cart_online extends RecyclerView.Adapter<apadter_ingredients_cart_online.MyHolderView>{
    private Context context;
    List<VentaDetalleProductoOnline> ingredientsList;
    private View.OnClickListener listener;

    public apadter_ingredients_cart_online(List<VentaDetalleProductoOnline> ingredientsList, Context context){
        this.ingredientsList = ingredientsList;
        this.context = context;
    }

    @NonNull
    @Override
    public apadter_ingredients_cart_online.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_ingredient,parent,false);
        return new apadter_ingredients_cart_online.MyHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull apadter_ingredients_cart_online.MyHolderView holder, int position) {
        String ingredientName = ingredientsList.get(position).getNombreProducto();
        double ingredientPrice = ingredientsList.get(position).getPrice();
        double ingredientQuantity = ingredientsList.get(position).getCantidad();
        String ingredientMetricUnit = ingredientsList.get(position).getUnidadMedida();
        String ingredientText = context.getString(R.string.ingredient, ingredientName, ingredientQuantity, ingredientMetricUnit, ingredientPrice);
        holder.ingredient.setText(ingredientText);
        holder.ingredient.setChecked(ingredientsList.get(position).isSelected());
        holder.ingredient.setOnCheckedChangeListener((checkbox,isChecked) -> {
            CuadroDialogoProCarritoOnline.ingredientsSelected.put(ingredientsList.get(position), isChecked);
            Log.i("mydebug", "ingredientsSelected in adapter: " + CuadroDialogoProCarritoOnline.ingredientsSelected.toString());
        });
    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public class MyHolderView extends RecyclerView.ViewHolder {
        private CheckBox ingredient;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);
            ingredient = itemView.findViewById(R.id.ingredient);
        }
    }
}
