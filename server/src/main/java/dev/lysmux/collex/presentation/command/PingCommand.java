package dev.lysmux.collex.presentation.command;


import dev.lysmux.collex.dto.response.Body;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.server.command.Command;


@Command(name = "ping")
final public class PingCommand {
    public Response<?> execute() {
        return Response.builder()
                .body(new Body<>("Pong"))
                .build();
    }
}
