const REGISTRAR_RESERVA_ESPORADICA = {
    "tipoReserva": "ESPORADICA",
    "cantidadAlumnos": 32,
    "apellidoDocente": "ApellidoDocente",
    "nombreDocente": "NombreDocente",
    "correoDocente": "correo@gmail.com",
    "actividadAcademica": "Programación Concurrente",
    "realizadaPor": "969edd26-6e52-4673-b697-7fdf7bab7263", // id del bedel que realiza la reserva

    // Información de cada día a reservar
    "reservasDia": [
        {
            "fecha": "2025-11-23",
            "horaInicio": "23:03:34",
            "duracion": 90,
            "nroAula": 10
        },
        {
            "fecha": "2025-11-24",
            "horaInicio": "23:03:34",
            "duracion": 120,
            "nroAula": 10
        }
    ]
}

const REGISTRAR_RESERVA_POR_PERIODO = {
    "tipoReserva": "POR_PERIODO",
    "cantidadAlumnos": 32,
    "apellidoDocente": "ApellidoDocente",
    "nombreDocente": "NombreDocente",
    "correoDocente": "correo@gmail.com",
    "actividadAcademica": "Sistemas Operativos",
    "realizadaPor": "969edd26-6e52-4673-b697-7fdf7bab7263", // id del bedel que realiza la reserva

    // Información del ciclo lectivo y los días de la semana a reservar
    "anioCicloLectivo": 2025,
    "periodoReserva": "ANUAL",
    "reservasDiasSemana": [
        {
            "diaSemana": "LUNES",
            "horaInicio": "09:00:00",
            "duracion": 90,
            "nroAula": 10
        },
        {
            "diaSemana": "MARTES",
            "horaInicio": "10:30:00",
            "duracion": 120,
            "nroAula": 10
        },
        {
            "diaSemana": "MIERCOLES",
            "horaInicio": "10:30:00",
            "duracion": 120,
            "nroAula": 10
        },
        {
            "diaSemana": "JUEVES",
            "horaInicio": "10:30:00",
            "duracion": 120,
            "nroAula": 10
        },
        {
            "diaSemana": "VIERNES",
            "horaInicio": "10:30:00",
            "duracion": 60,
            "nroAula": 10
        }
    ]
}