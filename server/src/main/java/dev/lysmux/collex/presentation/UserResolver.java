package dev.lysmux.collex.presentation;

import com.google.inject.Inject;
import dev.lysmux.collex.domain.User;
import dev.lysmux.collex.logic.service.user.UserNotFoundException;
import dev.lysmux.collex.logic.service.user.UserService;
import dev.lysmux.collex.dto.request.Request;
import dev.lysmux.collex.server.argument.ArgumentResolver;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Parameter;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UserResolver implements ArgumentResolver<User> {
    private final UserService userService;

    @Override
    public boolean supportsParameter(Parameter parameter) {
        return parameter.getType().equals(User.class);
    }

    @Override
    public User resolveArgument(Parameter parameter, Request request) {
        if (request.auth() == null) throw new UnauthorizedException();

        try {
            User user = userService.getUserByLogin(request.auth().login());
            if (!user.verifyPassword(request.auth().password())) {
                throw new UnauthorizedException();
            }
            return user;
        } catch (UserNotFoundException e) {
            throw new UnauthorizedException();
        }
    }
}
