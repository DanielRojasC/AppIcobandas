select registro.idRegistro,registro.usuarioRegistro, registro.estadoRegistro,registro.fechaRegistro,plantas.nameplanta, transportador.tipoTransportador,bandaElevadora.anchoPoleaColaElevadora, bandaElevadora.diametroPoleaColaElevadora, diametroPoleaMotrizElevadora, diametroPoleaColaElevadora, count(transportador.idTransportador) as 'Cantidad de Poleas'

from registro
join plantas on registro.codplanta=plantas.codplanta
join transportador on transportador.idTransportador=registro.idTransportador
join bandaElevadora on registro.idRegistro=bandaElevadora.idRegistro
where transportador.tipoTransportador='B.E'

group by registro.usuarioRegistro,registro.idRegistro,registro.estadoRegistro,registro.fechaRegistro, plantas.nameplanta, transportador.tipoTransportador,
transportador.tipoTransportador, bandaElevadora.anchoPoleaColaElevadora, bandaElevadora.diametroPoleaColaElevadora, diametroPoleaMotrizElevadora, diametroPoleaColaElevadora