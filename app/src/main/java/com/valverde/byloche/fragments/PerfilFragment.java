package com.valverde.byloche.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.valverde.byloche.LoginActivity;
import com.valverde.byloche.MainActivity;
import com.valverde.byloche.R;
import com.valverde.byloche.fragments.perfil.Categoria_Perfil_Activity;
import com.valverde.byloche.fragments.perfil.Cuenta_Perfil_Activity;
import com.valverde.byloche.Direcciones_PerfilActivity;
import com.valverde.byloche.fragments.perfil.Privilegio_Perfil_Activity;
import com.valverde.byloche.fragments.perfil.Producto_Perfil_Activity;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    Intent intent = new Intent();

    private String nlista [] = {"Mi cuenta","Direcciones","Mis Pedidos","Configuracion","Notificaciones",
            "Términos legales","Ayuda"};

    ArrayList<String> nlits_usuario = new ArrayList<>();
    ArrayList<String> nlits_operador = new ArrayList<>();
    ArrayList<String> nlits_administrador = new ArrayList<>();
    ArrayList<String> nlits = new ArrayList<>();

    private SharedPreferences prefs;
    private Button btn1, btn_cerrarsesion;
    private ListView listView;


    public PerfilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        prefs = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        btn_cerrarsesion = (Button)view.findViewById(R.id.but7);
        listView = (ListView)view.findViewById(R.id.lst_elements);

        if(MainActivity.tipo_usuario == 1){
            listaPerfil_Usuario();
        }else  if(MainActivity.tipo_usuario == 2){
            listaPerfil_Operador();
        }else if(MainActivity.tipo_usuario == 3){
            listaPerfil_Administrador();
        }


        btn_cerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removerPreferencias();
                cerrarSesión();
            }
        });

        agregarListView();
        selectListView();



        return  view;
    }

    public void cerrarSesión(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    public void removerPreferencias(){
        //todo en una linea simplificado
        prefs.edit().clear().apply();
    }

    /*FUNCIÓN DEL LISTVIEW*/

    private void agregarListView(){
        if(MainActivity.tipo_usuario == 1){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nlits_usuario);
            listView.setAdapter(adapter);
        }else  if(MainActivity.tipo_usuario == 2){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nlits_operador);
            listView.setAdapter(adapter);
        }else if(MainActivity.tipo_usuario == 3){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nlits_administrador);
            listView.setAdapter(adapter);
        }
    }
    private void listaPerfil_Usuario(){
        nlits_usuario.add("Mi cuenta");
        nlits_usuario.add("Direcciones");
        nlits_usuario.add("Mis Pedidos");
        //nlits_usuario.add("Facturación");
        nlits_usuario.add("Configuracion");
        nlits_usuario.add("Notificaciones");
        nlits_usuario.add("Ayuda");
    }

    private void listaPerfil_Operador(){
        nlits_operador.add("Mi cuenta");
        nlits_operador.add("Direcciones");
        nlits_operador.add("Mis Pedidos");
        nlits_operador.add("Pedidos");
        nlits_operador.add("Configuracion");
        nlits_operador.add("Notificaciones");
        nlits_operador.add("Ayuda");
    }

    private void listaPerfil_Administrador(){
        nlits_administrador.add("Mi cuenta");
        nlits_administrador.add("Direcciones");
        nlits_administrador.add("Mis Pedidos");
        //nlits_administrador.add("Facturación");
        nlits_administrador.add("Configuracion");
        nlits_administrador.add("Notificaciones");
        nlits_administrador.add("Modificar Prilegios");
        nlits_administrador.add("Agregar una Categoría");
        nlits_administrador.add("Agregar un Producto");
        nlits_administrador.add("Ayuda");
    }

    private  void selectListView(){
        if(MainActivity.tipo_usuario == 1){
            nlits = nlits_usuario;
        }else  if(MainActivity.tipo_usuario == 2){
            nlits = nlits_operador;
        }else if(MainActivity.tipo_usuario == 3){
            nlits = nlits_administrador;
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (nlits.get(i)){
                    case "Mi cuenta":
                        intent = new Intent(getContext(),Cuenta_Perfil_Activity.class);
                        startActivity(intent);
                        break;
                    case "Direcciones":
                        intent = new Intent(getContext(), Direcciones_PerfilActivity.class);
                        startActivity(intent);
                        break;
                    case "Mis Pedidos":
                        Toast.makeText(getContext(), "ACTIVITY MIS PEDIDO", Toast.LENGTH_SHORT).show();
                        break;
                    case "Pedidos":
                        Toast.makeText(getContext(), "ACTIVITY PEDIDO", Toast.LENGTH_SHORT).show();
                        break;
                    case "Configuracion":
                        Toast.makeText(getContext(), "ACTIVITY CONFIGURACION", Toast.LENGTH_SHORT).show();
                        break;
                    case "Notificaciones":
                        Toast.makeText(getContext(), "ACTIVITY NOTIFICACION", Toast.LENGTH_SHORT).show();
                        break;
                    case "Términos legales":
                        Toast.makeText(getContext(), "ACTIVITY TERMINOS LEGALES", Toast.LENGTH_SHORT).show();
                        break;
                    case "Modificar Prilegios":
                        intent = new Intent(getContext(), Privilegio_Perfil_Activity.class);
                        startActivity(intent);
                        break;
                    case "Agregar una Categoría":
                        intent = new Intent(getContext(), Categoria_Perfil_Activity.class);
                        startActivity(intent);
                        break;
                    case "Agregar un Producto":
                        intent = new Intent(getContext(), Producto_Perfil_Activity.class);
                        startActivity(intent);
                        break;
                }


            }
        });

    }





}
