package com.tpdisenio.gSM3C.services;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tpdisenio.gSM3C.domain.Aula;
import com.tpdisenio.gSM3C.domain.Bedel;
import com.tpdisenio.gSM3C.domain.Cuatrimestre;
import com.tpdisenio.gSM3C.domain.Reserva;
import com.tpdisenio.gSM3C.domain.ReservaDia;
import com.tpdisenio.gSM3C.dto.ReservaDTO;
import com.tpdisenio.gSM3C.dto.ReservaDiaDTO;
import com.tpdisenio.gSM3C.dto.ReservaDiaSemanaDTO;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.tpdisenio.gSM3C.dao.ReservaDao;
import com.tpdisenio.gSM3C.domain.CicloLectivo;
import com.tpdisenio.gSM3C.enums.TipoAula;
import com.tpdisenio.gSM3C.enums.TipoReserva;
import com.tpdisenio.gSM3C.exception.AulaNotFoundException;
import com.tpdisenio.gSM3C.exception.BedelNotFoundException;
import com.tpdisenio.gSM3C.exception.DuplicatedDayException;
import com.tpdisenio.gSM3C.utils.FechaUtils;
import com.tpdisenio.gSM3C.enums.PeriodoReserva;
import java.time.ZoneId;
import java.util.Calendar;

@Service
public class ReservaService {
    @Autowired
    @Qualifier("reservaSQLDao")
    private ReservaDao reservaDao;

    @Autowired
    private BedelService bedelService;

    @Autowired
    private CuatrimestreService cuatrimestreService;

    @Autowired
    private CicloLectivoService cicloLectivoService;

    @Autowired
    private AulaReservaService aulaReservaService;

    @Transactional
    public Reserva registrarReserva(ReservaDTO reservaDTO) {

        // Validamos los datos de la reserva
        validarReserva(reservaDTO);

        // Validamos la disponibilidad de las aulas
        validarDisponibilidadAulas(reservaDTO);

        // Creamos la reserva y asignamos los atributos comunes
        Reserva reserva = new Reserva();
        reserva.setTipoReserva(TipoReserva.fromString(reservaDTO.getTipoReserva()));
        reserva.setCantidadAlumnos(reservaDTO.getCantidadAlumnos());
        reserva.setFechaSolicitud(new Date());
        reserva.setApellidoDocente(reservaDTO.getApellidoDocente());
        reserva.setNombreDocente(reservaDTO.getNombreDocente());
        reserva.setCorreoDocente(reservaDTO.getCorreoDocente());
        reserva.setActividadAcademica(reservaDTO.getActividadAcademica());

        // Obtenemos y asignamos al bedel que realiza la reserva
        String bedelId = reservaDTO.getRealizadaPor();
        Bedel bedel = bedelService.buscarBedelPorId(bedelId);
        reserva.setRealizadaPor(bedel);

        // Obtenemos y asignamos los dias de reserva según el tipo de reserva
        if (TipoReserva.ESPORADICA.equals(reserva.getTipoReserva()))
            asignarReservasDiaEsporadica(reserva, reservaDTO);
        else if (TipoReserva.POR_PERIODO.equals(reserva.getTipoReserva()))
            asignarReservasDiaPorPeriodo(reserva, reservaDTO);

        // Guardamos la reserva en la base de datos
        return reservaDao.registrarReserva(reserva);
    }

    public boolean verificarHoraValida(Time horaInicio, int duracionMinutos) {
        LocalTime inicio = horaInicio.toLocalTime();

        // Sumar la duración
        LocalTime horaFinal = inicio.plusMinutes(duracionMinutos);

        // Verificar si la hora final es menor a la medianoche
        return !horaFinal.isBefore(inicio);
    }

