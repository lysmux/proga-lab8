package dev.lysmux.collex.infrastructure.repository;

import com.google.inject.Inject;
import dev.lysmux.collex.domain.*;
import dev.lysmux.collex.infrastructure.database.SqlExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SqlCollectionRepository implements CollectionRepository {
    private final SqlExecutor sqlExecutor;

    private static final String INSERT_SQL = """
                INSERT INTO lab_works
                (owner_id, name, minimal_point, difficulty, coordinate_x, coordinate_y, author_name, author_birthday, author_weight, author_location_x, author_location_y, author_location_name)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) 
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
        return sqlExecutor.executeQuery(INSERT_SQL, stmt -> {
            stmt.setInt(1, item.getOwnerId());
            stmt.setString(2, item.getName());
            stmt.setLong(3, item.getMinimalPoint());
            stmt.setString(4, item.getDifficulty().name());
            stmt.setInt(5, item.getCoordinates().getX());
            stmt.setDouble(6, item.getCoordinates().getY());
            stmt.setString(7, item.getAuthor().getName());
            stmt.setDate(8, Date.valueOf(item.getAuthor().getBirthday()));
            stmt.setLong(9, item.getAuthor().getWeight());
            stmt.setInt(10, item.getAuthor().getLocation().getX());
            stmt.setFloat(11, item.getAuthor().getLocation().getY());
            stmt.setString(12, item.getAuthor().getLocation().getName());

        }, rs -> {
            if (!rs.next()) return -1;
            return rs.getInt("id");
        });
    }

    @Override
    public void delete(int id) {
        sqlExecutor.executeUpdate(DELETE_BY_ID_SQL, stmt -> {
            stmt.setInt(1, id);
        });
    }

    @Override
    public void update(LabWork item) {
        sqlExecutor.executeUpdate(UPDATE_SQL, stmt -> {
            stmt.setInt(1, item.getOwnerId());
            stmt.setString(2, item.getName());
            stmt.setLong(3, item.getMinimalPoint());
            stmt.setString(4, item.getDifficulty().name());
            stmt.setInt(5, item.getCoordinates().getX());
            stmt.setDouble(6, item.getCoordinates().getY());
            stmt.setString(7, item.getAuthor().getName());
            stmt.setDate(8, Date.valueOf(item.getAuthor().getBirthday()));
            stmt.setLong(9, item.getAuthor().getWeight());
            stmt.setString(10, item.getAuthor().getLocation().getName());
            stmt.setInt(11, item.getAuthor().getLocation().getX());
            stmt.setFloat(12, item.getAuthor().getLocation().getY());
            stmt.setInt(13, item.getId());
        });
    }

    @Override
    public int clearByOwnerId(int ownerId) {
        return sqlExecutor.executeUpdate(DELETE_BY_OWNER_SQL, stmt -> {
            stmt.setInt(1, ownerId);
        });
    }

    @Override
    public List<LabWork> getAll() {
        List<LabWork> labWorks = new ArrayList<>();
        sqlExecutor.executeQuery(SELECT_ALL_SQL, stmt -> {}, rs -> {
            while (rs.next()) labWorks.add(buildLabWorkFromResultSet(rs));
            return null;
        });
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
                .birthday(rs.getDate("author_birthday").toLocalDate())
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
                .difficulty(Difficulty.valueOf(rs.getString("difficulty").toUpperCase()))
                .coordinates(coordinates)
                .author(author)
                .build();
    }
}
