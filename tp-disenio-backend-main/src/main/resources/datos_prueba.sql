-- AULAS
INSERT IGNORE INTO `aula` (`nro_aula`, `aire_acondicionado`, `capacidad`, `habilitada`, `piso`, `tipo_pizarron`, `ubicacion`, `ventiladores`) VALUES
(10, b'1', 35, b'1', 1, 'Pizarra', 'Edificio B', b'1'),
(11, b'1', 40, b'1', 2, 'Pizarra', 'Edificio A', b'1'),
(12, b'0', 25, b'1', 1, 'Pizarra Blanca', 'Edificio B', b'0'),
(13, b'1', 50, b'1', 3, 'Pizarra', 'Edificio C', b'1'),
(14, b'0', 30, b'1', 2, 'Pizarra Blanca', 'Edificio A', b'0'),
(15, b'1', 35, b'1', 1, 'Pizarra', 'Edificio B', b'1'),
(20, b'1', 45, b'1', 2, 'Pizarra Blanca', 'Edificio C', b'1'),
(21, b'0', 20, b'1', 1, 'Pizarra', 'Edificio A', b'0'),
(22, b'1', 50, b'1', 3, 'Pizarra Blanca', 'Edificio B', b'1'),
(23, b'1', 40, b'1', 2, 'Pizarra', 'Edificio C', b'1'),
(24, b'0', 30, b'1', 1, 'Pizarra', 'Edificio A', b'0'),
(25, b'1', 35, b'1', 1, 'Pizarra Blanca', 'Edificio B', b'1'),
(26, b'1', 50, b'1', 3, 'Pizarra Blanca', 'Edificio A', b'1'),
(27, b'1', 45, b'1', 2, 'Pizarra', 'Edificio B', b'1'),
(28, b'0', 30, b'1', 1, 'Pizarra Blanca', 'Edificio C', b'0'),
(30, b'1', 40, b'1', 2, 'Pizarra', 'Edificio B', b'1'),
(31, b'0', 25, b'1', 1, 'Pizarra', 'Edificio A', b'0'),
(32, b'1', 35, b'1', 2, 'Pizarra Blanca', 'Edificio C', b'1');


INSERT IGNORE INTO `aula_informatica` (`cantidadpcs`, `proyector`, `nro_aula`) VALUES
(30, b'1', 20),
(25, b'1', 21),
(20, b'0', 22),
(35, b'1', 23),
(28, b'1', 24),
(32, b'1', 25),
(40, b'0', 26),
(45, b'1', 27),
(35, b'1', 28);


INSERT IGNORE INTO `aula_multimedios` (`computadora`, `proyector`, `televisor`, `nro_aula`) VALUES
(b'1', b'1', b'1', 10),
(b'1', b'1', b'0', 11),
(b'0', b'1', b'1', 12),
(b'1', b'1', b'0', 13),
(b'0', b'1', b'1', 14),
(b'1', b'0', b'1', 15);

INSERT IGNORE INTO `aula_sin_recursos_adicionales` (`nro_aula`) VALUES
(30),
(31),
(32);




-- CICLOS LECTIVOS Y CUATRIMESTRES
INSERT IGNORE INTO `ciclo_lectivo` (`anio`) VALUES ('2024'), ('2025');

INSERT IGNORE INTO `cuatrimestre` (`id_cuatrimestre`, `fecha_inicio`, `fecha_final`, `anio`) VALUES 
('1', '2024-03-11 14:35:35.000000', '2024-07-05 14:35:35.000000', '2024'), 
('2', '2024-08-06 14:35:35.000000', '2024-11-29 14:35:35.000000', '2024'),
('3', '2025-03-11 14:35:35.000000', '2025-07-05 14:35:35.000000', '2025'), 
('4', '2025-08-06 14:35:35.000000', '2025-11-29 14:35:35.000000', '2025');


