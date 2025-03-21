package com.tpdisenio.gSM3C.services;

import com.tpdisenio.gSM3C.dao.AulaDAO;
import com.tpdisenio.gSM3C.domain.Aula;
import com.tpdisenio.gSM3C.domain.AulaDisponibilidad;
import com.tpdisenio.gSM3C.domain.Cuatrimestre;
import com.tpdisenio.gSM3C.domain.Reserva;
import com.tpdisenio.gSM3C.dto.ReservaSolapamientoDTO;
import com.tpdisenio.gSM3C.dto.AulaResponseDTO;
import com.tpdisenio.gSM3C.dto.BloqueHorarioDTO;
import com.tpdisenio.gSM3C.dto.ObtenerDisponibilidadAulasDTO;
import com.tpdisenio.gSM3C.dto.ReservaResponseDTO;
import com.tpdisenio.gSM3C.enums.PeriodoReserva;
import com.tpdisenio.gSM3C.enums.TipoAula;
import com.tpdisenio.gSM3C.enums.TipoReserva;
import com.tpdisenio.gSM3C.exception.DuplicatedDayException;
import com.tpdisenio.gSM3C.exception.NoAvailabilityException;
import com.tpdisenio.gSM3C.utils.DTOConverter;
import com.tpdisenio.gSM3C.utils.FechaUtils;

