package com.example.electronicaws;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.electronicaws.Objetos.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Hashtable;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    EditText txtUsuario, txtPass;
    Button btnLogin;
    private final Context context = this;
    private ProcesosPHP php = new ProcesosPHP();
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private Usuario usuario;
    private String serverip = "https://electronicaws.000webhostapp.com/WSElectronica/";
    private String pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtPass = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        requestQueue = Volley.newRequestQueue(context);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable())
                {
                    boolean completo = true;
                    if(txtUsuario.getText().toString().equals(""))
                    {
                        txtUsuario.setError("Introduce el Usuario");
                        completo = false;
                    }
                    if(txtPass.getText().toString().equals(""))
                    {
                        txtPass.setError("Introduce la contraseña");
                        completo = false;
                    }
                    if(completo)
                    {
                            String url = serverip + "wsConsultarUsuarios.php?usuario=" + txtUsuario.getText().toString() + "&pass=" + txtPass.getText().toString();
                            login(url);
                    }
                }
            }


        });

    }

    private void limpiar() {
        txtUsuario.setText("");
        txtPass.setText("");
        txtUsuario.setError(null);
        txtPass.setError(null);
        txtUsuario.requestFocus();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }


    public void login(String URL_SERVIDOR) {
        Log.i("url",URL_SERVIDOR);
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, URL_SERVIDOR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // En este apartado se programa lo que deseamos hacer en caso de no haber errores

                if(response.equals("ERROR 1")) {
                    Toast.makeText(MainActivity.this, "Se deben de llenar todos los campos.", Toast.LENGTH_SHORT).show();
                    limpiar();
                } else if(response.equals("ERROR 3")) {
                    Toast.makeText(MainActivity.this, "No existe ese registro.", Toast.LENGTH_SHORT).show();
                    limpiar();
                } else if(response.equals("Exito")){
                    Intent intent = new Intent (MainActivity.this, ListaActivity.class);
                    limpiar();
                    startActivity(intent);
                    Log.i("respuesta",response);
                    Toast.makeText(MainActivity.this, "Inicio de Sesion exitoso.", Toast.LENGTH_LONG).show();
                }else
                {
                    Toast.makeText(MainActivity.this, "Contraseña o usuario incorrectos", Toast.LENGTH_SHORT).show();
                    limpiar();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // En caso de tener algun error en la obtencion de los datos
                Toast.makeText(MainActivity.this, "ERROR AL INICIAR SESION", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                // En este metodo se hace el envio de valores de la aplicacion al servidor
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("usuario", txtUsuario.getText().toString().trim());
                parametros.put("contrasena", txtPass.getText().toString().trim());

                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }


}
