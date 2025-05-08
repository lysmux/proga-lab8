package dev.lysmux.collex.logic.service.collection;

import com.google.inject.Inject;
import dev.lysmux.collex.domain.LabWork;
import dev.lysmux.collex.infrastructure.repository.CollectionRepository;
import lombok.Getter;

import java.util.TreeSet;

public class CollectionService {
    private final CollectionRepository collectionRepository;

    @Getter
    private final TreeSet<LabWork> collection = new TreeSet<>();

    @Inject
    public CollectionService(final CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    public int add(LabWork item) {
        int entityId = collectionRepository.add(item);
        collection.add(item);

        return entityId;
    }

    public LabWork getById(int id) {
        return collection.stream()
                .filter(el -> el.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(id));
    }

    public void remove(int id) {
        collectionRepository.delete(id);
        collection.removeIf(el -> el.getId() == id);
    }

    public int clearByOwnerId(int ownerId) {
        int count = collectionRepository.clearByOwnerId(ownerId);
        collection.removeIf(el -> el.getId() == ownerId);

        return count;
    }

    public boolean exists(int id) {
        return collection.stream().anyMatch(el -> el.getId() == id);
    }

    public boolean checkUserAccess(int userId, int entityId) {
        return collection.stream()
                .anyMatch(el -> el.getId() == userId && el.getId() == entityId);
    }

    public void update(LabWork item) {
        collectionRepository.update(item);

        collection.remove(item);
        collection.add(item);
    }
}
