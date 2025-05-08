package dev.lysmux.collex.infrastructure.repository;

import com.google.inject.Inject;
import dev.lysmux.collex.domain.User;
import dev.lysmux.collex.infrastructure.database.SqlExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SqlUserRepository implements UserRepository {
    private final SqlExecutor sqlExecutor;

    private static final String SQL_GET_BY_ID = "SELECT * FROM users WHERE id = ?";
    private static final String SQL_GET_BY_LOGIN = "SELECT * FROM users WHERE login = ?";
    private static final String SQL_ADD = "INSERT INTO users (login, hashed_password, registration_time) VALUES (?, ?, ?) RETURNING id";
    private static final String SQL_UPDATE = "UPDATE users SET login = ?, hashed_password = ? WHERE id = ?";

    @Override
    public Optional<User> getById(int id) {
        return sqlExecutor.executeQuery(SQL_GET_BY_ID, stmt -> {
            stmt.setInt(1, id);
        }, rs -> {
            if (!rs.next()) return Optional.empty();
            return Optional.of(mapResultSetToUser(rs));
        });
    }

    @Override
    public Optional<User> getByLogin(String login) {
        return sqlExecutor.executeQuery(SQL_GET_BY_LOGIN, stmt -> {
            stmt.setString(1, login);
        }, rs -> {
            if (!rs.next()) return Optional.empty();
            return Optional.of(mapResultSetToUser(rs));
        });
    }

    @Override
    public int add(User user) {
        return sqlExecutor.executeQuery(SQL_ADD, stmt -> {
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getHashedPassword());
            stmt.setTimestamp(3, Timestamp.valueOf(user.getRegistrationTime()));
        }, rs -> {
            if (!rs.next()) return -1;
            return rs.getInt("id");
        });
    }

    @Override
    public void update(User user) {
        sqlExecutor.executeUpdate(SQL_UPDATE, stmt -> {
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getHashedPassword());
            stmt.setInt(3, user.getId());
        });
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getInt("id"))
                .login(rs.getString("login"))
                .hashedPassword(rs.getString("hashed_password"))
                .registrationTime(rs.getTimestamp("registration_time").toLocalDateTime())
                .build();
    }
}
