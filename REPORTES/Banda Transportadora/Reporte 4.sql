select registro.idRegistro,registro.usuarioRegistro, registro.estadoRegistro,registro.fechaRegistro,plantas.nameplanta, transportador.tipoTransportador,anchoPoleaMotrizTransportadora,diametroPoleaMotrizTransportadora,anchoPoleaColaTransportadora,diametroPoleaColaTransportadora,anchoPoleaAmarrePoleaCola,anchoPoleaAmarrePoleaMotriz,diametroPoleaAmarrePoleaMotriz,anchoPoleaTensora,diametroPoleaTensora, count(transportador.idTransportador) as 'Cantidad de Poleas'

from registro
join plantas on registro.codplanta=plantas.codplanta
join transportador on transportador.idTransportador=registro.idTransportador
join bandaTransportadora on registro.idRegistro=bandaTransportadora.idRegistro
where transportador.tipoTransportador='B.T'

group by registro.usuarioRegistro,registro.idRegistro,registro.estadoRegistro,registro.fechaRegistro, plantas.nameplanta, transportador.tipoTransportador,
transportador.tipoTransportador,anchoPoleaMotrizTransportadora,diametroPoleaMotrizTransportadora,anchoPoleaColaTransportadora,diametroPoleaColaTransportadora,anchoPoleaAmarrePoleaCola,anchoPoleaAmarrePoleaMotriz,diametroPoleaAmarrePoleaMotriz,anchoPoleaTensora,diametroPoleaTensora