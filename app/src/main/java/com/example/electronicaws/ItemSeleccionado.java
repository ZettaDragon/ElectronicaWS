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
import com.squareup.picasso.Picasso;

public class ItemSeleccionado extends AppCompatActivity implements View.OnClickListener {

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
    private String msg = "Entro a esta parte";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_seleccionado);
        Log.i("mensaje de inicio",msg);
        iniciar();
        setEvents();
    }

    public void iniciar(){
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
                    startActivityForResult(i,0);
                    break;
                case R.id.btnSelectFoto:
                    //openGalery();
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

    private void limpiar()
    {
        savedProductos = null;
        txtMarca.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        Picasso.get().load(R.drawable.com).into(imageView);
    }

    /*private void openGalery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(intent.createChooser(intent,"Seleccionar"),0);
    }*/

    private String convert(){
        if (imgUri == null && savedProductos.getFoto() == null){
            return Uri.parse("android.resource://com.example.electronicaws/" + R.drawable.com).toString();
        }
        if (imgUri == null){
            savedProductos.getFoto();
        }
        return imgUri.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.i("Intent de itemSeleccionado:",intent.getExtras().toString());
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (resultCode == Activity.RESULT_OK){
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
            }
            else{
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

}