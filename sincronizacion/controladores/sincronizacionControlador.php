<?php

class ControladorSincronizacion
{
    public function ctrSincronizarClientes()
    {


        $tabla = "clientes";
        // var_dump("VARIABLE ".$_POST["sincronizar"]);
        $respuestaHost  = ModeloSincronizacion::mdlTraerClientesHost($tabla);
        $respuestaLocal = ModeloSincronizacion::mdlTraerClientesLocal($tabla);
        //  var_dump(count($respuestaHost));
        // var_dump(count($respuestaLocal));
        $validacion   = 0;
        $nitDiferente = "";
        if ($respuestaHost == $respuestaLocal) 
        {

            $this->ctrSincronizarPlantas();
        } else {
                for ($i = 0; $i < count($respuestaHost); $i++) {
                    $validacion = 0;
                    for ($x = 0; $x < count($respuestaLocal); $x++) {
                        if ($respuestaHost[$i]["nit"] == $respuestaLocal[$x]["nit"]) {
                            $validacion = 1;
                            $x          = count($respuestaLocal) + 1;
                        }
                    }
                    if ($validacion != 1) {
                        $tabla = "clientes";
                        $datos = array(
                            "nit" => $respuestaHost[$i]["nit"],
                            "nameunido"          => $respuestaHost[$i]["nameunido"]
                        );
                        $respuesta = ModeloSincronizacion::mdlCrearCliente($tabla, $datos);
                    }
                }

                for ($i = 0; $i < count($respuestaLocal); $i++) {
                    $validacion = 0;
                    for ($x = 0; $x < count($respuestaHost); $x++) {
                        if ($respuestaLocal[$i]["nit"] == $respuestaHost[$x]["nit"]) {
                            $validacion = 1;
                            $x          = count($respuestaHost) + 1;
                        }
                    }
                    if ($validacion != 1) {
                        $tabla = "clientes";
                        $datos = array(
                            "nit" => $respuestaLocal[$i]["nit"],
                            "dv" => $respuestaLocal[$i]["dv"],
                            "tipocontrib" => $respuestaLocal[$i]["tipocontrib"],
                            "tipodoccli" => $respuestaLocal[$i]["tipodoccli"],
                            "noidentifcli" => $respuestaLocal[$i]["noidentifcli"],
                            "codciudexpdoccli" => $respuestaLocal[$i]["codciudexpdoccli"],
                            "apellido1cli" => $respuestaLocal[$i]["apellido1cli"],
                            "apellido2cli" => $respuestaLocal[$i]["apellido2cli"],
                            "nombre1cli" => $respuestaLocal[$i]["nombre1cli"],
                            "nombre2cli" => $respuestaLocal[$i]["nombre2cli"],
                            "razonsoc" => $respuestaLocal[$i]["razonsoc"],
                            "codmcipiocli" => $respuestaLocal[$i]["codmcipiocli"],
                            "emailcli" => $respuestaLocal[$i]["emailcli"],
                            "dirppalcli" => $respuestaLocal[$i]["dirppalcli"],
                            "pagweb" => $respuestaLocal[$i]["pagweb"],
                            "telef1cli" => $respuestaLocal[$i]["telef1cli"],
                            "telef2cli" => $respuestaLocal[$i]["telef2cli"],
                            "celular" => $respuestaLocal[$i]["celular"],
                            "celular2" => $respuestaLocal[$i]["celular2"],
                            "activciuuppal" => $respuestaLocal[$i]["activciuuppal"],
                            "activciuusec" => $respuestaLocal[$i]["activciuusec"],
                            "nameunido" => $respuestaLocal[$i]["nameunido"],
                            "fechacreac" => $respuestaLocal[$i]["fechacreac"],
                            "fechabaja" => $respuestaLocal[$i]["fechabaja"],
                            "debaja" => $respuestaLocal[$i]["debaja"],
                            "verrut" => $respuestaLocal[$i]["verrut"],
                            "codformpagcli" => $respuestaLocal[$i]["codformpagcli"],
                            "regimeniva" => $respuestaLocal[$i]["regimeniva"],
                            "grancontrib" => $respuestaLocal[$i]["grancontrib"],
                            "Autoreten" => $respuestaLocal[$i]["Autoreten"],
                            "fecharut" => $respuestaLocal[$i]["fecharut"],
                            "matriculamercantil" => $respuestaLocal[$i]["matriculamercantil"],
                            "barriocc" => $respuestaLocal[$i]["barriocc"],
                            "ciudadcc" => $respuestaLocal[$i]["ciudadcc"],
                            "zonapostalcc" => $respuestaLocal[$i]["zonapostalcc"],
                            "departamentocc" => $respuestaLocal[$i]["departamentocc"],
                            "direccioncc" => $respuestaLocal[$i]["direccioncc"],
                            "codigopaiscc" => $respuestaLocal[$i]["codigopaiscc"],
                            "paiscamaracomerciocc" => $respuestaLocal[$i]["paiscamaracomerciocc"],
                            "casilla53rut" => $respuestaLocal[$i]["casilla53rut"],
                            "casilla54rut" => $respuestaLocal[$i]["casilla54rut"]
                        );
                        $respuesta = ModeloSincronizacion::mdlCrearClienteHost($tabla, $datos);
                    }
                }

                $this->ctrSincronizarPlantas();
            }
    }

    public function ctrSincronizarPlantas()
    {

        $tabla = "plantas";

        $respuestaHost = ModeloSincronizacion::mdlTraerPlantasHost($tabla);
        // echo '<pre>'; print_r($respuestaHost); echo '</pre>';
        $respuestaLocal = ModeloSincronizacion::mdlTraerPlantasLocal($tabla);
        // echo '<pre>'; print_r(count($respuestaHost)); echo '</pre>';
        $validacion   = 0;
        $conteo       = 0;
        $nitDiferente = "";
        if ($respuestaHost == $respuestaLocal) {

            $this->sincronizarTransportadores();
        } else {
            for ($i = 0; $i < count($respuestaHost); $i++) {
                $validacion = 0;
                for ($x = 0; $x < count($respuestaLocal); $x++) {
                    $identificadorPlantaHost  = $respuestaHost[$i]["agenteplanta"] . $respuestaHost[$i]["nitplanta"] . $respuestaHost[$i]["ciudmciapl"];
                    $identificadorPlantaLocal = $respuestaLocal[$x]["agenteplanta"] . $respuestaLocal[$x]["nitplanta"] . $respuestaLocal[$x]["ciudmciapl"];

                    if ($identificadorPlantaLocal == $identificadorPlantaHost) {
                        $validacion = 1;
                        $x          = count($respuestaLocal) + 1;
                    }
                }
                if ($validacion != 1) {
                    $tabla = "plantas";
                    $datos = array(
                        "nameplanta" => $respuestaHost[$i]["nameplanta"],
                        "agenteplanta"              => $respuestaHost[$i]["agenteplanta"],
                        "nitplanta"                 => $respuestaHost[$i]["nitplanta"],
                        "dirmciapl"                 => $respuestaHost[$i]["dirmciapl"],
                        "ciudmciapl"                => $respuestaHost[$i]["ciudmciapl"]
                    );
                    $respuesta = ModeloSincronizacion::mdlCrearPlanta($tabla, $datos);
                    if ($respuesta == "ok") {
                        $tabla                                             = "transportador";
                        $idPlantaAntigua                                   = $respuestaHost[$i]["codplanta"];
                        $respuesta                                         = ModeloSincronizacion::mdlPlantaMaxima("plantas");
                        $idMaximaPlanta                                    = $respuesta[0];
                        $respuestaActualizarcionPlantasTransportadoresHost = ModeloSincronizacion::mdlActualizarTransportadoresHost($tabla, $idMaximaPlanta, $idPlantaAntigua);
                        $conteo++;
                    }
                }
            }

            ////////////////////////////////////////

            for ($i = 0; $i < count($respuestaLocal); $i++) {
                $validacion = 0;
                for ($x = 0; $x < count($respuestaHost); $x++) {
                    $identificadorPlantaHost  = $respuestaLocal[$i]["agenteplanta"] . $respuestaLocal[$i]["nitplanta"] . $respuestaLocal[$i]["ciudmciapl"];
                    $identificadorPlantaLocal = $respuestaHost[$x]["agenteplanta"] . $respuestaHost[$x]["nitplanta"] . $respuestaHost[$x]["ciudmciapl"];

                    if ($identificadorPlantaLocal == $identificadorPlantaHost) {
                        $validacion = 1;
                        $x          = count($respuestaHost) + 1;
                    }
                }
                if ($validacion != 1) {
                    $tabla = "plantas";
                    $datos = array(
                        "agenteplanta" => $respuestaLocal[$i]["agenteplanta"],
                        "nitplanta" => $respuestaLocal[$i]["nitplanta"],
                        "nameplanta" => $respuestaLocal[$i]["nameplanta"],
                        "ciudmciapl" => $respuestaLocal[$i]["ciudmciapl"],
                        "contacmciapl" => $respuestaLocal[$i]["contacmciapl"],
                        "dirmciapl" => $respuestaLocal[$i]["dirmciapl"],
                        "telfmciapl" => $respuestaLocal[$i]["telfmciapl"],
                        "recepmciahasta" => $respuestaLocal[$i]["recepmciahasta"],
                        "diasestillegmcia" => $respuestaLocal[$i]["diasestillegmcia"],
                        "condicdespmcia" => $respuestaLocal[$i]["condicdespmcia"],
                        "emailmcia" => $respuestaLocal[$i]["emailmcia"],
                        "fechlimrecfact" => $respuestaLocal[$i]["fechlimrecfact"],
                        "diasestillegfact" => $respuestaLocal[$i]["diasestillegfact"],
                        "despfactconmcia" => $respuestaLocal[$i]["despfactconmcia"],
                        "obligoc" => $respuestaLocal[$i]["obligoc"],
                        "mcipiofactpl" => $respuestaLocal[$i]["mcipiofactpl"],
                        "dirfactpl" => $respuestaLocal[$i]["dirfactpl"],
                        "docsadicradfact" => $respuestaLocal[$i]["docsadicradfact"],
                        "condicadicradfac" => $respuestaLocal[$i]["condicadicradfac"],
                        "emailfact" => $respuestaLocal[$i]["emailfact"],
                        "contacfact" => $respuestaLocal[$i]["contacfact"],
                        "teleffact" => $respuestaLocal[$i]["teleffact"],
                        "celufact" => $respuestaLocal[$i]["celufact"],
                        "contacteso" => $respuestaLocal[$i]["contacteso"],
                        "mcipioteso" => $respuestaLocal[$i]["mcipioteso"],
                        "dirteso" => $respuestaLocal[$i]["dirteso"],
                        "telefteso" => $respuestaLocal[$i]["telefteso"],
                        "celuteso" => $respuestaLocal[$i]["celuteso"],
                        "emailteso" => $respuestaLocal[$i]["emailteso"]
                    );
                    $respuesta = ModeloSincronizacion::mdlCrearPlantaHost($tabla, $datos);
                }
            }

            $this->sincronizarTransportadores();
        }
    }

    public function sincronizarTransportadores()
    {

        $tabla                                 = "transportador";
        $transportadoresPendientesInsertarHost = ModeloSincronizacion::mdlTransportadoresPendientesInsertarHost($tabla);
        $transportadoresPendientesActualizar   = ModeloSincronizacion::mdlTransportadoresPendientesActualizar($tabla);
        if (count($transportadoresPendientesInsertarHost) == 0 && count($transportadoresPendientesActualizar) == 0) {
            $this->sincronizarRegistros();
        } else {
            for ($i = 0; $i < count($transportadoresPendientesInsertarHost); $i++) {
                $tabla = "transportador";
                $datos = array(
                    "tipoTransportador"           => $transportadoresPendientesInsertarHost[$i]["tipoTransportador"],
                    "nombreTransportador"         => $transportadoresPendientesInsertarHost[$i]["nombreTransportador"],
                    "codplanta"                   => $transportadoresPendientesInsertarHost[$i]["codplanta"],
                    "caracteristicaTransportador" => $transportadoresPendientesInsertarHost[$i]["caracteristicaTransportador"]
                );
                $respuesta = ModeloSincronizacion::mdlCrearTransportador($tabla, $datos, "insertar", $transportadoresPendientesInsertarHost[$i]["idTransportador"]);
                if ($respuesta == "ok") {
                    $tabla                                             = "transportador";
                    $idPlantaAntigua                                   = $transportadoresPendientesInsertarHost[$i]["idTransportador"];
                    $respuesta                                         = ModeloSincronizacion::mdlTransportadorMaximo("transportador");
                    $idMaximaPlanta                                    = $respuesta[0];
                    $respuestaActualizarcionPlantasTransportadoresHost = ModeloSincronizacion::mdlActualizarTransportadoresHostId($tabla, $idMaximaPlanta, $idPlantaAntigua);
                } else {
                    var_dump("ERROR AL CREAR TRANSPORTADOR");
                }
            }

            for ($i = 0; $i < count($transportadoresPendientesActualizar); $i++) {
                $tabla = "transportador";
                $datos = array(
                    "tipoTransportador"           => $transportadoresPendientesActualizar[$i]["tipoTransportador"],
                    "nombreTransportador"         => $transportadoresPendientesActualizar[$i]["nombreTransportador"],
                    "codplanta"                   => $transportadoresPendientesActualizar[$i]["codplanta"],
                    "caracteristicaTransportador" => $transportadoresPendientesActualizar[$i]["caracteristicaTransportador"]
                );
                $respuesta = ModeloSincronizacion::mdlActualizarTransportador($tabla, $datos, $transportadoresPendientesActualizar[$i]["idTransportador"]);
                if ($respuesta == "ok") {
                    $tabla                                             = "transportador";
                    $idPlantaAntigua                                   = $transportadoresPendientesActualizar[$i]["idTransportador"];
                    $respuesta                                         = ModeloSincronizacion::mdlTransportadorMaximo("transportador");
                    $idMaximaPlanta                                    = $respuesta[0];
                    $respuestaActualizarcionPlantasTransportadoresHost = ModeloSincronizacion::mdlActualizarTransportadoresHostSincronizar($tabla, $idMaximaPlanta, $idPlantaAntigua);
                } else {
                    var_dump("ERROR AL CREAR TRANSPORTADOR");
                }
            };
            $this->sincronizarRegistros();
        }
    }

    public function sincronizarRegistros()
    {
        $tabla                           = "registro";
        $registrosPendientesInsertarHost = ModeloSincronizacion::mdlRegistrosPendientesInsertarHost($tabla);
        $registrosPendientesActualizar   = ModeloSincronizacion::mdlRegistrosPendientesActualizar($tabla);

        if (count($registrosPendientesInsertarHost) == 0 && count($registrosPendientesActualizar) == 0) {
            $this->sincronizarBandaElevadora();
        } else {
            $conteo = 0;

            for ($i = 0; $i < count($registrosPendientesInsertarHost); $i++) {
                $tabla = "registro";
                $datos = array(
                    "fechaRegistro" => $registrosPendientesInsertarHost[$i]["fechaRegistro"],
                    "idTransportador"              => $registrosPendientesInsertarHost[$i]["idTransportador"],
                    "codplanta"                    => $registrosPendientesInsertarHost[$i]["codplanta"],
                    "estadoRegistro"               => $registrosPendientesInsertarHost[$i]["estadoRegistro"],
                    "usuarioRegistro"              => $registrosPendientesInsertarHost[$i]["usuarioRegistro"]
                );
                $respuesta = ModeloSincronizacion::mdlCrearRegistro($tabla, $datos);
                if ($respuesta == "ok") {
                    $tabla                                       = "registro";
                    $idPlantaAntigua                             = $registrosPendientesInsertarHost[$i]["idRegistro"];
                    $respuesta                                   = ModeloSincronizacion::mdlRegistroMaximo("registro");
                    $idMaximaPlanta                              = $respuesta[0];
                    $respuestaActualizarcionPlantasRegistrosHost = ModeloSincronizacion::mdlActualizarRegistrosHostId($tabla, $idMaximaPlanta, $idPlantaAntigua);
                } else {
                    var_dump("ERROR AL CREAR REGISTRO");
                }
            }
            for ($i = 0; $i < count($registrosPendientesActualizar); $i++) {
                $datos = array(
                    "fechaRegistro" => $registrosPendientesActualizar[$i]["fechaRegistro"],
                    "idTransportador"              => $registrosPendientesActualizar[$i]["idTransportador"],
                    "codplanta"                    => $registrosPendientesActualizar[$i]["codplanta"],
                    "estadoRegistro"               => $registrosPendientesActualizar[$i]["estadoRegistro"],
                    "usuarioRegistro"              => $registrosPendientesActualizar[$i]["usuarioRegistro"],
                    "idRegistro"                   => $registrosPendientesActualizar[$i]["idRegistro"]
                );

                $respuesta = ModeloSincronizacion::mdlActualizarRegistro($tabla, $datos);
                if ($respuesta == "ok") {
                    $tabla                                       = "registro";
                    $idPlantaAntigua                             = $registrosPendientesActualizar[$i]["idRegistro"];
                    $respuesta                                   = ModeloSincronizacion::mdlRegistroMaximo("registro");
                    $idMaximaPlanta                              = $respuesta[0];
                    $respuestaActualizarcionPlantasRegistrosHost = ModeloSincronizacion::mdlActualizarRegistrosHostSincronizar($tabla, $idMaximaPlanta, $idPlantaAntigua);
                    if ($respuestaActualizarcionPlantasRegistrosHost == "ok") {
                        var_dump("REGISTRO ACTUALIZADO CORRECTAMENTE");
                    }
                } else {
                    var_dump("ERROR AL CREAR TRANSPORTADOR");
                }
            };
            $this->sincronizarBandaElevadora();
        }
    }

