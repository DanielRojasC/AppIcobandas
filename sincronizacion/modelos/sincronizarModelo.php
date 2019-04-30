<?php
require_once "conexion.php";

class ModeloSincronizacion
{
    public static function mdlTraerClientesHost($tabla)
    {
        $stmt = Conexion::getConexionHost()->prepare("SELECT * FROM $tabla");
        $stmt->execute();
        return $stmt->fetchAll();
    }

    public static function mdlTraerClientesLocal($tabla)
    {
        $stmt = Conexion::getConexionLocal()->prepare("SELECT * FROM $tabla");
        $stmt->execute();
        return $stmt->fetchAll();
    }

    public static function mdlCrearCliente($tabla, $datos)
    {
        // "INSERT INTO clientes (nit, nameunido) VALUES (:nit, :nameunido)"

        $stmt = Conexion::getConexionLocal()->prepare("INSERT INTO clientes (nit, nameunido) VALUES (:nit, :nameunido)");
        // var_dump($stmt);
        $stmt->bindParam(":nit", $datos["nit"], PDO::PARAM_STR);
        $stmt->bindParam(":nameunido", $datos["nameunido"], PDO::PARAM_STR);

        if ($stmt->execute()) {
            return "ok";
        } else {
            return "Error al crear clientes";
        }
        $stmt->close();
        $stmt = null;
    }

    public static function mdlCrearClienteHost($tabla, $datos)
    {
        // "INSERT INTO clientes (nit, nameunido) VALUES (:nit, :nameunido)"

        $stmt = Conexion::getConexionHost()->prepare("INSERT INTO clientes (nit, dv, tipocontrib, tipodoccli, noidentifcli, codciudexpdoccli, apellido1cli, apellido2cli, nombre1cli, nombre2cli, razonsoc, codmcipiocli, emailcli, dirppalcli, pagweb, telef1cli, telef2cli, celular, celular2, activciuuppal, activciuusec, 
                         nameunido, fechacreac, fechabaja, debaja, verrut, codformpagcli, regimeniva, grancontrib, Autoreten, fecharut, matriculamercantil, barriocc, ciudadcc, zonapostalcc, departamentocc, direccioncc, codigopaiscc, 
                         paiscamaracomerciocc, casilla53rut, casilla54rut) VALUES (:nit, :dv, :tipocontrib, :tipodoccli, :noidentifcli, :codciudexpdoccli, :apellido1cli, :apellido2cli, :nombre1cli, :nombre2cli, :razonsoc, :codmcipiocli, :emailcli, :dirppalcli, :pagweb, :telef1cli, :telef2cli, :celular, :celular2, :activciuuppal, :activciuusec, 
                         :nameunido, :fechacreac, :fechabaja, :debaja, :verrut, :codformpagcli, :regimeniva, :grancontrib, :Autoreten, :fecharut, :matriculamercantil, :barriocc, :ciudadcc, :zonapostalcc, :departamentocc, :direccioncc, :codigopaiscc, 
                         :paiscamaracomerciocc, :casilla53rut, :casilla54rut)");
        // var_dump($stmt);
        $stmt->bindParam("nit", $datos["nit"], PDO::PARAM_STR);
        $stmt->bindParam("dv", $datos["dv"], PDO::PARAM_STR);
        $stmt->bindParam("tipocontrib", $datos["tipocontrib"], PDO::PARAM_STR);
        $stmt->bindParam("tipodoccli", $datos["tipodoccli"], PDO::PARAM_STR);
        $stmt->bindParam("noidentifcli", $datos["noidentifcli"], PDO::PARAM_STR);
        $stmt->bindParam("codciudexpdoccli", $datos["codciudexpdoccli"], PDO::PARAM_STR);
        $stmt->bindParam("apellido1cli", $datos["apellido1cli"], PDO::PARAM_STR);
        $stmt->bindParam("apellido2cli", $datos["apellido2cli"], PDO::PARAM_STR);
        $stmt->bindParam("nombre1cli", $datos["nombre1cli"], PDO::PARAM_STR);
        $stmt->bindParam("nombre2cli", $datos["nombre2cli"], PDO::PARAM_STR);
        $stmt->bindParam("razonsoc", $datos["razonsoc"], PDO::PARAM_STR);
        $stmt->bindParam("codmcipiocli", $datos["codmcipiocli"], PDO::PARAM_STR);
        $stmt->bindParam("emailcli", $datos["emailcli"], PDO::PARAM_STR);
        $stmt->bindParam("dirppalcli", $datos["dirppalcli"], PDO::PARAM_STR);
        $stmt->bindParam("pagweb", $datos["pagweb"], PDO::PARAM_STR);
        $stmt->bindParam("telef1cli", $datos["telef1cli"], PDO::PARAM_STR);
        $stmt->bindParam("telef2cli", $datos["telef2cli"], PDO::PARAM_STR);
        $stmt->bindParam("celular", $datos["celular"], PDO::PARAM_STR);
        $stmt->bindParam("celular2", $datos["celular2"], PDO::PARAM_STR);
        $stmt->bindParam("activciuuppal", $datos["activciuuppal"], PDO::PARAM_STR);
        $stmt->bindParam("activciuusec", $datos["activciuusec"], PDO::PARAM_STR);
        $stmt->bindParam("nameunido", $datos["nameunido"], PDO::PARAM_STR);
        $stmt->bindParam("fechacreac", $datos["fechacreac"], PDO::PARAM_STR);
        $stmt->bindParam("fechabaja", $datos["fechabaja"], PDO::PARAM_STR);
        $stmt->bindParam("debaja", $datos["debaja"], PDO::PARAM_STR);
        $stmt->bindParam("verrut", $datos["verrut"], PDO::PARAM_STR);
        $stmt->bindParam("codformpagcli", $datos["codformpagcli"], PDO::PARAM_STR);
        $stmt->bindParam("regimeniva", $datos["regimeniva"], PDO::PARAM_STR);
        $stmt->bindParam("grancontrib", $datos["grancontrib"], PDO::PARAM_STR);
        $stmt->bindParam("Autoreten", $datos["Autoreten"], PDO::PARAM_STR);
        $stmt->bindParam("fecharut", $datos["fecharut"], PDO::PARAM_STR);
        $stmt->bindParam("matriculamercantil", $datos["matriculamercantil"], PDO::PARAM_STR);
        $stmt->bindParam("barriocc", $datos["barriocc"], PDO::PARAM_STR);
        $stmt->bindParam("ciudadcc", $datos["ciudadcc"], PDO::PARAM_STR);
        $stmt->bindParam("zonapostalcc", $datos["zonapostalcc"], PDO::PARAM_STR);
        $stmt->bindParam("departamentocc", $datos["departamentocc"], PDO::PARAM_STR);
        $stmt->bindParam("direccioncc", $datos["direccioncc"], PDO::PARAM_STR);
        $stmt->bindParam("codigopaiscc", $datos["codigopaiscc"], PDO::PARAM_STR);
        $stmt->bindParam("paiscamaracomerciocc", $datos["paiscamaracomerciocc"], PDO::PARAM_STR);
        $stmt->bindParam("casilla53rut", $datos["casilla53rut"], PDO::PARAM_STR);
        $stmt->bindParam("casilla54rut", $datos["casilla54rut"], PDO::PARAM_STR);
        if ($stmt->execute()) {
            return "ok";
        } else {
            return "Error al crear clientes";
        }
        $stmt->close();
        $stmt = null;
    }

    public static function mdlTraerPlantasHost($tabla)
    {
        $stmt = Conexion::getConexionHost()->prepare("SELECT * FROM $tabla");
        $stmt->execute();
        return $stmt->fetchAll();
    }

    public static function mdlTraerPlantasLocal($tabla)
    {
        $stmt = Conexion::getConexionLocal()->prepare("SELECT * FROM $tabla");
        $stmt->execute();
        return $stmt->fetchAll();
    }

    public static function mdlCrearPlanta($tabla, $datos)
    {
        // "INSERT INTO clientes (nit, nameunido) VALUES (:nit, :nameunido)"

        $stmt = Conexion::getConexionLocal()->prepare("INSERT INTO plantas (nameplanta, agenteplanta, nitplanta, dirmciapl, ciudmciapl) VALUES (:nameplanta, :agenteplanta, :nitplanta, :dirmciapl, :ciudmciapl)");
        // var_dump($datos);

        $stmt->bindParam(":nameplanta", $datos["nameplanta"], PDO::PARAM_STR);
        $stmt->bindParam(":agenteplanta", $datos["agenteplanta"], PDO::PARAM_STR);
        $stmt->bindParam(":nitplanta", $datos["nitplanta"], PDO::PARAM_STR);
        $stmt->bindParam(":dirmciapl", $datos["dirmciapl"], PDO::PARAM_STR);
        $stmt->bindParam(":ciudmciapl", $datos["ciudmciapl"], PDO::PARAM_STR);
        // var_dump($stmt);

        if ($stmt->execute()) {
            return "ok";
        } else {
            return "Error al crear plantas";
        }
        $stmt->close();
        $stmt = null;
    }

    public static function mdlCrearPlantaHost($tabla, $datos)
    {
        // "INSERT INTO clientes (nit, nameunido) VALUES (:nit, :nameunido)"

        $stmt = Conexion::getConexionHost()->prepare("INSERT INTO plantas ( agenteplanta, nitplanta, nameplanta, ciudmciapl, contacmciapl, dirmciapl, telfmciapl, recepmciahasta, diasestillegmcia, condicdespmcia, emailmcia, fechlimrecfact, diasestillegfact, despfactconmcia, obligoc, 
                         mcipiofactpl, dirfactpl, docsadicradfact, condicadicradfac, emailfact, contacfact, teleffact, celufact, contacteso, mcipioteso, dirteso, telefteso, celuteso, emailteso) VALUES (:agenteplanta, :nitplanta, :nameplanta, :ciudmciapl, :contacmciapl, :dirmciapl, :telfmciapl, :recepmciahasta, :diasestillegmcia, :condicdespmcia, :emailmcia, :fechlimrecfact, :diasestillegfact, :despfactconmcia, :obligoc, 
                         :mcipiofactpl, :dirfactpl, :docsadicradfact, :condicadicradfac, :emailfact, :contacfact, :teleffact, :celufact, :contacteso, :mcipioteso, :dirteso, :telefteso, :celuteso, :emailteso)");
        // var_dump($datos);

        $stmt->bindParam("agenteplanta", $datos["agenteplanta"], PDO::PARAM_STR);
        $stmt->bindParam("nitplanta", $datos["nitplanta"], PDO::PARAM_STR);
        $stmt->bindParam("nameplanta", $datos["nameplanta"], PDO::PARAM_STR);
        $stmt->bindParam("ciudmciapl", $datos["ciudmciapl"], PDO::PARAM_STR);
        $stmt->bindParam("contacmciapl", $datos["contacmciapl"], PDO::PARAM_STR);
        $stmt->bindParam("dirmciapl", $datos["dirmciapl"], PDO::PARAM_STR);
        $stmt->bindParam("telfmciapl", $datos["telfmciapl"], PDO::PARAM_STR);
        $stmt->bindParam("recepmciahasta", $datos["recepmciahasta"], PDO::PARAM_STR);
        $stmt->bindParam("diasestillegmcia", $datos["diasestillegmcia"], PDO::PARAM_STR);
        $stmt->bindParam("condicdespmcia", $datos["condicdespmcia"], PDO::PARAM_STR);
        $stmt->bindParam("emailmcia", $datos["emailmcia"], PDO::PARAM_STR);
        $stmt->bindParam("fechlimrecfact", $datos["fechlimrecfact"], PDO::PARAM_STR);
        $stmt->bindParam("diasestillegfact", $datos["diasestillegfact"], PDO::PARAM_STR);
        $stmt->bindParam("despfactconmcia", $datos["despfactconmcia"], PDO::PARAM_STR);
        $stmt->bindParam("obligoc", $datos["obligoc"], PDO::PARAM_STR);
        $stmt->bindParam("mcipiofactpl", $datos["mcipiofactpl"], PDO::PARAM_STR);
        $stmt->bindParam("dirfactpl", $datos["dirfactpl"], PDO::PARAM_STR);
        $stmt->bindParam("docsadicradfact", $datos["docsadicradfact"], PDO::PARAM_STR);
        $stmt->bindParam("condicadicradfac", $datos["condicadicradfac"], PDO::PARAM_STR);
        $stmt->bindParam("emailfact", $datos["emailfact"], PDO::PARAM_STR);
        $stmt->bindParam("contacfact", $datos["contacfact"], PDO::PARAM_STR);
        $stmt->bindParam("teleffact", $datos["teleffact"], PDO::PARAM_STR);
        $stmt->bindParam("celufact", $datos["celufact"], PDO::PARAM_STR);
        $stmt->bindParam("contacteso", $datos["contacteso"], PDO::PARAM_STR);
        $stmt->bindParam("mcipioteso", $datos["mcipioteso"], PDO::PARAM_STR);
        $stmt->bindParam("dirteso", $datos["dirteso"], PDO::PARAM_STR);
        $stmt->bindParam("telefteso", $datos["telefteso"], PDO::PARAM_STR);
        $stmt->bindParam("celuteso", $datos["celuteso"], PDO::PARAM_STR);
        $stmt->bindParam("emailteso", $datos["emailteso"], PDO::PARAM_STR);
        // var_dump($stmt);

        if ($stmt->execute()) {
            return "ok";
        } else {
            return "Error al crear plantas";
        }
        $stmt->close();
        $stmt = null;
    }

    public static function mdlPlantaMaxima($tabla)
    {
        $stmt = Conexion::getConexionLocal()->prepare("SELECT max(codplanta) FROM plantas");

        // $stmt->bindParam(":" . $item, $valor, PDO::PARAM_STR);

        $stmt->execute();
        // var_dump($stmt);

        return $stmt->fetch();
    }