-- Bedel para la creación de reservas de prueba
INSERT IGNORE INTO `administradores` (`id_usuario`, `apellido`, `habilitado`, `nombre`, `password`, `username`) VALUES
('admin1', 'Gonzalez', 1, 'Pedro', 'hashed_password_admin', 'pedrogonzalez');
INSERT IGNORE INTO `bedeles` (`id_usuario`, `apellido`, `habilitado`, `nombre`, `password`, `username`, `turno`, `registrado_por`) VALUES
('bedel1', 'Perez', 1, 'Juan', 'hashed_password', 'juanperez', 'TARDE', 'admin1');

-- Crear reservas esporádicas
INSERT IGNORE INTO `reserva` (`id_reserva`, `actividad_academica`, `apellido_docente`, `cantidad_alumnos`, `correo_docente`, `fecha_solicitud`, `nombre_docente`, `tipo_reserva`, `realizada_por`) VALUES
('reserva1', 'Matemáticas', 'Gomez', 30, 'gomez@example.com', '2024-01-15 10:00:00', 'Ana', 'ESPORADICA', 'bedel1'),
('reserva2', 'Física', 'Lopez', 25, 'lopez@example.com', '2024-01-16 11:00:00', 'Carlos', 'ESPORADICA', 'bedel1'),
('reserva3', 'Química', 'Martinez', 20, 'martinez@example.com', '2024-01-17 12:00:00', 'Maria', 'ESPORADICA', 'bedel1'),
('reserva4', 'Biología', 'Rodriguez', 35, 'rodriguez@example.com', '2024-01-18 09:00:00', 'Luis', 'ESPORADICA', 'bedel1'),
('reserva5', 'Historia', 'Fernandez', 40, 'fernandez@example.com', '2024-01-19 08:00:00', 'Laura', 'ESPORADICA', 'bedel1');

-- Crear días de reserva para cada reserva esporádica
INSERT IGNORE INTO `reserva_dia` (`id_reserva_dia`, `duracion`, `fecha`, `hora_inicio`, `nro_aula`, `id_reserva`) VALUES
('reserva_dia6', 90, '2025-04-21 10:00:00', '10:00:00', 10, 'reserva1'),
('reserva_dia7', 90, '2025-04-22 10:00:00', '10:00:00', 10, 'reserva1'),
('reserva_dia8', 90, '2025-04-23 10:00:00', '10:00:00', 11, 'reserva1'),
('reserva_dia9', 90, '2025-04-24 10:00:00', '10:00:00', 11, 'reserva1'),

('reserva_dia10', 120, '2025-04-21 11:00:00', '11:00:00', 13, 'reserva2'),
('reserva_dia11', 120, '2025-04-22 11:00:00', '11:00:00', 13, 'reserva2'),
('reserva_dia12', 120, '2025-04-23 11:00:00', '11:00:00', 13, 'reserva2'),
('reserva_dia13', 120, '2025-04-24 11:00:00', '11:00:00', 13, 'reserva2'),

('reserva_dia14', 60, '2025-04-21 12:00:00', '12:00:00', 14, 'reserva3'),
('reserva_dia15', 60, '2025-04-22 12:00:00', '12:00:00', 14, 'reserva3'),
('reserva_dia16', 60, '2025-04-23 12:00:00', '12:00:00', 14, 'reserva3'),
('reserva_dia17', 60, '2025-04-24 12:00:00', '12:00:00', 14, 'reserva3'),

('reserva_dia18', 90, '2025-04-21 09:00:00', '09:00:00', 12, 'reserva4'),
('reserva_dia19', 90, '2025-04-22 09:00:00', '09:00:00', 12, 'reserva4'),
('reserva_dia20', 90, '2025-04-23 09:00:00', '09:00:00', 12, 'reserva4'),
('reserva_dia21', 90, '2025-04-24 09:00:00', '09:00:00', 12, 'reserva4'),

