package dev.lysmux.collex.presentation.command.auth;


import dev.lysmux.collex.domain.User;
import dev.lysmux.collex.dto.response.Body;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.server.command.Command;
import lombok.RequiredArgsConstructor;


@Command(name = "login")
@RequiredArgsConstructor
final public class LoginCommand {
    public Response<?> execute(User user) {
        return Response.builder()
                .body(new Body<>("Successfully logged in", user.getId()))
                .build();
    }
}
