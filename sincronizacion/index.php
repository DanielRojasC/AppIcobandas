<?php

require_once "controladores/sincronizacionControlador.php";


require_once "modelos/sincronizarModelo.php";


require_once "controladores/plantillaControlador.php";

$mostrarMensaje = new ControladorSincronizacion();
$mostrarMensaje->ctrSincronizarClientes();
