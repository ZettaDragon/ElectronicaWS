package com.example.electronicaws;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.electronicaws.Objetos.ProcesosPHP;
import com.example.electronicaws.Objetos.Productos;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class ItemSeleccionado extends AppCompatActivity implements View.OnClickListener {

    private static final Object URL = "";
    private EditText txtMarca;
    private EditText txtDescripcion;
    private EditText txtPrecio;
    private ImageView imageView;
    private Button btnImagen;
    private Button btnBorrar;
    private Button btnGuardar;
    private Button btnListar;
    private Productos savedProductos;
    ProcesosPHP php;
    private int id;
    private Uri imgUri;
    private String serverip = "http://192.168.0.1/WSElectronica/";
    private Bitmap bitmap;
    StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_item_seleccionado);
        iniciar();
        setEvents();
    }

    public void iniciar() {
        this.php = new ProcesosPHP();
        php.setContext(this);
        txtMarca = findViewById(R.id.txtMarca);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtPrecio = findViewById(R.id.txtPrecio);
        imageView = findViewById(R.id.imgFoto);
        btnImagen = findViewById(R.id.btnSelectFoto);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnListar = findViewById(R.id.btnRegresar);
        savedProductos = null;
    }

    public void setEvents() {
        btnImagen.setOnClickListener(this);
        btnBorrar.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
        btnListar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (isNetworkAvailable()) {
            switch (v.getId()) {
                case R.id.btnGuardar:
                    if (savedProductos != null) {
                        cargarWebService();
                        /*try {
                            prueba();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    } else {
                        Toast.makeText(getApplicationContext(), "Debe seleccionar un registro primero", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnBorrar:
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(ItemSeleccionado.this);
                    builder1.setMessage("Desea borrar el registro?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Si",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int identificador) {
                                    php.borrarProductosWS(id);
                                    Toast.makeText(getApplicationContext(), "Producto Eliminado", Toast.LENGTH_SHORT).show();
                                    limpiar();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    break;
                case R.id.btnRegresar:
                    Intent i = new Intent(ItemSeleccionado.this, ListaActivity.class);
                    startActivityForResult(i, 0);
                    break;
                case R.id.btnSelectFoto:
                    cargarimg();
                    break;

            }
        } else {
            Toast.makeText(getApplicationContext(), "Se necesita tener conexion a internet", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 2) {
            Uri filePath = intent.getData();
            //Cómo obtener el mapa de bits de la Galería
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Configuración del mapa de bits en ImageView
            imageView.setImageBitmap(bitmap);
        } else {
            if (intent != null) {
                Bundle bundle = intent.getExtras();
                if (resultCode == Activity.RESULT_OK) {
                    Productos p = (Productos) bundle.getSerializable("producto");
                    savedProductos = p;
                    id = p.get_ID();
                    Log.i("idproducto", String.valueOf(p.get_ID()));
                    txtMarca.setText(p.getMarca());
                    txtDescripcion.setText(p.getDescripcion());
                    String pre = String.valueOf(p.getPrecio());
                    txtPrecio.setText(pre);
                    //imageView.setImageURI(Uri.parse(p.getFoto()));
                    Picasso.get().load(p.getFoto()).into(imageView);

                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void limpiar() {
        savedProductos = null;
        txtMarca.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        Picasso.get().load(R.drawable.com).into(imageView);
    }

    private void cargarimg() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Imagen"), 2);
        // startActivityForResult(Intent.createChooser(intent, "Select Imagen"), 1);
    }

    private String imgsrc() {
        if (imgUri == null && savedProductos.getFoto() == null) {
            return Uri.parse("android.resource://com.example.electronicaws" + R.drawable.com).toString();
        }
        if (imgUri == null) {
            return savedProductos.getFoto();
        }
        return imgUri.toString();
    }

    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void cargarWebService()
    {

        String url = "http://192.168.0.1/WSElectronica/wsActualizarProductos.php";
        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("Producto Registrado!!!"))
                {
                    Log.i("Tipo:",response);
                    Toast.makeText(ItemSeleccionado.this, response, Toast.LENGTH_LONG).show();
                    limpiar();
                }else{
                    Log.i("Tipo2:",response);
                    Toast.makeText(ItemSeleccionado.this, response, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ItemSeleccionado.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String img = getStringImagen(bitmap);
                Map<String,String> parametros = new HashMap<>();
                parametros.put("_ID", String.valueOf(savedProductos.get_ID()));
                parametros.put("marca",txtMarca.getText().toString());
                parametros.put("descripcion",txtDescripcion.getText().toString());
                parametros.put("precio",txtPrecio.getText().toString());
                parametros.put("foto",img);
                return  parametros;
            }
        };
        RequestQueue newRequestQueue = Volley.newRequestQueue(ItemSeleccionado.this);
        newRequestQueue.add(request);
    }

}
