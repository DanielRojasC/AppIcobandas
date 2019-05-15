select registro.idRegistro,registro.usuarioRegistro, registro.estadoRegistro,registro.fechaRegistro,plantas.nameplanta, transportador.tipoTransportador,
bandaTransmision.anchoBandaTransmision, count(transportador.idTransportador) as 'Cantidad Transportadores'

from registro
join plantas on registro.codplanta=plantas.codplanta
join transportador on transportador.idTransportador=registro.idTransportador
join bandaTransmision on registro.idRegistro=bandaTransmision.idRegistro
where transportador.tipoTransportador='B.DSF'

group by registro.usuarioRegistro,registro.idRegistro,registro.estadoRegistro,registro.fechaRegistro, plantas.nameplanta, transportador.tipoTransportador,
bandaTransmision.anchoBandaTransmision