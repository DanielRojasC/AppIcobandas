select registro.idRegistro,registro.usuarioRegistro, registro.estadoRegistro,registro.fechaRegistro,plantas.nameplanta, transportador.tipoTransportador,bandaTransportadora.tipoRevestimientoTolvaCarga,count(transportador.idTransportador) as 'Cantidad de Equipos'

from registro
join plantas on registro.codplanta=plantas.codplanta
join transportador on transportador.idTransportador=registro.idTransportador
join bandaTransportadora on registro.idRegistro=bandaTransportadora.idRegistro
where transportador.tipoTransportador='B.T'

group by registro.usuarioRegistro,registro.idRegistro,registro.estadoRegistro,registro.fechaRegistro, plantas.nameplanta, transportador.tipoTransportador,
transportador.tipoTransportador,bandaTransportadora.tipoRevestimientoTolvaCarga