package dev.lysmux.collex.infrastructure.repository;

import dev.lysmux.collex.domain.LabWork;

import java.util.List;

public interface CollectionRepository {
    int add(LabWork item);

    void delete(int id);

    void update(LabWork item);

    int clearByOwnerId(int id);

    List<LabWork> getAll();
}
