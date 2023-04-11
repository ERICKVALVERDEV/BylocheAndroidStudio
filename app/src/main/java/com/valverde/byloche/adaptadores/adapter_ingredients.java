package com.valverde.byloche.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valverde.byloche.CuadroDialogoProProducto;
import com.valverde.byloche.R;
import com.valverde.byloche.fragments.Online.MenuDetalle;

import java.util.List;

public class adapter_ingredients extends RecyclerView.Adapter<adapter_ingredients.MyHolderView>{
    private Context context;
    List<MenuDetalle> ingredientsList;
    private View.OnClickListener listener;

    public adapter_ingredients(List<MenuDetalle> ingredientsList, Context context){
        this.ingredientsList = ingredientsList;
        this.context = context;
    }

    @NonNull
    @Override
    public adapter_ingredients.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_ingredient,parent,false);
        return new adapter_ingredients.MyHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_ingredients.MyHolderView holder, int position) {
        String ingredientName = ingredientsList.get(position).getIngrediente();
        double ingredientPrice = ingredientsList.get(position).getPrecio();
        double ingredientQuantity = ingredientsList.get(position).getCantidad();
        String ingredientMetricUnit = ingredientsList.get(position).getUnidadMedida();
        String ingredientText = context.getString(R.string.ingredient, ingredientName, ingredientQuantity, ingredientMetricUnit, ingredientPrice);
        holder.ingredient.setText(ingredientText);
        holder.ingredient.setChecked(ingredientsList.get(position).isPorDefecto());
        holder.ingredient.setOnCheckedChangeListener((checkbox,isChecked) -> {
            CuadroDialogoProProducto.ingredientsSelected.put(ingredientsList.get(position), isChecked);
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
