package com.valverde.byloche.Interfaz;

import com.valverde.byloche.fragments.Online.CategoriaOnline;
import com.valverde.byloche.fragments.Online.ClientesOnline;
import com.valverde.byloche.fragments.Online.ExtraOnline;
import com.valverde.byloche.fragments.Online.MenuOnline;
import com.valverde.byloche.fragments.Online.MesaOnline;
import com.valverde.byloche.fragments.Online.ResponseServer;
import com.valverde.byloche.fragments.Online.UserOnline;
import com.valverde.byloche.fragments.Online.UsuarioLogin;
import com.valverde.byloche.fragments.Online.VentasDetalleOnline;
import com.valverde.byloche.fragments.Online.VentasOnline;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface iRestApi {
    @GET("/WebApiByloche/api/Usuario")
    Call<List<UsuarioLogin>> listRepos(@Query("Correo") String Correo, @Query("Clave") String Clave);

    @GET("/WebApiByloche/api/Usuario")
    Call<List<UserOnline>> getUsersByIdRestaurante(@Query("idRestaurante") int idRestaurante);

    @POST("/WebApiByloche/api/Categoria")
    Call<List<CategoriaOnline>> meCatergoria();

    @POST("/WebApiByloche/Menu/ObtenerPorCategoria")
    Call<List<MenuOnline>> meMenuId(@Query("IdCategoria") int IdCategoria);

    @POST("/WebApiByloche/Extras/ObetenerExtrasPorIdMenu")
    Call<List<ExtraOnline>> getExtrasByIdMenu(@Query("idMenu") int idMenu);

    @POST("/WebApiByloche/api/Clientes")
    Call<List<ClientesOnline>> meConsultarClientes();

    @POST("/WebApiByloche/api/Clientes/ObtenerPorCedula")
    Call<ClientesOnline> obtenerClientePorCedula(@Query("cedula") String cedula);

    @POST("/WebApiByloche/Ventas/RegistrarVenta")
    Call<ResponseServer> guardarPedido(@Query("xml") StringBuilder ventas, @Query("nombreUsuario") String nombreUsuario );

    @POST("/WebApiByloche/api/Ventas")
    Call<VentasOnline> getVentasById(@Query("idVenta") String idVenta);

    @POST("/WebApiByloche/Ventas/ObtenerVentasRestaurante")
    Call<List<VentasOnline>> getVentasByRestaurant(@Query("idRestaurante") int idRestaurant);

    @POST("/WebApiByloche/api/VentasDetalles")
    Call<List<VentasDetalleOnline>> getVentaDetallesByIdVenta(@Query("idVenta") int idVenta);

    @POST("/WebApiByloche/Mesa/ObtenerMesasRestaurante")
    Call<List<MesaOnline>> getTablesByRestaurant(@Query("idRestaurante") int idRestaurant);

    @POST("/WebApiByloche/Ventas/ModificarVenta")
    Call<ResponseServer> actualizarPedido(@Body VentasOnline ventaActualizada, @Query("nombreUsuario") String nombreUsuario);

}
