package com.valverde.byloche.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.valverde.byloche.R;
import com.valverde.byloche.fragments.Online.UserOnline;

import java.util.List;

public class adapter_users extends RecyclerView.Adapter<adapter_users.MyHolderView>{
    List<UserOnline> usersList;
    private int selectedPosition;
    private adapter_users.OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int userId);
    }


    public adapter_users(List<UserOnline> usersList, adapter_users.OnItemClickListener itemClickListener){
        this.usersList = usersList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public adapter_users.MyHolderView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new adapter_users.MyHolderView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter_users.MyHolderView holder, int position) {
        holder.radioButton.setText(usersList.get(position).getNombres() + " " + usersList.get(position).getApellidos());
        holder.radioButton.setChecked(position == selectedPosition);
        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    selectedPosition = holder.getAdapterPosition();
                    itemClickListener.onItemClick(usersList.get(selectedPosition).getIdUsuario());
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }


    public class MyHolderView extends RecyclerView.ViewHolder {
        RadioButton radioButton;

        public MyHolderView(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.user);
        }
    }

}