    /**
     * Valida los datos de la reserva a registrar según su tipo y sus datos
     * asociados.
     * Se encarga principalmente de realizar las validaciones que no están cubiertas
     * por
     * las anotaciones de validación en el DTO.
     *
     * @param reservaDTO el objeto ReservaDTO que contiene los datos de la reserva a
     *                   validar
     * @throws IllegalArgumentException si alguno de los datos de la reserva es
     *                                  inválido
     * @throws BedelNotFoundException   si el bedel que realiza la reserva no existe
     */
    private void validarReserva(ReservaDTO reservaDTO) {
        // Validar que el tipo de reserva sea válido (validación implícita en el método
        // fromString)
        TipoReserva tipoReserva = TipoReserva.fromString(reservaDTO.getTipoReserva());

        // Validar que el bedel que realiza la reserva exista
        if (bedelService.buscarBedelPorId(reservaDTO.getRealizadaPor()) == null) {
            throw new BedelNotFoundException("El ID del bedel que realiza la reserva es inválido.");
        }

        // Validar según el tipo de reserva
        if (TipoReserva.ESPORADICA.equals(tipoReserva)) {
            // Validación para reservas esporádicas
            if (reservaDTO.getReservasDia() == null || reservaDTO.getReservasDia().isEmpty()) {
                throw new IllegalArgumentException(
                        "Debe especificar al menos una fecha a reservar para el tipo de reserva esporádica.");
            }
            if (reservaDTO.getReservasDiasSemana() != null && !reservaDTO.getReservasDiasSemana().isEmpty()) {
                throw new IllegalArgumentException(
                        "No debe proporcionar reservas por días de la semana para una reserva esporádica.");
            }
            if (reservaDTO.getAnioCicloLectivo() != null || reservaDTO.getPeriodoReserva() != null) {
                throw new IllegalArgumentException(
                        "No debe proporcionar datos del ciclo lectivo o periodo para una reserva esporádica.");
            }
            // Validar cada ReservaDiaDTO
            Map<LocalDate, Integer> contadorFechas = new HashMap<>();
            for (ReservaDiaDTO reservaDiaDTO : reservaDTO.getReservasDia()) {
                if (reservaDiaDTO.getDuracion() % 30 != 0) {
                    throw new IllegalArgumentException(
                            "La duración de la reserva debe ser múltiplo de 30 minutos (Ver día "
                                    + reservaDiaDTO.getFecha() + ").");
                }
                if(reservaDiaDTO.getDuracion() > 300)
                    throw new IllegalArgumentException("La duración de la reserva no puede ser mayor a 5 horas (Ver día "
                            + reservaDiaDTO.getFecha() + ").");
            
                if (!verificarHoraValida(reservaDiaDTO.getHoraInicio(), reservaDiaDTO.getDuracion())) {
                    throw new IllegalArgumentException(
                            "La duración de la reserva debe respetar los limites horarios, no puede pasar las 24hs.");
                }

                if (aulaReservaService.obtenerAulaPorNumero(reservaDiaDTO.getNroAula()) == null) {
                    throw new AulaNotFoundException("El número de aula no corresponde a ningún aula existente (Ver día "
                            + reservaDiaDTO.getFecha() + ", aula " + reservaDiaDTO.getNroAula() + ").");
                }
                contadorFechas.put(reservaDiaDTO.getFecha(),
                        contadorFechas.getOrDefault(reservaDiaDTO.getFecha(), 0) + 1);
            }
            // Verificar si hay fechas repetidas
            for (Map.Entry<LocalDate, Integer> entry : contadorFechas.entrySet()) {
                if (entry.getValue() > 1) {
                    throw new DuplicatedDayException("No se puede reservar más de una vez en la misma fecha (Ver fecha "
                            + entry.getKey() + ").");
                }
            }
        } else if (TipoReserva.POR_PERIODO.equals(tipoReserva)) {
            // Validación para reservas por periodo
            if (reservaDTO.getReservasDiasSemana() == null || reservaDTO.getReservasDiasSemana().isEmpty()) {
                throw new IllegalArgumentException(
                        "Debe especificar al menos un día de la semana a reservar para el tipo de reserva por periodo.");
            }
            if (reservaDTO.getAnioCicloLectivo() == null) {
                throw new IllegalArgumentException(
                        "El año del ciclo lectivo es obligatorio para una reserva por periodo.");
            }
            if (reservaDTO.getAnioCicloLectivo() <= 0
                    || cicloLectivoService.obtenerCicloLectivoPorAnio(reservaDTO.getAnioCicloLectivo()) == null) {
                throw new IllegalArgumentException("El año del ciclo lectivo es inválido o no existe.");
            }
            if (reservaDTO.getPeriodoReserva() == null || reservaDTO.getPeriodoReserva().isEmpty()) {
                throw new IllegalArgumentException(
                        "El periodo de la reserva es obligatorio para una reserva por periodo.");
            }
            // Validar que el tipo de periodo sea válido (validación implícita en el método
            // fromString)
            PeriodoReserva.fromString(reservaDTO.getPeriodoReserva());
            if (reservaDTO.getReservasDia() != null && !reservaDTO.getReservasDia().isEmpty()) {
                throw new IllegalArgumentException(
                        "No debe proporcionar reservas por fechas para una reserva por periodo.");
            }
            // Validar cada ReservaDiaSemanaDTO
            Map<String, Integer> contadorDiasSemana = new HashMap<>();
            for (ReservaDiaSemanaDTO diaSemanaDTO : reservaDTO.getReservasDiasSemana()) {
                // Validar que el día de la semana sea válido (validación implícita en el método
                // convertirDiaSemana)
                FechaUtils.convertirDiaSemana(diaSemanaDTO.getDiaSemana());
                if (diaSemanaDTO.getDuracion() % 30 != 0) {
                    throw new IllegalArgumentException(
                            "La duración de la reserva debe ser múltiplo de 30 minutos (Ver día "
                                    + diaSemanaDTO.getDiaSemana() + ").");
                }
                if(diaSemanaDTO.getDuracion() > 300)
                    throw new IllegalArgumentException("La duración de la reserva no puede ser mayor a 5 horas (Ver día "
                            + diaSemanaDTO.getDiaSemana() + ").");
            
                if (!verificarHoraValida(diaSemanaDTO.getHoraInicio(), diaSemanaDTO.getDuracion())) {
                    throw new IllegalArgumentException(
                            "La duración de la reserva debe respetar los limites horarios, no puede pasar las 24hs.");
                }
                if (!FechaUtils.inLunesAViernes(diaSemanaDTO.getDiaSemana())) {
                    throw new IllegalArgumentException("Solo se pueden realizar reservas de Lunes a Viernes (Ver día "
                            + diaSemanaDTO.getDiaSemana() + ").");
                }
                if (aulaReservaService.obtenerAulaPorNumero(diaSemanaDTO.getNroAula()) == null) {
                    throw new AulaNotFoundException("El número de aula no corresponde a ningún aula existente (Ver día "
                            + diaSemanaDTO.getDiaSemana() + ", aula " + diaSemanaDTO.getNroAula() + ").");
                }
                contadorDiasSemana.put(diaSemanaDTO.getDiaSemana(),
                        contadorDiasSemana.getOrDefault(diaSemanaDTO.getDiaSemana(), 0) + 1);
            }
            // Verificar si hay dias de la semana repetidos
            for (Map.Entry<String, Integer> entry : contadorDiasSemana.entrySet()) {
                if (entry.getValue() > 1) {
                    throw new DuplicatedDayException(
                            "No se puede reservar más de una vez el mismo día (Ver día " + entry.getKey() + ").");
                }
            }
        } else {
            throw new IllegalArgumentException("Tipo de reserva desconocido.");
        }
    }

