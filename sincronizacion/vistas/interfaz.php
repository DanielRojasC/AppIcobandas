<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title></title>
	<link rel="stylesheet" href="">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script src="vistas/js/temporizador.js" type="text/javascript" charset="utf-8"></script>
</head>
<body>
	<form action="" method="post" accept-charset="utf-8">
	<input type="hidden" name="sincronizar" value="asdas">
	<button type="submit" name="botonSincronizar" id="botonSincronizar">SINCRONIZAR</button>	
	</form>
	<?php
$mostrarMensaje = new ControladorSincronizacion();
$mostrarMensaje->ctrSincronizarClientes();

?>
</body>
</html>