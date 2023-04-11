package com.valverde.byloche;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.valverde.byloche.adaptadores.adapter_users;
import com.valverde.byloche.fragments.PedidoFragment;

public class DialogUsers extends Dialog {
    RecyclerView usersRecycler;
    adapter_users.OnItemClickListener itemClickListener;
    adapter_users usersAdapter;
    int userIdSelected;

    public DialogUsers(@NonNull Context context) {
        super(context);

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_users);

        ImageView closeButton = dialog.findViewById(R.id.btn_cerrar);
        Button sendButton = dialog.findViewById(R.id.btn_aceptar);

        usersRecycler = dialog.findViewById(R.id.usersRecycler);
        itemClickListener = new adapter_users.OnItemClickListener() {
            @Override
            public void onItemClick(int userId) {
                usersRecycler.post(new Runnable() {
                    @Override
                    public void run() {
                        usersAdapter.notifyDataSetChanged();
                    }
                });
                userIdSelected = userId;
            }
        };
        usersRecycler.setLayoutManager(new LinearLayoutManager(context));
        usersAdapter = new adapter_users(PedidoFragment.setUsers, itemClickListener);
        usersRecycler.setAdapter(usersAdapter);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Update userId of Order with Retrofit call
                Toast.makeText(getContext(), "userId: " + userIdSelected, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