    /**
     * Genera y asigna una instancia de ReservaDia por cada día a reservar.
     * Utilizado para registrar reservas esporádicas.
     *
     * @param reserva    La reserva a la que se le asignarán los días.
     * @param reservaDTO El objeto de transferencia de datos que contiene la
     *                   información de la reserva.
     */
    private void asignarReservasDiaEsporadica(Reserva reserva, ReservaDTO reservaDTO) {
        reserva.setReservasDia(new ArrayList<>());

        for (ReservaDiaDTO reservaDiaDTO : reservaDTO.getReservasDia()) {
            ReservaDia reservaDia = new ReservaDia();
            reservaDia.setFecha(reservaDiaDTO.getFecha());
            reservaDia.setHoraInicio(reservaDiaDTO.getHoraInicio());
            reservaDia.setDuracion(reservaDiaDTO.getDuracion());

            // Recuperamos y asignamos la correspondiente aula
            Aula aula = aulaReservaService.obtenerAulaPorNumero(reservaDiaDTO.getNroAula());
            reservaDia.setAula(aula);

            reservaDia.setReserva(reserva);
            reserva.getReservasDia().add(reservaDia);
        }
    }

    /**
     * Genera y asigna una instancia de ReservaDia por cada día a reservar del
     * periodo seleccionado (cuatrimestre o año).
     * Utilizado para registrar reservas por periodo.
     *
     * @param reserva    La reserva a la que se le asignarán los días.
     * @param reservaDTO El objeto de transferencia de datos que contiene la
     *                   información de la reserva.
     */
    private void asignarReservasDiaPorPeriodo(Reserva reserva, ReservaDTO reservaDTO) {
        PeriodoReserva periodoReserva = PeriodoReserva.fromString(reservaDTO.getPeriodoReserva());

        // Obtenemos y asignamos el o los cuatrimestres correspondientes al año de la
        // reserva
        List<Cuatrimestre> cuatrimestres = cuatrimestreService.buscarPorCicloLectivo(reservaDTO.getAnioCicloLectivo());
        reserva.setCuatrimestres(new ArrayList<>());
        if (PeriodoReserva.PRIMER_CUATRIMESTRE.equals(periodoReserva)) {
            reserva.getCuatrimestres().add(cuatrimestres.get(0));
        } else if (PeriodoReserva.SEGUNDO_CUATRIMESTRE.equals(periodoReserva)) {
            reserva.getCuatrimestres().add(cuatrimestres.get(1));
        } else if (PeriodoReserva.ANUAL.equals(periodoReserva)) {
            reserva.getCuatrimestres().add(cuatrimestres.get(0));
            reserva.getCuatrimestres().add(cuatrimestres.get(1));
        }

        // Asignamos las reservas por cada día de la semana
        reserva.setReservasDia(new ArrayList<>());
        for (ReservaDiaSemanaDTO reservaDiaSemanaDTO : reservaDTO.getReservasDiasSemana()) {
            // Obtenemos las fechas del día de la semana a reservar
            String nombreDia = reservaDiaSemanaDTO.getDiaSemana();
            List<LocalDate> fechas = new ArrayList<>();
            for (Cuatrimestre cuatrimestre : reserva.getCuatrimestres()) {
                fechas.addAll(FechaUtils.obtenerDiasDeLaSemanaEnRango(
                        cuatrimestre.getFechaInicio(),
                        cuatrimestre.getFechaFinal(),
                        nombreDia));
            }

            for (LocalDate fecha : fechas) {
                ReservaDia reservaDia = new ReservaDia();
                reservaDia.setFecha(fecha);
                reservaDia.setHoraInicio(reservaDiaSemanaDTO.getHoraInicio());
                reservaDia.setDuracion(reservaDiaSemanaDTO.getDuracion());

                // Recuperar y asignar la correspondiente aula
                Aula aula = aulaReservaService.obtenerAulaPorNumero(reservaDiaSemanaDTO.getNroAula());
                reservaDia.setAula(aula);

                reservaDia.setReserva(reserva);
                reserva.getReservasDia().add(reservaDia);
            }
        }
    }

