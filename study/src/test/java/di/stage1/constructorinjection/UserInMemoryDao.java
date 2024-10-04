package di.stage1.constructorinjection;

import di.User;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

class UserInMemoryDao implements InMemoryDao {

    private static final Map<Long, User> users = new HashMap<>();

    private final JdbcDataSource dataSource;

    public UserInMemoryDao() {
        final var jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;");
        jdbcDataSource.setUser("");
        jdbcDataSource.setPassword("");

        this.dataSource = jdbcDataSource;
    }

    public void insert(User user) {
        try (final var connection = dataSource.getConnection()) {
            users.put(user.getId(), user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User findById(long id) {
        try (final var connection = dataSource.getConnection()) {
            return users.get(id);
        } catch (SQLException e) {
            return null;
        }
    }
}
