package dev.lysmux.collex.presentation.command.auth;

import dev.lysmux.collex.domain.User;
import dev.lysmux.collex.dto.UserInfo;
import dev.lysmux.collex.dto.response.Body;
import dev.lysmux.collex.dto.response.Response;
import dev.lysmux.collex.server.command.Command;
import lombok.RequiredArgsConstructor;

@Command(name = "getUserInfo")
@RequiredArgsConstructor
final public class GetUserInfoCommand {
    public Response<?> execute(User user) {
        UserInfo userInfo = UserInfo.builder()
                .id(user.getId())
                .login(user.getLogin())
                .registrationTime(user.getRegistrationTime())
                .build();

        return Response.builder()
                .body(new Body<>(null, userInfo))
                .build();
    }
}
