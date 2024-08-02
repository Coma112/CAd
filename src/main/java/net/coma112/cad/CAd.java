package net.coma112.cad;

import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import lombok.Getter;
import net.coma112.cad.config.Config;
import net.coma112.cad.database.AbstractDatabase;
import net.coma112.cad.database.MySQL;
import net.coma112.cad.database.SQLite;
import net.coma112.cad.enums.DatabaseType;
import net.coma112.cad.enums.LanguageType;
import net.coma112.cad.enums.keys.ConfigKeys;
import net.coma112.cad.language.Language;
import net.coma112.cad.utils.AdLogger;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.Objects;

import static net.coma112.cad.utils.StartingUtils.*;


public final class CAd extends JavaPlugin {
    @Getter
    private static CAd instance;
    @Getter
    private static AbstractDatabase database;
    @Getter
    private Language language;
    @Getter
    private TaskScheduler scheduler;
    private Config config;

    @Override
    public void onLoad() {
        instance = this;
        scheduler = UniversalScheduler.getScheduler(this);
        checkVersion();
    }

    @Override
    public void onEnable() {
        checkVM();

        saveDefaultConfig();
        initializeComponents();
        registerListenersAndCommands();
        initializeDatabaseManager();
        startExpirationCheckTask();
        checkUpdates();

        new Metrics(this, 22861);
    }

    @Override
    public void onDisable() {
        if (database != null) database.disconnect();
    }

    public Config getConfiguration() {
        return config;
    }

    private void initializeComponents() {
        config = new Config();

        saveResourceIfNotExists("locales/messages_en.yml");

        language = new Language("messages_" + LanguageType.valueOf(ConfigKeys.LANGUAGE.getString()));
    }

    private void initializeDatabaseManager() {
        try {
            switch (DatabaseType.valueOf(ConfigKeys.DATABASE.getString())) {
                case MYSQL, mysql -> {
                    database = new MySQL(Objects.requireNonNull(getConfiguration().getSection("database.mysql")));
                    MySQL mysql = (MySQL) database;
                    mysql.createTable();
                }

                case SQLITE, sqlite -> {
                    database = new SQLite();
                    SQLite sqlite = (SQLite) database;
                    sqlite.createTable();

                }
            }
        } catch (SQLException | ClassNotFoundException exception) {
            AdLogger.error(exception.getMessage());
        }
    }
}
