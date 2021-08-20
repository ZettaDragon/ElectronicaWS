<?php
    $hostname_localhost = "localhost";
	$database_localhost = "id17437319_db_electronica";
	$username_localhost = "usuariodb";
	$pass_localhost = "v6aQFTBSs6q4w8Ai";
    
    $json = array();
    
    $conexion = mysqli_connect ($hostname_localhost,$username_localhost, $pass_localhost,$database_localhost);
    
    $marca = $_POST['marca'];
	$descripcion = $_POST['descripcion'];
	$precio = $_POST['precio'];
	$foto = $_FILES['foto']['tmp_name'];
	$nom_img = $_FILES['foto']['name'];

    $caperta_destino = $_SERVER['DOCUMENT_ROOT'].'/img/';
	move_uploaded_file($foto,$caperta_destino.$nom_img);

    $carpeta_final = "http://192.168.0.1/img/" . $nom_img;
			
	        $insert = ("INSERT INTO productos(_ID,marca,descripcion,foto,precio)VALUES(NULL,'{$marca}','{$descripcion}','{$carpeta_final}','{$precio}')");
		    $resultado_insert = mysqli_query($conexion,$insert);
		    
			if($resultado_insert)
			{
			    //esto se necesita
			    $var = "Producto Agregado!!";
                echo "<SCRIPT>
                    alert('$var');
                    location = '/InsertarProducto.html';
                </SCRIPT>";
			}
			else{
				$resulta["_ID"] =0;
				$resulta["marca"] = 'No resultado';
				$resulta["descripcion"] = 'No resultado';
				$resulta["foto"] = 'No resultado';
				$resulta["precio"] = 'No resultado';
				$json['productos'][] = $resulta;
				echo json_encode($json);
			}
?>