package dev.lysmux.collex.logic.service.collection;

import com.google.inject.Inject;
import dev.lysmux.collex.domain.LabWork;
import dev.lysmux.collex.infrastructure.repository.CollectionRepository;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class CollectionService {
    private final CollectionRepository collectionRepository;

    @Getter
    private final List<LabWork> collection = new ArrayList<>();

    @Inject
    public CollectionService(final CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
        loadCollection();
    }

    public void loadCollection() {
        collection.clear();
        collection.addAll(collectionRepository.getAll());
    }

    public int add(LabWork item) {
        int entityId = collectionRepository.add(item);
        item.setId(entityId);
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
        collection.removeIf(el -> el.getOwnerId() == ownerId);

        return count;
    }

    public boolean checkUserAccess(int userId, int entityId) {
        return collection.stream()
                .anyMatch(el -> el.getOwnerId() == userId && el.getId() == entityId);
    }

    public void update(LabWork item) {
        collectionRepository.update(item);

        collection.remove(item);
        collection.add(item);
    }
}
