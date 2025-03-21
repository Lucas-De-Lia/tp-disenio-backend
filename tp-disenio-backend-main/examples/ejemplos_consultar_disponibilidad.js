const CONSULTAR_DISPONIBILIDAD_ESPORADICA = {
  "capacidad": 30, // 30 alumnos
  "tipoAula": "SIN_RECURSOS_ADICIONALES",
  "tipoReserva": "POR_PERIODO",
  "diasReserva": [
    {
      "fecha": "2025-04-24",
      "horaInicio": "08:00:00",
      "duracion": 120 // 2 horas
    },
    {
      "fecha": "2025-04-25",
      "horaInicio": "11:00:00",
      "duracion": 180 // 3 horas
    }
  ]
}

const CONSULTAR_DISPONIBILIDAD_POR_PERIODO = {
  "capacidad": 30, // 30 alumnos
  "tipoAula": "SIN_RECURSOS_ADICIONALES",
  "tipoReserva": "POR_PERIODO",
  "diasReserva": [
    {
      "diaSemana": "LUNES",
      "horaInicio": "08:00:00",
      "duracion": 120 // 2 horas
    },
    {
      "diaSemana": "MIERCOLES",
      "horaInicio": "11:00:00",
      "duracion": 180 // 3 horas
    }
  ]
}
