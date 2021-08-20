package com.example.electronicaws;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.electronicaws.Objetos.ProcesosPHP;
import com.example.electronicaws.Objetos.Productos;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListaActivity extends ListActivity implements Response.Listener<JSONObject>,Response.ErrorListener{

    private final Context context = this;
    private ProcesosPHP php = new ProcesosPHP();
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private ArrayList<Productos> listaProductos;
    private String serverip = "http://electronicaws.ddns.net/WSElectronica/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        listaProductos = new ArrayList<Productos>();
        requestQueue = Volley.newRequestQueue(context);
        consultarProductos();

    }

    public void consultarProductos(){
        String url = serverip + "wsConsultarProductos.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response){
        Productos productos = null;
        JSONArray json = response.optJSONArray("productos");
        try {
            for(int i=0;i<json.length();i++){
                productos = new Productos();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);
                productos.set_ID(jsonObject.optInt("_ID"));
                productos.setMarca(jsonObject.optString("marca"));
                productos.setDescripcion(jsonObject.optString("descripcion"));
                productos.setFoto(jsonObject.optString("foto"));
                productos.setPrecio(jsonObject.optDouble("precio"));
                listaProductos.add(productos);
            }
            MyArrayAdapter adapter = new MyArrayAdapter(context,R.layout.layout_productos,listaProductos);
            setListAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class MyArrayAdapter extends ArrayAdapter<Productos> {

        Context context;
        int textViewRecursoId;
        ArrayList<Productos> objects;

        public MyArrayAdapter(Context context, int textViewResourceId, ArrayList<Productos> objects){
            super(context, textViewResourceId, objects);
            this.context = context;
            this.textViewRecursoId = textViewResourceId;
            this.objects = objects;

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup){

            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(this.textViewRecursoId, null);

            TextView lblMarca = (TextView) view.findViewById(R.id.lblTitulo);
            TextView lblDescripcion = (TextView) view.findViewById(R.id.lblDescripcion);
            TextView lblPrecio = (TextView) view.findViewById(R.id.lblPrecio);
            ImageView foto = (ImageView) view.findViewById(R.id.imgFoto);
            Button btnVer = (Button) view.findViewById(R.id.btnVer);

            lblMarca.setText(objects.get(position).getMarca());
            lblDescripcion.setText(objects.get(position).getDescripcion());
            String p = String.valueOf(objects.get(position).getPrecio());
            lblPrecio.setText(p);
            lblPrecio.setText("$" + p);
            Picasso.get().load(objects.get(position).getFoto()).into(foto);

            btnVer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle oBundle = new Bundle();
                    oBundle.putSerializable("producto", objects.get(position));
                    Intent intento = new Intent();
                    intento.putExtras(oBundle);
                    setResult(Activity.RESULT_OK,intento);
                    finish();

                }
            });
            return view;

        }

    }

}