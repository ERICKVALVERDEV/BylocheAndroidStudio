package com.valverde.byloche.Datos;

import com.valverde.byloche.fragments.Online.TipoDocumentoCliente;

public class EnumsHelper {

    //region TipoDocumento Converter

    private static final String Cedula = "Cedula";
    private static final String Pasaporte = "Pasaporte";
    private static final String Ruc = "Ruc";

    public static TipoDocumentoCliente ConvertirTipoDocumentoClienteCodigo(String tipoDocumentoCliente){

        switch (tipoDocumentoCliente)
        {
            case Cedula: return TipoDocumentoCliente.Cedula;
            case Pasaporte: return TipoDocumentoCliente.Pasaporte;
            case Ruc: return TipoDocumentoCliente.Ruc;
        }
        return TipoDocumentoCliente.Cedula;
    }


    public static String ConvertirTipoDocumentoClienteString(String tipoDocumentoCliente)
    {
        switch (tipoDocumentoCliente)
        {
            case Cedula: return TipoDocumentoCliente.Cedula.toString();
            case Pasaporte: return TipoDocumentoCliente.Pasaporte.toString();
            case Ruc: return TipoDocumentoCliente.Ruc.toString();
        }
        return "";

    }



    //endregion


}
