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
import com.valverde.byloche.CuadroDialogoProProducto;
import com.valverde.byloche.R;
import com.valverde.byloche.SQLite.ConexionSQLiteHelper;
import com.valverde.byloche.SQLite.ingredient.Ingredient;
import com.valverde.byloche.fragments.Online.MenuDetalle;

import java.util.List;

public class adapter_ingredients_cart extends RecyclerView.Adapter<adapter_ingredients_cart.MyHolderView>{
    private Context context;
    List<Ingredient> ingredientsList;
    private View.OnClickListener listener;

    public adapter_ingredients_cart(List<Ingredient> ingredientsList, Context context){
        this.ingredientsList = ingredientsList;
        this.context = context;
    }

    @NonNull
    @Override
    public adapter_ingredients_cart.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_ingredient,parent,false);
        return new adapter_ingredients_cart.MyHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_ingredients_cart.MyHolderView holder, int position) {
        String ingredientName = ingredientsList.get(position).getName();
        double ingredientPrice = ingredientsList.get(position).getPrice();
        double ingredientQuantity = ingredientsList.get(position).getQuantity();
        String ingredientMetricUnit = ingredientsList.get(position).getMetricUnit();
        String ingredientText = context.getString(R.string.ingredient, ingredientName, ingredientQuantity, ingredientMetricUnit, ingredientPrice);
        holder.ingredient.setText(ingredientText);
        holder.ingredient.setChecked(ingredientsList.get(position).isSelected());
        holder.ingredient.setOnCheckedChangeListener((checkbox,isChecked) -> {
            CuadroDialogoProCarrito.ingredientsSelected.put(ingredientsList.get(position), isChecked);
            Log.i("mydebug", "ingredientsSelected in adapter: " + CuadroDialogoProCarrito.ingredientsSelected.toString());
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
