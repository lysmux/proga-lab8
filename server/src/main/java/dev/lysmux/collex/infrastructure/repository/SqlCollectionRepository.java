package dev.lysmux.collex.infrastructure.repository;

import com.google.inject.Inject;
import dev.lysmux.collex.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SqlCollectionRepository implements CollectionRepository {
    private final Connection connection;

    private static final String INSERT_SQL = """
                INSERT INTO lab_works 
                VALUES (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) 
                RETURNING id
            """;

    private static final String DELETE_BY_ID_SQL = "DELETE FROM lab_works WHERE id = ?";
    private static final String DELETE_BY_OWNER_SQL = "DELETE FROM lab_works WHERE owner_id = ?";
    private static final String SELECT_ALL_SQL = """
                SELECT
                    id, owner_id, created_at, name,
                    coordinate_x, coordinate_y,
                    minimal_point, difficulty,
                    author_name, author_birthday, author_weight,
                    author_location_x, author_location_y, author_location_name
                FROM lab_works
            """;
    private static final String UPDATE_SQL = """
            UPDATE lab_works SET
                owner_id = ?,
                name = ?,
                minimal_point = ?,
                difficulty = ?,
                coordinate_x = ?,
                coordinate_y = ?,
                author_name = ?,
                author_birthday =?,
                author_weight = ?,
                author_location_name = ?,
                author_location_x = ?,
                author_location_y = ?
            WHERE id = ?
            """;

    @Override
    public int add(LabWork item) {
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_SQL)) {
            stmt.setInt(1, item.getOwnerId());
            stmt.setString(2, item.getName());
            stmt.setLong(3, item.getMinimalPoint());
            stmt.setString(4, item.getDifficulty().name());
            stmt.setInt(5, item.getCoordinates().getX());
            stmt.setDouble(6, item.getCoordinates().getY());
            stmt.setString(7, item.getAuthor().getName());
            stmt.setDate(8, new Date(item.getAuthor().getBirthday().getTime()));
            stmt.setLong(9, item.getAuthor().getWeight());
            stmt.setString(10, item.getAuthor().getLocation().getName());
            stmt.setInt(11, item.getAuthor().getLocation().getX());
            stmt.setFloat(12, item.getAuthor().getLocation().getY());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("lw_id");
                }
                return -1;
            }

        } catch (SQLException e) {
            log.atError()
                    .setMessage("Failed to add labWork")
                    .addKeyValue("labWork", item)
                    .setCause(e)
                    .log();
            throw new DatabaseException();
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.atError()
                    .setMessage("Failed to delete labWork")
                    .addKeyValue("id", id)
                    .setCause(e)
                    .log();
            throw new DatabaseException();
        }
    }

    @Override
    public void update(LabWork item) {
        try (PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL)) {
            stmt.setInt(1, item.getOwnerId());
            stmt.setString(2, item.getName());
            stmt.setLong(3, item.getMinimalPoint());
            stmt.setString(4, item.getDifficulty().name());
            stmt.setInt(5, item.getCoordinates().getX());
            stmt.setDouble(6, item.getCoordinates().getY());
            stmt.setString(7, item.getAuthor().getName());
            stmt.setDate(8, new Date(item.getAuthor().getBirthday().getTime()));
            stmt.setLong(9, item.getAuthor().getWeight());
            stmt.setString(10, item.getAuthor().getLocation().getName());
            stmt.setInt(11, item.getAuthor().getLocation().getX());
            stmt.setFloat(12, item.getAuthor().getLocation().getY());
            stmt.setInt(13, item.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            log.atError()
                    .setMessage("Failed to update labWork")
                    .addKeyValue("labWork", item)
                    .setCause(e)
                    .log();
            throw new DatabaseException();
        }
    }

    @Override
    public int clearByOwnerId(int id) {
        try (PreparedStatement stmt = connection.prepareStatement(DELETE_BY_OWNER_SQL)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.atError()
                    .setMessage("Failed to clear collection by owner id")
                    .addKeyValue("ownerId", id)
                    .setCause(e)
                    .log();
            throw new DatabaseException();
        }
    }

    @Override
    public List<LabWork> getAll() {
        List<LabWork> labWorks = new ArrayList<>();
        try (
                PreparedStatement stmt = connection.prepareStatement(SELECT_ALL_SQL);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                labWorks.add(buildLabWorkFromResultSet(rs));
            }

        } catch (SQLException e) {
            log.atError()
                    .setMessage("Failed to get collection")
                    .setCause(e)
                    .log();
            throw new DatabaseException();
        }
        return labWorks;
    }

    private LabWork buildLabWorkFromResultSet(ResultSet rs) throws SQLException {
        Location location = Location.builder()
                .x(rs.getInt("author_location_x"))
                .y(rs.getFloat("author_location_y"))
                .name(rs.getString("author_location_name"))
                .build();
        Person author = Person.builder()
                .name(rs.getString("author_name"))
                .birthday(rs.getDate("author_birthday"))
                .weight(rs.getLong("author_weight"))
                .location(location)
                .build();

        Coordinates coordinates = Coordinates.builder()
                .x(rs.getInt("coordinate_x"))
                .y(rs.getDouble("coordinate_y"))
                .build();

        return LabWork.builder()
                .id(rs.getInt("id"))
                .ownerId(rs.getInt("owner_id"))
                .creationDate(rs.getDate("created_at").toLocalDate())
                .name(rs.getString("name"))
                .minimalPoint(rs.getLong("minimal_point"))
                .difficulty(Difficulty.valueOf(rs.getString("difficulty")))
                .coordinates(coordinates)
                .author(author)
                .build();
    }
}
