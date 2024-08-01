package net.coma112.cad.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import net.coma112.cad.CAd;
import net.coma112.cad.manager.Advertisement;
import net.coma112.cad.utils.AdLogger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
public class MySQL extends AbstractDatabase {
    private final Connection connection;

    public MySQL(@NotNull ConfigurationSection section) throws SQLException {
        HikariConfig hikariConfig = new HikariConfig();

        String host = section.getString("host");
        String database = section.getString("database");
        String user = section.getString("username");
        String pass = section.getString("password");
        int port = section.getInt("port");
        boolean ssl = section.getBoolean("ssl");
        boolean certificateVerification = section.getBoolean("certificateverification");
        int poolSize = section.getInt("poolsize");
        int maxLifetime = section.getInt("lifetime");

        hikariConfig.setPoolName("AdPool");
        hikariConfig.setMaximumPoolSize(poolSize);
        hikariConfig.setMaxLifetime(maxLifetime * 1000L);
        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(pass);

        hikariConfig.addDataSourceProperty("useSSL", String.valueOf(ssl));
        if (!certificateVerification) hikariConfig.addDataSourceProperty("verifyServerCertificate", String.valueOf(false));
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("encoding", "UTF-8");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("jdbcCompliantTruncation", "false");
        hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
        hikariConfig.addDataSourceProperty("rewriteBatchedStatements", "true");
        hikariConfig.addDataSourceProperty("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30)));
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "275");
        hikariConfig.addDataSourceProperty("useUnicode", "true");

        try (HikariDataSource dataSource = new HikariDataSource(hikariConfig)) {
            connection = dataSource.getConnection();
        }
    }

    @Override
    public void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS ads (ID INT AUTO_INCREMENT PRIMARY KEY, PLAYER VARCHAR(255) NOT NULL, TITLE VARCHAR(2048) NOT NULL, DESCRIPTION VARCHAR(2048) NOT NULL, STARTING_DATE VARCHAR NOT NULL, ENDING_DATE VARCHAR NOT NULL)";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
            preparedStatement.execute();
        } catch (SQLException exception) {
            AdLogger.error(exception.getMessage());
        }
    }

    @Override
    public void createAdvertisement(@NotNull Player player, @NotNull String title, @NotNull String description, @NotNull String startingDate, @NotNull String endingDate) {
        String insertQuery = "INSERT INTO ads (PLAYER, TITLE, DESCRIPTION, STARTING_DATE, ENDING_DATE) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(insertQuery)) {
            preparedStatement.setString(1, player.getName());
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, description);
            preparedStatement.setString(4, startingDate);
            preparedStatement.setString(5, endingDate);
            preparedStatement.execute();
        } catch (SQLException exception) {
            AdLogger.error(exception.getMessage());
        }
    }

    @Override
    public void deleteAdvertisement(int id) {
        String deleteQuery = "DELETE FROM ads WHERE ID = ?";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            AdLogger.error(exception.getMessage());
        }
    }

    @Override
    public boolean exists(int id) {
        String query = "SELECT * FROM ads WHERE ID = ?";

        try {
            try (PreparedStatement preparedStatement = getConnection().prepareStatement(query)) {
                preparedStatement.setInt(1, id);

                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public List<Advertisement> getAdvertisements() {
        List<Advertisement> advertisements = new ArrayList<>();
        String query = "SELECT * FROM ads";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String player = resultSet.getString("PLAYER");
                String title = resultSet.getString("TITLE");
                String description = resultSet.getString("DESCRIPTION");
                String startingDate = resultSet.getString("STARTING_DATE");
                String endingDate = resultSet.getString("ENDING_DATE");

                advertisements.add(new Advertisement(id, player, title, description, startingDate, endingDate));
            }
        } catch (SQLException exception) {
            AdLogger.error(exception.getMessage());
        }

        return advertisements;
    }

    @Override
    public void deleteExpiredAdvertisements() {
        String deleteQuery = "DELETE FROM ads WHERE ENDING_DATE <= CURRENT_TIMESTAMP";

        try (PreparedStatement preparedStatement = getConnection().prepareStatement(deleteQuery)) {
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            AdLogger.error(exception.getMessage());
        }
    }

    @Override
    public List<Advertisement> getAdvertisementsFromPlayer(@NotNull Player playerToGet) {
        List<Advertisement> advertisements = new ArrayList<>();
        String query = "SELECT * FROM ads WHERE PLAYER = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, playerToGet.getName());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String player = resultSet.getString("PLAYER");
                String title = resultSet.getString("TITLE");
                String description = resultSet.getString("DESCRIPTION");
                String startingDate = resultSet.getString("STARTING_DATE");
                String endingDate = resultSet.getString("ENDING_DATE");

                advertisements.add(new Advertisement(id, player, title, description, startingDate, endingDate));
            }
        } catch (SQLException exception) {
            AdLogger.error(exception.getMessage());
        }

        return advertisements;
    }

    @Override
    public int getAdvertisementsCount(@NotNull Player player) {
        String query = "SELECT COUNT(*) AS count FROM ads WHERE PLAYER = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, player.getName());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) return resultSet.getInt("count");
        } catch (SQLException exception) {
            AdLogger.error(exception.getMessage());
        }
        return 0;
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException exception) {
                AdLogger.error(exception.getMessage());
            }
        }
    }
}
