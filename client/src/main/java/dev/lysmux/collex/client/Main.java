package dev.lysmux.collex.client;

import dev.lysmux.collex.dto.request.Auth;
import dev.lysmux.collex.dto.request.Body;
import dev.lysmux.collex.dto.request.Request;
import dev.lysmux.collex.client.network.Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 8123);
        Request request = Request.builder()
                .auth(new Auth("test", "test"))
                .body(new Body("show"))
                .build();
        System.out.println(client.sendRequest(request));
    }
}
