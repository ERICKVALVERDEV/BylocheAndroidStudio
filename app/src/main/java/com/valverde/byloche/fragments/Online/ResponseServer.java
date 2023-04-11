package com.valverde.byloche.fragments.Online;

import com.google.gson.annotations.SerializedName;

public class ResponseServer {

    @SerializedName("Respuesta")
    private boolean Respuesta;
    @SerializedName("Mensaje")
    private String Mensaje;


    public boolean isRespuesta() {
        return Respuesta;
    }

    public void setRespuesta(boolean respuesta) {
        Respuesta = respuesta;
    }

    public String getMensaje() {
        return Mensaje;
    }

    public void setMensaje(String mensaje) {
        Mensaje = mensaje;
    }
}
