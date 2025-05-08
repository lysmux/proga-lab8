package dev.lysmux.collex.presentation.command.collection;


import com.google.inject.Inject;
import dev.lysmux.collex.domain.LabWork;
import dev.lysmux.collex.logic.service.collection.CollectionService;
import dev.lysmux.collex.dto.response.Body;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.server.command.Command;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@Command(
        name = "printFieldDescendingMinimalPoint",
        description = "Display the values of the minimal point field of all elements in descending order"
)
@RequiredArgsConstructor(onConstructor_ = @Inject)
final public class PrintFieldDescendingMinimalPoint {
    private final CollectionService collectionService;

    public Response<?> execute() {
        List<Long> minimalPoints = collectionService.getCollection().stream()
                .map(LabWork::getMinimalPoint)
                .sorted(Collections.reverseOrder())
                .toList();

        return Response.builder()
                .body(new Body<>(null, minimalPoints))
                .build();
    }
}
