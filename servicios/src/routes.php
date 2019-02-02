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

	
/*==========  FIN DE LOGIN  ==============*/
	
	
	
/*=============================================
=            	  REGISTROS			         =
=============================================*/
	
    $app->post('/crearRegistro', 'crearRegistro');
    $app->put('/actualizarRegistro', 'actualizarRegistro');
    $app->get('/maxRegistro', 'getMaxRegistro');
	
/*==========  FIN DE REGISTROS  ==============*/
    
    

/*=============================================
=            	  PLANTAS			         =
=============================================*/
	
    $app->post('/crearPlanta', 'crearPlanta');
    $app->get('/ciudades', 'getCiudades');
	
	
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
    /*--------------------------------------------*/
	
	
	/*-------------  Obtener Transportador  -------------*/
    $app->get('/transportadores/{nombreUsuario}', 'getTransportadores');
    $app->get('/buscarRegistroTransportadorVertical/{idTransportador}', 'buscarRegistroTransportadorVertical');
    /*--------------------------------------------*/
    
	
/*==========  FIN DE TRANSPORTADOR  ==============*/
	
	



/*==============================================================
=           			BANDA PESADA 			            =
==============================================================*/
	
	$app->post('/registroBandaPesada', 'registroBandaPesada');
	$app->put('/actualizarBandaPesada', 'actualizarBandaPesada');
    
	
/*=================  FIN DE BANDA PESADA  ====================*/




	
/*===================================================================================
=       				    	 BANDA ELEVADORA 	       	   					   =
====================================================================================*/

	/*---------------------------  Tornillos  ----------------------------*/
	$app->post('/registroTornillosElevadora', 'registroTornillosElevadora');
	$app->put('/actualizarTornillos', 'actualizarTornillosElevadora');
    /*--------------------------------------------------------------------*/
	

	/*---------------------------  Empalme  ----------------------------*/
	$app->post('/registroEmpalmeElevadora', 'registroEmpalmeElevadora');
	$app->put('/actualizarEmpalmeElevadora', 'actualizarEmpalmeElevadora');
    /*--------------------------------------------------------------------*/

	
	/*-----------------------  Puertas Inspeccion -----------------------*/
	$app->post('/registroPuertaInspeccion', 'registroPuertaInspeccion');
	$app->put('/actualizarPuertaInspeccion', 'actualizarPuertaInspeccion');
    /*--------------------------------------------------------------------*/


    /*-----------------------  Seguridad Elevadora -----------------------*/
	$app->post('/registroSeguridadElevadora', 'registroSeguridadElevadora');
	$app->put('/actualizarSeguridadElevadora', 'actualizarSeguridadElevadora');
    /*--------------------------------------------------------------------*/


    /*-----------------------  Polea Motriz Elevadora -----------------------*/
	$app->post('/registroPoleaMotrizElevadora', 'registroPoleaMotrizElevadora');
	$app->put('/actualizarPoleaMotrizElevadora', 'actualizarPoleaMotrizElevadora');
    /*--------------------------------------------------------------------*/


      /*-----------------------  Polea Cola Elevadora -----------------------*/
	$app->post('/registroPoleaColaElevadora', 'registroPoleaColaElevadora');
	$app->put('/actualizarPoleaColaElevadora', 'actualizarPoleaColaElevadora');
    /*--------------------------------------------------------------------*/


      /*------------------------  Cangilón -----------------------------*/
	$app->post('/registroCangilon', 'registroCangilon');
	$app->put('/actualizarCangilon', 'actualizarCangilon');
    /*--------------------------------------------------------------------*/

	
	 /*-------------------  Condiciones Carga Elevadora --------------------*/
	$app->post('/registroCondicionesCargaElevadora', 'registroCondicionesCargaElevadora');
	$app->put('/actualizarCondicionesCargaElevadora', 'actualizarCondicionesCargaElevadora');
    /*--------------------------------------------------------------------*/

	
	 /*------------------------- Banda Elevadora --------------------------*/
	$app->post('/registroBandaElevadora', 'registroBandaElevadora');
	$app->put('/actualizarBandaElevadora', 'actualizarBandaElevadora');
    /*--------------------------------------------------------------------*/

