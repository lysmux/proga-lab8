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
        name = "addIfMax",
        description = "Add a new element to a collection if its value exceeds the value of the largest item in that collection"
)
@RequiredArgsConstructor(onConstructor_ = @Inject)
final public class AddIfMaxCommand {
    private final CollectionService collectionService;

    public Response<?> execute(User user, LabWork entity) {
        entity.setOwnerId(user.getId());

        LabWork maxEntity = collectionService.getCollection().stream()
                .max(LabWork::compareTo)
                .orElse(null);
        if (maxEntity == null || maxEntity.compareTo(entity) < 0) {
            int entityId =collectionService.add(entity);
            return Response.builder()
                    .body(new Body<>("Entity added",entityId))
                    .build();
        }

        return Response.builder()
                .status(ResponseStatus.NOT_FOUND)
                .body(new Body<>("Entity not greater than each entity in collection"))
                .build();
    }
}
