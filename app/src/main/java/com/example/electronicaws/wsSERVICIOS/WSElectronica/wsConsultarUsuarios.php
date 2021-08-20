<?php
    $hostname_localhost = "localhost";
	$database_localhost = "id17437319_db_electronica";
	$username_localhost = "usuariodb";
	$pass_localhost = "v6aQFTBSs6q4w8Ai";

    $conexion = mysqli_connect($hostname_localhost,$username_localhost,$pass_localhost,$database_localhost);

    $usuario = $_GET['usuario'];
    $pass = $_GET['pass'];


        $consulta = $conexion->query("SELECT * FROM users WHERE usuario ='$usuario' ");
        
        $num = $consulta->num_rows;
        
        $resultado = mysqli_fetch_assoc($consulta);
        
        if($num > 0) {
                //Se asigna el campo contraseña a pass1
                $pass1 = $resultado['contrasena'];
                if(password_verify($pass,$pass1))
                {
                    echo "Exito";
                }else
                {
                    //contraseña incorrecta
                    echo "Error 2";
                }
                
        } else {
            // echo "No existe ese registro";
            echo "ERROR 3";
        }

?>