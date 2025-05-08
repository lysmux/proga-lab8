package dev.lysmux.collex.logic.service.collection;

import dev.lysmux.collex.logic.exception.NotFoundException;

public class EntityNotFoundException extends NotFoundException {
    public EntityNotFoundException(int id) {
        super("Entity not found: ID-%d".formatted(id));
    }
}
