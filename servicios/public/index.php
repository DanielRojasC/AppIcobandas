<?php
if (PHP_SAPI == 'cli-server') {
    // To help the built-in PHP dev server, check if the request was actually for
    // something which should probably be served as a static file
    phpinfo(INFO_MODULES);
    $url  = parse_url($_SERVER['REQUEST_URI']);
    $file = __DIR__ . $url['path'];
    if (is_file($file)) {
        return false;
    }
}

// function getConnection()
// {
//     $link = new PDO("sqlsrv:Server=localhost;Database=prueba");
//     $link->exec("set names utf8");

//     return $link;
// }

function getConnection()
{
    $_MYSQL_HOST = "190.14.240.195, 1443 "; //servidor
    $_MYSQL_DB = "base_prueba"; //base de datos
    $_MYSQL_USER = "jnavarro"; //Usuario
    $_MYSQL_PASS = "Bfr3641#"; //Password
    $link = new PDO("sqlsrv:Server=localhost ;Database=prueba");
//     try {
//     $pdo = new PDO('mysql:host=' . $_MYSQL_HOST . ';dbname=' . $_MYSQL_DB .';charset=utf8', $_MYSQL_USER, $_MYSQL_PASS);  //parametros de conexion
//     $pdo->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC); //atributos para la base de datos
//     echo 'LA CONEXION A LA BASE DE DATOS SE REALIZÓ SATISFACTORIAMENTE
// ';
// } catch (PDOException $e) { // captura de errores
//     echo 'ERROR AL CONECTAR A LA BASE DE DATOS' . $e -> getMessage(); // mensaje de errores
//     die(); //finalizar script 

    
    $link->exec("set names utf8");

    return $link;
}


/*============================================================================================================================
=                                                           LOGIN                                                          =
============================================================================================================================*/


