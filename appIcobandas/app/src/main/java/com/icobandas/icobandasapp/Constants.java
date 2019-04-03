package com.icobandas.icobandasapp;

public class Constants
{
    public  static  String url= "http://8e47e638.ngrok.io" +
            "/api/";

    public static String[] porcentajeFinos = new String[21];
    public static String[] tempMinAmbiente = new String[67];
    public static String[] tempMaxAmbiente = new String[72];
    //public static String[] largoTornillo = new String[11];


    public static final String [] marcaBanda={
            "Sin información","icobandas","BIT","China-BIT","China-AR Los Restrepos","China-Suresco","China-ByS de la Costa","Contitech","Phoenix","Fenner-Dunlop","Bridgestone","Bando","SIG","Kauman","Double-Arrow","HYC","Desconocida"
    };

    public static final String [] estadoPartes={
            "Sin información","Reparar","Sucio","Limpio","Ajustar","Reemplazar","Ofrecer","Ok"
    };
    public static final String [] noLonas={

            "Sin información","1","2","3","4","5","6","7","8"
    };

    public static final String [] tipoLona={
            "Sin información","B - Algodón","Tejido solido","Monolona","D- Aramida","NN / PP","EP","EE","ST","NN + Breaker","EP + Breaker"
    };

    public static final String [] tipoCubierta={
            "Sin información","DIN-A (Alimenticia, antiadherente) / ANL-S", "DIN-A (Alimenticia blanca) / ANL-B","DIN-XW (Antiabrasiva) / ANL", "DIN-C (Químicos)","ISO-D (Antiabrasiva) / ANL","DIN-E (Antiestática)","DIN-G (Aceites) / ANL-A","ISO-H (Antiabrasiva) / ANL","DIN-K (Antillama) / ANL-F","ISO-K (Antillama) / ANL-F","ISO-L (Antiabrasiva) / ANL","DIN-R (Resistente al frío)","DIN-S (Antllama con o sin cubiertas)","DIN-T (Alta temperatura) / ANL-T","DIN-V (Autoextinguible)","DIN-W (Antiabrasiva) / ANL","DIN-X (Antiabrasiva) / ANL","DIN-Y (Antiabrasiva)","DIN-Z (Antiabrasiva)"
    };

    public static final String [] tipoEmpalme={
            "Sin información","Mecánico","Vulcanizado Frío","Vulc. Caliente escalonado","Vulc. Caliente dedos"
    };

    public static final String [] localizacionTensor={
            "Sin información","Tornillo en la cola","Gravedad en el medio","Automático en la cola","Otro"
    };

    public static final String [] opcionSiNo={
            "Sin información","No","Si"
    };

    public static final String [] tipoTransicion={
            "Sin información","Linea de trabajo media y tangente al tambor","Linea tangente al tambor"
    };

    public static final String [] diasSemana={
            "Sin información","1","2","3","4","5","6","7"
    };

    public static final String [] horasTrabajo={
            "Sin información","4","8","12","16","20","24"
    };

    public static final String [] abrasividad={
            "Sin información","Alta","Media","Baja"
    };


    public static void llenar()
    {
        int x=1;
        porcentajeFinos[0]= "Sin información";
        for (int i=5;i<=100;i=i+5)
        {
           porcentajeFinos[x]= String.valueOf(i)+"%";
           x++;
        }
    }

    public static final String [] temperaturaSobreLaBanda={
            "Sin información","< 60ºC","> 60ºC < 100ºC","> 100ºC < 150ºC","> 150ºC < 204ºC","> 204ºC < 300ºC","> 300ºC"
    };

    public static final String [] fugaDeMateriales={
            "Sin información","Si, finos","Si, gruesos", "Si, finos y gruesos","No"
    };

    public static final String [] protectorGuardaBandas={
            "Sin información","X-Wear","Armorite", "N/A"
    };


    public static final String [] tipoRodilloCarga={
            "Sin información","CEMA A","CEMA B","CEMA C","CEMA D","CEMA E","CEMA F","Autolimpiante","De discos de caucho","De discos de uretano","Autoalineante"
    };

    public static final String [] tipoPolea={
            "Sin información","Lisa plana","Lisa corona","Aspas con sección cónica","Aspas sin sección cónica","Espiral","Rvto. Cerámico","Rvto. Caucho ranurado","Rvto Caucho romboidal","Rvto Caucho MSHA Arrowhead","Uretano ranurado"
    };

    public static final String [] ladoPasarela={
            "Sin información","Izquierda","Derecha", "No hay pasarela"
    };

    public static final String [] marcaLimpiador={
            "Sin información", "ASGCO","Martin","Flexco","Brelko","Matam","Hosh","Arch","Metso","Tip-Top","Gordon","Telos","Otro"
    };

    public static final String [] referenciaLimpiador={
            "Sin información", "Skalper-III","Skalper-IV","Skalper-MDX","EZ-Skalper","Super-Skalper","Mini-Skalper","Excalibur"
    };

    public static final String [] frecuenciaRevision={
            "Sin información","Diaria","Semanal","Quincenal","Mensual","Bimensual","Trimestral","Semestral","Anual"
    };

    public static final String [] marcaCangilon={
            "Sin información","Maxi-Lift","Tapco","Lufelo","4B","Stif","Sanwei","Otro"
    };

    public static final String [] materialCangilon={
            "Sin información","HDPE","Nylon","FDA Nylon","Uretano","Metálico soldado","Metálico fundido"
    };

