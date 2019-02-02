package com.icobandas.icobandasapp.Utilities;

public class Utilities {


    //CONSTANTES CAMPOS TABLA CLIENTES
    public static final String TABLA_CLIENTES="clientes";
    public static final String NIT_CLIENTE="nit";
    public static final String NOMBRE_CLIENTE="nombreCliente";



    //CONSTANTES CAMPOS TABLA USUARIO
    public static final String TABLA_USUARIOS="usuarios";
    public static final String NOMBRE_USUARIO="nombreUsuario";
    public static final String NOMBRE_VENDEDOR="nombreVendedor";
    public static final String PWD_USUARIO="contraseñaUsuario";


    //CONSTANTES CAMPOS TABLA PLANTAS
    public static final String ID_PLANTAS= "idPlanta";
    public static final String NOMBRE_PLANTAS= "nombrePlanta";
    public static final String NIT_CLIENTE_PLANTA="nitCliente";
    public static final String ID_CIUDAD="idCiudad";
    public static final String DIRECCION_PLANTA="direccionPlanta";
    public static final String TABLA_PLANTAS="plantas";


    //CONSTANTES CAMPOS TABLA CIUDAD
    public static final String COD_CIUDAD= "idCiudad";
    public static final String NOMBRE_CIUDAD= "nombreCiudad";
    public static final String TABLA_CIUDAD= "ciudad";



    //CONSTANTES CAMPOS TABLA TRANSPORTADORES
    public static final String ID_TRANSPORTADOR= "idTransportador";
    public static final String NOMBRE_TRANSPORTADOR= "nombreTransportador";
    public static final String TIPO_TRANSPORTADOR="tipoTransportador";
    public static final String COD_PLANTA="idPlanta";
    public static final String CARACTERISTICA_TRANSPORTADORA="caracteristicaTransportador";
    public static final String TABLA_TRANSPORTADOR="transportador";



    //CONSTANTES CAMPOS TABLA REGISTRO
    public static final String ID_REGISTRO= "idRegistro";
    public static final String FECHA_REGISTRO= "fechaRegistro";
    public static final String ID_TRANSPORTADOR_REGISTRO="idTransportador";
    public static final String COD_PLANTA_REGISTRO="codPlanta";
    public static final String USUARIO_REGISTRO="usuarioRegistro";
    public static final String TABLA_REGISTRO="registro";



    public static final String CREAR_TABLA_CLIENTES="CREATE TABLE "+TABLA_CLIENTES+"("+NIT_CLIENTE+" TEXT,"+NOMBRE_CLIENTE+" TEXT)";
    public static final String CREAR_TABLA_USUARIOS="CREATE TABLE "+TABLA_USUARIOS+"("+NOMBRE_USUARIO+" TEXT,"+NOMBRE_VENDEDOR+" TEXT,"+PWD_USUARIO+" TEXT)";
    public static final String CREAR_TABLA_PLANTAS="CREATE TABLE "+TABLA_PLANTAS+"("+ID_PLANTAS+" TEXT IDENTITY(1,1) PRIMARY KEY,"+NOMBRE_PLANTAS+" TEXT,"+NIT_CLIENTE_PLANTA+" TEXT,"+DIRECCION_PLANTA+" TEXT,"+ID_CIUDAD+" TEXT, FOREIGN  KEY("+NIT_CLIENTE_PLANTA+") REFERENCES "+TABLA_CLIENTES+"("+NIT_CLIENTE+"), FOREIGN KEY("+ID_CIUDAD+") REFERENCES "+TABLA_CIUDAD+"("+COD_CIUDAD+"))";
    public static final String CREAR_TABLA_CIUDAD="CREATE TABLE "+TABLA_CIUDAD+"("+COD_CIUDAD+" TEXT IDENTITY(1,1) PRIMARY KEY,"+NOMBRE_CIUDAD+" TEXT)";
    public static final String CREAR_TABLA_TRANSPORTADOR="CREATE TABLE "+TABLA_TRANSPORTADOR+"("+ID_TRANSPORTADOR+" TEXT IDENTITY(1,1) PRIMARY KEY,"+NOMBRE_TRANSPORTADOR+" TEXT,"+TIPO_TRANSPORTADOR+" TEXT,"+CARACTERISTICA_TRANSPORTADORA+" TEXT,"+COD_PLANTA+" TEXT, FOREIGN KEY("+COD_PLANTA+") references "+TABLA_PLANTAS+"("+ID_PLANTAS+"))";
    public static final String CREAR_TABLA_REGISTRO="CREATE TABLE "+TABLA_REGISTRO+"("+ID_REGISTRO+" TEXT IDENTITY(1,1) PRIMARY KEY,"+FECHA_REGISTRO+" TEXT,"+ID_TRANSPORTADOR_REGISTRO+" TEXT,"+COD_PLANTA_REGISTRO+" TEXT,"+USUARIO_REGISTRO+" TEXT, FOREIGN KEY("+COD_PLANTA_REGISTRO+") references "+TABLA_PLANTAS+"("+ID_PLANTAS+"), FOREIGN KEY("+ID_TRANSPORTADOR_REGISTRO+") REFERENCES "+TABLA_TRANSPORTADOR+"("+ID_TRANSPORTADOR+"), FOREIGN KEY("+USUARIO_REGISTRO+") REFERENCES "+TABLA_USUARIOS+"("+NOMBRE_USUARIO+"))";


}