import jakarta.transaction.Transactional;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AulaService {

    @Autowired
    @Qualifier("aulaSQLDao")
    private AulaDAO<Aula> aulaDao;

    @Autowired
    private AulaReservaService aulaReservaService;

    @Autowired
    private CuatrimestreService cuatrimestreService;

    @Transactional
    public List<BloqueHorarioDTO> obtenerDisponibilidadAulas(ObtenerDisponibilidadAulasDTO dto) {

        // Validar los datos del DTO
        validarObtenerDisponibilidadAulasDTO(dto);

        TipoAula tipoAula = TipoAula.valueOf(dto.getTipoAula());
        Integer capacidad = dto.getCapacidad();
        TipoReserva tipoReserva = TipoReserva.fromString(dto.getTipoReserva());

        // BloqueHorarioDTOs que se devolverán como resultado
        List<BloqueHorarioDTO> bloquesResultado = new ArrayList<>();
        
        List<BloqueHorarioDTO> bloquesAReservar = new ArrayList<>();
        if(tipoReserva == TipoReserva.POR_PERIODO) {
            generarBloquesHorarioPorPeriodo(dto, bloquesAReservar);
        }else {
            bloquesAReservar = dto.getDiasReserva();
        }

        // Evaluar la disponibilidad de aulas para cada día y horario solicitado
        for (BloqueHorarioDTO bloqueActual : bloquesAReservar) {
            // Obtener información de si las aulas están libres o reservadas y en qué horarios
            List<AulaDisponibilidad> resultados = aulaDao.obtenerDisponibilidadAulas(tipoAula, bloqueActual.getFecha(), capacidad);

            // Verificar si se obtuvieron resultados
            if (resultados.isEmpty())
                throw new NoAvailabilityException("No se obtuvieron resultados de disponibilidad para las características solicitadas. Asegúrese de que realmente existan aulas del tipo y capacidad especificados.");

            // Filtrar las aulas disponibles a partir de los resultados obtenidos
            List<AulaDisponibilidad> aulasDisponibles = filtrarAulasDisponibles(resultados, bloqueActual);

            // Verificar si hay aulas disponibles
            if (aulasDisponibles.size() > 0) {
                // Eliminar duplicados y ordenar por capacidad (ASC) para obtener las 3 aulas con menor capacidad
                aulasDisponibles = aulasDisponibles.stream()
                    .collect(Collectors.toMap(AulaDisponibilidad::getNroAula, ad -> ad, (ad1, ad2) -> ad1))
                    .values()
                    .stream()
                    .sorted((a1, a2) -> a1.getCapacidad().compareTo(a2.getCapacidad())) // Ordenar por capacidad (ASC)
                    .limit(3) // Nos quedamos con las primeras 3
                    .collect(Collectors.toList());

                // Recuperar los objetos Aula asociados a las aulas disponibles
                List<AulaResponseDTO> aulaDTOs = new ArrayList<>();
                for (AulaDisponibilidad infoDisponibilidad : aulasDisponibles) {
                    Aula aula = aulaDao.buscarPorNroAula(infoDisponibilidad.getNroAula());
                    AulaResponseDTO aulaDto = DTOConverter.convertirAulaResponseDTO(aula);
                    aulaDTOs.add(aulaDto);
                }
                
                // Crear un nuevo BloqueHorarioDTO que será devuelto al cliente
                bloquesResultado.add(crearBloqueHorarioResultado(bloqueActual, true, aulaDTOs, null));
            } else {
                // No hay aulas disponibles, buscar las reservas que producen solapamiento
                
                // Obtener el solapamiento mínimo y su(s) aula(s) asociada(s) a partir de los resultados obtenidos
                Map<AulaDisponibilidad, Double> aulasMinimoSolapamiento = obtenerAulasConMenorSolapamiento(resultados, bloqueActual);

                // Recuperar y guardar la información del aula y reserva asociada con el solapamiento mínimo
                List<ReservaSolapamientoDTO> reservasSolapadas = new ArrayList<>();
                for (Map.Entry<AulaDisponibilidad, Double> entry : aulasMinimoSolapamiento.entrySet()) {
                    AulaDisponibilidad infoDisponibilidad = entry.getKey();
                    Double solapamiento = entry.getValue();
                    
                    // Recuperar el objeto Aula asociado
                    Aula aula = aulaDao.buscarPorNroAula(infoDisponibilidad.getNroAula());
                    AulaResponseDTO aulaDto = DTOConverter.convertirAulaResponseDTO(aula);

                    // Recuperar el objeto Reserva asociado
                    Reserva reserva = aulaReservaService.obtenerReservaPorId(infoDisponibilidad.getIdReserva());
                    ReservaResponseDTO reservaDto = DTOConverter.convertirReservaResponseDTO(reserva);
                    
                    // Crear objeto ReservaSolapamientoDTO para luego agregarlo al newBloqueHorario
                    ReservaSolapamientoDTO reservaSolapamiento = new ReservaSolapamientoDTO();
                    reservaSolapamiento.setAula(aulaDto);
                    reservaSolapamiento.setReserva(reservaDto);
                    reservaSolapamiento.setTiempoSolapamiento(solapamiento);
                    reservaSolapamiento.setFecha(bloqueActual.getFecha());
                    reservaSolapamiento.setHoraInicio(infoDisponibilidad.getHoraInicio());
                    reservaSolapamiento.setDuracion(infoDisponibilidad.getDuracion());
                    reservasSolapadas.add(reservaSolapamiento);
                }

                // Crear un nuevo BloqueHorarioDTO que será devuelto al cliente
                bloquesResultado.add(crearBloqueHorarioResultado(bloqueActual, false, null, reservasSolapadas));
            }
        }

        // Resumir la información de los bloquesHorario para cada día de la semana
        if(tipoReserva == TipoReserva.POR_PERIODO){
            bloquesResultado = resumirBloquesHorario(bloquesResultado);
        }

        return bloquesResultado;
    }

    /**
     * Realiza las validaciones de ObtenerDisponibilidadAulasDTO que no se realizan en su clase.
     * @param dto
     */
    private void validarObtenerDisponibilidadAulasDTO(ObtenerDisponibilidadAulasDTO dto) {
        // Validar que el tipo de aula sea un valor válido
        TipoAula.validateTipoAula(dto.getTipoAula());
        
        // Validar el tipo de reserva y obtener el enum correspondiente
        TipoReserva tipoReserva = TipoReserva.fromString(dto.getTipoReserva());

        // Validar atributos simples para reservas por periodo
        if(tipoReserva == TipoReserva.POR_PERIODO){
            // Validar el periodo de reserva
            PeriodoReserva.validatePeriodoReserva(dto.getPeriodoReserva());

            // Validar el año del ciclo lectivo
            if (dto.getAnioCicloLectivo() == null)
                throw new IllegalArgumentException("El año del ciclo lectivo es obligatorio para reservas por periodo.");
            
            // Validar que el año del ciclo lectivo sea un valor válido
            if (dto.getAnioCicloLectivo() < LocalDate.now().getYear())
                throw new IllegalArgumentException("El año del ciclo lectivo debe ser mayor o igual al actual.");
        }

        // Validar cada bloque horario
        Map<LocalDate, Integer> contadorFechas = new HashMap<>();
        Map<String, Integer> contadorDiasSemana = new HashMap<>();
        for (BloqueHorarioDTO bloqueHorario : dto.getDiasReserva()) {

            if(tipoReserva == TipoReserva.ESPORADICA){
                // Validar que la fecha exista y sea futura
                if (bloqueHorario.getFecha() == null)
                    throw new IllegalArgumentException("La fecha de la reserva es obligatoria.");
                    
                // Validar que la fecha sea futura
                if (bloqueHorario.getFecha().isBefore(LocalDate.now().plusDays(1)))
                    throw new IllegalArgumentException("La fecha de la reserva debe ser futura (Ver día "+bloqueHorario.getFecha()+").");

                // Contar cantidad de veces que se repite una fecha
                contadorFechas.put(bloqueHorario.getFecha(), contadorFechas.getOrDefault(bloqueHorario.getFecha(), 0) + 1);
            }else if(tipoReserva == TipoReserva.POR_PERIODO){
                // Validar que la fecha sea nula
                if (bloqueHorario.getDiaSemana() == null || bloqueHorario.getDiaSemana().isEmpty())
                    throw new IllegalArgumentException("El día de la semana a reservar es obligatorio.");

                // Validar día de la semana
                if(!FechaUtils.inLunesAViernes(bloqueHorario.getDiaSemana()))
                    throw new IllegalArgumentException("El día de la semana a reservar debe ser un día laborable (Lunes a Viernes)."); 

                // Contar cantidad de veces que se repite un día de la semana
                contadorDiasSemana.put(bloqueHorario.getDiaSemana(), contadorDiasSemana.getOrDefault(bloqueHorario.getDiaSemana(), 0) + 1);
            }

            // Validar que cada fecha o día de la semana (el que corresponda) no se repita más de una vez
            if(tipoReserva == TipoReserva.ESPORADICA){
                // Verificar si hay fechas repetidas
                for (Map.Entry<LocalDate, Integer> entry : contadorFechas.entrySet()) {
                    if (entry.getValue() > 1) {
                        throw new DuplicatedDayException("No se puede reservar más de una vez en la misma fecha (Ver fecha " + entry.getKey() + ").");
                    }
                }
            }else if(tipoReserva == TipoReserva.POR_PERIODO){
                // Verificar si hay días de la semana repetidos
                for (Map.Entry<String, Integer> entry : contadorDiasSemana.entrySet()) {
                    if (entry.getValue() > 1) {
                        throw new DuplicatedDayException("No se puede reservar más de una vez en el mismo día de la semana (Ver día " + entry.getKey() + ").");
                    }
                }
            }

            // Validar que la duración sea múltiplo de 30 minutos
            if (bloqueHorario.getDuracion() % 30 != 0)
                throw new IllegalArgumentException("La duración de la reserva debe ser múltiplo de 30 minutos (Ver día "+bloqueHorario.getFecha()+").");
            if(bloqueHorario.getDuracion() > 300)
                throw new IllegalArgumentException("La duración de la reserva no puede ser mayor a 5 horas.");
            if(!verificarHoraValida(bloqueHorario.getHoraInicio(), bloqueHorario.getDuracion())){
                throw new IllegalArgumentException("La duración de la reserva debe respetar los limites horarios, no puede pasar las 24hs.");
            }
        }
        
    }

    private void generarBloquesHorarioPorPeriodo(ObtenerDisponibilidadAulasDTO dto , List<BloqueHorarioDTO> nuevosBloques) {
        PeriodoReserva periodoReserva = PeriodoReserva.fromString(dto.getPeriodoReserva());
        List<Cuatrimestre> cuatrimestres = cuatrimestreService.buscarPorCicloLectivo(dto.getAnioCicloLectivo());
        
        // Filtrar los cuatrimestres según el periodo de reserva (Si es ANUAL se omite el filtro)
        if(periodoReserva == PeriodoReserva.PRIMER_CUATRIMESTRE)
            cuatrimestres = Collections.singletonList(cuatrimestres.get(0));
        else if(periodoReserva == PeriodoReserva.SEGUNDO_CUATRIMESTRE)
            cuatrimestres = Collections.singletonList(cuatrimestres.get(1));
        
        // Por cada día de la semana, obtener las fechas y generar los bloquesHorarios correspondientes para cada fecha
        for (BloqueHorarioDTO bloqueHorario : dto.getDiasReserva()) {
            String nombreDiaSemana = bloqueHorario.getDiaSemana();
            List<LocalDate> fechasDiaSemana = new ArrayList<>();

            // Obtener las fechas del día de la semana
            for (Cuatrimestre cuatrimestre : cuatrimestres) {
                fechasDiaSemana.addAll(FechaUtils.obtenerDiasDeLaSemanaEnRango(
                    cuatrimestre.getFechaInicio(), 
                    cuatrimestre.getFechaFinal(), 
                    nombreDiaSemana
                    ));
            }

            // Crear bloquesHorario para las fechas obtenidas
            for (LocalDate fecha : fechasDiaSemana) {
                BloqueHorarioDTO bloque = new BloqueHorarioDTO();
                bloque.setFecha(fecha);
                bloque.setHoraInicio(bloqueHorario.getHoraInicio());
                bloque.setDuracion(bloqueHorario.getDuracion());
                bloque.setDiaSemana(bloqueHorario.getDiaSemana());
                nuevosBloques.add(bloque);
            }
        }
    }

    /**
     * Filtra las aulas disponibles, eliminando aquellas que están asociadas a reservas que producen solapamiento.
     * @param disponibilidades Lista de objetos AulaDisponibilidad que contiene la información de disponibilidad de las aulas.
     * @param bloqueHorario Objeto BloqueHorarioDTO que contiene la fecha, hora de inicio y duración del intervalo de tiempo a verificar.
     * @return Lista de objetos AulaDisponibilidad que representan las aulas disponibles.
     */
    private List<AulaDisponibilidad> filtrarAulasDisponibles(List<AulaDisponibilidad> disponibilidades, BloqueHorarioDTO bloqueHorario) {
        // Agrupar la información de diponibilidad por número de aula
        Map<Integer, List<AulaDisponibilidad>> disponibilidadPorNroAula = disponibilidades.stream()
            .collect(Collectors.groupingBy(AulaDisponibilidad::getNroAula));
        
        /**
         * Descartamos las aulas que tienen reservas asociadas que producen solapamiento.
         * Si el aula tiene aunque sea una reserva asociada que produce solapamiento, 
         * no está disponible y se eliminan del Map todas sus apariciones.
         **/
        for (AulaDisponibilidad infoDisponibilidad : disponibilidades) {
            if (infoDisponibilidad.tieneReservaAsociada() && infoDisponibilidad.haySolapamiento(bloqueHorario.getFecha(), bloqueHorario.getHoraInicio(), bloqueHorario.getDuracion())) {
                disponibilidadPorNroAula.remove(infoDisponibilidad.getNroAula());
            }
        }

        // Convertir el mapa a una lista
        List<AulaDisponibilidad> aulasDisponibles = disponibilidadPorNroAula.values().stream()
            .flatMap(List::stream)
            .collect(Collectors.toList());

        return aulasDisponibles;
    }

    /**
     * Obtiene un mapa de disponibilidades de aulas con el menor solapamiento posible respecto a un bloque horario dado.
     * 
     * @param disponibilidades Lista de objetos AulaDisponibilidad que contiene la información de disponibilidad de las aulas.
     * @param bloqueHorario Objeto BloqueHorarioDTO que contiene la fecha, hora de inicio y duración del intervalo de tiempo a verificar.
     * @return Map que contiene las disponibilidades de aulas que presentan el menor solapamiento posible junto con la duración del mismo.
     *         Las aulas están ordenadas por cantidad de horas solapadas de menor a mayor.
     *         Solo incluye las aulas que tienen el mismo valor mínimo de solapamiento.
     */
    private Map<AulaDisponibilidad, Double> obtenerAulasConMenorSolapamiento(List<AulaDisponibilidad> disponibilidades, BloqueHorarioDTO bloqueHorario) {
        // Mapa que contiene las disponibilidades que producen solapamiento y la duración del mismo
        Map<AulaDisponibilidad, Double> solapamientoMap = new HashMap<>();

        // Calcular los solapamientos de cada aula con la reserva solicitada
        for (AulaDisponibilidad infoDisponibilidad : disponibilidades) {
            if (infoDisponibilidad.tieneReservaAsociada()) {
                Double duracionSolapamiento = infoDisponibilidad.calcularSolapamiento(bloqueHorario.getFecha(), bloqueHorario.getHoraInicio(), bloqueHorario.getDuracion());
                if (duracionSolapamiento > 0) {
                    solapamientoMap.put(infoDisponibilidad, duracionSolapamiento);
                }
            }
        }

        // Ordenar aulas por cantidad de horas solapadas
        Map<AulaDisponibilidad, Double> solapamientoMapOrdenado = solapamientoMap.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        // Obtener el menor solapamiento de todos
        OptionalDouble minimoSolapamiento = solapamientoMapOrdenado.values().stream().mapToDouble(Double::doubleValue).min();
        Double minimoSolapamientoValue = minimoSolapamiento.getAsDouble();
        
        // Retornar solo las aulas que tienen el menor valor de solapamiento
        return solapamientoMap.entrySet().stream()
            .filter(entry -> entry.getValue().equals(minimoSolapamientoValue))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Resumir la información de los bloquesHorario para cada día de la semana.
     * @param bloquesResultado Lista de BloqueHorarioDTO que contiene la información de disponibilidad para cada fecha a reservar.
     * @return Lista de BloqueHorarioDTO que contiene la información resumida de los bloquesHorario para cada día de la semana.
     */
    private List<BloqueHorarioDTO> resumirBloquesHorario(List<BloqueHorarioDTO> bloquesResultado) {
        List<BloqueHorarioDTO> bloquesHorarioFinal = new ArrayList<>();

        // Agrupar los BloquesHorario por día de la semana
        Map<String, List<BloqueHorarioDTO>> bloquesPorDiaSemana = bloquesResultado.stream()
            .collect(Collectors.groupingBy(BloqueHorarioDTO::getDiaSemana));

        // Resumir la información de los bloquesHorario para cada día de la semana
        for (Map.Entry<String, List<BloqueHorarioDTO>> entry : bloquesPorDiaSemana.entrySet()) {
            String diaSemana = entry.getKey();
            List<BloqueHorarioDTO> bloquesDiaSemana = entry.getValue();

            // Crear un nuevo BloqueHorarioDTO con la información resumida del día de la semana
            BloqueHorarioDTO bloqueHorarioFinal = new BloqueHorarioDTO();
            bloqueHorarioFinal.setDiaSemana(diaSemana);
            bloqueHorarioFinal.setHoraInicio(bloquesDiaSemana.get(0).getHoraInicio());
            bloqueHorarioFinal.setDuracion(bloquesDiaSemana.get(0).getDuracion());

            // Obtener los bloques sin aulas disponibles para el día de la semana
            List<BloqueHorarioDTO> bloquesNoDisponibles = bloquesDiaSemana.stream()
                .filter(bloque -> !bloque.getDisponible())
                .collect(Collectors.toList());

            // Si hay bloques no disponibles, se resume la información de las reservas solapadas
            // Si no hay bloques no disponibles, se resume la información de las aulas disponibles
            if(!bloquesNoDisponibles.isEmpty()){
                // Obtener todas las reservas solapadas de los bloques no disponibles
                List<ReservaSolapamientoDTO> todasReservasSolapadas = new ArrayList<>();
                for (BloqueHorarioDTO bloqueNoDisponible : bloquesNoDisponibles) {
                    todasReservasSolapadas.addAll(bloqueNoDisponible.getReservasSolapadas());
                }

                // Evitamos repetición de información para reservas POR_PERIODO
                todasReservasSolapadas = filtrarReservasSolapadasPorPeriodo(todasReservasSolapadas);

                // Obtener el menor solapamiento de todas las reservas solapadas
                OptionalDouble menorSolapamientoOpt = todasReservasSolapadas.stream()
                    .mapToDouble(ReservaSolapamientoDTO::getTiempoSolapamiento)
                    .min();
                    
                // Filtrar las reservas solapadas que tengan el menor valor de solapamiento
                double menorSolapamiento = menorSolapamientoOpt.getAsDouble();
                List<ReservaSolapamientoDTO> reservasMenorSolapamiento = todasReservasSolapadas.stream()
                    .filter(reservaSolapada -> reservaSolapada.getTiempoSolapamiento() == menorSolapamiento)
                    .collect(Collectors.toList());

                // Agregar las reservas con menor solapamiento al bloqueHorario del día de la semana
                bloqueHorarioFinal.setReservasSolapadas(reservasMenorSolapamiento);
                bloqueHorarioFinal.setDisponible(false);
            }else{
                // Como no hay bloques no disponibles, se supone que en todos los bloques hay aulas disponibles
                            
                // Obtener las aulas disponibles de cada bloque
                List<List<AulaResponseDTO>> aulasDisponiblesPorBloque = bloquesDiaSemana.stream()
                    .map(BloqueHorarioDTO::getAulasDisponibles)
                    .collect(Collectors.toList());

                // Encontrar las aulas que aparecen en todos los bloques
                List<AulaResponseDTO> aulasComunes = aulasDisponiblesPorBloque.stream()
                    .skip(1)
                    .collect(() -> new ArrayList<>(aulasDisponiblesPorBloque.get(0)), List::retainAll, List::retainAll);
                
                // Verificar si hay aulas comunes
                if(aulasComunes.isEmpty()){
                    throw new NoAvailabilityException("No hay aulas disponibles que cumplan los criterios especificados para el día "+diaSemana+".");
                }

                // Agregar las aulas comunes al bloqueHorario del día de la semana
                bloqueHorarioFinal.setAulasDisponibles(aulasComunes);
                bloqueHorarioFinal.setDisponible(true);
            }
            
            bloquesHorarioFinal.add(bloqueHorarioFinal);
        }

        return bloquesHorarioFinal;
    }

    /**
     * Crea un nuevo objeto de BloqueHorarioDTO, copia la información general del BloqueHorarioDTO enviado por el cliente
     * y agrega la información de aulas disponibles y reservas solapadas.
     * @param bloqueActual BloqueHorarioDTO enviado por el cliente
     * @param disponible Indica si hay aulas disponibles para el día y horario solicitado
     * @param aulaDTOs Lista de aulas disponibles (en caso de haber)
     * @param reservasSolapadas Lista de ReservaSolapamientoDTO que contiene las reservas solapadas (en caso de haber)
     * @return Nuevo objeto BloqueHorarioDTO con la información de disponibilidad de aulas y reservas solapadas.
     */
    private BloqueHorarioDTO crearBloqueHorarioResultado(BloqueHorarioDTO bloqueActual, Boolean disponible, List<AulaResponseDTO> aulaDTOs, List<ReservaSolapamientoDTO> reservasSolapadas) {
        BloqueHorarioDTO bloque = new BloqueHorarioDTO();
        bloque.setFecha(bloqueActual.getFecha());
        bloque.setDiaSemana(bloqueActual.getDiaSemana());
        bloque.setHoraInicio(bloqueActual.getHoraInicio());
        bloque.setDuracion(bloqueActual.getDuracion());
        bloque.setDisponible(disponible);
        bloque.setAulasDisponibles(aulaDTOs);
        bloque.setReservasSolapadas(reservasSolapadas);
        return bloque;
    }

    /**
     * Filtra las reservas solapadas para evitar la repetición de información.
     * Si una reserva es de tipo POR_PERIODO, se mantiene solo una instancia de ReservaSolapamientoDTO.
     * Si una reserva es de tipo ESPORADICA, se mantienen todas las instancias de ReservaSolapamientoDTO.
     * 
     * @param todasReservasSolapadas Lista de todas las reservas solapadas de un mismo día de la semana.
     * @return Lista filtrada de ReservaSolapamientoDTO.
     */
    private List<ReservaSolapamientoDTO> filtrarReservasSolapadasPorPeriodo(List<ReservaSolapamientoDTO> reservasSolapadas) {
        // Agrupamos los ReservaSolapamientoDTOs por id de reserva.
        Map<String, List<ReservaSolapamientoDTO>> reservasSolapadasPorId = reservasSolapadas.stream()
            .collect(Collectors.groupingBy(reserva -> reserva.getReserva().getIdReserva()));
        
        List<ReservaSolapamientoDTO> newReservasSolapadas = new ArrayList<>();
        for (Map.Entry<String, List<ReservaSolapamientoDTO>> entry : reservasSolapadasPorId.entrySet()) {
            List<ReservaSolapamientoDTO> reservas = entry.getValue(); // Reservas solapadas con el mismo id
            TipoReserva tipoReserva = reservas.get(0).getReserva().getTipoReserva();
            
            if (tipoReserva == TipoReserva.POR_PERIODO) {
                // Si la solapada es POR_PERIODO, se deja solo una reserva (para no repetir información)
                ReservaSolapamientoDTO primerReserva = reservas.get(0);
                primerReserva.setFecha(null);
                newReservasSolapadas.add(primerReserva);
            } else {
                // Si la solapada es ESPORADICA, se dejan todas las reservas
                newReservasSolapadas.addAll(reservas);
            }
        }
        return newReservasSolapadas;
    }

    public Aula obtenerAulaPorNumero(Integer nroAula) {
        return aulaDao.buscarPorNroAula(nroAula);
    }

    public Aula modificarAula(Aula aula) {
       if (aula == null || aula.getNroAula() == null) {
        throw new IllegalArgumentException("El aula o el número de aula no pueden ser nulos.");
    }

    boolean actualizado = aulaDao.modificarAula(aula);
    if (actualizado) {
        return aula; // Retornamos el aula modificada
    } else {
        throw new RuntimeException("No se pudo modificar el aula.");
    }
    }

    public Boolean eliminarAula(Integer numAula) {
        if (numAula == null) {
        throw new IllegalArgumentException("El número de aula no puede ser nulo.");
    }

    return aulaDao.eliminarAula(numAula);
    }

    public List<Aula> buscarAulas(Integer numAula, String tipoAula, Integer capacidad) {
        if (numAula != null && numAula < 0) {
            throw new IllegalArgumentException("El número de aula no puede ser negativo.");
        }

        if (capacidad != null && capacidad < 0) {
            throw new IllegalArgumentException("La capacidad no puede ser negativa.");
        }

    // Llamar al DAO para obtener la lista de aulas que cumplen los criterios
        return aulaDao.buscarAulas(numAula, tipoAula, capacidad);

    }

    public List<Aula> obtenerAulasHabilitadas() {
        return aulaDao.obtenerAulasHabilitadas();
    }

    public  boolean verificarHoraValida(Time horaInicio, int duracionMinutos) {
        LocalTime inicio = horaInicio.toLocalTime();
        
        // Sumar la duración
        LocalTime horaFinal = inicio.plusMinutes(duracionMinutos);
        
        // Verificar si la hora final es menor a la medianoche
        return !horaFinal.isBefore(inicio);
    }
}