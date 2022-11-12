package com.valverde.byloche.Interfaz;

import com.valverde.byloche.Online.CategoriaOnline;
import com.valverde.byloche.Online.MenuOnline;
import com.valverde.byloche.Online.UsuarioLogin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface iRestApi {
    @GET("/api/Usuario")
    Call<List<UsuarioLogin>> listRepos(@Query("Correo") String Correo, @Query("Clave") String Clave);

    @POST("api/Categoria")
    Call<List<CategoriaOnline>> meCatergoria();

    @POST("api/MenuId")
    Call<List<MenuOnline>> meMenuId(@Query("IdCategoria") int IdCategoria);
}
