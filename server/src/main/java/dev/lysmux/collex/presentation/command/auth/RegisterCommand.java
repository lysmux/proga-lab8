package dev.lysmux.collex.presentation.command.auth;


import com.google.inject.Inject;
import dev.lysmux.collex.logic.service.user.UserService;
import dev.lysmux.collex.dto.response.Body;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.server.command.Command;
import lombok.RequiredArgsConstructor;


@Command(name = "register")
@RequiredArgsConstructor(onConstructor_ = @Inject)
final public class RegisterCommand {
    private final UserService userService;

    public Response<?> execute(String login, String password) {
        userService.register(login, password);

        return Response.builder()
                .body(new Body<>("User registered", login))
                .build();
    }
}