    public function sincronizarBandaElevadora()
    {
        $tabla                       = "bandaElevadora";
        $registrosBandaElevadoraHost = ModeloSincronizacion::mdlBandaElevadoraHost($tabla);

        $registrosBandaElevadoraPendienteActualizar = ModeloSincronizacion::mdlBandaElevadoraActualizar($tabla);

        if (count($registrosBandaElevadoraHost) == 0 && count($registrosBandaElevadoraPendienteActualizar) == 0) {
            $this->sincronizarBandaTransmision();
        } else {
            for ($i = 0; $i < count($registrosBandaElevadoraHost); $i++) {

                $datos = array(
                    "idRegistro"                     => $registrosBandaElevadoraHost[$i]["idRegistro"],
                    "marcaBandaElevadora"                           => $registrosBandaElevadoraHost[$i]["marcaBandaElevadora"],
                    "anchoBandaElevadora"                           => $registrosBandaElevadoraHost[$i]["anchoBandaElevadora"],
                    "distanciaEntrePoleasElevadora"                 => $registrosBandaElevadoraHost[$i]["distanciaEntrePoleasElevadora"],
                    "noLonaBandaElevadora"                          => $registrosBandaElevadoraHost[$i]["noLonaBandaElevadora"],
                    "tipoLonaBandaElevadora"                        => $registrosBandaElevadoraHost[$i]["tipoLonaBandaElevadora"],
                    "espesorTotalBandaElevadora"                    => $registrosBandaElevadoraHost[$i]["espesorTotalBandaElevadora"],
                    "espesorCojinActualElevadora"                   => $registrosBandaElevadoraHost[$i]["espesorCojinActualElevadora"],
                    "espesorCubiertaSuperiorElevadora"              => $registrosBandaElevadoraHost[$i]["espesorCubiertaSuperiorElevadora"],
                    "espesorCubiertaInferiorElevadora"              => $registrosBandaElevadoraHost[$i]["espesorCubiertaInferiorElevadora"],
                    "tipoCubiertaElevadora"                         => $registrosBandaElevadoraHost[$i]["tipoCubiertaElevadora"],
                    "tipoEmpalmeElevadora"                          => $registrosBandaElevadoraHost[$i]["tipoEmpalmeElevadora"],
                    "estadoEmpalmeElevadora"                        => $registrosBandaElevadoraHost[$i]["estadoEmpalmeElevadora"],
                    "resistenciaRoturaLonaElevadora"                => $registrosBandaElevadoraHost[$i]["resistenciaRoturaLonaElevadora"],
                    "velocidadBandaElevadora"                       => $registrosBandaElevadoraHost[$i]["velocidadBandaElevadora"],
                    "marcaBandaElevadoraAnterior"                   => $registrosBandaElevadoraHost[$i]["marcaBandaElevadoraAnterior"],
                    "anchoBandaElevadoraAnterior"                   => $registrosBandaElevadoraHost[$i]["anchoBandaElevadoraAnterior"],
                    "noLonasBandaElevadoraAnterior"                 => $registrosBandaElevadoraHost[$i]["noLonasBandaElevadoraAnterior"],
                    "tipoLonaBandaElevadoraAnterior"                => $registrosBandaElevadoraHost[$i]["tipoLonaBandaElevadoraAnterior"],
                    "espesorTotalBandaElevadoraAnterior"            => $registrosBandaElevadoraHost[$i]["espesorTotalBandaElevadoraAnterior"],
                    "espesorCubiertaSuperiorBandaElevadoraAnterior" => $registrosBandaElevadoraHost[$i]["espesorCubiertaSuperiorBandaElevadoraAnterior"],
                    "espesorCojinElevadoraAnterior"                 => $registrosBandaElevadoraHost[$i]["espesorCojinElevadoraAnterior"],
                    "espesorCubiertaInferiorBandaElevadoraAnterior" => $registrosBandaElevadoraHost[$i]["espesorCubiertaInferiorBandaElevadoraAnterior"],
                    "tipoCubiertaElevadoraAnterior"                 => $registrosBandaElevadoraHost[$i]["tipoCubiertaElevadoraAnterior"],
                    "tipoEmpalmeElevadoraAnterior"                  => $registrosBandaElevadoraHost[$i]["tipoEmpalmeElevadoraAnterior"],
                    "resistenciaRoturaBandaElevadoraAnterior"       => $registrosBandaElevadoraHost[$i]["resistenciaRoturaBandaElevadoraAnterior"],
                    "tonsTransportadasBandaElevadoraAnterior"       => $registrosBandaElevadoraHost[$i]["tonsTransportadasBandaElevadoraAnterior"],
                    "velocidadBandaElevadoraAnterior"               => $registrosBandaElevadoraHost[$i]["velocidadBandaElevadoraAnterior"],
                    "causaFallaCambioBandaElevadoraAnterior"        => $registrosBandaElevadoraHost[$i]["causaFallaCambioBandaElevadoraAnterior"],
                    "pesoMaterialEnCadaCangilon"                    => $registrosBandaElevadoraHost[$i]["pesoMaterialEnCadaCangilon"],
                    "pesoCangilonVacio"                             => $registrosBandaElevadoraHost[$i]["pesoCangilonVacio"],
                    "longitudCangilon"                              => $registrosBandaElevadoraHost[$i]["longitudCangilon"],
                    "materialCangilon"                              => $registrosBandaElevadoraHost[$i]["materialCangilon"],
                    "tipoCangilon"                                  => $registrosBandaElevadoraHost[$i]["tipoCangilon"],
                    "proyeccionCangilon"                            => $registrosBandaElevadoraHost[$i]["proyeccionCangilon"],
                    "profundidadCangilon"                           => $registrosBandaElevadoraHost[$i]["profundidadCangilon"],
                    "marcaCangilon"                                 => $registrosBandaElevadoraHost[$i]["marcaCangilon"],
                    "referenciaCangilon"                            => $registrosBandaElevadoraHost[$i]["referenciaCangilon"],
                    "capacidadCangilon"                             => $registrosBandaElevadoraHost[$i]["capacidadCangilon"],
                    "noFilasCangilones"                             => $registrosBandaElevadoraHost[$i]["noFilasCangilones"],
                    "separacionCangilones"                          => $registrosBandaElevadoraHost[$i]["separacionCangilones"],
                    "noAgujeros"                                    => $registrosBandaElevadoraHost[$i]["noAgujeros"],
                    "distanciaBordeBandaEstructura"                 => $registrosBandaElevadoraHost[$i]["distanciaBordeBandaEstructura"],
                    "distanciaPosteriorBandaEstructura"             => $registrosBandaElevadoraHost[$i]["distanciaPosteriorBandaEstructura"],
                    "distanciaLaboFrontalCangilonEstructura"        => $registrosBandaElevadoraHost[$i]["distanciaLaboFrontalCangilonEstructura"],
                    "distanciaBordesCangilonEstructura"             => $registrosBandaElevadoraHost[$i]["distanciaBordesCangilonEstructura"],
                    "tipoVentilacion"                               => $registrosBandaElevadoraHost[$i]["tipoVentilacion"],
                    "diametroPoleaMotrizElevadora"                  => $registrosBandaElevadoraHost[$i]["diametroPoleaMotrizElevadora"],
                    "anchoPoleaMotrizElevadora"                     => $registrosBandaElevadoraHost[$i]["anchoPoleaMotrizElevadora"],
                    "tipoPoleaMotrizElevadora"                      => $registrosBandaElevadoraHost[$i]["tipoPoleaMotrizElevadora"],
                    "largoEjeMotrizElevadora"                       => $registrosBandaElevadoraHost[$i]["largoEjeMotrizElevadora"],
                    "diametroEjeMotrizElevadora"                    => $registrosBandaElevadoraHost[$i]["diametroEjeMotrizElevadora"],
                    "bandaCentradaEnPoleaMotrizElevadora"           => $registrosBandaElevadoraHost[$i]["bandaCentradaEnPoleaMotrizElevadora"],
                    "estadoRevestimientoPoleaMotrizElevadora"       => $registrosBandaElevadoraHost[$i]["estadoRevestimientoPoleaMotrizElevadora"],
                    "potenciaMotorMotrizElevadora"                  => $registrosBandaElevadoraHost[$i]["potenciaMotorMotrizElevadora"],
                    "rpmSalidaReductorMotrizElevadora"              => $registrosBandaElevadoraHost[$i]["rpmSalidaReductorMotrizElevadora"],
                    "guardaReductorPoleaMotrizElevadora"            => $registrosBandaElevadoraHost[$i]["guardaReductorPoleaMotrizElevadora"],
                    "diametroPoleaColaElevadora"                    => $registrosBandaElevadoraHost[$i]["diametroPoleaColaElevadora"],
                    "anchoPoleaColaElevadora"                       => $registrosBandaElevadoraHost[$i]["anchoPoleaColaElevadora"],
                    "tipoPoleaColaElevadora"                        => $registrosBandaElevadoraHost[$i]["tipoPoleaColaElevadora"],
                    "largoEjePoleaColaElevadora"                    => $registrosBandaElevadoraHost[$i]["largoEjePoleaColaElevadora"],
                    "diametroEjePoleaColaElevadora"                 => $registrosBandaElevadoraHost[$i]["diametroEjePoleaColaElevadora"],
                    "bandaCentradaEnPoleaColaElevadora"             => $registrosBandaElevadoraHost[$i]["bandaCentradaEnPoleaColaElevadora"],
                    "estadoRevestimientoPoleaColaElevadora"         => $registrosBandaElevadoraHost[$i]["estadoRevestimientoPoleaColaElevadora"],
                    "longitudTensorTornilloPoleaColaElevadora"      => $registrosBandaElevadoraHost[$i]["longitudTensorTornilloPoleaColaElevadora"],
                    "longitudRecorridoContrapesaPoleaColaElevadora" => $registrosBandaElevadoraHost[$i]["longitudRecorridoContrapesaPoleaColaElevadora"],
                    "cargaTrabajoBandaElevadora"                    => $registrosBandaElevadoraHost[$i]["cargaTrabajoBandaElevadora"],
                    "temperaturaMaterialElevadora"                  => $registrosBandaElevadoraHost[$i]["temperaturaMaterialElevadora"],
                    "empalmeMecanicoElevadora"                      => $registrosBandaElevadoraHost[$i]["empalmeMecanicoElevadora"],
                    "diametroRoscaElevadora"                        => $registrosBandaElevadoraHost[$i]["diametroRoscaElevadora"],
                    "largoTornilloElevadora"                        => $registrosBandaElevadoraHost[$i]["largoTornilloElevadora"],
                    "materialTornilloElevadora"                     => $registrosBandaElevadoraHost[$i]["materialTornilloElevadora"],
                    "anchoCabezaElevadorPuertaInspeccion"           => $registrosBandaElevadoraHost[$i]["anchoCabezaElevadorPuertaInspeccion"],
                    "largoCabezaElevadorPuertaInspeccion"           => $registrosBandaElevadoraHost[$i]["largoCabezaElevadorPuertaInspeccion"],
                    "anchoBotaElevadorPuertaInspeccion"             => $registrosBandaElevadoraHost[$i]["anchoBotaElevadorPuertaInspeccion"],
                    "largoBotaElevadorPuertaInspeccion"             => $registrosBandaElevadoraHost[$i]["largoBotaElevadorPuertaInspeccion"],
                    "monitorPeligro"                                => $registrosBandaElevadoraHost[$i]["monitorPeligro"],
                    "rodamiento"                                    => $registrosBandaElevadoraHost[$i]["rodamiento"],
                    "monitorDesalineacion"                          => $registrosBandaElevadoraHost[$i]["monitorDesalineacion"],
                    "monitorVelocidad"                              => $registrosBandaElevadoraHost[$i]["monitorVelocidad"],
                    "sensorInductivo"                               => $registrosBandaElevadoraHost[$i]["sensorInductivo"],
                    "indicadorNivel"                                => $registrosBandaElevadoraHost[$i]["indicadorNivel"],
                    "cajaUnion"                                     => $registrosBandaElevadoraHost[$i]["cajaUnion"],
                    "alarmaYPantalla"                               => $registrosBandaElevadoraHost[$i]["alarmaYPantalla"],
                    "interruptorSeguridad"                          => $registrosBandaElevadoraHost[$i]["interruptorSeguridad"],
                    "materialElevadora"                             => $registrosBandaElevadoraHost[$i]["materialElevadora"],
                    "ataqueQuimicoElevadora"                        => $registrosBandaElevadoraHost[$i]["ataqueQuimicoElevadora"],
                    "ataqueTemperaturaElevadora"                    => $registrosBandaElevadoraHost[$i]["ataqueTemperaturaElevadora"],
                    "ataqueAceitesElevadora"                        => $registrosBandaElevadoraHost[$i]["ataqueAceitesElevadora"],
                    "ataqueAbrasivoElevadora"                       => $registrosBandaElevadoraHost[$i]["ataqueAbrasivoElevadora"],
                    "capacidadElevadora"                            => $registrosBandaElevadoraHost[$i]["capacidadElevadora"],
                    "horasTrabajoDiaElevadora"                      => $registrosBandaElevadoraHost[$i]["horasTrabajoDiaElevadora"],
                    "diasTrabajoSemanaElevadora"                    => $registrosBandaElevadoraHost[$i]["diasTrabajoSemanaElevadora"],
                    "abrasividadElevadora"                          => $registrosBandaElevadoraHost[$i]["abrasividadElevadora"],
                    "porcentajeFinosElevadora"                      => $registrosBandaElevadoraHost[$i]["porcentajeFinosElevadora"],
                    "maxGranulometriaElevadora"                     => $registrosBandaElevadoraHost[$i]["maxGranulometriaElevadora"],
                    "densidadMaterialElevadora"                     => $registrosBandaElevadoraHost[$i]["densidadMaterialElevadora"],
                    "tempMaxMaterialSobreBandaElevadora"            => $registrosBandaElevadoraHost[$i]["tempMaxMaterialSobreBandaElevadora"],
                    "tempPromedioMaterialSobreBandaElevadora"       => $registrosBandaElevadoraHost[$i]["tempPromedioMaterialSobreBandaElevadora"],
                    "variosPuntosDeAlimentacion"                    => $registrosBandaElevadoraHost[$i]["variosPuntosDeAlimentacion"],
                    "lluviaDeMaterial"                              => $registrosBandaElevadoraHost[$i]["lluviaDeMaterial"],
                    "anchoPiernaElevador"                           => $registrosBandaElevadoraHost[$i]["anchoPiernaElevador"],
                    "profundidadPiernaElevador"                     => $registrosBandaElevadoraHost[$i]["profundidadPiernaElevador"],
                    "tempAmbienteMin"                               => $registrosBandaElevadoraHost[$i]["tempAmbienteMin"],
                    "tempAmbienteMax"                               => $registrosBandaElevadoraHost[$i]["tempAmbienteMax"],
                    "tipoDescarga"                                  => $registrosBandaElevadoraHost[$i]["tipoDescarga"],
                    "tipoCarga"                                     => $registrosBandaElevadoraHost[$i]["tipoCarga"],
                    "observacionRegistroElevadora"                  => $registrosBandaElevadoraHost[$i]["observacionRegistroElevadora"]
                );
                $respuesta = ModeloSincronizacion::mdlDatosBandaElevadora($tabla, $datos);
                if ($respuesta == "ok") {
                    $respuesta = ModeloSincronizacion::mdlActualizarBandaElevadoraHost("bandaElevadora", $registrosBandaElevadoraHost[$i]["idRegistro"]);
                    if ($respuesta == "ok") {
                        var_dump("REGISTRO HOST ACTUALIZADO CORRECTAMENTE");
                    } else {
                        var_dump("REGISTRO HOST NO ACTUALIZADO CORRECTAMENTE");
                    }
                } else {
                    var_dump("ERROR");
                }
            }

            for ($i = 0; $i < count($registrosBandaElevadoraPendienteActualizar); $i++) {

                $datos = array(
                    "idRegistro"                     => $registrosBandaElevadoraPendienteActualizar[$i]["idRegistro"],
                    "marcaBandaElevadora"                           => $registrosBandaElevadoraPendienteActualizar[$i]["marcaBandaElevadora"],
                    "anchoBandaElevadora"                           => $registrosBandaElevadoraPendienteActualizar[$i]["anchoBandaElevadora"],
                    "distanciaEntrePoleasElevadora"                 => $registrosBandaElevadoraPendienteActualizar[$i]["distanciaEntrePoleasElevadora"],
                    "noLonaBandaElevadora"                          => $registrosBandaElevadoraPendienteActualizar[$i]["noLonaBandaElevadora"],
                    "tipoLonaBandaElevadora"                        => $registrosBandaElevadoraPendienteActualizar[$i]["tipoLonaBandaElevadora"],
                    "espesorTotalBandaElevadora"                    => $registrosBandaElevadoraPendienteActualizar[$i]["espesorTotalBandaElevadora"],
                    "espesorCojinActualElevadora"                   => $registrosBandaElevadoraPendienteActualizar[$i]["espesorCojinActualElevadora"],
                    "espesorCubiertaSuperiorElevadora"              => $registrosBandaElevadoraPendienteActualizar[$i]["espesorCubiertaSuperiorElevadora"],
                    "espesorCubiertaInferiorElevadora"              => $registrosBandaElevadoraPendienteActualizar[$i]["espesorCubiertaInferiorElevadora"],
                    "tipoCubiertaElevadora"                         => $registrosBandaElevadoraPendienteActualizar[$i]["tipoCubiertaElevadora"],
                    "tipoEmpalmeElevadora"                          => $registrosBandaElevadoraPendienteActualizar[$i]["tipoEmpalmeElevadora"],
                    "estadoEmpalmeElevadora"                        => $registrosBandaElevadoraPendienteActualizar[$i]["estadoEmpalmeElevadora"],
                    "resistenciaRoturaLonaElevadora"                => $registrosBandaElevadoraPendienteActualizar[$i]["resistenciaRoturaLonaElevadora"],
                    "velocidadBandaElevadora"                       => $registrosBandaElevadoraPendienteActualizar[$i]["velocidadBandaElevadora"],
                    "marcaBandaElevadoraAnterior"                   => $registrosBandaElevadoraPendienteActualizar[$i]["marcaBandaElevadoraAnterior"],
                    "anchoBandaElevadoraAnterior"                   => $registrosBandaElevadoraPendienteActualizar[$i]["anchoBandaElevadoraAnterior"],
                    "noLonasBandaElevadoraAnterior"                 => $registrosBandaElevadoraPendienteActualizar[$i]["noLonasBandaElevadoraAnterior"],
                    "tipoLonaBandaElevadoraAnterior"                => $registrosBandaElevadoraPendienteActualizar[$i]["tipoLonaBandaElevadoraAnterior"],
                    "espesorTotalBandaElevadoraAnterior"            => $registrosBandaElevadoraPendienteActualizar[$i]["espesorTotalBandaElevadoraAnterior"],
                    "espesorCubiertaSuperiorBandaElevadoraAnterior" => $registrosBandaElevadoraPendienteActualizar[$i]["espesorCubiertaSuperiorBandaElevadoraAnterior"],
                    "espesorCojinElevadoraAnterior"                 => $registrosBandaElevadoraPendienteActualizar[$i]["espesorCojinElevadoraAnterior"],
                    "espesorCubiertaInferiorBandaElevadoraAnterior" => $registrosBandaElevadoraPendienteActualizar[$i]["espesorCubiertaInferiorBandaElevadoraAnterior"],
                    "tipoCubiertaElevadoraAnterior"                 => $registrosBandaElevadoraPendienteActualizar[$i]["tipoCubiertaElevadoraAnterior"],
                    "tipoEmpalmeElevadoraAnterior"                  => $registrosBandaElevadoraPendienteActualizar[$i]["tipoEmpalmeElevadoraAnterior"],
                    "resistenciaRoturaBandaElevadoraAnterior"       => $registrosBandaElevadoraPendienteActualizar[$i]["resistenciaRoturaBandaElevadoraAnterior"],
                    "tonsTransportadasBandaElevadoraAnterior"       => $registrosBandaElevadoraPendienteActualizar[$i]["tonsTransportadasBandaElevadoraAnterior"],
                    "velocidadBandaElevadoraAnterior"               => $registrosBandaElevadoraPendienteActualizar[$i]["velocidadBandaElevadoraAnterior"],
                    "causaFallaCambioBandaElevadoraAnterior"        => $registrosBandaElevadoraPendienteActualizar[$i]["causaFallaCambioBandaElevadoraAnterior"],
                    "pesoMaterialEnCadaCangilon"                    => $registrosBandaElevadoraPendienteActualizar[$i]["pesoMaterialEnCadaCangilon"],
                    "pesoCangilonVacio"                             => $registrosBandaElevadoraPendienteActualizar[$i]["pesoCangilonVacio"],
                    "longitudCangilon"                              => $registrosBandaElevadoraPendienteActualizar[$i]["longitudCangilon"],
                    "materialCangilon"                              => $registrosBandaElevadoraPendienteActualizar[$i]["materialCangilon"],
                    "tipoCangilon"                                  => $registrosBandaElevadoraPendienteActualizar[$i]["tipoCangilon"],
                    "proyeccionCangilon"                            => $registrosBandaElevadoraPendienteActualizar[$i]["proyeccionCangilon"],
                    "profundidadCangilon"                           => $registrosBandaElevadoraPendienteActualizar[$i]["profundidadCangilon"],
                    "marcaCangilon"                                 => $registrosBandaElevadoraPendienteActualizar[$i]["marcaCangilon"],
                    "referenciaCangilon"                            => $registrosBandaElevadoraPendienteActualizar[$i]["referenciaCangilon"],
                    "capacidadCangilon"                             => $registrosBandaElevadoraPendienteActualizar[$i]["capacidadCangilon"],
                    "noFilasCangilones"                             => $registrosBandaElevadoraPendienteActualizar[$i]["noFilasCangilones"],
                    "separacionCangilones"                          => $registrosBandaElevadoraPendienteActualizar[$i]["separacionCangilones"],
                    "noAgujeros"                                    => $registrosBandaElevadoraPendienteActualizar[$i]["noAgujeros"],
                    "distanciaBordeBandaEstructura"                 => $registrosBandaElevadoraPendienteActualizar[$i]["distanciaBordeBandaEstructura"],
                    "distanciaPosteriorBandaEstructura"             => $registrosBandaElevadoraPendienteActualizar[$i]["distanciaPosteriorBandaEstructura"],
                    "distanciaLaboFrontalCangilonEstructura"        => $registrosBandaElevadoraPendienteActualizar[$i]["distanciaLaboFrontalCangilonEstructura"],
                    "distanciaBordesCangilonEstructura"             => $registrosBandaElevadoraPendienteActualizar[$i]["distanciaBordesCangilonEstructura"],
                    "tipoVentilacion"                               => $registrosBandaElevadoraPendienteActualizar[$i]["tipoVentilacion"],
                    "diametroPoleaMotrizElevadora"                  => $registrosBandaElevadoraPendienteActualizar[$i]["diametroPoleaMotrizElevadora"],
                    "anchoPoleaMotrizElevadora"                     => $registrosBandaElevadoraPendienteActualizar[$i]["anchoPoleaMotrizElevadora"],
                    "tipoPoleaMotrizElevadora"                      => $registrosBandaElevadoraPendienteActualizar[$i]["tipoPoleaMotrizElevadora"],
                    "largoEjeMotrizElevadora"                       => $registrosBandaElevadoraPendienteActualizar[$i]["largoEjeMotrizElevadora"],
                    "diametroEjeMotrizElevadora"                    => $registrosBandaElevadoraPendienteActualizar[$i]["diametroEjeMotrizElevadora"],
                    "bandaCentradaEnPoleaMotrizElevadora"           => $registrosBandaElevadoraPendienteActualizar[$i]["bandaCentradaEnPoleaMotrizElevadora"],
                    "estadoRevestimientoPoleaMotrizElevadora"       => $registrosBandaElevadoraPendienteActualizar[$i]["estadoRevestimientoPoleaMotrizElevadora"],
                    "potenciaMotorMotrizElevadora"                  => $registrosBandaElevadoraPendienteActualizar[$i]["potenciaMotorMotrizElevadora"],
                    "rpmSalidaReductorMotrizElevadora"              => $registrosBandaElevadoraPendienteActualizar[$i]["rpmSalidaReductorMotrizElevadora"],
                    "guardaReductorPoleaMotrizElevadora"            => $registrosBandaElevadoraPendienteActualizar[$i]["guardaReductorPoleaMotrizElevadora"],
                    "diametroPoleaColaElevadora"                    => $registrosBandaElevadoraPendienteActualizar[$i]["diametroPoleaColaElevadora"],
                    "anchoPoleaColaElevadora"                       => $registrosBandaElevadoraPendienteActualizar[$i]["anchoPoleaColaElevadora"],
                    "tipoPoleaColaElevadora"                        => $registrosBandaElevadoraPendienteActualizar[$i]["tipoPoleaColaElevadora"],
                    "largoEjePoleaColaElevadora"                    => $registrosBandaElevadoraPendienteActualizar[$i]["largoEjePoleaColaElevadora"],
                    "diametroEjePoleaColaElevadora"                 => $registrosBandaElevadoraPendienteActualizar[$i]["diametroEjePoleaColaElevadora"],
                    "bandaCentradaEnPoleaColaElevadora"             => $registrosBandaElevadoraPendienteActualizar[$i]["bandaCentradaEnPoleaColaElevadora"],
                    "estadoRevestimientoPoleaColaElevadora"         => $registrosBandaElevadoraPendienteActualizar[$i]["estadoRevestimientoPoleaColaElevadora"],
                    "longitudTensorTornilloPoleaColaElevadora"      => $registrosBandaElevadoraPendienteActualizar[$i]["longitudTensorTornilloPoleaColaElevadora"],
                    "longitudRecorridoContrapesaPoleaColaElevadora" => $registrosBandaElevadoraPendienteActualizar[$i]["longitudRecorridoContrapesaPoleaColaElevadora"],
                    "cargaTrabajoBandaElevadora"                    => $registrosBandaElevadoraPendienteActualizar[$i]["cargaTrabajoBandaElevadora"],
                    "temperaturaMaterialElevadora"                  => $registrosBandaElevadoraPendienteActualizar[$i]["temperaturaMaterialElevadora"],
                    "empalmeMecanicoElevadora"                      => $registrosBandaElevadoraPendienteActualizar[$i]["empalmeMecanicoElevadora"],
                    "diametroRoscaElevadora"                        => $registrosBandaElevadoraPendienteActualizar[$i]["diametroRoscaElevadora"],
                    "largoTornilloElevadora"                        => $registrosBandaElevadoraPendienteActualizar[$i]["largoTornilloElevadora"],
                    "materialTornilloElevadora"                     => $registrosBandaElevadoraPendienteActualizar[$i]["materialTornilloElevadora"],
                    "anchoCabezaElevadorPuertaInspeccion"           => $registrosBandaElevadoraPendienteActualizar[$i]["anchoCabezaElevadorPuertaInspeccion"],
                    "largoCabezaElevadorPuertaInspeccion"           => $registrosBandaElevadoraPendienteActualizar[$i]["largoCabezaElevadorPuertaInspeccion"],
                    "anchoBotaElevadorPuertaInspeccion"             => $registrosBandaElevadoraPendienteActualizar[$i]["anchoBotaElevadorPuertaInspeccion"],
                    "largoBotaElevadorPuertaInspeccion"             => $registrosBandaElevadoraPendienteActualizar[$i]["largoBotaElevadorPuertaInspeccion"],
                    "monitorPeligro"                                => $registrosBandaElevadoraPendienteActualizar[$i]["monitorPeligro"],
                    "rodamiento"                                    => $registrosBandaElevadoraPendienteActualizar[$i]["rodamiento"],
                    "monitorDesalineacion"                          => $registrosBandaElevadoraPendienteActualizar[$i]["monitorDesalineacion"],
                    "monitorVelocidad"                              => $registrosBandaElevadoraPendienteActualizar[$i]["monitorVelocidad"],
                    "sensorInductivo"                               => $registrosBandaElevadoraPendienteActualizar[$i]["sensorInductivo"],
                    "indicadorNivel"                                => $registrosBandaElevadoraPendienteActualizar[$i]["indicadorNivel"],
                    "cajaUnion"                                     => $registrosBandaElevadoraPendienteActualizar[$i]["cajaUnion"],
                    "alarmaYPantalla"                               => $registrosBandaElevadoraPendienteActualizar[$i]["alarmaYPantalla"],
                    "interruptorSeguridad"                          => $registrosBandaElevadoraPendienteActualizar[$i]["interruptorSeguridad"],
                    "materialElevadora"                             => $registrosBandaElevadoraPendienteActualizar[$i]["materialElevadora"],
                    "ataqueQuimicoElevadora"                        => $registrosBandaElevadoraPendienteActualizar[$i]["ataqueQuimicoElevadora"],
                    "ataqueTemperaturaElevadora"                    => $registrosBandaElevadoraPendienteActualizar[$i]["ataqueTemperaturaElevadora"],
                    "ataqueAceitesElevadora"                        => $registrosBandaElevadoraPendienteActualizar[$i]["ataqueAceitesElevadora"],
                    "ataqueAbrasivoElevadora"                       => $registrosBandaElevadoraPendienteActualizar[$i]["ataqueAbrasivoElevadora"],
                    "capacidadElevadora"                            => $registrosBandaElevadoraPendienteActualizar[$i]["capacidadElevadora"],
                    "horasTrabajoDiaElevadora"                      => $registrosBandaElevadoraPendienteActualizar[$i]["horasTrabajoDiaElevadora"],
                    "diasTrabajoSemanaElevadora"                    => $registrosBandaElevadoraPendienteActualizar[$i]["diasTrabajoSemanaElevadora"],
                    "abrasividadElevadora"                          => $registrosBandaElevadoraPendienteActualizar[$i]["abrasividadElevadora"],
                    "porcentajeFinosElevadora"                      => $registrosBandaElevadoraPendienteActualizar[$i]["porcentajeFinosElevadora"],
                    "maxGranulometriaElevadora"                     => $registrosBandaElevadoraPendienteActualizar[$i]["maxGranulometriaElevadora"],
                    "densidadMaterialElevadora"                     => $registrosBandaElevadoraPendienteActualizar[$i]["densidadMaterialElevadora"],
                    "tempMaxMaterialSobreBandaElevadora"            => $registrosBandaElevadoraPendienteActualizar[$i]["tempMaxMaterialSobreBandaElevadora"],
                    "tempPromedioMaterialSobreBandaElevadora"       => $registrosBandaElevadoraPendienteActualizar[$i]["tempPromedioMaterialSobreBandaElevadora"],
                    "variosPuntosDeAlimentacion"                    => $registrosBandaElevadoraPendienteActualizar[$i]["variosPuntosDeAlimentacion"],
                    "lluviaDeMaterial"                              => $registrosBandaElevadoraPendienteActualizar[$i]["lluviaDeMaterial"],
                    "anchoPiernaElevador"                           => $registrosBandaElevadoraPendienteActualizar[$i]["anchoPiernaElevador"],
                    "profundidadPiernaElevador"                     => $registrosBandaElevadoraPendienteActualizar[$i]["profundidadPiernaElevador"],
                    "tempAmbienteMin"                               => $registrosBandaElevadoraPendienteActualizar[$i]["tempAmbienteMin"],
                    "tempAmbienteMax"                               => $registrosBandaElevadoraPendienteActualizar[$i]["tempAmbienteMax"],
                    "tipoDescarga"                                  => $registrosBandaElevadoraPendienteActualizar[$i]["tipoDescarga"],
                    "tipoCarga"                                     => $registrosBandaElevadoraPendienteActualizar[$i]["tipoCarga"],
                    "observacionRegistroElevadora"                  => $registrosBandaElevadoraPendienteActualizar[$i]["observacionRegistroElevadora"]
                );
                $respuesta = ModeloSincronizacion::mdlActualizarBandaElevadora($tabla, $datos);
                if ($respuesta == "ok") {
                    $respuesta = ModeloSincronizacion::mdlActualizarBandaElevadoraHost("bandaElevadora", $registrosBandaElevadoraPendienteActualizar[$i]["idRegistro"]);
                    if ($respuesta == "ok") {
                        // var_dump("REGISTRO HOST ACTUALIZADO CORRECTAMENTE");
                    } else {
                        // var_dump("REGISTRO HOST NO ACTUALIZADO CORRECTAMENTE");
                    }
                }
            }
            $this->sincronizarBandaTransmision();
        }
    }

