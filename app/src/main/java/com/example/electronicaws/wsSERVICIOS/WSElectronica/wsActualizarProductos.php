<?php
if($_SERVER['REQUEST_METHOD']=='POST')
{
    $hostname_localhost = "localhost";
	$database_localhost = "id17437319_db_electronica";
	$username_localhost = "usuariodb";
	$pass_localhost = "v6aQFTBSs6q4w8Ai";
    
    $conexion = mysqli_connect ($hostname_localhost,$username_localhost, $pass_localhost,$database_localhost);
            
    $_ID = $_POST['_ID'];
    $marca = $_POST['marca'];
	$descripcion = $_POST['descripcion'];
	$precio = $_POST['precio'];
	$foto = $_POST['foto'];


    $data2 = base64_decode($foto);
    $im = imagecreatefromstring($data2);
    $path = $_ID;
    $content = " ";
    //ubicacion del archivo con su extensión y nombre
    $caperta_destino = $_SERVER['DOCUMENT_ROOT']."/img/".$path.'.jpeg';

    //Crea el archivo en la carpeta destino
    file_put_contents($caperta_destino,$content);
    //autoriza los permisos para que se pueda escribir el archivo
    chmod($caperta_destino, 0777);
    $file = fopen($caperta_destino, "wb");

    //$data = explode(',', $caperta_destino);

    fwrite($file, base64_decode($foto));
    fclose($file);

    $caperta_final = "http://192.168.0.1/img/" . $path . '.jpeg';

    $update = ("UPDATE productos SET marca='{$marca}',descripcion= '{$descripcion}' ,foto='{$caperta_final}', precio='{$precio}' WHERE _ID = '{$_ID}'");

    $resultado_update = mysqli_query($conexion, $update);
    if(!$resultado_update)
    {
        echo "Error al registrar";
    }else{
        echo "Producto Registrado!!!";
    }
}else
{
    echo "Error";
}
?>