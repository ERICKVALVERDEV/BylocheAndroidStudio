package com.valverde.byloche;

import static com.valverde.byloche.Utils.AlertDialog.DialogAlerta;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.valverde.byloche.fragments.Online.MesaOnline;
import com.valverde.byloche.fragments.Online.ResponseServer;
import com.valverde.byloche.fragments.Online.RetrofitCall;
import com.valverde.byloche.fragments.PedidoFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogUsers extends Dialog {
    RecyclerView usersRecycler;
    adapter_users.OnItemClickListener itemClickListener;
    adapter_users usersAdapter;
    int userIdSelected;
    int idVenta;

    public DialogUsers(@NonNull Context context, int idVenta) {
        super(context);
        this.idVenta = idVenta;
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
                try {
                    Log.i("mydebug", "usuario actulizacion: " + idVenta + " : " + userIdSelected);
                    Call<ResponseServer> call = RetrofitCall.getApiService().actualizarUsuarioVenta(userIdSelected, idVenta);
                    call.enqueue(new Callback<ResponseServer>() {
                        @Override
                        public void onResponse(Call<ResponseServer> call, Response<ResponseServer> response) {
                            DialogAlerta(getContext(),"Estado",response.body().getMensaje());
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.putExtra("fragmentToLoad","pedidos");
                            getContext().startActivity(intent);
                            dialog.dismiss();
                        }
                        @Override
                        public void onFailure(Call<ResponseServer> call, Throwable t) {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.putExtra("fragmentToLoad","pedidos");
                            getContext().startActivity(intent);
                            dialog.dismiss();
                        }
                    });
                }catch (Exception ex){
                    DialogAlerta(getContext(),"Error",ex.getMessage());
                }
            }
        });

        dialog.show();
    }
}
