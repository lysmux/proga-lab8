package dev.lysmux.collex.presentation.command.collection;


import com.google.inject.Inject;
import dev.lysmux.collex.domain.LabWork;
import dev.lysmux.collex.domain.User;
import dev.lysmux.collex.logic.service.collection.CollectionService;
import dev.lysmux.collex.dto.response.Body;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.server.command.Command;
import lombok.RequiredArgsConstructor;


@Command(
        name = "countGreaterThanMinimalPoint",
        description = "Display the number of elements whose minimum point field value is greater than the specified one"
)
@RequiredArgsConstructor(onConstructor_ = @Inject)
final public class CountGreaterThanMinimalPointCommand {
    private final CollectionService collectionService;


    public Response<?> execute(User user, long minimalPoint) {
        long count = collectionService.getCollection().stream()
                .map(LabWork::getMinimalPoint)
                .filter(el -> el > minimalPoint)
                .count();

        return Response
                .builder()
                .body(new Body<>(null, count))
                .build();
    }
}
