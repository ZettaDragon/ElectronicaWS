package com.example.electronicaws;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.electronicaws.Objetos.ProcesosPHP;
import com.example.electronicaws.Objetos.Productos;

public class ItemSeleccionado extends AppCompatActivity implements View.OnClickListener {

    private TextView lblMarca;
    private TextView lblDescripcion;
    private TextView lblPrecio;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_seleccionado);
        iniciar();
        setEvents();
    }

    public void iniciar(){
        this.php = new ProcesosPHP();
        php.setContext(this);
        lblMarca = findViewById(R.id.lblTitulo);
        lblDescripcion = findViewById(R.id.lblDescripcion);
        lblPrecio = findViewById(R.id.lblPrecio);
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
        if (isNetworkAvailable()){
            switch (v.getId()){
                case R.id.btnGuardar:

                    Productos po = new Productos();
                    po.setMarca(txtMarca.getText().toString());
                    po.setDescripcion(txtDescripcion.getText().toString());
                    po.setPrecio(Double.parseDouble(txtPrecio.getText().toString()));
                    po.setFoto(convert());

                    if (savedProductos != null){
                        php.actualizarProductoWS(po,id);
                        Toast.makeText(getApplicationContext(), "Actualizado", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.btnBorrar:
                    /*php.borrarProductosWS(sproductos.get_ID());
                    Log.i("ID", String.valueOf(sproductos.get_ID()));
                    Toast.makeText(getApplicationContext(), "Producto eliminado con exito", Toast.LENGTH_SHORT).show();*/
                    break;
                case R.id.btnRegresar:
                    Intent i = new Intent(ItemSeleccionado.this, ListActivity.class);
                    startActivityForResult(i,0);
                    break;
                case R.id.btnSelectFoto:
                    openGalery();
                    break;

            }
        }else{
            Toast.makeText(getApplicationContext(), "Se necesita tener conexion a internet", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    private void openGalery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(intent.createChooser(intent,"Seleccionar"),0);
    }

    private String convert(){
        if (imgUri == null && savedProductos.getFoto() == null){
            return Uri.parse("android.resource://com.example.electronicaws/" + R.drawable.com).toString();
        }
        if (imgUri == null){
            savedProductos.getFoto();
        }
        return imgUri.toString();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (Activity.RESULT_OK == resultCode){
                Productos p = (Productos) bundle.getSerializable("producto");
                savedProductos = p;
                id = p.get_ID();
                txtMarca.setText(p.getMarca());
                txtDescripcion.setText(p.getDescripcion());
                String pre = String.valueOf(p.getPrecio());
                txtPrecio.setText(pre);
                imageView.setImageURI(Uri.parse(p.getFoto()));
            }
            else{
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

}