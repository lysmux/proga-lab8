package dev.lysmux.collex.client.network;

import dev.lysmux.collex.client.exception.ConnectionException;
import dev.lysmux.collex.client.exception.ServerException;
import dev.lysmux.collex.client.network.connection.Connection;
import dev.lysmux.collex.client.network.connection.TCPConnection;
import dev.lysmux.collex.dto.request.Auth;
import dev.lysmux.collex.dto.request.Body;
import dev.lysmux.collex.dto.request.Request;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.dto.response.ResponseStatus;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

import java.util.Objects;

@Slf4j
@Setter
public class Client {
    private String serverHost;
    private Integer serverPort;
    private Auth auth;

    public <T> Response<T> sendRequest(Body body) {
        Objects.requireNonNull(serverHost, "Server host not set");
        Objects.requireNonNull(serverPort, "Server port not set");

        Response<T> response;
        Request request = Request.builder()
                .auth(auth)
                .body(body)
                .build();

        try (Connection connection = new TCPConnection(serverHost, serverPort)) {
            connection.send(SerializationUtils.serialize(request));
            response = SerializationUtils.deserialize(connection.read());
        } catch (Exception e) {
            log.atError()
                    .setMessage("Error while sending request")
                    .addKeyValue("request", request)
                    .setCause(e)
                    .log();
            throw new ConnectionException(e.getMessage());
        }

        if (response.getStatus() == ResponseStatus.INTERNAL_SERVER_ERROR) {
            throw new ServerException(response.getStatus(), response.getBody().message());
        }

        log.atInfo()
                .setMessage("Response received")
                .addKeyValue("status", response.getStatus())
                .addKeyValue("message", response.getBody().message())
                .log();

        return response;
    }
}
