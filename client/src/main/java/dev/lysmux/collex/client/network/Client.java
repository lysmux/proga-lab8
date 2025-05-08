package dev.lysmux.collex.client.network;

import dev.lysmux.collex.dto.request.Request;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.client.network.connection.Connection;
import dev.lysmux.collex.client.network.connection.TCPConnection;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;

public class Client implements AutoCloseable {
    private final String serverHost;
    private final int serverPort;

    private Connection connection;

    public Client(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public void connect() throws IOException {
        connection = new TCPConnection(serverHost, serverPort);
    }

    public Response sendRequest(Request request) throws IOException {
        if (connection == null) connect();

        connection.send(SerializationUtils.serialize(request));
        Response response = SerializationUtils.deserialize(connection.read());
        return response;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) connection.close();
    }
}