    public function sincronizarBandaTransmision()
    {
        $tabla                               = "bandaTransmision";
        $registrosbandaTransmisionHost       = ModeloSincronizacion::mdlbandaTransmisionHost($tabla);
        $registrosBandaTransmisionActualizar = ModeloSincronizacion::mdlBandaTransmisionActualizar($tabla);
        if (count($registrosbandaTransmisionHost) == 0 && count($registrosBandaTransmisionActualizar) == 0) {
            $this->sincronizarBandaTransportadora();
        } else {
            for ($i = 0; $i < count($registrosbandaTransmisionHost); $i++) {
                $datos = array(
                    "idRegistro"         => $registrosbandaTransmisionHost[$i]["idRegistro"],
                    "anchoBandaTransmision"             => $registrosbandaTransmisionHost[$i]["anchoBandaTransmision"],
                    "distanciaEntreCentrosTransmision"  => $registrosbandaTransmisionHost[$i]["distanciaEntreCentrosTransmision"],
                    "potenciaMotorTransmision"          => $registrosbandaTransmisionHost[$i]["potenciaMotorTransmision"],
                    "rpmSalidaReductorTransmision"      => $registrosbandaTransmisionHost[$i]["rpmSalidaReductorTransmision"],
                    "diametroPoleaConducidaTransmision" => $registrosbandaTransmisionHost[$i]["diametroPoleaConducidaTransmision"],
                    "anchoPoleaConducidaTransmision"    => $registrosbandaTransmisionHost[$i]["anchoPoleaConducidaTransmision"],
                    "diametroPoleaMotrizTransmision"    => $registrosbandaTransmisionHost[$i]["diametroPoleaMotrizTransmision"],
                    "anchoPoleaMotrizTransmision"       => $registrosbandaTransmisionHost[$i]["anchoPoleaMotrizTransmision"],
                    "tipoParteTransmision"              => $registrosbandaTransmisionHost[$i]["tipoParteTransmision"],
                    "observacionRegistroPesada"         => $registrosbandaTransmisionHost[$i]["observacionRegistro"]
                );
                $respuesta = ModeloSincronizacion::mdlDatosTransmision($tabla, $datos);
                if ($respuesta == "ok") {
                    $respuesta = ModeloSincronizacion::mdlActualizarTransmsionHost("bandaTransmision", $registrosbandaTransmisionHost[$i]["idRegistro"]);
                }
                // var_dump($respuesta);

            }

            for ($i = 0; $i < count($registrosBandaTransmisionActualizar); $i++) {
                $datos = array(
                    "idRegistro"         => $registrosBandaTransmisionActualizar[$i]["idRegistro"],
                    "anchoBandaTransmision"             => $registrosBandaTransmisionActualizar[$i]["anchoBandaTransmision"],
                    "distanciaEntreCentrosTransmision"  => $registrosBandaTransmisionActualizar[$i]["distanciaEntreCentrosTransmision"],
                    "potenciaMotorTransmision"          => $registrosBandaTransmisionActualizar[$i]["potenciaMotorTransmision"],
                    "rpmSalidaReductorTransmision"      => $registrosBandaTransmisionActualizar[$i]["rpmSalidaReductorTransmision"],
                    "diametroPoleaConducidaTransmision" => $registrosBandaTransmisionActualizar[$i]["diametroPoleaConducidaTransmision"],
                    "anchoPoleaConducidaTransmision"    => $registrosBandaTransmisionActualizar[$i]["anchoPoleaConducidaTransmision"],
                    "diametroPoleaMotrizTransmision"    => $registrosBandaTransmisionActualizar[$i]["diametroPoleaMotrizTransmision"],
                    "anchoPoleaMotrizTransmision"       => $registrosBandaTransmisionActualizar[$i]["anchoPoleaMotrizTransmision"],
                    "tipoParteTransmision"              => $registrosBandaTransmisionActualizar[$i]["tipoParteTransmision"],
                    "observacionRegistroPesada"         => $registrosBandaTransmisionActualizar[$i]["observacionRegistro"]
                );
                $respuesta = ModeloSincronizacion::mdlDatosTransmision($tabla, $datos);
                if ($respuesta == "ok") {
                    $respuesta = ModeloSincronizacion::mdlActualizarTransmsionHost("bandaTransmision", $registrosBandaTransmisionActualizar[$i]["idRegistro"]);
                }
                // var_dump($respuesta);

            }
            $this->sincronizarBandaTransportadora();
        }
    }