function login($response)
{
    $route         = $response->getAttribute('route');
    $args          = $route->getArguments();
    $nombreUsuario = $args['nombreUsuario'];
    $contraUsuario = $args['contra'];

    // $contrasenia  = hash('sha512', $contraUsuario);
    $sql = "SELECT        agentes.codagente, agentes.nombreagte, agentes.pwdagente, registro.idRegistro, registro.fechaRegistro, registro.idTransportador, registro.codplanta AS codPlantaRegistro, registro.usuarioRegistro, 
                        bandaTransportadora.marcaBandaTransportadora, bandaTransportadora.anchoBandaTransportadora, bandaTransportadora.noLonasBandaTransportadora, 
                         bandaTransportadora.tipoLonaBandaTransportadora, bandaTransportadora.espesorTotalBandaTransportadora, bandaTransportadora.espesorCubiertaSuperiorTransportadora, bandaTransportadora.espesorCojinTransportadora, 
                         bandaTransportadora.espesorCubiertaInferiorTransportadora, bandaTransportadora.tipoCubiertaTransportadora, bandaTransportadora.tipoEmpalmeTransportadora, bandaTransportadora.estadoEmpalmeTransportadora, 
                         bandaTransportadora.resistenciaRoturaLonaTransportadora, bandaTransportadora.localizacionTensorTransportadora, bandaTransportadora.bandaReversible, bandaTransportadora.bandaDeArrastre, 
                         bandaTransportadora.diametroPoleaColaTransportadora, bandaTransportadora.anchoPoleaColaTransportadora, bandaTransportadora.tipoPoleaColaTransportadora, bandaTransportadora.largoEjePoleaColaTransportadora, 
                         bandaTransportadora.diametroEjePoleaColaHorizontal, bandaTransportadora.icobandasCentradaPoleaColaTransportadora, bandaTransportadora.anguloAmarrePoleaColaTransportadora, bandaTransportadora.estadoRvtoPoleaColaTransportadora, 
                         bandaTransportadora.tipoTransicionPoleaColaTransportadora, bandaTransportadora.distanciaTransicionPoleaColaTransportadora, bandaTransportadora.longitudTensorTornilloPoleaColaTransportadora, 
                         bandaTransportadora.longitudRecorridoContrapesaPoleaColaTransportadora, bandaTransportadora.guardaPoleaColaTransportadora, bandaTransportadora.hayDesviador, bandaTransportadora.elDesviadorBascula, 
                         bandaTransportadora.presionUniformeALoAnchoDeLaBanda, bandaTransportadora.cauchoVPlow, bandaTransportadora.anchoVPlow, bandaTransportadora.espesorVPlow, bandaTransportadora.tipoRevestimientoTolvaCarga, 
                         bandaTransportadora.estadoRevestimientoTolvaCarga, bandaTransportadora.duracionPromedioRevestimiento, bandaTransportadora.deflectores, bandaTransportadora.altureCaida, bandaTransportadora.longitudImpacto, 
                         bandaTransportadora.material, bandaTransportadora.anguloSobreCarga, bandaTransportadora.ataqueQuimicoTransportadora, bandaTransportadora.ataqueTemperaturaTransportadora, 
                         bandaTransportadora.ataqueAceiteTransportadora, bandaTransportadora.ataqueImpactoTransportadora, bandaTransportadora.capacidadTransportadora, bandaTransportadora.horasTrabajoPorDiaTransportadora, 
                         bandaTransportadora.diasTrabajPorSemanaTransportadora, bandaTransportadora.alimentacionCentradaTransportadora, bandaTransportadora.abrasividadTransportadora, bandaTransportadora.porcentajeFinosTransportadora, 
                         bandaTransportadora.maxGranulometriaTransportadora, bandaTransportadora.maxPesoTransportadora, bandaTransportadora.densidadTransportadora, bandaTransportadora.tempMaximaMaterialSobreBandaTransportadora, 
                         bandaTransportadora.tempPromedioMaterialSobreBandaTransportadora, bandaTransportadora.cajaColaDeTolva, bandaTransportadora.fugaMateriales, bandaTransportadora.fugaDeMaterialesEnLaColaDelChute, 
                         bandaTransportadora.fugaDeMaterialesPorLosCostados, bandaTransportadora.fugaDeMaterialParticulaALaSalidaDelChute, bandaTransportadora.anchoChute, bandaTransportadora.largoChute, 
                         bandaTransportadora.alturaChute, bandaTransportadora.abrazadera, bandaTransportadora.cauchoGuardabandas, bandaTransportadora.triSealMultiSeal, bandaTransportadora.espesorGuardaBandas, 
                         bandaTransportadora.anchoGuardaBandas, bandaTransportadora.largoGuardaBandas, bandaTransportadora.protectorGuardaBandas, bandaTransportadora.cortinaAntiPolvo1, bandaTransportadora.cortinaAntiPolvo2, 
                         bandaTransportadora.cortinaAntiPolvo3, bandaTransportadora.boquillasCanonesDeAire, bandaTransportadora.tempAmbienteMaxTransportadora, bandaTransportadora.tempAmbienteMinTransportadora, 
                         bandaTransportadora.tieneRodillosImpacto, bandaTransportadora.camaImpacto, bandaTransportadora.camaSellado, bandaTransportadora.basculaPesaje, bandaTransportadora.rodilloCarga, 
                         bandaTransportadora.rodilloImpacto, bandaTransportadora.basculaASGCO, bandaTransportadora.barraImpacto, bandaTransportadora.barraDeslizamiento, bandaTransportadora.espesorUHMV, bandaTransportadora.anchoBarra,
                          bandaTransportadora.largoBarra, bandaTransportadora.anguloAcanalamientoArtesa1, bandaTransportadora.anguloAcanalamientoArtesa2, bandaTransportadora.anguloAcanalamientoArtesa3, 
                         bandaTransportadora.anguloAcanalamientoArtesa1AntesPoleaMotriz, bandaTransportadora.anguloAcanalamientoArtesa2AntesPoleaMotriz, bandaTransportadora.anguloAcanalamientoArtesa3AntesPoleaMotriz, 
                         bandaTransportadora.integridadSoportesRodilloImpacto, bandaTransportadora.materialAtrapadoEntreCortinas, bandaTransportadora.materialAtrapadoEntreGuardabandas, bandaTransportadora.materialAtrapadoEnBanda, 
                         bandaTransportadora.integridadSoportesCamaImpacto, bandaTransportadora.integridadSoporteCamaSellado, bandaTransportadora.inclinacionZonaCargue, bandaTransportadora.sistemaAlineacionCarga, 
                         bandaTransportadora.cantidadSistemaAlineacionEnCarga, bandaTransportadora.sistemasAlineacionCargaFuncionando, bandaTransportadora.sistemaAlineacionEnRetorno, 
                         bandaTransportadora.cantidadSistemaAlineacionEnRetorno, bandaTransportadora.sistemasAlineacionRetornoFuncionando, bandaTransportadora.sistemaAlineacionRetornoPlano, 
                         bandaTransportadora.sistemaAlineacionArtesaCarga, bandaTransportadora.sistemaAlineacionRetornoEnV, bandaTransportadora.largoEjeRodilloCentralCarga, bandaTransportadora.diametroEjeRodilloCentralCarga, 
                         bandaTransportadora.largoTuboRodilloCentralCarga, bandaTransportadora.largoEjeRodilloLateralCarga, bandaTransportadora.diametroEjeRodilloLateralCarga, bandaTransportadora.diametroRodilloLateralCarga, 
                         bandaTransportadora.largoTuboRodilloLateralCarga, bandaTransportadora.tipoRodilloCarga, bandaTransportadora.distanciaEntreArtesasCarga, bandaTransportadora.anchoInternoChasisRodilloCarga, 
                         bandaTransportadora.anchoExternoChasisRodilloCarga, bandaTransportadora.anguloAcanalamientoArtesaCArga, bandaTransportadora.detalleRodilloCentralCarga, bandaTransportadora.detalleRodilloLateralCarg, 
                         bandaTransportadora.diametroPoleaMotrizTransportadora, bandaTransportadora.anchoPoleaMotrizTransportadora, bandaTransportadora.tipoPoleaMotrizTransportadora, 
                         bandaTransportadora.largoEjePoleaMotrizTransportadora, bandaTransportadora.diametroEjeMotrizTransportadora, bandaTransportadora.icobandasCentraEnPoleaMotrizTransportadora, 
                         bandaTransportadora.anguloAmarrePoleaMotrizTransportadora, bandaTransportadora.estadoRevestimientoPoleaMotrizTransportadora, bandaTransportadora.tipoTransicionPoleaMotrizTransportadora, 
                         bandaTransportadora.distanciaTransicionPoleaMotrizTransportadora, bandaTransportadora.potenciaMotorTransportadora, bandaTransportadora.guardaPoleaMotrizTransportadora, bandaTransportadora.anchoEstructura, 
                         bandaTransportadora.anchoTrayectoCarga, bandaTransportadora.pasarelaRespectoAvanceBanda, bandaTransportadora.materialAlimenticioTransportadora, bandaTransportadora.materialAcidoTransportadora, 
                         bandaTransportadora.materialTempEntre80y150Transportadora, bandaTransportadora.materialSecoTransportadora, bandaTransportadora.materialHumedoTransportadora, 
                         bandaTransportadora.materialAbrasivoFinoTransportadora, bandaTransportadora.materialPegajosoTransportadora, bandaTransportadora.materialGrasosoAceitosoTransportadora, bandaTransportadora.marcaLimpiadorPrimario,
                          bandaTransportadora.referenciaLimpiadorPrimario, bandaTransportadora.anchoCuchillaLimpiadorPrimario, bandaTransportadora.altoCuchillaLimpiadorPrimario, bandaTransportadora.estadoCuchillaLimpiadorPrimario, 
                         bandaTransportadora.estadoTensorLimpiadorPrimario, bandaTransportadora.estadoTuboLimpiadorPrimario, bandaTransportadora.frecuenciaRevisionCuchilla, bandaTransportadora.cuchillaEnContactoConBanda, 
                         bandaTransportadora.marcaLimpiadorSecundario, bandaTransportadora.referenciaLimpiadorSecundario, bandaTransportadora.anchoCuchillaLimpiadorSecundario, bandaTransportadora.altoCuchillaLimpiadorSecundario, 
                         bandaTransportadora.estadoCuchillaLimpiadorSecundario, bandaTransportadora.estadoTensorLimpiadorSecundario, bandaTransportadora.estadoTuboLimpiadorSecundario, bandaTransportadora.frecuenciaRevisionCuchilla1, 
                         bandaTransportadora.cuchillaEnContactoConBanda1, bandaTransportadora.sistemaDribbleChute, bandaTransportadora.marcaLimpiadorTerciario, bandaTransportadora.referenciaLimpiadorTerciario, 
                         bandaTransportadora.anchoCuchillaLimpiadorTerciario, bandaTransportadora.altoCuchillaLimpiadorTerciario, bandaTransportadora.estadoCuchillaLimpiadorTerciario, bandaTransportadora.estadoTensorLimpiadorTerciario, 
                         bandaTransportadora.estadoTuboLimpiadorTerciario, bandaTransportadora.frecuenciaRevisionCuchilla2, bandaTransportadora.cuchillaEnContactoConBanda2, bandaTransportadora.estadoRodilloRetorno, 
                         bandaTransportadora.largoEjeRodilloRetorno, bandaTransportadora.diametroEjeRodilloRetorno, bandaTransportadora.diametroRodilloRetorno, bandaTransportadora.largoTuboRodilloRetorno, 
                         bandaTransportadora.tipoRodilloRetorno, bandaTransportadora.distanciaEntreRodillosRetorno, bandaTransportadora.anchoInternoChasisRetorno, bandaTransportadora.anchoExternoChasisRetorno, 
                         bandaTransportadora.detalleRodilloRetorno, bandaTransportadora.diametroPoleaAmarrePoleaMotriz, bandaTransportadora.anchoPoleaAmarrePoleaMotriz, bandaTransportadora.tipoPoleaAmarrePoleaMotriz, 
                         bandaTransportadora.largoEjePoleaAmarrePoleaMotriz, bandaTransportadora.diametroEjePoleaAmarrePoleaMotriz, bandaTransportadora.icobandasCentradaPoleaAmarrePoleaMotriz, 
                         bandaTransportadora.estadoRevestimientoPoleaAmarrePoleaMotriz, bandaTransportadora.dimetroPoleaAmarrePoleaCola, bandaTransportadora.anchoPoleaAmarrePoleaCola, bandaTransportadora.tipoPoleaAmarrePoleaCola, 
                         bandaTransportadora.largoEjePoleaAmarrePoleaCola, bandaTransportadora.diametroEjePoleaAmarrePoleaCola, bandaTransportadora.icobandasCentradaPoleaAmarrePoleaCola, 
                         bandaTransportadora.estadoRevestimientoPoleaAmarrePoleaCola, bandaTransportadora.diametroPoleaTensora, bandaTransportadora.anchoPoleaTensora, bandaTransportadora.tipoPoleaTensora, 
                         bandaTransportadora.largoEjePoleaTensora, bandaTransportadora.diametroEjePoleaTensora, bandaTransportadora.icobandasCentradaEnPoleaTensora, bandaTransportadora.recorridoPoleaTensora, 
                         bandaTransportadora.estadoRevestimientoPoleaTensora, bandaTransportadora.tipoTransicionPoleaTensora, bandaTransportadora.distanciaTransicionPoleaColaTensora, bandaTransportadora.potenciaMotorPoleaTensora, 
                         bandaTransportadora.guardaPoleaTensora, bandaTransportadora.puertasInspeccion, bandaTransportadora.guardaRodilloRetornoPlano, bandaTransportadora.guardaTruTrainer, bandaTransportadora.guardaPoleaDeflectora, 
                         bandaTransportadora.guardaZonaDeTransito, bandaTransportadora.guardaMotores, bandaTransportadora.guardaCadenas, bandaTransportadora.guardaCorreas, bandaTransportadora.interruptoresDeSeguridad, 
                         bandaTransportadora.sirenasDeSeguridad, bandaElevadora.marcaBandaElevadora, bandaElevadora.anchoBandaElevadora, bandaElevadora.noLonaBandaElevadora, 
                         bandaElevadora.tipoLonaBandaElevadora, bandaElevadora.espesorTotalBandaElevadora, bandaElevadora.espesorCojinActualElevadora, bandaElevadora.espesorCubiertaSuperiorElevadora, 
                         bandaElevadora.espesorCubiertaInferiorElevadora, bandaElevadora.tipoCubiertaElevadora, bandaElevadora.tipoEmpalmeElevadora, bandaElevadora.estadoEmpalmeElevadora, 
                         bandaElevadora.resistenciaRoturaLonaElevadora, bandaElevadora.velocidadBandaElevadora, bandaElevadora.pesoMaterialEnCadaCangilon, bandaElevadora.pesoCangilonVacio, bandaElevadora.materialCangilon, 
                         bandaElevadora.longitudCangilon, bandaElevadora.proyeccionCangilon, bandaElevadora.profundidadCangilon, bandaElevadora.marcaCangilon, bandaElevadora.referenciaCangilon, bandaElevadora.capacidadCangilon, 
                         bandaElevadora.noFilasCangilones, bandaElevadora.separacionCangilones, bandaElevadora.noAgujeros, bandaElevadora.distanciaBordeBandaEstructura, bandaElevadora.distanciaPosteriorBandaEstructura, 
                         bandaElevadora.distanciaLaboFrontalCangilonEstructura, bandaElevadora.distanciaBordesCangilonEstructura, bandaElevadora.tipoVentilacion, bandaElevadora.diametroPoleaMotrizElevadora, 
                         bandaElevadora.anchoPoleaMotrizElevadora, bandaElevadora.tipoPoleaMotrizElevadora, bandaElevadora.largoEjeMotrizElevadora, bandaElevadora.diametroEjeMotrizElevadora, 
                         bandaElevadora.bandaCentradaEnPoleaMotrizElevadora, bandaElevadora.estadoRevestimientoPoleaMotrizElevadora, bandaElevadora.potenciaMotorMotrizElevadora, bandaElevadora.rpmSalidaReductorMotrizElevadora, 
                         bandaElevadora.guardaReductorPoleaMotrizElevadora, bandaElevadora.diametroPoleaColaElevadora, bandaElevadora.anchoPoleaColaElevadora, bandaElevadora.tipoPoleaColaElevadora, 
                         bandaElevadora.largoEjePoleaColaElevadora, bandaElevadora.diametroEjePoleaColaElevadora, bandaElevadora.bandaCentradaEnPoleaColaElevadora, bandaElevadora.estadoRevestimientoPoleaColaElevadora, 
                         bandaElevadora.longitudTensorTornilloPoleaColaElevadora, bandaElevadora.longitudRecorridoContrapesaPoleaColaElevadora, bandaElevadora.cargaTrabajoBandaElevadora, bandaElevadora.temperaturaMaterialElevadora, 
                         bandaElevadora.empalmeMecanicoElevadora, bandaElevadora.diametroRoscaElevadora, bandaElevadora.largoTornilloElevadora, bandaElevadora.materialTornilloElevadora, 
                         bandaElevadora.anchoCabezaElevadorPuertaInspeccion, bandaElevadora.largoCabezaElevadorPuertaInspeccion, bandaElevadora.anchoBotaElevadorPuertaInspeccion, bandaElevadora.largoBotaElevadorPuertaInspeccion, 
                         bandaElevadora.monitorPeligro, bandaElevadora.rodamiento, bandaElevadora.monitorDesalineacion, bandaElevadora.monitorVelocidad, bandaElevadora.sensorInductivo, bandaElevadora.indicadorNivel, 
                         bandaElevadora.cajaUnion, bandaElevadora.alarmaYPantalla, bandaElevadora.interruptorSeguridad, bandaElevadora.materialElevadora, bandaElevadora.ataqueQuimicoElevadora, 
                         bandaElevadora.ataqueTemperaturaElevadora, bandaElevadora.ataqueAceitesElevadora, bandaElevadora.ataqueAbrasivoElevadora, bandaElevadora.capacidadElevadora, bandaElevadora.horasTrabajoDiaElevadora, 
                         bandaElevadora.diasTrabajoSemanaElevadora, bandaElevadora.abrasividadElevadora, bandaElevadora.porcentajeFinosElevadora, bandaElevadora.maxGranulometriaElevadora, bandaElevadora.densidadMaterialElevadora, 
                         bandaElevadora.tempMaxMaterialSobreBandaElevadora, bandaElevadora.tempPromedioMaterialSobreBandaElevadora, bandaElevadora.variosPuntosDeAlimentacion, bandaElevadora.lluviaDeMaterial, 
                         bandaElevadora.anchoPiernaElevador, bandaElevadora.tempAmbienteMin, bandaElevadora.tempAmbienteMax, bandaTransmision.anchoBandaTransmision, 
                         bandaTransmision.distanciaEntreCentrosTransmision, bandaTransmision.potenciaMotorTransmision, bandaTransmision.rpmSalidaReductorTransmision, bandaTransmision.diametroPoleaConducidaTransmision, 
                         bandaTransmision.anchoPoleaConducidaTransmision, bandaTransmision.diametroPoleaMotrizTransmision, bandaTransmision.anchoPoleaMotrizTransmision, plantas.nameplanta, plantas.codplanta, 
                         transportador.nombreTransportador, clientes.nameunido, clientes.nit, bandaElevadora.tipoCangilon, bandaElevadora.profundidadPiernaElevador, bandaElevadora.tipoCarga, bandaElevadora.tipoDescarga, 
                         bandaElevadora.marcaBandaElevadoraAnterior, bandaElevadora.anchoBandaElevadoraAnterior, bandaElevadora.noLonasBandaElevadoraAnterior, bandaElevadora.tipoLonaBandaElevadoraAnterior, 
                         bandaElevadora.espesorTotalBandaElevadoraAnterior, bandaElevadora.espesorCubiertaSuperiorBandaElevadoraAnterior, bandaElevadora.espesorCojinElevadoraAnterior, 
                         bandaElevadora.espesorCubiertaInferiorBandaElevadoraAnterior, bandaElevadora.tipoCubiertaElevadoraAnterior, bandaElevadora.tipoEmpalmeElevadoraAnterior, bandaElevadora.resistenciaRoturaBandaElevadoraAnterior, 
                         bandaElevadora.tonsTransportadasBandaElevadoraAnterior, bandaElevadora.velocidadBandaElevadoraAnterior, bandaElevadora.causaFallaCambioBandaElevadoraAnterior, bandaElevadora.distanciaEntrePoleasElevadora, 
                         transportador.tipoTransportador, bandaTransportadora.velocidadBandaHorizontal, bandaTransportadora.marcaBandaHorizontalAnterior, bandaTransportadora.anchoBandaHorizontalAnterior, 
                         bandaTransportadora.noLonasBandaHorizontalAnterior, bandaTransportadora.tipoLonaBandaHorizontalAnterior, bandaTransportadora.espesorTotalBandaHorizontalAnterior, 
                         bandaTransportadora.espesorCubiertaSuperiorBandaHorizontalAnterior, bandaTransportadora.espesorCubiertaInferiorBandaHorizontalAnterior, bandaTransportadora.espesorCojinBandaHorizontalAnterior, 
                         bandaTransportadora.tipoCubiertaBandaHorizontalAnterior, bandaTransportadora.tipoEmpalmeBandaHorizontalAnterior, bandaTransportadora.resistenciaRoturaLonaBandaHorizontalAnterior, 
                         bandaTransportadora.causaFallaCambioBandaHorizontal, bandaTransportadora.distanciaEntrePoleasBandaHorizontal, bandaTransportadora.inclinacionBandaHorizontal, 
                         bandaTransportadora.recorridoUtilTensorBandaHorizontal, bandaTransportadora.longitudSinfinBandaHorizontal, bandaTransportadora.tonsTransportadasBandaHoizontalAnterior, bandaTransportadora.guardaRodilloRetornoV, bandaTransportadora.diametroRodilloCentralCarga, bandaTransportadora.tipoRodilloImpacto, bandaTransmision.tipoParteTransmision, bandaTransportadora.ataqueAbrasivoTransportadora, agentes.estadoagte, registro.estadoRegistro,bandaTransmision.observacionRegistro, bandaElevadora.observacionRegistroElevadora, bandaTransportadora.observacionRegistroTransportadora
FROM            agentes LEFT OUTER JOIN
                         plantas ON agentes.codagente = plantas.agenteplanta LEFT OUTER JOIN
                         clientes ON clientes.nit = plantas.nitplanta LEFT OUTER JOIN
                         registro ON plantas.codplanta = registro.codplanta LEFT OUTER JOIN
                         transportador ON transportador.idTransportador = registro.idTransportador LEFT OUTER JOIN
                         bandaTransportadora ON bandaTransportadora.idRegistro = registro.idRegistro LEFT OUTER JOIN
                         bandaElevadora ON bandaElevadora.idRegistro = registro.idRegistro LEFT OUTER JOIN
                         bandaTransmision ON bandaTransmision.idRegistro = registro.idRegistro
WHERE        (agentes.codagente = '$nombreUsuario') AND (agentes.pwdagente = '$contraUsuario')";


    try {

        $stmt    = getConnection()->query($sql);
        $usuario = array();
        $usuario = $stmt->fetchAll(PDO::FETCH_OBJ);
        // $arreglo        = (array) $usuario;
        // $retorno        = array_map('utf8', $arreglo);
        $arrayCrudo     = json_encode($usuario);
        $JsonSinSlash   = str_replace("\/", "/", $arrayCrudo);
        $ArrayRespuesta = json_decode($JsonSinSlash, true);
        return $JsonSinSlash;
        var_dump($JsonSinSlash);
        $db = null;
    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

function loginAdmin($response)
{
    $route         = $response->getAttribute('route');
    $args          = $route->getArguments();
    

    // $contrasenia  = hash('sha512', $contraUsuario);
    $sql = "SELECT        agentes.codagente, agentes.nombreagte, agentes.pwdagente, registro.idRegistro, registro.fechaRegistro, registro.idTransportador, registro.codplanta AS codPlantaRegistro, registro.usuarioRegistro, 
                        bandaTransportadora.marcaBandaTransportadora, bandaTransportadora.anchoBandaTransportadora, bandaTransportadora.noLonasBandaTransportadora, 
                         bandaTransportadora.tipoLonaBandaTransportadora, bandaTransportadora.espesorTotalBandaTransportadora, bandaTransportadora.espesorCubiertaSuperiorTransportadora, bandaTransportadora.espesorCojinTransportadora, 
                         bandaTransportadora.espesorCubiertaInferiorTransportadora, bandaTransportadora.tipoCubiertaTransportadora, bandaTransportadora.tipoEmpalmeTransportadora, bandaTransportadora.estadoEmpalmeTransportadora, 
                         bandaTransportadora.resistenciaRoturaLonaTransportadora, bandaTransportadora.localizacionTensorTransportadora, bandaTransportadora.bandaReversible, bandaTransportadora.bandaDeArrastre, 
                         bandaTransportadora.diametroPoleaColaTransportadora, bandaTransportadora.anchoPoleaColaTransportadora, bandaTransportadora.tipoPoleaColaTransportadora, bandaTransportadora.largoEjePoleaColaTransportadora, 
                         bandaTransportadora.diametroEjePoleaColaHorizontal, bandaTransportadora.icobandasCentradaPoleaColaTransportadora, bandaTransportadora.anguloAmarrePoleaColaTransportadora, bandaTransportadora.estadoRvtoPoleaColaTransportadora, 
                         bandaTransportadora.tipoTransicionPoleaColaTransportadora, bandaTransportadora.distanciaTransicionPoleaColaTransportadora, bandaTransportadora.longitudTensorTornilloPoleaColaTransportadora, 
                         bandaTransportadora.longitudRecorridoContrapesaPoleaColaTransportadora, bandaTransportadora.guardaPoleaColaTransportadora, bandaTransportadora.hayDesviador, bandaTransportadora.elDesviadorBascula, 
                         bandaTransportadora.presionUniformeALoAnchoDeLaBanda, bandaTransportadora.cauchoVPlow, bandaTransportadora.anchoVPlow, bandaTransportadora.espesorVPlow, bandaTransportadora.tipoRevestimientoTolvaCarga, 
                         bandaTransportadora.estadoRevestimientoTolvaCarga, bandaTransportadora.duracionPromedioRevestimiento, bandaTransportadora.deflectores, bandaTransportadora.altureCaida, bandaTransportadora.longitudImpacto, 
                         bandaTransportadora.material, bandaTransportadora.anguloSobreCarga, bandaTransportadora.ataqueQuimicoTransportadora, bandaTransportadora.ataqueTemperaturaTransportadora, 
                         bandaTransportadora.ataqueAceiteTransportadora, bandaTransportadora.ataqueImpactoTransportadora, bandaTransportadora.capacidadTransportadora, bandaTransportadora.horasTrabajoPorDiaTransportadora, 
                         bandaTransportadora.diasTrabajPorSemanaTransportadora, bandaTransportadora.alimentacionCentradaTransportadora, bandaTransportadora.abrasividadTransportadora, bandaTransportadora.porcentajeFinosTransportadora, 
                         bandaTransportadora.maxGranulometriaTransportadora, bandaTransportadora.maxPesoTransportadora, bandaTransportadora.densidadTransportadora, bandaTransportadora.tempMaximaMaterialSobreBandaTransportadora, 
                         bandaTransportadora.tempPromedioMaterialSobreBandaTransportadora, bandaTransportadora.cajaColaDeTolva, bandaTransportadora.fugaMateriales, bandaTransportadora.fugaDeMaterialesEnLaColaDelChute, 
                         bandaTransportadora.fugaDeMaterialesPorLosCostados, bandaTransportadora.fugaDeMaterialParticulaALaSalidaDelChute, bandaTransportadora.anchoChute, bandaTransportadora.largoChute, 
                         bandaTransportadora.alturaChute, bandaTransportadora.abrazadera, bandaTransportadora.cauchoGuardabandas, bandaTransportadora.triSealMultiSeal, bandaTransportadora.espesorGuardaBandas, 
                         bandaTransportadora.anchoGuardaBandas, bandaTransportadora.largoGuardaBandas, bandaTransportadora.protectorGuardaBandas, bandaTransportadora.cortinaAntiPolvo1, bandaTransportadora.cortinaAntiPolvo2, 
                         bandaTransportadora.cortinaAntiPolvo3, bandaTransportadora.boquillasCanonesDeAire, bandaTransportadora.tempAmbienteMaxTransportadora, bandaTransportadora.tempAmbienteMinTransportadora, 
                         bandaTransportadora.tieneRodillosImpacto, bandaTransportadora.camaImpacto, bandaTransportadora.camaSellado, bandaTransportadora.basculaPesaje, bandaTransportadora.rodilloCarga, 
                         bandaTransportadora.rodilloImpacto, bandaTransportadora.basculaASGCO, bandaTransportadora.barraImpacto, bandaTransportadora.barraDeslizamiento, bandaTransportadora.espesorUHMV, bandaTransportadora.anchoBarra,
                          bandaTransportadora.largoBarra, bandaTransportadora.anguloAcanalamientoArtesa1, bandaTransportadora.anguloAcanalamientoArtesa2, bandaTransportadora.anguloAcanalamientoArtesa3, 
                         bandaTransportadora.anguloAcanalamientoArtesa1AntesPoleaMotriz, bandaTransportadora.anguloAcanalamientoArtesa2AntesPoleaMotriz, bandaTransportadora.anguloAcanalamientoArtesa3AntesPoleaMotriz, 
                         bandaTransportadora.integridadSoportesRodilloImpacto, bandaTransportadora.materialAtrapadoEntreCortinas, bandaTransportadora.materialAtrapadoEntreGuardabandas, bandaTransportadora.materialAtrapadoEnBanda, 
                         bandaTransportadora.integridadSoportesCamaImpacto, bandaTransportadora.integridadSoporteCamaSellado, bandaTransportadora.inclinacionZonaCargue, bandaTransportadora.sistemaAlineacionCarga, 
                         bandaTransportadora.cantidadSistemaAlineacionEnCarga, bandaTransportadora.sistemasAlineacionCargaFuncionando, bandaTransportadora.sistemaAlineacionEnRetorno, 
                         bandaTransportadora.cantidadSistemaAlineacionEnRetorno, bandaTransportadora.sistemasAlineacionRetornoFuncionando, bandaTransportadora.sistemaAlineacionRetornoPlano, 
                         bandaTransportadora.sistemaAlineacionArtesaCarga, bandaTransportadora.sistemaAlineacionRetornoEnV, bandaTransportadora.largoEjeRodilloCentralCarga, bandaTransportadora.diametroEjeRodilloCentralCarga, 
                         bandaTransportadora.largoTuboRodilloCentralCarga, bandaTransportadora.largoEjeRodilloLateralCarga, bandaTransportadora.diametroEjeRodilloLateralCarga, bandaTransportadora.diametroRodilloLateralCarga, 
                         bandaTransportadora.largoTuboRodilloLateralCarga, bandaTransportadora.tipoRodilloCarga, bandaTransportadora.distanciaEntreArtesasCarga, bandaTransportadora.anchoInternoChasisRodilloCarga, 
                         bandaTransportadora.anchoExternoChasisRodilloCarga, bandaTransportadora.anguloAcanalamientoArtesaCArga, bandaTransportadora.detalleRodilloCentralCarga, bandaTransportadora.detalleRodilloLateralCarg, 
                         bandaTransportadora.diametroPoleaMotrizTransportadora, bandaTransportadora.anchoPoleaMotrizTransportadora, bandaTransportadora.tipoPoleaMotrizTransportadora, 
                         bandaTransportadora.largoEjePoleaMotrizTransportadora, bandaTransportadora.diametroEjeMotrizTransportadora, bandaTransportadora.icobandasCentraEnPoleaMotrizTransportadora, 
                         bandaTransportadora.anguloAmarrePoleaMotrizTransportadora, bandaTransportadora.estadoRevestimientoPoleaMotrizTransportadora, bandaTransportadora.tipoTransicionPoleaMotrizTransportadora, 
                         bandaTransportadora.distanciaTransicionPoleaMotrizTransportadora, bandaTransportadora.potenciaMotorTransportadora, bandaTransportadora.guardaPoleaMotrizTransportadora, bandaTransportadora.anchoEstructura, 
                         bandaTransportadora.anchoTrayectoCarga, bandaTransportadora.pasarelaRespectoAvanceBanda, bandaTransportadora.materialAlimenticioTransportadora, bandaTransportadora.materialAcidoTransportadora, 
                         bandaTransportadora.materialTempEntre80y150Transportadora, bandaTransportadora.materialSecoTransportadora, bandaTransportadora.materialHumedoTransportadora, 
                         bandaTransportadora.materialAbrasivoFinoTransportadora, bandaTransportadora.materialPegajosoTransportadora, bandaTransportadora.materialGrasosoAceitosoTransportadora, bandaTransportadora.marcaLimpiadorPrimario,
                          bandaTransportadora.referenciaLimpiadorPrimario, bandaTransportadora.anchoCuchillaLimpiadorPrimario, bandaTransportadora.altoCuchillaLimpiadorPrimario, bandaTransportadora.estadoCuchillaLimpiadorPrimario, 
                         bandaTransportadora.estadoTensorLimpiadorPrimario, bandaTransportadora.estadoTuboLimpiadorPrimario, bandaTransportadora.frecuenciaRevisionCuchilla, bandaTransportadora.cuchillaEnContactoConBanda, 
                         bandaTransportadora.marcaLimpiadorSecundario, bandaTransportadora.referenciaLimpiadorSecundario, bandaTransportadora.anchoCuchillaLimpiadorSecundario, bandaTransportadora.altoCuchillaLimpiadorSecundario, 
                         bandaTransportadora.estadoCuchillaLimpiadorSecundario, bandaTransportadora.estadoTensorLimpiadorSecundario, bandaTransportadora.estadoTuboLimpiadorSecundario, bandaTransportadora.frecuenciaRevisionCuchilla1, 
                         bandaTransportadora.cuchillaEnContactoConBanda1, bandaTransportadora.sistemaDribbleChute, bandaTransportadora.marcaLimpiadorTerciario, bandaTransportadora.referenciaLimpiadorTerciario, 
                         bandaTransportadora.anchoCuchillaLimpiadorTerciario, bandaTransportadora.altoCuchillaLimpiadorTerciario, bandaTransportadora.estadoCuchillaLimpiadorTerciario, bandaTransportadora.estadoTensorLimpiadorTerciario, 
                         bandaTransportadora.estadoTuboLimpiadorTerciario, bandaTransportadora.frecuenciaRevisionCuchilla2, bandaTransportadora.cuchillaEnContactoConBanda2, bandaTransportadora.estadoRodilloRetorno, 
                         bandaTransportadora.largoEjeRodilloRetorno, bandaTransportadora.diametroEjeRodilloRetorno, bandaTransportadora.diametroRodilloRetorno, bandaTransportadora.largoTuboRodilloRetorno, 
                         bandaTransportadora.tipoRodilloRetorno, bandaTransportadora.distanciaEntreRodillosRetorno, bandaTransportadora.anchoInternoChasisRetorno, bandaTransportadora.anchoExternoChasisRetorno, 
                         bandaTransportadora.detalleRodilloRetorno, bandaTransportadora.diametroPoleaAmarrePoleaMotriz, bandaTransportadora.anchoPoleaAmarrePoleaMotriz, bandaTransportadora.tipoPoleaAmarrePoleaMotriz, 
                         bandaTransportadora.largoEjePoleaAmarrePoleaMotriz, bandaTransportadora.diametroEjePoleaAmarrePoleaMotriz, bandaTransportadora.icobandasCentradaPoleaAmarrePoleaMotriz, 
                         bandaTransportadora.estadoRevestimientoPoleaAmarrePoleaMotriz, bandaTransportadora.dimetroPoleaAmarrePoleaCola, bandaTransportadora.anchoPoleaAmarrePoleaCola, bandaTransportadora.tipoPoleaAmarrePoleaCola, 
                         bandaTransportadora.largoEjePoleaAmarrePoleaCola, bandaTransportadora.diametroEjePoleaAmarrePoleaCola, bandaTransportadora.icobandasCentradaPoleaAmarrePoleaCola, 
                         bandaTransportadora.estadoRevestimientoPoleaAmarrePoleaCola, bandaTransportadora.diametroPoleaTensora, bandaTransportadora.anchoPoleaTensora, bandaTransportadora.tipoPoleaTensora, 
                         bandaTransportadora.largoEjePoleaTensora, bandaTransportadora.diametroEjePoleaTensora, bandaTransportadora.icobandasCentradaEnPoleaTensora, bandaTransportadora.recorridoPoleaTensora, 
                         bandaTransportadora.estadoRevestimientoPoleaTensora, bandaTransportadora.tipoTransicionPoleaTensora, bandaTransportadora.distanciaTransicionPoleaColaTensora, bandaTransportadora.potenciaMotorPoleaTensora, 
                         bandaTransportadora.guardaPoleaTensora, bandaTransportadora.puertasInspeccion, bandaTransportadora.guardaRodilloRetornoPlano, bandaTransportadora.guardaTruTrainer, bandaTransportadora.guardaPoleaDeflectora, 
                         bandaTransportadora.guardaZonaDeTransito, bandaTransportadora.guardaMotores, bandaTransportadora.guardaCadenas, bandaTransportadora.guardaCorreas, bandaTransportadora.interruptoresDeSeguridad, 
                         bandaTransportadora.sirenasDeSeguridad, bandaElevadora.marcaBandaElevadora, bandaElevadora.anchoBandaElevadora, bandaElevadora.noLonaBandaElevadora, 
                         bandaElevadora.tipoLonaBandaElevadora, bandaElevadora.espesorTotalBandaElevadora, bandaElevadora.espesorCojinActualElevadora, bandaElevadora.espesorCubiertaSuperiorElevadora, 
                         bandaElevadora.espesorCubiertaInferiorElevadora, bandaElevadora.tipoCubiertaElevadora, bandaElevadora.tipoEmpalmeElevadora, bandaElevadora.estadoEmpalmeElevadora, 
                         bandaElevadora.resistenciaRoturaLonaElevadora, bandaElevadora.velocidadBandaElevadora, bandaElevadora.pesoMaterialEnCadaCangilon, bandaElevadora.pesoCangilonVacio, bandaElevadora.materialCangilon, 
                         bandaElevadora.longitudCangilon, bandaElevadora.proyeccionCangilon, bandaElevadora.profundidadCangilon, bandaElevadora.marcaCangilon, bandaElevadora.referenciaCangilon, bandaElevadora.capacidadCangilon, 
                         bandaElevadora.noFilasCangilones, bandaElevadora.separacionCangilones, bandaElevadora.noAgujeros, bandaElevadora.distanciaBordeBandaEstructura, bandaElevadora.distanciaPosteriorBandaEstructura, 
                         bandaElevadora.distanciaLaboFrontalCangilonEstructura, bandaElevadora.distanciaBordesCangilonEstructura, bandaElevadora.tipoVentilacion, bandaElevadora.diametroPoleaMotrizElevadora, 
                         bandaElevadora.anchoPoleaMotrizElevadora, bandaElevadora.tipoPoleaMotrizElevadora, bandaElevadora.largoEjeMotrizElevadora, bandaElevadora.diametroEjeMotrizElevadora, 
                         bandaElevadora.bandaCentradaEnPoleaMotrizElevadora, bandaElevadora.estadoRevestimientoPoleaMotrizElevadora, bandaElevadora.potenciaMotorMotrizElevadora, bandaElevadora.rpmSalidaReductorMotrizElevadora, 
                         bandaElevadora.guardaReductorPoleaMotrizElevadora, bandaElevadora.diametroPoleaColaElevadora, bandaElevadora.anchoPoleaColaElevadora, bandaElevadora.tipoPoleaColaElevadora, 
                         bandaElevadora.largoEjePoleaColaElevadora, bandaElevadora.diametroEjePoleaColaElevadora, bandaElevadora.bandaCentradaEnPoleaColaElevadora, bandaElevadora.estadoRevestimientoPoleaColaElevadora, 
                         bandaElevadora.longitudTensorTornilloPoleaColaElevadora, bandaElevadora.longitudRecorridoContrapesaPoleaColaElevadora, bandaElevadora.cargaTrabajoBandaElevadora, bandaElevadora.temperaturaMaterialElevadora, 
                         bandaElevadora.empalmeMecanicoElevadora, bandaElevadora.diametroRoscaElevadora, bandaElevadora.largoTornilloElevadora, bandaElevadora.materialTornilloElevadora, 
                         bandaElevadora.anchoCabezaElevadorPuertaInspeccion, bandaElevadora.largoCabezaElevadorPuertaInspeccion, bandaElevadora.anchoBotaElevadorPuertaInspeccion, bandaElevadora.largoBotaElevadorPuertaInspeccion, 
                         bandaElevadora.monitorPeligro, bandaElevadora.rodamiento, bandaElevadora.monitorDesalineacion, bandaElevadora.monitorVelocidad, bandaElevadora.sensorInductivo, bandaElevadora.indicadorNivel, 
                         bandaElevadora.cajaUnion, bandaElevadora.alarmaYPantalla, bandaElevadora.interruptorSeguridad, bandaElevadora.materialElevadora, bandaElevadora.ataqueQuimicoElevadora, 
                         bandaElevadora.ataqueTemperaturaElevadora, bandaElevadora.ataqueAceitesElevadora, bandaElevadora.ataqueAbrasivoElevadora, bandaElevadora.capacidadElevadora, bandaElevadora.horasTrabajoDiaElevadora, 
                         bandaElevadora.diasTrabajoSemanaElevadora, bandaElevadora.abrasividadElevadora, bandaElevadora.porcentajeFinosElevadora, bandaElevadora.maxGranulometriaElevadora, bandaElevadora.densidadMaterialElevadora, 
                         bandaElevadora.tempMaxMaterialSobreBandaElevadora, bandaElevadora.tempPromedioMaterialSobreBandaElevadora, bandaElevadora.variosPuntosDeAlimentacion, bandaElevadora.lluviaDeMaterial, 
                         bandaElevadora.anchoPiernaElevador, bandaElevadora.tempAmbienteMin, bandaElevadora.tempAmbienteMax, bandaTransmision.anchoBandaTransmision, 
                         bandaTransmision.distanciaEntreCentrosTransmision, bandaTransmision.potenciaMotorTransmision, bandaTransmision.rpmSalidaReductorTransmision, bandaTransmision.diametroPoleaConducidaTransmision, 
                         bandaTransmision.anchoPoleaConducidaTransmision, bandaTransmision.diametroPoleaMotrizTransmision, bandaTransmision.anchoPoleaMotrizTransmision, plantas.nameplanta, plantas.codplanta, 
                         transportador.nombreTransportador, clientes.nameunido, clientes.nit, bandaElevadora.tipoCangilon, bandaElevadora.profundidadPiernaElevador, bandaElevadora.tipoCarga, bandaElevadora.tipoDescarga, 
                         bandaElevadora.marcaBandaElevadoraAnterior, bandaElevadora.anchoBandaElevadoraAnterior, bandaElevadora.noLonasBandaElevadoraAnterior, bandaElevadora.tipoLonaBandaElevadoraAnterior, 
                         bandaElevadora.espesorTotalBandaElevadoraAnterior, bandaElevadora.espesorCubiertaSuperiorBandaElevadoraAnterior, bandaElevadora.espesorCojinElevadoraAnterior, 
                         bandaElevadora.espesorCubiertaInferiorBandaElevadoraAnterior, bandaElevadora.tipoCubiertaElevadoraAnterior, bandaElevadora.tipoEmpalmeElevadoraAnterior, bandaElevadora.resistenciaRoturaBandaElevadoraAnterior, 
                         bandaElevadora.tonsTransportadasBandaElevadoraAnterior, bandaElevadora.velocidadBandaElevadoraAnterior, bandaElevadora.causaFallaCambioBandaElevadoraAnterior, bandaElevadora.distanciaEntrePoleasElevadora, 
                         transportador.tipoTransportador, bandaTransportadora.velocidadBandaHorizontal, bandaTransportadora.marcaBandaHorizontalAnterior, bandaTransportadora.anchoBandaHorizontalAnterior, 
                         bandaTransportadora.noLonasBandaHorizontalAnterior, bandaTransportadora.tipoLonaBandaHorizontalAnterior, bandaTransportadora.espesorTotalBandaHorizontalAnterior, 
                         bandaTransportadora.espesorCubiertaSuperiorBandaHorizontalAnterior, bandaTransportadora.espesorCubiertaInferiorBandaHorizontalAnterior, bandaTransportadora.espesorCojinBandaHorizontalAnterior, 
                         bandaTransportadora.tipoCubiertaBandaHorizontalAnterior, bandaTransportadora.tipoEmpalmeBandaHorizontalAnterior, bandaTransportadora.resistenciaRoturaLonaBandaHorizontalAnterior, 
                         bandaTransportadora.causaFallaCambioBandaHorizontal, bandaTransportadora.distanciaEntrePoleasBandaHorizontal, bandaTransportadora.inclinacionBandaHorizontal, 
                         bandaTransportadora.recorridoUtilTensorBandaHorizontal, bandaTransportadora.longitudSinfinBandaHorizontal, bandaTransportadora.tonsTransportadasBandaHoizontalAnterior, bandaTransportadora.guardaRodilloRetornoV, bandaTransportadora.diametroRodilloCentralCarga, bandaTransportadora.tipoRodilloImpacto, bandaTransmision.tipoParteTransmision, bandaTransportadora.ataqueAbrasivoTransportadora, agentes.estadoagte, registro.estadoRegistro,bandaTransmision.observacionRegistro, bandaElevadora.observacionRegistroElevadora, bandaTransportadora.observacionRegistroTransportadora
FROM            agentes LEFT OUTER JOIN
                         plantas ON agentes.codagente = plantas.agenteplanta LEFT OUTER JOIN
                         clientes ON clientes.nit = plantas.nitplanta LEFT OUTER JOIN
                         registro ON plantas.codplanta = registro.codplanta LEFT OUTER JOIN
                         transportador ON transportador.idTransportador = registro.idTransportador LEFT OUTER JOIN
                         bandaTransportadora ON bandaTransportadora.idRegistro = registro.idRegistro LEFT OUTER JOIN
                         bandaElevadora ON bandaElevadora.idRegistro = registro.idRegistro LEFT OUTER JOIN
                         bandaTransmision ON bandaTransmision.idRegistro = registro.idRegistro";


    try {

        $stmt    = getConnection()->query($sql);
        $usuario = array();
        $usuario = $stmt->fetchAll(PDO::FETCH_OBJ);
        // $arreglo        = (array) $usuario;
        // $retorno        = array_map('utf8', $arreglo);
        $arrayCrudo     = json_encode($usuario);
        $JsonSinSlash   = str_replace("\/", "/", $arrayCrudo);
        $ArrayRespuesta = json_decode($JsonSinSlash, true);
        return $JsonSinSlash;
        var_dump($JsonSinSlash);
        $db = null;
    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}
/*==========================================================================================================================
=                                                        FIN DE LOGIN                                                =
===========================================================================================================================*/

/*--------------------------------------------------------------------------------------------------------------------------*/


/*==========================================================================================================================
=                                                        TRANSPORTADORES                                                    =
===========================================================================================================================*/

function getTransportadores($response)
{
    $route = $response->getAttribute('route');
    $args  = $route->getArguments();

    $nombreUsuario = $args['nombreUsuario'];

    $sql = "SELECT  transportador.*
from transportador
left outer join plantas on (transportador.codplanta=plantas.codplanta)
left outer join agentes on (plantas.agenteplanta=agentes.codagente)
where agentes.codagente='$nombreUsuario'";

    try {

        $stmt    = getConnection()->query($sql);
        $usuario = array();
        $usuario = $stmt->fetchAll(PDO::FETCH_OBJ);
        // $arreglo        = (array) $usuario;
        // $retorno        = array_map('utf8', $arreglo);
        $arrayCrudo     = json_encode($usuario);
        $JsonSinSlash   = str_replace("\/", "/", $arrayCrudo);
        $ArrayRespuesta = json_decode($JsonSinSlash, true);
        return $JsonSinSlash;
        $db = null;
    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}
function getTransportadoresAdmin($response)
{
    $route = $response->getAttribute('route');
    $args  = $route->getArguments();

    

    $sql = "SELECT  transportador.*
from transportador
left outer join plantas on (transportador.codplanta=plantas.codplanta)
left outer join agentes on (plantas.agenteplanta=agentes.codagente)";

    try {

        $stmt    = getConnection()->query($sql);
        $usuario = array();
        $usuario = $stmt->fetchAll(PDO::FETCH_OBJ);
        // $arreglo        = (array) $usuario;
        // $retorno        = array_map('utf8', $arreglo);
        $arrayCrudo     = json_encode($usuario);
        $JsonSinSlash   = str_replace("\/", "/", $arrayCrudo);
        $ArrayRespuesta = json_decode($JsonSinSlash, true);
        return $JsonSinSlash;
        $db = null;
    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function cliente($response)
{
    $route = $response->getAttribute('route');
    $args  = $route->getArguments();


    $sql = "SELECT  clientes.*
from clientes
left outer join plantas on (plantas.nitplanta=clientes.nit)
left outer join agentes on (plantas.agenteplanta=agentes.codagente)
where agentes.codagente='lepe'";

    try {

        $stmt    = getConnection()->query($sql);
        $usuario = array();
        $usuario = $stmt->fetchAll(PDO::FETCH_OBJ);
        // $arreglo        = (array) $usuario;
        // $retorno        = array_map('utf8', $arreglo);
        $arrayCrudo     = json_encode($usuario);
        $JsonSinSlash   = str_replace("\/", "/", $arrayCrudo);
        $ArrayRespuesta = json_decode($JsonSinSlash, true);
        return $JsonSinSlash;
        $db = null;
    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function crearTransportador($request)
{
    $emp = $request->getParams();
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "INSERT INTO transportador VALUES (:tipoTransportador, :nombreTransportador, :codplanta, :descripcionTransportador)";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':tipoTransportador', $emp["tipoTransportador"]);
        $stmt->bindParam(":nombreTransportador", $emp["nombreTransportador"]);
        $stmt->bindParam(":codplanta", $emp["idPlanta"]);
        $stmt->bindParam(":descripcionTransportador", $emp["descripcionTransportador"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function buscarRegistroTransportadorVertical($response)
{
    $route           = $response->getAttribute('route');
    $args            = $route->getArguments();
    $idTransportador = $args['idTransportador'];

    $sql = "SELECT idRegistro as max
      from registro
      where registro.idTransportador=$idTransportador";

    try {

        $stmt    = getConnection()->query($sql);
        $usuario = array();
        $usuario = $stmt->fetchAll(PDO::FETCH_OBJ);
        // $arreglo        = (array) $usuario;
        // $retorno        = array_map('utf8', $arreglo);
        // $arrayCrudo     = json_encode($usuario);
        // $JsonSinSlash   = str_replace("\/", "/", $arrayCrudo);
        // $ArrayRespuesta = json_decode($JsonSinSlash, true);

        return json_encode($usuario);
        $db = null;
    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}


function maxTransportador($response)
{
    $route           = $response->getAttribute('route');
    $args            = $route->getArguments();

    $sql = "SELECT max(idTransportador) as max
      from transportador";

    try {

        $stmt    = getConnection()->query($sql);
        $usuario = array();
        $usuario = $stmt->fetchAll(PDO::FETCH_OBJ);
        // $arreglo        = (array) $usuario;
        // $retorno        = array_map('utf8', $arreglo);
        // $arrayCrudo     = json_encode($usuario);
        // $JsonSinSlash   = str_replace("\/", "/", $arrayCrudo);
        // $ArrayRespuesta = json_decode($JsonSinSlash, true);

        return json_encode($usuario);
        $db = null;
    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function verificarTransportador($response)
{
    $route           = $response->getAttribute('route');
    $args            = $route->getArguments();
    $idTransportador = $args['nombreUsuario'];
    

    $sql = "SELECT max(transportador.idTransportador) as max from transportador join plantas on(transportador.codplanta=plantas.codplanta) where plantas.agenteplanta='$idTransportador'";
    

    try {

        $stmt    = getConnection()->query($sql);
        $usuario = array();
        $usuario = $stmt->fetchAll(PDO::FETCH_OBJ);
        // $arreglo        = (array) $usuario;
        // $retorno        = array_map('utf8', $arreglo);
        // $arrayCrudo     = json_encode($usuario);
        // $JsonSinSlash   = str_replace("\/", "/", $arrayCrudo);
        // $ArrayRespuesta = json_decode($JsonSinSlash, true);

        return json_encode($usuario);
        $db = null;
    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }

}
/*=========================================================================================================================
=                                                     FIN DE TRANSPORTADORES                                               =
============================================================================================================================*/

/*--------------------------------------------------------------------------------------------------------------------------*/


/*==========================================================================================================================
=                                                             REGISTRO                                                     =
===========================================================================================================================*/
function crearRegistro($request)
{
    $emp = $request->getParams();

    $sql = "INSERT INTO registro VALUES (:fechaRegistro, :idTransportador, :idPlanta, :usuarioRegistro, 1)";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':fechaRegistro', $emp["fechaRegistro"]);
        $stmt->bindParam(":idPlanta", $emp["idPlanta"]);
        $stmt->bindParam(":idTransportador", $emp["idTransportador"]);
        $stmt->bindParam(":usuarioRegistro", $emp["usuarioRegistro"]);
        
        if($stmt->execute())
        {
            return 'ok';
        }

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function getMaxRegistro($response)
{
    $route = $response->getAttribute('route');
    $args  = $route->getArguments();

    $sql = "SELECT max(idRegistro) as max from registro";

    try {

        $stmt    = getConnection()->query($sql);
        $usuario = array();
        $usuario = $stmt->fetchAll(PDO::FETCH_OBJ);
        // $arreglo        = (array) $usuario;
        // $retorno        = array_map('utf8', $arreglo);
        $arrayCrudo     = json_encode($usuario);
        $JsonSinSlash   = str_replace("\/", "/", $arrayCrudo);
        $ArrayRespuesta = json_decode($JsonSinSlash, true);
        return $JsonSinSlash;
        $db = null;
    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarRegistro($request)
{
    $emp = $request->getParams();

    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "UPDATE registro set fechaRegistro=:fechaRegistro, usuarioRegistro=:usuarioRegistro
  where idRegistro=:idRegistro";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':idRegistro', $emp["idRegistro"]);
        $stmt->bindParam(":fechaRegistro", $emp["fechaRegistro"]);
        $stmt->bindParam(":usuarioRegistro", $emp["usuarioRegistro"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarEstadoRegistro($request)
{
    $emp = $request->getParams();

    $sql = "UPDATE registro set estadoRegistro=:estadoRegistro
  where idRegistro=:idRegistro";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':idRegistro', $emp["idRegistro"]);
        $stmt->bindParam(":estadoRegistro", $emp["estadoRegistro"]);
        

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}


/*===========================================================================================================================
=                                                           FIN DE REGISTRO                                                 =
============================================================================================================================*/

/*--------------------------------------------------------------------------------------------------------------------------*/


/*===========================================================================================================================
=                                                         CLIENTES                                                           =
============================================================================================================================*/

function validarNit($response)
{
    $route = $response->getAttribute('route');
    $args  = $route->getArguments();
    $nit   = $args["nit"];

    $sql = "SELECT * from clientes where nit= '$nit'";

    try {

        $stmt    = getConnection()->query($sql);
        $usuario = array();
        $usuario = $stmt->fetchAll(PDO::FETCH_OBJ);
        // $arreglo        = (array) $usuario;
        // $retorno        = array_map('utf8', $arreglo);
        $arrayCrudo     = json_encode($usuario);
        $JsonSinSlash   = str_replace("\/", "/", $arrayCrudo);
        $ArrayRespuesta = json_decode($JsonSinSlash, true);
        return $JsonSinSlash;
        $db = null;
    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function crearCliente($request)
{
    $emp = $request->getParams();

    $sql = "INSERT INTO clientes (nit, nameunido) VALUES (:nit, :nameunido)";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":nit", $emp["nitCliente"]);
        $stmt->bindParam(":nameunido", $emp["nombreCliente"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}



function sincroClientes($request)
{
    $emp = $request->getParams();

    $sql = "INSERT INTO clientes (nit, nameunido) VALUES (:nit, :nameunido)";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":nit", $emp["nitCliente"]);
        $stmt->bindParam(":nameunido", $emp["nombreCliente"]);

        if($stmt->execute())
        {
            return "ok";
        }

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

/*==========================================================================================================================
=                                                  FIN DE CLIENTES                                                         =
============================================================================================================================*/

/*------------------------------------------------------------------------------------------------------------------------*/

/*===========================================================================================================================
=                                                           PLANTAS                                                       =
===========================================================================================================================*/


function crearPlanta($request)
{
    $emp = $request->getParams();

    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "INSERT INTO plantas (nameplanta, agenteplanta, nitplanta, dirmciapl, ciudmciapl) VALUES (:nameplanta, :agenteplanta, :nit, :direccionPlanta, :ciudad)";

    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':nit', $emp["nitCliente"]);
        $stmt->bindParam(":nameplanta", $emp["NombrePlanta"]);
        $stmt->bindParam(":agenteplanta", $emp["idUsuario"]);
        $stmt->bindParam(":direccionPlanta", $emp["direccionPlanta"]);
        $stmt->bindParam(":ciudad", $emp["ciudad"]);

        if($stmt->execute())
        {
            return "ok";
        }

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function getCiudades($response)
{
    $route = $response->getAttribute('route');
    $args  = $route->getArguments();

    $sql = "SELECT codpoblado, unido from ciudades";

    try {

        $stmt    = getConnection()->query($sql);
        $usuario = array();
        $usuario = $stmt->fetchAll(PDO::FETCH_OBJ);
        // $arreglo        = (array) $usuario;
        // $retorno        = array_map('utf8', $arreglo);
        $arrayCrudo     = json_encode($usuario);
        $JsonSinSlash   = str_replace("\/", "/", $arrayCrudo);
        $ArrayRespuesta = json_decode($JsonSinSlash, true);
        return $JsonSinSlash;
        $db = null;
    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}


function maxPlanta($response)
{
    $route = $response->getAttribute('route');
    $args  = $route->getArguments();

    // $nombreUsuario = $args['nombreUsuario'];

    $sql = "SELECT  max(codplanta) as max from plantas";

    try {

        $stmt    = getConnection()->query($sql);
        $usuario = array();
        $usuario = $stmt->fetchAll(PDO::FETCH_OBJ);
        // $arreglo        = (array) $usuario;
        // $retorno        = array_map('utf8', $arreglo);
        $arrayCrudo     = json_encode($usuario);
        $JsonSinSlash   = str_replace("\/", "/", $arrayCrudo);
        $ArrayRespuesta = json_decode($JsonSinSlash, true);
        return $JsonSinSlash;
        $db = null;
    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

/*==========================================================================================================================
=                                                    FIN DE PLANTAS                                                       =
============================================================================================================================*/

/*-------------------------------------------------------------------------------------------------------------------------*/


/*========================================================================================================================
=                                                       BANDA ELEVADORA                                                      =
===========================================================================================================================*/


function registroSincronizacion($request)
{
    $emp = $request->getParams();

    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "INSERT INTO bandaElevadora (idRegistro, marcaBandaElevadora, anchoBandaElevadora, distanciaEntrePoleasElevadora, noLonaBandaElevadora, tipoLonaBandaElevadora, espesorTotalBandaElevadora, espesorCojinActualElevadora, 
                         espesorCubiertaSuperiorElevadora, espesorCubiertaInferiorElevadora, tipoCubiertaElevadora, tipoEmpalmeElevadora, estadoEmpalmeElevadora, resistenciaRoturaLonaElevadora, velocidadBandaElevadora, 
                         marcaBandaElevadoraAnterior, anchoBandaElevadoraAnterior, noLonasBandaElevadoraAnterior, tipoLonaBandaElevadoraAnterior, espesorTotalBandaElevadoraAnterior, espesorCubiertaSuperiorBandaElevadoraAnterior, 
                         espesorCojinElevadoraAnterior, espesorCubiertaInferiorBandaElevadoraAnterior, tipoCubiertaElevadoraAnterior, tipoEmpalmeElevadoraAnterior, resistenciaRoturaBandaElevadoraAnterior, 
                         tonsTransportadasBandaElevadoraAnterior, velocidadBandaElevadoraAnterior, causaFallaCambioBandaElevadoraAnterior, pesoMaterialEnCadaCangilon, pesoCangilonVacio, longitudCangilon, materialCangilon, tipoCangilon, 
                         proyeccionCangilon, profundidadCangilon, marcaCangilon, referenciaCangilon, capacidadCangilon, noFilasCangilones, separacionCangilones, noAgujeros, distanciaBordeBandaEstructura, distanciaPosteriorBandaEstructura, 
                         distanciaLaboFrontalCangilonEstructura, distanciaBordesCangilonEstructura, tipoVentilacion, diametroPoleaMotrizElevadora, anchoPoleaMotrizElevadora, tipoPoleaMotrizElevadora, largoEjeMotrizElevadora, 
                         diametroEjeMotrizElevadora, bandaCentradaEnPoleaMotrizElevadora, estadoRevestimientoPoleaMotrizElevadora, potenciaMotorMotrizElevadora, rpmSalidaReductorMotrizElevadora, guardaReductorPoleaMotrizElevadora, 
                         diametroPoleaColaElevadora, anchoPoleaColaElevadora, tipoPoleaColaElevadora, largoEjePoleaColaElevadora, diametroEjePoleaColaElevadora, bandaCentradaEnPoleaColaElevadora, 
                         estadoRevestimientoPoleaColaElevadora, longitudTensorTornilloPoleaColaElevadora, longitudRecorridoContrapesaPoleaColaElevadora, cargaTrabajoBandaElevadora, temperaturaMaterialElevadora, 
                         empalmeMecanicoElevadora, diametroRoscaElevadora, largoTornilloElevadora, materialTornilloElevadora, anchoCabezaElevadorPuertaInspeccion, largoCabezaElevadorPuertaInspeccion, anchoBotaElevadorPuertaInspeccion, 
                         largoBotaElevadorPuertaInspeccion, monitorPeligro, rodamiento, monitorDesalineacion, monitorVelocidad, sensorInductivo, indicadorNivel, cajaUnion, alarmaYPantalla, interruptorSeguridad, materialElevadora, 
                         ataqueQuimicoElevadora, ataqueTemperaturaElevadora, ataqueAceitesElevadora, ataqueAbrasivoElevadora, capacidadElevadora, horasTrabajoDiaElevadora, diasTrabajoSemanaElevadora, abrasividadElevadora, 
                         porcentajeFinosElevadora, maxGranulometriaElevadora, densidadMaterialElevadora, tempMaxMaterialSobreBandaElevadora, tempPromedioMaterialSobreBandaElevadora, variosPuntosDeAlimentacion, lluviaDeMaterial, 
                         anchoPiernaElevador, profundidadPiernaElevador, tempAmbienteMin, tempAmbienteMax, tipoDescarga, tipoCarga, observacionRegistroElevadora

  VALUES (:idRegistro, :marcaBandaElevadora, :anchoBandaElevadora, :distanciaEntrePoleasElevadora, :noLonaBandaElevadora, :tipoLonaBandaElevadora, :espesorTotalBandaElevadora, :espesorCojinActualElevadora, 
                         :espesorCubiertaSuperiorElevadora, :espesorCubiertaInferiorElevadora, :tipoCubiertaElevadora, :tipoEmpalmeElevadora, :estadoEmpalmeElevadora, :resistenciaRoturaLonaElevadora, :velocidadBandaElevadora, 
                         :marcaBandaElevadoraAnterior, :anchoBandaElevadoraAnterior, :noLonasBandaElevadoraAnterior, :tipoLonaBandaElevadoraAnterior, :espesorTotalBandaElevadoraAnterior, :espesorCubiertaSuperiorBandaElevadoraAnterior, 
                         :espesorCojinElevadoraAnterior, :espesorCubiertaInferiorBandaElevadoraAnterior, :tipoCubiertaElevadoraAnterior, :tipoEmpalmeElevadoraAnterior, :resistenciaRoturaBandaElevadoraAnterior, 
                         :tonsTransportadasBandaElevadoraAnterior, :velocidadBandaElevadoraAnterior, :causaFallaCambioBandaElevadoraAnterior, :pesoMaterialEnCadaCangilon, :pesoCangilonVacio, :longitudCangilon, :materialCangilon, :tipoCangilon, 
                         :proyeccionCangilon, :profundidadCangilon, :marcaCangilon, :referenciaCangilon, :capacidadCangilon, :noFilasCangilones, :separacionCangilones, :noAgujeros, :distanciaBordeBandaEstructura, :distanciaPosteriorBandaEstructura, 
                         :distanciaLaboFrontalCangilonEstructura, :distanciaBordesCangilonEstructura, :tipoVentilacion, :diametroPoleaMotrizElevadora, :anchoPoleaMotrizElevadora, :tipoPoleaMotrizElevadora, :largoEjeMotrizElevadora, 
                         :diametroEjeMotrizElevadora, :bandaCentradaEnPoleaMotrizElevadora, :estadoRevestimientoPoleaMotrizElevadora, :potenciaMotorMotrizElevadora, :rpmSalidaReductorMotrizElevadora, :guardaReductorPoleaMotrizElevadora, 
                         :diametroPoleaColaElevadora, :anchoPoleaColaElevadora, :tipoPoleaColaElevadora, :largoEjePoleaColaElevadora, :diametroEjePoleaColaElevadora, :bandaCentradaEnPoleaColaElevadora, 
                         :estadoRevestimientoPoleaColaElevadora, :longitudTensorTornilloPoleaColaElevadora, :longitudRecorridoContrapesaPoleaColaElevadora, :cargaTrabajoBandaElevadora, :temperaturaMaterialElevadora, 
                         :empalmeMecanicoElevadora, :diametroRoscaElevadora, :largoTornilloElevadora, :materialTornilloElevadora, :anchoCabezaElevadorPuertaInspeccion, :largoCabezaElevadorPuertaInspeccion, :anchoBotaElevadorPuertaInspeccion, 
                         :largoBotaElevadorPuertaInspeccion, :monitorPeligro, :rodamiento, :monitorDesalineacion, :monitorVelocidad, :sensorInductivo, :indicadorNivel, :cajaUnion, :alarmaYPantalla, :interruptorSeguridad, :materialElevadora, 
                         :ataqueQuimicoElevadora, :ataqueTemperaturaElevadora, :ataqueAceitesElevadora, :ataqueAbrasivoElevadora, :capacidadElevadora, :horasTrabajoDiaElevadora, :diasTrabajoSemanaElevadora, :abrasividadElevadora, 
                         :porcentajeFinosElevadora, :maxGranulometriaElevadora, :densidadMaterialElevadora, :tempMaxMaterialSobreBandaElevadora, :tempPromedioMaterialSobreBandaElevadora, :variosPuntosDeAlimentacion, :lluviaDeMaterial, 
                         :anchoPiernaElevador, :profundidadPiernaElevador, :tempAmbienteMin, :tempAmbienteMax, :tipoDescarga, :tipoCarga, :observacionRegistroElevadora";

    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':idRegistro', $emp["idRegistro"]);
        $stmt->bindParam(":diametroRoscaElevadora", $emp["diametroRoscaElevadora"]);
        $stmt->bindParam(":largoTornilloElevadora", $emp["largoTornilloElevadora"]);
        $stmt->bindParam(":materialTornilloElevadora", $emp["materialTornilloElevadora"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}




/*------------------------------------------------------------ TORNILLOS --------------------------------------------------*/
function registroTornillosElevadora($request)
{
    $emp = $request->getParams();

    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "INSERT INTO bandaElevadora (idRegistro, diametroRoscaElevadora, largoTornilloElevadora, materialTornilloElevadora)
  VALUES (:idRegistro, :diametroRoscaElevadora, :largoTornilloElevadora, :materialTornilloElevadora)";

    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':idRegistro', $emp["idRegistro"]);
        $stmt->bindParam(":diametroRoscaElevadora", $emp["diametroRoscaElevadora"]);
        $stmt->bindParam(":largoTornilloElevadora", $emp["largoTornilloElevadora"]);
        $stmt->bindParam(":materialTornilloElevadora", $emp["materialTornilloElevadora"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarTornillosElevadora($request)
{
    $emp = $request->getParams();

    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "UPDATE bandaElevadora set diametroRoscaElevadora=:diametroRoscaElevadora, largoTornilloElevadora=:largoTornilloElevadora, materialTornilloElevadora=:materialTornilloElevadora
  where idRegistro=:idRegistro";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':idRegistro', $emp["idRegistro"]);
        $stmt->bindParam(":diametroRoscaElevadora", $emp["diametroRoscaElevadora"]);
        $stmt->bindParam(":largoTornilloElevadora", $emp["largoTornilloElevadora"]);
        $stmt->bindParam(":materialTornilloElevadora", $emp["materialTornilloElevadora"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

/*------------------------------------------------------------ FIN DE TORNILLOS ----------------------------------------*/
/*============================================================================================================================  
============================================================================================================================*/



function observacionElevadora($request)
{
    $emp = $request->getParams();

    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "INSERT INTO bandaElevadora (idRegistro, observacionRegistroElevadora) values (:idRegistro,:observacionRegistroElevadora)";

    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':idRegistro', $emp["idRegistro"]);
        $stmt->bindParam(":observacionRegistroElevadora", $emp["observacionRegistroElevadora"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarObservacionElevadora($request)
{
    $emp = $request->getParams();

    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "UPDATE bandaElevadora set observacionRegistroElevadora=:observacionRegistroElevadora where idRegistro=:idRegistro";

    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':idRegistro', $emp["idRegistro"]);
        $stmt->bindParam(":observacionRegistroElevadora", $emp["observacionRegistroElevadora"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}


/*--------------------------------------------------------------- EMPALME -------------------------------------------------*/

function registroEmpalmeElevadora($request)
{
    $emp = $request->getParams();

    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "INSERT INTO bandaElevadora (idRegistro, cargaTrabajoBandaElevadora, temperaturaMaterialElevadora, empalmeMecanicoElevadora)
  VALUES (:idRegistro, :cargaTrabajoBandaElevadora, :temperaturaMaterialElevadora, :empalmeMecanicoElevadora)";

    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':idRegistro', $emp["idRegistro"]);
        $stmt->bindParam(":cargaTrabajoBandaElevadora", $emp["cargaTrabajoBandaElevadora"]);
        $stmt->bindParam(":temperaturaMaterialElevadora", $emp["temperaturaMaterialElevadora"]);
        $stmt->bindParam(":empalmeMecanicoElevadora", $emp["empalmeMecanicoElevadora"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarEmpalmeElevadora($request)
{
    $emp = $request->getParams();

    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "UPDATE bandaElevadora set cargaTrabajoBandaElevadora=:cargaTrabajoBandaElevadora, temperaturaMaterialElevadora=:temperaturaMaterialElevadora, empalmeMecanicoElevadora=:empalmeMecanicoElevadora
  where idRegistro=:idRegistro";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':idRegistro', $emp["idRegistro"]);
        $stmt->bindParam(":cargaTrabajoBandaElevadora", $emp["cargaTrabajoBandaElevadora"]);
        $stmt->bindParam(":temperaturaMaterialElevadora", $emp["temperaturaMaterialElevadora"]);
        $stmt->bindParam(":empalmeMecanicoElevadora", $emp["empalmeMecanicoElevadora"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}


/*--------------------------------------------------------- FIN DE EMPALME ----------------------------------------------*/

/*===========================================================================================================================
===========================================================================================================================*/

/*--------------------------------------------------------- PUERTA INSPECCIÓN ------------------------------------------*/

function registroPuertaInspeccion($request)
{
    $emp = $request->getParams();

    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "INSERT INTO bandaElevadora (idRegistro, anchoCabezaElevadorPuertaInspeccion, largoCabezaElevadorPuertaInspeccion, anchoBotaElevadorPuertaInspeccion, largoBotaElevadorPuertaInspeccion)

  VALUES (:idRegistro, :anchoCabezaElevadorPuertaInspeccion, :largoCabezaElevadorPuertaInspeccion, :anchoBotaElevadorPuertaInspeccion, .largoBotaElevadorPuertaInspeccion)";

    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':idRegistro', $emp["idRegistro"]);
        $stmt->bindParam(":anchoCabezaElevadorPuertaInspeccion", $emp["anchoCabezaElevadorPuertaInspeccion"]);
        $stmt->bindParam(":largoCabezaElevadorPuertaInspeccion", $emp["largoCabezaElevadorPuertaInspeccion"]);
        $stmt->bindParam(":anchoBotaElevadorPuertaInspeccion", $emp["anchoBotaElevadorPuertaInspeccion"]);
        $stmt->bindParam(":largoBotaElevadorPuertaInspeccion", $emp["largoBotaElevadorPuertaInspeccion"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarPuertaInspeccion($request)
{
    $emp = $request->getParams();

    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "UPDATE bandaElevadora set anchoCabezaElevadorPuertaInspeccion=:anchoCabezaElevadorPuertaInspeccion, largoCabezaElevadorPuertaInspeccion=:largoCabezaElevadorPuertaInspeccion, anchoBotaElevadorPuertaInspeccion=:anchoBotaElevadorPuertaInspeccion, largoBotaElevadorPuertaInspeccion=:largoBotaElevadorPuertaInspeccion
  where idRegistro=:idRegistro";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':idRegistro', $emp["idRegistro"]);
        $stmt->bindParam(":anchoCabezaElevadorPuertaInspeccion", $emp["anchoCabezaElevadorPuertaInspeccion"]);
        $stmt->bindParam(":largoCabezaElevadorPuertaInspeccion", $emp["largoCabezaElevadorPuertaInspeccion"]);
        $stmt->bindParam(":anchoBotaElevadorPuertaInspeccion", $emp["anchoBotaElevadorPuertaInspeccion"]);
        $stmt->bindParam(":largoBotaElevadorPuertaInspeccion", $emp["largoBotaElevadorPuertaInspeccion"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

/*----------------------------------------------------- FIN DE PUERTA INSPECCIÓN ----------------------------------------*/

/*============================================================================================================================
===========================================================================================================================*/


/*------------------------------------------------ SEGURIDAD -----------------------------------------------------------*/


function registroSeguridadElevadora($request)
{
    $emp = $request->getParams();

    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "INSERT INTO bandaElevadora (idRegistro, monitorPeligro, rodamiento, monitorDesalineacion, monitorVelocidad, sensorInductivo, indicadorNivel, cajaUnion,alarmaYPantalla,interruptorSeguridad)

  VALUES (:idRegistro, :monitorPeligro, :rodamiento, :monitorDesalineacion, .monitorVelocidad,:sensorInductivo, :indicadorNivel, :cajaUnion,:alarmaYPantalla,:interruptorSeguridad)";

    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':idRegistro', $emp["idRegistro"]);
        $stmt->bindParam(":monitorPeligro", $emp["monitorPeligro"]);
        $stmt->bindParam(":rodamiento", $emp["rodamiento"]);
        $stmt->bindParam(":monitorDesalineacion", $emp["monitorDesalineacion"]);
        $stmt->bindParam(":monitorVelocidad", $emp["monitorVelocidad"]);
        $stmt->bindParam(":sensorInductivo", $emp["sensorInductivo"]);
        $stmt->bindParam(":indicadorNivel", $emp["indicadorNivel"]);
        $stmt->bindParam(":cajaUnion", $emp["cajaUnion"]);
        $stmt->bindParam(":alarmaYPantalla", $emp["alarmaYPantalla"]);
        $stmt->bindParam(":interruptorSeguridad", $emp["interruptorSeguridad"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarSeguridadElevadora($request)
{
    $emp = $request->getParams();

    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "UPDATE bandaElevadora set monitorPeligro=:monitorPeligro, rodamiento=:rodamiento, monitorDesalineacion=:monitorDesalineacion, monitorVelocidad=:monitorVelocidad, sensorInductivo=:sensorInductivo, indicadorNivel=:indicadorNivel, cajaUnion=:cajaUnion,alarmaYPantalla=:alarmaYPantalla,interruptorSeguridad=:interruptorSeguridad

  where idRegistro=:idRegistro";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':idRegistro', $emp["idRegistro"]);
        $stmt->bindParam(":monitorPeligro", $emp["monitorPeligro"]);
        $stmt->bindParam(":rodamiento", $emp["rodamiento"]);
        $stmt->bindParam(":monitorDesalineacion", $emp["monitorDesalineacion"]);
        $stmt->bindParam(":monitorVelocidad", $emp["monitorVelocidad"]);
        $stmt->bindParam(":sensorInductivo", $emp["sensorInductivo"]);
        $stmt->bindParam(":indicadorNivel", $emp["indicadorNivel"]);
        $stmt->bindParam(":cajaUnion", $emp["cajaUnion"]);
        $stmt->bindParam(":alarmaYPantalla", $emp["alarmaYPantalla"]);
        $stmt->bindParam(":interruptorSeguridad", $emp["interruptorSeguridad"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

/*------------------------------------------------ FIN DE SEGURIDAD -------------------------------------------------------*/

/*============================================================================================================================
============================================================================================================================*/


/*---------------------------------------------------------POLEA MOTRIZ --------------------------------------------------*/

function registroPoleaMotrizElevadora($request)
{
    $emp = $request->getParams();

    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "INSERT INTO bandaElevadora (idRegistro, diametroPoleaMotrizElevadora, anchoPoleaMotrizElevadora, tipoPoleaMotrizElevadora, largoEjeMotrizElevadora, diametroEjeMotrizElevadora, bandaCentradaEnPoleaMotrizElevadora, estadoRevestimientoPoleaMotrizElevadora,potenciaMotorMotrizElevadora,rpmSalidaReductorMotrizElevadora,guardaReductorPoleaMotrizElevadora)

  VALUES (:idRegistro, :diametroPoleaMotrizElevadora, :anchoPoleaMotrizElevadora, :tipoPoleaMotrizElevadora, .largoEjeMotrizElevadora,:diametroEjeMotrizElevadora, :bandaCentradaEnPoleaMotrizElevadora, :estadoRevestimientoPoleaMotrizElevadora,:potenciaMotorMotrizElevadora,:rpmSalidaReductorMotrizElevadora,:guardaReductorPoleaMotrizElevadora)";

    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':idRegistro', $emp["idRegistro"]);
        $stmt->bindParam(":diametroPoleaMotrizElevadora", $emp["diametroPoleaMotrizElevadora"]);
        $stmt->bindParam(":anchoPoleaMotrizElevadora", $emp["anchoPoleaMotrizElevadora"]);
        $stmt->bindParam(":tipoPoleaMotrizElevadora", $emp["tipoPoleaMotrizElevadora"]);
        $stmt->bindParam(":largoEjeMotrizElevadora", $emp["largoEjeMotrizElevadora"]);
        $stmt->bindParam(":diametroEjeMotrizElevadora", $emp["diametroEjeMotrizElevadora"]);
        $stmt->bindParam(":bandaCentradaEnPoleaMotrizElevadora", $emp["bandaCentradaEnPoleaMotrizElevadora"]);
        $stmt->bindParam(":estadoRevestimientoPoleaMotrizElevadora", $emp["estadoRevestimientoPoleaMotrizElevadora"]);
        $stmt->bindParam(":potenciaMotorMotrizElevadora", $emp["potenciaMotorMotrizElevadora"]);
        $stmt->bindParam(":rpmSalidaReductorMotrizElevadora", $emp["rpmSalidaReductorMotrizElevadora"]);
        $stmt->bindParam(":guardaReductorPoleaMotrizElevadora", $emp["guardaReductorPoleaMotrizElevadora"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarPoleaMotrizElevadora($request)
{
    $emp = $request->getParams();

    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "UPDATE bandaElevadora set  diametroPoleaMotrizElevadora=:diametroPoleaMotrizElevadora, anchoPoleaMotrizElevadora=:anchoPoleaMotrizElevadora, tipoPoleaMotrizElevadora=:tipoPoleaMotrizElevadora, largoEjeMotrizElevadora=:largoEjeMotrizElevadora, diametroEjeMotrizElevadora=:diametroEjeMotrizElevadora, bandaCentradaEnPoleaMotrizElevadora=:bandaCentradaEnPoleaMotrizElevadora, estadoRevestimientoPoleaMotrizElevadora=:estadoRevestimientoPoleaMotrizElevadora,potenciaMotorMotrizElevadora=:potenciaMotorMotrizElevadora,rpmSalidaReductorMotrizElevadora=:rpmSalidaReductorMotrizElevadora,guardaReductorPoleaMotrizElevadora=:guardaReductorPoleaMotrizElevadora

  where idRegistro=:idRegistro";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':idRegistro', $emp["idRegistro"]);
        $stmt->bindParam(":diametroPoleaMotrizElevadora", $emp["diametroPoleaMotrizElevadora"]);
        $stmt->bindParam(":anchoPoleaMotrizElevadora", $emp["anchoPoleaMotrizElevadora"]);
        $stmt->bindParam(":tipoPoleaMotrizElevadora", $emp["tipoPoleaMotrizElevadora"]);
        $stmt->bindParam(":largoEjeMotrizElevadora", $emp["largoEjeMotrizElevadora"]);
        $stmt->bindParam(":diametroEjeMotrizElevadora", $emp["diametroEjeMotrizElevadora"]);
        $stmt->bindParam(":bandaCentradaEnPoleaMotrizElevadora", $emp["bandaCentradaEnPoleaMotrizElevadora"]);
        $stmt->bindParam(":estadoRevestimientoPoleaMotrizElevadora", $emp["estadoRevestimientoPoleaMotrizElevadora"]);
        $stmt->bindParam(":potenciaMotorMotrizElevadora", $emp["potenciaMotorMotrizElevadora"]);
        $stmt->bindParam(":rpmSalidaReductorMotrizElevadora", $emp["rpmSalidaReductorMotrizElevadora"]);
        $stmt->bindParam(":guardaReductorPoleaMotrizElevadora", $emp["guardaReductorPoleaMotrizElevadora"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

/*------------------------------------------------ FIN DE POLEA MOTRIZ ----------------------------------------------------*/
/*============================================================================================================================
============================================================================================================================*/


/*--------------------------------------------------------- POLEA DE COLA ---------------------------------------------*/

function registroPoleaColaElevadora($request)
{
    $emp = $request->getParams();

    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "INSERT INTO bandaElevadora (idRegistro, diametroPoleaColaElevadora, anchoPoleaColaElevadora, tipoPoleaColaElevadora, largoEjePoleaColaElevadora, diametroEjePoleaColaElevadora, bandaCentradaEnPoleaColaElevadora, estadoRevestimientoPoleaColaElevadora,longitudTensorTornilloPoleaColaElevadora,longitudRecorridoContrapesaPoleaColaElevadora)

  VALUES (:idRegistro, :diametroPoleaColaElevadora, :anchoPoleaColaElevadora, :tipoPoleaColaElevadora, :largoEjePoleaColaElevadora,:diametroEjePoleaColaElevadora, :bandaCentradaEnPoleaColaElevadora, :estadoRevestimientoPoleaColaElevadora,:longitudTensorTornilloPoleaColaElevadora,:longitudRecorridoContrapesaPoleaColaElevadora)";

    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':idRegistro', $emp["idRegistro"]);
        $stmt->bindParam(":diametroPoleaColaElevadora", $emp["diametroPoleaColaElevadora"]);
        $stmt->bindParam(":anchoPoleaColaElevadora", $emp["anchoPoleaColaElevadora"]);
        $stmt->bindParam(":tipoPoleaColaElevadora", $emp["tipoPoleaColaElevadora"]);
        $stmt->bindParam(":largoEjePoleaColaElevadora", $emp["largoEjePoleaColaElevadora"]);
        $stmt->bindParam(":diametroEjePoleaColaElevadora", $emp["diametroEjePoleaColaElevadora"]);
        $stmt->bindParam(":bandaCentradaEnPoleaColaElevadora", $emp["bandaCentradaEnPoleaColaElevadora"]);
        $stmt->bindParam(":estadoRevestimientoPoleaColaElevadora", $emp["estadoRevestimientoPoleaColaElevadora"]);
        $stmt->bindParam(":longitudTensorTornilloPoleaColaElevadora", $emp["longitudTensorTornilloPoleaColaElevadora"]);
        $stmt->bindParam(":longitudRecorridoContrapesaPoleaColaElevadora", $emp["longitudRecorridoContrapesaPoleaColaElevadora"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarPoleaColaElevadora($request)
{
    $emp = $request->getParams();

    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "UPDATE bandaElevadora set  diametroPoleaColaElevadora=:diametroPoleaColaElevadora, anchoPoleaColaElevadora=:anchoPoleaColaElevadora, tipoPoleaColaElevadora=:tipoPoleaColaElevadora, largoEjePoleaColaElevadora=:largoEjePoleaColaElevadora, diametroEjePoleaColaElevadora=:diametroEjePoleaColaElevadora, bandaCentradaEnPoleaColaElevadora=:bandaCentradaEnPoleaColaElevadora, estadoRevestimientoPoleaColaElevadora=:estadoRevestimientoPoleaColaElevadora,longitudTensorTornilloPoleaColaElevadora=:longitudTensorTornilloPoleaColaElevadora,longitudRecorridoContrapesaPoleaColaElevadora=:longitudRecorridoContrapesaPoleaColaElevadora

  where idRegistro=:idRegistro";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':idRegistro', $emp["idRegistro"]);
        $stmt->bindParam(":diametroPoleaColaElevadora", $emp["diametroPoleaColaElevadora"]);
        $stmt->bindParam(":anchoPoleaColaElevadora", $emp["anchoPoleaColaElevadora"]);
        $stmt->bindParam(":tipoPoleaColaElevadora", $emp["tipoPoleaColaElevadora"]);
        $stmt->bindParam(":largoEjePoleaColaElevadora", $emp["largoEjePoleaColaElevadora"]);
        $stmt->bindParam(":diametroEjePoleaColaElevadora", $emp["diametroEjePoleaColaElevadora"]);
        $stmt->bindParam(":bandaCentradaEnPoleaColaElevadora", $emp["bandaCentradaEnPoleaColaElevadora"]);
        $stmt->bindParam(":estadoRevestimientoPoleaColaElevadora", $emp["estadoRevestimientoPoleaColaElevadora"]);
        $stmt->bindParam(":longitudTensorTornilloPoleaColaElevadora", $emp["longitudTensorTornilloPoleaColaElevadora"]);
        $stmt->bindParam(":longitudRecorridoContrapesaPoleaColaElevadora", $emp["longitudRecorridoContrapesaPoleaColaElevadora"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

/*-------------------------------------------------- FIN DE POLEA COLA ----------------------------------------------------*/
/*============================================================================================================== /


/*-------------------------------------------------------- CANGILÓN ------------------------------------------------------*/

function registroCangilon($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = "INSERT INTO bandaElevadora (idRegistro, longitudCangilon, proyeccionCangilon, profundidadCangilon, materialCangilon, pesoMaterialEnCadaCangilon, pesoCangilonVacio, marcaCangilon,referenciaCangilon,capacidadCangilon,noFilasCangilones,separacionCangilones,noAgujeros,distanciaBordeBandaEstructura, distanciaPosteriorBandaEstructura, distanciaLaboFrontalCangilonEstructura, distanciaBordesCangilonEstructura, tipoVentilacion, tipoCangilon) VALUES (:idRegistro, :longitudCangilon, :proyeccionCangilon, :profundidadCangilon, :materialCangilon, :pesoMaterialEnCadaCangilon, :pesoCangilonVacio, :marcaCangilon,:referenciaCangilon,:capacidadCangilon,:noFilasCangilones,:separacionCangilones,:noAgujeros,:distanciaBordeBandaEstructura, :distanciaPosteriorBandaEstructura, :distanciaLaboFrontalCangilonEstructura, :distanciaBordesCangilonEstructura, :tipoVentilacion,:tipoCangilon) ";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':longitudCangilon', $emp["longitudCangilon"]);
        $stmt->bindParam(":proyeccionCangilon", $emp["proyeccionCangilon"]);
        $stmt->bindParam(":profundidadCangilon", $emp["profundidadCangilon"]);
        $stmt->bindParam(":materialCangilon", $emp["materialCangilon"]);

        $stmt->bindParam(':pesoMaterialEnCadaCangilon', $emp["pesoMaterialEnCadaCangilon"]);
        $stmt->bindParam(":pesoCangilonVacio", $emp["pesoCangilonVacio"]);
        $stmt->bindParam(":marcaCangilon", $emp["marcaCangilon"]);
        $stmt->bindParam(":referenciaCangilon", $emp["referenciaCangilon"]);

        $stmt->bindParam(':capacidadCangilon', $emp["capacidadCangilon"]);
        $stmt->bindParam(":noFilasCangilones", $emp["noFilasCangilones"]);
        $stmt->bindParam(":separacionCangilones", $emp["separacionCangilones"]);
        $stmt->bindParam(":noAgujeros", $emp["noAgujeros"]);

        $stmt->bindParam(':distanciaBordeBandaEstructura', $emp["distanciaBordeBandaEstructura"]);
        $stmt->bindParam(":distanciaPosteriorBandaEstructura", $emp["distanciaPosteriorBandaEstructura"]);
        $stmt->bindParam(":distanciaLaboFrontalCangilonEstructura", $emp["distanciaLaboFrontalCangilonEstructura"]);
        $stmt->bindParam(":distanciaBordesCangilonEstructura", $emp["distanciaBordesCangilonEstructura"]);
        $stmt->bindParam(":tipoVentilacion", $emp["tipoVentilacion"]);
        $stmt->bindParam(":tipoCangilon", $emp["tipoCangilon"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarCangilon($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = " UPDATE bandaElevadora set longitudCangilon=:longitudCangilon, proyeccionCangilon=:proyeccionCangilon, profundidadCangilon=:profundidadCangilon, materialCangilon=:materialCangilon, pesoMaterialEnCadaCangilon=:pesoMaterialEnCadaCangilon, pesoCangilonVacio=:pesoCangilonVacio, marcaCangilon=:marcaCangilon,referenciaCangilon=:referenciaCangilon,capacidadCangilon=:capacidadCangilon,noFilasCangilones=:noFilasCangilones,separacionCangilones=:separacionCangilones,noAgujeros=:noAgujeros,distanciaBordeBandaEstructura=:distanciaBordeBandaEstructura, distanciaPosteriorBandaEstructura=:distanciaPosteriorBandaEstructura, distanciaLaboFrontalCangilonEstructura=:distanciaLaboFrontalCangilonEstructura, distanciaBordesCangilonEstructura=:distanciaBordesCangilonEstructura, tipoVentilacion=:tipoVentilacion, tipoCangilon=:tipoCangilon

  where idRegistro=:idRegistro";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':longitudCangilon', $emp["longitudCangilon"]);
        $stmt->bindParam(":proyeccionCangilon", $emp["proyeccionCangilon"]);
        $stmt->bindParam(":profundidadCangilon", $emp["profundidadCangilon"]);
        $stmt->bindParam(":materialCangilon", $emp["materialCangilon"]);

        $stmt->bindParam(':pesoMaterialEnCadaCangilon', $emp["pesoMaterialEnCadaCangilon"]);
        $stmt->bindParam(":pesoCangilonVacio", $emp["pesoCangilonVacio"]);
        $stmt->bindParam(":marcaCangilon", $emp["marcaCangilon"]);
        $stmt->bindParam(":referenciaCangilon", $emp["referenciaCangilon"]);

        $stmt->bindParam(':capacidadCangilon', $emp["capacidadCangilon"]);
        $stmt->bindParam(":noFilasCangilones", $emp["noFilasCangilones"]);
        $stmt->bindParam(":separacionCangilones", $emp["separacionCangilones"]);
        $stmt->bindParam(":noAgujeros", $emp["noAgujeros"]);

        $stmt->bindParam(':distanciaBordeBandaEstructura', $emp["distanciaBordeBandaEstructura"]);
        $stmt->bindParam(":distanciaPosteriorBandaEstructura", $emp["distanciaPosteriorBandaEstructura"]);
        $stmt->bindParam(":distanciaLaboFrontalCangilonEstructura", $emp["distanciaLaboFrontalCangilonEstructura"]);
        $stmt->bindParam(":distanciaBordesCangilonEstructura", $emp["distanciaBordesCangilonEstructura"]);
        $stmt->bindParam(":tipoVentilacion", $emp["tipoVentilacion"]);
        $stmt->bindParam(":tipoCangilon", $emp["tipoCangilon"]);

        print_r($stmt);
        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

/*--------------------------------------------------------- FIN DE CANGILÓN ------------------------------------------------*/
/*============================================================================================================== /


/*--------------------------------------------------------- CONDICIONES CARGA --------------------------------------------*/

function registroCondicionesCargaElevadora($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = "INSERT INTO bandaElevadora (idRegistro, materialElevadora, ataqueQuimicoElevadora, ataqueTemperaturaElevadora, ataqueAceitesElevadora, ataqueAbrasivoElevadora, horasTrabajoDiaElevadora, capacidadElevadora,diasTrabajoSemanaElevadora,porcentajeFinosElevadora,abrasividadElevadora,maxGranulometriaElevadora,densidadMaterialElevadora,tempMaxSobreBandaElevadora, tempPromedioMaterialSobreBandaElevadora, variosPuntosDeAlimentacion, lluviaDeMaterial, anchoPiernaElevador, profundidadPiernaElevador,tempAmbienteMin,tempAmbienteMax,tipoCarga, tipoDescarga) VALUES (:idRegistro, :materialElevadora, :ataqueQuimicoElevadora, :ataqueTemperaturaElevadora, :ataqueAceitesElevadora, :ataqueAbrasivoElevadora, :horasTrabajoDiaElevadora, :capacidadElevadora,:diasTrabajoSemanaElevadora,:porcentajeFinosElevadora,:abrasividadElevadora,:maxGranulometriaElevadora,:densidadMaterialElevadora,:tempMaxSobreBandaElevadora, :tempPromedioMaterialSobreBandaElevadora, :variosPuntosDeAlimentacion, :lluviaDeMaterial, :anchoPiernaElevador,:profundidadPiernaElevador,:tempAmbienteMin, tempAmbienteMax, :tipoCarga, :tipoDescarga) ";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':materialElevadora', $emp["materialElevadora"]);
        $stmt->bindParam(":ataqueQuimicoElevadora", $emp["ataqueQuimicoElevadora"]);
        $stmt->bindParam(":ataqueTemperaturaElevadora", $emp["ataqueTemperaturaElevadora"]);
        $stmt->bindParam(":ataqueAceitesElevadora", $emp["ataqueAceitesElevadora"]);

        $stmt->bindParam(':ataqueAbrasivoElevadora', $emp["ataqueAbrasivoElevadora"]);
        $stmt->bindParam(":horasTrabajoDiaElevadora", $emp["horasTrabajoDiaElevadora"]);
        $stmt->bindParam(":capacidadElevadora", $emp["capacidadElevadora"]);
        $stmt->bindParam(":diasTrabajoSemanaElevadora", $emp["diasTrabajoSemanaElevadora"]);

        $stmt->bindParam(':porcentajeFinosElevadora', $emp["porcentajeFinosElevadora"]);
        $stmt->bindParam(":abrasividadElevadora", $emp["abrasividadElevadora"]);
        $stmt->bindParam(":maxGranulometriaElevadora", $emp["maxGranulometriaElevadora"]);
        $stmt->bindParam(":densidadMaterialElevadora", $emp["densidadMaterialElevadora"]);
        $stmt->bindParam(":profundidadPiernaElevador", $emp["profundidadPiernaElevador"]);

        $stmt->bindParam(':tempMaxSobreBandaElevadora', $emp["tempMaxSobreBandaElevadora"]);
        $stmt->bindParam(":tempPromedioMaterialSobreBandaElevadora", $emp["tempPromedioMaterialSobreBandaElevadora"]);
        $stmt->bindParam(":variosPuntosDeAlimentacion", $emp["variosPuntosDeAlimentacion"]);
        $stmt->bindParam(":lluviaDeMaterial", $emp["lluviaDeMaterial"]);
        $stmt->bindParam(":anchoPiernaElevador", $emp["anchoPiernaElevador"]);
        $stmt->bindParam(":tempAmbienteMin", $emp["tempAmbienteMin"]);
        $stmt->bindParam(":tempAmbienteMax", $emp["tempAmbienteMax"]);
        $stmt->bindParam(":tipoCarga", $emp["tipoCarga"]);
        $stmt->bindParam(":tipoDescarga", $emp["tipoDescarga"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarCondicionesCargaElevadora($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = " UPDATE bandaElevadora set
  materialElevadora=:materialElevadora,
  ataqueQuimicoElevadora=:ataqueQuimicoElevadora,
  ataqueTemperaturaElevadora=:ataqueTemperaturaElevadora,
  ataqueAceitesElevadora=:ataqueAceitesElevadora,
  ataqueAbrasivoElevadora=:ataqueAbrasivoElevadora,
  horasTrabajoDiaElevadora=:horasTrabajoDiaElevadora,
  capacidadElevadora=:capacidadElevadora,
  diasTrabajoSemanaElevadora=:diasTrabajoSemanaElevadora,
  porcentajeFinosElevadora=:porcentajeFinosElevadora,
  abrasividadElevadora=:abrasividadElevadora,
  maxGranulometriaElevadora=:maxGranulometriaElevadora,
  densidadMaterialElevadora=:densidadMaterialElevadora,
  tempMaxMaterialSobreBandaElevadora=:tempMaxMaterialSobreBandaElevadora,
  tempPromedioMaterialSobreBandaElevadora=:tempPromedioMaterialSobreBandaElevadora,
  variosPuntosDeAlimentacion=:variosPuntosDeAlimentacion,
  lluviaDeMaterial=:lluviaDeMaterial,
  anchoPiernaElevador=:anchoPiernaElevador,
  profundidadPiernaElevador=:profundidadPiernaElevador,
  tempAmbienteMin=:tempAmbienteMin,
  tempAmbienteMax=:tempAmbienteMax,
  tipoCarga=:tipoCarga,
  tipoDescarga=:tipoDescarga

  where idRegistro=:idRegistro";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':materialElevadora', $emp["materialElevadora"]);
        $stmt->bindParam(":ataqueQuimicoElevadora", $emp["ataqueQuimicoElevadora"]);
        $stmt->bindParam(":ataqueTemperaturaElevadora", $emp["ataqueTemperaturaElevadora"]);
        $stmt->bindParam(":ataqueAceitesElevadora", $emp["ataqueAceitesElevadora"]);

        $stmt->bindParam(':ataqueAbrasivoElevadora', $emp["ataqueAbrasivoElevadora"]);
        $stmt->bindParam(":horasTrabajoDiaElevadora", $emp["horasTrabajoDiaElevadora"]);
        $stmt->bindParam(":capacidadElevadora", $emp["capacidadElevadora"]);
        $stmt->bindParam(":diasTrabajoSemanaElevadora", $emp["diasTrabajoSemanaElevadora"]);

        $stmt->bindParam(':porcentajeFinosElevadora', $emp["porcentajeFinosElevadora"]);
        $stmt->bindParam(":abrasividadElevadora", $emp["abrasividadElevadora"]);
        $stmt->bindParam(":maxGranulometriaElevadora", $emp["maxGranulometriaElevadora"]);
        $stmt->bindParam(":densidadMaterialElevadora", $emp["densidadMaterialElevadora"]);

        $stmt->bindParam(':tempMaxMaterialSobreBandaElevadora', $emp["tempMaxMaterialSobreBandaElevadora"]);
        $stmt->bindParam(":tempPromedioMaterialSobreBandaElevadora", $emp["tempPromedioMaterialSobreBandaElevadora"]);
        $stmt->bindParam(":variosPuntosDeAlimentacion", $emp["variosPuntosDeAlimentacion"]);
        $stmt->bindParam(":lluviaDeMaterial", $emp["lluviaDeMaterial"]);
        $stmt->bindParam(":anchoPiernaElevador", $emp["anchoPiernaElevador"]);
        $stmt->bindParam(":profundidadPiernaElevador", $emp["profundidadPiernaElevador"]);

        $stmt->bindParam(":tempAmbienteMin", $emp["tempAmbienteMin"]);
        $stmt->bindParam(":tempAmbienteMax", $emp["tempAmbienteMax"]);
        $stmt->bindParam(":tipoCarga", $emp["tipoCarga"]);
        $stmt->bindParam(":tipoDescarga", $emp["tipoDescarga"]);

        print_r($stmt);
        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}
/*--------------------------------------------------------- FIN DE CONDICIONES CARGA --------------------------------------*/
/*============================================================================================================================  
============================================================================================================================*/


/*------------------------------------------------------------------ BANDA ---------------------------------------------*/

function registroBandaElevadora($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = "INSERT into bandaElevadora (idRegistro, marcaBandaElevadora, anchoBandaElevadora, distanciaEntrePoleasElevadora, noLonaBandaElevadora, tipoLonaBandaElevadora,
                      espesorTotalBandaElevadora, espesorCojinActualElevadora, espesorCubiertaSuperiorElevadora, espesorCubiertaInferiorElevadora, tipoCubiertaElevadora,
                      tipoEmpalmeElevadora, estadoEmpalmeElevadora, resistenciaRoturaLonaElevadora, velocidadBandaElevadora, marcaBandaElevadoraAnterior,
                      anchoBandaElevadoraAnterior, noLonasBandaElevadoraAnterior, tipoLonaBandaElevadoraAnterior, espesorTotalBandaElevadoraAnterior,
                      espesorCubiertaSuperiorBandaElevadoraAnterior, espesorCojinElevadoraAnterior, espesorCubiertaInferiorBandaElevadoraAnterior, tipoCubiertaElevadoraAnterior,
                      tipoEmpalmeElevadoraAnterior, resistenciaRoturaBandaElevadoraAnterior, tonsTransportadasBandaElevadoraAnterior,
                      causaFallaCambioBandaElevadoraAnterior, velocidadBandaElevadoraAnterior)

  values(:idRegistro, :marcaBandaElevadora, :anchoBandaElevadora, :distanciaEntrePoleasElevadora, :noLonaBandaElevadora, :tipoLonaBandaElevadora, :espesorTotalBandaElevadora, :espesorCojinActualElevadora, :espesorCubiertaSuperiorElevadora, :espesorCubiertaInferiorElevadora, :tipoCubiertaElevadora, :tipoEmpalmeElevadora, :estadoEmpalmeElevadora, :resistenciaRoturaLonaElevadora, :velocidadBandaElevadora, :marcaBandaElevadoraAnterior, :anchoBandaElevadoraAnterior, :noLonasBandaElevadoraAnterior, :tipoLonaBandaElevadoraAnterior, :espesorTotalBandaElevadoraAnterior, :espesorCubiertaSuperiorBandaElevadoraAnterior, :espesorCojinElevadoraAnterior, :espesorCubiertaInferiorBandaElevadoraAnterior, :tipoCubiertaElevadoraAnterior, :tipoEmpalmeElevadoraAnterior, :resistenciaRoturaBandaElevadoraAnterior, :tonsTransportadasBandaElevadoraAnterior, :causaFallaCambioBandaElevadoraAnterior, :velocidadBandaElevadoraAnterior) ";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':marcaBandaElevadora', $emp["marcaBandaElevadora"]);
        $stmt->bindParam(":anchoBandaElevadora", $emp["anchoBandaElevadora"]);
        $stmt->bindParam(":distanciaEntrePoleasElevadora", $emp["distanciaEntrePoleasElevadora"]);
        $stmt->bindParam(":noLonaBandaElevadora", $emp["noLonaBandaElevadora"]);
        $stmt->bindParam(':tipoLonaBandaElevadora', $emp["tipoLonaBandaElevadora"]);
        $stmt->bindParam(":espesorTotalBandaElevadora", $emp["espesorTotalBandaElevadora"]);
        $stmt->bindParam(":espesorCojinActualElevadora", $emp["espesorCojinActualElevadora"]);
        $stmt->bindParam(":espesorCubiertaSuperiorElevadora", $emp["espesorCubiertaSuperiorElevadora"]);
        $stmt->bindParam(':espesorCubiertaInferiorElevadora', $emp["espesorCubiertaInferiorElevadora"]);
        $stmt->bindParam(":tipoCubiertaElevadora", $emp["tipoCubiertaElevadora"]);
        $stmt->bindParam(":tipoEmpalmeElevadora", $emp["tipoEmpalmeElevadora"]);
        $stmt->bindParam(":estadoEmpalmeElevadora", $emp["estadoEmpalmeElevadora"]);
        $stmt->bindParam(":resistenciaRoturaLonaElevadora", $emp["resistenciaRoturaLonaElevadora"]);
        $stmt->bindParam(':velocidadBandaElevadora', $emp["velocidadBandaElevadora"]);
        $stmt->bindParam(":marcaBandaElevadoraAnterior", $emp["marcaBandaElevadoraAnterior"]);
        $stmt->bindParam(":anchoBandaElevadoraAnterior", $emp["anchoBandaElevadoraAnterior"]);
        $stmt->bindParam(":noLonasBandaElevadoraAnterior", $emp["noLonasBandaElevadoraAnterior"]);
        $stmt->bindParam(":tipoLonaBandaElevadoraAnterior", $emp["tipoLonaBandaElevadoraAnterior"]);
        $stmt->bindParam(":espesorTotalBandaElevadoraAnterior", $emp["espesorTotalBandaElevadoraAnterior"]);
        $stmt->bindParam(":espesorCubiertaSuperiorBandaElevadoraAnterior", $emp["espesorCubiertaSuperiorBandaElevadoraAnterior"]);
        $stmt->bindParam(":espesorCojinElevadoraAnterior", $emp["espesorCojinElevadoraAnterior"]);
        $stmt->bindParam(":espesorCubiertaInferiorBandaElevadoraAnterior", $emp["espesorCubiertaInferiorBandaElevadoraAnterior"]);
        $stmt->bindParam(':tipoCubiertaElevadoraAnterior', $emp["tipoCubiertaElevadoraAnterior"]);
        $stmt->bindParam(":tipoEmpalmeElevadoraAnterior", $emp["tipoEmpalmeElevadoraAnterior"]);
        $stmt->bindParam(":resistenciaRoturaBandaElevadoraAnterior", $emp["resistenciaRoturaBandaElevadoraAnterior"]);
        $stmt->bindParam(":tonsTransportadasBandaElevadoraAnterior", $emp["tonsTransportadasBandaElevadoraAnterior"]);
        $stmt->bindParam(":causaFallaCambioBandaElevadoraAnterior", $emp["causaFallaCambioBandaElevadoraAnterior"]);
        $stmt->bindParam(":velocidadBandaElevadoraAnterior", $emp["velocidadBandaElevadoraAnterior"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarBandaElevadora($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = "UPDATE bandaElevadora set
marcaBandaElevadora=:marcaBandaElevadora,
anchoBandaElevadora=:anchoBandaElevadora,
distanciaEntrePoleasElevadora=:distanciaEntrePoleasElevadora,
noLonaBandaElevadora=:noLonaBandaElevadora, tipoLonaBandaElevadora=:tipoLonaBandaElevadora,
espesorTotalBandaElevadora=:espesorTotalBandaElevadora,
espesorCojinActualElevadora=:espesorCojinActualElevadora, espesorCubiertaSuperiorElevadora=:espesorCubiertaSuperiorElevadora, espesorCubiertaInferiorElevadora=:espesorCubiertaInferiorElevadora,
tipoCubiertaElevadora=:tipoCubiertaElevadora,
tipoEmpalmeElevadora=:tipoEmpalmeElevadora,
estadoEmpalmeElevadora=:estadoEmpalmeElevadora,
resistenciaRoturaLonaElevadora=:resistenciaRoturaLonaElevadora,
velocidadBandaElevadora=:velocidadBandaElevadora,
marcaBandaElevadoraAnterior=:marcaBandaElevadoraAnterior,
anchoBandaElevadoraAnterior=:anchoBandaElevadoraAnterior, noLonasBandaElevadoraAnterior=:noLonasBandaElevadoraAnterior, tipoLonaBandaElevadoraAnterior=:tipoLonaBandaElevadoraAnterior, espesorTotalBandaElevadoraAnterior=:espesorTotalBandaElevadoraAnterior,
espesorCubiertaSuperiorBandaElevadoraAnterior=:espesorCubiertaSuperiorBandaElevadoraAnterior, espesorCojinElevadoraAnterior=:espesorCojinElevadoraAnterior, espesorCubiertaInferiorBandaElevadoraAnterior=:espesorCubiertaInferiorBandaElevadoraAnterior, tipoCubiertaElevadoraAnterior=:tipoCubiertaElevadoraAnterior,
tipoEmpalmeElevadoraAnterior=:tipoEmpalmeElevadoraAnterior, resistenciaRoturaBandaElevadoraAnterior=:resistenciaRoturaBandaElevadoraAnterior, tonsTransportadasBandaElevadoraAnterior=:tonsTransportadasBandaElevadoraAnterior,
causaFallaCambioBandaElevadoraAnterior=:causaFallaCambioBandaElevadoraAnterior,
velocidadBandaElevadoraAnterior=:velocidadBandaElevadoraAnterior

  where idRegistro=:idRegistro";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':marcaBandaElevadora', $emp["marcaBandaElevadora"]);
        $stmt->bindParam(":anchoBandaElevadora", $emp["anchoBandaElevadora"]);
        $stmt->bindParam(":distanciaEntrePoleasElevadora", $emp["distanciaEntrePoleasElevadora"]);
        $stmt->bindParam(":noLonaBandaElevadora", $emp["noLonaBandaElevadora"]);
        $stmt->bindParam(':tipoLonaBandaElevadora', $emp["tipoLonaBandaElevadora"]);
        $stmt->bindParam(":espesorTotalBandaElevadora", $emp["espesorTotalBandaElevadora"]);
        $stmt->bindParam(":espesorCojinActualElevadora", $emp["espesorCojinActualElevadora"]);
        $stmt->bindParam(":espesorCubiertaSuperiorElevadora", $emp["espesorCubiertaSuperiorElevadora"]);
        $stmt->bindParam(':espesorCubiertaInferiorElevadora', $emp["espesorCubiertaInferiorElevadora"]);
        $stmt->bindParam(":tipoCubiertaElevadora", $emp["tipoCubiertaElevadora"]);
        $stmt->bindParam(":tipoEmpalmeElevadora", $emp["tipoEmpalmeElevadora"]);
        $stmt->bindParam(":estadoEmpalmeElevadora", $emp["estadoEmpalmeElevadora"]);
        $stmt->bindParam(":resistenciaRoturaLonaElevadora", $emp["resistenciaRoturaLonaElevadora"]);
        $stmt->bindParam(':velocidadBandaElevadora', $emp["velocidadBandaElevadora"]);
        $stmt->bindParam(":marcaBandaElevadoraAnterior", $emp["marcaBandaElevadoraAnterior"]);
        $stmt->bindParam(":anchoBandaElevadoraAnterior", $emp["anchoBandaElevadoraAnterior"]);
        $stmt->bindParam(":noLonasBandaElevadoraAnterior", $emp["noLonasBandaElevadoraAnterior"]);
        $stmt->bindParam(":tipoLonaBandaElevadoraAnterior", $emp["tipoLonaBandaElevadoraAnterior"]);
        $stmt->bindParam(":espesorTotalBandaElevadoraAnterior", $emp["espesorTotalBandaElevadoraAnterior"]);
        $stmt->bindParam(":espesorCubiertaSuperiorBandaElevadoraAnterior", $emp["espesorCubiertaSuperiorBandaElevadoraAnterior"]);
        $stmt->bindParam(":espesorCojinElevadoraAnterior", $emp["espesorCojinElevadoraAnterior"]);
        $stmt->bindParam(":espesorCubiertaInferiorBandaElevadoraAnterior", $emp["espesorCubiertaInferiorBandaElevadoraAnterior"]);
        $stmt->bindParam(':tipoCubiertaElevadoraAnterior', $emp["tipoCubiertaElevadoraAnterior"]);
        $stmt->bindParam(":tipoEmpalmeElevadoraAnterior", $emp["tipoEmpalmeElevadoraAnterior"]);
        $stmt->bindParam(":resistenciaRoturaBandaElevadoraAnterior", $emp["resistenciaRoturaBandaElevadoraAnterior"]);
        $stmt->bindParam(":tonsTransportadasBandaElevadoraAnterior", $emp["tonsTransportadasBandaElevadoraAnterior"]);
        $stmt->bindParam(":causaFallaCambioBandaElevadoraAnterior", $emp["causaFallaCambioBandaElevadoraAnterior"]);
        $stmt->bindParam(":velocidadBandaElevadoraAnterior", $emp["velocidadBandaElevadoraAnterior"]);

        print_r($stmt);
        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}
/*------------------------------------------------------------- FIN DE BANDA ----------------------------------------*/


/*============================================================================================================================
=                                                           FIN DE BANDA ELEVADORA                                         =
===========================================================================================================================*/

/*--------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------*/
/*-------------------------------------------------------------------------------------------------------------------------*/


/*============================================================================================================================
=                                                    BANDA PESADA                                                           =
============================================================================================================================*/

function registroBandaPesada($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }


    $sql = "INSERT into bandaTransmision (idRegistro, tipoParteTransmision, anchoPoleaMotrizTransmision, anchoPoleaConducidaTransmision, diametroPoleaMotrizTransmision, diametroPoleaConducidaTransmision, rpmSalidaReductorTransmision, potenciaMotorTransmision, distanciaEntreCentrosTransmision, anchoBandaTransmision, observacionRegistro)

  values(:idRegistro, :tipoParteTransmision, :anchoPoleaMotrizTransmision, :anchoPoleaConducidaTransmision, :diametroPoleaMotrizTransmision, :diametroPoleaConducidaTransmision, :rpmSalidaReductorTransmision, :potenciaMotorTransmision, :distanciaEntreCentrosTransmision, :anchoBandaTransmision,:observacionRegistro) ";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(":anchoBandaTransmision", $emp["anchoBandaTransmision"]);
        $stmt->bindParam(":distanciaEntreCentrosTransmision", $emp["distanciaEntreCentrosTransmision"]);
        $stmt->bindParam(":potenciaMotorTransmision", $emp["potenciaMotorTransmision"]);
        $stmt->bindParam(':rpmSalidaReductorTransmision', $emp["rpmSalidaReductorTransmision"]);
        $stmt->bindParam(":diametroPoleaConducidaTransmision", $emp["diametroPoleaConducidaTransmision"]);
        $stmt->bindParam(":anchoPoleaConducidaTransmision", $emp["anchoPoleaConducidaTransmision"]);
        $stmt->bindParam(":diametroPoleaMotrizTransmision", $emp["diametroPoleaMotrizTransmision"]);
        $stmt->bindParam(':anchoPoleaMotrizTransmision', $emp["anchoPoleaMotrizTransmision"]);
        $stmt->bindParam(':tipoParteTransmision', $emp["tipoParteTransmision"]);
        $stmt->bindParam(':observacionRegistro', $emp["observacionRegistroPesada"]);


        if($stmt->execute())
        {
            return 'ok';
        }
        else
        {
            return 'error';
        }

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarBandaPesada($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = "UPDATE bandaTransmision set anchoBandaTransmision=:anchoBandaTransmision, distanciaEntreCentrosTransmision=:distanciaEntreCentrosTransmision, potenciaMotorTransmision=:potenciaMotorTransmision, rpmSalidaReductorTransmision=:rpmSalidaReductorTransmision, diametroPoleaConducidaTransmision=:diametroPoleaConducidaTransmision, anchoPoleaConducidaTransmision=:anchoPoleaConducidaTransmision, diametroPoleaMotrizTransmision=:diametroPoleaMotrizTransmision, anchoPoleaMotrizTransmision=:anchoPoleaMotrizTransmision, tipoParteTransmision=:tipoParteTransmision, observacionRegistro=:observacionRegistroPesada

  where idRegistro=:idRegistro";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(":anchoBandaTransmision", $emp["anchoBandaTransmision"]);
        $stmt->bindParam(":distanciaEntreCentrosTransmision", $emp["distanciaEntreCentrosTransmision"]);
        $stmt->bindParam(":potenciaMotorTransmision", $emp["potenciaMotorTransmision"]);
        $stmt->bindParam(':rpmSalidaReductorTransmision', $emp["rpmSalidaReductorTransmision"]);
        $stmt->bindParam(":diametroPoleaConducidaTransmision", $emp["diametroPoleaConducidaTransmision"]);
        $stmt->bindParam(":anchoPoleaConducidaTransmision", $emp["anchoPoleaConducidaTransmision"]);
        $stmt->bindParam(":diametroPoleaMotrizTransmision", $emp["diametroPoleaMotrizTransmision"]);
        $stmt->bindParam(':anchoPoleaMotrizTransmision', $emp["anchoPoleaMotrizTransmision"]);
        $stmt->bindParam(':tipoParteTransmision', $emp["tipoParteTransmision"]);
        $stmt->bindParam(':observacionRegistro', $emp["observacionRegistroPesada"]);
        

        print_r($stmt);
        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

/*==========================================================================================================================================
=                                                              FIN BANDA PESADA                                            =
===========================================================================================================================================*/


/*-----------------------------------------------------------------------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------------------------------------------------------------------*/


/*==========================================================================================================================================
=                                                               BANDA HORIZONTAL                                                           =
===========================================================================================================================================*/

/*------------------------------------------------------------------ BANDA ------------------------------------------------------------*/

function registroBandaHorizontal($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    

    $sql = "INSERT into bandaTransportadora (idRegistro, marcaBandaTransportadora, anchoBandaTransportadora, noLonasBandaTransportadora, tipoLonaBandaTransportadora, espesorTotalBandaTransportadora, espesorCubiertaSuperiorTransportadora, espesorCojinTransportadora, espesorCubiertaInferiorTransportadora, tipoCubiertaTransportadora, tipoEmpalmeTransportadora, estadoEmpalmeTransportadora, distanciaEntrePoleasBandaHorizontal, inclinacionBandaHorizontal, recorridoUtilTensorBandaHorizontal, longitudSinfinBandaHorizontal, resistenciaRoturaLonaTransportadora, localizacionTensorTransportadora, bandaReversible, bandaDeArrastre, velocidadBandaHorizontal, marcaBandaHorizontalAnterior, anchoBandaHorizontalAnterior, noLonasBandaHorizontalAnterior, tipoLonaBandaHorizontalAnterior, espesorTotalBandaHorizontalAnterior, espesorCubiertaSuperiorBandaHorizontalAnterior, espesorCubiertaInferiorBandaHorizontalAnterior, espesorCojinBandaHorizontalAnterior, tipoCubiertaBandaHorizontalAnterior, tipoEmpalmeBandaHorizontalAnterior, resistenciaRoturaLonaBandaHorizontalAnterior, tonsTransportadasBandaHoizontalAnterior, causaFallaCambioBandaHorizontal)

  values(:idRegistro, :marcaBandaTransportadora, :anchoBandaTransportadora, :noLonasBandaTransportadora, :tipoLonaBandaTransportadora, :espesorTotalBandaTransportadora, :espesorCubiertaSuperiorTransportadora, :espesorCojinTransportadora, :espesorCubiertaInferiorTransportadora, :tipoCubiertaTransportadora, :tipoEmpalmeTransportadora, :estadoEmpalmeTransportadora, :distanciaEntrePoleasBandaHorizontal, :inclinacionBandaHorizontal, :recorridoUtilTensorBandaHorizontal, :longitudSinfinBandaHorizontal, :resistenciaRoturaLonaTransportadora, :localizacionTensorTransportadora, :bandaReversible, :bandaDeArrastre, :velocidadBandaHorizontal, :marcaBandaHorizontalAnterior, :anchoBandaHorizontalAnterior, :noLonasBandaHorizontalAnterior, :tipoLonaBandaHorizontalAnterior, :espesorTotalBandaHorizontalAnterior, :espesorCubiertaSuperiorBandaHorizontalAnterior, :espesorCubiertaInferiorBandaHorizontalAnterior, :espesorCojinBandaHorizontalAnterior, :tipoCubiertaBandaHorizontalAnterior, :tipoEmpalmeBandaHorizontalAnterior, :resistenciaRoturaLonaBandaHorizontalAnterior, :tonsTransportadasBandaHoizontalAnterior, :causaFallaCambioBandaHorizontal) ";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':marcaBandaTransportadora', $emp["marcaBandaTransportadora"]);
        $stmt->bindParam(":anchoBandaTransportadora", $emp["anchoBandaTransportadora"]);
        $stmt->bindParam(":noLonasBandaTransportadora", $emp["noLonasBandaTransportadora"]);
        $stmt->bindParam(":tipoLonaBandaTransportadora", $emp["tipoLonaBandaTransportadora"]);
        $stmt->bindParam(':espesorTotalBandaTransportadora', $emp["espesorTotalBandaTransportadora"]);
        $stmt->bindParam(":espesorCubiertaSuperiorTransportadora", $emp["espesorCubiertaSuperiorTransportadora"]);
        $stmt->bindParam(":espesorCojinTransportadora", $emp["espesorCojinTransportadora"]);
        $stmt->bindParam(":espesorCubiertaInferiorTransportadora", $emp["espesorCubiertaInferiorTransportadora"]);
        $stmt->bindParam(':tipoCubiertaTransportadora', $emp["tipoCubiertaTransportadora"]);
        $stmt->bindParam(":tipoEmpalmeTransportadora", $emp["tipoEmpalmeTransportadora"]);
        $stmt->bindParam(":estadoEmpalmeTransportadora", $emp["estadoEmpalmeTransportadora"]);
        $stmt->bindParam(":distanciaEntrePoleasBandaHorizontal", $emp["distanciaEntrePoleasBandaHorizontal"]);
        $stmt->bindParam(":inclinacionBandaHorizontal", $emp["inclinacionBandaHorizontal"]);
        $stmt->bindParam(':recorridoUtilTensorBandaHorizontal', $emp["recorridoUtilTensorBandaHorizontal"]);
        $stmt->bindParam(":longitudSinfinBandaHorizontal", $emp["longitudSinfinBandaHorizontal"]);
        $stmt->bindParam(":resistenciaRoturaLonaTransportadora", $emp["resistenciaRoturaLonaTransportadora"]);
        $stmt->bindParam(":localizacionTensorTransportadora", $emp["localizacionTensorTransportadora"]);
        $stmt->bindParam(":bandaReversible", $emp["bandaReversible"]);
        $stmt->bindParam(":bandaDeArrastre", $emp["bandaDeArrastre"]);
        $stmt->bindParam(":velocidadBandaHorizontal", $emp["velocidadBandaHorizontal"]);
        $stmt->bindParam(":marcaBandaHorizontalAnterior", $emp["marcaBandaHorizontalAnterior"]);
        $stmt->bindParam(":anchoBandaHorizontalAnterior", $emp["anchoBandaHorizontalAnterior"]);
        $stmt->bindParam(':noLonasBandaHorizontalAnterior', $emp["noLonasBandaHorizontalAnterior"]);
        $stmt->bindParam(":tipoLonaBandaHorizontalAnterior", $emp["tipoLonaBandaHorizontalAnterior"]);
        $stmt->bindParam(":espesorTotalBandaHorizontalAnterior", $emp["espesorTotalBandaHorizontalAnterior"]);
        $stmt->bindParam(":espesorCubiertaSuperiorBandaHorizontalAnterior", $emp["espesorCubiertaSuperiorBandaHorizontalAnterior"]);
        $stmt->bindParam(":espesorCubiertaInferiorBandaHorizontalAnterior", $emp["espesorCubiertaInferiorBandaHorizontalAnterior"]);
        $stmt->bindParam(":espesorCojinBandaHorizontalAnterior", $emp["espesorCojinBandaHorizontalAnterior"]);
        $stmt->bindParam(":tipoCubiertaBandaHorizontalAnterior", $emp["tipoCubiertaBandaHorizontalAnterior"]);
        $stmt->bindParam(":tipoEmpalmeBandaHorizontalAnterior", $emp["tipoEmpalmeBandaHorizontalAnterior"]);
        $stmt->bindParam(":resistenciaRoturaLonaBandaHorizontalAnterior", $emp["resistenciaRoturaLonaBandaHorizontalAnterior"]);
        $stmt->bindParam(":tonsTransportadasBandaHoizontalAnterior", $emp["tonsTransportadasBandaHoizontalAnterior"]);
        $stmt->bindParam(":causaFallaCambioBandaHorizontal", $emp["causaFallaCambioBandaHorizontal"]);

        print_r($stmt);
        
        if($stmt->execute())
        {
            return 'ok';
        }
        else
        {
            return 'error';
        }

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarBandaHorizontal($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = "UPDATE bandaTransportadora set
marcaBandaTransportadora=:marcaBandaTransportadora, anchoBandaTransportadora=:anchoBandaTransportadora, noLonasBandaTransportadora=:noLonasBandaTransportadora, tipoLonaBandaTransportadora=:tipoLonaBandaTransportadora, espesorTotalBandaTransportadora=:espesorTotalBandaTransportadora,
                      espesorCubiertaSuperiorTransportadora=:espesorCubiertaSuperiorTransportadora, espesorCojinTransportadora=:espesorCojinTransportadora, espesorCubiertaInferiorTransportadora=:espesorCubiertaInferiorTransportadora, tipoCubiertaTransportadora=:tipoCubiertaTransportadora, tipoEmpalmeTransportadora=:tipoEmpalmeTransportadora,
                      estadoEmpalmeTransportadora=:estadoEmpalmeTransportadora, localizacionTensorTransportadora=:localizacionTensorTransportadora, resistenciaRoturaLonaTransportadora=:resistenciaRoturaLonaTransportadora, velocidadBandaHorizontal=:velocidadBandaHorizontal, bandaReversible=:bandaReversible,
                     bandaDeArrastre=:bandaDeArrastre, marcaBandaHorizontalAnterior=:marcaBandaHorizontalAnterior, anchoBandaHorizontalAnterior=:anchoBandaHorizontalAnterior, noLonasBandaHorizontalAnterior=:noLonasBandaHorizontalAnterior,
                      tipoLonaBandaHorizontalAnterior=:tipoLonaBandaHorizontalAnterior, espesorTotalBandaHorizontalAnterior=:espesorTotalBandaHorizontalAnterior, espesorCubiertaSuperiorBandaHorizontalAnterior=:espesorCubiertaSuperiorBandaHorizontalAnterior, espesorCubiertaInferiorBandaHorizontalAnterior=:espesorCubiertaInferiorBandaHorizontalAnterior,
                      espesorCojinBandaHorizontalAnterior=:espesorCojinBandaHorizontalAnterior, tipoEmpalmeBandaHorizontalAnterior=:tipoEmpalmeBandaHorizontalAnterior, resistenciaRoturaLonaBandaHorizontalAnterior=:resistenciaRoturaLonaBandaHorizontalAnterior,
                      causaFallaCambioBandaHorizontal=:causaFallaCambioBandaHorizontal, tipoCubiertaBandaHorizontalAnterior=:tipoCubiertaBandaHorizontalAnterior, distanciaEntrePoleasBandaHorizontal=:distanciaEntrePoleasBandaHorizontal, inclinacionBandaHorizontal=:inclinacionBandaHorizontal,recorridoUtilTensorBandaHorizontal=:recorridoUtilTensorBandaHorizontal,longitudSinfinBandaHorizontal=:longitudSinfinBandaHorizontal,tonsTransportadasBandaHoizontalAnterior=:tonsTransportadasBandaHoizontalAnterior

  where idRegistro=:idRegistro";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':marcaBandaTransportadora', $emp["marcaBandaTransportadora"]);
        $stmt->bindParam(":anchoBandaTransportadora", $emp["anchoBandaTransportadora"]);
        $stmt->bindParam(":noLonasBandaTransportadora", $emp["noLonasBandaTransportadora"]);
        $stmt->bindParam(":tipoLonaBandaTransportadora", $emp["tipoLonaBandaTransportadora"]);
        $stmt->bindParam(':espesorTotalBandaTransportadora', $emp["espesorTotalBandaTransportadora"]);
        $stmt->bindParam(":espesorCubiertaSuperiorTransportadora", $emp["espesorCubiertaSuperiorTransportadora"]);
        $stmt->bindParam(":espesorCojinTransportadora", $emp["espesorCojinTransportadora"]);
        $stmt->bindParam(":espesorCubiertaInferiorTransportadora", $emp["espesorCubiertaInferiorTransportadora"]);
        $stmt->bindParam(':tipoCubiertaTransportadora', $emp["tipoCubiertaTransportadora"]);
        $stmt->bindParam(":tipoEmpalmeTransportadora", $emp["tipoEmpalmeTransportadora"]);
        $stmt->bindParam(":estadoEmpalmeTransportadora", $emp["estadoEmpalmeTransportadora"]);
        $stmt->bindParam(":localizacionTensorTransportadora", $emp["localizacionTensorTransportadora"]);
        $stmt->bindParam(":resistenciaRoturaLonaTransportadora", $emp["resistenciaRoturaLonaTransportadora"]);
        $stmt->bindParam(':velocidadBandaHorizontal', $emp["velocidadBandaHorizontal"]);
        $stmt->bindParam(":bandaReversible", $emp["bandaReversible"]);
        $stmt->bindParam("bandaDeArrastre", $emp["bandaDeArrastre"]);
        $stmt->bindParam(":marcaBandaHorizontalAnterior", $emp["marcaBandaHorizontalAnterior"]);
        $stmt->bindParam(":anchoBandaHorizontalAnterior", $emp["anchoBandaHorizontalAnterior"]);
        $stmt->bindParam(":noLonasBandaHorizontalAnterior", $emp["noLonasBandaHorizontalAnterior"]);
        $stmt->bindParam(":tipoLonaBandaHorizontalAnterior", $emp["tipoLonaBandaHorizontalAnterior"]);
        $stmt->bindParam(":espesorTotalBandaHorizontalAnterior", $emp["espesorTotalBandaHorizontalAnterior"]);
        $stmt->bindParam(":espesorCubiertaSuperiorBandaHorizontalAnterior", $emp["espesorCubiertaSuperiorBandaHorizontalAnterior"]);
        $stmt->bindParam(':espesorCubiertaInferiorBandaHorizontalAnterior', $emp["espesorCubiertaInferiorBandaHorizontalAnterior"]);
        $stmt->bindParam(":espesorCojinBandaHorizontalAnterior", $emp["espesorCojinBandaHorizontalAnterior"]);
        $stmt->bindParam(":tipoEmpalmeBandaHorizontalAnterior", $emp["tipoEmpalmeBandaHorizontalAnterior"]);
        $stmt->bindParam(":resistenciaRoturaLonaBandaHorizontalAnterior", $emp["resistenciaRoturaLonaBandaHorizontalAnterior"]);
        $stmt->bindParam(":causaFallaCambioBandaHorizontal", $emp["causaFallaCambioBandaHorizontal"]);
        $stmt->bindParam(":tipoCubiertaBandaHorizontalAnterior", $emp["tipoCubiertaBandaHorizontalAnterior"]);

        $stmt->bindParam(":distanciaEntrePoleasBandaHorizontal", $emp["distanciaEntrePoleasBandaHorizontal"]);
        $stmt->bindParam(":inclinacionBandaHorizontal", $emp["inclinacionBandaHorizontal"]);
        $stmt->bindParam(":recorridoUtilTensorBandaHorizontal", $emp["recorridoUtilTensorBandaHorizontal"]);
        $stmt->bindParam(":longitudSinfinBandaHorizontal", $emp["longitudSinfinBandaHorizontal"]);
        $stmt->bindParam(":tonsTransportadasBandaHoizontalAnterior", $emp["tonsTransportadasBandaHoizontalAnterior"]);

        


        print_r($stmt);
        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}
/*------------------------------------------------------------- FIN DE BANDA -----------------------------------------------------*/



/*-------------------------------------------------------------- DESVIADOR --------------------------------------------------------*/

function registroDesviador($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = "INSERT into bandaTransportadora (idRegistro, cauchoVPlow, hayDesviador, presionUniformeALoAnchoDeLaBanda, elDesviadorBascula, anchoVPlow,
                      espesorVPlow)

  values(:idRegistro, :cauchoVPlow, :hayDesviador, :presionUniformeALoAnchoDeLaBanda, :elDesviadorBascula, :anchoVPlow, :espesorVPlow)";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':cauchoVPlow', $emp["cauchoVPlow"]);
        $stmt->bindParam(":hayDesviador", $emp["hayDesviador"]);
        $stmt->bindParam(":presionUniformeALoAnchoDeLaBanda", $emp["presionUniformeALoAnchoDeLaBanda"]);
        $stmt->bindParam(":elDesviadorBascula", $emp["elDesviadorBascula"]);
        $stmt->bindParam(':anchoVPlow', $emp["anchoVPlow"]);
        $stmt->bindParam(":espesorVPlow", $emp["espesorVPlow"]);

        

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarDesviador($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = "UPDATE bandaTransportadora set
cauchoVPlow=:cauchoVPlow, hayDesviador=:hayDesviador, presionUniformeALoAnchoDeLaBanda=:presionUniformeALoAnchoDeLaBanda, elDesviadorBascula=:elDesviadorBascula, anchoVPlow=:anchoVPlow, espesorVPlow=:espesorVPlow

  where idRegistro=:idRegistro";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':cauchoVPlow', $emp["cauchoVPlow"]);
        $stmt->bindParam(":hayDesviador", $emp["hayDesviador"]);
        $stmt->bindParam(":presionUniformeALoAnchoDeLaBanda", $emp["presionUniformeALoAnchoDeLaBanda"]);
        $stmt->bindParam(":elDesviadorBascula", $emp["elDesviadorBascula"]);
        $stmt->bindParam(':anchoVPlow', $emp["anchoVPlow"]);
        $stmt->bindParam(":espesorVPlow", $emp["espesorVPlow"]);

        


        print_r($stmt);
        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}
/*------------------------------------------------------------- FIN DE DESVIADOR -------------------------------------------------*/


/*-------------------------------------------------------------- POLEA TENSORA ---------------------------------------------------*/

function registroPoleaTensora($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = "INSERT into bandaTransportadora (idRegistro, diametroPoleaTensora, anchoPoleaTensora, tipoPoleaTensora, largoEjePoleaTensora, 
                         diametroEjePoleaTensora, icobandasCentradaEnPoleaTensora, recorridoPoleaTensora, estadoRevestimientoPoleaTensora, tipoTransicionPoleaTensora, distanciaTransicionPoleaColaTensora, potenciaMotorPoleaTensora, 
                         guardaPoleaTensora)

  values(:idRegistro,:diametroPoleaTensora, :anchoPoleaTensora, :tipoPoleaTensora, :largoEjePoleaTensora, 
                         :diametroEjePoleaTensora, :icobandasCentradaEnPoleaTensora, :recorridoPoleaTensora, :estadoRevestimientoPoleaTensora, :tipoTransicionPoleaTensora, :distanciaTransicionPoleaColaTensora, :potenciaMotorPoleaTensora, 
                         :guardaPoleaTensora)";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':diametroPoleaTensora', $emp["diametroPoleaTensora"]);
        $stmt->bindParam(":anchoPoleaTensora", $emp["anchoPoleaTensora"]);
        $stmt->bindParam(":tipoPoleaTensora", $emp["tipoPoleaTensora"]);
        $stmt->bindParam(":largoEjePoleaTensora", $emp["largoEjePoleaTensora"]);
        $stmt->bindParam(':tipoTransicionPoleaTensora', $emp["tipoTransicionPoleaTensora"]);
        $stmt->bindParam(":distanciaTransicionPoleaColaTensora", $emp["distanciaTransicionPoleaColaTensora"]);
        $stmt->bindParam(":potenciaMotorPoleaTensora", $emp["potenciaMotorPoleaTensora"]);
        $stmt->bindParam(":guardaPoleaTensora", $emp["guardaPoleaTensora"]);
        $stmt->bindParam(":diametroEjePoleaTensora", $emp["diametroEjePoleaTensora"]);
        $stmt->bindParam(":icobandasCentradaEnPoleaTensora", $emp["icobandasCentradaEnPoleaTensora"]);
        $stmt->bindParam(":recorridoPoleaTensora", $emp["recorridoPoleaTensora"]);
        $stmt->bindParam(":estadoRevestimientoPoleaTensora", $emp["estadoRevestimientoPoleaTensora"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarPoleaTensora($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = "UPDATE bandaTransportadora set diametroPoleaTensora=:diametroPoleaTensora, anchoPoleaTensora=:anchoPoleaTensora, tipoPoleaTensora=:tipoPoleaTensora, largoEjePoleaTensora=:largoEjePoleaTensora, 
                         diametroEjePoleaTensora=:diametroEjePoleaTensora, icobandasCentradaEnPoleaTensora=:icobandasCentradaEnPoleaTensora, recorridoPoleaTensora=:recorridoPoleaTensora, estadoRevestimientoPoleaTensora=:estadoRevestimientoPoleaTensora, tipoTransicionPoleaTensora=:tipoTransicionPoleaTensora, distanciaTransicionPoleaColaTensora=:distanciaTransicionPoleaColaTensora, potenciaMotorPoleaTensora=:potenciaMotorPoleaTensora, 
                         guardaPoleaTensora=:guardaPoleaTensora

  where idRegistro=:idRegistro";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':diametroPoleaTensora', $emp["diametroPoleaTensora"]);
        $stmt->bindParam(":anchoPoleaTensora", $emp["anchoPoleaTensora"]);
        $stmt->bindParam(":tipoPoleaTensora", $emp["tipoPoleaTensora"]);
        $stmt->bindParam(":largoEjePoleaTensora", $emp["largoEjePoleaTensora"]);
        $stmt->bindParam(':tipoTransicionPoleaTensora', $emp["tipoTransicionPoleaTensora"]);
        $stmt->bindParam(":distanciaTransicionPoleaColaTensora", $emp["distanciaTransicionPoleaColaTensora"]);
        $stmt->bindParam(":potenciaMotorPoleaTensora", $emp["potenciaMotorPoleaTensora"]);
        $stmt->bindParam(":guardaPoleaTensora", $emp["guardaPoleaTensora"]);
        $stmt->bindParam(":diametroEjePoleaTensora", $emp["diametroEjePoleaTensora"]);
        $stmt->bindParam(":icobandasCentradaEnPoleaTensora", $emp["icobandasCentradaEnPoleaTensora"]);
        $stmt->bindParam(":recorridoPoleaTensora", $emp["recorridoPoleaTensora"]);
        $stmt->bindParam(":estadoRevestimientoPoleaTensora", $emp["estadoRevestimientoPoleaTensora"]);

        


        print_r($stmt);
        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}
/*--------------------------------------------------------- FIN DE POLEA TENSORA -------------------------------------------------*/



/*-----------------------------------------------------------  SIST. ALINEACIÓN -----------------------------------------------*/

function registroAlineacion($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = "INSERT into bandaTransportadora (idRegistro, cantidadSistemaAlineacionEnCarga, sistemasAlineacionCargaFuncionando, sistemaAlineacionEnRetorno, cantidadSistemaAlineacionEnRetorno, sistemasAlineacionRetornoFuncionando, sistemaAlineacionRetornoPlano, 
                         sistemaAlineacionArtesaCarga, sistemaAlineacionRetornoEnV, detalleRodilloCentralCarga, detalleRodilloLateralCarg, detalleRodilloRetorno, sistemaAlineacionCarga)

  values(:idRegistro,:cantidadSistemaAlineacionEnCarga, :sistemasAlineacionCargaFuncionando, :sistemaAlineacionEnRetorno, :cantidadSistemaAlineacionEnRetorno, :sistemasAlineacionRetornoFuncionando, :sistemaAlineacionRetornoPlano, :sistemaAlineacionArtesaCarga,:msistemaAlineacionRetornoEnV, :detalleRodilloCentralCarga, :detalleRodilloLateralCarg, :detalleRodilloRetorno, :sistemaAlineacionCarga) ";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':cantidadSistemaAlineacionEnCarga', $emp["cantidadSistemaAlineacionEnCarga"]);
        $stmt->bindParam(":sistemasAlineacionCargaFuncionando", $emp["sistemasAlineacionCargaFuncionando"]);
        $stmt->bindParam(":sistemaAlineacionEnRetorno", $emp["sistemaAlineacionEnRetorno"]);
        $stmt->bindParam(":cantidadSistemaAlineacionEnRetorno", $emp["cantidadSistemaAlineacionEnRetorno"]);
        $stmt->bindParam(':sistemasAlineacionRetornoFuncionando', $emp["sistemasAlineacionRetornoFuncionando"]);
        $stmt->bindParam(":sistemaAlineacionRetornoPlano", $emp["sistemaAlineacionRetornoPlano"]);
        $stmt->bindParam(":sistemaAlineacionArtesaCarga", $emp["sistemaAlineacionArtesaCarga"]);
        $stmt->bindParam(":sistemaAlineacionRetornoEnV", $emp["sistemaAlineacionRetornoEnV"]);
        $stmt->bindParam(":detalleRodilloCentralCarga", $emp["detalleRodilloCentralCarga"]);
        $stmt->bindParam(":detalleRodilloLateralCarg", $emp["detalleRodilloLateralCarg"]);
        $stmt->bindParam(":detalleRodilloRetorno", $emp["detalleRodilloRetorno"]);
        $stmt->bindParam(":sistemaAlineacionCarga", $emp["sistemaAlineacionCarga"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarAlineacion($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = "UPDATE bandaTransportadora set cantidadSistemaAlineacionEnCarga=:cantidadSistemaAlineacionEnCarga, sistemasAlineacionCargaFuncionando=:sistemasAlineacionCargaFuncionando, sistemaAlineacionEnRetorno=:sistemaAlineacionEnRetorno, cantidadSistemaAlineacionEnRetorno=:cantidadSistemaAlineacionEnRetorno, sistemasAlineacionRetornoFuncionando=:sistemasAlineacionRetornoFuncionando, sistemaAlineacionRetornoPlano=:sistemaAlineacionRetornoPlano, sistemaAlineacionArtesaCarga=:sistemaAlineacionArtesaCarga, sistemaAlineacionRetornoEnV=:sistemaAlineacionRetornoEnV, detalleRodilloCentralCarga=:detalleRodilloCentralCarga, detalleRodilloLateralCarg=:detalleRodilloLateralCarg, detalleRodilloRetorno=:detalleRodilloRetorno, sistemaAlineacionCarga=:sistemaAlineacionCarga

  where idRegistro=:idRegistro";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':cantidadSistemaAlineacionEnCarga', $emp["cantidadSistemaAlineacionEnCarga"]);
        $stmt->bindParam(":sistemasAlineacionCargaFuncionando", $emp["sistemasAlineacionCargaFuncionando"]);
        $stmt->bindParam(":sistemaAlineacionEnRetorno", $emp["sistemaAlineacionEnRetorno"]);
        $stmt->bindParam(":cantidadSistemaAlineacionEnRetorno", $emp["cantidadSistemaAlineacionEnRetorno"]);
        $stmt->bindParam(':sistemasAlineacionRetornoFuncionando', $emp["sistemasAlineacionRetornoFuncionando"]);
        $stmt->bindParam(":sistemaAlineacionRetornoPlano", $emp["sistemaAlineacionRetornoPlano"]);
        $stmt->bindParam(":sistemaAlineacionArtesaCarga", $emp["sistemaAlineacionArtesaCarga"]);
        $stmt->bindParam(":sistemaAlineacionRetornoEnV", $emp["sistemaAlineacionRetornoEnV"]);
        $stmt->bindParam(":detalleRodilloCentralCarga", $emp["detalleRodilloCentralCarga"]);
        $stmt->bindParam(":detalleRodilloLateralCarg", $emp["detalleRodilloLateralCarg"]);
        $stmt->bindParam(":detalleRodilloRetorno", $emp["detalleRodilloRetorno"]);
        $stmt->bindParam(":sistemaAlineacionCarga", $emp["sistemaAlineacionCarga"]);

        


        print_r($stmt);
        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}



/*--------------------------------------------------- FIN DE SISTEMA ALINEACION --------------------------------------------*/


/*----------------------------------------------------------- SEGURIDAD -----------------------------------------------*/

function registroSeguridad($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = "INSERT into bandaTransportadora (idRegistro, puertasInspeccion, guardaRodilloRetornoPlano, guardaTruTrainer, guardaPoleaDeflectora, guardaZonaDeTransito, guardaMotores, guardaCadenas, guardaCorreas, interruptoresDeSeguridad, sirenasDeSeguridad, guardaPoleaTensora, guardaPoleaMotrizTransportadora, , guardaPoleaColaTransportadora, guardaRodilloRetornoV)

  values(:idRegistro, :puertasInspeccion, :guardaRodilloRetornoPlano, :guardaTruTrainer, :guardaPoleaDeflectora, :guardaZonaDeTransito, :guardaMotores, :guardaCadenas, :guardaCorreas, :interruptoresDeSeguridad, :sirenasDeSeguridad, :guardaPoleaTensora, :guardaPoleaMotrizTransportadora, :guardaPoleaColaTransportadora, :guardaRodilloRetornoV) ";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':puertasInspeccion', $emp["puertasInspeccion"]);
        $stmt->bindParam(":guardaRodilloRetornoPlano", $emp["guardaRodilloRetornoPlano"]);
        $stmt->bindParam(":guardaTruTrainer", $emp["guardaTruTrainer"]);
        $stmt->bindParam(":guardaPoleaDeflectora", $emp["guardaPoleaDeflectora"]);
        $stmt->bindParam(':guardaZonaDeTransito', $emp["guardaZonaDeTransito"]);
        $stmt->bindParam(":guardaMotores", $emp["guardaMotores"]);
        $stmt->bindParam(":guardaCadenas", $emp["guardaCadenas"]);
        $stmt->bindParam(":guardaCorreas", $emp["guardaCorreas"]);
        $stmt->bindParam(":interruptoresDeSeguridad", $emp["interruptoresDeSeguridad"]);
        $stmt->bindParam(":sirenasDeSeguridad", $emp["sirenasDeSeguridad"]);
        $stmt->bindParam(":guardaPoleaTensora", $emp["guardaPoleaTensora"]);
        $stmt->bindParam(":guardaPoleaMotrizTransportadora", $emp["guardaPoleaMotrizTransportadora"]);
      
        $stmt->bindParam(":guardaPoleaColaTransportadora", $emp["guardaPoleaColaTransportadora"]);
        $stmt->bindParam(":guardaRodilloRetornoV", $emp["guardaRodilloRetornoV"]);


        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarSeguridad($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = "UPDATE bandaTransportadora set puertasInspeccion=:puertasInspeccion, guardaRodilloRetornoPlano=:guardaRodilloRetornoPlano, guardaTruTrainer=:guardaTruTrainer, guardaPoleaDeflectora=:guardaPoleaDeflectora, guardaZonaDeTransito=:guardaZonaDeTransito, guardaMotores=:guardaMotores, guardaCadenas=:guardaCadenas, guardaCorreas=:guardaCorreas, interruptoresDeSeguridad=:interruptoresDeSeguridad, sirenasDeSeguridad=:sirenasDeSeguridad, guardaPoleaTensora=:guardaPoleaTensora, guardaPoleaMotrizTransportadora=:guardaPoleaMotrizTransportadora, guardaPoleaColaTransportadora=:guardaPoleaColaTransportadora, guardaRodilloRetornoV=:guardaRodilloRetornoV

  where idRegistro=:idRegistro";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':puertasInspeccion', $emp["puertasInspeccion"]);
        $stmt->bindParam(":guardaRodilloRetornoPlano", $emp["guardaRodilloRetornoPlano"]);
        $stmt->bindParam(":guardaTruTrainer", $emp["guardaTruTrainer"]);
        $stmt->bindParam(":guardaPoleaDeflectora", $emp["guardaPoleaDeflectora"]);
        $stmt->bindParam(':guardaZonaDeTransito', $emp["guardaZonaDeTransito"]);
        $stmt->bindParam(":guardaMotores", $emp["guardaMotores"]);
        $stmt->bindParam(":guardaCadenas", $emp["guardaCadenas"]);
        $stmt->bindParam(":guardaCorreas", $emp["guardaCorreas"]);
        $stmt->bindParam(":interruptoresDeSeguridad", $emp["interruptoresDeSeguridad"]);
        $stmt->bindParam(":sirenasDeSeguridad", $emp["sirenasDeSeguridad"]);
        $stmt->bindParam(":guardaPoleaTensora", $emp["guardaPoleaTensora"]);
        $stmt->bindParam(":guardaPoleaMotrizTransportadora", $emp["guardaPoleaMotrizTransportadora"]);
        
        $stmt->bindParam(":guardaPoleaColaTransportadora", $emp["guardaPoleaColaTransportadora"]);
        $stmt->bindParam(":guardaRodilloRetornoV", $emp["guardaRodilloRetornoV"]);

        print_r($stmt);
        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}
/*--------------------------------------------------- FIN DE SISTEMA SEGURIDAD --------------------------------------------*/



/*------------------------------------------------------- POLEA MOTRIZ -----------------------------------------------------*/

function registroPoleaMotrizHorizontal($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = "INSERT into bandaTransportadora (idRegistro, diametroPoleaMotrizTransportadora, anchoPoleaMotrizTransportadora, tipoPoleaMotrizTransportadora, largoEjePoleaMotrizTransportadora, diametroEjeMotrizTransportadora, icobandasCentraEnPoleaMotrizTransportadora, anguloAmarrePoleaMotrizTransportadora, estadoRevestimientoPoleaMotrizTransportadora, tipoTransicionPoleaMotrizTransportadora, distanciaTransicionPoleaMotrizTransportadora, potenciaMotorTransportadora, guardaPoleaMotrizTransportadora)

  values(:idRegistro, :diametroPoleaMotrizTransportadora, :anchoPoleaMotrizTransportadora, :tipoPoleaMotrizTransportadora, :largoEjePoleaMotrizTransportadora, :diametroEjeMotrizTransportadora, :icobandasCentraEnPoleaMotrizTransportadora, :anguloAmarrePoleaMotrizTransportadora, :estadoRevestimientoPoleaMotrizTransportadora, :tipoTransicionPoleaMotrizTransportadora, :distanciaTransicionPoleaMotrizTransportadora, :potenciaMotorTransportadora, :guardaPoleaMotrizTransportadora) ";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':diametroPoleaMotrizTransportadora', $emp["diametroPoleaMotrizTransportadora"]);
        $stmt->bindParam(":anchoPoleaMotrizTransportadora", $emp["anchoPoleaMotrizTransportadora"]);
        $stmt->bindParam(":tipoPoleaMotrizTransportadora", $emp["tipoPoleaMotrizTransportadora"]);
        $stmt->bindParam(":largoEjePoleaMotrizTransportadora", $emp["largoEjePoleaMotrizTransportadora"]);
        $stmt->bindParam(':diametroEjeMotrizTransportadora', $emp["diametroEjeMotrizTransportadora"]);
        $stmt->bindParam(":icobandasCentraEnPoleaMotrizTransportadora", $emp["icobandasCentraEnPoleaMotrizTransportadora"]);
        $stmt->bindParam(":anguloAmarrePoleaMotrizTransportadora", $emp["anguloAmarrePoleaMotrizTransportadora"]);
        $stmt->bindParam(":estadoRevestimientoPoleaMotrizTransportadora", $emp["estadoRevestimientoPoleaMotrizTransportadora"]);
        $stmt->bindParam(":tipoTransicionPoleaMotrizTransportadora", $emp["tipoTransicionPoleaMotrizTransportadora"]);
        $stmt->bindParam(":distanciaTransicionPoleaMotrizTransportadora", $emp["distanciaTransicionPoleaMotrizTransportadora"]);
        $stmt->bindParam(":potenciaMotorTransportadora", $emp["potenciaMotorTransportadora"]);
        $stmt->bindParam(":guardaPoleaMotrizTransportadora", $emp["guardaPoleaMotrizTransportadora"]);


        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarPoleaMotrizHorizontal($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = "UPDATE bandaTransportadora set diametroPoleaMotrizTransportadora=:diametroPoleaMotrizTransportadora, anchoPoleaMotrizTransportadora=:anchoPoleaMotrizTransportadora, tipoPoleaMotrizTransportadora=:tipoPoleaMotrizTransportadora, largoEjePoleaMotrizTransportadora=:largoEjePoleaMotrizTransportadora, diametroEjeMotrizTransportadora=:diametroEjeMotrizTransportadora, icobandasCentraEnPoleaMotrizTransportadora=:icobandasCentraEnPoleaMotrizTransportadora, anguloAmarrePoleaMotrizTransportadora=:anguloAmarrePoleaMotrizTransportadora, estadoRevestimientoPoleaMotrizTransportadora=:estadoRevestimientoPoleaMotrizTransportadora, tipoTransicionPoleaMotrizTransportadora=:tipoTransicionPoleaMotrizTransportadora, distanciaTransicionPoleaMotrizTransportadora=:distanciaTransicionPoleaMotrizTransportadora, potenciaMotorTransportadora=:potenciaMotorTransportadora, guardaPoleaMotrizTransportadora=:guardaPoleaMotrizTransportadora

  where idRegistro=:idRegistro";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':diametroPoleaMotrizTransportadora', $emp["diametroPoleaMotrizTransportadora"]);
        $stmt->bindParam(":anchoPoleaMotrizTransportadora", $emp["anchoPoleaMotrizTransportadora"]);
        $stmt->bindParam(":tipoPoleaMotrizTransportadora", $emp["tipoPoleaMotrizTransportadora"]);
        $stmt->bindParam(":largoEjePoleaMotrizTransportadora", $emp["largoEjePoleaMotrizTransportadora"]);
        $stmt->bindParam(':diametroEjeMotrizTransportadora', $emp["diametroEjeMotrizTransportadora"]);
        $stmt->bindParam(":icobandasCentraEnPoleaMotrizTransportadora", $emp["icobandasCentraEnPoleaMotrizTransportadora"]);
        $stmt->bindParam(":anguloAmarrePoleaMotrizTransportadora", $emp["anguloAmarrePoleaMotrizTransportadora"]);
        $stmt->bindParam(":estadoRevestimientoPoleaMotrizTransportadora", $emp["estadoRevestimientoPoleaMotrizTransportadora"]);
        $stmt->bindParam(":tipoTransicionPoleaMotrizTransportadora", $emp["tipoTransicionPoleaMotrizTransportadora"]);
        $stmt->bindParam(":distanciaTransicionPoleaMotrizTransportadora", $emp["distanciaTransicionPoleaMotrizTransportadora"]);
        $stmt->bindParam(":potenciaMotorTransportadora", $emp["potenciaMotorTransportadora"]);
        $stmt->bindParam(":guardaPoleaMotrizTransportadora", $emp["guardaPoleaMotrizTransportadora"]);

        print_r($stmt);
        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}
/*------------------------------------------------ FIN DE POLEA MOTRIZ ----------------------------------------------------*/



/*------------------------------------------------------- POLEA COLA -----------------------------------------------------*/

function registroPoleaColaHorizontal($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = "INSERT into bandaTransportadora (idRegistro, diametroPoleaColaTransportadora, anchoPoleaColaTransportadora, tipoPoleaColaTransportadora, largoEjePoleaColaTransportadora, diametroEjePoleaColaHorizontal, icobandasCentradaPoleaColaTransportadora, anguloAmarrePoleaColaTransportadora, estadoRvtoPoleaColaTransportadora, tipoTransicionPoleaColaTransportadora, distanciaTransicionPoleaColaTransportadora, longitudTensorTornilloPoleaColaTransportadora, longitudRecorridoContrapesaPoleaColaTransportadora, guardaPoleaColaTransportadora)

  values(:idRegistro, :diametroPoleaColaTransportadora, :anchoPoleaColaTransportadora, :tipoPoleaColaTransportadora, :largoEjePoleaColaTransportadora, :diametroEjePoleaColaHorizontal, :icobandasCentradaPoleaColaTransportadora, :anguloAmarrePoleaColaTransportadora, :estadoRvtoPoleaColaTransportadora, :tipoTransicionPoleaColaTransportadora, :distanciaTransicionPoleaColaTransportadora, :longitudTensorTornilloPoleaColaTransportadora, :longitudRecorridoContrapesaPoleaColaTransportadora, :guardaPoleaColaTransportadora) ";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':diametroPoleaColaTransportadora', $emp["diametroPoleaColaTransportadora"]);
        $stmt->bindParam(":anchoPoleaColaTransportadora", $emp["anchoPoleaColaTransportadora"]);
        $stmt->bindParam(":tipoPoleaColaTransportadora", $emp["tipoPoleaColaTransportadora"]);
        $stmt->bindParam(":largoEjePoleaColaTransportadora", $emp["largoEjePoleaColaTransportadora"]);
        $stmt->bindParam(':diametroEjePoleaColaHorizontal', $emp["diametroEjePoleaColaHorizontal"]);
        $stmt->bindParam(":icobandasCentradaPoleaColaTransportadora", $emp["icobandasCentradaPoleaColaTransportadora"]);
        $stmt->bindParam(":anguloAmarrePoleaColaTransportadora", $emp["anguloAmarrePoleaColaTransportadora"]);
        $stmt->bindParam(":estadoRvtoPoleaColaTransportadora", $emp["estadoRvtoPoleaColaTransportadora"]);
        $stmt->bindParam(":tipoTransicionPoleaColaTransportadora", $emp["tipoTransicionPoleaColaTransportadora"]);
        $stmt->bindParam(":distanciaTransicionPoleaColaTransportadora", $emp["distanciaTransicionPoleaColaTransportadora"]);
        $stmt->bindParam(":longitudTensorTornilloPoleaColaTransportadora", $emp["longitudTensorTornilloPoleaColaTransportadora"]);
        $stmt->bindParam(":longitudRecorridoContrapesaPoleaColaTransportadora", $emp["longitudRecorridoContrapesaPoleaColaTransportadora"]);
        $stmt->bindParam(":guardaPoleaColaTransportadora", $emp["guardaPoleaColaTransportadora"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarPoleaColaHorizontal($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    print_r($emp);

    $sql = "UPDATE bandaTransportadora set diametroPoleaColaTransportadora=:diametroPoleaColaTransportadora, anchoPoleaColaTransportadora=:anchoPoleaColaTransportadora, tipoPoleaColaTransportadora=:tipoPoleaColaTransportadora, largoEjePoleaColaTransportadora=:largoEjePoleaColaTransportadora, diametroEjePoleaColaHorizontal=:diametroEjePoleaColaHorizontal, icobandasCentradaPoleaColaTransportadora=:icobandasCentradaPoleaColaTransportadora, anguloAmarrePoleaColaTransportadora=:anguloAmarrePoleaColaTransportadora, estadoRvtoPoleaColaTransportadora=:estadoRvtoPoleaColaTransportadora, tipoTransicionPoleaColaTransportadora=:tipoTransicionPoleaColaTransportadora, distanciaTransicionPoleaColaTransportadora=:distanciaTransicionPoleaColaTransportadora, longitudTensorTornilloPoleaColaTransportadora=:longitudTensorTornilloPoleaColaTransportadora, longitudRecorridoContrapesaPoleaColaTransportadora=:longitudRecorridoContrapesaPoleaColaTransportadora, guardaPoleaColaTransportadora=:guardaPoleaColaTransportadora

  where idRegistro=:idRegistro";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':diametroPoleaColaTransportadora', $emp["diametroPoleaColaTransportadora"]);
        $stmt->bindParam(":anchoPoleaColaTransportadora", $emp["anchoPoleaColaTransportadora"]);
        $stmt->bindParam(":tipoPoleaColaTransportadora", $emp["tipoPoleaColaTransportadora"]);
        $stmt->bindParam(":largoEjePoleaColaTransportadora", $emp["largoEjePoleaColaTransportadora"]);
        $stmt->bindParam(':diametroEjePoleaColaHorizontal', $emp["diametroEjePoleaColaHorizontal"]);
        $stmt->bindParam(":icobandasCentradaPoleaColaTransportadora", $emp["icobandasCentradaPoleaColaTransportadora"]);
        $stmt->bindParam(":anguloAmarrePoleaColaTransportadora", $emp["anguloAmarrePoleaColaTransportadora"]);
        $stmt->bindParam(":estadoRvtoPoleaColaTransportadora", $emp["estadoRvtoPoleaColaTransportadora"]);
        $stmt->bindParam(":tipoTransicionPoleaColaTransportadora", $emp["tipoTransicionPoleaColaTransportadora"]);
        $stmt->bindParam(":distanciaTransicionPoleaColaTransportadora", $emp["distanciaTransicionPoleaColaTransportadora"]);
        $stmt->bindParam(":longitudTensorTornilloPoleaColaTransportadora", $emp["longitudTensorTornilloPoleaColaTransportadora"]);
        $stmt->bindParam(":longitudRecorridoContrapesaPoleaColaTransportadora", $emp["longitudRecorridoContrapesaPoleaColaTransportadora"]);
        $stmt->bindParam(":guardaPoleaColaTransportadora", $emp["guardaPoleaColaTransportadora"]);

        print_r($stmt);
        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}
/*------------------------------------------------ FIN DE POLEA COLA ----------------------------------------------------*/



/*------------------------------------------------------- SOPORTE CARGA -----------------------------------------------------*/

function registroSoporteCarga($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    };

    // print_r($emp);

    $sql = "INSERT into bandaTransportadora (idRegistro, 
tieneRodillosImpacto, camaImpacto, camaSellado, basculaPesaje, rodilloCarga, rodilloImpacto, basculaASGCO, barraImpacto, barraDeslizamiento, espesorUHMV, anchoBarra, anguloAcanalamientoArtesa1, anguloAcanalamientoArtesa2, anguloAcanalamientoArtesa3, anguloAcanalamientoArtesa1AntesPoleaMotriz, anguloAcanalamientoArtesa2AntesPoleaMotriz, anguloAcanalamientoArtesa3AntesPoleaMotriz,integridadSoportesRodilloImpacto, materialAtrapadoEntreCortinas, materialAtrapadoEntreGuardabandas, materialAtrapadoEnBanda, integridadSoportesCamaImpacto, integridadSoporteCamaSellado, inclinacionZonaCargue, detalleRodilloCentralCarga, detalleRodilloLateralCarg, largoBarra, largoEjeRodilloCentralCarga, diametroEjeRodilloCentralCarga, largoTuboRodilloCentralCarga, largoEjeRodilloLateralCarga, diametroEjeRodilloLateralCarga, diametroRodilloLateralCarga, largoTuboRodilloLateralCarga, tipoRodilloCarga, tipoRodilloImpacto, diametroRodilloCentralCarga, anchoExternoChasisRodilloCarga, anchoInternoChasisRodilloCarga)

  values(:idRegistro, :tieneRodillosImpacto, :camaImpacto, :camaSellado, :basculaPesaje, :rodilloCarga, :rodilloImpacto, :basculaASGCO, :barraImpacto, :barraDeslizamiento, :espesorUHMV, :anchoBarra, :anguloAcanalamientoArtesa1, :anguloAcanalamientoArtesa2, :anguloAcanalamientoArtesa3, :anguloAcanalamientoArtesa1AntesPoleaMotriz, :anguloAcanalamientoArtesa2AntesPoleaMotriz, :anguloAcanalamientoArtesa3AntesPoleaMotriz,:integridadSoportesRodilloImpacto, :materialAtrapadoEntreCortinas, :materialAtrapadoEntreGuardabandas, :materialAtrapadoEnBanda, :integridadSoportesCamaImpacto, :integridadSoporteCamaSellado, :inclinacionZonaCargue, :detalleRodilloCentralCarga, :detalleRodilloLateralCarg, :largoBarra, :largoEjeRodilloCentralCarga, :diametroEjeRodilloCentralCarga, :largoTuboRodilloCentralCarga, :largoEjeRodilloLateralCarga, :diametroEjeRodilloLateralCarga, :diametroRodilloLateralCarga, :largoTuboRodilloLateralCarga, :tipoRodilloCarga, :tipoRodilloImpacto, :diametroRodilloCentralCarga, :anchoExternoChasisRodilloCarga, :anchoInternoChasisRodilloCarga)";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':tieneRodillosImpacto', $emp["tieneRodillosImpacto"]);
        $stmt->bindParam(":camaImpacto", $emp["camaImpacto"]);
        $stmt->bindParam(":camaSellado", $emp["camaSellado"]);
        $stmt->bindParam(":basculaPesaje", $emp["basculaPesaje"]);
        $stmt->bindParam(':rodilloCarga', $emp["rodilloCarga"]);
        $stmt->bindParam(":rodilloImpacto", $emp["rodilloImpacto"]);
        $stmt->bindParam(":basculaASGCO", $emp["basculaASGCO"]);
        $stmt->bindParam(":barraImpacto", $emp["barraImpacto"]);
        $stmt->bindParam(":barraDeslizamiento", $emp["barraDeslizamiento"]);
        $stmt->bindParam(":espesorUHMV", $emp["espesorUHMV"]);
        $stmt->bindParam(":anchoBarra", $emp["anchoBarra"]);
        $stmt->bindParam(":anguloAcanalamientoArtesa1", $emp["anguloAcanalamientoArtesa1"]);
        $stmt->bindParam(":anguloAcanalamientoArtesa2", $emp["anguloAcanalamientoArtesa2"]);
        $stmt->bindParam(":anguloAcanalamientoArtesa3", $emp["anguloAcanalamientoArtesa3"]);
        $stmt->bindParam(":anguloAcanalamientoArtesa1AntesPoleaMotriz", $emp["anguloAcanalamientoArtesa1AntesPoleaMotriz"]);
        $stmt->bindParam(":anguloAcanalamientoArtesa2AntesPoleaMotriz", $emp["anguloAcanalamientoArtesa2AntesPoleaMotriz"]);
        $stmt->bindParam(":anguloAcanalamientoArtesa3AntesPoleaMotriz", $emp["anguloAcanalamientoArtesa3AntesPoleaMotriz"]);
        $stmt->bindParam(":integridadSoportesRodilloImpacto", $emp["integridadSoportesRodilloImpacto"]);
        $stmt->bindParam(":materialAtrapadoEntreCortinas", $emp["materialAtrapadoEntreCortinas"]);
        $stmt->bindParam(":materialAtrapadoEntreGuardabandas", $emp["materialAtrapadoEntreGuardabandas"]);
        $stmt->bindParam(":materialAtrapadoEnBanda", $emp["materialAtrapadoEnBanda"]);
        $stmt->bindParam(":integridadSoportesCamaImpacto", $emp["integridadSoportesCamaImpacto"]);
        $stmt->bindParam(":integridadSoporteCamaSellado", $emp["integridadSoporteCamaSellado"]);
        $stmt->bindParam(":inclinacionZonaCargue", $emp["inclinacionZonaCargue"]);     
        $stmt->bindParam(":detalleRodilloCentralCarga", $emp["detalleRodilloCentralCarga"]);
        $stmt->bindParam(":detalleRodilloLateralCarg", $emp["detalleRodilloLateralCarg"]);
        $stmt->bindParam(":largoBarra", $emp["largoBarra"]);
        $stmt->bindParam(":largoEjeRodilloCentralCarga", $emp["largoEjeRodilloCentralCarga"]);
        $stmt->bindParam(":diametroEjeRodilloCentralCarga", $emp["diametroEjeRodilloCentralCarga"]);
        $stmt->bindParam(":largoTuboRodilloCentralCarga", $emp["largoTuboRodilloCentralCarga"]);
        $stmt->bindParam(":largoEjeRodilloLateralCarga", $emp["largoEjeRodilloLateralCarga"]);
        $stmt->bindParam(":diametroEjeRodilloLateralCarga", $emp["diametroEjeRodilloLateralCarga"]);
        $stmt->bindParam(":diametroRodilloLateralCarga", $emp["diametroRodilloLateralCarga"]);
        $stmt->bindParam(":largoTuboRodilloLateralCarga", $emp["largoTuboRodilloLateralCarga"]);
        $stmt->bindParam(":tipoRodilloCarga", $emp["tipoRodilloCarga"]);
        $stmt->bindParam(":tipoRodilloImpacto", $emp["tipoRodilloImpacto"]);
        $stmt->bindParam(":diametroRodilloCentralCarga", $emp["diametroRodilloCentralCarga"]);
        $stmt->bindParam(":anchoExternoChasisRodilloCarga", $emp["anchoExternoChasisRodilloCarga"]);
        $stmt->bindParam(":anchoInternoChasisRodilloCarga", $emp["anchoInternoChasisRodilloCarga"]);
        
        
        // print_r($sql);

        if($stmt->execute())
        {
          return "ok";
        }
        else
        {
          return "error";

        }


    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarSoporteCarga($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    };

    $sql = "UPDATE bandaTransportadora set tieneRodillosImpacto=:tieneRodillosImpacto, camaImpacto=:camaImpacto, camaSellado=:camaSellado, basculaPesaje=:basculaPesaje, rodilloCarga=:rodilloCarga, rodilloImpacto=:rodilloImpacto, basculaASGCO=:basculaASGCO, barraImpacto=:barraImpacto, barraDeslizamiento=:barraDeslizamiento, espesorUHMV=:espesorUHMV, anchoBarra=:anchoBarra, anguloAcanalamientoArtesa1=:anguloAcanalamientoArtesa1, anguloAcanalamientoArtesa2=:anguloAcanalamientoArtesa2, anguloAcanalamientoArtesa3=:anguloAcanalamientoArtesa3, anguloAcanalamientoArtesa1AntesPoleaMotriz=:anguloAcanalamientoArtesa1AntesPoleaMotriz, anguloAcanalamientoArtesa2AntesPoleaMotriz=:anguloAcanalamientoArtesa2AntesPoleaMotriz, anguloAcanalamientoArtesa3AntesPoleaMotriz=:anguloAcanalamientoArtesa3AntesPoleaMotriz,integridadSoportesRodilloImpacto=:integridadSoportesRodilloImpacto, materialAtrapadoEntreCortinas=:materialAtrapadoEntreCortinas, materialAtrapadoEntreGuardabandas=:materialAtrapadoEntreGuardabandas, materialAtrapadoEnBanda=:materialAtrapadoEnBanda, integridadSoportesCamaImpacto=:integridadSoportesCamaImpacto, integridadSoporteCamaSellado=:integridadSoporteCamaSellado, inclinacionZonaCargue=:inclinacionZonaCargue, detalleRodilloCentralCarga=:detalleRodilloCentralCarga, detalleRodilloLateralCarg=:detalleRodilloLateralCarg, largoBarra=:largoBarra, largoEjeRodilloCentralCarga=:largoEjeRodilloCentralCarga, diametroEjeRodilloCentralCarga=:diametroEjeRodilloCentralCarga, largoTuboRodilloCentralCarga=:largoTuboRodilloCentralCarga, largoEjeRodilloLateralCarga=:largoEjeRodilloLateralCarga, diametroEjeRodilloLateralCarga=:diametroEjeRodilloLateralCarga, diametroRodilloLateralCarga=:diametroRodilloLateralCarga, largoTuboRodilloLateralCarga=:largoTuboRodilloLateralCarga, tipoRodilloCarga=:tipoRodilloCarga, tipoRodilloImpacto=:tipoRodilloImpacto, diametroRodilloCentralCarga=:diametroRodilloCentralCarga, anchoExternoChasisRodilloCarga=:anchoExternoChasisRodilloCarga, anchoInternoChasisRodilloCarga=:anchoInternoChasisRodilloCarga

    where idRegistro=:idRegistro";
    try {

        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':tieneRodillosImpacto', $emp["tieneRodillosImpacto"]);
        $stmt->bindParam(":camaImpacto", $emp["camaImpacto"]);
        $stmt->bindParam(":camaSellado", $emp["camaSellado"]);
        $stmt->bindParam(":basculaPesaje", $emp["basculaPesaje"]);
        $stmt->bindParam(':rodilloCarga', $emp["rodilloCarga"]);
        $stmt->bindParam(":rodilloImpacto", $emp["rodilloImpacto"]);
        $stmt->bindParam(":basculaASGCO", $emp["basculaASGCO"]);
        $stmt->bindParam(":barraImpacto", $emp["barraImpacto"]);
        $stmt->bindParam(":barraDeslizamiento", $emp["barraDeslizamiento"]);
        $stmt->bindParam(":espesorUHMV", $emp["espesorUHMV"]);
        $stmt->bindParam(":anchoBarra", $emp["anchoBarra"]);
        $stmt->bindParam(":anguloAcanalamientoArtesa1", $emp["anguloAcanalamientoArtesa1"]);
        $stmt->bindParam(":anguloAcanalamientoArtesa2", $emp["anguloAcanalamientoArtesa2"]);
        $stmt->bindParam(":anguloAcanalamientoArtesa3", $emp["anguloAcanalamientoArtesa3"]);
        $stmt->bindParam(":anguloAcanalamientoArtesa1AntesPoleaMotriz", $emp["anguloAcanalamientoArtesa1AntesPoleaMotriz"]);
        $stmt->bindParam(":anguloAcanalamientoArtesa2AntesPoleaMotriz", $emp["anguloAcanalamientoArtesa2AntesPoleaMotriz"]);
        $stmt->bindParam(":anguloAcanalamientoArtesa3AntesPoleaMotriz", $emp["anguloAcanalamientoArtesa3AntesPoleaMotriz"]);
        $stmt->bindParam(":integridadSoportesRodilloImpacto", $emp["integridadSoportesRodilloImpacto"]);
        $stmt->bindParam(":materialAtrapadoEntreCortinas", $emp["materialAtrapadoEntreCortinas"]);
        $stmt->bindParam(":materialAtrapadoEntreGuardabandas", $emp["materialAtrapadoEntreGuardabandas"]);
        $stmt->bindParam(":materialAtrapadoEnBanda", $emp["materialAtrapadoEnBanda"]);
        $stmt->bindParam(":integridadSoportesCamaImpacto", $emp["integridadSoportesCamaImpacto"]);
        $stmt->bindParam(":integridadSoporteCamaSellado", $emp["integridadSoporteCamaSellado"]);
        $stmt->bindParam(":inclinacionZonaCargue", $emp["inclinacionZonaCargue"]);     
        $stmt->bindParam(":detalleRodilloCentralCarga", $emp["detalleRodilloCentralCarga"]);
        $stmt->bindParam(":detalleRodilloLateralCarg", $emp["detalleRodilloLateralCarg"]);
        $stmt->bindParam(":largoBarra", $emp["largoBarra"]);
        $stmt->bindParam(":largoEjeRodilloCentralCarga", $emp["largoEjeRodilloCentralCarga"]);
        $stmt->bindParam(":diametroEjeRodilloCentralCarga", $emp["diametroEjeRodilloCentralCarga"]);
        $stmt->bindParam(":largoTuboRodilloCentralCarga", $emp["largoTuboRodilloCentralCarga"]);
        $stmt->bindParam(":largoEjeRodilloLateralCarga", $emp["largoEjeRodilloLateralCarga"]);
        $stmt->bindParam(":diametroEjeRodilloLateralCarga", $emp["diametroEjeRodilloLateralCarga"]);
        $stmt->bindParam(":diametroRodilloLateralCarga", $emp["diametroRodilloLateralCarga"]);
        $stmt->bindParam(":largoTuboRodilloLateralCarga", $emp["largoTuboRodilloLateralCarga"]);
        $stmt->bindParam(":tipoRodilloCarga", $emp["tipoRodilloCarga"]);
        $stmt->bindParam(":tipoRodilloImpacto", $emp["tipoRodilloImpacto"]);
        $stmt->bindParam(":diametroRodilloCentralCarga", $emp["diametroRodilloCentralCarga"]);
        $stmt->bindParam(":anchoExternoChasisRodilloCarga", $emp["anchoExternoChasisRodilloCarga"]);
        $stmt->bindParam(":anchoInternoChasisRodilloCarga", $emp["anchoInternoChasisRodilloCarga"]);
        
      
        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}
/*------------------------------------------------ FIN DE SOPORTE CARGA ----------------------------------------------------*/



/*------------------------------------------------------- CONDICION CARGA -----------------------------------------------------*/

function registroCondicionCargaTransportadora($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    };

    // print_r($emp);

    $sql = "INSERT into bandaTransportadora (idRegistro, tipoRevestimientoTolvaCarga, estadoRevestimientoTolvaCarga, duracionPromedioRevestimiento, deflectores, altureCaida, longitudImpacto, material, anguloSobreCarga, ataqueQuimicoTransportadora, 
                         ataqueTemperaturaTransportadora, ataqueAceiteTransportadora, ataqueImpactoTransportadora, capacidadTransportadora, horasTrabajoPorDiaTransportadora, diasTrabajPorSemanaTransportadora, 
                         alimentacionCentradaTransportadora, abrasividadTransportadora, porcentajeFinosTransportadora, maxGranulometriaTransportadora, maxPesoTransportadora, densidadTransportadora, 
                         tempMaximaMaterialSobreBandaTransportadora, tempPromedioMaterialSobreBandaTransportadora, cajaColaDeTolva, fugaMateriales, fugaDeMaterialesEnLaColaDelChute, fugaDeMaterialesPorLosCostados, anchoChute, 
                         largoChute, alturaChute, abrazadera, cauchoGuardabandas, fugaDeMaterialParticulaALaSalidaDelChute, triSealMultiSeal, espesorGuardaBandas, anchoGuardaBandas, largoGuardaBandas, protectorGuardaBandas, 
                         cortinaAntiPolvo1, cortinaAntiPolvo2, cortinaAntiPolvo3, boquillasCanonesDeAire, ataqueAbrasivoTransportadora, tempAmbienteMaxTransportadora, tempAmbienteMinTransportadora)

  values(:idRegistro, :tipoRevestimientoTolvaCarga, :estadoRevestimientoTolvaCarga, :duracionPromedioRevestimiento, :deflectores, :altureCaida, :longitudImpacto, :material, :anguloSobreCarga, :ataqueQuimicoTransportadora, 
                         :ataqueTemperaturaTransportadora, :ataqueAceiteTransportadora, :ataqueImpactoTransportadora, :capacidadTransportadora, :horasTrabajoPorDiaTransportadora, :diasTrabajPorSemanaTransportadora, 
                         :alimentacionCentradaTransportadora, :abrasividadTransportadora, :porcentajeFinosTransportadora, :maxGranulometriaTransportadora, :maxPesoTransportadora, :densidadTransportadora, 
                         :tempMaximaMaterialSobreBandaTransportadora, :tempPromedioMaterialSobreBandaTransportadora, :cajaColaDeTolva, :fugaMateriales, :fugaDeMaterialesEnLaColaDelChute, :fugaDeMaterialesPorLosCostados, :anchoChute, 
                         :largoChute, :alturaChute, :abrazadera, :cauchoGuardabandas, :fugaDeMaterialParticulaALaSalidaDelChute, :triSealMultiSeal, :espesorGuardaBandas, :anchoGuardaBandas, :largoGuardaBandas, :protectorGuardaBandas, 
                         :cortinaAntiPolvo1, :cortinaAntiPolvo2, :cortinaAntiPolvo3, :boquillasCanonesDeAire, :ataqueAbrasivoTransportadora, :tempAmbienteMaxTransportadora, :tempAmbienteMinTransportadora)";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':tipoRevestimientoTolvaCarga', $emp["tipoRevestimientoTolvaCarga"]);
        $stmt->bindParam(":estadoRevestimientoTolvaCarga", $emp["estadoRevestimientoTolvaCarga"]);
        $stmt->bindParam(":duracionPromedioRevestimiento", $emp["duracionPromedioRevestimiento"]);
        $stmt->bindParam(":deflectores", $emp["deflectores"]);
        $stmt->bindParam(':altureCaida', $emp["altureCaida"]);
        $stmt->bindParam(":longitudImpacto", $emp["longitudImpacto"]);
        $stmt->bindParam(":material", $emp["material"]);
        $stmt->bindParam(":anguloSobreCarga", $emp["anguloSobreCarga"]);
        $stmt->bindParam(":ataqueQuimicoTransportadora", $emp["ataqueQuimicoTransportadora"]);
        $stmt->bindParam(":ataqueTemperaturaTransportadora", $emp["ataqueTemperaturaTransportadora"]);
        $stmt->bindParam(":ataqueAceiteTransportadora", $emp["ataqueAceiteTransportadora"]);
        $stmt->bindParam(":ataqueImpactoTransportadora", $emp["ataqueImpactoTransportadora"]);
        $stmt->bindParam(":capacidadTransportadora", $emp["capacidadTransportadora"]);
        $stmt->bindParam(":horasTrabajoPorDiaTransportadora", $emp["horasTrabajoPorDiaTransportadora"]);
        $stmt->bindParam(":diasTrabajPorSemanaTransportadora", $emp["diasTrabajPorSemanaTransportadora"]);
        $stmt->bindParam(":alimentacionCentradaTransportadora", $emp["alimentacionCentradaTransportadora"]);
        $stmt->bindParam(":abrasividadTransportadora", $emp["abrasividadTransportadora"]);
        $stmt->bindParam(":porcentajeFinosTransportadora", $emp["porcentajeFinosTransportadora"]);
        $stmt->bindParam(":maxGranulometriaTransportadora", $emp["maxGranulometriaTransportadora"]);
        $stmt->bindParam(":maxPesoTransportadora", $emp["maxPesoTransportadora"]);
        $stmt->bindParam(":densidadTransportadora", $emp["densidadTransportadora"]);
        $stmt->bindParam(":tempMaximaMaterialSobreBandaTransportadora", $emp["tempMaximaMaterialSobreBandaTransportadora"]);
        $stmt->bindParam(":tempPromedioMaterialSobreBandaTransportadora", $emp["tempPromedioMaterialSobreBandaTransportadora"]);
        $stmt->bindParam(":cajaColaDeTolva", $emp["cajaColaDeTolva"]);     
        $stmt->bindParam(":fugaMateriales", $emp["fugaMateriales"]);
        $stmt->bindParam(":fugaDeMaterialesEnLaColaDelChute", $emp["fugaDeMaterialesEnLaColaDelChute"]);
        $stmt->bindParam(":fugaDeMaterialesPorLosCostados", $emp["fugaDeMaterialesPorLosCostados"]);
        $stmt->bindParam(":anchoChute", $emp["anchoChute"]);
        $stmt->bindParam(":largoChute", $emp["largoChute"]);
        $stmt->bindParam(":alturaChute", $emp["alturaChute"]);
        $stmt->bindParam(":abrazadera", $emp["abrazadera"]);
        $stmt->bindParam(":cauchoGuardabandas", $emp["cauchoGuardabandas"]);
        $stmt->bindParam(":fugaDeMaterialParticulaALaSalidaDelChute", $emp["fugaDeMaterialParticulaALaSalidaDelChute"]);
        $stmt->bindParam(":triSealMultiSeal", $emp["triSealMultiSeal"]);
        $stmt->bindParam(":espesorGuardaBandas", $emp["espesorGuardaBandas"]);
        $stmt->bindParam(":anchoGuardaBandas", $emp["anchoGuardaBandas"]);
        $stmt->bindParam(":largoGuardaBandas", $emp["largoGuardaBandas"]);
        $stmt->bindParam(":protectorGuardaBandas", $emp["protectorGuardaBandas"]);
        $stmt->bindParam(":cortinaAntiPolvo1", $emp["cortinaAntiPolvo1"]);
        $stmt->bindParam(":cortinaAntiPolvo2", $emp["cortinaAntiPolvo2"]);
        $stmt->bindParam(":cortinaAntiPolvo3", $emp["cortinaAntiPolvo3"]);
        $stmt->bindParam(":boquillasCanonesDeAire", $emp["boquillasCanonesDeAire"]);
        $stmt->bindParam(":ataqueAbrasivoTransportadora", $emp["ataqueAbrasivoTransportadora"]);
        $stmt->bindParam(":tempAmbienteMaxTransportadora", $emp["tempAmbienteMaxTransportadora"]);
        $stmt->bindParam(":tempAmbienteMinTransportadora", $emp["tempAmbienteMinTransportadora"]);
        
        // print_r($sql);

        if($stmt->execute())
        {
          return "ok";
        }
        else
        {
          return "error";

        }


    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarCondicionCargaTransportadora($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    };

    $sql = "UPDATE bandaTransportadora set tipoRevestimientoTolvaCarga=:tipoRevestimientoTolvaCarga, estadoRevestimientoTolvaCarga=:estadoRevestimientoTolvaCarga, duracionPromedioRevestimiento=:duracionPromedioRevestimiento, deflectores=:deflectores, altureCaida=:altureCaida, longitudImpacto=:longitudImpacto, material=:material, anguloSobreCarga=:anguloSobreCarga, ataqueQuimicoTransportadora=:ataqueQuimicoTransportadora, ataqueTemperaturaTransportadora=:ataqueTemperaturaTransportadora, ataqueAceiteTransportadora=:ataqueAceiteTransportadora, ataqueImpactoTransportadora=:ataqueImpactoTransportadora, capacidadTransportadora=:capacidadTransportadora, horasTrabajoPorDiaTransportadora=:horasTrabajoPorDiaTransportadora, diasTrabajPorSemanaTransportadora=:diasTrabajPorSemanaTransportadora, alimentacionCentradaTransportadora=:alimentacionCentradaTransportadora, abrasividadTransportadora=:abrasividadTransportadora, porcentajeFinosTransportadora=:porcentajeFinosTransportadora, maxGranulometriaTransportadora=:maxGranulometriaTransportadora, maxPesoTransportadora=:maxPesoTransportadora, densidadTransportadora=:densidadTransportadora, tempMaximaMaterialSobreBandaTransportadora=:tempMaximaMaterialSobreBandaTransportadora, tempPromedioMaterialSobreBandaTransportadora=:tempPromedioMaterialSobreBandaTransportadora, cajaColaDeTolva=:cajaColaDeTolva, fugaMateriales=:fugaMateriales, fugaDeMaterialesEnLaColaDelChute=:fugaDeMaterialesEnLaColaDelChute, fugaDeMaterialesPorLosCostados=:fugaDeMaterialesPorLosCostados, anchoChute=:anchoChute, largoChute=:largoChute, alturaChute=:alturaChute, abrazadera=:abrazadera, cauchoGuardabandas=:cauchoGuardabandas, fugaDeMaterialParticulaALaSalidaDelChute=:fugaDeMaterialParticulaALaSalidaDelChute, triSealMultiSeal=:triSealMultiSeal, espesorGuardaBandas=:espesorGuardaBandas, anchoGuardaBandas=:anchoGuardaBandas, largoGuardaBandas=:largoGuardaBandas, protectorGuardaBandas=:protectorGuardaBandas, cortinaAntiPolvo1=:cortinaAntiPolvo1, cortinaAntiPolvo2=:cortinaAntiPolvo2, cortinaAntiPolvo3=:cortinaAntiPolvo3, boquillasCanonesDeAire=:boquillasCanonesDeAire, ataqueAbrasivoTransportadora=:ataqueAbrasivoTransportadora, tempAmbienteMaxTransportadora=:tempAmbienteMaxTransportadora, tempAmbienteMinTransportadora=:tempAmbienteMinTransportadora

    where idRegistro=:idRegistro";
    try {

        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':tipoRevestimientoTolvaCarga', $emp["tipoRevestimientoTolvaCarga"]);
        $stmt->bindParam(":estadoRevestimientoTolvaCarga", $emp["estadoRevestimientoTolvaCarga"]);
        $stmt->bindParam(":duracionPromedioRevestimiento", $emp["duracionPromedioRevestimiento"]);
        $stmt->bindParam(":deflectores", $emp["deflectores"]);
        $stmt->bindParam(':altureCaida', $emp["altureCaida"]);
        $stmt->bindParam(":longitudImpacto", $emp["longitudImpacto"]);
        $stmt->bindParam(":material", $emp["material"]);
        $stmt->bindParam(":anguloSobreCarga", $emp["anguloSobreCarga"]);
        $stmt->bindParam(":ataqueQuimicoTransportadora", $emp["ataqueQuimicoTransportadora"]);
        $stmt->bindParam(":ataqueTemperaturaTransportadora", $emp["ataqueTemperaturaTransportadora"]);
        $stmt->bindParam(":ataqueAceiteTransportadora", $emp["ataqueAceiteTransportadora"]);
        $stmt->bindParam(":ataqueImpactoTransportadora", $emp["ataqueImpactoTransportadora"]);
        $stmt->bindParam(":capacidadTransportadora", $emp["capacidadTransportadora"]);
        $stmt->bindParam(":horasTrabajoPorDiaTransportadora", $emp["horasTrabajoPorDiaTransportadora"]);
        $stmt->bindParam(":diasTrabajPorSemanaTransportadora", $emp["diasTrabajPorSemanaTransportadora"]);
        $stmt->bindParam(":alimentacionCentradaTransportadora", $emp["alimentacionCentradaTransportadora"]);
        $stmt->bindParam(":abrasividadTransportadora", $emp["abrasividadTransportadora"]);
        $stmt->bindParam(":porcentajeFinosTransportadora", $emp["porcentajeFinosTransportadora"]);
        $stmt->bindParam(":maxGranulometriaTransportadora", $emp["maxGranulometriaTransportadora"]);
        $stmt->bindParam(":maxPesoTransportadora", $emp["maxPesoTransportadora"]);
        $stmt->bindParam(":densidadTransportadora", $emp["densidadTransportadora"]);
        $stmt->bindParam(":tempMaximaMaterialSobreBandaTransportadora", $emp["tempMaximaMaterialSobreBandaTransportadora"]);
        $stmt->bindParam(":tempPromedioMaterialSobreBandaTransportadora", $emp["tempPromedioMaterialSobreBandaTransportadora"]);
        $stmt->bindParam(":cajaColaDeTolva", $emp["cajaColaDeTolva"]);     
        $stmt->bindParam(":fugaMateriales", $emp["fugaMateriales"]);
        $stmt->bindParam(":fugaDeMaterialesEnLaColaDelChute", $emp["fugaDeMaterialesEnLaColaDelChute"]);
        $stmt->bindParam(":fugaDeMaterialesPorLosCostados", $emp["fugaDeMaterialesPorLosCostados"]);
        $stmt->bindParam(":anchoChute", $emp["anchoChute"]);
        $stmt->bindParam(":largoChute", $emp["largoChute"]);
        $stmt->bindParam(":alturaChute", $emp["alturaChute"]);
        $stmt->bindParam(":abrazadera", $emp["abrazadera"]);
        $stmt->bindParam(":cauchoGuardabandas", $emp["cauchoGuardabandas"]);
        $stmt->bindParam(":fugaDeMaterialParticulaALaSalidaDelChute", $emp["fugaDeMaterialParticulaALaSalidaDelChute"]);
        $stmt->bindParam(":triSealMultiSeal", $emp["triSealMultiSeal"]);
        $stmt->bindParam(":espesorGuardaBandas", $emp["espesorGuardaBandas"]);
        $stmt->bindParam(":anchoGuardaBandas", $emp["anchoGuardaBandas"]);
        $stmt->bindParam(":largoGuardaBandas", $emp["largoGuardaBandas"]);
        $stmt->bindParam(":protectorGuardaBandas", $emp["protectorGuardaBandas"]);
        $stmt->bindParam(":cortinaAntiPolvo1", $emp["cortinaAntiPolvo1"]);
        $stmt->bindParam(":cortinaAntiPolvo2", $emp["cortinaAntiPolvo2"]);
        $stmt->bindParam(":cortinaAntiPolvo3", $emp["cortinaAntiPolvo3"]);
        $stmt->bindParam(":ataqueAbrasivoTransportadora", $emp["ataqueAbrasivoTransportadora"]);
        $stmt->bindParam(":boquillasCanonesDeAire", $emp["boquillasCanonesDeAire"]);
        $stmt->bindParam(":tempAmbienteMaxTransportadora", $emp["tempAmbienteMaxTransportadora"]);
        $stmt->bindParam(":tempAmbienteMinTransportadora", $emp["tempAmbienteMinTransportadora"]);

        
      
        if($stmt->execute())
        {
          return "ok";
        }
        else
        {
          return "error";

        }

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}
/*------------------------------------------------ FIN DE CONDICION CARGA ----------------------------------------------------*/



/*-------------------------------------------------- LIMPIADOR PRIMARIO -----------------------------------------------------*/

function registroLimpiadorPrimario($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    };

    // print_r($emp);

    $sql = "INSERT into bandaTransportadora (idRegistro, anchoEstructura, anchoTrayectoCarga, pasarelaRespectoAvanceBanda, materialAlimenticioTransportadora, materialAcidoTransportadora, materialTempEntre80y150Transportadora, materialSecoTransportadora, materialHumedoTransportadora, materialAbrasivoFinoTransportadora, materialPegajosoTransportadora, materialGrasosoAceitosoTransportadora, marcaLimpiadorPrimario, referenciaLimpiadorPrimario, anchoCuchillaLimpiadorPrimario, altoCuchillaLimpiadorPrimario, estadoCuchillaLimpiadorPrimario, estadoTensorLimpiadorPrimario, estadoTuboLimpiadorPrimario, frecuenciaRevisionCuchilla, cuchillaEnContactoConBanda)

  values(:idRegistro, :anchoEstructura, :anchoTrayectoCarga, :pasarelaRespectoAvanceBanda, :materialAlimenticioTransportadora, :materialAcidoTransportadora, :materialTempEntre80y150Transportadora, :materialSecoTransportadora, :materialHumedoTransportadora, :materialAbrasivoFinoTransportadora, :materialPegajosoTransportadora, :materialGrasosoAceitosoTransportadora, :marcaLimpiadorPrimario, :referenciaLimpiadorPrimario, :anchoCuchillaLimpiadorPrimario, :altoCuchillaLimpiadorPrimario, :estadoCuchillaLimpiadorPrimario, :estadoTensorLimpiadorPrimario, :estadoTuboLimpiadorPrimario, :frecuenciaRevisionCuchilla, :cuchillaEnContactoConBanda)";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':anchoEstructura', $emp["anchoEstructura"]);
        $stmt->bindParam(":anchoTrayectoCarga", $emp["anchoTrayectoCarga"]);
        $stmt->bindParam(":pasarelaRespectoAvanceBanda", $emp["pasarelaRespectoAvanceBanda"]);
        $stmt->bindParam(":materialAlimenticioTransportadora", $emp["materialAlimenticioTransportadora"]);
        $stmt->bindParam(':materialAcidoTransportadora', $emp["materialAcidoTransportadora"]);
        $stmt->bindParam(":materialTempEntre80y150Transportadora", $emp["materialTempEntre80y150Transportadora"]);
        $stmt->bindParam(":materialSecoTransportadora", $emp["materialSecoTransportadora"]);
        $stmt->bindParam(":materialHumedoTransportadora", $emp["materialHumedoTransportadora"]);
        $stmt->bindParam(":materialAbrasivoFinoTransportadora", $emp["materialAbrasivoFinoTransportadora"]);
        $stmt->bindParam(":materialPegajosoTransportadora", $emp["materialPegajosoTransportadora"]);
        $stmt->bindParam(":materialGrasosoAceitosoTransportadora", $emp["materialGrasosoAceitosoTransportadora"]);
        $stmt->bindParam(":marcaLimpiadorPrimario", $emp["marcaLimpiadorPrimario"]);
        $stmt->bindParam(":referenciaLimpiadorPrimario", $emp["referenciaLimpiadorPrimario"]);
        $stmt->bindParam(":anchoCuchillaLimpiadorPrimario", $emp["anchoCuchillaLimpiadorPrimario"]);
        $stmt->bindParam(":altoCuchillaLimpiadorPrimario", $emp["altoCuchillaLimpiadorPrimario"]);
        $stmt->bindParam(":estadoCuchillaLimpiadorPrimario", $emp["estadoCuchillaLimpiadorPrimario"]);
        $stmt->bindParam(":estadoTensorLimpiadorPrimario", $emp["estadoTensorLimpiadorPrimario"]);
        $stmt->bindParam(":estadoTuboLimpiadorPrimario", $emp["estadoTuboLimpiadorPrimario"]);
        $stmt->bindParam(":frecuenciaRevisionCuchilla", $emp["frecuenciaRevisionCuchilla"]);
        $stmt->bindParam(":cuchillaEnContactoConBanda", $emp["cuchillaEnContactoConBanda"]);

        
        // print_r($sql);

        if($stmt->execute())
        {
          return "ok";
        }
        else
        {
          return "error";

        }


    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarLimpiadorPrimario($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    };

    $sql = "UPDATE bandaTransportadora set anchoEstructura=:anchoEstructura, anchoTrayectoCarga=:anchoTrayectoCarga, pasarelaRespectoAvanceBanda=:pasarelaRespectoAvanceBanda, materialAlimenticioTransportadora=:materialAlimenticioTransportadora, materialAcidoTransportadora=:materialAcidoTransportadora, materialTempEntre80y150Transportadora=:materialTempEntre80y150Transportadora, materialSecoTransportadora=:materialSecoTransportadora, materialHumedoTransportadora=:materialHumedoTransportadora, materialAbrasivoFinoTransportadora=:materialAbrasivoFinoTransportadora, materialPegajosoTransportadora=:materialPegajosoTransportadora, materialGrasosoAceitosoTransportadora=:materialGrasosoAceitosoTransportadora, marcaLimpiadorPrimario=:marcaLimpiadorPrimario, referenciaLimpiadorPrimario=:referenciaLimpiadorPrimario, anchoCuchillaLimpiadorPrimario=:anchoCuchillaLimpiadorPrimario, altoCuchillaLimpiadorPrimario=:altoCuchillaLimpiadorPrimario, estadoCuchillaLimpiadorPrimario=:estadoCuchillaLimpiadorPrimario, estadoTensorLimpiadorPrimario=:estadoTensorLimpiadorPrimario, estadoTuboLimpiadorPrimario=:estadoTuboLimpiadorPrimario, frecuenciaRevisionCuchilla=:frecuenciaRevisionCuchilla, cuchillaEnContactoConBanda=:cuchillaEnContactoConBanda

    where idRegistro=:idRegistro";
    try {

        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(':anchoEstructura', $emp["anchoEstructura"]);
        $stmt->bindParam(":anchoTrayectoCarga", $emp["anchoTrayectoCarga"]);
        $stmt->bindParam(":pasarelaRespectoAvanceBanda", $emp["pasarelaRespectoAvanceBanda"]);
        $stmt->bindParam(":materialAlimenticioTransportadora", $emp["materialAlimenticioTransportadora"]);
        $stmt->bindParam(':materialAcidoTransportadora', $emp["materialAcidoTransportadora"]);
        $stmt->bindParam(":materialTempEntre80y150Transportadora", $emp["materialTempEntre80y150Transportadora"]);
        $stmt->bindParam(":materialSecoTransportadora", $emp["materialSecoTransportadora"]);
        $stmt->bindParam(":materialHumedoTransportadora", $emp["materialHumedoTransportadora"]);
        $stmt->bindParam(":materialAbrasivoFinoTransportadora", $emp["materialAbrasivoFinoTransportadora"]);
        $stmt->bindParam(":materialPegajosoTransportadora", $emp["materialPegajosoTransportadora"]);
        $stmt->bindParam(":materialGrasosoAceitosoTransportadora", $emp["materialGrasosoAceitosoTransportadora"]);
        $stmt->bindParam(":marcaLimpiadorPrimario", $emp["marcaLimpiadorPrimario"]);
        $stmt->bindParam(":referenciaLimpiadorPrimario", $emp["referenciaLimpiadorPrimario"]);
        $stmt->bindParam(":anchoCuchillaLimpiadorPrimario", $emp["anchoCuchillaLimpiadorPrimario"]);
        $stmt->bindParam(":altoCuchillaLimpiadorPrimario", $emp["altoCuchillaLimpiadorPrimario"]);
        $stmt->bindParam(":estadoCuchillaLimpiadorPrimario", $emp["estadoCuchillaLimpiadorPrimario"]);
        $stmt->bindParam(":estadoTensorLimpiadorPrimario", $emp["estadoTensorLimpiadorPrimario"]);
        $stmt->bindParam(":estadoTuboLimpiadorPrimario", $emp["estadoTuboLimpiadorPrimario"]);
        $stmt->bindParam(":frecuenciaRevisionCuchilla", $emp["frecuenciaRevisionCuchilla"]);
        $stmt->bindParam(":cuchillaEnContactoConBanda", $emp["cuchillaEnContactoConBanda"]);

        
        if($stmt->execute())
        {
          return "ok";
        }
        else
        {
          return "error";

        }

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}
/*------------------------------------------------ FIN DE LIMPIADOR PRIMARIO ---------------------------------------------*/




function observacionTransportadora($request)
{
    $emp = $request->getParams();

    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "INSERT INTO bandaTransportadora (idRegistro, observacionRegistroTransportadora) values (:idRegistro,:observacionRegistroTransportadora)";

    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':idRegistro', $emp["idRegistro"]);
        $stmt->bindParam(":observacionRegistroTransportadora", $emp["observacionRegistroTransportadora"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarObservacionTransportadora($request)
{
    $emp = $request->getParams();

    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    }

    $sql = "UPDATE bandaTransportadora set observacionRegistroTransportadora=:observacionRegistroTransportadora where idRegistro=:idRegistro";

    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(':idRegistro', $emp["idRegistro"]);
        $stmt->bindParam(":observacionRegistroTransportadora", $emp["observacionRegistroTransportadora"]);

        $stmt->execute();

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}



/*-------------------------------------------------- LIMPIADOR SECUNDARIO -----------------------------------------------------*/

function registroLimpiadorSecundario($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    };

    // print_r($emp);

    $sql = "INSERT into bandaTransportadora (idRegistro, marcaLimpiadorSecundario, referenciaLimpiadorSecundario, anchoCuchillaLimpiadorSecundario, altoCuchillaLimpiadorSecundario, estadoTensorLimpiadorSecundario, estadoCuchillaLimpiadorSecundario, estadoTuboLimpiadorSecundario, frecuenciaRevisionCuchilla1, cuchillaEnContactoConBanda1, sistemaDribbleChute, marcaLimpiadorTerciario, referenciaLimpiadorTerciario, anchoCuchillaLimpiadorTerciario, altoCuchillaLimpiadorTerciario, estadoCuchillaLimpiadorTerciario, estadoTensorLimpiadorTerciario, estadoTuboLimpiadorTerciario, frecuenciaRevisionCuchilla2, cuchillaEnContactoConBanda2)

  values(:idRegistro, :marcaLimpiadorSecundario, :referenciaLimpiadorSecundario, :anchoCuchillaLimpiadorSecundario, :altoCuchillaLimpiadorSecundario, :estadoTensorLimpiadorSecundario, :estadoCuchillaLimpiadorSecundario, :estadoTuboLimpiadorSecundario, :frecuenciaRevisionCuchilla1, :cuchillaEnContactoConBanda1, :sistemaDribbleChute, :marcaLimpiadorTerciario, :referenciaLimpiadorTerciario, :anchoCuchillaLimpiadorTerciario, :altoCuchillaLimpiadorTerciario, :estadoCuchillaLimpiadorTerciario, :estadoTensorLimpiadorTerciario, :estadoTuboLimpiadorTerciario, :frecuenciaRevisionCuchilla2, :cuchillaEnContactoConBanda2)";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(":marcaLimpiadorSecundario", $emp["marcaLimpiadorSecundario"]);
        $stmt->bindParam(":referenciaLimpiadorSecundario", $emp["referenciaLimpiadorSecundario"]);
        $stmt->bindParam(":anchoCuchillaLimpiadorSecundario", $emp["anchoCuchillaLimpiadorSecundario"]);
        $stmt->bindParam(":altoCuchillaLimpiadorSecundario", $emp["altoCuchillaLimpiadorSecundario"]);
        $stmt->bindParam(":estadoCuchillaLimpiadorSecundario", $emp["estadoCuchillaLimpiadorSecundario"]);
        $stmt->bindParam(":estadoTensorLimpiadorSecundario", $emp["estadoTensorLimpiadorSecundario"]);
        $stmt->bindParam(":estadoTuboLimpiadorSecundario", $emp["estadoTuboLimpiadorSecundario"]);
        $stmt->bindParam(":frecuenciaRevisionCuchilla1", $emp["frecuenciaRevisionCuchilla1"]);
        $stmt->bindParam(":cuchillaEnContactoConBanda1", $emp["cuchillaEnContactoConBanda1"]);
        $stmt->bindParam(":sistemaDribbleChute", $emp["sistemaDribbleChute"]);
        $stmt->bindParam(":marcaLimpiadorTerciario", $emp["marcaLimpiadorTerciario"]);
        $stmt->bindParam(":referenciaLimpiadorTerciario", $emp["referenciaLimpiadorTerciario"]);
        $stmt->bindParam(":anchoCuchillaLimpiadorTerciario", $emp["anchoCuchillaLimpiadorTerciario"]);
        $stmt->bindParam(":altoCuchillaLimpiadorTerciario", $emp["altoCuchillaLimpiadorTerciario"]);
        $stmt->bindParam(":estadoCuchillaLimpiadorTerciario", $emp["estadoCuchillaLimpiadorTerciario"]);
        $stmt->bindParam(":estadoTensorLimpiadorTerciario", $emp["estadoTensorLimpiadorTerciario"]);
        $stmt->bindParam(":estadoTuboLimpiadorTerciario", $emp["estadoTuboLimpiadorTerciario"]);
        $stmt->bindParam(":frecuenciaRevisionCuchilla2", $emp["frecuenciaRevisionCuchilla2"]);
        $stmt->bindParam(":cuchillaEnContactoConBanda2", $emp["cuchillaEnContactoConBanda2"]);

        
        // print_r($sql);

        if($stmt->execute())
        {
          return "ok";
        }
        else
        {
          return "error";

        }


    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarLimpiadorSecundaro($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    };

    $sql = "UPDATE bandaTransportadora set marcaLimpiadorSecundario=:marcaLimpiadorSecundario, referenciaLimpiadorSecundario=:referenciaLimpiadorSecundario, anchoCuchillaLimpiadorSecundario=:anchoCuchillaLimpiadorSecundario, altoCuchillaLimpiadorSecundario=:altoCuchillaLimpiadorSecundario, estadoTensorLimpiadorSecundario=:estadoTensorLimpiadorSecundario, estadoCuchillaLimpiadorSecundario=:estadoCuchillaLimpiadorSecundario, estadoTuboLimpiadorSecundario=:estadoTuboLimpiadorSecundario, frecuenciaRevisionCuchilla1=:frecuenciaRevisionCuchilla1, cuchillaEnContactoConBanda1=:cuchillaEnContactoConBanda1, sistemaDribbleChute=:sistemaDribbleChute, marcaLimpiadorTerciario=:marcaLimpiadorTerciario, referenciaLimpiadorTerciario=:referenciaLimpiadorTerciario, anchoCuchillaLimpiadorTerciario=:anchoCuchillaLimpiadorTerciario, altoCuchillaLimpiadorTerciario=:altoCuchillaLimpiadorTerciario, estadoCuchillaLimpiadorTerciario=:estadoCuchillaLimpiadorTerciario, estadoTensorLimpiadorTerciario=:estadoTensorLimpiadorTerciario, estadoTuboLimpiadorTerciario=:estadoTuboLimpiadorTerciario, frecuenciaRevisionCuchilla2=:frecuenciaRevisionCuchilla2, cuchillaEnContactoConBanda2=:cuchillaEnContactoConBanda2

    where idRegistro=:idRegistro";
    try {

        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(":marcaLimpiadorSecundario", $emp["marcaLimpiadorSecundario"]);
        $stmt->bindParam(":referenciaLimpiadorSecundario", $emp["referenciaLimpiadorSecundario"]);
        $stmt->bindParam(":anchoCuchillaLimpiadorSecundario", $emp["anchoCuchillaLimpiadorSecundario"]);
        $stmt->bindParam(":altoCuchillaLimpiadorSecundario", $emp["altoCuchillaLimpiadorSecundario"]);
        $stmt->bindParam(":estadoCuchillaLimpiadorSecundario", $emp["estadoCuchillaLimpiadorSecundario"]);
        $stmt->bindParam(":estadoTensorLimpiadorSecundario", $emp["estadoTensorLimpiadorSecundario"]);
        $stmt->bindParam(":estadoTuboLimpiadorSecundario", $emp["estadoTuboLimpiadorSecundario"]);
        $stmt->bindParam(":frecuenciaRevisionCuchilla1", $emp["frecuenciaRevisionCuchilla1"]);
        $stmt->bindParam(":cuchillaEnContactoConBanda1", $emp["cuchillaEnContactoConBanda1"]);
        $stmt->bindParam(":sistemaDribbleChute", $emp["sistemaDribbleChute"]);
        $stmt->bindParam(":marcaLimpiadorTerciario", $emp["marcaLimpiadorTerciario"]);
        $stmt->bindParam(":referenciaLimpiadorTerciario", $emp["referenciaLimpiadorTerciario"]);
        $stmt->bindParam(":anchoCuchillaLimpiadorTerciario", $emp["anchoCuchillaLimpiadorTerciario"]);
        $stmt->bindParam(":altoCuchillaLimpiadorTerciario", $emp["altoCuchillaLimpiadorTerciario"]);
        $stmt->bindParam(":estadoCuchillaLimpiadorTerciario", $emp["estadoCuchillaLimpiadorTerciario"]);
        $stmt->bindParam(":estadoTensorLimpiadorTerciario", $emp["estadoTensorLimpiadorTerciario"]);
        $stmt->bindParam(":estadoTuboLimpiadorTerciario", $emp["estadoTuboLimpiadorTerciario"]);
        $stmt->bindParam(":frecuenciaRevisionCuchilla2", $emp["frecuenciaRevisionCuchilla2"]);
        $stmt->bindParam(":cuchillaEnContactoConBanda2", $emp["cuchillaEnContactoConBanda2"]);

        
        if($stmt->execute())
        {
          return "ok";
        }
        else
        {
          return "error";

        }

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}
/*------------------------------------------------ FIN DE LLIMPIADOR SECUNDARIO ---------------------------------------------*/




/*-------------------------------------------------- POLEA AMARRE -----------------------------------------------------*/

function registroPoleaAmarre($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    };

    // print_r($emp);

    $sql = "INSERT into bandaTransportadora (idRegistro, diametroPoleaAmarrePoleaMotriz, anchoPoleaAmarrePoleaMotriz, tipoPoleaAmarrePoleaMotriz, largoEjePoleaAmarrePoleaMotriz, diametroEjePoleaAmarrePoleaMotriz, icobandasCentradaPoleaAmarrePoleaMotriz, estadoRevestimientoPoleaAmarrePoleaMotriz, dimetroPoleaAmarrePoleaCola, anchoPoleaAmarrePoleaCola, tipoPoleaAmarrePoleaCola, largoEjePoleaAmarrePoleaCola, diametroEjePoleaAmarrePoleaCola, icobandasCentradaPoleaAmarrePoleaCola, estadoRevestimientoPoleaAmarrePoleaCola)

  values(:idRegistro, :diametroPoleaAmarrePoleaMotriz, :anchoPoleaAmarrePoleaMotriz, :tipoPoleaAmarrePoleaMotriz, :largoEjePoleaAmarrePoleaMotriz, :diametroEjePoleaAmarrePoleaMotriz, :icobandasCentradaPoleaAmarrePoleaMotriz, :estadoRevestimientoPoleaAmarrePoleaMotriz, :dimetroPoleaAmarrePoleaCola, :anchoPoleaAmarrePoleaCola, :tipoPoleaAmarrePoleaCola, :largoEjePoleaAmarrePoleaCola, :diametroEjePoleaAmarrePoleaCola, :icobandasCentradaPoleaAmarrePoleaCola, :estadoRevestimientoPoleaAmarrePoleaCola)";
    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(":diametroPoleaAmarrePoleaMotriz", $emp["diametroPoleaAmarrePoleaMotriz"]);
        $stmt->bindParam(":tipoPoleaAmarrePoleaMotriz", $emp["tipoPoleaAmarrePoleaMotriz"]);
        $stmt->bindParam(":largoEjePoleaAmarrePoleaMotriz", $emp["largoEjePoleaAmarrePoleaMotriz"]);
        $stmt->bindParam(":diametroEjePoleaAmarrePoleaMotriz", $emp["diametroEjePoleaAmarrePoleaMotriz"]);
        $stmt->bindParam(":icobandasCentradaPoleaAmarrePoleaMotriz", $emp["icobandasCentradaPoleaAmarrePoleaMotriz"]);
        $stmt->bindParam(":estadoRevestimientoPoleaAmarrePoleaMotriz", $emp["estadoRevestimientoPoleaAmarrePoleaMotriz"]);
        $stmt->bindParam(":dimetroPoleaAmarrePoleaCola", $emp["dimetroPoleaAmarrePoleaCola"]);
        $stmt->bindParam(":anchoPoleaAmarrePoleaCola", $emp["anchoPoleaAmarrePoleaCola"]);
        $stmt->bindParam(":tipoPoleaAmarrePoleaCola", $emp["tipoPoleaAmarrePoleaCola"]);
        $stmt->bindParam(":largoEjePoleaAmarrePoleaCola", $emp["largoEjePoleaAmarrePoleaCola"]);
        $stmt->bindParam(":diametroEjePoleaAmarrePoleaCola", $emp["diametroEjePoleaAmarrePoleaCola"]);
        $stmt->bindParam(":icobandasCentradaPoleaAmarrePoleaCola", $emp["icobandasCentradaPoleaAmarrePoleaCola"]);
        $stmt->bindParam(":estadoRevestimientoPoleaAmarrePoleaCola", $emp["estadoRevestimientoPoleaAmarrePoleaCola"]);
        $stmt->bindParam(":anchoPoleaAmarrePoleaMotriz", $emp["anchoPoleaAmarrePoleaMotriz"]);
        

        
        // print_r($sql);

        if($stmt->execute())
        {
          return "ok";
        }
        else
        {
          return "error";

        }


    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarPoleaAmarre($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    };

    $sql = "UPDATE bandaTransportadora set diametroPoleaAmarrePoleaMotriz=:diametroPoleaAmarrePoleaMotriz, anchoPoleaAmarrePoleaMotriz=:anchoPoleaAmarrePoleaMotriz, tipoPoleaAmarrePoleaMotriz=:tipoPoleaAmarrePoleaMotriz, largoEjePoleaAmarrePoleaMotriz=:largoEjePoleaAmarrePoleaMotriz, diametroEjePoleaAmarrePoleaMotriz=:diametroEjePoleaAmarrePoleaMotriz, icobandasCentradaPoleaAmarrePoleaMotriz=:icobandasCentradaPoleaAmarrePoleaMotriz, estadoRevestimientoPoleaAmarrePoleaMotriz=:estadoRevestimientoPoleaAmarrePoleaMotriz, dimetroPoleaAmarrePoleaCola=:dimetroPoleaAmarrePoleaCola, anchoPoleaAmarrePoleaCola=:anchoPoleaAmarrePoleaCola, tipoPoleaAmarrePoleaCola=:tipoPoleaAmarrePoleaCola, largoEjePoleaAmarrePoleaCola=:largoEjePoleaAmarrePoleaCola, diametroEjePoleaAmarrePoleaCola=:diametroEjePoleaAmarrePoleaCola, icobandasCentradaPoleaAmarrePoleaCola=:icobandasCentradaPoleaAmarrePoleaCola, estadoRevestimientoPoleaAmarrePoleaCola=:estadoRevestimientoPoleaAmarrePoleaCola

    where idRegistro=:idRegistro";
    try {

        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(":diametroPoleaAmarrePoleaMotriz", $emp["diametroPoleaAmarrePoleaMotriz"]);
        $stmt->bindParam(":tipoPoleaAmarrePoleaMotriz", $emp["tipoPoleaAmarrePoleaMotriz"]);
        $stmt->bindParam(":largoEjePoleaAmarrePoleaMotriz", $emp["largoEjePoleaAmarrePoleaMotriz"]);
        $stmt->bindParam(":diametroEjePoleaAmarrePoleaMotriz", $emp["diametroEjePoleaAmarrePoleaMotriz"]);
        $stmt->bindParam(":icobandasCentradaPoleaAmarrePoleaMotriz", $emp["icobandasCentradaPoleaAmarrePoleaMotriz"]);
        $stmt->bindParam(":estadoRevestimientoPoleaAmarrePoleaMotriz", $emp["estadoRevestimientoPoleaAmarrePoleaMotriz"]);
        $stmt->bindParam(":dimetroPoleaAmarrePoleaCola", $emp["dimetroPoleaAmarrePoleaCola"]);
        $stmt->bindParam(":anchoPoleaAmarrePoleaCola", $emp["anchoPoleaAmarrePoleaCola"]);
        $stmt->bindParam(":tipoPoleaAmarrePoleaCola", $emp["tipoPoleaAmarrePoleaCola"]);
        $stmt->bindParam(":largoEjePoleaAmarrePoleaCola", $emp["largoEjePoleaAmarrePoleaCola"]);
        $stmt->bindParam(":diametroEjePoleaAmarrePoleaCola", $emp["diametroEjePoleaAmarrePoleaCola"]);
        $stmt->bindParam(":icobandasCentradaPoleaAmarrePoleaCola", $emp["icobandasCentradaPoleaAmarrePoleaCola"]);
        $stmt->bindParam(":estadoRevestimientoPoleaAmarrePoleaCola", $emp["estadoRevestimientoPoleaAmarrePoleaCola"]);
        $stmt->bindParam(":anchoPoleaAmarrePoleaMotriz", $emp["anchoPoleaAmarrePoleaMotriz"]);

        
        if($stmt->execute())
        {
          return "ok";
        }
        else
        {
          return "error";

        }

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}
/*------------------------------------------------ FIN DE POLEA AMARRE ---------------------------------------------*/



/*-------------------------------------------------- RODILLO CARGA -----------------------------------------------------*/

function registroRodilloCarga($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    };

    // print_r($emp);

    $sql = "INSERT into bandaTransportadora (idRegistro, largoEjeRodilloCentralCarga, diametroEjeRodilloCentralCarga, largoTuboRodilloCentralCarga, largoEjeRodilloLateralCarga, diametroEjeRodilloLateralCarga, diametroRodilloLateralCarga, largoTuboRodilloLateralCarga, tipoRodilloCarga, distanciaEntreArtesasCarga, anchoInternoChasisRodilloCarga, anchoExternoChasisRodilloCarga, anguloAcanalamientoArtesaCArga, detalleRodilloCentralCarga, detalleRodilloLateralCarg, diametroRodilloCentralCarga)

  values(:idRegistro, :largoEjeRodilloCentralCarga, :diametroEjeRodilloCentralCarga, :largoTuboRodilloCentralCarga, :largoEjeRodilloLateralCarga, :diametroEjeRodilloLateralCarga, :diametroRodilloLateralCarga, :largoTuboRodilloLateralCarga, :tipoRodilloCarga, :distanciaEntreArtesasCarga, :anchoInternoChasisRodilloCarga, :anchoExternoChasisRodilloCarga, :anguloAcanalamientoArtesaCArga, :detalleRodilloCentralCarga, :detalleRodilloLateralCarg, :diametroRodilloCentralCarga)";

    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(":largoEjeRodilloCentralCarga", $emp["largoEjeRodilloCentralCarga"]);
        $stmt->bindParam(":diametroEjeRodilloCentralCarga", $emp["diametroEjeRodilloCentralCarga"]);
        $stmt->bindParam(":largoTuboRodilloCentralCarga", $emp["largoTuboRodilloCentralCarga"]);
        $stmt->bindParam(":largoEjeRodilloLateralCarga", $emp["largoEjeRodilloLateralCarga"]);
        $stmt->bindParam(":diametroEjeRodilloLateralCarga", $emp["diametroEjeRodilloLateralCarga"]);
        $stmt->bindParam(":diametroRodilloLateralCarga", $emp["diametroRodilloLateralCarga"]);

        $stmt->bindParam(":largoTuboRodilloLateralCarga", $emp["largoTuboRodilloLateralCarga"]);
        $stmt->bindParam(":tipoRodilloCarga", $emp["tipoRodilloCarga"]);
        $stmt->bindParam(":distanciaEntreArtesasCarga", $emp["distanciaEntreArtesasCarga"]);
        $stmt->bindParam(":anchoInternoChasisRodilloCarga", $emp["anchoInternoChasisRodilloCarga"]);
        $stmt->bindParam(":anchoExternoChasisRodilloCarga", $emp["anchoExternoChasisRodilloCarga"]);
        $stmt->bindParam(":anguloAcanalamientoArtesaCArga", $emp["anguloAcanalamientoArtesaCArga"]);
        $stmt->bindParam(":detalleRodilloCentralCarga", $emp["detalleRodilloCentralCarga"]);
        $stmt->bindParam(":detalleRodilloLateralCarg", $emp["detalleRodilloLateralCarg"]);
        $stmt->bindParam(":diametroRodilloCentralCarga", $emp["diametroRodilloCentralCarga"]);
        

        
        // print_r($sql);

        if($stmt->execute())
        {
          return "ok";
        }
        else
        {
          return "error";

        }


    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarRodilloCarga($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    };

    $sql = "UPDATE bandaTransportadora set largoEjeRodilloCentralCarga=:largoEjeRodilloCentralCarga, diametroEjeRodilloCentralCarga=:diametroEjeRodilloCentralCarga, largoTuboRodilloCentralCarga=:largoTuboRodilloCentralCarga, largoEjeRodilloLateralCarga=:largoEjeRodilloLateralCarga, diametroEjeRodilloLateralCarga=:diametroEjeRodilloLateralCarga, diametroRodilloLateralCarga=:diametroRodilloLateralCarga, largoTuboRodilloLateralCarga=:largoTuboRodilloLateralCarga, tipoRodilloCarga=:tipoRodilloCarga, distanciaEntreArtesasCarga=:distanciaEntreArtesasCarga, anchoInternoChasisRodilloCarga=:anchoInternoChasisRodilloCarga, anchoExternoChasisRodilloCarga=:anchoExternoChasisRodilloCarga, anguloAcanalamientoArtesaCArga=:anguloAcanalamientoArtesaCArga, detalleRodilloCentralCarga=:detalleRodilloCentralCarga, detalleRodilloLateralCarg=:detalleRodilloLateralCarg, diametroRodilloCentralCarga=:diametroRodilloCentralCarga

    where idRegistro=:idRegistro";
    try {

        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(":largoEjeRodilloCentralCarga", $emp["largoEjeRodilloCentralCarga"]);
        $stmt->bindParam(":diametroEjeRodilloCentralCarga", $emp["diametroEjeRodilloCentralCarga"]);
        $stmt->bindParam(":largoTuboRodilloCentralCarga", $emp["largoTuboRodilloCentralCarga"]);
        $stmt->bindParam(":largoEjeRodilloLateralCarga", $emp["largoEjeRodilloLateralCarga"]);
        $stmt->bindParam(":diametroEjeRodilloLateralCarga", $emp["diametroEjeRodilloLateralCarga"]);
        $stmt->bindParam(":diametroRodilloLateralCarga", $emp["diametroRodilloLateralCarga"]);
        $stmt->bindParam(":largoTuboRodilloLateralCarga", $emp["largoTuboRodilloLateralCarga"]);
        $stmt->bindParam(":tipoRodilloCarga", $emp["tipoRodilloCarga"]);
        $stmt->bindParam(":distanciaEntreArtesasCarga", $emp["distanciaEntreArtesasCarga"]);
        $stmt->bindParam(":anchoInternoChasisRodilloCarga", $emp["anchoInternoChasisRodilloCarga"]);
        $stmt->bindParam(":anchoExternoChasisRodilloCarga", $emp["anchoExternoChasisRodilloCarga"]);
        $stmt->bindParam(":anguloAcanalamientoArtesaCArga", $emp["anguloAcanalamientoArtesaCArga"]);
        $stmt->bindParam(":detalleRodilloCentralCarga", $emp["detalleRodilloCentralCarga"]);
        $stmt->bindParam(":detalleRodilloLateralCarg", $emp["detalleRodilloLateralCarg"]);
        $stmt->bindParam(":diametroRodilloCentralCarga", $emp["diametroRodilloCentralCarga"]);

        
        if($stmt->execute())
        {
          return "ok";
        }
        else
        {
          return "error";

        }

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}
/*------------------------------------------------ FIN DE RODILLO CARGA ---------------------------------------------*/


/*-------------------------------------------------- RODILLO RETORNO -----------------------------------------------------*/

function registroRodilloRetorno($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    };

    // print_r($emp);

    $sql = "INSERT into bandaTransportadora (idRegistro, estadoRodilloRetorno, largoEjeRodilloRetorno, diametroEjeRodilloRetorno, diametroRodilloRetorno, largoTuboRodilloRetorno, tipoRodilloRetorno, distanciaEntreRodillosRetorno, 
                         anchoInternoChasisRetorno, anchoExternoChasisRetorno, detalleRodilloRetorno)

  values(:idRegistro, :estadoRodilloRetorno, :largoEjeRodilloRetorno, :diametroEjeRodilloRetorno, :diametroRodilloRetorno, :largoTuboRodilloRetorno, :tipoRodilloRetorno, :distanciaEntreRodillosRetorno, 
                         :anchoInternoChasisRetorno, :anchoExternoChasisRetorno, :detalleRodilloRetorno)";

    try {
        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(":estadoRodilloRetorno", $emp["estadoRodilloRetorno"]);
        $stmt->bindParam(":largoEjeRodilloRetorno", $emp["largoEjeRodilloRetorno"]);
        $stmt->bindParam(":diametroEjeRodilloRetorno", $emp["diametroEjeRodilloRetorno"]);
        $stmt->bindParam(":diametroRodilloRetorno", $emp["diametroRodilloRetorno"]);
        $stmt->bindParam(":largoTuboRodilloRetorno", $emp["largoTuboRodilloRetorno"]);
        $stmt->bindParam(":tipoRodilloRetorno", $emp["tipoRodilloRetorno"]);
        $stmt->bindParam(":distanciaEntreRodillosRetorno", $emp["distanciaEntreRodillosRetorno"]);
        $stmt->bindParam(":anchoInternoChasisRetorno", $emp["anchoInternoChasisRetorno"]);
        $stmt->bindParam(":anchoExternoChasisRetorno", $emp["anchoExternoChasisRetorno"]);
        $stmt->bindParam(":detalleRodilloRetorno", $emp["detalleRodilloRetorno"]);
        
        
        // print_r($sql);

        if($stmt->execute())
        {
          return "ok";
        }
        else
        {
          return "error";

        }


    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}

function actualizarRodilloRetorno($request)
{
    $emp = $request->getParams();
    foreach ($emp as $key => $value) {
        if (empty($emp[$key])) {
            $emp[$key] = null;
        }
    };

    $sql = "UPDATE bandaTransportadora set estadoRodilloRetorno=:estadoRodilloRetorno, largoEjeRodilloRetorno=:largoEjeRodilloRetorno, diametroEjeRodilloRetorno=:diametroEjeRodilloRetorno, diametroRodilloRetorno=:diametroRodilloRetorno, largoTuboRodilloRetorno=:largoTuboRodilloRetorno, tipoRodilloRetorno=:tipoRodilloRetorno, distanciaEntreRodillosRetorno=:distanciaEntreRodillosRetorno, 
                         anchoInternoChasisRetorno=:anchoInternoChasisRetorno, anchoExternoChasisRetorno=:anchoExternoChasisRetorno, detalleRodilloRetorno=:detalleRodilloRetorno

    where idRegistro=:idRegistro";
    try {

        $db   = getConnection();
        $stmt = $db->prepare($sql);
        $stmt->bindParam(":idRegistro", $emp["idRegistro"]);
        $stmt->bindParam(":estadoRodilloRetorno", $emp["estadoRodilloRetorno"]);
        $stmt->bindParam(":largoEjeRodilloRetorno", $emp["largoEjeRodilloRetorno"]);
        $stmt->bindParam(":diametroEjeRodilloRetorno", $emp["diametroEjeRodilloRetorno"]);
        $stmt->bindParam(":diametroRodilloRetorno", $emp["diametroRodilloRetorno"]);
        $stmt->bindParam(":largoTuboRodilloRetorno", $emp["largoTuboRodilloRetorno"]);
        $stmt->bindParam(":tipoRodilloRetorno", $emp["tipoRodilloRetorno"]);
        $stmt->bindParam(":distanciaEntreRodillosRetorno", $emp["distanciaEntreRodillosRetorno"]);
        $stmt->bindParam(":anchoInternoChasisRetorno", $emp["anchoInternoChasisRetorno"]);
        $stmt->bindParam(":anchoExternoChasisRetorno", $emp["anchoExternoChasisRetorno"]);
        $stmt->bindParam(":detalleRodilloRetorno", $emp["detalleRodilloRetorno"]);

        
        if($stmt->execute())
        {
          return "ok";
        }
        else
        {
          return "error";

        }

    } catch (PDOException $e) {
        echo '{"error":{"text":' . $e->getMessage() . '}}';
    }
}
/*------------------------------------------------ FIN DE RODILLO RETORNO ---------------------------------------------*/

/*=================================================================================================================================
=                                                        FIN BANDA HORIZONTAL                                           =
==================================================================================================================================*/

function utf8($arreglo)
{
    $arreglo2;
    foreach ($arreglo as $key => $value) {
        $arreglo2[$key] = utf8_encode($value);
    }
    return $arreglo2;
}

require __DIR__ . '/../vendor/autoload.php';

session_start();

// Instantiate the app
$settings = require __DIR__ . '/../src/settings.php';
$app      = new \Slim\App($settings);

// Set up dependencies
require __DIR__ . '/../src/dependencies.php';

// Register middleware
require __DIR__ . '/../src/middleware.php';

// Register routes
require __DIR__ . '/../src/routes.php';

// Run app
$app->run();
