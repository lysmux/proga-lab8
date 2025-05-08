package dev.lysmux.collex.logic.service.user;

import com.google.inject.Inject;
import dev.lysmux.collex.domain.User;
import dev.lysmux.collex.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UserService {
    private final UserRepository userRepository;

    public void register(String login, String password){
        if (userRepository.getByLogin(login) != null) throw new UserExistsException(login);

        User user = User.builderWithRawPassword(password)
                .login(login)
                .build();
        userRepository.add(user);
    }

    public User getUserById(int id) {
        User user = userRepository.getById(id);
        if (user == null) throw new UserNotFoundException(id);

        return user;
    }

    public User getUserByLogin(String login) {
        User user = userRepository.getByLogin(login);
        if (user == null) throw new UserNotFoundException(login);

        return user;
    }

    public void changePassword(int userId, String newPassword) {
        User user = getUserById(userId);
        user.setRawPassword(newPassword);

        userRepository.update(user);
    }
}
