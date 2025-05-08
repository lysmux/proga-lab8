package dev.lysmux.collex.presentation.command.collection;


import com.google.inject.Inject;
import dev.lysmux.collex.domain.LabWork;
import dev.lysmux.collex.domain.User;
import dev.lysmux.collex.dto.response.ResponseStatus;
import dev.lysmux.collex.logic.service.collection.CollectionService;
import dev.lysmux.collex.dto.response.Body;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.server.command.Command;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;



@Command(
        name = "removeGreater",
        description = "Remove all items from the collection that exceed the specified size"
)
@RequiredArgsConstructor(onConstructor_ = @Inject)
final public class RemoveGreaterCommand {
    private final CollectionService collectionService;

    public Response<?> execute(User user, LabWork entity) {
        Set<Integer> idsToRemove = collectionService.getCollection().stream()
                .filter(el -> el.getOwnerId() == user.getId())
                .filter(el -> el.compareTo(entity) > 0)
                .map(LabWork::getId)
                .collect(Collectors.toSet());

        if (idsToRemove.isEmpty()) {
            return Response.builder()
                    .status(ResponseStatus.NOT_FOUND)
                    .body(new Body<>("Entities to remove not found"))
                    .build();
        }

        idsToRemove.forEach(collectionService::remove);

        return Response.builder()
                .body(new Body<>("Entities removed", idsToRemove.size()))
                .build();
    }
}