    public function sincronizarBandaTransportadora()
    {
        $tabla                                  = "bandaTransportadora";
        $registrosbandaTransportadoraHost       = ModeloSincronizacion::mdlBandaTransportadoraHost($tabla);
        $registrosBandaTransportadoraActualizar = ModeloSincronizacion::mdlBandaTransportadoraActualizar($tabla);

        if (count($registrosbandaTransportadoraHost) == 0 && count($registrosBandaTransportadoraActualizar) == 0) {
            $this->ctrMostrarMensajeFinal();
        } else {
            for ($i = 0; $i < count($registrosbandaTransportadoraHost); $i++) {
                $datos = array(
                    "idRegistro"                          => $registrosbandaTransportadoraHost[$i]["idRegistro"],
                    "marcaBandaTransportadora"                           => $registrosbandaTransportadoraHost[$i]["marcaBandaTransportadora"],
                    "anchoBandaTransportadora"                           => $registrosbandaTransportadoraHost[$i]["anchoBandaTransportadora"],
                    "noLonasBandaTransportadora"                         => $registrosbandaTransportadoraHost[$i]["noLonasBandaTransportadora"],
                    "tipoLonaBandaTransportadora"                        => $registrosbandaTransportadoraHost[$i]["tipoLonaBandaTransportadora"],
                    "espesorTotalBandaTransportadora"                    => $registrosbandaTransportadoraHost[$i]["espesorTotalBandaTransportadora"],
                    "espesorCubiertaSuperiorTransportadora"              => $registrosbandaTransportadoraHost[$i]["espesorCubiertaSuperiorTransportadora"],
                    "espesorCojinTransportadora"                         => $registrosbandaTransportadoraHost[$i]["espesorCojinTransportadora"],
                    "espesorCubiertaInferiorTransportadora"              => $registrosbandaTransportadoraHost[$i]["espesorCubiertaInferiorTransportadora"],
                    "tipoCubiertaTransportadora"                         => $registrosbandaTransportadoraHost[$i]["tipoCubiertaTransportadora"],
                    "tipoEmpalmeTransportadora"                          => $registrosbandaTransportadoraHost[$i]["tipoEmpalmeTransportadora"],
                    "estadoEmpalmeTransportadora"                        => $registrosbandaTransportadoraHost[$i]["estadoEmpalmeTransportadora"],
                    "distanciaEntrePoleasBandaHorizontal"                => $registrosbandaTransportadoraHost[$i]["distanciaEntrePoleasBandaHorizontal"],
                    "inclinacionBandaHorizontal"                         => $registrosbandaTransportadoraHost[$i]["inclinacionBandaHorizontal"],
                    "recorridoUtilTensorBandaHorizontal"                 => $registrosbandaTransportadoraHost[$i]["recorridoUtilTensorBandaHorizontal"],
                    "longitudSinfinBandaHorizontal"                      => $registrosbandaTransportadoraHost[$i]["longitudSinfinBandaHorizontal"],
                    "resistenciaRoturaLonaTransportadora"                => $registrosbandaTransportadoraHost[$i]["resistenciaRoturaLonaTransportadora"],
                    "localizacionTensorTransportadora"                   => $registrosbandaTransportadoraHost[$i]["localizacionTensorTransportadora"],
                    "bandaReversible"                                    => $registrosbandaTransportadoraHost[$i]["bandaReversible"],
                    "bandaDeArrastre"                                    => $registrosbandaTransportadoraHost[$i]["bandaDeArrastre"],
                    "velocidadBandaHorizontal"                           => $registrosbandaTransportadoraHost[$i]["velocidadBandaHorizontal"],
                    "marcaBandaHorizontalAnterior"                       => $registrosbandaTransportadoraHost[$i]["marcaBandaHorizontalAnterior"],
                    "anchoBandaHorizontalAnterior"                       => $registrosbandaTransportadoraHost[$i]["anchoBandaHorizontalAnterior"],
                    "noLonasBandaHorizontalAnterior"                     => $registrosbandaTransportadoraHost[$i]["noLonasBandaHorizontalAnterior"],
                    "tipoLonaBandaHorizontalAnterior"                    => $registrosbandaTransportadoraHost[$i]["tipoLonaBandaHorizontalAnterior"],
                    "espesorTotalBandaHorizontalAnterior"                => $registrosbandaTransportadoraHost[$i]["espesorTotalBandaHorizontalAnterior"],
                    "espesorCubiertaSuperiorBandaHorizontalAnterior"     => $registrosbandaTransportadoraHost[$i]["espesorCubiertaSuperiorBandaHorizontalAnterior"],
                    "espesorCubiertaInferiorBandaHorizontalAnterior"     => $registrosbandaTransportadoraHost[$i]["espesorCubiertaInferiorBandaHorizontalAnterior"],
                    "espesorCojinBandaHorizontalAnterior"                => $registrosbandaTransportadoraHost[$i]["espesorCojinBandaHorizontalAnterior"],
                    "tipoEmpalmeBandaHorizontalAnterior"                 => $registrosbandaTransportadoraHost[$i]["tipoEmpalmeBandaHorizontalAnterior"],
                    "resistenciaRoturaLonaBandaHorizontalAnterior"       => $registrosbandaTransportadoraHost[$i]["resistenciaRoturaLonaBandaHorizontalAnterior"],
                    "tipoCubiertaBandaHorizontalAnterior"                => $registrosbandaTransportadoraHost[$i]["tipoCubiertaBandaHorizontalAnterior"],
                    "tonsTransportadasBandaHoizontalAnterior"            => $registrosbandaTransportadoraHost[$i]["tonsTransportadasBandaHoizontalAnterior"],
                    "causaFallaCambioBandaHorizontal"                    => $registrosbandaTransportadoraHost[$i]["causaFallaCambioBandaHorizontal"],
                    "diametroPoleaColaTransportadora"                    => $registrosbandaTransportadoraHost[$i]["diametroPoleaColaTransportadora"],
                    "anchoPoleaColaTransportadora"                       => $registrosbandaTransportadoraHost[$i]["anchoPoleaColaTransportadora"],
                    "tipoPoleaColaTransportadora"                        => $registrosbandaTransportadoraHost[$i]["tipoPoleaColaTransportadora"],
                    "largoEjePoleaColaTransportadora"                    => $registrosbandaTransportadoraHost[$i]["largoEjePoleaColaTransportadora"],
                    "diametroEjePoleaColaHorizontal"                     => $registrosbandaTransportadoraHost[$i]["diametroEjePoleaColaHorizontal"],
                    "icobandasCentradaPoleaColaTransportadora"           => $registrosbandaTransportadoraHost[$i]["icobandasCentradaPoleaColaTransportadora"],
                    "anguloAmarrePoleaColaTransportadora"                => $registrosbandaTransportadoraHost[$i]["anguloAmarrePoleaColaTransportadora"],
                    "estadoRvtoPoleaColaTransportadora"                  => $registrosbandaTransportadoraHost[$i]["estadoRvtoPoleaColaTransportadora"],
                    "tipoTransicionPoleaColaTransportadora"              => $registrosbandaTransportadoraHost[$i]["tipoTransicionPoleaColaTransportadora"],
                    "distanciaTransicionPoleaColaTransportadora"         => $registrosbandaTransportadoraHost[$i]["distanciaTransicionPoleaColaTransportadora"],
                    "longitudTensorTornilloPoleaColaTransportadora"      => $registrosbandaTransportadoraHost[$i]["longitudTensorTornilloPoleaColaTransportadora"],
                    "longitudRecorridoContrapesaPoleaColaTransportadora" => $registrosbandaTransportadoraHost[$i]["longitudRecorridoContrapesaPoleaColaTransportadora"],
                    "guardaPoleaColaTransportadora"                      => $registrosbandaTransportadoraHost[$i]["guardaPoleaColaTransportadora"],
                    "hayDesviador"                                       => $registrosbandaTransportadoraHost[$i]["hayDesviador"],
                    "elDesviadorBascula"                                 => $registrosbandaTransportadoraHost[$i]["elDesviadorBascula"],
                    "presionUniformeALoAnchoDeLaBanda"                   => $registrosbandaTransportadoraHost[$i]["presionUniformeALoAnchoDeLaBanda"],
                    "cauchoVPlow"                                        => $registrosbandaTransportadoraHost[$i]["cauchoVPlow"],
                    "anchoVPlow"                                         => $registrosbandaTransportadoraHost[$i]["anchoVPlow"],
                    "espesorVPlow"                                       => $registrosbandaTransportadoraHost[$i]["espesorVPlow"],
                    "tipoRevestimientoTolvaCarga"                        => $registrosbandaTransportadoraHost[$i]["tipoRevestimientoTolvaCarga"],
                    "estadoRevestimientoTolvaCarga"                      => $registrosbandaTransportadoraHost[$i]["estadoRevestimientoTolvaCarga"],
                    "duracionPromedioRevestimiento"                      => $registrosbandaTransportadoraHost[$i]["duracionPromedioRevestimiento"],
                    "deflectores"                                        => $registrosbandaTransportadoraHost[$i]["deflectores"],
                    "altureCaida"                                        => $registrosbandaTransportadoraHost[$i]["altureCaida"],
                    "longitudImpacto"                                    => $registrosbandaTransportadoraHost[$i]["longitudImpacto"],
                    "material"                                           => $registrosbandaTransportadoraHost[$i]["material"],
                    "anguloSobreCarga"                                   => $registrosbandaTransportadoraHost[$i]["anguloSobreCarga"],
                    "ataqueQuimicoTransportadora"                        => $registrosbandaTransportadoraHost[$i]["ataqueQuimicoTransportadora"],
                    "ataqueTemperaturaTransportadora"                    => $registrosbandaTransportadoraHost[$i]["ataqueTemperaturaTransportadora"],
                    "ataqueAceiteTransportadora"                         => $registrosbandaTransportadoraHost[$i]["ataqueAceiteTransportadora"],
                    "ataqueImpactoTransportadora"                        => $registrosbandaTransportadoraHost[$i]["ataqueImpactoTransportadora"],
                    "capacidadTransportadora"                            => $registrosbandaTransportadoraHost[$i]["capacidadTransportadora"],
                    "horasTrabajoPorDiaTransportadora"                   => $registrosbandaTransportadoraHost[$i]["horasTrabajoPorDiaTransportadora"],
                    "diasTrabajPorSemanaTransportadora"                  => $registrosbandaTransportadoraHost[$i]["diasTrabajPorSemanaTransportadora"],
                    "alimentacionCentradaTransportadora"                 => $registrosbandaTransportadoraHost[$i]["alimentacionCentradaTransportadora"],
                    "abrasividadTransportadora"                          => $registrosbandaTransportadoraHost[$i]["abrasividadTransportadora"],
                    "porcentajeFinosTransportadora"                      => $registrosbandaTransportadoraHost[$i]["porcentajeFinosTransportadora"],
                    "maxGranulometriaTransportadora"                     => $registrosbandaTransportadoraHost[$i]["maxGranulometriaTransportadora"],
                    "maxPesoTransportadora"                              => $registrosbandaTransportadoraHost[$i]["maxPesoTransportadora"],
                    "densidadTransportadora"                             => $registrosbandaTransportadoraHost[$i]["densidadTransportadora"],
                    "tempMaximaMaterialSobreBandaTransportadora"         => $registrosbandaTransportadoraHost[$i]["tempMaximaMaterialSobreBandaTransportadora"],
                    "tempPromedioMaterialSobreBandaTransportadora"       => $registrosbandaTransportadoraHost[$i]["tempPromedioMaterialSobreBandaTransportadora"],
                    "fugaDeMaterialesEnLaColaDelChute"                   => $registrosbandaTransportadoraHost[$i]["fugaDeMaterialesEnLaColaDelChute"],
                    "fugaDeMaterialesPorLosCostados"                     => $registrosbandaTransportadoraHost[$i]["fugaDeMaterialesPorLosCostados"],
                    "fugaMateriales"                                     => $registrosbandaTransportadoraHost[$i]["fugaMateriales"],
                    "cajaColaDeTolva"                                    => $registrosbandaTransportadoraHost[$i]["cajaColaDeTolva"],
                    "fugaDeMaterialParticulaALaSalidaDelChute"           => $registrosbandaTransportadoraHost[$i]["fugaDeMaterialParticulaALaSalidaDelChute"],
                    "anchoChute"                                         => $registrosbandaTransportadoraHost[$i]["anchoChute"],
                    "largoChute"                                         => $registrosbandaTransportadoraHost[$i]["largoChute"],
                    "alturaChute"                                        => $registrosbandaTransportadoraHost[$i]["alturaChute"],
                    "abrazadera"                                         => $registrosbandaTransportadoraHost[$i]["abrazadera"],
                    "cauchoGuardabandas"                                 => $registrosbandaTransportadoraHost[$i]["cauchoGuardabandas"],
                    "triSealMultiSeal"                                   => $registrosbandaTransportadoraHost[$i]["triSealMultiSeal"],
                    "espesorGuardaBandas"                                => $registrosbandaTransportadoraHost[$i]["espesorGuardaBandas"],
                    "anchoGuardaBandas"                                  => $registrosbandaTransportadoraHost[$i]["anchoGuardaBandas"],
                    "largoGuardaBandas"                                  => $registrosbandaTransportadoraHost[$i]["largoGuardaBandas"],
                    "protectorGuardaBandas"                              => $registrosbandaTransportadoraHost[$i]["protectorGuardaBandas"],
                    "cortinaAntiPolvo1"                                  => $registrosbandaTransportadoraHost[$i]["cortinaAntiPolvo1"],
                    "cortinaAntiPolvo2"                                  => $registrosbandaTransportadoraHost[$i]["cortinaAntiPolvo2"],
                    "cortinaAntiPolvo3"                                  => $registrosbandaTransportadoraHost[$i]["cortinaAntiPolvo3"],
                    "boquillasCanonesDeAire"                             => $registrosbandaTransportadoraHost[$i]["boquillasCanonesDeAire"],
                    "tempAmbienteMaxTransportadora"                      => $registrosbandaTransportadoraHost[$i]["tempAmbienteMaxTransportadora"],
                    "tempAmbienteMinTransportadora"                      => $registrosbandaTransportadoraHost[$i]["tempAmbienteMinTransportadora"],
                    "tieneRodillosImpacto"                               => $registrosbandaTransportadoraHost[$i]["tieneRodillosImpacto"],
                    "camaImpacto"                                        => $registrosbandaTransportadoraHost[$i]["camaImpacto"],
                    "camaSellado"                                        => $registrosbandaTransportadoraHost[$i]["camaSellado"],
                    "basculaPesaje"                                      => $registrosbandaTransportadoraHost[$i]["basculaPesaje"],
                    "rodilloCarga"                                       => $registrosbandaTransportadoraHost[$i]["rodilloCarga"],
                    "rodilloImpacto"                                     => $registrosbandaTransportadoraHost[$i]["rodilloImpacto"],
                    "basculaASGCO"                                       => $registrosbandaTransportadoraHost[$i]["basculaASGCO"],
                    "barraImpacto"                                       => $registrosbandaTransportadoraHost[$i]["barraImpacto"],
                    "barraDeslizamiento"                                 => $registrosbandaTransportadoraHost[$i]["barraDeslizamiento"],
                    "espesorUHMV"                                        => $registrosbandaTransportadoraHost[$i]["espesorUHMV"],
                    "anchoBarra"                                         => $registrosbandaTransportadoraHost[$i]["anchoBarra"],
                    "largoBarra"                                         => $registrosbandaTransportadoraHost[$i]["largoBarra"],
                    "anguloAcanalamientoArtesa1"                         => $registrosbandaTransportadoraHost[$i]["anguloAcanalamientoArtesa1"],
                    "anguloAcanalamientoArtesa2"                         => $registrosbandaTransportadoraHost[$i]["anguloAcanalamientoArtesa2"],
                    "anguloAcanalamientoArtesa3"                         => $registrosbandaTransportadoraHost[$i]["anguloAcanalamientoArtesa3"],
                    "anguloAcanalamientoArtesa1AntesPoleaMotriz"         => $registrosbandaTransportadoraHost[$i]["anguloAcanalamientoArtesa1AntesPoleaMotriz"],
                    "anguloAcanalamientoArtesa2AntesPoleaMotriz"         => $registrosbandaTransportadoraHost[$i]["anguloAcanalamientoArtesa2AntesPoleaMotriz"],
                    "anguloAcanalamientoArtesa3AntesPoleaMotriz"         => $registrosbandaTransportadoraHost[$i]["anguloAcanalamientoArtesa3AntesPoleaMotriz"],
                    "integridadSoportesRodilloImpacto"                   => $registrosbandaTransportadoraHost[$i]["integridadSoportesRodilloImpacto"],
                    "materialAtrapadoEntreCortinas"                      => $registrosbandaTransportadoraHost[$i]["materialAtrapadoEntreCortinas"],
                    "materialAtrapadoEntreGuardabandas"                  => $registrosbandaTransportadoraHost[$i]["materialAtrapadoEntreGuardabandas"],
                    "materialAtrapadoEnBanda"                            => $registrosbandaTransportadoraHost[$i]["materialAtrapadoEnBanda"],
                    "integridadSoportesCamaImpacto"                      => $registrosbandaTransportadoraHost[$i]["integridadSoportesCamaImpacto"],
                    "inclinacionZonaCargue"                              => $registrosbandaTransportadoraHost[$i]["inclinacionZonaCargue"],
                    "sistemaAlineacionCarga"                             => $registrosbandaTransportadoraHost[$i]["sistemaAlineacionCarga"],
                    "cantidadSistemaAlineacionEnCarga"                   => $registrosbandaTransportadoraHost[$i]["cantidadSistemaAlineacionEnCarga"],
                    "sistemasAlineacionCargaFuncionando"                 => $registrosbandaTransportadoraHost[$i]["sistemasAlineacionCargaFuncionando"],
                    "sistemaAlineacionEnRetorno"                         => $registrosbandaTransportadoraHost[$i]["sistemaAlineacionEnRetorno"],
                    "cantidadSistemaAlineacionEnRetorno"                 => $registrosbandaTransportadoraHost[$i]["cantidadSistemaAlineacionEnRetorno"],
                    "sistemasAlineacionRetornoFuncionando"               => $registrosbandaTransportadoraHost[$i]["sistemasAlineacionRetornoFuncionando"],
                    "sistemaAlineacionRetornoPlano"                      => $registrosbandaTransportadoraHost[$i]["sistemaAlineacionRetornoPlano"],
                    "sistemaAlineacionArtesaCarga"                       => $registrosbandaTransportadoraHost[$i]["sistemaAlineacionArtesaCarga"],
                    "sistemaAlineacionRetornoEnV"                        => $registrosbandaTransportadoraHost[$i]["sistemaAlineacionRetornoEnV"],
                    "largoEjeRodilloCentralCarga"                        => $registrosbandaTransportadoraHost[$i]["largoEjeRodilloCentralCarga"],
                    "diametroEjeRodilloCentralCarga"                     => $registrosbandaTransportadoraHost[$i]["diametroEjeRodilloCentralCarga"],
                    "largoTuboRodilloCentralCarga"                       => $registrosbandaTransportadoraHost[$i]["largoTuboRodilloCentralCarga"],
                    "largoEjeRodilloLateralCarga"                        => $registrosbandaTransportadoraHost[$i]["largoEjeRodilloLateralCarga"],
                    "diametroEjeRodilloLateralCarga"                     => $registrosbandaTransportadoraHost[$i]["diametroEjeRodilloLateralCarga"],
                    "diametroRodilloLateralCarga"                        => $registrosbandaTransportadoraHost[$i]["diametroRodilloLateralCarga"],
                    "largoTuboRodilloLateralCarga"                       => $registrosbandaTransportadoraHost[$i]["largoTuboRodilloLateralCarga"],
                    "tipoRodilloCarga"                                   => $registrosbandaTransportadoraHost[$i]["tipoRodilloCarga"],
                    "distanciaEntreArtesasCarga"                         => $registrosbandaTransportadoraHost[$i]["distanciaEntreArtesasCarga"],
                    "anchoInternoChasisRodilloCarga"                     => $registrosbandaTransportadoraHost[$i]["anchoInternoChasisRodilloCarga"],
                    "anchoExternoChasisRodilloCarga"                     => $registrosbandaTransportadoraHost[$i]["anchoExternoChasisRodilloCarga"],
                    "anguloAcanalamientoArtesaCArga"                     => $registrosbandaTransportadoraHost[$i]["anguloAcanalamientoArtesaCArga"],
                    "detalleRodilloCentralCarga"                         => $registrosbandaTransportadoraHost[$i]["detalleRodilloCentralCarga"],
                    "detalleRodilloLateralCarg"                          => $registrosbandaTransportadoraHost[$i]["detalleRodilloLateralCarg"],
                    "diametroPoleaMotrizTransportadora"                  => $registrosbandaTransportadoraHost[$i]["diametroPoleaMotrizTransportadora"],
                    "anchoPoleaMotrizTransportadora"                     => $registrosbandaTransportadoraHost[$i]["anchoPoleaMotrizTransportadora"],
                    "tipoPoleaMotrizTransportadora"                      => $registrosbandaTransportadoraHost[$i]["tipoPoleaMotrizTransportadora"],
                    "largoEjePoleaMotrizTransportadora"                  => $registrosbandaTransportadoraHost[$i]["largoEjePoleaMotrizTransportadora"],
                    "diametroEjeMotrizTransportadora"                    => $registrosbandaTransportadoraHost[$i]["diametroEjeMotrizTransportadora"],
                    "icobandasCentraEnPoleaMotrizTransportadora"         => $registrosbandaTransportadoraHost[$i]["icobandasCentraEnPoleaMotrizTransportadora"],
                    "anguloAmarrePoleaMotrizTransportadora"              => $registrosbandaTransportadoraHost[$i]["anguloAmarrePoleaMotrizTransportadora"],
                    "estadoRevestimientoPoleaMotrizTransportadora"       => $registrosbandaTransportadoraHost[$i]["estadoRevestimientoPoleaMotrizTransportadora"],
                    "tipoTransicionPoleaMotrizTransportadora"            => $registrosbandaTransportadoraHost[$i]["tipoTransicionPoleaMotrizTransportadora"],
                    "distanciaTransicionPoleaMotrizTransportadora"       => $registrosbandaTransportadoraHost[$i]["distanciaTransicionPoleaMotrizTransportadora"],
                    "potenciaMotorTransportadora"                        => $registrosbandaTransportadoraHost[$i]["potenciaMotorTransportadora"],
                    "guardaPoleaMotrizTransportadora"                    => $registrosbandaTransportadoraHost[$i]["guardaPoleaMotrizTransportadora"],
                    "anchoEstructura"                                    => $registrosbandaTransportadoraHost[$i]["anchoEstructura"],
                    "anchoTrayectoCarga"                                 => $registrosbandaTransportadoraHost[$i]["anchoTrayectoCarga"],
                    "pasarelaRespectoAvanceBanda"                        => $registrosbandaTransportadoraHost[$i]["pasarelaRespectoAvanceBanda"],
                    "materialAlimenticioTransportadora"                  => $registrosbandaTransportadoraHost[$i]["materialAlimenticioTransportadora"],
                    "materialAcidoTransportadora"                        => $registrosbandaTransportadoraHost[$i]["materialAcidoTransportadora"],
                    "materialTempEntre80y150Transportadora"              => $registrosbandaTransportadoraHost[$i]["materialTempEntre80y150Transportadora"],
                    "materialSecoTransportadora"                         => $registrosbandaTransportadoraHost[$i]["materialSecoTransportadora"],
                    "materialHumedoTransportadora"                       => $registrosbandaTransportadoraHost[$i]["materialHumedoTransportadora"],
                    "materialAbrasivoFinoTransportadora"                 => $registrosbandaTransportadoraHost[$i]["materialAbrasivoFinoTransportadora"],
                    "materialPegajosoTransportadora"                     => $registrosbandaTransportadoraHost[$i]["materialPegajosoTransportadora"],
                    "materialGrasosoAceitosoTransportadora"              => $registrosbandaTransportadoraHost[$i]["materialGrasosoAceitosoTransportadora"],
                    "marcaLimpiadorPrimario"                             => $registrosbandaTransportadoraHost[$i]["marcaLimpiadorPrimario"],
                    "referenciaLimpiadorPrimario"                        => $registrosbandaTransportadoraHost[$i]["referenciaLimpiadorPrimario"],
                    "anchoCuchillaLimpiadorPrimario"                     => $registrosbandaTransportadoraHost[$i]["anchoCuchillaLimpiadorPrimario"],
                    "altoCuchillaLimpiadorPrimario"                      => $registrosbandaTransportadoraHost[$i]["altoCuchillaLimpiadorPrimario"],
                    "estadoCuchillaLimpiadorPrimario"                    => $registrosbandaTransportadoraHost[$i]["estadoCuchillaLimpiadorPrimario"],
                    "estadoTensorLimpiadorPrimario"                      => $registrosbandaTransportadoraHost[$i]["estadoTensorLimpiadorPrimario"],
                    "estadoTuboLimpiadorPrimario"                        => $registrosbandaTransportadoraHost[$i]["estadoTuboLimpiadorPrimario"],
                    "frecuenciaRevisionCuchilla"                         => $registrosbandaTransportadoraHost[$i]["frecuenciaRevisionCuchilla"],
                    "cuchillaEnContactoConBanda"                         => $registrosbandaTransportadoraHost[$i]["cuchillaEnContactoConBanda"],
                    "marcaLimpiadorSecundario"                           => $registrosbandaTransportadoraHost[$i]["marcaLimpiadorSecundario"],
                    "referenciaLimpiadorSecundario"                      => $registrosbandaTransportadoraHost[$i]["referenciaLimpiadorSecundario"],
                    "anchoCuchillaLimpiadorSecundario"                   => $registrosbandaTransportadoraHost[$i]["anchoCuchillaLimpiadorSecundario"],
                    "altoCuchillaLimpiadorSecundario"                    => $registrosbandaTransportadoraHost[$i]["altoCuchillaLimpiadorSecundario"],
                    "estadoCuchillaLimpiadorSecundario"                  => $registrosbandaTransportadoraHost[$i]["estadoCuchillaLimpiadorSecundario"],
                    "estadoTensorLimpiadorSecundario"                    => $registrosbandaTransportadoraHost[$i]["estadoTensorLimpiadorSecundario"],
                    "estadoTuboLimpiadorSecundario"                      => $registrosbandaTransportadoraHost[$i]["estadoTuboLimpiadorSecundario"],
                    "frecuenciaRevisionCuchilla1"                        => $registrosbandaTransportadoraHost[$i]["frecuenciaRevisionCuchilla1"],
                    "cuchillaEnContactoConBanda1"                        => $registrosbandaTransportadoraHost[$i]["cuchillaEnContactoConBanda1"],
                    "sistemaDribbleChute"                                => $registrosbandaTransportadoraHost[$i]["sistemaDribbleChute"],
                    "marcaLimpiadorTerciario"                            => $registrosbandaTransportadoraHost[$i]["marcaLimpiadorTerciario"],
                    "referenciaLimpiadorTerciario"                       => $registrosbandaTransportadoraHost[$i]["referenciaLimpiadorTerciario"],
                    "anchoCuchillaLimpiadorTerciario"                    => $registrosbandaTransportadoraHost[$i]["anchoCuchillaLimpiadorTerciario"],
                    "altoCuchillaLimpiadorTerciario"                     => $registrosbandaTransportadoraHost[$i]["altoCuchillaLimpiadorTerciario"],
                    "estadoCuchillaLimpiadorTerciario"                   => $registrosbandaTransportadoraHost[$i]["estadoCuchillaLimpiadorTerciario"],
                    "estadoTensorLimpiadorTerciario"                     => $registrosbandaTransportadoraHost[$i]["estadoTensorLimpiadorTerciario"],
                    "estadoTuboLimpiadorTerciario"                       => $registrosbandaTransportadoraHost[$i]["estadoTuboLimpiadorTerciario"],
                    "frecuenciaRevisionCuchilla2"                        => $registrosbandaTransportadoraHost[$i]["frecuenciaRevisionCuchilla2"],
                    "cuchillaEnContactoConBanda2"                        => $registrosbandaTransportadoraHost[$i]["cuchillaEnContactoConBanda2"],
                    "estadoRodilloRetorno"                               => $registrosbandaTransportadoraHost[$i]["estadoRodilloRetorno"],
                    "largoEjeRodilloRetorno"                             => $registrosbandaTransportadoraHost[$i]["largoEjeRodilloRetorno"],
                    "diametroEjeRodilloRetorno"                          => $registrosbandaTransportadoraHost[$i]["diametroEjeRodilloRetorno"],
                    "diametroRodilloRetorno"                             => $registrosbandaTransportadoraHost[$i]["diametroRodilloRetorno"],
                    "largoTuboRodilloRetorno"                            => $registrosbandaTransportadoraHost[$i]["largoTuboRodilloRetorno"],
                    "tipoRodilloRetorno"                                 => $registrosbandaTransportadoraHost[$i]["tipoRodilloRetorno"],
                    "distanciaEntreRodillosRetorno"                      => $registrosbandaTransportadoraHost[$i]["distanciaEntreRodillosRetorno"],
                    "anchoInternoChasisRetorno"                          => $registrosbandaTransportadoraHost[$i]["anchoInternoChasisRetorno"],
                    "anchoExternoChasisRetorno"                          => $registrosbandaTransportadoraHost[$i]["anchoExternoChasisRetorno"],
                    "detalleRodilloRetorno"                              => $registrosbandaTransportadoraHost[$i]["detalleRodilloRetorno"],
                    "diametroPoleaAmarrePoleaMotriz"                     => $registrosbandaTransportadoraHost[$i]["diametroPoleaAmarrePoleaMotriz"],
                    "anchoPoleaAmarrePoleaMotriz"                        => $registrosbandaTransportadoraHost[$i]["anchoPoleaAmarrePoleaMotriz"],
                    "tipoPoleaAmarrePoleaMotriz"                         => $registrosbandaTransportadoraHost[$i]["tipoPoleaAmarrePoleaMotriz"],
                    "largoEjePoleaAmarrePoleaMotriz"                     => $registrosbandaTransportadoraHost[$i]["largoEjePoleaAmarrePoleaMotriz"],
                    "diametroEjePoleaAmarrePoleaMotriz"                  => $registrosbandaTransportadoraHost[$i]["diametroEjePoleaAmarrePoleaMotriz"],
                    "icobandasCentradaPoleaAmarrePoleaMotriz"            => $registrosbandaTransportadoraHost[$i]["icobandasCentradaPoleaAmarrePoleaMotriz"],
                    "estadoRevestimientoPoleaAmarrePoleaMotriz"          => $registrosbandaTransportadoraHost[$i]["estadoRevestimientoPoleaAmarrePoleaMotriz"],
                    "dimetroPoleaAmarrePoleaCola"                        => $registrosbandaTransportadoraHost[$i]["dimetroPoleaAmarrePoleaCola"],
                    "anchoPoleaAmarrePoleaCola"                          => $registrosbandaTransportadoraHost[$i]["anchoPoleaAmarrePoleaCola"],
                    "largoEjePoleaAmarrePoleaCola"                       => $registrosbandaTransportadoraHost[$i]["largoEjePoleaAmarrePoleaCola"],
                    "tipoPoleaAmarrePoleaCola"                           => $registrosbandaTransportadoraHost[$i]["tipoPoleaAmarrePoleaCola"],
                    "diametroEjePoleaAmarrePoleaCola"                    => $registrosbandaTransportadoraHost[$i]["diametroEjePoleaAmarrePoleaCola"],
                    "icobandasCentradaPoleaAmarrePoleaCola"              => $registrosbandaTransportadoraHost[$i]["icobandasCentradaPoleaAmarrePoleaCola"],
                    "estadoRevestimientoPoleaAmarrePoleaCola"            => $registrosbandaTransportadoraHost[$i]["estadoRevestimientoPoleaAmarrePoleaCola"],
                    "diametroPoleaTensora"                               => $registrosbandaTransportadoraHost[$i]["diametroPoleaTensora"],
                    "anchoPoleaTensora"                                  => $registrosbandaTransportadoraHost[$i]["anchoPoleaTensora"],
                    "tipoPoleaTensora"                                   => $registrosbandaTransportadoraHost[$i]["tipoPoleaTensora"],
                    "largoEjePoleaTensora"                               => $registrosbandaTransportadoraHost[$i]["largoEjePoleaTensora"],
                    "diametroEjePoleaTensora"                            => $registrosbandaTransportadoraHost[$i]["diametroEjePoleaTensora"],
                    "icobandasCentradaEnPoleaTensora"                    => $registrosbandaTransportadoraHost[$i]["icobandasCentradaEnPoleaTensora"],
                    "recorridoPoleaTensora"                              => $registrosbandaTransportadoraHost[$i]["recorridoPoleaTensora"],
                    "estadoRevestimientoPoleaTensora"                    => $registrosbandaTransportadoraHost[$i]["estadoRevestimientoPoleaTensora"],
                    "tipoTransicionPoleaTensora"                         => $registrosbandaTransportadoraHost[$i]["tipoTransicionPoleaTensora"],
                    "distanciaTransicionPoleaColaTensora"                => $registrosbandaTransportadoraHost[$i]["distanciaTransicionPoleaColaTensora"],
                    "potenciaMotorPoleaTensora"                          => $registrosbandaTransportadoraHost[$i]["potenciaMotorPoleaTensora"],
                    "guardaPoleaTensora"                                 => $registrosbandaTransportadoraHost[$i]["guardaPoleaTensora"],
                    "puertasInspeccion"                                  => $registrosbandaTransportadoraHost[$i]["puertasInspeccion"],
                    "guardaRodilloRetornoPlano"                          => $registrosbandaTransportadoraHost[$i]["guardaRodilloRetornoPlano"],
                    "guardaTruTrainer"                                   => $registrosbandaTransportadoraHost[$i]["guardaTruTrainer"],
                    "guardaPoleaDeflectora"                              => $registrosbandaTransportadoraHost[$i]["guardaPoleaDeflectora"],
                    "guardaZonaDeTransito"                               => $registrosbandaTransportadoraHost[$i]["guardaZonaDeTransito"],
                    "guardaMotores"                                      => $registrosbandaTransportadoraHost[$i]["guardaMotores"],
                    "guardaCadenas"                                      => $registrosbandaTransportadoraHost[$i]["guardaCadenas"],
                    "guardaCorreas"                                      => $registrosbandaTransportadoraHost[$i]["guardaCorreas"],
                    "interruptoresDeSeguridad"                           => $registrosbandaTransportadoraHost[$i]["interruptoresDeSeguridad"],
                    "sirenasDeSeguridad"                                 => $registrosbandaTransportadoraHost[$i]["sirenasDeSeguridad"],
                    "guardaRodilloRetornoV"                              => $registrosbandaTransportadoraHost[$i]["guardaRodilloRetornoV"],
                    "diametroRodilloCentralCarga"                        => $registrosbandaTransportadoraHost[$i]["diametroRodilloCentralCarga"],
                    "tipoRodilloImpacto"                                 => $registrosbandaTransportadoraHost[$i]["tipoRodilloImpacto"],
                    "integridadSoporteCamaSellado"                       => $registrosbandaTransportadoraHost[$i]["integridadSoporteCamaSellado"],
                    "ataqueAbrasivoTransportadora"                       => $registrosbandaTransportadoraHost[$i]["ataqueAbrasivoTransportadora"],
                    "observacionRegistroTransportadora"                  => $registrosbandaTransportadoraHost[$i]["observacionRegistroTransportadora"]
                );
                $respuesta = ModeloSincronizacion::mdlDatosTransportadora($tabla, $datos);
                if ($respuesta == "ok") {
                    $respuesta = ModeloSincronizacion::mdlActualizarTransportadoraHost("bandaTransportadora", $registrosbandaTransportadoraHost[$i]["idRegistro"]);
                    if ($respuesta == "ok") {
                        // echo "DATOS SINCRONIZADOS CORRECTAMENTE";
                    }
                }
                for ($i = 0; $i < count($registrosBandaTransportadoraActualizar); $i++) {
                    $datos = array(
                        "idRegistro"                          => $registrosBandaTransportadoraActualizar[$i]["idRegistro"],
                        "marcaBandaTransportadora"                           => $registrosBandaTransportadoraActualizar[$i]["marcaBandaTransportadora"],
                        "anchoBandaTransportadora"                           => $registrosBandaTransportadoraActualizar[$i]["anchoBandaTransportadora"],
                        "noLonasBandaTransportadora"                         => $registrosBandaTransportadoraActualizar[$i]["noLonasBandaTransportadora"],
                        "tipoLonaBandaTransportadora"                        => $registrosBandaTransportadoraActualizar[$i]["tipoLonaBandaTransportadora"],
                        "espesorTotalBandaTransportadora"                    => $registrosBandaTransportadoraActualizar[$i]["espesorTotalBandaTransportadora"],
                        "espesorCubiertaSuperiorTransportadora"              => $registrosBandaTransportadoraActualizar[$i]["espesorCubiertaSuperiorTransportadora"],
                        "espesorCojinTransportadora"                         => $registrosBandaTransportadoraActualizar[$i]["espesorCojinTransportadora"],
                        "espesorCubiertaInferiorTransportadora"              => $registrosBandaTransportadoraActualizar[$i]["espesorCubiertaInferiorTransportadora"],
                        "tipoCubiertaTransportadora"                         => $registrosBandaTransportadoraActualizar[$i]["tipoCubiertaTransportadora"],
                        "tipoEmpalmeTransportadora"                          => $registrosBandaTransportadoraActualizar[$i]["tipoEmpalmeTransportadora"],
                        "estadoEmpalmeTransportadora"                        => $registrosBandaTransportadoraActualizar[$i]["estadoEmpalmeTransportadora"],
                        "distanciaEntrePoleasBandaHorizontal"                => $registrosBandaTransportadoraActualizar[$i]["distanciaEntrePoleasBandaHorizontal"],
                        "inclinacionBandaHorizontal"                         => $registrosBandaTransportadoraActualizar[$i]["inclinacionBandaHorizontal"],
                        "recorridoUtilTensorBandaHorizontal"                 => $registrosBandaTransportadoraActualizar[$i]["recorridoUtilTensorBandaHorizontal"],
                        "longitudSinfinBandaHorizontal"                      => $registrosBandaTransportadoraActualizar[$i]["longitudSinfinBandaHorizontal"],
                        "resistenciaRoturaLonaTransportadora"                => $registrosBandaTransportadoraActualizar[$i]["resistenciaRoturaLonaTransportadora"],
                        "localizacionTensorTransportadora"                   => $registrosBandaTransportadoraActualizar[$i]["localizacionTensorTransportadora"],
                        "bandaReversible"                                    => $registrosBandaTransportadoraActualizar[$i]["bandaReversible"],
                        "bandaDeArrastre"                                    => $registrosBandaTransportadoraActualizar[$i]["bandaDeArrastre"],
                        "velocidadBandaHorizontal"                           => $registrosBandaTransportadoraActualizar[$i]["velocidadBandaHorizontal"],
                        "marcaBandaHorizontalAnterior"                       => $registrosBandaTransportadoraActualizar[$i]["marcaBandaHorizontalAnterior"],
                        "anchoBandaHorizontalAnterior"                       => $registrosBandaTransportadoraActualizar[$i]["anchoBandaHorizontalAnterior"],
                        "noLonasBandaHorizontalAnterior"                     => $registrosBandaTransportadoraActualizar[$i]["noLonasBandaHorizontalAnterior"],
                        "tipoLonaBandaHorizontalAnterior"                    => $registrosBandaTransportadoraActualizar[$i]["tipoLonaBandaHorizontalAnterior"],
                        "espesorTotalBandaHorizontalAnterior"                => $registrosBandaTransportadoraActualizar[$i]["espesorTotalBandaHorizontalAnterior"],
                        "espesorCubiertaSuperiorBandaHorizontalAnterior"     => $registrosBandaTransportadoraActualizar[$i]["espesorCubiertaSuperiorBandaHorizontalAnterior"],
                        "espesorCubiertaInferiorBandaHorizontalAnterior"     => $registrosBandaTransportadoraActualizar[$i]["espesorCubiertaInferiorBandaHorizontalAnterior"],
                        "espesorCojinBandaHorizontalAnterior"                => $registrosBandaTransportadoraActualizar[$i]["espesorCojinBandaHorizontalAnterior"],
                        "tipoEmpalmeBandaHorizontalAnterior"                 => $registrosBandaTransportadoraActualizar[$i]["tipoEmpalmeBandaHorizontalAnterior"],
                        "resistenciaRoturaLonaBandaHorizontalAnterior"       => $registrosBandaTransportadoraActualizar[$i]["resistenciaRoturaLonaBandaHorizontalAnterior"],
                        "tipoCubiertaBandaHorizontalAnterior"                => $registrosBandaTransportadoraActualizar[$i]["tipoCubiertaBandaHorizontalAnterior"],
                        "tonsTransportadasBandaHoizontalAnterior"            => $registrosBandaTransportadoraActualizar[$i]["tonsTransportadasBandaHoizontalAnterior"],
                        "causaFallaCambioBandaHorizontal"                    => $registrosBandaTransportadoraActualizar[$i]["causaFallaCambioBandaHorizontal"],
                        "diametroPoleaColaTransportadora"                    => $registrosBandaTransportadoraActualizar[$i]["diametroPoleaColaTransportadora"],
                        "anchoPoleaColaTransportadora"                       => $registrosBandaTransportadoraActualizar[$i]["anchoPoleaColaTransportadora"],
                        "tipoPoleaColaTransportadora"                        => $registrosBandaTransportadoraActualizar[$i]["tipoPoleaColaTransportadora"],
                        "largoEjePoleaColaTransportadora"                    => $registrosBandaTransportadoraActualizar[$i]["largoEjePoleaColaTransportadora"],
                        "diametroEjePoleaColaHorizontal"                     => $registrosBandaTransportadoraActualizar[$i]["diametroEjePoleaColaHorizontal"],
                        "icobandasCentradaPoleaColaTransportadora"           => $registrosBandaTransportadoraActualizar[$i]["icobandasCentradaPoleaColaTransportadora"],
                        "anguloAmarrePoleaColaTransportadora"                => $registrosBandaTransportadoraActualizar[$i]["anguloAmarrePoleaColaTransportadora"],
                        "estadoRvtoPoleaColaTransportadora"                  => $registrosBandaTransportadoraActualizar[$i]["estadoRvtoPoleaColaTransportadora"],
                        "tipoTransicionPoleaColaTransportadora"              => $registrosBandaTransportadoraActualizar[$i]["tipoTransicionPoleaColaTransportadora"],
                        "distanciaTransicionPoleaColaTransportadora"         => $registrosBandaTransportadoraActualizar[$i]["distanciaTransicionPoleaColaTransportadora"],
                        "longitudTensorTornilloPoleaColaTransportadora"      => $registrosBandaTransportadoraActualizar[$i]["longitudTensorTornilloPoleaColaTransportadora"],
                        "longitudRecorridoContrapesaPoleaColaTransportadora" => $registrosBandaTransportadoraActualizar[$i]["longitudRecorridoContrapesaPoleaColaTransportadora"],
                        "guardaPoleaColaTransportadora"                      => $registrosBandaTransportadoraActualizar[$i]["guardaPoleaColaTransportadora"],
                        "hayDesviador"                                       => $registrosBandaTransportadoraActualizar[$i]["hayDesviador"],
                        "elDesviadorBascula"                                 => $registrosBandaTransportadoraActualizar[$i]["elDesviadorBascula"],
                        "presionUniformeALoAnchoDeLaBanda"                   => $registrosBandaTransportadoraActualizar[$i]["presionUniformeALoAnchoDeLaBanda"],
                        "cauchoVPlow"                                        => $registrosBandaTransportadoraActualizar[$i]["cauchoVPlow"],
                        "anchoVPlow"                                         => $registrosBandaTransportadoraActualizar[$i]["anchoVPlow"],
                        "espesorVPlow"                                       => $registrosBandaTransportadoraActualizar[$i]["espesorVPlow"],
                        "tipoRevestimientoTolvaCarga"                        => $registrosBandaTransportadoraActualizar[$i]["tipoRevestimientoTolvaCarga"],
                        "estadoRevestimientoTolvaCarga"                      => $registrosBandaTransportadoraActualizar[$i]["estadoRevestimientoTolvaCarga"],
                        "duracionPromedioRevestimiento"                      => $registrosBandaTransportadoraActualizar[$i]["duracionPromedioRevestimiento"],
                        "deflectores"                                        => $registrosBandaTransportadoraActualizar[$i]["deflectores"],
                        "altureCaida"                                        => $registrosBandaTransportadoraActualizar[$i]["altureCaida"],
                        "longitudImpacto"                                    => $registrosBandaTransportadoraActualizar[$i]["longitudImpacto"],
                        "material"                                           => $registrosBandaTransportadoraActualizar[$i]["material"],
                        "anguloSobreCarga"                                   => $registrosBandaTransportadoraActualizar[$i]["anguloSobreCarga"],
                        "ataqueQuimicoTransportadora"                        => $registrosBandaTransportadoraActualizar[$i]["ataqueQuimicoTransportadora"],
                        "ataqueTemperaturaTransportadora"                    => $registrosBandaTransportadoraActualizar[$i]["ataqueTemperaturaTransportadora"],
                        "ataqueAceiteTransportadora"                         => $registrosBandaTransportadoraActualizar[$i]["ataqueAceiteTransportadora"],
                        "ataqueImpactoTransportadora"                        => $registrosBandaTransportadoraActualizar[$i]["ataqueImpactoTransportadora"],
                        "capacidadTransportadora"                            => $registrosBandaTransportadoraActualizar[$i]["capacidadTransportadora"],
                        "horasTrabajoPorDiaTransportadora"                   => $registrosBandaTransportadoraActualizar[$i]["horasTrabajoPorDiaTransportadora"],
                        "diasTrabajPorSemanaTransportadora"                  => $registrosBandaTransportadoraActualizar[$i]["diasTrabajPorSemanaTransportadora"],
                        "alimentacionCentradaTransportadora"                 => $registrosBandaTransportadoraActualizar[$i]["alimentacionCentradaTransportadora"],
                        "abrasividadTransportadora"                          => $registrosBandaTransportadoraActualizar[$i]["abrasividadTransportadora"],
                        "porcentajeFinosTransportadora"                      => $registrosBandaTransportadoraActualizar[$i]["porcentajeFinosTransportadora"],
                        "maxGranulometriaTransportadora"                     => $registrosBandaTransportadoraActualizar[$i]["maxGranulometriaTransportadora"],
                        "maxPesoTransportadora"                              => $registrosBandaTransportadoraActualizar[$i]["maxPesoTransportadora"],
                        "densidadTransportadora"                             => $registrosBandaTransportadoraActualizar[$i]["densidadTransportadora"],
                        "tempMaximaMaterialSobreBandaTransportadora"         => $registrosBandaTransportadoraActualizar[$i]["tempMaximaMaterialSobreBandaTransportadora"],
                        "tempPromedioMaterialSobreBandaTransportadora"       => $registrosBandaTransportadoraActualizar[$i]["tempPromedioMaterialSobreBandaTransportadora"],
                        "fugaDeMaterialesEnLaColaDelChute"                   => $registrosBandaTransportadoraActualizar[$i]["fugaDeMaterialesEnLaColaDelChute"],
                        "fugaDeMaterialesPorLosCostados"                     => $registrosBandaTransportadoraActualizar[$i]["fugaDeMaterialesPorLosCostados"],
                        "fugaMateriales"                                     => $registrosBandaTransportadoraActualizar[$i]["fugaMateriales"],
                        "cajaColaDeTolva"                                    => $registrosBandaTransportadoraActualizar[$i]["cajaColaDeTolva"],
                        "fugaDeMaterialParticulaALaSalidaDelChute"           => $registrosBandaTransportadoraActualizar[$i]["fugaDeMaterialParticulaALaSalidaDelChute"],
                        "anchoChute"                                         => $registrosBandaTransportadoraActualizar[$i]["anchoChute"],
                        "largoChute"                                         => $registrosBandaTransportadoraActualizar[$i]["largoChute"],
                        "alturaChute"                                        => $registrosBandaTransportadoraActualizar[$i]["alturaChute"],
                        "abrazadera"                                         => $registrosBandaTransportadoraActualizar[$i]["abrazadera"],
                        "cauchoGuardabandas"                                 => $registrosBandaTransportadoraActualizar[$i]["cauchoGuardabandas"],
                        "triSealMultiSeal"                                   => $registrosBandaTransportadoraActualizar[$i]["triSealMultiSeal"],
                        "espesorGuardaBandas"                                => $registrosBandaTransportadoraActualizar[$i]["espesorGuardaBandas"],
                        "anchoGuardaBandas"                                  => $registrosBandaTransportadoraActualizar[$i]["anchoGuardaBandas"],
                        "largoGuardaBandas"                                  => $registrosBandaTransportadoraActualizar[$i]["largoGuardaBandas"],
                        "protectorGuardaBandas"                              => $registrosBandaTransportadoraActualizar[$i]["protectorGuardaBandas"],
                        "cortinaAntiPolvo1"                                  => $registrosBandaTransportadoraActualizar[$i]["cortinaAntiPolvo1"],
                        "cortinaAntiPolvo2"                                  => $registrosBandaTransportadoraActualizar[$i]["cortinaAntiPolvo2"],
                        "cortinaAntiPolvo3"                                  => $registrosBandaTransportadoraActualizar[$i]["cortinaAntiPolvo3"],
                        "boquillasCanonesDeAire"                             => $registrosBandaTransportadoraActualizar[$i]["boquillasCanonesDeAire"],
                        "tempAmbienteMaxTransportadora"                      => $registrosBandaTransportadoraActualizar[$i]["tempAmbienteMaxTransportadora"],
                        "tempAmbienteMinTransportadora"                      => $registrosBandaTransportadoraActualizar[$i]["tempAmbienteMinTransportadora"],
                        "tieneRodillosImpacto"                               => $registrosBandaTransportadoraActualizar[$i]["tieneRodillosImpacto"],
                        "camaImpacto"                                        => $registrosBandaTransportadoraActualizar[$i]["camaImpacto"],
                        "camaSellado"                                        => $registrosBandaTransportadoraActualizar[$i]["camaSellado"],
                        "basculaPesaje"                                      => $registrosBandaTransportadoraActualizar[$i]["basculaPesaje"],
                        "rodilloCarga"                                       => $registrosBandaTransportadoraActualizar[$i]["rodilloCarga"],
                        "rodilloImpacto"                                     => $registrosBandaTransportadoraActualizar[$i]["rodilloImpacto"],
                        "basculaASGCO"                                       => $registrosBandaTransportadoraActualizar[$i]["basculaASGCO"],
                        "barraImpacto"                                       => $registrosBandaTransportadoraActualizar[$i]["barraImpacto"],
                        "barraDeslizamiento"                                 => $registrosBandaTransportadoraActualizar[$i]["barraDeslizamiento"],
                        "espesorUHMV"                                        => $registrosBandaTransportadoraActualizar[$i]["espesorUHMV"],
                        "anchoBarra"                                         => $registrosBandaTransportadoraActualizar[$i]["anchoBarra"],
                        "largoBarra"                                         => $registrosBandaTransportadoraActualizar[$i]["largoBarra"],
                        "anguloAcanalamientoArtesa1"                         => $registrosBandaTransportadoraActualizar[$i]["anguloAcanalamientoArtesa1"],
                        "anguloAcanalamientoArtesa2"                         => $registrosBandaTransportadoraActualizar[$i]["anguloAcanalamientoArtesa2"],
                        "anguloAcanalamientoArtesa3"                         => $registrosBandaTransportadoraActualizar[$i]["anguloAcanalamientoArtesa3"],
                        "anguloAcanalamientoArtesa1AntesPoleaMotriz"         => $registrosBandaTransportadoraActualizar[$i]["anguloAcanalamientoArtesa1AntesPoleaMotriz"],
                        "anguloAcanalamientoArtesa2AntesPoleaMotriz"         => $registrosBandaTransportadoraActualizar[$i]["anguloAcanalamientoArtesa2AntesPoleaMotriz"],
                        "anguloAcanalamientoArtesa3AntesPoleaMotriz"         => $registrosBandaTransportadoraActualizar[$i]["anguloAcanalamientoArtesa3AntesPoleaMotriz"],
                        "integridadSoportesRodilloImpacto"                   => $registrosBandaTransportadoraActualizar[$i]["integridadSoportesRodilloImpacto"],
                        "materialAtrapadoEntreCortinas"                      => $registrosBandaTransportadoraActualizar[$i]["materialAtrapadoEntreCortinas"],
                        "materialAtrapadoEntreGuardabandas"                  => $registrosBandaTransportadoraActualizar[$i]["materialAtrapadoEntreGuardabandas"],
                        "materialAtrapadoEnBanda"                            => $registrosBandaTransportadoraActualizar[$i]["materialAtrapadoEnBanda"],
                        "integridadSoportesCamaImpacto"                      => $registrosBandaTransportadoraActualizar[$i]["integridadSoportesCamaImpacto"],
                        "inclinacionZonaCargue"                              => $registrosBandaTransportadoraActualizar[$i]["inclinacionZonaCargue"],
                        "sistemaAlineacionCarga"                             => $registrosBandaTransportadoraActualizar[$i]["sistemaAlineacionCarga"],
                        "cantidadSistemaAlineacionEnCarga"                   => $registrosBandaTransportadoraActualizar[$i]["cantidadSistemaAlineacionEnCarga"],
                        "sistemasAlineacionCargaFuncionando"                 => $registrosBandaTransportadoraActualizar[$i]["sistemasAlineacionCargaFuncionando"],
                        "sistemaAlineacionEnRetorno"                         => $registrosBandaTransportadoraActualizar[$i]["sistemaAlineacionEnRetorno"],
                        "cantidadSistemaAlineacionEnRetorno"                 => $registrosBandaTransportadoraActualizar[$i]["cantidadSistemaAlineacionEnRetorno"],
                        "sistemasAlineacionRetornoFuncionando"               => $registrosBandaTransportadoraActualizar[$i]["sistemasAlineacionRetornoFuncionando"],
                        "sistemaAlineacionRetornoPlano"                      => $registrosBandaTransportadoraActualizar[$i]["sistemaAlineacionRetornoPlano"],
                        "sistemaAlineacionArtesaCarga"                       => $registrosBandaTransportadoraActualizar[$i]["sistemaAlineacionArtesaCarga"],
                        "sistemaAlineacionRetornoEnV"                        => $registrosBandaTransportadoraActualizar[$i]["sistemaAlineacionRetornoEnV"],
                        "largoEjeRodilloCentralCarga"                        => $registrosBandaTransportadoraActualizar[$i]["largoEjeRodilloCentralCarga"],
                        "diametroEjeRodilloCentralCarga"                     => $registrosBandaTransportadoraActualizar[$i]["diametroEjeRodilloCentralCarga"],
                        "largoTuboRodilloCentralCarga"                       => $registrosBandaTransportadoraActualizar[$i]["largoTuboRodilloCentralCarga"],
                        "largoEjeRodilloLateralCarga"                        => $registrosBandaTransportadoraActualizar[$i]["largoEjeRodilloLateralCarga"],
                        "diametroEjeRodilloLateralCarga"                     => $registrosBandaTransportadoraActualizar[$i]["diametroEjeRodilloLateralCarga"],
                        "diametroRodilloLateralCarga"                        => $registrosBandaTransportadoraActualizar[$i]["diametroRodilloLateralCarga"],
                        "largoTuboRodilloLateralCarga"                       => $registrosBandaTransportadoraActualizar[$i]["largoTuboRodilloLateralCarga"],
                        "tipoRodilloCarga"                                   => $registrosBandaTransportadoraActualizar[$i]["tipoRodilloCarga"],
                        "distanciaEntreArtesasCarga"                         => $registrosBandaTransportadoraActualizar[$i]["distanciaEntreArtesasCarga"],
                        "anchoInternoChasisRodilloCarga"                     => $registrosBandaTransportadoraActualizar[$i]["anchoInternoChasisRodilloCarga"],
                        "anchoExternoChasisRodilloCarga"                     => $registrosBandaTransportadoraActualizar[$i]["anchoExternoChasisRodilloCarga"],
                        "anguloAcanalamientoArtesaCArga"                     => $registrosBandaTransportadoraActualizar[$i]["anguloAcanalamientoArtesaCArga"],
                        "detalleRodilloCentralCarga"                         => $registrosBandaTransportadoraActualizar[$i]["detalleRodilloCentralCarga"],
                        "detalleRodilloLateralCarg"                          => $registrosBandaTransportadoraActualizar[$i]["detalleRodilloLateralCarg"],
                        "diametroPoleaMotrizTransportadora"                  => $registrosBandaTransportadoraActualizar[$i]["diametroPoleaMotrizTransportadora"],
                        "anchoPoleaMotrizTransportadora"                     => $registrosBandaTransportadoraActualizar[$i]["anchoPoleaMotrizTransportadora"],
                        "tipoPoleaMotrizTransportadora"                      => $registrosBandaTransportadoraActualizar[$i]["tipoPoleaMotrizTransportadora"],
                        "largoEjePoleaMotrizTransportadora"                  => $registrosBandaTransportadoraActualizar[$i]["largoEjePoleaMotrizTransportadora"],
                        "diametroEjeMotrizTransportadora"                    => $registrosBandaTransportadoraActualizar[$i]["diametroEjeMotrizTransportadora"],
                        "icobandasCentraEnPoleaMotrizTransportadora"         => $registrosBandaTransportadoraActualizar[$i]["icobandasCentraEnPoleaMotrizTransportadora"],
                        "anguloAmarrePoleaMotrizTransportadora"              => $registrosBandaTransportadoraActualizar[$i]["anguloAmarrePoleaMotrizTransportadora"],
                        "estadoRevestimientoPoleaMotrizTransportadora"       => $registrosBandaTransportadoraActualizar[$i]["estadoRevestimientoPoleaMotrizTransportadora"],
                        "tipoTransicionPoleaMotrizTransportadora"            => $registrosBandaTransportadoraActualizar[$i]["tipoTransicionPoleaMotrizTransportadora"],
                        "distanciaTransicionPoleaMotrizTransportadora"       => $registrosBandaTransportadoraActualizar[$i]["distanciaTransicionPoleaMotrizTransportadora"],
                        "potenciaMotorTransportadora"                        => $registrosBandaTransportadoraActualizar[$i]["potenciaMotorTransportadora"],
                        "guardaPoleaMotrizTransportadora"                    => $registrosBandaTransportadoraActualizar[$i]["guardaPoleaMotrizTransportadora"],
                        "anchoEstructura"                                    => $registrosBandaTransportadoraActualizar[$i]["anchoEstructura"],
                        "anchoTrayectoCarga"                                 => $registrosBandaTransportadoraActualizar[$i]["anchoTrayectoCarga"],
                        "pasarelaRespectoAvanceBanda"                        => $registrosBandaTransportadoraActualizar[$i]["pasarelaRespectoAvanceBanda"],
                        "materialAlimenticioTransportadora"                  => $registrosBandaTransportadoraActualizar[$i]["materialAlimenticioTransportadora"],
                        "materialAcidoTransportadora"                        => $registrosBandaTransportadoraActualizar[$i]["materialAcidoTransportadora"],
                        "materialTempEntre80y150Transportadora"              => $registrosBandaTransportadoraActualizar[$i]["materialTempEntre80y150Transportadora"],
                        "materialSecoTransportadora"                         => $registrosBandaTransportadoraActualizar[$i]["materialSecoTransportadora"],
                        "materialHumedoTransportadora"                       => $registrosBandaTransportadoraActualizar[$i]["materialHumedoTransportadora"],
                        "materialAbrasivoFinoTransportadora"                 => $registrosBandaTransportadoraActualizar[$i]["materialAbrasivoFinoTransportadora"],
                        "materialPegajosoTransportadora"                     => $registrosBandaTransportadoraActualizar[$i]["materialPegajosoTransportadora"],
                        "materialGrasosoAceitosoTransportadora"              => $registrosBandaTransportadoraActualizar[$i]["materialGrasosoAceitosoTransportadora"],
                        "marcaLimpiadorPrimario"                             => $registrosBandaTransportadoraActualizar[$i]["marcaLimpiadorPrimario"],
                        "referenciaLimpiadorPrimario"                        => $registrosBandaTransportadoraActualizar[$i]["referenciaLimpiadorPrimario"],
                        "anchoCuchillaLimpiadorPrimario"                     => $registrosBandaTransportadoraActualizar[$i]["anchoCuchillaLimpiadorPrimario"],
                        "altoCuchillaLimpiadorPrimario"                      => $registrosBandaTransportadoraActualizar[$i]["altoCuchillaLimpiadorPrimario"],
                        "estadoCuchillaLimpiadorPrimario"                    => $registrosBandaTransportadoraActualizar[$i]["estadoCuchillaLimpiadorPrimario"],
                        "estadoTensorLimpiadorPrimario"                      => $registrosBandaTransportadoraActualizar[$i]["estadoTensorLimpiadorPrimario"],
                        "estadoTuboLimpiadorPrimario"                        => $registrosBandaTransportadoraActualizar[$i]["estadoTuboLimpiadorPrimario"],
                        "frecuenciaRevisionCuchilla"                         => $registrosBandaTransportadoraActualizar[$i]["frecuenciaRevisionCuchilla"],
                        "cuchillaEnContactoConBanda"                         => $registrosBandaTransportadoraActualizar[$i]["cuchillaEnContactoConBanda"],
                        "marcaLimpiadorSecundario"                           => $registrosBandaTransportadoraActualizar[$i]["marcaLimpiadorSecundario"],
                        "referenciaLimpiadorSecundario"                      => $registrosBandaTransportadoraActualizar[$i]["referenciaLimpiadorSecundario"],
                        "anchoCuchillaLimpiadorSecundario"                   => $registrosBandaTransportadoraActualizar[$i]["anchoCuchillaLimpiadorSecundario"],
                        "altoCuchillaLimpiadorSecundario"                    => $registrosBandaTransportadoraActualizar[$i]["altoCuchillaLimpiadorSecundario"],
                        "estadoCuchillaLimpiadorSecundario"                  => $registrosBandaTransportadoraActualizar[$i]["estadoCuchillaLimpiadorSecundario"],
                        "estadoTensorLimpiadorSecundario"                    => $registrosBandaTransportadoraActualizar[$i]["estadoTensorLimpiadorSecundario"],
                        "estadoTuboLimpiadorSecundario"                      => $registrosBandaTransportadoraActualizar[$i]["estadoTuboLimpiadorSecundario"],
                        "frecuenciaRevisionCuchilla1"                        => $registrosBandaTransportadoraActualizar[$i]["frecuenciaRevisionCuchilla1"],
                        "cuchillaEnContactoConBanda1"                        => $registrosBandaTransportadoraActualizar[$i]["cuchillaEnContactoConBanda1"],
                        "sistemaDribbleChute"                                => $registrosBandaTransportadoraActualizar[$i]["sistemaDribbleChute"],
                        "marcaLimpiadorTerciario"                            => $registrosBandaTransportadoraActualizar[$i]["marcaLimpiadorTerciario"],
                        "referenciaLimpiadorTerciario"                       => $registrosBandaTransportadoraActualizar[$i]["referenciaLimpiadorTerciario"],
                        "anchoCuchillaLimpiadorTerciario"                    => $registrosBandaTransportadoraActualizar[$i]["anchoCuchillaLimpiadorTerciario"],
                        "altoCuchillaLimpiadorTerciario"                     => $registrosBandaTransportadoraActualizar[$i]["altoCuchillaLimpiadorTerciario"],
                        "estadoCuchillaLimpiadorTerciario"                   => $registrosBandaTransportadoraActualizar[$i]["estadoCuchillaLimpiadorTerciario"],
                        "estadoTensorLimpiadorTerciario"                     => $registrosBandaTransportadoraActualizar[$i]["estadoTensorLimpiadorTerciario"],
                        "estadoTuboLimpiadorTerciario"                       => $registrosBandaTransportadoraActualizar[$i]["estadoTuboLimpiadorTerciario"],
                        "frecuenciaRevisionCuchilla2"                        => $registrosBandaTransportadoraActualizar[$i]["frecuenciaRevisionCuchilla2"],
                        "cuchillaEnContactoConBanda2"                        => $registrosBandaTransportadoraActualizar[$i]["cuchillaEnContactoConBanda2"],
                        "estadoRodilloRetorno"                               => $registrosBandaTransportadoraActualizar[$i]["estadoRodilloRetorno"],
                        "largoEjeRodilloRetorno"                             => $registrosBandaTransportadoraActualizar[$i]["largoEjeRodilloRetorno"],
                        "diametroEjeRodilloRetorno"                          => $registrosBandaTransportadoraActualizar[$i]["diametroEjeRodilloRetorno"],
                        "diametroRodilloRetorno"                             => $registrosBandaTransportadoraActualizar[$i]["diametroRodilloRetorno"],
                        "largoTuboRodilloRetorno"                            => $registrosBandaTransportadoraActualizar[$i]["largoTuboRodilloRetorno"],
                        "tipoRodilloRetorno"                                 => $registrosBandaTransportadoraActualizar[$i]["tipoRodilloRetorno"],
                        "distanciaEntreRodillosRetorno"                      => $registrosBandaTransportadoraActualizar[$i]["distanciaEntreRodillosRetorno"],
                        "anchoInternoChasisRetorno"                          => $registrosBandaTransportadoraActualizar[$i]["anchoInternoChasisRetorno"],
                        "anchoExternoChasisRetorno"                          => $registrosBandaTransportadoraActualizar[$i]["anchoExternoChasisRetorno"],
                        "detalleRodilloRetorno"                              => $registrosBandaTransportadoraActualizar[$i]["detalleRodilloRetorno"],
                        "diametroPoleaAmarrePoleaMotriz"                     => $registrosBandaTransportadoraActualizar[$i]["diametroPoleaAmarrePoleaMotriz"],
                        "anchoPoleaAmarrePoleaMotriz"                        => $registrosBandaTransportadoraActualizar[$i]["anchoPoleaAmarrePoleaMotriz"],
                        "tipoPoleaAmarrePoleaMotriz"                         => $registrosBandaTransportadoraActualizar[$i]["tipoPoleaAmarrePoleaMotriz"],
                        "largoEjePoleaAmarrePoleaMotriz"                     => $registrosBandaTransportadoraActualizar[$i]["largoEjePoleaAmarrePoleaMotriz"],
                        "diametroEjePoleaAmarrePoleaMotriz"                  => $registrosBandaTransportadoraActualizar[$i]["diametroEjePoleaAmarrePoleaMotriz"],
                        "icobandasCentradaPoleaAmarrePoleaMotriz"            => $registrosBandaTransportadoraActualizar[$i]["icobandasCentradaPoleaAmarrePoleaMotriz"],
                        "estadoRevestimientoPoleaAmarrePoleaMotriz"          => $registrosBandaTransportadoraActualizar[$i]["estadoRevestimientoPoleaAmarrePoleaMotriz"],
                        "dimetroPoleaAmarrePoleaCola"                        => $registrosBandaTransportadoraActualizar[$i]["dimetroPoleaAmarrePoleaCola"],
                        "anchoPoleaAmarrePoleaCola"                          => $registrosBandaTransportadoraActualizar[$i]["anchoPoleaAmarrePoleaCola"],
                        "largoEjePoleaAmarrePoleaCola"                       => $registrosBandaTransportadoraActualizar[$i]["largoEjePoleaAmarrePoleaCola"],
                        "tipoPoleaAmarrePoleaCola"                           => $registrosBandaTransportadoraActualizar[$i]["tipoPoleaAmarrePoleaCola"],
                        "diametroEjePoleaAmarrePoleaCola"                    => $registrosBandaTransportadoraActualizar[$i]["diametroEjePoleaAmarrePoleaCola"],
                        "icobandasCentradaPoleaAmarrePoleaCola"              => $registrosBandaTransportadoraActualizar[$i]["icobandasCentradaPoleaAmarrePoleaCola"],
                        "estadoRevestimientoPoleaAmarrePoleaCola"            => $registrosBandaTransportadoraActualizar[$i]["estadoRevestimientoPoleaAmarrePoleaCola"],
                        "diametroPoleaTensora"                               => $registrosBandaTransportadoraActualizar[$i]["diametroPoleaTensora"],
                        "anchoPoleaTensora"                                  => $registrosBandaTransportadoraActualizar[$i]["anchoPoleaTensora"],
                        "tipoPoleaTensora"                                   => $registrosBandaTransportadoraActualizar[$i]["tipoPoleaTensora"],
                        "largoEjePoleaTensora"                               => $registrosBandaTransportadoraActualizar[$i]["largoEjePoleaTensora"],
                        "diametroEjePoleaTensora"                            => $registrosBandaTransportadoraActualizar[$i]["diametroEjePoleaTensora"],
                        "icobandasCentradaEnPoleaTensora"                    => $registrosBandaTransportadoraActualizar[$i]["icobandasCentradaEnPoleaTensora"],
                        "recorridoPoleaTensora"                              => $registrosBandaTransportadoraActualizar[$i]["recorridoPoleaTensora"],
                        "estadoRevestimientoPoleaTensora"                    => $registrosBandaTransportadoraActualizar[$i]["estadoRevestimientoPoleaTensora"],
                        "tipoTransicionPoleaTensora"                         => $registrosBandaTransportadoraActualizar[$i]["tipoTransicionPoleaTensora"],
                        "distanciaTransicionPoleaColaTensora"                => $registrosBandaTransportadoraActualizar[$i]["distanciaTransicionPoleaColaTensora"],
                        "potenciaMotorPoleaTensora"                          => $registrosBandaTransportadoraActualizar[$i]["potenciaMotorPoleaTensora"],
                        "guardaPoleaTensora"                                 => $registrosBandaTransportadoraActualizar[$i]["guardaPoleaTensora"],
                        "puertasInspeccion"                                  => $registrosBandaTransportadoraActualizar[$i]["puertasInspeccion"],
                        "guardaRodilloRetornoPlano"                          => $registrosBandaTransportadoraActualizar[$i]["guardaRodilloRetornoPlano"],
                        "guardaTruTrainer"                                   => $registrosBandaTransportadoraActualizar[$i]["guardaTruTrainer"],
                        "guardaPoleaDeflectora"                              => $registrosBandaTransportadoraActualizar[$i]["guardaPoleaDeflectora"],
                        "guardaZonaDeTransito"                               => $registrosBandaTransportadoraActualizar[$i]["guardaZonaDeTransito"],
                        "guardaMotores"                                      => $registrosBandaTransportadoraActualizar[$i]["guardaMotores"],
                        "guardaCadenas"                                      => $registrosBandaTransportadoraActualizar[$i]["guardaCadenas"],
                        "guardaCorreas"                                      => $registrosBandaTransportadoraActualizar[$i]["guardaCorreas"],
                        "interruptoresDeSeguridad"                           => $registrosBandaTransportadoraActualizar[$i]["interruptoresDeSeguridad"],
                        "sirenasDeSeguridad"                                 => $registrosBandaTransportadoraActualizar[$i]["sirenasDeSeguridad"],
                        "guardaRodilloRetornoV"                              => $registrosBandaTransportadoraActualizar[$i]["guardaRodilloRetornoV"],
                        "diametroRodilloCentralCarga"                        => $registrosBandaTransportadoraActualizar[$i]["diametroRodilloCentralCarga"],
                        "tipoRodilloImpacto"                                 => $registrosBandaTransportadoraActualizar[$i]["tipoRodilloImpacto"],
                        "integridadSoporteCamaSellado"                       => $registrosBandaTransportadoraActualizar[$i]["integridadSoporteCamaSellado"],
                        "ataqueAbrasivoTransportadora"                       => $registrosBandaTransportadoraActualizar[$i]["ataqueAbrasivoTransportadora"],
                        "observacionRegistroTransportadora"                  => $registrosBandaTransportadoraActualizar[$i]["observacionRegistroTransportadora"]
                    );
                    $respuesta = ModeloSincronizacion::mdlActualizarTransportadora($tabla, $datos);
                    if ($respuesta == "ok") {
                        $respuesta = ModeloSincronizacion::mdlActualizarTransportadoraHost("bandaTransportadora", $registrosBandaTransportadoraActualizar[$i]["idRegistro"]);
                        if ($respuesta == "ok") { }
                    }
                }
            }
            $this->ctrMostrarMensajeFinal();
        }
    }

    public function ctrMostrarMensajeFinal()
    {
        echo "DATOS SINCRONIZADOS CORRECTAMENTE";
        echo '<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>';
        echo '<script>

                    $(document).ready(function() {
    setTimeout(function() {
        location.reload(true);
    }, 10000);
})

                    </script>';
    }
}
