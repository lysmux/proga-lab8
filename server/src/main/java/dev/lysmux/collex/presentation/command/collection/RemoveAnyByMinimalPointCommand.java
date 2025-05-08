package dev.lysmux.collex.presentation.command.collection;


import com.google.inject.Inject;
import dev.lysmux.collex.domain.LabWork;
import dev.lysmux.collex.domain.User;
import dev.lysmux.collex.logic.service.collection.CollectionService;
import dev.lysmux.collex.dto.response.Body;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.dto.response.ResponseStatus;
import dev.lysmux.collex.server.command.Command;
import lombok.RequiredArgsConstructor;

@Command(
        name = "removeAnyByMinimalPoint",
        description = "Remove one item from the collection, the value of the minimal Point field of which is equivalent to the specified one"
)
@RequiredArgsConstructor(onConstructor_ = @Inject)
final public class RemoveAnyByMinimalPointCommand {
    private final CollectionService collectionService;

    public Response<?> execute(User user, long minimalPoint) {
        LabWork entity = collectionService.getCollection().stream()
                .filter(el -> el.getOwnerId() == user.getId())
                .filter(el -> el.getMinimalPoint() == minimalPoint)
                .findFirst().orElse(null);
        if (entity == null) {
            return Response.builder()
                    .status(ResponseStatus.NOT_FOUND)
                    .body(new Body<>("Entity with this minimal point not found"))
                    .build();
        }
        collectionService.remove(entity.getId());

        return Response.builder()
                .body(new Body<>("Entity removed", entity.getId()))
                .build();
    }
}
