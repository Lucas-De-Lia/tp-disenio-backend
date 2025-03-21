package com.tpdisenio.gSM3C.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class BuscarBedelDTO {
    @Schema(description = "Apellido del bedel a buscar", example = "Perez")
    private String apellido;

    @Schema(description = "Turno del bedel", example = "MANIANA")
    private String turno;
}
