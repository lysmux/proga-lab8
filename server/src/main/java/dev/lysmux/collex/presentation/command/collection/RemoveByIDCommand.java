package dev.lysmux.collex.presentation.command.collection;


import com.google.inject.Inject;
import dev.lysmux.collex.domain.User;
import dev.lysmux.collex.logic.service.collection.CollectionService;
import dev.lysmux.collex.dto.response.Body;
import dev.lysmux.collex.dto.response.ResponseStatus;
import dev.lysmux.collex.server.command.Command;
import dev.lysmux.collex.dto.response.Response;
import lombok.RequiredArgsConstructor;


@Command(name = "removeById", description = "Remove collection element by given ID")
@RequiredArgsConstructor(onConstructor_ = @Inject)
final public class RemoveByIDCommand {
    private final CollectionService collectionService;

    public Response<?> execute(User user, int entityId) {
        collectionService.getById(entityId);
        if (!collectionService.checkUserAccess(user.getId(), entityId)) {
            return Response.builder()
                    .status(ResponseStatus.FORBIDDEN)
                    .body(new Body<>("You do not have access to this collection item"))
                    .build();
        }
        collectionService.remove(entityId);

        return Response.builder()
                .body(new Body<>("Entity removed", entityId))
                .build();
    }
}
