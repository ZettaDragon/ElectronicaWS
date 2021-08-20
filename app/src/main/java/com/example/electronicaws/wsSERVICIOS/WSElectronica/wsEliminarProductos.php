<?php
    $hostname_localhost = "localhost";
	$database_localhost = "id17437319_db_electronica";
	$username_localhost = "usuariodb";
	$pass_localhost = "v6aQFTBSs6q4w8Ai";
    
    $json = array();

    if(isset($_GET["_ID"])){
        $_ID = $_GET['_ID'];
        $conexion = mysqli_connect ($hostname_localhost,$username_localhost, $pass_localhost,$database_localhost);

        $delete = ("DELETE FROM productos WHERE _ID = '{$_ID}'");

        $resultado_delete = mysqli_query($conexion,$delete);
    }else{
        $resulta["_ID"] =0;
        $json['productos'][] =$resulta;
        echo json_encode($json);
    }
?>