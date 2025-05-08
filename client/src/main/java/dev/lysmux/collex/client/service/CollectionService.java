package dev.lysmux.collex.client.service;

import com.google.inject.Inject;
import dev.lysmux.collex.client.network.Client;
import dev.lysmux.collex.domain.LabWork;
import dev.lysmux.collex.dto.request.Body;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.dto.response.ResponseStatus;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class CollectionService {
    private final Client client;

    public List<LabWork> getAll() {
        Body body = Body.builder().command("show").build();
        Response<List<LabWork>> response = client.sendRequest(body);
        return response.getBody().result();
    }

    public boolean add(LabWork item) {
        Body body = Body.builder()
                .command("add")
                .arg("entity", item)
                .build();
        Response<?> response = client.sendRequest(body);
        return response.getStatus() == ResponseStatus.SUCCESS;
    }

    public boolean addIfMax(LabWork item) {
        Body body = Body.builder()
                .command("add")
                .arg("entity", item)
                .build();
        Response<?> response = client.sendRequest(body);
        return response.getStatus() == ResponseStatus.SUCCESS;
    }

    public boolean update(LabWork item) {
        Body body = Body.builder()
                .command("update")
                .arg("entity", item)
                .build();
        Response<?> response = client.sendRequest(body);
        return response.getStatus() == ResponseStatus.SUCCESS;
    }

    public boolean remove(int entityId) {
        Body body = Body.builder()
                .command("removeById")
                .arg("entityId", entityId)
                .build();
        Response<?> response = client.sendRequest(body);
        return response.getStatus() == ResponseStatus.SUCCESS;
    }

    public boolean removeGreater(LabWork entity) {
        Body body = Body.builder()
                .command("removeGreater")
                .arg("entity", entity)
                .build();
        Response<?> response = client.sendRequest(body);
        return response.getStatus() == ResponseStatus.SUCCESS;
    }

    public boolean removeLower(LabWork entity) {
        Body body = Body.builder()
                .command("removeLower")
                .arg("entity", entity)
                .build();
        Response<?> response = client.sendRequest(body);
        return response.getStatus() == ResponseStatus.SUCCESS;
    }

    public boolean clear() {
        Body body = Body.builder().command("clear").build();
        Response<?> response = client.sendRequest(body);
        return response.getStatus() == ResponseStatus.SUCCESS;
    }

    public boolean addRandom(int count) {
        Body body = Body.builder()
                .command("addRandom")
                .arg("count", count)
                .build();
        Response<?> response = client.sendRequest(body);
        return response.getStatus() == ResponseStatus.SUCCESS;
    }

    public long countGreaterThanMinimalPoint(long minimalPoint) {
        Body body = Body.builder()
                .command("countGreaterThanMinimalPoint")
                .arg("minimalPoint", minimalPoint)
                .build();
        Response<Long> response = client.sendRequest(body);
        return response.getBody().result();
    }
}
