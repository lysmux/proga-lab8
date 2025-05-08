package dev.lysmux.collex.presentation.command.collection;

import com.google.inject.Inject;
import dev.lysmux.collex.domain.*;
import dev.lysmux.collex.logic.service.collection.CollectionService;
import dev.lysmux.collex.dto.response.Body;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.dto.response.ResponseStatus;
import dev.lysmux.collex.server.command.Command;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.Random;


@Command(name = "addRandom", description = "Add random entity to collection")
@RequiredArgsConstructor(onConstructor_ = @Inject)
final public class AddRandomCommand {
    private final CollectionService collectionService;

    private final Random random = new Random();

    private final int maxCount = 10000;

    public Response<?> execute(User user, int count) {
        if (count < 1) {
            return Response.builder()
                    .status(ResponseStatus.BAD_REQUEST)
                    .body(new Body<>("Invalid number of elements"))
                    .build();
        }

        if (count > maxCount) {
            return Response.builder()
                    .status(ResponseStatus.BAD_REQUEST)
                    .body(new Body<>("Maximum number of elements exceeded: " + maxCount))
                    .build();
        }

        for (int i = 0; i < count; i++) {
            addRandom(user.getId());
        }

        return Response.builder()
                .body(new Body<>("Entities added", count))
                .build();
    }

    private void addRandom(int ownerId) {
        LabWork entity = LabWork.builder()
                .ownerId(ownerId)
                .name("entity_%d".formatted(random.nextInt(1000)))
                .coordinates(getRandomCoordinates())
                .minimalPoint(random.nextLong(100) + 1)
                .difficulty(getRandomDifficulty())
                .author(getRandomPerson())
                .build();

        collectionService.add(entity);
    }

    private Coordinates getRandomCoordinates() {
        return Coordinates.builder()
                .x(random.nextInt(-10, 10))
                .y(random.nextDouble(-10, 10))
                .build();
    }

    private Difficulty getRandomDifficulty() {
        Difficulty[] values = Difficulty.values();
        return values[random.nextInt(values.length)];
    }

    private Person getRandomPerson() {
        return Person.builder()
                .name("person_%d".formatted(random.nextInt(10)))
                .birthday(LocalDate.now())
                .weight(random.nextLong(1, 100))
                .location(getRandomLocation())
                .build();
    }

    private Location getRandomLocation() {
        return Location.builder()
                .x(random.nextInt(-10, 10))
                .y(random.nextFloat(-10, 10))
                .name("location_%d".formatted(random.nextInt(10)))
                .build();
    }
}
