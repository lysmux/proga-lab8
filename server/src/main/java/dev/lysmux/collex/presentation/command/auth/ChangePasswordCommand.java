package dev.lysmux.collex.presentation.command.auth;

import com.google.inject.Inject;
import dev.lysmux.collex.domain.User;
import dev.lysmux.collex.logic.service.user.UserService;
import dev.lysmux.collex.dto.response.Body;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.server.command.Command;
import lombok.RequiredArgsConstructor;

@Command(name = "changePassword")
@RequiredArgsConstructor(onConstructor_ = @Inject)
final public class ChangePasswordCommand {
    private final UserService userService;

    public Response<?> execute(User user, String newPassword) {
        userService.changePassword(user.getId(), newPassword);

        return Response.builder()
                .body(new Body<>("Password changed"))
                .build();
    }
}
