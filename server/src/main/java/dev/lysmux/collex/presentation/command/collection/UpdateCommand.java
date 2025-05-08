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



@Command(name = "update", description = "Update collection with given ID")
@RequiredArgsConstructor(onConstructor_ = @Inject)
final public class UpdateCommand {
    private final CollectionService collectionService;

    public Response<?> execute(User user, LabWork entity) {
        collectionService.getById(entity.getId());
        if (!collectionService.checkUserAccess(user.getId(), entity.getId())) {
            return Response.builder()
                    .status(ResponseStatus.FORBIDDEN)
                    .body(new Body<>("You do not have access to this collection item"))
                    .build();
        }
        collectionService.update(entity);

        return Response
                .builder()
                .body(new Body<>("Entity updated"))
                .build();
    }
}
