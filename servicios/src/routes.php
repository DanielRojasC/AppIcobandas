<?php
use Slim\Http\Request;
use Slim\Http\Response;
// Routes
$app->get('/[{name}]', function (Request $request, Response $response, array $args) {
    // Sample log message
    $this->logger->info("Slim-Skeleton '/' route");
    // Render index view
    return $this->renderer->render($response, 'index.phtml', $args);
});
$app->group('/api', function () use ($app) {
    // Version group
	
	
	
/*=============================================
=            	  LOGIN			         =
=============================================*/
	
	
    $app->get('/login/{nombreUsuario}&{contra}', 'login');
    $app->get('/loginAdmin', 'loginAdmin');
    $app->get('/cliente', 'cliente');


	
/*==========  FIN DE LOGIN  ==============*/
	
	
	
/*=============================================
=            	  REGISTROS			         =
=============================================*/
	
    $app->post('/crearRegistro', 'crearRegistro');
    $app->post('/actualizarRegistro', 'actualizarRegistro');
    $app->get('/maxRegistro', 'getMaxRegistro');
    $app->post('/actualizarEstadoRegistro/{idRegistro}&{estadoRegistro}', 'actualizarEstadoRegistro');

	
/*==========  FIN DE REGISTROS  ==============*/
    
    

/*=============================================
=            	  PLANTAS			         =
=============================================*/
	
    $app->post('/crearPlanta', 'crearPlanta');
    $app->get('/ciudades', 'getCiudades');
    $app->get('/maxPlanta', 'maxPlanta');
	
	
/*==========  FIN DE PLANTAS  ==============*/
	
	
	
/*=============================================
=            CLIENTES            =
=============================================*/
	
	/*-----  Validación NIT y Crear Cliente  ------*/
	$app->get('/validarNit/{nit}', 'validarNit');
	$app->post('/crearCliente', 'crearCliente');
	/*--------------------------------------------*/


/*==========  FIN DE CLIENTES  ==============*/



/*=============================================
=            TRANSPORTADOR            =
=============================================*/
	
	/*-------------  Crear Transportador  -------------*/
    $app->post('/crearTransportador', 'crearTransportador');
    $app->get('/maxTransportador', 'maxTransportador');
    $app->post('/actualizarTransportador/{idTransportador}', 'actualizarTransportador');

    


    /*--------------------------------------------*/
	
	
	/*-------------  Obtener Transportador  -------------*/
    $app->get('/transportadores/{nombreUsuario}', 'getTransportadores');
    $app->get('/transportadoresAdmin', 'getTransportadoresAdmin');

    $app->get('/buscarRegistroTransportadorVertical/{idTransportador}', 'buscarRegistroTransportadorVertical');
    /*--------------------------------------------*/
    
	
/*==========  FIN DE TRANSPORTADOR  ==============*/
	
	



/*==============================================================
=           			BANDA PESADA 			            =
==============================================================*/
	
	$app->post('/registroBandaPesada', 'registroBandaPesada');
	$app->post('/actualizarBandaPesada', 'actualizarBandaPesada');
    
	
/*=================  FIN DE BANDA PESADA  ====================*/




	
/*===================================================================================
=       				    	 BANDA ELEVADORA 	       	   					   =
====================================================================================*/
	$app->post('/sincronizacionElevadora', 'sincronizacionElevadora');
	$app->post('/actualizarElevadora', 'actualizarElevadora');

	$app->post('observacionElevadora', 'observacionElevadora');
	$app->post('/actualizarObservacionElevadora', 'actualizarObservacionElevadora');






	/*---------------------------  Tornillos  ----------------------------*/
	$app->post('/registroTornillosElevadora', 'registroTornillosElevadora');
	$app->post('/actualizarTornillos', 'actualizarTornillosElevadora');
    /*--------------------------------------------------------------------*/
	

	/*---------------------------  Empalme  ----------------------------*/
	$app->post('/registroEmpalmeElevadora', 'registroEmpalmeElevadora');
	$app->post('/actualizarEmpalmeElevadora', 'actualizarEmpalmeElevadora');
    /*--------------------------------------------------------------------*/

	
	/*-----------------------  Puertas Inspeccion -----------------------*/
	$app->post('/registroPuertaInspeccion', 'registroPuertaInspeccion');
	$app->post('/actualizarPuertaInspeccion', 'actualizarPuertaInspeccion');
    /*--------------------------------------------------------------------*/


    /*-----------------------  Seguridad Elevadora -----------------------*/
	$app->post('/registroSeguridadElevadora', 'registroSeguridadElevadora');
	$app->post('/actualizarSeguridadElevadora', 'actualizarSeguridadElevadora');
    /*--------------------------------------------------------------------*/


    /*-----------------------  Polea Motriz Elevadora -----------------------*/
	$app->post('/registroPoleaMotrizElevadora', 'registroPoleaMotrizElevadora');
	$app->post('/actualizarPoleaMotrizElevadora', 'actualizarPoleaMotrizElevadora');
    /*--------------------------------------------------------------------*/


      /*-----------------------  Polea Cola Elevadora -----------------------*/
	$app->post('/registroPoleaColaElevadora', 'registroPoleaColaElevadora');
	$app->post('/actualizarPoleaColaElevadora', 'actualizarPoleaColaElevadora');
    /*--------------------------------------------------------------------*/


      /*------------------------  Cangilón -----------------------------*/
	$app->post('/registroCangilon', 'registroCangilon');
	$app->post('/actualizarCangilon', 'actualizarCangilon');
    /*--------------------------------------------------------------------*/

	
	 /*-------------------  Condiciones Carga Elevadora --------------------*/
	$app->post('/registroCondicionesCargaElevadora', 'registroCondicionesCargaElevadora');
	$app->post('/actualizarCondicionesCargaElevadora', 'actualizarCondicionesCargaElevadora');
    /*--------------------------------------------------------------------*/

	
	 /*------------------------- Banda Elevadora --------------------------*/
	$app->post('/registroBandaElevadora', 'registroBandaElevadora');
	$app->post('/actualizarBandaElevadora', 'actualizarBandaElevadora');
    /*--------------------------------------------------------------------*/

/*===============================  FIN BANDA ELEVADORA ====================================*/





/*======================================================================================================
=								        	 BANDA HORIZONTAL       								   =
=======================================================================================================*/

$app->post('/sincronizarHorizontal', 'sincronizarHorizontal');
$app->post('/actualizarHorizontal', 'actualizarHorizontal');


$app->post('/observacionTransportadora', 'observacionTransportadora');
	$app->post('/actualizarObservacionTransportadora', 'actualizarObservacionTransportadora');

/*----------------------------  Banda Horizontal  -------------------------*/
	$app->post('/registroBandaHorizontal', 'registroBandaHorizontal');
	$app->post('/actualizarBandaHorizontal', 'actualizarBandaHorizontal');
/*-------------------------------------------------------------------------*/


/*--------------------------------- Desviador ----------------------------*/
	$app->post('/registroDesviador', 'registroDesviador');
	$app->post('/actualizarDesviador', 'actualizarDesviador');
/*-------------------------------------------------------------------------*/


/*------------------------------- Polea Tensora ----------------------------*/
	$app->post('/registroPoleaTensora', 'registroPoleaTensora');
	$app->post('/actualizarPoleaTensora', 'actualizarPoleaTensora');
/*-------------------------------------------------------------------------*/


/*---------------------------- Sistema Alineación -----------------------*/
	$app->post('/registroAlineacion', 'registroAlineacion');
	$app->post('/actualizarAlineacion', 'actualizarAlineacion');
/*-------------------------------------------------------------------------*/


/*--------------------------------- Seguridad -----------------------------*/
	$app->post('/registroSeguridad', 'registroSeguridad');
	$app->post('/actualizarSeguridad', 'actualizarSeguridad');
/*-------------------------------------------------------------------------*/


/*----------------------------- Polea Motriz -----------------------------*/
	$app->post('/registroPoleaMotrizHorizontal', 'registroPoleaMotrizHorizontal');
	$app->post('/actualizarPoleaMotrizHorizontal', 'actualizarPoleaMotrizHorizontal');
/*-------------------------------------------------------------------------*/


/*----------------------------- Polea Cola -----------------------------*/
	$app->post('/registroPoleaColaHorizontal', 'registroPoleaColaHorizontal');
	$app->post('/actualizarPoleaColaHorizontal', 'actualizarPoleaColaHorizontal');
/*-------------------------------------------------------------------------*/


/*----------------------------- Soporte Carga -----------------------------*/
	$app->post('/registroSoporteCarga', 'registroSoporteCarga');
	$app->post('/actualizarSoporteCarga', 'actualizarSoporteCarga');

/*-------------------------------------------------------------------------*/


/*----------------------------- Condicion Carga -----------------------------*/
	$app->post('/registroCondicionCargaTransportadora', 'registroCondicionCargaTransportadora');
	$app->post('/actualizarCondicionCargaTransportadora', 'actualizarCondicionCargaTransportadora');
/*-------------------------------------------------------------------------*/


/*----------------------------- Limpiador Primario -----------------------------*/
	$app->post('/registroLimpiadorPrimario', 'registroLimpiadorPrimario');
	$app->post('/actualizarLimpiadorPrimario', 'actualizarLimpiadorPrimario');
/*-------------------------------------------------------------------------*/


/*----------------------------- Limpiador Primario -----------------------------*/
	$app->post('/registroLimpiadorSecundario', 'registroLimpiadorSecundario');
	$app->post('/actualizarLimpiadorSecundaro', 'actualizarLimpiadorSecundaro');
/*-------------------------------------------------------------------------*/



/*----------------------------- Polea Amarre -----------------------------*/
	$app->post('/registroPoleaAmarre', 'registroPoleaAmarre');
	$app->post('/actualizarPoleaAmarre', 'actualizarPoleaAmarre');
/*-------------------------------------------------------------------------*/


/*----------------------------- Rodillo Carga -----------------------------*/
	$app->post('/registroRodilloCarga', 'registroRodilloCarga');
	$app->post('/actualizarRodilloCarga', 'actualizarRodilloCarga');
/*-------------------------------------------------------------------------*/

/*----------------------------- Rodillo Retorno -----------------------------*/
	$app->post('/registroRodilloRetorno', 'registroRodilloRetorno');
	$app->post('/actualizarRodilloRetorno', 'actualizarRodilloRetorno');
/*-------------------------------------------------------------------------*/


/*====================================  End of BANDA HORIZONTAL  =======================================*/


/*=============================================
=        	  SINCRONIZACION		     =
=============================================*/
	
    $app->post('/sincroClientes', 'sincroClientes');

    $app->post('/sincronizarBasesDatosClientes', 'sincronizarBasesDatosClientes');



	
/*====================================  End of SINCRONIZACION =======================================*/


});
