package com.valverde.byloche;

public class MapsLocation {

    private double latitud;
    private double longitud;

    public MapsLocation(double latitud, double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public MapsLocation() {
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}
