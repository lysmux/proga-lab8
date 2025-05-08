package dev.lysmux.collex.client.service;

import com.google.inject.Inject;
import dev.lysmux.collex.client.network.Client;
import dev.lysmux.collex.dto.request.Body;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ConnectionService {
    private final Client client;

    public void connect(String host, int port) {
        client.setServerHost(host);
        client.setServerPort(port);

        Body body = Body.builder().command("ping").build();
        client.sendRequest(body);
    }
}
