package dev.lysmux.collex.presentation.command.collection;

import com.google.inject.Inject;
import dev.lysmux.collex.domain.User;
import dev.lysmux.collex.logic.service.collection.CollectionService;
import dev.lysmux.collex.dto.response.Body;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.server.command.Command;
import lombok.RequiredArgsConstructor;

@Command(name = "show")
@RequiredArgsConstructor(onConstructor_ = @Inject)
final public class ShowCommand {
    private final CollectionService collectionService;

    public Response<?> execute(User user) {
        return Response.builder()
                .body(new Body<>(null, collectionService.getCollection()))
                .build();
    }
}
