package dev.lysmux.collex.logic.service.user;

import com.google.inject.Inject;
import dev.lysmux.collex.domain.User;
import dev.lysmux.collex.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UserService {
    private final UserRepository userRepository;

    public void register(String login, String password){
        if (userRepository.getByLogin(login).isPresent()) throw new UserExistsException(login);

        User user = User.builderWithRawPassword(password)
                .login(login)
                .build();
        userRepository.add(user);
    }

    public User getUserById(int id) {
        return userRepository.getById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User getUserByLogin(String login) {
        return userRepository.getByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login));
    }

    public void changePassword(int userId, String newPassword) {
        User user = getUserById(userId);
        user.setRawPassword(newPassword);

        userRepository.update(user);
    }
}
