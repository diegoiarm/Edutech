package com.edutech.common.exception;

import java.util.Optional;
import java.util.function.Function;

import feign.FeignException;

public class ExceptionUtils {

    private ExceptionUtils() {
        // Constructor privado para evitar instancias
    }

    public static <T> T orThrow(Optional<T> optional, String entityName) {
        return optional.orElseThrow(() -> new ResourceNotFoundException(entityName + " no encontrado"));
    }

    public static <T> T orThrow2(Optional<T> optional, String entityName) {
        return optional.orElseThrow(() -> new ResourceNotFoundException(entityName + " no encontrada"));
    }

    public static <ID, T> T orThrowFeign(ID id, Function<ID, T> caller, String entity) {
    try {
        return caller.apply(id);
    } catch (FeignException.NotFound e) {
        throw new ResourceNotFoundException(entity + " no encontrado");
    }
}

    public static <ID, T> T orThrowFeign2(ID id, Function<ID, T> caller, String entity) {
        try {
            return caller.apply(id);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException(entity + " no encontrada");
        }
    }
}