    public static final String [] noFilasCangilon={
            "Sin información", "1","2","3","4"
    };
    public static final String [] noAgujerosCangilon={
            "Sin información", "0","1","2","3","4","5","6","7","8"
    };

    public static final String [] tipoVentilacion={
            "Sin información","Ninguna","Tipo E","Tipo F","Tipo G","Tipo H","Personalizada"
    };

    public static final String [] materialTornillo={
            "Sin información", "Acero al carbón","Zincado","Inoxidable"
    };

    public static final String [] monitorPeligro={
            "Sin información","T500 Elite Hotbus™"," Watchdog™ Super Elite (WDC4)","T400N Elite","T400 Elite Hotswitch (versión PTC)","A400 Elite Trackswitch","B400 Elite Beltswitch","X400 Elite Alarmswitch"
    };

    public static final String [] rodamiento={
            "Sin información","WDB serie de sensores de temperatura","ADB serie de sensores de temperatura™","WDB7 Sensores  tipo orejeta","Serie MDB de sensores"
    };

    public static final String [] monitorDesalineacion={
            "Sin información","Touchswitch™","Sensor BAP","Bulldog","Velocidad - WDA"
    };

    public static final String [] monitorVelocidad={
            "Sin información","M100 Stopswitch","M300 Slipswitch","M800 Speed Switch","Whirligig®","Whirligig Heavy Duty","Rotech - Monitor Eje Rotativo"
    };

    public static final String [] sensorInductivo={
            "Sin información","P100 18mm","P300 30mm de Proximidad","P800 norma DIN de Proximidad"
    };

    public static final String [] indicadorDeNivel={
            "Sin información","Binswitch Sensor Capacitivo","Auto-Set","Auto-Set™ Remote","Auto-Set™ Flush Probe","RLI Shaker","Roto-Safe™"
    };

    public static final String [] cajaUnion={
            "Sin información", "Tipo 4BJ","En línea"
    };

    public static final String [] alarmaYPantalla={
            "Sin información", "X400 Elite Alarmswitch","Tachocount Programable"
    };

    public static final String [] interruptorSeguridad={
            "Sin información","X400 Elite Alarmswitch","Tachocount Programable"
    };

    public static final String [] resistenciaRoturaLona={
            "Sin información", "30","40","50","70","100","125","150","175","200", "225","250","275","300","315","350","375","400","450","500","550","600"
    };

    public static final String [] tipoCarga={
            "Sin información", "Carga Excavando", "Carga por caída libre"
    };

    public static final String [] tipoDescarga={
            "Sin información", "Descarga centrífuga", "Descarga por gravedad", "Descargar contínua (híbrida)"
    };


    public static final String [] tipoRevestimiento={
            "Sin información", "icobandas - FPI", "Caucho/Cerámico", "Sin revestir", "Otro"
    };

    public static final String [] meses={
            "Sin información", "1", "2", "3", "4","5", "6", "7", "8","9", "10", "11", "12",
    };

    public static final String [] inclinacionZonaCarga={
            "Sin información","6°- 10°", "11°- 15°", "16°- 20°", "21°- 25°","26°- 30°", "31°- 35°", "36°- 40°", "41°- 45°"
    };

    public static final String [] tipoCangilon={
            "Sin información", "Cangilón", "Cangilón Excavador"
    };

    public static final String [] angulosAcanalamiento={
            "Sin información", "0°", "15°","20°","35°","45°"
    };

    public static final String [] anguloAmarre={
            "Sin información", "180°","190°","200°","210°","220°","230°","240°","360°","380°","400°","420°","440°","460°","480°"
    };

    public static void llenarTempMin()
    {
        int x=1;
        tempMinAmbiente[0]="Sin información";
        for (int i=-40;i<=25;i++)
        {
            tempMinAmbiente[x]= String.valueOf(i)+"°C";
            x++;
        }
    }

    public static void llenarTempMax()
    {
        int x=1;
        tempMaxAmbiente[0]="Sin información";
        for (int i=0;i<71;i++)
        {
            tempMaxAmbiente[x]= String.valueOf(i)+"°C";
            x++;
        }
    }


    public static final String [] maxGranulometria={
            "Sin información","5","10","15","20","25","37","50","75","100","125","150","175","200","225","250"
    };

    public static final String [] tipoTransmision={
            "Sin información","Tangencial","SemiCruzada","Cruzada"
    };

    public static final String [] diametroRosca={
            "Sin información","1/4\" / 6.35","5/16\" / 8.4","3/8 \" / 9.6"
    };

    public static final String [] longitudImpacto={
            "Sin información","18","48","60"
    };

    public static final String [] espesorVPlow={
            "Sin información","0.25\" / 6.4","0.375\" / 9.6","0.5\" / 12.7","0.625\" / 12.9","0.75\" / 19.1","1\" / 25.4"
    };

    public static final String [] anchoVPlow={
            "Sin información","2\" / 50","4\" / 100","6\" / 150","8\" / 204","10\" / 254","12\" / 305"
    };

    public static final String [] espesorUHMV={
            "Sin información", "1/4\"","1/2\"","1\""
    };

    public static final String [] anchoBarra={
            "Sin información","2\" / 50","3\" / 75","4\" / 100"
    };

    public static final String [] largoTornillo={
            "Sin información","0.75","1","1.25","1.5","1.75","2","2.25","2.5","2.75","3"
    };


}