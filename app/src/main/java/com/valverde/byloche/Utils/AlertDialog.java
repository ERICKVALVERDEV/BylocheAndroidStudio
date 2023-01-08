package com.valverde.byloche.Utils;

import android.content.Context;
import android.content.DialogInterface;

public class AlertDialog {


    public static void DialogAlerta(Context context, String Title, String Messagge){
        android.app.AlertDialog.Builder builder;

        builder = new android.app.AlertDialog.Builder(context);

        builder.setMessage(Messagge)
                .setCancelable(true)
                .setTitle(Title)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }
}
