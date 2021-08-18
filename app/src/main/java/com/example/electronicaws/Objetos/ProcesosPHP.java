package com.example.electronicaws.Objetos;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

public class ProcesosPHP implements Response.Listener<JSONObject>, Response.ErrorListener {

    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private ArrayList<Productos> productos = new ArrayList<Productos>();
    private String serverip = "https://electronicaws.000webhostapp.com/WSElectronica/";

    public void setContext(Context context){requestQueue = Volley.newRequestQueue(context); }


    //Procesos PHP para los productos
    public void actualizarProductoWS(Productos p, int id)
    {
        String url = serverip + "wsActualizarProductos.php?_ID=" + id + "&marca=" + p.getMarca() + "&descripcion=" + p.getDescripcion() + "&foto=" + p.getFoto() + "&precio=" + p.getPrecio();
        url = url.replace("","%20");
        Log.i("url",url);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        requestQueue.add(jsonObjectRequest);
    }
    public void borrarProductosWS(int id)
    {
        String url = serverip + "wsEliminarProductos.php=_ID=" + id;
        Log.i("url",url);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url, null, this, this);
        requestQueue.add(jsonObjectRequest);
    }

    public void validarUsuarioWS(Usuario usuario)
    {
        String url = serverip + "wsConsultarUsuarios.php?usuario=" + usuario.getUsuario() + "&pass" + usuario.getContraseña();
        url = url.replace("","%20");
        Log.i("url",url);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,url,null,this,this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.i("Error",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {

    }
}
