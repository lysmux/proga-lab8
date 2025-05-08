package dev.lysmux.collex.infrastructure.repository;

import com.google.inject.Inject;
import dev.lysmux.collex.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SqlUserRepository implements UserRepository {

    private final Connection connection;

    @Override
    public User getById(int id) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) return null;

            return User.builder()
                    .id(rs.getInt("id"))
                    .login(rs.getString("login"))
                    .hashedPassword(rs.getString("hashed_password"))
                    .registrationTime(rs.getTimestamp("registration_time").toLocalDateTime())
                    .build();
        } catch (SQLException e) {
            log.atError()
                    .setMessage("Failed to get user by id")
                    .addKeyValue("id", id)
                    .setCause(e)
                    .log();
            throw new DatabaseException();
        }
    }

    @Override
    public User getByLogin(String login) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE login = ?");
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) return null;

            return User.builder()
                    .id(rs.getInt("id"))
                    .login(rs.getString("login"))
                    .hashedPassword(rs.getString("hashed_password"))
                    .registrationTime(rs.getTimestamp("registration_time").toLocalDateTime())
                    .build();
        } catch (SQLException e) {
            log.atError()
                    .setMessage("Failed to get user by login")
                    .addKeyValue("login", login)
                    .setCause(e)
                    .log();
            throw new DatabaseException();
        }
    }

    @Override
    public int add(User user) {
        try {
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO users (login, hashed_password, registration_time) VALUES (?, ?, ?) RETURNING id");
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getHashedPassword());
            stmt.setTimestamp(3, Timestamp.valueOf(user.getRegistrationTime()));
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) return -1;
            return rs.getInt("id");
        } catch (SQLException e) {
            log.atError()
                    .setMessage("Failed to add user")
                    .addKeyValue("user", user)
                    .setCause(e)
                    .log();
            throw new DatabaseException();
        }
    }

    @Override
    public void update(User user) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE users SET login = ?, hashed_password = ? WHERE id = ?");
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getHashedPassword());
            stmt.setInt(3, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.atError()
                    .setMessage("Failed to update user")
                    .addKeyValue("user", user)
                    .setCause(e)
                    .log();
            throw new DatabaseException();
        }
    }
}
