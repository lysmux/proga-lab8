package dev.lysmux.collex.presentation.command.collection;


import com.google.inject.Inject;
import dev.lysmux.collex.domain.LabWork;
import dev.lysmux.collex.domain.User;
import dev.lysmux.collex.logic.service.collection.CollectionService;
import dev.lysmux.collex.dto.response.Body;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.server.command.Command;
import lombok.RequiredArgsConstructor;


@Command(name = "add", description = "Add entity to collection")
@RequiredArgsConstructor(onConstructor_ = @Inject)
final public class AddCommand {
    private final CollectionService collectionService;

    public Response<?> execute(User user, LabWork entity) {
        entity.setOwnerId(user.getId());
        int entityId = collectionService.add(entity);

        return Response
                .builder()
                .body(new Body<>("Entity added", entityId))
                .build();
    }
}