    public static function mdlActualizarTransportadoresHost($tabla, $idPlantaNueva, $idPlantaAntigua)
    {
        // var_dump('$idPlantaNueva);
        $stmt = Conexion::getConexionHost()->prepare("SET identity_insert plantas on
INSERT into plantas (nameplanta, agenteplanta, nitplanta, dirmciapl, ciudmciapl, codplanta)
SELECT nameplanta, agenteplanta, nitplanta, dirmciapl, ciudmciapl,$idPlantaNueva
                         from plantas where codplanta=$idPlantaAntigua
                         set identity_insert plantas off");
        if ($stmt->execute()) {
            $stmt = Conexion::getConexionHost()->prepare("UPDATE $tabla SET codplanta=$idPlantaNueva WHERE codplanta=$idPlantaAntigua");

            if ($stmt->execute()) {
                $stmt = Conexion::getConexionHost()->prepare("UPDATE registro SET codplanta=$idPlantaNueva WHERE codplanta=$idPlantaAntigua");

                if ($stmt->execute()) {
                    $stmt = Conexion::getConexionHost()->prepare("DELETE from plantas WHERE codplanta=$idPlantaAntigua");

                    if ($stmt->execute()) {
                        return "ok";
                    } else {
                        var_dump("Error actualizar registro con transportadores");
                    }
                } else {
                    return "ERROR AL ELIMINAR REGISTRO VIEJO PLANTAS";
                }
            } else {
                return "ERROR AL INSERTAR PLANTAS NUEVA";
            }
            $stmt->close();
            $stmt = null;
        }
    }

    public static function mdlTransportadoresPendientesInsertarHost($tabla)
    {
        $stmt = Conexion::getConexionHost()->prepare("SELECT * FROM $tabla where estadoTransportador='Pendiente Insertar'");
        $stmt->execute();
        return $stmt->fetchAll();
    }

    public static function mdlTransportadoresPendientesActualizar($tabla)
    {
        $stmt = Conexion::getConexionHost()->prepare("SELECT * FROM $tabla where estadoTransportador='Pendiente Actualizar'");
        $stmt->execute();
        return $stmt->fetchAll();
    }

    public static function mdlCrearTransportador($tabla, $datos, $proceso, $idTransportador)
    {
        // "INSERTINTOclientes(nit, nameunido)VALUES(:nit, :nameunido)
        $stmt = "";

        $stmt = Conexion::getConexionLocal()->prepare("INSERT INTO $tabla (tipoTransportador, nombreTransportador, codplanta, caracteristicaTransportador) VALUES (:tipoTransportador, :nombreTransportador, :codplanta, :caracteristicaTransportador)");


        $stmt->bindParam(":tipoTransportador", $datos["tipoTransportador"], PDO::PARAM_STR);
        $stmt->bindParam(":nombreTransportador", $datos["nombreTransportador"], PDO::PARAM_STR);
        $stmt->bindParam(":codplanta", $datos["codplanta"], PDO::PARAM_STR);
        $stmt->bindParam(":caracteristicaTransportador", $datos["caracteristicaTransportador"], PDO::PARAM_STR);



        if ($stmt->execute()) {
            return "ok";
        } else {
            return "ERROR AL CREAR TRANSPORTADOR";
        }

        $stmt->close();
        $stmt = null;
    }

    public static function mdlActualizarTransportador($tabla, $datos, $idTransportador)
    {
        // "INSERTINTOclientes(nit, nameunido)VALUES(:nit, :nameunido)
        $stmt = "";

        $stmt = Conexion::getConexionLocal()->prepare("UPDATE $tabla set tipoTransportador=:tipoTransportador, nombreTransportador=:nombreTransportador, codplanta=:codplanta, caracteristicaTransportador=:caracteristicaTransportador where idTransportador=$idTransportador");

        $stmt->bindParam(":tipoTransportador", $datos["tipoTransportador"], PDO::PARAM_STR);
        $stmt->bindParam(":nombreTransportador", $datos["nombreTransportador"], PDO::PARAM_STR);
        $stmt->bindParam(":codplanta", $datos["codplanta"], PDO::PARAM_STR);
        $stmt->bindParam(":caracteristicaTransportador", $datos["caracteristicaTransportador"], PDO::PARAM_STR);



        if ($stmt->execute()) {
            return "ok";
        } else {
            return "ERROR AL  TRANSPORTADOR";
        }

        // $stmt->close();
        // $stmt = null;
    }

    public static function mdlTransportadorMaximo($tabla)
    {
        $stmt = Conexion::getConexionLocal()->prepare("SELECT max(idTransportador) FROM $tabla");

        // $stmt->bindParam(":" . $item, $valor, PDO::PARAM_STR);

        $stmt->execute();
        // var_dump($stmt);

        return $stmt->fetch();
    }

    public static function mdlActualizarTransportadoresHostId($tabla, $idPlantaNueva, $idPlantaAntigua)
    {
        // var_dump($idPlantaNueva);
        $stmt = Conexion::getConexionHost()->prepare("SET identity_insert transportador on
    INSERT into transportador (tipoTransportador, nombreTransportador, codplanta, caracteristicaTransportador, idTransportador, estadoTransportador)
    SELECT tipoTransportador, nombreTransportador, codplanta, caracteristicaTransportador,$idPlantaNueva, 'Sincronizado'
                             from transportador where idTransportador=$idPlantaAntigua
                             set identity_insert transportador off");
        // print_r($stmt);
        if ($stmt->execute()) {
            $stmt = Conexion::getConexionHost()->prepare("UPDATE registro SET idTransportador=$idPlantaNueva WHERE idTransportador=$idPlantaAntigua");

            if ($stmt->execute()) {
                $stmt = Conexion::getConexionHost()->prepare("DELETE from transportador WHERE idTransportador=$idPlantaAntigua");

                if ($stmt->execute()) {
                    return "ok";
                } else {
                    var_dump("ERROR AL ACTUALIZAR REGISTRO TRANSPORTADOR");
                }
            } else {
                return "ERROR AL ELIMINAR REGISTRO VIEJO TRANSPORTADOR";
            }
            $stmt->close();
            $stmt = null;
        } else {
            var_dump("ERROR AL INSERTAR REGISTRO NUEVO TRANSPORTADOR");
        }
    }

    public static function mdlActualizarTransportadoresHostSincronizar($tabla, $idPlantaNueva, $idPlantaAntigua)
    {
        // var_dump($idPlantaAntigua);
        $stmt = Conexion::getConexionHost()->prepare("UPDATE $tabla set estadoTransportador='Sincronizado' where idTransportador=$idPlantaAntigua");
        if ($stmt->execute()) {
            return "ok";
        } else {
            var_dump("ERROR AL INSERTAR REGISTRO NUEVO TRANSPORTADOR");
        }
    }

    public static function mdlRegistrosPendientesInsertarHost($tabla)
    {
        $stmt = Conexion::getConexionHost()->prepare("SELECT * FROM $tabla where estadoRegistroSincronizacion='Pendiente Insertar' or estadoRegistroSincronizacion=''");
        $stmt->execute();
        return $stmt->fetchAll();
    }

    public static function mdlRegistrosPendientesActualizar($tabla)
    {
        $stmt = Conexion::getConexionHost()->prepare("SELECT * FROM $tabla where estadoRegistroSincronizacion='Pendiente Actualizar'");
        $stmt->execute();
        return $stmt->fetchAll();
    }

    public static function mdlCrearRegistro($tabla, $datos)
    {
        // "INSERT INTO clientes (nit, nameunido) VALUES (:nit, :nameunido)"

        $stmt = Conexion::getConexionLocal()->prepare("INSERT INTO registro (fechaRegistro, idTransportador, usuarioRegistro, codplanta, estadoRegistro) VALUES (:fechaRegistro, :idTransportador, :usuarioRegistro, :codplanta, :estadoRegistro)");

        $stmt->bindParam(":fechaRegistro", $datos["fechaRegistro"], PDO::PARAM_STR);
        $stmt->bindParam(":idTransportador", $datos["idTransportador"], PDO::PARAM_STR);
        $stmt->bindParam(":usuarioRegistro", $datos["usuarioRegistro"], PDO::PARAM_STR);
        $stmt->bindParam(":codplanta", $datos["codplanta"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRegistro", $datos["estadoRegistro"], PDO::PARAM_STR);

        if ($stmt->execute()) {
            return "ok";
        } else {
            return "ERROR AL CREAR REGISTRO";
        }
        var_dump($stmt);

        $stmt->close();
        $stmt = null;
    }

    public static function mdlActualizarRegistro($tabla, $datos)
    {
        // "INSERT INTO clientes (nit, nameunido) VALUES (:nit, :nameunido)"

        $stmt = Conexion::getConexionLocal()->prepare("UPDATE $tabla SET fechaRegistro=:fechaRegistro, idTransportador=:idTransportador, usuarioRegistro=:usuarioRegistro, codplanta=:codplanta, estadoRegistro=:estadoRegistro where idRegistro=:idRegistro");

        $stmt->bindParam(":idRegistro", $datos["idRegistro"], PDO::PARAM_STR);
        $stmt->bindParam(":fechaRegistro", $datos["fechaRegistro"], PDO::PARAM_STR);
        $stmt->bindParam(":idTransportador", $datos["idTransportador"], PDO::PARAM_STR);
        $stmt->bindParam(":usuarioRegistro", $datos["usuarioRegistro"], PDO::PARAM_STR);
        $stmt->bindParam(":codplanta", $datos["codplanta"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRegistro", $datos["estadoRegistro"], PDO::PARAM_STR);


        // echo '<pre>'; print_r($stmt); echo '</pre>';

        if ($stmt->execute()) {
            return "ok";
        } else {
            return "ERROR AL actualizar REGISTRO";
        }
        // var_dump($stmt);

        // $stmt->close();
        // $stmt = null;
    }

    public static function mdlActualizarRegistrosHostSincronizar($tabla, $idPlantaNueva, $idPlantaAntigua)
    {
        // var_dump($idPlantaAntigua);
        $stmt = Conexion::getConexionHost()->prepare("UPDATE $tabla set estadoRegistroSincronizacion='Sincronizado' where idRegistro=$idPlantaAntigua");
        if ($stmt->execute()) {
            return "ok";
        } else {
            var_dump("ERROR AL INSERTAR REGISTRO NUEVO TRANSPORTADOR");
        }
    }

    public static function mdlRegistroMaximo($tabla)
    {
        $stmt = Conexion::getConexionLocal()->prepare("SELECT max(idRegistro) FROM $tabla");

        // $stmt->bindParam(":" . $item, $valor, PDO::PARAM_STR);

        $stmt->execute();
        // var_dump($stmt);

        return $stmt->fetch();
    }

    public static function mdlActualizarRegistrosHostId($tabla, $idPlantaNueva, $idPlantaAntigua)
    {
        // var_dump('$idPlantaNueva);
        $stmt = Conexion::getConexionHost()->prepare("SET identity_insert registro on
INSERT into registro (fechaRegistro, idTransportador, usuarioRegistro, codplanta, estadoRegistro, idRegistro, estadoRegistroSincronizacion)
SELECT fechaRegistro, idTransportador, usuarioRegistro, codplanta, estadoRegistro,$idPlantaNueva, 'Sincronizado'
                         from registro where idRegistro=$idPlantaAntigua
                         set identity_insert registro off");
        if ($stmt->execute()) {
            $stmt = Conexion::getConexionHost()->prepare("UPDATE bandaElevadora SET idRegistro=$idPlantaNueva WHERE idRegistro=$idPlantaAntigua");
            if ($stmt->execute()) {
                    $stmt = Conexion::getConexionHost()->prepare("UPDATE bandaTransportadora SET idRegistro=$idPlantaNueva WHERE idRegistro=$idPlantaAntigua");
                    if ($stmt->execute()) {
                        $stmt = Conexion::getConexionHost()->prepare("UPDATE bandaTransmision SET idRegistro=$idPlantaNueva WHERE idRegistro=$idPlantaAntigua");
                        if ($stmt->execute()) {
                            $stmt = Conexion::getConexionHost()->prepare("DELETE from registro WHERE idRegistro=$idPlantaAntigua");
                            // var_dump($stmt);
                            if ($stmt->execute()) {
                                return "ok";
                            } else {
                                return "Error al eliminar registro viejo";
                            }
                        } else {
                            var_dump("error al actualizar banda transmision");
                        }
                    } else {
                        var_dump("Error al actualizar banda transportadora");
                    }
                } else {
                var_dump("Error al actualizar bandaeLEVADORA");
            }

            // $stmt->close();
            // $stmt = null;
        } else {
            var_dump("Error insertar nuevo registro");
        }
    }

    public static function mdlBandaElevadoraHost($tabla)
    {
        $stmt = Conexion::getConexionHost()->prepare("SELECT * FROM bandaElevadora where estadoRegistroSincronizadoElevadora='Pendiente Insertar' or estadoRegistroSincronizadoElevadora is null");
        // print_r($stmt);
        $stmt->execute();
        return $stmt->fetchAll();
    }

    public static function mdlBandaElevadoraActualizar($tabla)
    {
        $stmt = Conexion::getConexionHost()->prepare("SELECT * FROM $tabla where estadoRegistroSincronizadoElevadora='Pendiente Actualizar'");
        $stmt->execute();
        return $stmt->fetchAll();
    }

    public static function mdlDatosBandaElevadora($tabla, $datos)
    {
        // "INSERT INTO clientes (nit, nameunido) VALUES (:nit, :nameunido)"

        $stmt = Conexion::getConexionLocal()->prepare("INSERT INTO bandaElevadora(idRegistro, marcaBandaElevadora, anchoBandaElevadora, distanciaEntrePoleasElevadora, noLonaBandaElevadora, tipoLonaBandaElevadora, espesorTotalBandaElevadora, espesorCojinActualElevadora,
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
                         anchoPiernaElevador, profundidadPiernaElevador, tempAmbienteMin, tempAmbienteMax, tipoDescarga, tipoCarga, observacionRegistroElevadora)

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
                         :anchoPiernaElevador, :profundidadPiernaElevador, :tempAmbienteMin, :tempAmbienteMax, :tipoDescarga, :tipoCarga, :observacionRegistroElevadora)");

        $stmt->bindParam(':idRegistro', $datos["idRegistro"], PDO::PARAM_STR);
        $stmt->bindParam(":marcaBandaElevadora", $datos["marcaBandaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoBandaElevadora", $datos["anchoBandaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaEntrePoleasElevadora", $datos["distanciaEntrePoleasElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":noLonaBandaElevadora", $datos["noLonaBandaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoLonaBandaElevadora", $datos["tipoLonaBandaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorTotalBandaElevadora", $datos["espesorTotalBandaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCojinActualElevadora", $datos["espesorCojinActualElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCubiertaSuperiorElevadora", $datos["espesorCubiertaSuperiorElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCubiertaInferiorElevadora", $datos["espesorCubiertaInferiorElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoCubiertaElevadora", $datos["tipoCubiertaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoEmpalmeElevadora", $datos["tipoEmpalmeElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoEmpalmeElevadora", $datos["estadoEmpalmeElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":resistenciaRoturaLonaElevadora", $datos["resistenciaRoturaLonaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":velocidadBandaElevadora", $datos["velocidadBandaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":marcaBandaElevadoraAnterior", $datos["marcaBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoBandaElevadoraAnterior", $datos["anchoBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":noLonasBandaElevadoraAnterior", $datos["noLonasBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoLonaBandaElevadoraAnterior", $datos["tipoLonaBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorTotalBandaElevadoraAnterior", $datos["espesorTotalBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCubiertaSuperiorBandaElevadoraAnterior", $datos["espesorCubiertaSuperiorBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCojinElevadoraAnterior", $datos["espesorCojinElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCubiertaInferiorBandaElevadoraAnterior", $datos["espesorCubiertaInferiorBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoCubiertaElevadoraAnterior", $datos["tipoCubiertaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoEmpalmeElevadoraAnterior", $datos["tipoEmpalmeElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":resistenciaRoturaBandaElevadoraAnterior", $datos["resistenciaRoturaBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":tonsTransportadasBandaElevadoraAnterior", $datos["tonsTransportadasBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":velocidadBandaElevadoraAnterior", $datos["velocidadBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":causaFallaCambioBandaElevadoraAnterior", $datos["causaFallaCambioBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":pesoMaterialEnCadaCangilon", $datos["pesoMaterialEnCadaCangilon"], PDO::PARAM_STR);
        $stmt->bindParam(":pesoCangilonVacio", $datos["pesoCangilonVacio"], PDO::PARAM_STR);
        $stmt->bindParam(":longitudCangilon", $datos["longitudCangilon"], PDO::PARAM_STR);
        $stmt->bindParam(":materialCangilon", $datos["materialCangilon"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoCangilon", $datos["tipoCangilon"], PDO::PARAM_STR);
        $stmt->bindParam(":proyeccionCangilon", $datos["proyeccionCangilon"], PDO::PARAM_STR);
        $stmt->bindParam(":profundidadCangilon", $datos["profundidadCangilon"], PDO::PARAM_STR);
        $stmt->bindParam(":marcaCangilon", $datos["marcaCangilon"], PDO::PARAM_STR);
        $stmt->bindParam(":referenciaCangilon", $datos["referenciaCangilon"], PDO::PARAM_STR);
        $stmt->bindParam(":capacidadCangilon", $datos["capacidadCangilon"], PDO::PARAM_STR);
        $stmt->bindParam(":noFilasCangilones", $datos["noFilasCangilones"], PDO::PARAM_STR);
        $stmt->bindParam(":separacionCangilones", $datos["separacionCangilones"], PDO::PARAM_STR);
        $stmt->bindParam(":noAgujeros", $datos["noAgujeros"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaBordeBandaEstructura", $datos["distanciaBordeBandaEstructura"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaPosteriorBandaEstructura", $datos["distanciaPosteriorBandaEstructura"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaLaboFrontalCangilonEstructura", $datos["distanciaLaboFrontalCangilonEstructura"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaBordesCangilonEstructura", $datos["distanciaBordesCangilonEstructura"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoVentilacion", $datos["tipoVentilacion"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroPoleaMotrizElevadora", $datos["diametroPoleaMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPoleaMotrizElevadora", $datos["anchoPoleaMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoPoleaMotrizElevadora", $datos["tipoPoleaMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjeMotrizElevadora", $datos["largoEjeMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjeMotrizElevadora", $datos["diametroEjeMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":bandaCentradaEnPoleaMotrizElevadora", $datos["bandaCentradaEnPoleaMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRevestimientoPoleaMotrizElevadora", $datos["estadoRevestimientoPoleaMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":potenciaMotorMotrizElevadora", $datos["potenciaMotorMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":rpmSalidaReductorMotrizElevadora", $datos["rpmSalidaReductorMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaReductorPoleaMotrizElevadora", $datos["guardaReductorPoleaMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroPoleaColaElevadora", $datos["diametroPoleaColaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPoleaColaElevadora", $datos["anchoPoleaColaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoPoleaColaElevadora", $datos["tipoPoleaColaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjePoleaColaElevadora", $datos["largoEjePoleaColaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjePoleaColaElevadora", $datos["diametroEjePoleaColaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":bandaCentradaEnPoleaColaElevadora", $datos["bandaCentradaEnPoleaColaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRevestimientoPoleaColaElevadora", $datos["estadoRevestimientoPoleaColaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":longitudTensorTornilloPoleaColaElevadora", $datos["longitudTensorTornilloPoleaColaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":longitudRecorridoContrapesaPoleaColaElevadora", $datos["longitudRecorridoContrapesaPoleaColaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":cargaTrabajoBandaElevadora", $datos["cargaTrabajoBandaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":temperaturaMaterialElevadora", $datos["temperaturaMaterialElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":empalmeMecanicoElevadora", $datos["empalmeMecanicoElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroRoscaElevadora", $datos["diametroRoscaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":largoTornilloElevadora", $datos["largoTornilloElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":materialTornilloElevadora", $datos["materialTornilloElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoCabezaElevadorPuertaInspeccion", $datos["anchoCabezaElevadorPuertaInspeccion"], PDO::PARAM_STR);
        $stmt->bindParam(":largoCabezaElevadorPuertaInspeccion", $datos["largoCabezaElevadorPuertaInspeccion"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoBotaElevadorPuertaInspeccion", $datos["anchoBotaElevadorPuertaInspeccion"], PDO::PARAM_STR);
        $stmt->bindParam(":largoBotaElevadorPuertaInspeccion", $datos["largoBotaElevadorPuertaInspeccion"], PDO::PARAM_STR);
        $stmt->bindParam(":monitorPeligro", $datos["monitorPeligro"], PDO::PARAM_STR);
        $stmt->bindParam(":rodamiento", $datos["rodamiento"], PDO::PARAM_STR);
        $stmt->bindParam(":monitorDesalineacion", $datos["monitorDesalineacion"], PDO::PARAM_STR);
        $stmt->bindParam(":monitorVelocidad", $datos["monitorVelocidad"], PDO::PARAM_STR);
        $stmt->bindParam(":sensorInductivo", $datos["sensorInductivo"], PDO::PARAM_STR);
        $stmt->bindParam(":indicadorNivel", $datos["indicadorNivel"], PDO::PARAM_STR);
        $stmt->bindParam(":cajaUnion", $datos["cajaUnion"], PDO::PARAM_STR);
        $stmt->bindParam(":alarmaYPantalla", $datos["alarmaYPantalla"], PDO::PARAM_STR);
        $stmt->bindParam(":interruptorSeguridad", $datos["interruptorSeguridad"], PDO::PARAM_STR);
        $stmt->bindParam(":materialElevadora", $datos["materialElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":ataqueQuimicoElevadora", $datos["ataqueQuimicoElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":ataqueTemperaturaElevadora", $datos["ataqueTemperaturaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":ataqueAceitesElevadora", $datos["ataqueAceitesElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":ataqueAbrasivoElevadora", $datos["ataqueAbrasivoElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":capacidadElevadora", $datos["capacidadElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":horasTrabajoDiaElevadora", $datos["horasTrabajoDiaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":diasTrabajoSemanaElevadora", $datos["diasTrabajoSemanaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":abrasividadElevadora", $datos["abrasividadElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":porcentajeFinosElevadora", $datos["porcentajeFinosElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":maxGranulometriaElevadora", $datos["maxGranulometriaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":densidadMaterialElevadora", $datos["densidadMaterialElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tempMaxMaterialSobreBandaElevadora", $datos["tempMaxMaterialSobreBandaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tempPromedioMaterialSobreBandaElevadora", $datos["tempPromedioMaterialSobreBandaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":variosPuntosDeAlimentacion", $datos["variosPuntosDeAlimentacion"], PDO::PARAM_STR);
        $stmt->bindParam(":lluviaDeMaterial", $datos["lluviaDeMaterial"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPiernaElevador", $datos["anchoPiernaElevador"], PDO::PARAM_STR);
        $stmt->bindParam(":profundidadPiernaElevador", $datos["profundidadPiernaElevador"], PDO::PARAM_STR);
        $stmt->bindParam(":tempAmbienteMin", $datos["tempAmbienteMin"], PDO::PARAM_STR);
        $stmt->bindParam(":tempAmbienteMax", $datos["tempAmbienteMax"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoDescarga", $datos["tipoDescarga"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoCarga", $datos["tipoCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":observacionRegistroElevadora", $datos["observacionRegistroElevadora"], PDO::PARAM_STR);
        // echo '<pre>'; print_r($stmt); echo '</pre>';


        if ($stmt->execute()) {
            return "ok";
        } else {
            return "ERROR AL CREAR REGISTRO BANDA ELEVADORA";
        }

        $stmt->close();
        $stmt = null;
        // var_dump($datos);
    }

    public static function mdlActualizarBandaElevadora($tabla, $datos)
    {
        // "INSERT INTO clientes (nit, nameunido) VALUES (:nit, :nameunido)"

        $stmt = Conexion::getConexionLocal()->prepare("UPDATE bandaElevadora set marcaBandaElevadora=:marcaBandaElevadora, anchoBandaElevadora=:anchoBandaElevadora, distanciaEntrePoleasElevadora=:distanciaEntrePoleasElevadora, noLonaBandaElevadora=:noLonaBandaElevadora, tipoLonaBandaElevadora=:tipoLonaBandaElevadora, espesorTotalBandaElevadora=:espesorTotalBandaElevadora, espesorCojinActualElevadora=:espesorCojinActualElevadora,
                         espesorCubiertaSuperiorElevadora=:espesorCubiertaSuperiorElevadora, espesorCubiertaInferiorElevadora=:espesorCubiertaInferiorElevadora, tipoCubiertaElevadora=:tipoCubiertaElevadora, tipoEmpalmeElevadora=:tipoEmpalmeElevadora, estadoEmpalmeElevadora=:estadoEmpalmeElevadora, resistenciaRoturaLonaElevadora=:resistenciaRoturaLonaElevadora, velocidadBandaElevadora=:velocidadBandaElevadora,
                         marcaBandaElevadoraAnterior=:marcaBandaElevadoraAnterior, anchoBandaElevadoraAnterior=:anchoBandaElevadoraAnterior, noLonasBandaElevadoraAnterior=:noLonasBandaElevadoraAnterior, tipoLonaBandaElevadoraAnterior=:tipoLonaBandaElevadoraAnterior, espesorTotalBandaElevadoraAnterior=:espesorTotalBandaElevadoraAnterior, espesorCubiertaSuperiorBandaElevadoraAnterior=:espesorCubiertaSuperiorBandaElevadoraAnterior,
                         espesorCojinElevadoraAnterior=:espesorCojinElevadoraAnterior, espesorCubiertaInferiorBandaElevadoraAnterior=:espesorCubiertaInferiorBandaElevadoraAnterior, tipoCubiertaElevadoraAnterior=:tipoCubiertaElevadoraAnterior, tipoEmpalmeElevadoraAnterior=:tipoEmpalmeElevadoraAnterior, resistenciaRoturaBandaElevadoraAnterior=:resistenciaRoturaBandaElevadoraAnterior,
                         tonsTransportadasBandaElevadoraAnterior=:tonsTransportadasBandaElevadoraAnterior, velocidadBandaElevadoraAnterior=:velocidadBandaElevadoraAnterior, causaFallaCambioBandaElevadoraAnterior=:causaFallaCambioBandaElevadoraAnterior, pesoMaterialEnCadaCangilon=:pesoMaterialEnCadaCangilon, pesoCangilonVacio=:pesoCangilonVacio, longitudCangilon=:longitudCangilon, materialCangilon=:materialCangilon, tipoCangilon=:tipoCangilon,
                         proyeccionCangilon=:proyeccionCangilon, profundidadCangilon=:profundidadCangilon, marcaCangilon=:marcaCangilon, referenciaCangilon=:referenciaCangilon, capacidadCangilon=:capacidadCangilon, noFilasCangilones=:noFilasCangilones, separacionCangilones=:separacionCangilones, noAgujeros=:noAgujeros, distanciaBordeBandaEstructura=:distanciaBordeBandaEstructura, distanciaPosteriorBandaEstructura=:distanciaPosteriorBandaEstructura,
                         distanciaLaboFrontalCangilonEstructura=:distanciaLaboFrontalCangilonEstructura, distanciaBordesCangilonEstructura=:distanciaBordesCangilonEstructura, tipoVentilacion=:tipoVentilacion, diametroPoleaMotrizElevadora=:diametroPoleaMotrizElevadora, anchoPoleaMotrizElevadora=:anchoPoleaMotrizElevadora, tipoPoleaMotrizElevadora=:tipoPoleaMotrizElevadora, largoEjeMotrizElevadora=:largoEjeMotrizElevadora,
                         diametroEjeMotrizElevadora=:diametroEjeMotrizElevadora, bandaCentradaEnPoleaMotrizElevadora=:bandaCentradaEnPoleaMotrizElevadora, estadoRevestimientoPoleaMotrizElevadora=:estadoRevestimientoPoleaMotrizElevadora, potenciaMotorMotrizElevadora=:potenciaMotorMotrizElevadora, rpmSalidaReductorMotrizElevadora=:rpmSalidaReductorMotrizElevadora, guardaReductorPoleaMotrizElevadora=:guardaReductorPoleaMotrizElevadora,
                         diametroPoleaColaElevadora=:diametroPoleaColaElevadora, anchoPoleaColaElevadora=:anchoPoleaColaElevadora, tipoPoleaColaElevadora=:tipoPoleaColaElevadora, largoEjePoleaColaElevadora=:largoEjePoleaColaElevadora, diametroEjePoleaColaElevadora=:diametroEjePoleaColaElevadora, bandaCentradaEnPoleaColaElevadora=:bandaCentradaEnPoleaColaElevadora,
                         estadoRevestimientoPoleaColaElevadora=:estadoRevestimientoPoleaColaElevadora, longitudTensorTornilloPoleaColaElevadora=:longitudTensorTornilloPoleaColaElevadora, longitudRecorridoContrapesaPoleaColaElevadora=:longitudRecorridoContrapesaPoleaColaElevadora, cargaTrabajoBandaElevadora=:cargaTrabajoBandaElevadora, temperaturaMaterialElevadora=:temperaturaMaterialElevadora,
                         empalmeMecanicoElevadora=:empalmeMecanicoElevadora, diametroRoscaElevadora=:diametroRoscaElevadora, largoTornilloElevadora=:largoTornilloElevadora, materialTornilloElevadora=:materialTornilloElevadora, anchoCabezaElevadorPuertaInspeccion=:anchoCabezaElevadorPuertaInspeccion, largoCabezaElevadorPuertaInspeccion=:largoCabezaElevadorPuertaInspeccion, anchoBotaElevadorPuertaInspeccion=:anchoBotaElevadorPuertaInspeccion,
                         largoBotaElevadorPuertaInspeccion=:largoBotaElevadorPuertaInspeccion, monitorPeligro=:monitorPeligro, rodamiento=:rodamiento, monitorDesalineacion=:monitorDesalineacion, monitorVelocidad=:monitorVelocidad, sensorInductivo=:sensorInductivo, indicadorNivel=:indicadorNivel, cajaUnion=:cajaUnion, alarmaYPantalla=:alarmaYPantalla, interruptorSeguridad=:interruptorSeguridad, materialElevadora=:materialElevadora,
                         ataqueQuimicoElevadora=:ataqueQuimicoElevadora, ataqueTemperaturaElevadora=:ataqueTemperaturaElevadora, ataqueAceitesElevadora=:ataqueAceitesElevadora, ataqueAbrasivoElevadora=:ataqueAbrasivoElevadora, capacidadElevadora=:capacidadElevadora, horasTrabajoDiaElevadora=:horasTrabajoDiaElevadora, diasTrabajoSemanaElevadora=:diasTrabajoSemanaElevadora, abrasividadElevadora=:abrasividadElevadora,
                         porcentajeFinosElevadora=:porcentajeFinosElevadora, maxGranulometriaElevadora=:maxGranulometriaElevadora, densidadMaterialElevadora=:densidadMaterialElevadora, tempMaxMaterialSobreBandaElevadora=:tempMaxMaterialSobreBandaElevadora, tempPromedioMaterialSobreBandaElevadora=:tempPromedioMaterialSobreBandaElevadora, variosPuntosDeAlimentacion=:variosPuntosDeAlimentacion, lluviaDeMaterial=:lluviaDeMaterial,
                         anchoPiernaElevador=:anchoPiernaElevador, profundidadPiernaElevador=:profundidadPiernaElevador, tempAmbienteMin=:tempAmbienteMin, tempAmbienteMax=:tempAmbienteMax, tipoDescarga=:tipoDescarga, tipoCarga=:tipoCarga, observacionRegistroElevadora=:observacionRegistroElevadora where idRegistro=:idRegistro");

        $stmt->bindParam(':idRegistro', $datos["idRegistro"], PDO::PARAM_STR);
        $stmt->bindParam(":marcaBandaElevadora", $datos["marcaBandaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoBandaElevadora", $datos["anchoBandaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaEntrePoleasElevadora", $datos["distanciaEntrePoleasElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":noLonaBandaElevadora", $datos["noLonaBandaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoLonaBandaElevadora", $datos["tipoLonaBandaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorTotalBandaElevadora", $datos["espesorTotalBandaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCojinActualElevadora", $datos["espesorCojinActualElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCubiertaSuperiorElevadora", $datos["espesorCubiertaSuperiorElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCubiertaInferiorElevadora", $datos["espesorCubiertaInferiorElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoCubiertaElevadora", $datos["tipoCubiertaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoEmpalmeElevadora", $datos["tipoEmpalmeElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoEmpalmeElevadora", $datos["estadoEmpalmeElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":resistenciaRoturaLonaElevadora", $datos["resistenciaRoturaLonaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":velocidadBandaElevadora", $datos["velocidadBandaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":marcaBandaElevadoraAnterior", $datos["marcaBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoBandaElevadoraAnterior", $datos["anchoBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":noLonasBandaElevadoraAnterior", $datos["noLonasBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoLonaBandaElevadoraAnterior", $datos["tipoLonaBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorTotalBandaElevadoraAnterior", $datos["espesorTotalBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCubiertaSuperiorBandaElevadoraAnterior", $datos["espesorCubiertaSuperiorBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCojinElevadoraAnterior", $datos["espesorCojinElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCubiertaInferiorBandaElevadoraAnterior", $datos["espesorCubiertaInferiorBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoCubiertaElevadoraAnterior", $datos["tipoCubiertaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoEmpalmeElevadoraAnterior", $datos["tipoEmpalmeElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":resistenciaRoturaBandaElevadoraAnterior", $datos["resistenciaRoturaBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":tonsTransportadasBandaElevadoraAnterior", $datos["tonsTransportadasBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":velocidadBandaElevadoraAnterior", $datos["velocidadBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":causaFallaCambioBandaElevadoraAnterior", $datos["causaFallaCambioBandaElevadoraAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":pesoMaterialEnCadaCangilon", $datos["pesoMaterialEnCadaCangilon"], PDO::PARAM_STR);
        $stmt->bindParam(":pesoCangilonVacio", $datos["pesoCangilonVacio"], PDO::PARAM_STR);
        $stmt->bindParam(":longitudCangilon", $datos["longitudCangilon"], PDO::PARAM_STR);
        $stmt->bindParam(":materialCangilon", $datos["materialCangilon"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoCangilon", $datos["tipoCangilon"], PDO::PARAM_STR);
        $stmt->bindParam(":proyeccionCangilon", $datos["proyeccionCangilon"], PDO::PARAM_STR);
        $stmt->bindParam(":profundidadCangilon", $datos["profundidadCangilon"], PDO::PARAM_STR);
        $stmt->bindParam(":marcaCangilon", $datos["marcaCangilon"], PDO::PARAM_STR);
        $stmt->bindParam(":referenciaCangilon", $datos["referenciaCangilon"], PDO::PARAM_STR);
        $stmt->bindParam(":capacidadCangilon", $datos["capacidadCangilon"], PDO::PARAM_STR);
        $stmt->bindParam(":noFilasCangilones", $datos["noFilasCangilones"], PDO::PARAM_STR);
        $stmt->bindParam(":separacionCangilones", $datos["separacionCangilones"], PDO::PARAM_STR);
        $stmt->bindParam(":noAgujeros", $datos["noAgujeros"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaBordeBandaEstructura", $datos["distanciaBordeBandaEstructura"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaPosteriorBandaEstructura", $datos["distanciaPosteriorBandaEstructura"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaLaboFrontalCangilonEstructura", $datos["distanciaLaboFrontalCangilonEstructura"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaBordesCangilonEstructura", $datos["distanciaBordesCangilonEstructura"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoVentilacion", $datos["tipoVentilacion"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroPoleaMotrizElevadora", $datos["diametroPoleaMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPoleaMotrizElevadora", $datos["anchoPoleaMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoPoleaMotrizElevadora", $datos["tipoPoleaMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjeMotrizElevadora", $datos["largoEjeMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjeMotrizElevadora", $datos["diametroEjeMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":bandaCentradaEnPoleaMotrizElevadora", $datos["bandaCentradaEnPoleaMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRevestimientoPoleaMotrizElevadora", $datos["estadoRevestimientoPoleaMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":potenciaMotorMotrizElevadora", $datos["potenciaMotorMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":rpmSalidaReductorMotrizElevadora", $datos["rpmSalidaReductorMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaReductorPoleaMotrizElevadora", $datos["guardaReductorPoleaMotrizElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroPoleaColaElevadora", $datos["diametroPoleaColaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPoleaColaElevadora", $datos["anchoPoleaColaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoPoleaColaElevadora", $datos["tipoPoleaColaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjePoleaColaElevadora", $datos["largoEjePoleaColaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjePoleaColaElevadora", $datos["diametroEjePoleaColaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":bandaCentradaEnPoleaColaElevadora", $datos["bandaCentradaEnPoleaColaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRevestimientoPoleaColaElevadora", $datos["estadoRevestimientoPoleaColaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":longitudTensorTornilloPoleaColaElevadora", $datos["longitudTensorTornilloPoleaColaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":longitudRecorridoContrapesaPoleaColaElevadora", $datos["longitudRecorridoContrapesaPoleaColaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":cargaTrabajoBandaElevadora", $datos["cargaTrabajoBandaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":temperaturaMaterialElevadora", $datos["temperaturaMaterialElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":empalmeMecanicoElevadora", $datos["empalmeMecanicoElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroRoscaElevadora", $datos["diametroRoscaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":largoTornilloElevadora", $datos["largoTornilloElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":materialTornilloElevadora", $datos["materialTornilloElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoCabezaElevadorPuertaInspeccion", $datos["anchoCabezaElevadorPuertaInspeccion"], PDO::PARAM_STR);
        $stmt->bindParam(":largoCabezaElevadorPuertaInspeccion", $datos["largoCabezaElevadorPuertaInspeccion"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoBotaElevadorPuertaInspeccion", $datos["anchoBotaElevadorPuertaInspeccion"], PDO::PARAM_STR);
        $stmt->bindParam(":largoBotaElevadorPuertaInspeccion", $datos["largoBotaElevadorPuertaInspeccion"], PDO::PARAM_STR);
        $stmt->bindParam(":monitorPeligro", $datos["monitorPeligro"], PDO::PARAM_STR);
        $stmt->bindParam(":rodamiento", $datos["rodamiento"], PDO::PARAM_STR);
        $stmt->bindParam(":monitorDesalineacion", $datos["monitorDesalineacion"], PDO::PARAM_STR);
        $stmt->bindParam(":monitorVelocidad", $datos["monitorVelocidad"], PDO::PARAM_STR);
        $stmt->bindParam(":sensorInductivo", $datos["sensorInductivo"], PDO::PARAM_STR);
        $stmt->bindParam(":indicadorNivel", $datos["indicadorNivel"], PDO::PARAM_STR);
        $stmt->bindParam(":cajaUnion", $datos["cajaUnion"], PDO::PARAM_STR);
        $stmt->bindParam(":alarmaYPantalla", $datos["alarmaYPantalla"], PDO::PARAM_STR);
        $stmt->bindParam(":interruptorSeguridad", $datos["interruptorSeguridad"], PDO::PARAM_STR);
        $stmt->bindParam(":materialElevadora", $datos["materialElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":ataqueQuimicoElevadora", $datos["ataqueQuimicoElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":ataqueTemperaturaElevadora", $datos["ataqueTemperaturaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":ataqueAceitesElevadora", $datos["ataqueAceitesElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":ataqueAbrasivoElevadora", $datos["ataqueAbrasivoElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":capacidadElevadora", $datos["capacidadElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":horasTrabajoDiaElevadora", $datos["horasTrabajoDiaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":diasTrabajoSemanaElevadora", $datos["diasTrabajoSemanaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":abrasividadElevadora", $datos["abrasividadElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":porcentajeFinosElevadora", $datos["porcentajeFinosElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":maxGranulometriaElevadora", $datos["maxGranulometriaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":densidadMaterialElevadora", $datos["densidadMaterialElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tempMaxMaterialSobreBandaElevadora", $datos["tempMaxMaterialSobreBandaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tempPromedioMaterialSobreBandaElevadora", $datos["tempPromedioMaterialSobreBandaElevadora"], PDO::PARAM_STR);
        $stmt->bindParam(":variosPuntosDeAlimentacion", $datos["variosPuntosDeAlimentacion"], PDO::PARAM_STR);
        $stmt->bindParam(":lluviaDeMaterial", $datos["lluviaDeMaterial"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPiernaElevador", $datos["anchoPiernaElevador"], PDO::PARAM_STR);
        $stmt->bindParam(":profundidadPiernaElevador", $datos["profundidadPiernaElevador"], PDO::PARAM_STR);
        $stmt->bindParam(":tempAmbienteMin", $datos["tempAmbienteMin"], PDO::PARAM_STR);
        $stmt->bindParam(":tempAmbienteMax", $datos["tempAmbienteMax"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoDescarga", $datos["tipoDescarga"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoCarga", $datos["tipoCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":observacionRegistroElevadora", $datos["observacionRegistroElevadora"], PDO::PARAM_STR);

        if ($stmt->execute()) {
            return "ok";
        } else {
            return "ERROR AL CREAR REGISTRO BANDA ELEVADORA";
        }

        $stmt->close();
        $stmt = null;
        // var_dump($datos);
    }

    public static function mdlActualizarBandaElevadoraHost($tabla, $idRegistro)
    {
        $stmt = Conexion::getConexionHost()->prepare("UPDATE bandaElevadora set estadoRegistroSincronizadoElevadora='Sincronizado' where idRegistro=$idRegistro");

        if ($stmt->execute()) {
            return "ok";
        } else {
            return "ERROR AL CREAR REGISTRO BANDA ELEVADORA";
        }

        $stmt->close();
        $stmt = null;
    }

    public static function mdlBandaTransmisionHost($tabla)
    {
        $stmt = Conexion::getConexionHost()->prepare("SELECT * FROM $tabla where estadoRegistroSincronizadoTransmision='Pendiente Insertar'");
        $stmt->execute();
        return $stmt->fetchAll();
    }

    public static function mdlBandaTransmisionActualizar($tabla)
    {
        $stmt = Conexion::getConexionLocal()->prepare("SELECT * FROM $tabla where estadoRegistroSincronizadoTransmision='Pendiente Actualizar'");
        $stmt->execute();
        return $stmt->fetchAll();
    }

    public static function mdlDatosTransmision($tabla, $datos)
    {
        // "INSERT INTO clientes (nit, nameunido) VALUES (:nit, :nameunido)"

        $stmt = Conexion::getConexionLocal()->prepare("INSERT into bandaTransmision (idRegistro, tipoParteTransmision, anchoPoleaMotrizTransmision, anchoPoleaConducidaTransmision, diametroPoleaMotrizTransmision, diametroPoleaConducidaTransmision, rpmSalidaReductorTransmision, potenciaMotorTransmision, distanciaEntreCentrosTransmision, anchoBandaTransmision, observacionRegistro)

  values(:idRegistro, :tipoParteTransmision, :anchoPoleaMotrizTransmision, :anchoPoleaConducidaTransmision, :diametroPoleaMotrizTransmision, :diametroPoleaConducidaTransmision, :rpmSalidaReductorTransmision, :potenciaMotorTransmision, :distanciaEntreCentrosTransmision, :anchoBandaTransmision,:observacionRegistro)");

        $stmt->bindParam(":idRegistro", $datos["idRegistro"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoBandaTransmision", $datos["anchoBandaTransmision"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaEntreCentrosTransmision", $datos["distanciaEntreCentrosTransmision"], PDO::PARAM_STR);
        $stmt->bindParam(":potenciaMotorTransmision", $datos["potenciaMotorTransmision"], PDO::PARAM_STR);
        $stmt->bindParam(':rpmSalidaReductorTransmision', $datos["rpmSalidaReductorTransmision"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroPoleaConducidaTransmision", $datos["diametroPoleaConducidaTransmision"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPoleaConducidaTransmision", $datos["anchoPoleaConducidaTransmision"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroPoleaMotrizTransmision", $datos["diametroPoleaMotrizTransmision"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPoleaMotrizTransmision", $datos["anchoPoleaMotrizTransmision"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoParteTransmision", $datos["tipoParteTransmision"], PDO::PARAM_STR);
        $stmt->bindParam(":observacionRegistro", $datos["observacionRegistroPesada"], PDO::PARAM_STR);

        if ($stmt->execute()) {
            return "ok";
        } else {
            return "ERROR AL CREAR REGISTRO BANDA ELEVADORA";
        }

        $stmt->close();
        $stmt = null;
    }

    public static function mdlActualizarTransmision($tabla, $datos)
    {
        // "INSERT INTO clientes (nit, nameunido) VALUES (:nit, :nameunido)"

        $stmt = Conexion::getConexionLocal()->prepare(" UPDATE $tabla set tipoParteTransmision=:tipoParteTransmision, anchoPoleaMotrizTransmision=:anchoPoleaMotrizTransmision, anchoPoleaConducidaTransmision=:anchoPoleaConducidaTransmision, diametroPoleaMotrizTransmision=:diametroPoleaMotrizTransmision, diametroPoleaConducidaTransmision=:diametroPoleaConducidaTransmision, rpmSalidaReductorTransmision=:rpmSalidaReductorTransmision, potenciaMotorTransmision=:potenciaMotorTransmision, distanciaEntreCentrosTransmision=:distanciaEntreCentrosTransmision, anchoBandaTransmision=:anchoBandaTransmision,observacionRegistro=:observacionRegistro where idRegistro=:idRegistro");

        $stmt->bindParam(":idRegistro", $datos["idRegistro"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoBandaTransmision", $datos["anchoBandaTransmision"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaEntreCentrosTransmision", $datos["distanciaEntreCentrosTransmision"], PDO::PARAM_STR);
        $stmt->bindParam(":potenciaMotorTransmision", $datos["potenciaMotorTransmision"], PDO::PARAM_STR);
        $stmt->bindParam(':rpmSalidaReductorTransmision', $datos["rpmSalidaReductorTransmision"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroPoleaConducidaTransmision", $datos["diametroPoleaConducidaTransmision"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPoleaConducidaTransmision", $datos["anchoPoleaConducidaTransmision"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroPoleaMotrizTransmision", $datos["diametroPoleaMotrizTransmision"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPoleaMotrizTransmision", $datos["anchoPoleaMotrizTransmision"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoParteTransmision", $datos["tipoParteTransmision"], PDO::PARAM_STR);
        $stmt->bindParam(":observacionRegistro", $datos["observacionRegistroPesada"], PDO::PARAM_STR);

        if ($stmt->execute()) {
            return "ok";
        } else {
            return "ERROR AL ACTUALIZAR TRANSMISION";
        }

        $stmt->close();
        $stmt = null;
    }

    public static function mdlActualizarTransmsionHost($tabla, $idRegistro)
    {
        $stmt = Conexion::getConexionHost()->prepare("UPDATE $tabla set estadoRegistroSincronizadoTransmision='Sincronizado' where idRegistro=$idRegistro");

        if ($stmt->execute()) {
            return "ok";
        } else {
            return "ERROR AL CREAR REGISTRO BANDA ELEVADORA";
        }

        $stmt->close();
        $stmt = null;
    }

    public static function mdlBandaTransportadoraHost($tabla)
    {
        $stmt = Conexion::getConexionHost()->prepare("SELECT * FROM $tabla where estadoRegistroSincronizadoTransportadora='Pendiente Insertar' or estadoRegistroSincronizadoTransportadora is null");
        $stmt->execute();
        return $stmt->fetchAll();
    }

    public static function mdlBandaTransportadoraActualizar($tabla)
    {
        $stmt = Conexion::getConexionLocal()->prepare("SELECT * FROM $tabla where estadoRegistroSincronizadoTransportadora='Pendiente Actualizar");
        $stmt->execute();
        return $stmt->fetchAll();
    }

    public static function mdlDatosTransportadora($tabla, $datos)
    {
        // "INSERT INTO clientes (nit, nameunido) VALUES (:nit, :nameunido)"

        $stmt = Conexion::getConexionLocal()->prepare("INSERT into bandaTransportadora (idRegistro, marcaBandaTransportadora, anchoBandaTransportadora, noLonasBandaTransportadora, tipoLonaBandaTransportadora, espesorTotalBandaTransportadora, espesorCubiertaSuperiorTransportadora,
                         espesorCojinTransportadora, espesorCubiertaInferiorTransportadora, tipoCubiertaTransportadora, tipoEmpalmeTransportadora, estadoEmpalmeTransportadora, distanciaEntrePoleasBandaHorizontal,
                         inclinacionBandaHorizontal, recorridoUtilTensorBandaHorizontal, longitudSinfinBandaHorizontal, resistenciaRoturaLonaTransportadora, localizacionTensorTransportadora, bandaReversible, bandaDeArrastre,
                         velocidadBandaHorizontal, marcaBandaHorizontalAnterior, anchoBandaHorizontalAnterior, noLonasBandaHorizontalAnterior, tipoLonaBandaHorizontalAnterior, espesorTotalBandaHorizontalAnterior,
                         espesorCubiertaSuperiorBandaHorizontalAnterior, espesorCubiertaInferiorBandaHorizontalAnterior, espesorCojinBandaHorizontalAnterior, tipoEmpalmeBandaHorizontalAnterior, resistenciaRoturaLonaBandaHorizontalAnterior,
                         tipoCubiertaBandaHorizontalAnterior, tonsTransportadasBandaHoizontalAnterior, causaFallaCambioBandaHorizontal, diametroPoleaColaTransportadora, anchoPoleaColaTransportadora, tipoPoleaColaTransportadora,
                         largoEjePoleaColaTransportadora, diametroEjePoleaColaHorizontal, icobandasCentradaPoleaColaTransportadora, anguloAmarrePoleaColaTransportadora, estadoRvtoPoleaColaTransportadora,
                         tipoTransicionPoleaColaTransportadora, distanciaTransicionPoleaColaTransportadora, longitudTensorTornilloPoleaColaTransportadora, longitudRecorridoContrapesaPoleaColaTransportadora, guardaPoleaColaTransportadora,
                         hayDesviador, elDesviadorBascula, presionUniformeALoAnchoDeLaBanda, cauchoVPlow, anchoVPlow, espesorVPlow, tipoRevestimientoTolvaCarga, estadoRevestimientoTolvaCarga, duracionPromedioRevestimiento,
                         deflectores, altureCaida, longitudImpacto, material, anguloSobreCarga, ataqueQuimicoTransportadora, ataqueTemperaturaTransportadora, ataqueAceiteTransportadora, ataqueImpactoTransportadora, capacidadTransportadora,
                         horasTrabajoPorDiaTransportadora, diasTrabajPorSemanaTransportadora, alimentacionCentradaTransportadora, abrasividadTransportadora, porcentajeFinosTransportadora, maxGranulometriaTransportadora,
                         maxPesoTransportadora, densidadTransportadora, tempMaximaMaterialSobreBandaTransportadora, tempPromedioMaterialSobreBandaTransportadora, fugaDeMaterialesEnLaColaDelChute, fugaDeMaterialesPorLosCostados,
                         fugaMateriales, cajaColaDeTolva, fugaDeMaterialParticulaALaSalidaDelChute, anchoChute, largoChute, alturaChute, abrazadera, cauchoGuardabandas, triSealMultiSeal, espesorGuardaBandas, anchoGuardaBandas,
                         largoGuardaBandas, protectorGuardaBandas, cortinaAntiPolvo1, cortinaAntiPolvo2, cortinaAntiPolvo3, boquillasCanonesDeAire, tempAmbienteMaxTransportadora, tempAmbienteMinTransportadora, tieneRodillosImpacto,
                         camaImpacto, camaSellado, basculaPesaje, rodilloCarga, rodilloImpacto, basculaASGCO, barraImpacto, barraDeslizamiento, espesorUHMV, anchoBarra, largoBarra, anguloAcanalamientoArtesa1, anguloAcanalamientoArtesa2,
                         anguloAcanalamientoArtesa3, anguloAcanalamientoArtesa1AntesPoleaMotriz, anguloAcanalamientoArtesa2AntesPoleaMotriz, anguloAcanalamientoArtesa3AntesPoleaMotriz, integridadSoportesRodilloImpacto,
                         materialAtrapadoEntreCortinas, materialAtrapadoEntreGuardabandas, materialAtrapadoEnBanda, integridadSoportesCamaImpacto, inclinacionZonaCargue, sistemaAlineacionCarga, cantidadSistemaAlineacionEnCarga,
                         sistemasAlineacionCargaFuncionando, sistemaAlineacionEnRetorno, cantidadSistemaAlineacionEnRetorno, sistemasAlineacionRetornoFuncionando, sistemaAlineacionRetornoPlano, sistemaAlineacionArtesaCarga,
                         sistemaAlineacionRetornoEnV, largoEjeRodilloCentralCarga, diametroEjeRodilloCentralCarga, largoTuboRodilloCentralCarga, largoEjeRodilloLateralCarga, diametroEjeRodilloLateralCarga, diametroRodilloLateralCarga,
                         largoTuboRodilloLateralCarga, tipoRodilloCarga, distanciaEntreArtesasCarga, anchoInternoChasisRodilloCarga, anchoExternoChasisRodilloCarga, anguloAcanalamientoArtesaCArga, detalleRodilloCentralCarga,
                         detalleRodilloLateralCarg, diametroPoleaMotrizTransportadora, anchoPoleaMotrizTransportadora, tipoPoleaMotrizTransportadora, largoEjePoleaMotrizTransportadora, diametroEjeMotrizTransportadora,
                         icobandasCentraEnPoleaMotrizTransportadora, anguloAmarrePoleaMotrizTransportadora, estadoRevestimientoPoleaMotrizTransportadora, tipoTransicionPoleaMotrizTransportadora,
                         distanciaTransicionPoleaMotrizTransportadora, potenciaMotorTransportadora, guardaPoleaMotrizTransportadora, anchoEstructura, anchoTrayectoCarga, pasarelaRespectoAvanceBanda, materialAlimenticioTransportadora,
                         materialAcidoTransportadora, materialTempEntre80y150Transportadora, materialSecoTransportadora, materialHumedoTransportadora, materialAbrasivoFinoTransportadora, materialPegajosoTransportadora,
                         materialGrasosoAceitosoTransportadora, marcaLimpiadorPrimario, referenciaLimpiadorPrimario, anchoCuchillaLimpiadorPrimario, altoCuchillaLimpiadorPrimario, estadoCuchillaLimpiadorPrimario,
                         estadoTensorLimpiadorPrimario, estadoTuboLimpiadorPrimario, frecuenciaRevisionCuchilla, cuchillaEnContactoConBanda, marcaLimpiadorSecundario, referenciaLimpiadorSecundario, anchoCuchillaLimpiadorSecundario,
                         altoCuchillaLimpiadorSecundario, estadoCuchillaLimpiadorSecundario, estadoTensorLimpiadorSecundario, estadoTuboLimpiadorSecundario, frecuenciaRevisionCuchilla1, cuchillaEnContactoConBanda1, sistemaDribbleChute,
                         marcaLimpiadorTerciario, referenciaLimpiadorTerciario, anchoCuchillaLimpiadorTerciario, altoCuchillaLimpiadorTerciario, estadoCuchillaLimpiadorTerciario, estadoTensorLimpiadorTerciario, estadoTuboLimpiadorTerciario,
                         frecuenciaRevisionCuchilla2, cuchillaEnContactoConBanda2, estadoRodilloRetorno, largoEjeRodilloRetorno, diametroEjeRodilloRetorno, diametroRodilloRetorno, largoTuboRodilloRetorno, tipoRodilloRetorno,
                         distanciaEntreRodillosRetorno, anchoInternoChasisRetorno, anchoExternoChasisRetorno, detalleRodilloRetorno, diametroPoleaAmarrePoleaMotriz, anchoPoleaAmarrePoleaMotriz, tipoPoleaAmarrePoleaMotriz,
                         largoEjePoleaAmarrePoleaMotriz, diametroEjePoleaAmarrePoleaMotriz, icobandasCentradaPoleaAmarrePoleaMotriz, estadoRevestimientoPoleaAmarrePoleaMotriz, dimetroPoleaAmarrePoleaCola, anchoPoleaAmarrePoleaCola,
                         largoEjePoleaAmarrePoleaCola, tipoPoleaAmarrePoleaCola, diametroEjePoleaAmarrePoleaCola, icobandasCentradaPoleaAmarrePoleaCola, estadoRevestimientoPoleaAmarrePoleaCola, diametroPoleaTensora,
                         anchoPoleaTensora, tipoPoleaTensora, largoEjePoleaTensora, diametroEjePoleaTensora, icobandasCentradaEnPoleaTensora, recorridoPoleaTensora, estadoRevestimientoPoleaTensora, tipoTransicionPoleaTensora,
                         distanciaTransicionPoleaColaTensora, potenciaMotorPoleaTensora, guardaPoleaTensora, puertasInspeccion, guardaRodilloRetornoPlano, guardaTruTrainer, guardaPoleaDeflectora, guardaZonaDeTransito, guardaMotores,
                         guardaCadenas, guardaCorreas, interruptoresDeSeguridad, sirenasDeSeguridad, guardaRodilloRetornoV, diametroRodilloCentralCarga, tipoRodilloImpacto, integridadSoporteCamaSellado, ataqueAbrasivoTransportadora,
                         observacionRegistroTransportadora)

  values(:idRegistro, :marcaBandaTransportadora, :anchoBandaTransportadora, :noLonasBandaTransportadora, :tipoLonaBandaTransportadora, :espesorTotalBandaTransportadora, :espesorCubiertaSuperiorTransportadora,
                         :espesorCojinTransportadora, :espesorCubiertaInferiorTransportadora, :tipoCubiertaTransportadora, :tipoEmpalmeTransportadora, :estadoEmpalmeTransportadora, :distanciaEntrePoleasBandaHorizontal,
                         :inclinacionBandaHorizontal, :recorridoUtilTensorBandaHorizontal, :longitudSinfinBandaHorizontal, :resistenciaRoturaLonaTransportadora, :localizacionTensorTransportadora, :bandaReversible, :bandaDeArrastre,
                         :velocidadBandaHorizontal, :marcaBandaHorizontalAnterior, :anchoBandaHorizontalAnterior, :noLonasBandaHorizontalAnterior, :tipoLonaBandaHorizontalAnterior, :espesorTotalBandaHorizontalAnterior,
                         :espesorCubiertaSuperiorBandaHorizontalAnterior, :espesorCubiertaInferiorBandaHorizontalAnterior, :espesorCojinBandaHorizontalAnterior, :tipoEmpalmeBandaHorizontalAnterior, :resistenciaRoturaLonaBandaHorizontalAnterior,
                         :tipoCubiertaBandaHorizontalAnterior, :tonsTransportadasBandaHoizontalAnterior, :causaFallaCambioBandaHorizontal, :diametroPoleaColaTransportadora, :anchoPoleaColaTransportadora, :tipoPoleaColaTransportadora,
                         :largoEjePoleaColaTransportadora, :diametroEjePoleaColaHorizontal, :icobandasCentradaPoleaColaTransportadora, :anguloAmarrePoleaColaTransportadora, :estadoRvtoPoleaColaTransportadora,
                         :tipoTransicionPoleaColaTransportadora, :distanciaTransicionPoleaColaTransportadora, :longitudTensorTornilloPoleaColaTransportadora, :longitudRecorridoContrapesaPoleaColaTransportadora, :guardaPoleaColaTransportadora,
                         :hayDesviador, :elDesviadorBascula, :presionUniformeALoAnchoDeLaBanda, :cauchoVPlow, :anchoVPlow, :espesorVPlow, :tipoRevestimientoTolvaCarga, :estadoRevestimientoTolvaCarga, :duracionPromedioRevestimiento,
                         :deflectores, :altureCaida, :longitudImpacto, :material, :anguloSobreCarga, :ataqueQuimicoTransportadora, :ataqueTemperaturaTransportadora, :ataqueAceiteTransportadora, :ataqueImpactoTransportadora, :capacidadTransportadora,
                         :horasTrabajoPorDiaTransportadora, :diasTrabajPorSemanaTransportadora, :alimentacionCentradaTransportadora, :abrasividadTransportadora, :porcentajeFinosTransportadora, :maxGranulometriaTransportadora,
                         :maxPesoTransportadora, :densidadTransportadora, :tempMaximaMaterialSobreBandaTransportadora, :tempPromedioMaterialSobreBandaTransportadora, :fugaDeMaterialesEnLaColaDelChute, :fugaDeMaterialesPorLosCostados,
                         :fugaMateriales, :cajaColaDeTolva, :fugaDeMaterialParticulaALaSalidaDelChute, :anchoChute, :largoChute, :alturaChute, :abrazadera, :cauchoGuardabandas, :triSealMultiSeal, :espesorGuardaBandas, :anchoGuardaBandas,
                         :largoGuardaBandas, :protectorGuardaBandas, :cortinaAntiPolvo1, :cortinaAntiPolvo2, :cortinaAntiPolvo3, :boquillasCanonesDeAire, :tempAmbienteMaxTransportadora, :tempAmbienteMinTransportadora, :tieneRodillosImpacto,
                         :camaImpacto, :camaSellado, :basculaPesaje, :rodilloCarga, :rodilloImpacto, :basculaASGCO, :barraImpacto, :barraDeslizamiento, :espesorUHMV, :anchoBarra, :largoBarra, :anguloAcanalamientoArtesa1, :anguloAcanalamientoArtesa2,
                         :anguloAcanalamientoArtesa3, :anguloAcanalamientoArtesa1AntesPoleaMotriz, :anguloAcanalamientoArtesa2AntesPoleaMotriz, :anguloAcanalamientoArtesa3AntesPoleaMotriz, :integridadSoportesRodilloImpacto,
                         :materialAtrapadoEntreCortinas, :materialAtrapadoEntreGuardabandas, :materialAtrapadoEnBanda, :integridadSoportesCamaImpacto, :inclinacionZonaCargue, :sistemaAlineacionCarga, :cantidadSistemaAlineacionEnCarga,
                         :sistemasAlineacionCargaFuncionando, :sistemaAlineacionEnRetorno, :cantidadSistemaAlineacionEnRetorno, :sistemasAlineacionRetornoFuncionando, :sistemaAlineacionRetornoPlano, :sistemaAlineacionArtesaCarga,
                         :sistemaAlineacionRetornoEnV, :largoEjeRodilloCentralCarga, :diametroEjeRodilloCentralCarga, :largoTuboRodilloCentralCarga, :largoEjeRodilloLateralCarga, :diametroEjeRodilloLateralCarga, :diametroRodilloLateralCarga,
                         :largoTuboRodilloLateralCarga, :tipoRodilloCarga, :distanciaEntreArtesasCarga, :anchoInternoChasisRodilloCarga, :anchoExternoChasisRodilloCarga, :anguloAcanalamientoArtesaCArga, :detalleRodilloCentralCarga,
                         :detalleRodilloLateralCarg, :diametroPoleaMotrizTransportadora, :anchoPoleaMotrizTransportadora, :tipoPoleaMotrizTransportadora, :largoEjePoleaMotrizTransportadora, :diametroEjeMotrizTransportadora,
                         :icobandasCentraEnPoleaMotrizTransportadora, :anguloAmarrePoleaMotrizTransportadora, :estadoRevestimientoPoleaMotrizTransportadora, :tipoTransicionPoleaMotrizTransportadora,
                         :distanciaTransicionPoleaMotrizTransportadora, :potenciaMotorTransportadora, :guardaPoleaMotrizTransportadora, :anchoEstructura, :anchoTrayectoCarga, :pasarelaRespectoAvanceBanda, :materialAlimenticioTransportadora,
                         :materialAcidoTransportadora, :materialTempEntre80y150Transportadora, :materialSecoTransportadora, :materialHumedoTransportadora, :materialAbrasivoFinoTransportadora, :materialPegajosoTransportadora,
                         :materialGrasosoAceitosoTransportadora, :marcaLimpiadorPrimario, :referenciaLimpiadorPrimario, :anchoCuchillaLimpiadorPrimario, :altoCuchillaLimpiadorPrimario, :estadoCuchillaLimpiadorPrimario,
                         :estadoTensorLimpiadorPrimario, :estadoTuboLimpiadorPrimario, :frecuenciaRevisionCuchilla, :cuchillaEnContactoConBanda, :marcaLimpiadorSecundario, :referenciaLimpiadorSecundario, :anchoCuchillaLimpiadorSecundario,
                         :altoCuchillaLimpiadorSecundario, :estadoCuchillaLimpiadorSecundario, :estadoTensorLimpiadorSecundario, :estadoTuboLimpiadorSecundario, :frecuenciaRevisionCuchilla1, :cuchillaEnContactoConBanda1, :sistemaDribbleChute,
                         :marcaLimpiadorTerciario, :referenciaLimpiadorTerciario, :anchoCuchillaLimpiadorTerciario, :altoCuchillaLimpiadorTerciario, :estadoCuchillaLimpiadorTerciario, :estadoTensorLimpiadorTerciario, :estadoTuboLimpiadorTerciario,
                         :frecuenciaRevisionCuchilla2, :cuchillaEnContactoConBanda2, :estadoRodilloRetorno, :largoEjeRodilloRetorno, :diametroEjeRodilloRetorno, :diametroRodilloRetorno, :largoTuboRodilloRetorno, :tipoRodilloRetorno,
                         :distanciaEntreRodillosRetorno, :anchoInternoChasisRetorno, :anchoExternoChasisRetorno, :detalleRodilloRetorno, :diametroPoleaAmarrePoleaMotriz, :anchoPoleaAmarrePoleaMotriz, :tipoPoleaAmarrePoleaMotriz,
                         :largoEjePoleaAmarrePoleaMotriz, :diametroEjePoleaAmarrePoleaMotriz, :icobandasCentradaPoleaAmarrePoleaMotriz, :estadoRevestimientoPoleaAmarrePoleaMotriz, :dimetroPoleaAmarrePoleaCola, :anchoPoleaAmarrePoleaCola,
                         :largoEjePoleaAmarrePoleaCola, :tipoPoleaAmarrePoleaCola, :diametroEjePoleaAmarrePoleaCola, :icobandasCentradaPoleaAmarrePoleaCola, :estadoRevestimientoPoleaAmarrePoleaCola, :diametroPoleaTensora,
                         :anchoPoleaTensora, :tipoPoleaTensora, :largoEjePoleaTensora, :diametroEjePoleaTensora, :icobandasCentradaEnPoleaTensora, :recorridoPoleaTensora, :estadoRevestimientoPoleaTensora, :tipoTransicionPoleaTensora,
                         :distanciaTransicionPoleaColaTensora, :potenciaMotorPoleaTensora, :guardaPoleaTensora, :puertasInspeccion, :guardaRodilloRetornoPlano, :guardaTruTrainer, :guardaPoleaDeflectora, :guardaZonaDeTransito, :guardaMotores,
                         :guardaCadenas, :guardaCorreas, :interruptoresDeSeguridad, :sirenasDeSeguridad, :guardaRodilloRetornoV, :diametroRodilloCentralCarga, :tipoRodilloImpacto, :integridadSoporteCamaSellado, :ataqueAbrasivoTransportadora,
                         :observacionRegistroTransportadora)");

        $stmt->bindParam(":idRegistro", $datos["idRegistro"], PDO::PARAM_STR);
        $stmt->bindParam(":marcaBandaTransportadora", $datos["marcaBandaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoBandaTransportadora", $datos["anchoBandaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":noLonasBandaTransportadora", $datos["noLonasBandaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoLonaBandaTransportadora", $datos["tipoLonaBandaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorTotalBandaTransportadora", $datos["espesorTotalBandaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCubiertaSuperiorTransportadora", $datos["espesorCubiertaSuperiorTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCojinTransportadora", $datos["espesorCojinTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCubiertaInferiorTransportadora", $datos["espesorCubiertaInferiorTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoCubiertaTransportadora", $datos["tipoCubiertaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoEmpalmeTransportadora", $datos["tipoEmpalmeTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoEmpalmeTransportadora", $datos["estadoEmpalmeTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaEntrePoleasBandaHorizontal", $datos["distanciaEntrePoleasBandaHorizontal"], PDO::PARAM_STR);
        $stmt->bindParam(":inclinacionBandaHorizontal", $datos["inclinacionBandaHorizontal"], PDO::PARAM_STR);
        $stmt->bindParam(":recorridoUtilTensorBandaHorizontal", $datos["recorridoUtilTensorBandaHorizontal"], PDO::PARAM_STR);
        $stmt->bindParam(":longitudSinfinBandaHorizontal", $datos["longitudSinfinBandaHorizontal"], PDO::PARAM_STR);
        $stmt->bindParam(":resistenciaRoturaLonaTransportadora", $datos["resistenciaRoturaLonaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":localizacionTensorTransportadora", $datos["localizacionTensorTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":bandaReversible", $datos["bandaReversible"], PDO::PARAM_STR);
        $stmt->bindParam(":bandaDeArrastre", $datos["bandaDeArrastre"], PDO::PARAM_STR);
        $stmt->bindParam(":velocidadBandaHorizontal", $datos["velocidadBandaHorizontal"], PDO::PARAM_STR);
        $stmt->bindParam(":marcaBandaHorizontalAnterior", $datos["marcaBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoBandaHorizontalAnterior", $datos["anchoBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":noLonasBandaHorizontalAnterior", $datos["noLonasBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoLonaBandaHorizontalAnterior", $datos["tipoLonaBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorTotalBandaHorizontalAnterior", $datos["espesorTotalBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCubiertaSuperiorBandaHorizontalAnterior", $datos["espesorCubiertaSuperiorBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCubiertaInferiorBandaHorizontalAnterior", $datos["espesorCubiertaInferiorBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCojinBandaHorizontalAnterior", $datos["espesorCojinBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoEmpalmeBandaHorizontalAnterior", $datos["tipoEmpalmeBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":resistenciaRoturaLonaBandaHorizontalAnterior", $datos["resistenciaRoturaLonaBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoCubiertaBandaHorizontalAnterior", $datos["tipoCubiertaBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":tonsTransportadasBandaHoizontalAnterior", $datos["tonsTransportadasBandaHoizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":causaFallaCambioBandaHorizontal", $datos["causaFallaCambioBandaHorizontal"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroPoleaColaTransportadora", $datos["diametroPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPoleaColaTransportadora", $datos["anchoPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoPoleaColaTransportadora", $datos["tipoPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjePoleaColaTransportadora", $datos["largoEjePoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjePoleaColaHorizontal", $datos["diametroEjePoleaColaHorizontal"], PDO::PARAM_STR);
        $stmt->bindParam(":icobandasCentradaPoleaColaTransportadora", $datos["icobandasCentradaPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloAmarrePoleaColaTransportadora", $datos["anguloAmarrePoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRvtoPoleaColaTransportadora", $datos["estadoRvtoPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoTransicionPoleaColaTransportadora", $datos["tipoTransicionPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaTransicionPoleaColaTransportadora", $datos["distanciaTransicionPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":longitudTensorTornilloPoleaColaTransportadora", $datos["longitudTensorTornilloPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":longitudRecorridoContrapesaPoleaColaTransportadora", $datos["longitudRecorridoContrapesaPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaPoleaColaTransportadora", $datos["guardaPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":hayDesviador", $datos["hayDesviador"], PDO::PARAM_STR);
        $stmt->bindParam(":elDesviadorBascula", $datos["elDesviadorBascula"], PDO::PARAM_STR);
        $stmt->bindParam(":presionUniformeALoAnchoDeLaBanda", $datos["presionUniformeALoAnchoDeLaBanda"], PDO::PARAM_STR);
        $stmt->bindParam(":cauchoVPlow", $datos["cauchoVPlow"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoVPlow", $datos["anchoVPlow"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorVPlow", $datos["espesorVPlow"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoRevestimientoTolvaCarga", $datos["tipoRevestimientoTolvaCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRevestimientoTolvaCarga", $datos["estadoRevestimientoTolvaCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":duracionPromedioRevestimiento", $datos["duracionPromedioRevestimiento"], PDO::PARAM_STR);
        $stmt->bindParam(":deflectores", $datos["deflectores"], PDO::PARAM_STR);
        $stmt->bindParam(":altureCaida", $datos["altureCaida"], PDO::PARAM_STR);
        $stmt->bindParam(":longitudImpacto", $datos["longitudImpacto"], PDO::PARAM_STR);
        $stmt->bindParam(":material", $datos["material"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloSobreCarga", $datos["anguloSobreCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":ataqueQuimicoTransportadora", $datos["ataqueQuimicoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":ataqueTemperaturaTransportadora", $datos["ataqueTemperaturaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":ataqueAceiteTransportadora", $datos["ataqueAceiteTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":ataqueImpactoTransportadora", $datos["ataqueImpactoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":capacidadTransportadora", $datos["capacidadTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":horasTrabajoPorDiaTransportadora", $datos["horasTrabajoPorDiaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":diasTrabajPorSemanaTransportadora", $datos["diasTrabajPorSemanaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":alimentacionCentradaTransportadora", $datos["alimentacionCentradaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":abrasividadTransportadora", $datos["abrasividadTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":porcentajeFinosTransportadora", $datos["porcentajeFinosTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":maxGranulometriaTransportadora", $datos["maxGranulometriaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":maxPesoTransportadora", $datos["maxPesoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":densidadTransportadora", $datos["densidadTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tempMaximaMaterialSobreBandaTransportadora", $datos["tempMaximaMaterialSobreBandaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tempPromedioMaterialSobreBandaTransportadora", $datos["tempPromedioMaterialSobreBandaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":fugaDeMaterialesEnLaColaDelChute", $datos["fugaDeMaterialesEnLaColaDelChute"], PDO::PARAM_STR);
        $stmt->bindParam(":fugaDeMaterialesPorLosCostados", $datos["fugaDeMaterialesPorLosCostados"], PDO::PARAM_STR);
        $stmt->bindParam(":fugaMateriales", $datos["fugaMateriales"], PDO::PARAM_STR);
        $stmt->bindParam(":cajaColaDeTolva", $datos["cajaColaDeTolva"], PDO::PARAM_STR);
        $stmt->bindParam(":fugaDeMaterialParticulaALaSalidaDelChute", $datos["fugaDeMaterialParticulaALaSalidaDelChute"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoChute", $datos["anchoChute"], PDO::PARAM_STR);
        $stmt->bindParam(":largoChute", $datos["largoChute"], PDO::PARAM_STR);
        $stmt->bindParam(":alturaChute", $datos["alturaChute"], PDO::PARAM_STR);
        $stmt->bindParam(":abrazadera", $datos["abrazadera"], PDO::PARAM_STR);
        $stmt->bindParam(":cauchoGuardabandas", $datos["cauchoGuardabandas"], PDO::PARAM_STR);
        $stmt->bindParam(":triSealMultiSeal", $datos["triSealMultiSeal"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorGuardaBandas", $datos["espesorGuardaBandas"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoGuardaBandas", $datos["anchoGuardaBandas"], PDO::PARAM_STR);
        $stmt->bindParam(":largoGuardaBandas", $datos["largoGuardaBandas"], PDO::PARAM_STR);
        $stmt->bindParam(":protectorGuardaBandas", $datos["protectorGuardaBandas"], PDO::PARAM_STR);
        $stmt->bindParam(":cortinaAntiPolvo1", $datos["cortinaAntiPolvo1"], PDO::PARAM_STR);
        $stmt->bindParam(":cortinaAntiPolvo2", $datos["cortinaAntiPolvo2"], PDO::PARAM_STR);
        $stmt->bindParam(":cortinaAntiPolvo3", $datos["cortinaAntiPolvo3"], PDO::PARAM_STR);
        $stmt->bindParam(":boquillasCanonesDeAire", $datos["boquillasCanonesDeAire"], PDO::PARAM_STR);
        $stmt->bindParam(":tempAmbienteMaxTransportadora", $datos["tempAmbienteMaxTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tempAmbienteMinTransportadora", $datos["tempAmbienteMinTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tieneRodillosImpacto", $datos["tieneRodillosImpacto"], PDO::PARAM_STR);
        $stmt->bindParam(":camaImpacto", $datos["camaImpacto"], PDO::PARAM_STR);
        $stmt->bindParam(":camaSellado", $datos["camaSellado"], PDO::PARAM_STR);
        $stmt->bindParam(":basculaPesaje", $datos["basculaPesaje"], PDO::PARAM_STR);
        $stmt->bindParam(":rodilloCarga", $datos["rodilloCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":rodilloImpacto", $datos["rodilloImpacto"], PDO::PARAM_STR);
        $stmt->bindParam(":basculaASGCO", $datos["basculaASGCO"], PDO::PARAM_STR);
        $stmt->bindParam(":barraImpacto", $datos["barraImpacto"], PDO::PARAM_STR);
        $stmt->bindParam(":barraDeslizamiento", $datos["barraDeslizamiento"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorUHMV", $datos["espesorUHMV"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoBarra", $datos["anchoBarra"], PDO::PARAM_STR);
        $stmt->bindParam(":largoBarra", $datos["largoBarra"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloAcanalamientoArtesa1", $datos["anguloAcanalamientoArtesa1"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloAcanalamientoArtesa2", $datos["anguloAcanalamientoArtesa2"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloAcanalamientoArtesa3", $datos["anguloAcanalamientoArtesa3"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloAcanalamientoArtesa1AntesPoleaMotriz", $datos["anguloAcanalamientoArtesa1AntesPoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloAcanalamientoArtesa2AntesPoleaMotriz", $datos["anguloAcanalamientoArtesa2AntesPoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloAcanalamientoArtesa3AntesPoleaMotriz", $datos["anguloAcanalamientoArtesa3AntesPoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":integridadSoportesRodilloImpacto", $datos["integridadSoportesRodilloImpacto"], PDO::PARAM_STR);
        $stmt->bindParam(":materialAtrapadoEntreCortinas", $datos["materialAtrapadoEntreCortinas"], PDO::PARAM_STR);
        $stmt->bindParam(":materialAtrapadoEntreGuardabandas", $datos["materialAtrapadoEntreGuardabandas"], PDO::PARAM_STR);
        $stmt->bindParam(":materialAtrapadoEnBanda", $datos["materialAtrapadoEnBanda"], PDO::PARAM_STR);
        $stmt->bindParam(":integridadSoportesCamaImpacto", $datos["integridadSoportesCamaImpacto"], PDO::PARAM_STR);
        $stmt->bindParam(":inclinacionZonaCargue", $datos["inclinacionZonaCargue"], PDO::PARAM_STR);
        $stmt->bindParam(":sistemaAlineacionCarga", $datos["sistemaAlineacionCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":cantidadSistemaAlineacionEnCarga", $datos["cantidadSistemaAlineacionEnCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":sistemasAlineacionCargaFuncionando", $datos["sistemasAlineacionCargaFuncionando"], PDO::PARAM_STR);
        $stmt->bindParam(":sistemaAlineacionEnRetorno", $datos["sistemaAlineacionEnRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":cantidadSistemaAlineacionEnRetorno", $datos["cantidadSistemaAlineacionEnRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":sistemasAlineacionRetornoFuncionando", $datos["sistemasAlineacionRetornoFuncionando"], PDO::PARAM_STR);
        $stmt->bindParam(":sistemaAlineacionRetornoPlano", $datos["sistemaAlineacionRetornoPlano"], PDO::PARAM_STR);
        $stmt->bindParam(":sistemaAlineacionArtesaCarga", $datos["sistemaAlineacionArtesaCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":sistemaAlineacionRetornoEnV", $datos["sistemaAlineacionRetornoEnV"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjeRodilloCentralCarga", $datos["largoEjeRodilloCentralCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjeRodilloCentralCarga", $datos["diametroEjeRodilloCentralCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":largoTuboRodilloCentralCarga", $datos["largoTuboRodilloCentralCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjeRodilloLateralCarga", $datos["largoEjeRodilloLateralCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjeRodilloLateralCarga", $datos["diametroEjeRodilloLateralCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroRodilloLateralCarga", $datos["diametroRodilloLateralCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":largoTuboRodilloLateralCarga", $datos["largoTuboRodilloLateralCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoRodilloCarga", $datos["tipoRodilloCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaEntreArtesasCarga", $datos["distanciaEntreArtesasCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoInternoChasisRodilloCarga", $datos["anchoInternoChasisRodilloCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoExternoChasisRodilloCarga", $datos["anchoExternoChasisRodilloCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloAcanalamientoArtesaCArga", $datos["anguloAcanalamientoArtesaCArga"], PDO::PARAM_STR);
        $stmt->bindParam(":detalleRodilloCentralCarga", $datos["detalleRodilloCentralCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":detalleRodilloLateralCarg", $datos["detalleRodilloLateralCarg"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroPoleaMotrizTransportadora", $datos["diametroPoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPoleaMotrizTransportadora", $datos["anchoPoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoPoleaMotrizTransportadora", $datos["tipoPoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjePoleaMotrizTransportadora", $datos["largoEjePoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjeMotrizTransportadora", $datos["diametroEjeMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":icobandasCentraEnPoleaMotrizTransportadora", $datos["icobandasCentraEnPoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloAmarrePoleaMotrizTransportadora", $datos["anguloAmarrePoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRevestimientoPoleaMotrizTransportadora", $datos["estadoRevestimientoPoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoTransicionPoleaMotrizTransportadora", $datos["tipoTransicionPoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaTransicionPoleaMotrizTransportadora", $datos["distanciaTransicionPoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":potenciaMotorTransportadora", $datos["potenciaMotorTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaPoleaMotrizTransportadora", $datos["guardaPoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoEstructura", $datos["anchoEstructura"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoTrayectoCarga", $datos["anchoTrayectoCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":pasarelaRespectoAvanceBanda", $datos["pasarelaRespectoAvanceBanda"], PDO::PARAM_STR);
        $stmt->bindParam(":materialAlimenticioTransportadora", $datos["materialAlimenticioTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":materialAcidoTransportadora", $datos["materialAcidoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":materialTempEntre80y150Transportadora", $datos["materialTempEntre80y150Transportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":materialSecoTransportadora", $datos["materialSecoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":materialHumedoTransportadora", $datos["materialHumedoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":materialAbrasivoFinoTransportadora", $datos["materialAbrasivoFinoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":materialPegajosoTransportadora", $datos["materialPegajosoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":materialGrasosoAceitosoTransportadora", $datos["materialGrasosoAceitosoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":marcaLimpiadorPrimario", $datos["marcaLimpiadorPrimario"], PDO::PARAM_STR);
        $stmt->bindParam(":referenciaLimpiadorPrimario", $datos["referenciaLimpiadorPrimario"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoCuchillaLimpiadorPrimario", $datos["anchoCuchillaLimpiadorPrimario"], PDO::PARAM_STR);
        $stmt->bindParam(":altoCuchillaLimpiadorPrimario", $datos["altoCuchillaLimpiadorPrimario"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoCuchillaLimpiadorPrimario", $datos["estadoCuchillaLimpiadorPrimario"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoTensorLimpiadorPrimario", $datos["estadoTensorLimpiadorPrimario"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoTuboLimpiadorPrimario", $datos["estadoTuboLimpiadorPrimario"], PDO::PARAM_STR);
        $stmt->bindParam(":frecuenciaRevisionCuchilla", $datos["frecuenciaRevisionCuchilla"], PDO::PARAM_STR);
        $stmt->bindParam(":cuchillaEnContactoConBanda", $datos["cuchillaEnContactoConBanda"], PDO::PARAM_STR);
        $stmt->bindParam(":marcaLimpiadorSecundario", $datos["marcaLimpiadorSecundario"], PDO::PARAM_STR);
        $stmt->bindParam(":referenciaLimpiadorSecundario", $datos["referenciaLimpiadorSecundario"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoCuchillaLimpiadorSecundario", $datos["anchoCuchillaLimpiadorSecundario"], PDO::PARAM_STR);
        $stmt->bindParam(":altoCuchillaLimpiadorSecundario", $datos["altoCuchillaLimpiadorSecundario"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoCuchillaLimpiadorSecundario", $datos["estadoCuchillaLimpiadorSecundario"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoTensorLimpiadorSecundario", $datos["estadoTensorLimpiadorSecundario"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoTuboLimpiadorSecundario", $datos["estadoTuboLimpiadorSecundario"], PDO::PARAM_STR);
        $stmt->bindParam(":frecuenciaRevisionCuchilla1", $datos["frecuenciaRevisionCuchilla1"], PDO::PARAM_STR);
        $stmt->bindParam(":cuchillaEnContactoConBanda1", $datos["cuchillaEnContactoConBanda1"], PDO::PARAM_STR);
        $stmt->bindParam(":sistemaDribbleChute", $datos["sistemaDribbleChute"], PDO::PARAM_STR);
        $stmt->bindParam(":marcaLimpiadorTerciario", $datos["marcaLimpiadorTerciario"], PDO::PARAM_STR);
        $stmt->bindParam(":referenciaLimpiadorTerciario", $datos["referenciaLimpiadorTerciario"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoCuchillaLimpiadorTerciario", $datos["anchoCuchillaLimpiadorTerciario"], PDO::PARAM_STR);
        $stmt->bindParam(":altoCuchillaLimpiadorTerciario", $datos["altoCuchillaLimpiadorTerciario"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoCuchillaLimpiadorTerciario", $datos["estadoCuchillaLimpiadorTerciario"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoTensorLimpiadorTerciario", $datos["estadoTensorLimpiadorTerciario"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoTuboLimpiadorTerciario", $datos["estadoTuboLimpiadorTerciario"], PDO::PARAM_STR);
        $stmt->bindParam(":frecuenciaRevisionCuchilla2", $datos["frecuenciaRevisionCuchilla2"], PDO::PARAM_STR);
        $stmt->bindParam(":cuchillaEnContactoConBanda2", $datos["cuchillaEnContactoConBanda2"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRodilloRetorno", $datos["estadoRodilloRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjeRodilloRetorno", $datos["largoEjeRodilloRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjeRodilloRetorno", $datos["diametroEjeRodilloRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroRodilloRetorno", $datos["diametroRodilloRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":largoTuboRodilloRetorno", $datos["largoTuboRodilloRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoRodilloRetorno", $datos["tipoRodilloRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaEntreRodillosRetorno", $datos["distanciaEntreRodillosRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoInternoChasisRetorno", $datos["anchoInternoChasisRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoExternoChasisRetorno", $datos["anchoExternoChasisRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":detalleRodilloRetorno", $datos["detalleRodilloRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroPoleaAmarrePoleaMotriz", $datos["diametroPoleaAmarrePoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPoleaAmarrePoleaMotriz", $datos["anchoPoleaAmarrePoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoPoleaAmarrePoleaMotriz", $datos["tipoPoleaAmarrePoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjePoleaAmarrePoleaMotriz", $datos["largoEjePoleaAmarrePoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjePoleaAmarrePoleaMotriz", $datos["diametroEjePoleaAmarrePoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":icobandasCentradaPoleaAmarrePoleaMotriz", $datos["icobandasCentradaPoleaAmarrePoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRevestimientoPoleaAmarrePoleaMotriz", $datos["estadoRevestimientoPoleaAmarrePoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":dimetroPoleaAmarrePoleaCola", $datos["dimetroPoleaAmarrePoleaCola"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPoleaAmarrePoleaCola", $datos["anchoPoleaAmarrePoleaCola"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjePoleaAmarrePoleaCola", $datos["largoEjePoleaAmarrePoleaCola"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoPoleaAmarrePoleaCola", $datos["tipoPoleaAmarrePoleaCola"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjePoleaAmarrePoleaCola", $datos["diametroEjePoleaAmarrePoleaCola"], PDO::PARAM_STR);
        $stmt->bindParam(":icobandasCentradaPoleaAmarrePoleaCola", $datos["icobandasCentradaPoleaAmarrePoleaCola"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRevestimientoPoleaAmarrePoleaCola", $datos["estadoRevestimientoPoleaAmarrePoleaCola"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroPoleaTensora", $datos["diametroPoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPoleaTensora", $datos["anchoPoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoPoleaTensora", $datos["tipoPoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjePoleaTensora", $datos["largoEjePoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjePoleaTensora", $datos["diametroEjePoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":icobandasCentradaEnPoleaTensora", $datos["icobandasCentradaEnPoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":recorridoPoleaTensora", $datos["recorridoPoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRevestimientoPoleaTensora", $datos["estadoRevestimientoPoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoTransicionPoleaTensora", $datos["tipoTransicionPoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaTransicionPoleaColaTensora", $datos["distanciaTransicionPoleaColaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":potenciaMotorPoleaTensora", $datos["potenciaMotorPoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaPoleaTensora", $datos["guardaPoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":puertasInspeccion", $datos["puertasInspeccion"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaRodilloRetornoPlano", $datos["guardaRodilloRetornoPlano"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaTruTrainer", $datos["guardaTruTrainer"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaPoleaDeflectora", $datos["guardaPoleaDeflectora"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaZonaDeTransito", $datos["guardaZonaDeTransito"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaMotores", $datos["guardaMotores"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaCadenas", $datos["guardaCadenas"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaCorreas", $datos["guardaCorreas"], PDO::PARAM_STR);
        $stmt->bindParam(":interruptoresDeSeguridad", $datos["interruptoresDeSeguridad"], PDO::PARAM_STR);
        $stmt->bindParam(":sirenasDeSeguridad", $datos["sirenasDeSeguridad"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaRodilloRetornoV", $datos["guardaRodilloRetornoV"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroRodilloCentralCarga", $datos["diametroRodilloCentralCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoRodilloImpacto", $datos["tipoRodilloImpacto"], PDO::PARAM_STR);
        $stmt->bindParam(":integridadSoporteCamaSellado", $datos["integridadSoporteCamaSellado"], PDO::PARAM_STR);
        $stmt->bindParam(":ataqueAbrasivoTransportadora", $datos["ataqueAbrasivoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":observacionRegistroTransportadora", $datos["observacionRegistroTransportadora"], PDO::PARAM_STR);

        if ($stmt->execute()) {
            return "ok";
        } else {
            return "ERROR AL CREAR REGISTRO BANDA TRANSPORTADORA";
        }

        $stmt->close();
        $stmt = null;
    }

    public static function mdlActualizarTransportadora($tabla, $datos)
    {
        // "INSERT INTO clientes (nit, nameunido) VALUES (:nit, :nameunido)"

        $stmt = Conexion::getConexionLocal()->prepare(" UPDATE bandaTransportadora set marcaBandaTransportadora=:marcaBandaTransportadora, anchoBandaTransportadora=:anchoBandaTransportadora, noLonasBandaTransportadora=:noLonasBandaTransportadora, tipoLonaBandaTransportadora=:tipoLonaBandaTransportadora, espesorTotalBandaTransportadora=:espesorTotalBandaTransportadora, espesorCubiertaSuperiorTransportadora=:espesorCubiertaSuperiorTransportadora,
                         espesorCojinTransportadora=:espesorCojinTransportadora, espesorCubiertaInferiorTransportadora=:espesorCubiertaInferiorTransportadora, tipoCubiertaTransportadora=:tipoCubiertaTransportadora, tipoEmpalmeTransportadora=:tipoEmpalmeTransportadora, estadoEmpalmeTransportadora=:estadoEmpalmeTransportadora, distanciaEntrePoleasBandaHorizontal=:distanciaEntrePoleasBandaHorizontal,
                         inclinacionBandaHorizontal=:inclinacionBandaHorizontal, recorridoUtilTensorBandaHorizontal=:recorridoUtilTensorBandaHorizontal, longitudSinfinBandaHorizontal=:longitudSinfinBandaHorizontal, resistenciaRoturaLonaTransportadora=:resistenciaRoturaLonaTransportadora, localizacionTensorTransportadora=:localizacionTensorTransportadora, bandaReversible=:bandaReversible, bandaDeArrastre=:bandaDeArrastre,
                         velocidadBandaHorizontal=:velocidadBandaHorizontal, marcaBandaHorizontalAnterior=:marcaBandaHorizontalAnterior, anchoBandaHorizontalAnterior=:anchoBandaHorizontalAnterior, noLonasBandaHorizontalAnterior=:noLonasBandaHorizontalAnterior, tipoLonaBandaHorizontalAnterior=:tipoLonaBandaHorizontalAnterior, espesorTotalBandaHorizontalAnterior=:espesorTotalBandaHorizontalAnterior,
                         espesorCubiertaSuperiorBandaHorizontalAnterior=:espesorCubiertaSuperiorBandaHorizontalAnterior, espesorCubiertaInferiorBandaHorizontalAnterior=:espesorCubiertaInferiorBandaHorizontalAnterior, espesorCojinBandaHorizontalAnterior=:espesorCojinBandaHorizontalAnterior, tipoEmpalmeBandaHorizontalAnterior=:tipoEmpalmeBandaHorizontalAnterior, resistenciaRoturaLonaBandaHorizontalAnterior=:resistenciaRoturaLonaBandaHorizontalAnterior,
                         tipoCubiertaBandaHorizontalAnterior=:tipoCubiertaBandaHorizontalAnterior, tonsTransportadasBandaHoizontalAnterior=:tonsTransportadasBandaHoizontalAnterior, causaFallaCambioBandaHorizontal=:causaFallaCambioBandaHorizontal, diametroPoleaColaTransportadora=:diametroPoleaColaTransportadora, anchoPoleaColaTransportadora=:anchoPoleaColaTransportadora, tipoPoleaColaTransportadora=:tipoPoleaColaTransportadora,
                         largoEjePoleaColaTransportadora=:largoEjePoleaColaTransportadora, diametroEjePoleaColaHorizontal=:diametroEjePoleaColaHorizontal, icobandasCentradaPoleaColaTransportadora=:icobandasCentradaPoleaColaTransportadora, anguloAmarrePoleaColaTransportadora=:anguloAmarrePoleaColaTransportadora, estadoRvtoPoleaColaTransportadora=:estadoRvtoPoleaColaTransportadora,
                         tipoTransicionPoleaColaTransportadora=:tipoTransicionPoleaColaTransportadora, distanciaTransicionPoleaColaTransportadora=:distanciaTransicionPoleaColaTransportadora, longitudTensorTornilloPoleaColaTransportadora=:longitudTensorTornilloPoleaColaTransportadora, longitudRecorridoContrapesaPoleaColaTransportadora=:longitudRecorridoContrapesaPoleaColaTransportadora, guardaPoleaColaTransportadora=:guardaPoleaColaTransportadora,
                         hayDesviador=:hayDesviador, elDesviadorBascula=:elDesviadorBascula, presionUniformeALoAnchoDeLaBanda=:presionUniformeALoAnchoDeLaBanda, cauchoVPlow=:cauchoVPlow, anchoVPlow=:anchoVPlow, espesorVPlow=:espesorVPlow, tipoRevestimientoTolvaCarga=:tipoRevestimientoTolvaCarga, estadoRevestimientoTolvaCarga=:estadoRevestimientoTolvaCarga, duracionPromedioRevestimiento=:duracionPromedioRevestimiento,
                         deflectores=:deflectores, altureCaida=:altureCaida, longitudImpacto=:longitudImpacto, material=:material, anguloSobreCarga=:anguloSobreCarga, ataqueQuimicoTransportadora=:ataqueQuimicoTransportadora, ataqueTemperaturaTransportadora=:ataqueTemperaturaTransportadora, ataqueAceiteTransportadora=:ataqueAceiteTransportadora, ataqueImpactoTransportadora=:ataqueImpactoTransportadora, capacidadTransportadora=:capacidadTransportadora,
                         horasTrabajoPorDiaTransportadora=:horasTrabajoPorDiaTransportadora, diasTrabajPorSemanaTransportadora=:diasTrabajPorSemanaTransportadora, alimentacionCentradaTransportadora=:alimentacionCentradaTransportadora, abrasividadTransportadora=:abrasividadTransportadora, porcentajeFinosTransportadora=:porcentajeFinosTransportadora, maxGranulometriaTransportadora=:maxGranulometriaTransportadora,
                         maxPesoTransportadora=:maxPesoTransportadora, densidadTransportadora=:densidadTransportadora, tempMaximaMaterialSobreBandaTransportadora=:tempMaximaMaterialSobreBandaTransportadora, tempPromedioMaterialSobreBandaTransportadora=:tempPromedioMaterialSobreBandaTransportadora, fugaDeMaterialesEnLaColaDelChute=:fugaDeMaterialesEnLaColaDelChute, fugaDeMaterialesPorLosCostados=:fugaDeMaterialesPorLosCostados,
                         fugaMateriales=:fugaMateriales, cajaColaDeTolva=:cajaColaDeTolva, fugaDeMaterialParticulaALaSalidaDelChute=:fugaDeMaterialParticulaALaSalidaDelChute, anchoChute=:anchoChute, largoChute=:largoChute, alturaChute=:alturaChute, abrazadera=:abrazadera, cauchoGuardabandas=:cauchoGuardabandas, triSealMultiSeal=:triSealMultiSeal, espesorGuardaBandas=:espesorGuardaBandas, anchoGuardaBandas=:anchoGuardaBandas,
                         largoGuardaBandas=:largoGuardaBandas, protectorGuardaBandas=:protectorGuardaBandas, cortinaAntiPolvo1=:cortinaAntiPolvo1, cortinaAntiPolvo2=:cortinaAntiPolvo2, cortinaAntiPolvo3=:cortinaAntiPolvo3, boquillasCanonesDeAire=:boquillasCanonesDeAire, tempAmbienteMaxTransportadora=:tempAmbienteMaxTransportadora, tempAmbienteMinTransportadora=:tempAmbienteMinTransportadora, tieneRodillosImpacto=:tieneRodillosImpacto,
                         camaImpacto=:camaImpacto, camaSellado=:camaSellado, basculaPesaje=:basculaPesaje, rodilloCarga=:rodilloCarga, rodilloImpacto=:rodilloImpacto, basculaASGCO=:basculaASGCO, barraImpacto=:barraImpacto, barraDeslizamiento=:barraDeslizamiento, espesorUHMV=:espesorUHMV, anchoBarra=:anchoBarra, largoBarra=:largoBarra, anguloAcanalamientoArtesa1=:anguloAcanalamientoArtesa1, anguloAcanalamientoArtesa2=:anguloAcanalamientoArtesa2,
                         anguloAcanalamientoArtesa3=:anguloAcanalamientoArtesa3, anguloAcanalamientoArtesa1AntesPoleaMotriz=:anguloAcanalamientoArtesa1AntesPoleaMotriz, anguloAcanalamientoArtesa2AntesPoleaMotriz=:anguloAcanalamientoArtesa2AntesPoleaMotriz, anguloAcanalamientoArtesa3AntesPoleaMotriz=:anguloAcanalamientoArtesa3AntesPoleaMotriz, integridadSoportesRodilloImpacto=:integridadSoportesRodilloImpacto,
                         materialAtrapadoEntreCortinas=:materialAtrapadoEntreCortinas, materialAtrapadoEntreGuardabandas=:materialAtrapadoEntreGuardabandas, materialAtrapadoEnBanda=:materialAtrapadoEnBanda, integridadSoportesCamaImpacto=:integridadSoportesCamaImpacto, inclinacionZonaCargue=:inclinacionZonaCargue, sistemaAlineacionCarga=:sistemaAlineacionCarga, cantidadSistemaAlineacionEnCarga=:cantidadSistemaAlineacionEnCarga,
                         sistemasAlineacionCargaFuncionando=:sistemasAlineacionCargaFuncionando, sistemaAlineacionEnRetorno=:sistemaAlineacionEnRetorno, cantidadSistemaAlineacionEnRetorno=:cantidadSistemaAlineacionEnRetorno, sistemasAlineacionRetornoFuncionando=:sistemasAlineacionRetornoFuncionando, sistemaAlineacionRetornoPlano=:sistemaAlineacionRetornoPlano, sistemaAlineacionArtesaCarga=:sistemaAlineacionArtesaCarga,
                         sistemaAlineacionRetornoEnV=:sistemaAlineacionRetornoEnV, largoEjeRodilloCentralCarga=:largoEjeRodilloCentralCarga, diametroEjeRodilloCentralCarga=:diametroEjeRodilloCentralCarga, largoTuboRodilloCentralCarga=:largoTuboRodilloCentralCarga, largoEjeRodilloLateralCarga=:largoEjeRodilloLateralCarga, diametroEjeRodilloLateralCarga=:diametroEjeRodilloLateralCarga, diametroRodilloLateralCarga=:diametroRodilloLateralCarga,
                         largoTuboRodilloLateralCarga=:largoTuboRodilloLateralCarga, tipoRodilloCarga=:tipoRodilloCarga, distanciaEntreArtesasCarga=:distanciaEntreArtesasCarga, anchoInternoChasisRodilloCarga=:anchoInternoChasisRodilloCarga, anchoExternoChasisRodilloCarga=:anchoExternoChasisRodilloCarga, anguloAcanalamientoArtesaCArga=:anguloAcanalamientoArtesaCArga, detalleRodilloCentralCarga=:detalleRodilloCentralCarga,
                         detalleRodilloLateralCarg=:detalleRodilloLateralCarg, diametroPoleaMotrizTransportadora=:diametroPoleaMotrizTransportadora, anchoPoleaMotrizTransportadora=:anchoPoleaMotrizTransportadora, tipoPoleaMotrizTransportadora=:tipoPoleaMotrizTransportadora, largoEjePoleaMotrizTransportadora=:largoEjePoleaMotrizTransportadora, diametroEjeMotrizTransportadora=:diametroEjeMotrizTransportadora,
                         icobandasCentraEnPoleaMotrizTransportadora=:icobandasCentraEnPoleaMotrizTransportadora, anguloAmarrePoleaMotrizTransportadora=:anguloAmarrePoleaMotrizTransportadora, estadoRevestimientoPoleaMotrizTransportadora=:estadoRevestimientoPoleaMotrizTransportadora, tipoTransicionPoleaMotrizTransportadora=:tipoTransicionPoleaMotrizTransportadora,
                         distanciaTransicionPoleaMotrizTransportadora=:distanciaTransicionPoleaMotrizTransportadora, potenciaMotorTransportadora=:potenciaMotorTransportadora, guardaPoleaMotrizTransportadora=:guardaPoleaMotrizTransportadora, anchoEstructura=:anchoEstructura, anchoTrayectoCarga=:anchoTrayectoCarga, pasarelaRespectoAvanceBanda=:pasarelaRespectoAvanceBanda, materialAlimenticioTransportadora=:materialAlimenticioTransportadora,
                         materialAcidoTransportadora=:materialAcidoTransportadora, materialTempEntre80y150Transportadora=:materialTempEntre80y150Transportadora, materialSecoTransportadora=:materialSecoTransportadora, materialHumedoTransportadora=:materialHumedoTransportadora, materialAbrasivoFinoTransportadora=:materialAbrasivoFinoTransportadora, materialPegajosoTransportadora=:materialPegajosoTransportadora,
                         materialGrasosoAceitosoTransportadora=:materialGrasosoAceitosoTransportadora, marcaLimpiadorPrimario=:marcaLimpiadorPrimario, referenciaLimpiadorPrimario=:referenciaLimpiadorPrimario, anchoCuchillaLimpiadorPrimario=:anchoCuchillaLimpiadorPrimario, altoCuchillaLimpiadorPrimario=:altoCuchillaLimpiadorPrimario, estadoCuchillaLimpiadorPrimario=:estadoCuchillaLimpiadorPrimario,
                         estadoTensorLimpiadorPrimario=:estadoTensorLimpiadorPrimario, estadoTuboLimpiadorPrimario=:estadoTuboLimpiadorPrimario, frecuenciaRevisionCuchilla=:frecuenciaRevisionCuchilla, cuchillaEnContactoConBanda=:cuchillaEnContactoConBanda, marcaLimpiadorSecundario=:marcaLimpiadorSecundario, referenciaLimpiadorSecundario=:referenciaLimpiadorSecundario, anchoCuchillaLimpiadorSecundario=:anchoCuchillaLimpiadorSecundario,
                         altoCuchillaLimpiadorSecundario=:altoCuchillaLimpiadorSecundario, estadoCuchillaLimpiadorSecundario=:estadoCuchillaLimpiadorSecundario, estadoTensorLimpiadorSecundario=:estadoTensorLimpiadorSecundario, estadoTuboLimpiadorSecundario=:estadoTuboLimpiadorSecundario, frecuenciaRevisionCuchilla1=:frecuenciaRevisionCuchilla1, cuchillaEnContactoConBanda1=:cuchillaEnContactoConBanda1, sistemaDribbleChute=:sistemaDribbleChute,
                         marcaLimpiadorTerciario=:marcaLimpiadorTerciario, referenciaLimpiadorTerciario=:referenciaLimpiadorTerciario, anchoCuchillaLimpiadorTerciario=:anchoCuchillaLimpiadorTerciario, altoCuchillaLimpiadorTerciario=:altoCuchillaLimpiadorTerciario, estadoCuchillaLimpiadorTerciario=:estadoCuchillaLimpiadorTerciario, estadoTensorLimpiadorTerciario=:estadoTensorLimpiadorTerciario, estadoTuboLimpiadorTerciario=:estadoTuboLimpiadorTerciario,
                         frecuenciaRevisionCuchilla2=:frecuenciaRevisionCuchilla2, cuchillaEnContactoConBanda2=:cuchillaEnContactoConBanda2, estadoRodilloRetorno=:estadoRodilloRetorno, largoEjeRodilloRetorno=:largoEjeRodilloRetorno, diametroEjeRodilloRetorno=:diametroEjeRodilloRetorno, diametroRodilloRetorno=:diametroRodilloRetorno, largoTuboRodilloRetorno=:largoTuboRodilloRetorno, tipoRodilloRetorno=:tipoRodilloRetorno,
                         distanciaEntreRodillosRetorno=:distanciaEntreRodillosRetorno, anchoInternoChasisRetorno=:anchoInternoChasisRetorno, anchoExternoChasisRetorno=:anchoExternoChasisRetorno, detalleRodilloRetorno=:detalleRodilloRetorno, diametroPoleaAmarrePoleaMotriz=:diametroPoleaAmarrePoleaMotriz, anchoPoleaAmarrePoleaMotriz=:anchoPoleaAmarrePoleaMotriz, tipoPoleaAmarrePoleaMotriz=:tipoPoleaAmarrePoleaMotriz,
                         largoEjePoleaAmarrePoleaMotriz=:largoEjePoleaAmarrePoleaMotriz, diametroEjePoleaAmarrePoleaMotriz=:diametroEjePoleaAmarrePoleaMotriz, icobandasCentradaPoleaAmarrePoleaMotriz=:icobandasCentradaPoleaAmarrePoleaMotriz, estadoRevestimientoPoleaAmarrePoleaMotriz=:estadoRevestimientoPoleaAmarrePoleaMotriz, dimetroPoleaAmarrePoleaCola=:dimetroPoleaAmarrePoleaCola, anchoPoleaAmarrePoleaCola=:anchoPoleaAmarrePoleaCola,
                         largoEjePoleaAmarrePoleaCola=:largoEjePoleaAmarrePoleaCola, tipoPoleaAmarrePoleaCola=:tipoPoleaAmarrePoleaCola, diametroEjePoleaAmarrePoleaCola=:diametroEjePoleaAmarrePoleaCola, icobandasCentradaPoleaAmarrePoleaCola=:icobandasCentradaPoleaAmarrePoleaCola, estadoRevestimientoPoleaAmarrePoleaCola=:estadoRevestimientoPoleaAmarrePoleaCola, diametroPoleaTensora=:diametroPoleaTensora,
                         anchoPoleaTensora=:anchoPoleaTensora, tipoPoleaTensora=:tipoPoleaTensora, largoEjePoleaTensora=:largoEjePoleaTensora, diametroEjePoleaTensora=:diametroEjePoleaTensora, icobandasCentradaEnPoleaTensora=:icobandasCentradaEnPoleaTensora, recorridoPoleaTensora=:recorridoPoleaTensora, estadoRevestimientoPoleaTensora=:estadoRevestimientoPoleaTensora, tipoTransicionPoleaTensora=:tipoTransicionPoleaTensora,
                         distanciaTransicionPoleaColaTensora=:distanciaTransicionPoleaColaTensora, potenciaMotorPoleaTensora=:potenciaMotorPoleaTensora, guardaPoleaTensora=:guardaPoleaTensora, puertasInspeccion=:puertasInspeccion, guardaRodilloRetornoPlano=:guardaRodilloRetornoPlano, guardaTruTrainer=:guardaTruTrainer, guardaPoleaDeflectora=:guardaPoleaDeflectora, guardaZonaDeTransito=:guardaZonaDeTransito, guardaMotores=:guardaMotores,
                         guardaCadenas=:guardaCadenas, guardaCorreas=:guardaCorreas, interruptoresDeSeguridad=:interruptoresDeSeguridad, sirenasDeSeguridad=:sirenasDeSeguridad, guardaRodilloRetornoV=:guardaRodilloRetornoV, diametroRodilloCentralCarga=:diametroRodilloCentralCarga, tipoRodilloImpacto=:tipoRodilloImpacto, integridadSoporteCamaSellado=:integridadSoporteCamaSellado, ataqueAbrasivoTransportadora=:ataqueAbrasivoTransportadora,
                         observacionRegistroTransportadora=:observacionRegistroTransportadora where idRegistro=:idRegistro");

        $stmt->bindParam(":idRegistro", $datos["idRegistro"], PDO::PARAM_STR);
        $stmt->bindParam(":marcaBandaTransportadora", $datos["marcaBandaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoBandaTransportadora", $datos["anchoBandaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":noLonasBandaTransportadora", $datos["noLonasBandaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoLonaBandaTransportadora", $datos["tipoLonaBandaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorTotalBandaTransportadora", $datos["espesorTotalBandaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCubiertaSuperiorTransportadora", $datos["espesorCubiertaSuperiorTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCojinTransportadora", $datos["espesorCojinTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCubiertaInferiorTransportadora", $datos["espesorCubiertaInferiorTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoCubiertaTransportadora", $datos["tipoCubiertaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoEmpalmeTransportadora", $datos["tipoEmpalmeTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoEmpalmeTransportadora", $datos["estadoEmpalmeTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaEntrePoleasBandaHorizontal", $datos["distanciaEntrePoleasBandaHorizontal"], PDO::PARAM_STR);
        $stmt->bindParam(":inclinacionBandaHorizontal", $datos["inclinacionBandaHorizontal"], PDO::PARAM_STR);
        $stmt->bindParam(":recorridoUtilTensorBandaHorizontal", $datos["recorridoUtilTensorBandaHorizontal"], PDO::PARAM_STR);
        $stmt->bindParam(":longitudSinfinBandaHorizontal", $datos["longitudSinfinBandaHorizontal"], PDO::PARAM_STR);
        $stmt->bindParam(":resistenciaRoturaLonaTransportadora", $datos["resistenciaRoturaLonaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":localizacionTensorTransportadora", $datos["localizacionTensorTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":bandaReversible", $datos["bandaReversible"], PDO::PARAM_STR);
        $stmt->bindParam(":bandaDeArrastre", $datos["bandaDeArrastre"], PDO::PARAM_STR);
        $stmt->bindParam(":velocidadBandaHorizontal", $datos["velocidadBandaHorizontal"], PDO::PARAM_STR);
        $stmt->bindParam(":marcaBandaHorizontalAnterior", $datos["marcaBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoBandaHorizontalAnterior", $datos["anchoBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":noLonasBandaHorizontalAnterior", $datos["noLonasBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoLonaBandaHorizontalAnterior", $datos["tipoLonaBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorTotalBandaHorizontalAnterior", $datos["espesorTotalBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCubiertaSuperiorBandaHorizontalAnterior", $datos["espesorCubiertaSuperiorBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCubiertaInferiorBandaHorizontalAnterior", $datos["espesorCubiertaInferiorBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorCojinBandaHorizontalAnterior", $datos["espesorCojinBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoEmpalmeBandaHorizontalAnterior", $datos["tipoEmpalmeBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":resistenciaRoturaLonaBandaHorizontalAnterior", $datos["resistenciaRoturaLonaBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoCubiertaBandaHorizontalAnterior", $datos["tipoCubiertaBandaHorizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":tonsTransportadasBandaHoizontalAnterior", $datos["tonsTransportadasBandaHoizontalAnterior"], PDO::PARAM_STR);
        $stmt->bindParam(":causaFallaCambioBandaHorizontal", $datos["causaFallaCambioBandaHorizontal"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroPoleaColaTransportadora", $datos["diametroPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPoleaColaTransportadora", $datos["anchoPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoPoleaColaTransportadora", $datos["tipoPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjePoleaColaTransportadora", $datos["largoEjePoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjePoleaColaHorizontal", $datos["diametroEjePoleaColaHorizontal"], PDO::PARAM_STR);
        $stmt->bindParam(":icobandasCentradaPoleaColaTransportadora", $datos["icobandasCentradaPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloAmarrePoleaColaTransportadora", $datos["anguloAmarrePoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRvtoPoleaColaTransportadora", $datos["estadoRvtoPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoTransicionPoleaColaTransportadora", $datos["tipoTransicionPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaTransicionPoleaColaTransportadora", $datos["distanciaTransicionPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":longitudTensorTornilloPoleaColaTransportadora", $datos["longitudTensorTornilloPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":longitudRecorridoContrapesaPoleaColaTransportadora", $datos["longitudRecorridoContrapesaPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaPoleaColaTransportadora", $datos["guardaPoleaColaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":hayDesviador", $datos["hayDesviador"], PDO::PARAM_STR);
        $stmt->bindParam(":elDesviadorBascula", $datos["elDesviadorBascula"], PDO::PARAM_STR);
        $stmt->bindParam(":presionUniformeALoAnchoDeLaBanda", $datos["presionUniformeALoAnchoDeLaBanda"], PDO::PARAM_STR);
        $stmt->bindParam(":cauchoVPlow", $datos["cauchoVPlow"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoVPlow", $datos["anchoVPlow"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorVPlow", $datos["espesorVPlow"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoRevestimientoTolvaCarga", $datos["tipoRevestimientoTolvaCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRevestimientoTolvaCarga", $datos["estadoRevestimientoTolvaCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":duracionPromedioRevestimiento", $datos["duracionPromedioRevestimiento"], PDO::PARAM_STR);
        $stmt->bindParam(":deflectores", $datos["deflectores"], PDO::PARAM_STR);
        $stmt->bindParam(":altureCaida", $datos["altureCaida"], PDO::PARAM_STR);
        $stmt->bindParam(":longitudImpacto", $datos["longitudImpacto"], PDO::PARAM_STR);
        $stmt->bindParam(":material", $datos["material"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloSobreCarga", $datos["anguloSobreCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":ataqueQuimicoTransportadora", $datos["ataqueQuimicoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":ataqueTemperaturaTransportadora", $datos["ataqueTemperaturaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":ataqueAceiteTransportadora", $datos["ataqueAceiteTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":ataqueImpactoTransportadora", $datos["ataqueImpactoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":capacidadTransportadora", $datos["capacidadTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":horasTrabajoPorDiaTransportadora", $datos["horasTrabajoPorDiaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":diasTrabajPorSemanaTransportadora", $datos["diasTrabajPorSemanaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":alimentacionCentradaTransportadora", $datos["alimentacionCentradaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":abrasividadTransportadora", $datos["abrasividadTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":porcentajeFinosTransportadora", $datos["porcentajeFinosTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":maxGranulometriaTransportadora", $datos["maxGranulometriaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":maxPesoTransportadora", $datos["maxPesoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":densidadTransportadora", $datos["densidadTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tempMaximaMaterialSobreBandaTransportadora", $datos["tempMaximaMaterialSobreBandaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tempPromedioMaterialSobreBandaTransportadora", $datos["tempPromedioMaterialSobreBandaTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":fugaDeMaterialesEnLaColaDelChute", $datos["fugaDeMaterialesEnLaColaDelChute"], PDO::PARAM_STR);
        $stmt->bindParam(":fugaDeMaterialesPorLosCostados", $datos["fugaDeMaterialesPorLosCostados"], PDO::PARAM_STR);
        $stmt->bindParam(":fugaMateriales", $datos["fugaMateriales"], PDO::PARAM_STR);
        $stmt->bindParam(":cajaColaDeTolva", $datos["cajaColaDeTolva"], PDO::PARAM_STR);
        $stmt->bindParam(":fugaDeMaterialParticulaALaSalidaDelChute", $datos["fugaDeMaterialParticulaALaSalidaDelChute"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoChute", $datos["anchoChute"], PDO::PARAM_STR);
        $stmt->bindParam(":largoChute", $datos["largoChute"], PDO::PARAM_STR);
        $stmt->bindParam(":alturaChute", $datos["alturaChute"], PDO::PARAM_STR);
        $stmt->bindParam(":abrazadera", $datos["abrazadera"], PDO::PARAM_STR);
        $stmt->bindParam(":cauchoGuardabandas", $datos["cauchoGuardabandas"], PDO::PARAM_STR);
        $stmt->bindParam(":triSealMultiSeal", $datos["triSealMultiSeal"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorGuardaBandas", $datos["espesorGuardaBandas"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoGuardaBandas", $datos["anchoGuardaBandas"], PDO::PARAM_STR);
        $stmt->bindParam(":largoGuardaBandas", $datos["largoGuardaBandas"], PDO::PARAM_STR);
        $stmt->bindParam(":protectorGuardaBandas", $datos["protectorGuardaBandas"], PDO::PARAM_STR);
        $stmt->bindParam(":cortinaAntiPolvo1", $datos["cortinaAntiPolvo1"], PDO::PARAM_STR);
        $stmt->bindParam(":cortinaAntiPolvo2", $datos["cortinaAntiPolvo2"], PDO::PARAM_STR);
        $stmt->bindParam(":cortinaAntiPolvo3", $datos["cortinaAntiPolvo3"], PDO::PARAM_STR);
        $stmt->bindParam(":boquillasCanonesDeAire", $datos["boquillasCanonesDeAire"], PDO::PARAM_STR);
        $stmt->bindParam(":tempAmbienteMaxTransportadora", $datos["tempAmbienteMaxTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tempAmbienteMinTransportadora", $datos["tempAmbienteMinTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tieneRodillosImpacto", $datos["tieneRodillosImpacto"], PDO::PARAM_STR);
        $stmt->bindParam(":camaImpacto", $datos["camaImpacto"], PDO::PARAM_STR);
        $stmt->bindParam(":camaSellado", $datos["camaSellado"], PDO::PARAM_STR);
        $stmt->bindParam(":basculaPesaje", $datos["basculaPesaje"], PDO::PARAM_STR);
        $stmt->bindParam(":rodilloCarga", $datos["rodilloCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":rodilloImpacto", $datos["rodilloImpacto"], PDO::PARAM_STR);
        $stmt->bindParam(":basculaASGCO", $datos["basculaASGCO"], PDO::PARAM_STR);
        $stmt->bindParam(":barraImpacto", $datos["barraImpacto"], PDO::PARAM_STR);
        $stmt->bindParam(":barraDeslizamiento", $datos["barraDeslizamiento"], PDO::PARAM_STR);
        $stmt->bindParam(":espesorUHMV", $datos["espesorUHMV"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoBarra", $datos["anchoBarra"], PDO::PARAM_STR);
        $stmt->bindParam(":largoBarra", $datos["largoBarra"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloAcanalamientoArtesa1", $datos["anguloAcanalamientoArtesa1"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloAcanalamientoArtesa2", $datos["anguloAcanalamientoArtesa2"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloAcanalamientoArtesa3", $datos["anguloAcanalamientoArtesa3"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloAcanalamientoArtesa1AntesPoleaMotriz", $datos["anguloAcanalamientoArtesa1AntesPoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloAcanalamientoArtesa2AntesPoleaMotriz", $datos["anguloAcanalamientoArtesa2AntesPoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloAcanalamientoArtesa3AntesPoleaMotriz", $datos["anguloAcanalamientoArtesa3AntesPoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":integridadSoportesRodilloImpacto", $datos["integridadSoportesRodilloImpacto"], PDO::PARAM_STR);
        $stmt->bindParam(":materialAtrapadoEntreCortinas", $datos["materialAtrapadoEntreCortinas"], PDO::PARAM_STR);
        $stmt->bindParam(":materialAtrapadoEntreGuardabandas", $datos["materialAtrapadoEntreGuardabandas"], PDO::PARAM_STR);
        $stmt->bindParam(":materialAtrapadoEnBanda", $datos["materialAtrapadoEnBanda"], PDO::PARAM_STR);
        $stmt->bindParam(":integridadSoportesCamaImpacto", $datos["integridadSoportesCamaImpacto"], PDO::PARAM_STR);
        $stmt->bindParam(":inclinacionZonaCargue", $datos["inclinacionZonaCargue"], PDO::PARAM_STR);
        $stmt->bindParam(":sistemaAlineacionCarga", $datos["sistemaAlineacionCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":cantidadSistemaAlineacionEnCarga", $datos["cantidadSistemaAlineacionEnCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":sistemasAlineacionCargaFuncionando", $datos["sistemasAlineacionCargaFuncionando"], PDO::PARAM_STR);
        $stmt->bindParam(":sistemaAlineacionEnRetorno", $datos["sistemaAlineacionEnRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":cantidadSistemaAlineacionEnRetorno", $datos["cantidadSistemaAlineacionEnRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":sistemasAlineacionRetornoFuncionando", $datos["sistemasAlineacionRetornoFuncionando"], PDO::PARAM_STR);
        $stmt->bindParam(":sistemaAlineacionRetornoPlano", $datos["sistemaAlineacionRetornoPlano"], PDO::PARAM_STR);
        $stmt->bindParam(":sistemaAlineacionArtesaCarga", $datos["sistemaAlineacionArtesaCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":sistemaAlineacionRetornoEnV", $datos["sistemaAlineacionRetornoEnV"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjeRodilloCentralCarga", $datos["largoEjeRodilloCentralCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjeRodilloCentralCarga", $datos["diametroEjeRodilloCentralCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":largoTuboRodilloCentralCarga", $datos["largoTuboRodilloCentralCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjeRodilloLateralCarga", $datos["largoEjeRodilloLateralCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjeRodilloLateralCarga", $datos["diametroEjeRodilloLateralCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroRodilloLateralCarga", $datos["diametroRodilloLateralCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":largoTuboRodilloLateralCarga", $datos["largoTuboRodilloLateralCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoRodilloCarga", $datos["tipoRodilloCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaEntreArtesasCarga", $datos["distanciaEntreArtesasCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoInternoChasisRodilloCarga", $datos["anchoInternoChasisRodilloCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoExternoChasisRodilloCarga", $datos["anchoExternoChasisRodilloCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloAcanalamientoArtesaCArga", $datos["anguloAcanalamientoArtesaCArga"], PDO::PARAM_STR);
        $stmt->bindParam(":detalleRodilloCentralCarga", $datos["detalleRodilloCentralCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":detalleRodilloLateralCarg", $datos["detalleRodilloLateralCarg"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroPoleaMotrizTransportadora", $datos["diametroPoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPoleaMotrizTransportadora", $datos["anchoPoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoPoleaMotrizTransportadora", $datos["tipoPoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjePoleaMotrizTransportadora", $datos["largoEjePoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjeMotrizTransportadora", $datos["diametroEjeMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":icobandasCentraEnPoleaMotrizTransportadora", $datos["icobandasCentraEnPoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anguloAmarrePoleaMotrizTransportadora", $datos["anguloAmarrePoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRevestimientoPoleaMotrizTransportadora", $datos["estadoRevestimientoPoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoTransicionPoleaMotrizTransportadora", $datos["tipoTransicionPoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaTransicionPoleaMotrizTransportadora", $datos["distanciaTransicionPoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":potenciaMotorTransportadora", $datos["potenciaMotorTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaPoleaMotrizTransportadora", $datos["guardaPoleaMotrizTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoEstructura", $datos["anchoEstructura"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoTrayectoCarga", $datos["anchoTrayectoCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":pasarelaRespectoAvanceBanda", $datos["pasarelaRespectoAvanceBanda"], PDO::PARAM_STR);
        $stmt->bindParam(":materialAlimenticioTransportadora", $datos["materialAlimenticioTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":materialAcidoTransportadora", $datos["materialAcidoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":materialTempEntre80y150Transportadora", $datos["materialTempEntre80y150Transportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":materialSecoTransportadora", $datos["materialSecoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":materialHumedoTransportadora", $datos["materialHumedoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":materialAbrasivoFinoTransportadora", $datos["materialAbrasivoFinoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":materialPegajosoTransportadora", $datos["materialPegajosoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":materialGrasosoAceitosoTransportadora", $datos["materialGrasosoAceitosoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":marcaLimpiadorPrimario", $datos["marcaLimpiadorPrimario"], PDO::PARAM_STR);
        $stmt->bindParam(":referenciaLimpiadorPrimario", $datos["referenciaLimpiadorPrimario"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoCuchillaLimpiadorPrimario", $datos["anchoCuchillaLimpiadorPrimario"], PDO::PARAM_STR);
        $stmt->bindParam(":altoCuchillaLimpiadorPrimario", $datos["altoCuchillaLimpiadorPrimario"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoCuchillaLimpiadorPrimario", $datos["estadoCuchillaLimpiadorPrimario"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoTensorLimpiadorPrimario", $datos["estadoTensorLimpiadorPrimario"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoTuboLimpiadorPrimario", $datos["estadoTuboLimpiadorPrimario"], PDO::PARAM_STR);
        $stmt->bindParam(":frecuenciaRevisionCuchilla", $datos["frecuenciaRevisionCuchilla"], PDO::PARAM_STR);
        $stmt->bindParam(":cuchillaEnContactoConBanda", $datos["cuchillaEnContactoConBanda"], PDO::PARAM_STR);
        $stmt->bindParam(":marcaLimpiadorSecundario", $datos["marcaLimpiadorSecundario"], PDO::PARAM_STR);
        $stmt->bindParam(":referenciaLimpiadorSecundario", $datos["referenciaLimpiadorSecundario"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoCuchillaLimpiadorSecundario", $datos["anchoCuchillaLimpiadorSecundario"], PDO::PARAM_STR);
        $stmt->bindParam(":altoCuchillaLimpiadorSecundario", $datos["altoCuchillaLimpiadorSecundario"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoCuchillaLimpiadorSecundario", $datos["estadoCuchillaLimpiadorSecundario"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoTensorLimpiadorSecundario", $datos["estadoTensorLimpiadorSecundario"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoTuboLimpiadorSecundario", $datos["estadoTuboLimpiadorSecundario"], PDO::PARAM_STR);
        $stmt->bindParam(":frecuenciaRevisionCuchilla1", $datos["frecuenciaRevisionCuchilla1"], PDO::PARAM_STR);
        $stmt->bindParam(":cuchillaEnContactoConBanda1", $datos["cuchillaEnContactoConBanda1"], PDO::PARAM_STR);
        $stmt->bindParam(":sistemaDribbleChute", $datos["sistemaDribbleChute"], PDO::PARAM_STR);
        $stmt->bindParam(":marcaLimpiadorTerciario", $datos["marcaLimpiadorTerciario"], PDO::PARAM_STR);
        $stmt->bindParam(":referenciaLimpiadorTerciario", $datos["referenciaLimpiadorTerciario"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoCuchillaLimpiadorTerciario", $datos["anchoCuchillaLimpiadorTerciario"], PDO::PARAM_STR);
        $stmt->bindParam(":altoCuchillaLimpiadorTerciario", $datos["altoCuchillaLimpiadorTerciario"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoCuchillaLimpiadorTerciario", $datos["estadoCuchillaLimpiadorTerciario"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoTensorLimpiadorTerciario", $datos["estadoTensorLimpiadorTerciario"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoTuboLimpiadorTerciario", $datos["estadoTuboLimpiadorTerciario"], PDO::PARAM_STR);
        $stmt->bindParam(":frecuenciaRevisionCuchilla2", $datos["frecuenciaRevisionCuchilla2"], PDO::PARAM_STR);
        $stmt->bindParam(":cuchillaEnContactoConBanda2", $datos["cuchillaEnContactoConBanda2"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRodilloRetorno", $datos["estadoRodilloRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjeRodilloRetorno", $datos["largoEjeRodilloRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjeRodilloRetorno", $datos["diametroEjeRodilloRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroRodilloRetorno", $datos["diametroRodilloRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":largoTuboRodilloRetorno", $datos["largoTuboRodilloRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoRodilloRetorno", $datos["tipoRodilloRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaEntreRodillosRetorno", $datos["distanciaEntreRodillosRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoInternoChasisRetorno", $datos["anchoInternoChasisRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoExternoChasisRetorno", $datos["anchoExternoChasisRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":detalleRodilloRetorno", $datos["detalleRodilloRetorno"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroPoleaAmarrePoleaMotriz", $datos["diametroPoleaAmarrePoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPoleaAmarrePoleaMotriz", $datos["anchoPoleaAmarrePoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoPoleaAmarrePoleaMotriz", $datos["tipoPoleaAmarrePoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjePoleaAmarrePoleaMotriz", $datos["largoEjePoleaAmarrePoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjePoleaAmarrePoleaMotriz", $datos["diametroEjePoleaAmarrePoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":icobandasCentradaPoleaAmarrePoleaMotriz", $datos["icobandasCentradaPoleaAmarrePoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRevestimientoPoleaAmarrePoleaMotriz", $datos["estadoRevestimientoPoleaAmarrePoleaMotriz"], PDO::PARAM_STR);
        $stmt->bindParam(":dimetroPoleaAmarrePoleaCola", $datos["dimetroPoleaAmarrePoleaCola"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPoleaAmarrePoleaCola", $datos["anchoPoleaAmarrePoleaCola"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjePoleaAmarrePoleaCola", $datos["largoEjePoleaAmarrePoleaCola"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoPoleaAmarrePoleaCola", $datos["tipoPoleaAmarrePoleaCola"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjePoleaAmarrePoleaCola", $datos["diametroEjePoleaAmarrePoleaCola"], PDO::PARAM_STR);
        $stmt->bindParam(":icobandasCentradaPoleaAmarrePoleaCola", $datos["icobandasCentradaPoleaAmarrePoleaCola"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRevestimientoPoleaAmarrePoleaCola", $datos["estadoRevestimientoPoleaAmarrePoleaCola"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroPoleaTensora", $datos["diametroPoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":anchoPoleaTensora", $datos["anchoPoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoPoleaTensora", $datos["tipoPoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":largoEjePoleaTensora", $datos["largoEjePoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroEjePoleaTensora", $datos["diametroEjePoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":icobandasCentradaEnPoleaTensora", $datos["icobandasCentradaEnPoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":recorridoPoleaTensora", $datos["recorridoPoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":estadoRevestimientoPoleaTensora", $datos["estadoRevestimientoPoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoTransicionPoleaTensora", $datos["tipoTransicionPoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":distanciaTransicionPoleaColaTensora", $datos["distanciaTransicionPoleaColaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":potenciaMotorPoleaTensora", $datos["potenciaMotorPoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaPoleaTensora", $datos["guardaPoleaTensora"], PDO::PARAM_STR);
        $stmt->bindParam(":puertasInspeccion", $datos["puertasInspeccion"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaRodilloRetornoPlano", $datos["guardaRodilloRetornoPlano"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaTruTrainer", $datos["guardaTruTrainer"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaPoleaDeflectora", $datos["guardaPoleaDeflectora"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaZonaDeTransito", $datos["guardaZonaDeTransito"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaMotores", $datos["guardaMotores"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaCadenas", $datos["guardaCadenas"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaCorreas", $datos["guardaCorreas"], PDO::PARAM_STR);
        $stmt->bindParam(":interruptoresDeSeguridad", $datos["interruptoresDeSeguridad"], PDO::PARAM_STR);
        $stmt->bindParam(":sirenasDeSeguridad", $datos["sirenasDeSeguridad"], PDO::PARAM_STR);
        $stmt->bindParam(":guardaRodilloRetornoV", $datos["guardaRodilloRetornoV"], PDO::PARAM_STR);
        $stmt->bindParam(":diametroRodilloCentralCarga", $datos["diametroRodilloCentralCarga"], PDO::PARAM_STR);
        $stmt->bindParam(":tipoRodilloImpacto", $datos["tipoRodilloImpacto"], PDO::PARAM_STR);
        $stmt->bindParam(":integridadSoporteCamaSellado", $datos["integridadSoporteCamaSellado"], PDO::PARAM_STR);
        $stmt->bindParam(":ataqueAbrasivoTransportadora", $datos["ataqueAbrasivoTransportadora"], PDO::PARAM_STR);
        $stmt->bindParam(":observacionRegistroTransportadora", $datos["observacionRegistroTransportadora"], PDO::PARAM_STR);

        if ($stmt->execute()) {
            return "ok";
        } else {
            return "ERROR AL CREAR REGISTRO BANDA TRANSPORTADORA";
        }

        $stmt->close();
        $stmt = null;
    }

    public static function mdlActualizarTransportadoraHost($tabla, $idRegistro)
    {
        $stmt = Conexion::getConexionHost()->prepare("UPDATE $tabla set estadoRegistroSincronizadoTransportadora='Sincronizado' where idRegistro=$idRegistro");

        if ($stmt->execute()) {
            return "ok";
        } else {
            return "ERROR AL CREAR REGISTRO BANDA ELEVADORA";
        }

        $stmt->close();
        $stmt = null;
    }
}