    public List<Reserva> listarReservaPorDia(Date fecha, String tipoAula, Integer numAula) {
        // Validar que la fecha no sea nula
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria.");
        }

        // Validar que no se busque por tipo de aula y numero a la vez
        if (tipoAula != null && numAula != null) {
            throw new IllegalArgumentException("No se puede buscar por tipo de aula y número de aula al mismo tiempo.");
        }

        // Validar que el tipo de aula sea válido si se proporciona
        if (tipoAula != null) {
            TipoAula.validateTipoAula(tipoAula);
        }

        // Validar que el número de aula sea válido si se proporciona
        if (numAula != null && numAula <= 0) {
            throw new IllegalArgumentException("El número de aula debe ser un número positivo.");
        }

        List<Reserva> reservas = reservaDao.listarReservaPorDia(fecha, tipoAula, numAula);

        return reservas;
    }

    public List<Reserva> listarReservaPorCurso(String curso) {
        // Validar que el curso no sea nulo ni vacío
        if (curso == null || curso.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del curso es obligatorio.");
        }

        // Obtener las reservas por curso
        List<Reserva> reservas = reservaDao.listarReservaPorCurso(curso);

        return reservas;
    }

    public Reserva obtenerReservaPorId(String idReserva) {
        // Validar que el ID de la reserva no sea nulo ni vacío
        if (idReserva == null || idReserva.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID de la reserva es obligatorio.");
        }

        // Obtener la reserva por ID
        Reserva reserva = reservaDao.obtenerReservaPorId(idReserva);

        return reserva;
    }

    private List<LocalDate> obtenerFechasDelPeriodo(int anioCicloLectivo, String diaSemana, String periodoReserva) {

        // Determinar las fechas de inicio y fin según el período
        Date fechaInicio;
        Date fechaFin;
        Cuatrimestre cuatrimestre1 = cuatrimestreService.obtenerPrimerCuatrimestre(anioCicloLectivo);
        Cuatrimestre cuatrimestre2 = cuatrimestreService.obtenerSegundoCuatrimestre(anioCicloLectivo);

        switch (periodoReserva.toUpperCase()) {
            case "PRIMER_CUATRIMESTRE":
                fechaInicio = cuatrimestre1.getFechaInicio();
                ;
                fechaFin = cuatrimestre1.getFechaFinal();
                ;
                ;
                break;
            case "SEGUNDO_CUATRIMESTRE":
                fechaInicio = cuatrimestre2.getFechaInicio();
                fechaFin = cuatrimestre2.getFechaFinal();
                break;
            case "ANUAL":
                fechaInicio = cuatrimestre1.getFechaInicio();
                fechaFin = cuatrimestre2.getFechaFinal();
                break;
            default:
                throw new IllegalArgumentException("El período de reserva no es válido: " + periodoReserva);
        }

        List<LocalDate> fechas = new ArrayList<>();

        // Crear un Calendar para iterar las fechas
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fechaInicio);

        while (!calendario.getTime().after(fechaFin)) {
            // Verificar si el día de la semana coincide
            DayOfWeek diaSemanaConvertido = FechaUtils.convertirDiaSemana(diaSemana);
            int diaSemanaCalendar = (calendario.get(Calendar.DAY_OF_WEEK) + 5) % 7 + 1; // Convert to match DayOfWeek values
            if (diaSemanaConvertido.getValue() == diaSemanaCalendar) {
                LocalDate fecha = calendario.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                fechas.add(fecha);
            }

            // Avanzar un día
            calendario.add(Calendar.DAY_OF_MONTH, 1);
        }
        return fechas;
    }

    /*
     * Valida que las aulas de las reservas este disponibles al momento de
     * realizarla
     */

    private void validarDisponibilidadAulas(ReservaDTO reservaDTO) {
        if (reservaDTO.getTipoReserva() != null
                && reservaDTO.getTipoReserva().equals(TipoReserva.ESPORADICA.toString())) {
            List<ReservaDiaDTO> reservasDia = reservaDTO.getReservasDia();
            reservasDia.forEach(reservaDia -> {
                List<ReservaDia> reservas = reservaDao.obtenerReservasDiaPorAulayFecha(reservaDia.getNroAula(),
                        reservaDia.getFecha());
                reservas.forEach(reserva -> {
                    // verificar que no se choquen con las restricciones y sino lanzar una excepcion
                    long horaInicioResExistente = reserva.getHoraInicio().getTime();
                    long horaFinResExistente = horaInicioResExistente + reserva.getDuracion() * 60000;
                    long horaInicioNuevo = reservaDia.getHoraInicio().getTime();
                    long horaFinNuevo = horaInicioNuevo + reservaDia.getDuracion() * 60000;
                    if (horaInicioResExistente < horaFinNuevo && horaInicioNuevo < horaFinResExistente)
                        throw new AulaNotFoundException("El aula numero: " + reservaDia.getNroAula()
                                + " que intenta reservar ya se encuentra ocupada en la fecha y hora seleccionada (Ver fecha " + reservaDia.getFecha() + ").");
                });
            });
        } else {
            if (reservaDTO.getTipoReserva() != null
                    && reservaDTO.getTipoReserva().equals(TipoReserva.POR_PERIODO.toString())) {
                List<ReservaDiaSemanaDTO> reservasDias = reservaDTO.getReservasDiasSemana();

                reservasDias.forEach(reservaDiaSemana -> {
                    // Obtener las fechas dentro del rango especificado
                    List<LocalDate> fechasDelPeriodo = obtenerFechasDelPeriodo(reservaDTO.getAnioCicloLectivo(),
                            reservaDiaSemana.getDiaSemana(), reservaDTO.getPeriodoReserva());

                    fechasDelPeriodo.forEach(fecha -> {
                        // Verificar si ya existen reservas en el aula para cada fecha calculada
                        List<ReservaDia> reservas = reservaDao
                                .obtenerReservasDiaPorAulayFecha(reservaDiaSemana.getNroAula(), fecha);

                        reservas.forEach(reserva -> {
                            // Validar colisiones en horarios
                            long horaInicioResExistente = reserva.getHoraInicio().getTime();
                            long horaFinResExistente = horaInicioResExistente + reserva.getDuracion() * 60000;
                            long horaInicioNuevo = reservaDiaSemana.getHoraInicio().getTime();
                            long horaFinNuevo = horaInicioNuevo + reservaDiaSemana.getDuracion() * 60000;

                            if (horaInicioResExistente < horaFinNuevo && horaInicioNuevo < horaFinResExistente) {
                                throw new AulaNotFoundException("El aula numero: " + reservaDiaSemana.getNroAula()
                                        + " que intenta reservar ya se encuentra ocupada en la fecha y hora seleccionada (Ver día " + reservaDiaSemana.getDiaSemana() + ").");
                            }
                        });
                    });
                });
            }
        }
    }

}
