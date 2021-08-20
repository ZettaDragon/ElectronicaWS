<?php
    $hostname_localhost = "localhost";
	$database_localhost = "id17437319_db_electronica";
	$username_localhost = "usuariodb";
	$pass_localhost = "v6aQFTBSs6q4w8Ai";
    $json = array();

    $conexion = mysqli_connect ($hostname_localhost,$username_localhost, $pass_localhost,$database_localhost);
    
    $usuario = $_POST["usuario"];
    $pass = $_POST["pass"];
    $passencryp = password_hash($pass,PASSWORD_DEFAULT);
        
    $consulta = $conexion->query("SELECT * FROM users WHERE usuario = '$usuario'");
    $num = $consulta->num_rows;
    
    $resultado = mysqli_fetch_assoc($consulta);
        if($num > 0)
        {
            $pass1 = $resultado['contrasena'];
            if(password_verify($pass,$pass1))
            {
                $var = "Bienvenido!!!!";
                echo "<SCRIPT>
                    alert('$var');
                    location = '/InsertarProducto.html';
                </SCRIPT>";
            }else{
                $var = "Contraseña o Usuario incorrectos!";
                echo "<SCRIPT>
                    alert('$var');
                    location = '/';
                </SCRIPT>";
            }
            
        }else
        {
            $var = "Usuario no existe!!";
                echo "<SCRIPT>
                    alert('$var');
                    location = '/Index.html';
                </SCRIPT>";
        }
?>