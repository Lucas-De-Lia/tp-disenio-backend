package com.tpdisenio.gSM3C.exception;

public class UsuarioAlreadyExistsException extends RuntimeException {
    public UsuarioAlreadyExistsException(String message) {
        super(message);
    }
}