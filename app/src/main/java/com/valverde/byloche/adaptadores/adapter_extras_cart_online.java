package com.valverde.byloche.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.valverde.byloche.CuadroDialogoProCarritoOnline;
import com.valverde.byloche.R;
import com.valverde.byloche.fragments.Online.ExtraOnline;

import java.util.List;

public class adapter_extras_cart_online extends RecyclerView.Adapter<adapter_extras_cart_online.MyHolderView>{
    private Context context;
    List<ExtraOnline> extrasList;
    private View.OnClickListener listener;

    public adapter_extras_cart_online(List<ExtraOnline> extrasList, Context context){
        this.extrasList = extrasList;
        this.context = context;
    }

    @NonNull
    @Override
    public adapter_extras_cart_online.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.item_ingredient,parent,false);
        return new adapter_extras_cart_online.MyHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_extras_cart_online.MyHolderView holder, int position) {
        String extraName = extrasList.get(position).getDescripcion();
        holder.extra.setText(extraName);
        holder.extra.setChecked(extrasList.get(position).isSelected());
        holder.extra.setOnCheckedChangeListener((checkbox,isChecked) -> {
            CuadroDialogoProCarritoOnline.extrasSelected.put(extrasList.get(position), isChecked);
            Log.i("mydebug", "extrasSelected in adapter: " + CuadroDialogoProCarritoOnline.extrasSelected.toString());
        });
    }

    @Override
    public int getItemCount() {
        return extrasList.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public class MyHolderView extends RecyclerView.ViewHolder {
        private CheckBox extra;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);
            extra = itemView.findViewById(R.id.ingredient);
        }
    }
}
