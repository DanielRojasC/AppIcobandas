<?php

class Conexion
{

    public static function getConexionLocal()
    {
        $link = new PDO("sqlsrv:Server=localhost;Database=prueba");
        $link->exec("set names utf8");

        return $link;
    }
    public static function getConexionHost()
    {
        $link = new PDO('sqlsrv:server=198.71.226.2;Database=icobandasapp_db', 'administrador', 'icobandasAppAdmin');
        $link->exec("set names utf8");
        return $link;
        
    }

}
