package net.realmidc.niuzi.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * @author Polar-Pumpkin
 * @date 2022/2/16 8:30 PM
 */
public class HikariCPUtil {

    private static final Map<KotlinPlugin, HikariDataSource> sqlConnectionPool = new HashMap<>();

    public static void setSqlConnectionPool(final KotlinPlugin plugin,
                                            final String address, final String port, final String database,
                                            final String username, final String password) {
        final HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.jdbc.Driver");

        config.setConnectionTimeout(30000);
        config.setMinimumIdle(10);
        config.setMaximumPoolSize(50);

        final String url = "jdbc:mysql://" + address + ":" + port + "/" + database
                + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai";
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);

        config.setAutoCommit(true);

        sqlConnectionPool.put(plugin, new HikariDataSource(config));
    }

    public static Connection getConnection(final KotlinPlugin plugin) {
        try {
            final HikariDataSource source = sqlConnectionPool.get(plugin);
            if (Objects.isNull(source)) {
                // plugin.getLang().log.error(I18n.GET, "数据库链接", "未初始化 SQL 链接池.");
                plugin.getLogger().error("获取值 数据库链接 时遇到错误(未初始化 SQL 链接池.).");
                return null;
            }
            return source.getConnection();
        } catch (SQLException e) {
            // plugin.getLang().log.error(I18n.GET, "数据库链接", e, plugin.getPackageName());
            plugin.getLogger().error("获取值 数据库链接 时遇到错误(" + e + ").", e);
        }
        return null;
    }

    public static void execute(@NotNull final KotlinPlugin plugin, @NotNull final String sql,
                               @Nullable final Function<PreparedStatement, PreparedStatement> args) {
        final Connection connection = getConnection(plugin);
        if (Objects.isNull(connection)) {
            return;
        }

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            if (Objects.nonNull(args)) {
                statement = args.apply(statement);
            }
            if (Objects.isNull(statement)) {
                return;
            }
            statement.execute();
        } catch (SQLException exception) {
            // plugin.getLang().log.error(I18n.EXECUTE, "MySQL 语句", exception, plugin.getPackageName());
            plugin.getLogger().error("执行 MySQL 语句 时遇到错误(" + exception + ").", exception);
        } finally {
            close(plugin, connection, statement, null);
        }
    }

    @Nullable
    public static <T> T query(@NotNull final KotlinPlugin plugin, @NotNull final String sql,
                              @NotNull final Function<ResultSet, T> getter,
                              @Nullable final Function<PreparedStatement, PreparedStatement> args) {
        final Connection connection = getConnection(plugin);
        if (Objects.isNull(connection)) {
            return null;
        }

        PreparedStatement statement = null;
        ResultSet result = null;
        T value = null;
        try {
            statement = connection.prepareStatement(sql);
            if (Objects.nonNull(args)) {
                statement = args.apply(statement);
            }
            if (Objects.isNull(statement)) {
                return null;
            }
            result = statement.executeQuery();
            value = getter.apply(result);
        } catch (SQLException exception) {
            // plugin.getLang().log.error(I18n.EXECUTE, "MySQL 语句", exception, plugin.getPackageName());
            plugin.getLogger().error("执行 MySQL 语句 时遇到错误(" + exception + ").", exception);
        } finally {
            close(plugin, connection, statement, result);
        }
        return value;
    }

    public static void close(@NotNull final KotlinPlugin plugin,
                             @Nullable final Connection connection,
                             @Nullable final PreparedStatement statement,
                             @Nullable final ResultSet result) {
        try {
            if (Objects.nonNull(connection)) {
                connection.close();
            }
            if (Objects.nonNull(statement)) {
                statement.close();
            }
            if (Objects.nonNull(result)) {
                result.close();
            }
        } catch (final SQLException e) {
            // plugin.getLang().log.error("关闭", "数据库链接", e, "serverct");
            plugin.getLogger().error("关闭 数据库链接 时遇到错误(" + e + ").", e);
        }
    }

}
