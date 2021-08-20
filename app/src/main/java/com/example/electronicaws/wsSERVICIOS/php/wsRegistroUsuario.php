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
    
    if($num > 0)
    {
        $var = "El usuario ya existe, intente con otro diferente!";
                echo "<SCRIPT>
                    alert('$var');
                    location = '/AgregarUsuario.html';
                </SCRIPT>";
    }else
    {
        $insert = ("INSERT INTO users(_ID,usuario,contrasena)
        VALUES(NULL,'{$usuario}','{$passencryp}')");
        $resultado_insert = mysqli_query($conexion,$insert);
        if(!$resultado_insert)
        {
        echo "Error";
        }
        else
        {
            $var = "Usuario Registrado con exito";
            echo "<SCRIPT>
                alert('$var');
                location = '/Login.html';
            </SCRIPT>";
            
        }
    }
?>