('reserva_dia22', 120, '2025-04-21 08:00:00', '08:00:00', 10, 'reserva5'),
('reserva_dia23', 120, '2025-04-22 08:00:00', '08:00:00', 10, 'reserva5'),
('reserva_dia24', 120, '2025-04-23 08:00:00', '08:00:00', 10, 'reserva5'),
('reserva_dia25', 120, '2025-04-24 08:00:00', '08:00:00', 12, 'reserva5');

-- Crear reservas esporádicas adicionales
INSERT IGNORE INTO `reserva` (`id_reserva`, `actividad_academica`, `apellido_docente`, `cantidad_alumnos`, `correo_docente`, `fecha_solicitud`, `nombre_docente`, `tipo_reserva`, `realizada_por`) VALUES
('reserva6', 'Geografía', 'Sanchez', 28, 'sanchez@example.com', '2024-01-20 10:00:00', 'Miguel', 'ESPORADICA', 'bedel1'),
('reserva7', 'Literatura', 'Diaz', 32, 'diaz@example.com', '2024-01-21 11:00:00', 'Sofia', 'ESPORADICA', 'bedel1'),
('reserva8', 'Inglés', 'Torres', 22, 'torres@example.com', '2024-01-22 12:00:00', 'Lucia', 'ESPORADICA', 'bedel1'),
('reserva9', 'Arte', 'Ramirez', 26, 'ramirez@example.com', '2024-01-23 09:00:00', 'Jorge', 'ESPORADICA', 'bedel1'),
('reserva10', 'Música', 'Hernandez', 34, 'hernandez@example.com', '2024-01-24 08:00:00', 'Elena', 'ESPORADICA', 'bedel1');

-- Crear días de reserva para cada reserva esporádica adicional
INSERT IGNORE INTO `reserva_dia` (`id_reserva_dia`, `duracion`, `fecha`, `hora_inicio`, `nro_aula`, `id_reserva`) VALUES
('reserva_dia26', 90, '2025-04-25 10:00:00', '10:00:00', 10, 'reserva6'),
('reserva_dia27', 90, '2025-04-26 10:00:00', '10:00:00', 10, 'reserva6'),
('reserva_dia28', 90, '2025-04-27 10:00:00', '10:00:00', 11, 'reserva6'),
('reserva_dia29', 90, '2025-04-28 10:00:00', '10:00:00', 11, 'reserva6'),

('reserva_dia30', 120, '2025-04-25 11:00:00', '11:00:00', 13, 'reserva7'),
('reserva_dia31', 120, '2025-04-26 11:00:00', '11:00:00', 13, 'reserva7'),
('reserva_dia32', 120, '2025-04-27 11:00:00', '11:00:00', 13, 'reserva7'),
('reserva_dia33', 120, '2025-04-28 11:00:00', '11:00:00', 13, 'reserva7'),

('reserva_dia34', 60, '2025-04-25 12:00:00', '12:00:00', 14, 'reserva8'),
('reserva_dia35', 60, '2025-04-26 12:00:00', '12:00:00', 14, 'reserva8'),
('reserva_dia36', 60, '2025-04-27 12:00:00', '12:00:00', 14, 'reserva8'),
('reserva_dia37', 60, '2025-04-28 12:00:00', '12:00:00', 14, 'reserva8'),

('reserva_dia38', 90, '2025-04-25 09:00:00', '09:00:00', 12, 'reserva9'),
('reserva_dia39', 90, '2025-04-26 09:00:00', '09:00:00', 12, 'reserva9'),
('reserva_dia40', 90, '2025-04-27 09:00:00', '09:00:00', 12, 'reserva9'),
('reserva_dia41', 90, '2025-04-28 09:00:00', '09:00:00', 12, 'reserva9'),

('reserva_dia42', 120, '2025-04-25 08:00:00', '08:00:00', 10, 'reserva10'),
('reserva_dia43', 120, '2025-04-26 08:00:00', '08:00:00', 10, 'reserva10'),
('reserva_dia44', 120, '2025-04-27 08:00:00', '08:00:00', 10, 'reserva10'),
('reserva_dia45', 120, '2025-04-28 08:00:00', '08:00:00', 12, 'reserva10');