/*===============================  FIN BANDA ELEVADORA ====================================*/





/*======================================================================================================
=								        	 BANDA HORIZONTAL       								   =
=======================================================================================================*/


/*----------------------------  Banda Horizontal  -------------------------*/
	$app->post('/registroBandaHorizontal', 'registroBandaHorizontal');
	$app->put('/actualizarBandaHorizontal', 'actualizarBandaHorizontal');
/*-------------------------------------------------------------------------*/


/*--------------------------------- Desviador ----------------------------*/
	$app->post('/registroDesviador', 'registroDesviador');
	$app->put('/actualizarDesviador', 'actualizarDesviador');
/*-------------------------------------------------------------------------*/


/*------------------------------- Polea Tensora ----------------------------*/
	$app->post('/registroPoleaTensora', 'registroPoleaTensora');
	$app->put('/actualizarPoleaTensora', 'actualizarPoleaTensora');
/*-------------------------------------------------------------------------*/


/*---------------------------- Sistema Alineación -----------------------*/
	$app->post('/registroAlineacion', 'registroAlineacion');
	$app->put('/actualizarAlineacion', 'actualizarAlineacion');
/*-------------------------------------------------------------------------*/


/*--------------------------------- Seguridad -----------------------------*/
	$app->post('/registroSeguridad', 'registroSeguridad');
	$app->put('/actualizarSeguridad', 'actualizarSeguridad');
/*-------------------------------------------------------------------------*/


/*----------------------------- Polea Motriz -----------------------------*/
	$app->post('/registroPoleaMotrizHorizontal', 'registroPoleaMotrizHorizontal');
	$app->put('/actualizarPoleaMotrizHorizontal', 'actualizarPoleaMotrizHorizontal');
/*-------------------------------------------------------------------------*/


/*----------------------------- Polea Cola -----------------------------*/
	$app->post('/registroPoleaColaHorizontal', 'registroPoleaColaHorizontal');
	$app->put('/actualizarPoleaColaHorizontal', 'actualizarPoleaColaHorizontal');
/*-------------------------------------------------------------------------*/


/*----------------------------- Soporte Carga -----------------------------*/
	$app->post('/registroSoporteCarga', 'registroSoporteCarga');
	$app->put('/actualizarSoporteCarga', 'actualizarSoporteCarga');

/*-------------------------------------------------------------------------*/


/*----------------------------- Condicion Carga -----------------------------*/
	$app->post('/registroCondicionCargaTransportadora', 'registroCondicionCargaTransportadora');
	$app->put('/actualizarCondicionCargaTransportadora', 'actualizarCondicionCargaTransportadora');
/*-------------------------------------------------------------------------*/


/*----------------------------- Limpiador Primario -----------------------------*/
	$app->post('/registroLimpiadorPrimario', 'registroLimpiadorPrimario');
	$app->put('/actualizarLimpiadorPrimario', 'actualizarLimpiadorPrimario');
/*-------------------------------------------------------------------------*/


/*----------------------------- Limpiador Primario -----------------------------*/
	$app->post('/registroLimpiadorSecundario', 'registroLimpiadorSecundario');
	$app->put('/actualizarLimpiadorSecundaro', 'actualizarLimpiadorSecundaro');
/*-------------------------------------------------------------------------*/



/*----------------------------- Polea Amarre -----------------------------*/
	$app->post('/registroPoleaAmarre', 'registroPoleaAmarre');
	$app->put('/actualizarPoleaAmarre', 'actualizarPoleaAmarre');
/*-------------------------------------------------------------------------*/


/*----------------------------- Rodillo Carga -----------------------------*/
	$app->post('/registroRodilloCarga', 'registroRodilloCarga');
	$app->put('/actualizarRodilloCarga', 'actualizarRodilloCarga');
/*-------------------------------------------------------------------------*/

/*----------------------------- Rodillo Retorno -----------------------------*/
	$app->post('/registroRodilloRetorno', 'registroRodilloRetorno');
	$app->put('/actualizarRodilloRetorno', 'actualizarRodilloRetorno');
/*-------------------------------------------------------------------------*/


/*====================================  End of BANDA HORIZONTAL  =======================================*/


});
