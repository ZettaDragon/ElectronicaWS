<?php
    $hostname_localhost = "localhost";
	$database_localhost = "id17437319_db_electronica";
	$username_localhost = "usuariodb";
	$pass_localhost = "v6aQFTBSs6q4w8Ai";
    
    $json = array();

    $conexion = mysqli_connect ($hostname_localhost,$username_localhost, $pass_localhost,$database_localhost);

    $consulta = "SELECT * FROM productos";
    $reusltado_consulta = mysqli_query($conexion,$consulta);

    while($registro = mysqli_fetch_array($reusltado_consulta))
    {
        $json['productos'][] = $registro;
    }

    mysqli_close($conexion);
    echo json_encode($json);
?>