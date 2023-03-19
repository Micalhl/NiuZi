package net.realmidc.niuzi.util

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*
import java.util.function.Function

/**
 * Java code from ParrotX
 * @author Polar-Pumpkin
 * @date 2022/2/16 8:30 PM
 */
object HikariCP {

    private val sqlConnectionPool: MutableMap<KotlinPlugin, HikariDataSource> = HashMap()

    fun setSqlConnectionPool(
        plugin: KotlinPlugin,
        address: String, port: String, database: String,
        username: String?, password: String?
    ) {
        val config = HikariConfig()
        // 2022-12-12 20:44 记录一下发烧38.7℃，实在不想排查原因了，先用脑瘫办法尝试解决一下，回来再看。
        config.driverClassName = try {
            "com.mysql.jdbc.Driver"
        } catch (e: Throwable) {
            "com.mysql.jc.jdbc.Driver"
        }
        config.connectionTimeout = 30000
        config.minimumIdle = 10
        config.maximumPoolSize = 50
        val url = ("jdbc:mysql://" + address + ":" + port + "/" + database
                + "?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai")
        config.jdbcUrl = url
        config.username = username
        config.password = password
        config.isAutoCommit = true
        sqlConnectionPool[plugin] = HikariDataSource(config)
    }

    private fun getConnection(plugin: KotlinPlugin): Connection? {
        try {
            val source = sqlConnectionPool[plugin]
            if (Objects.isNull(source)) {
                // plugin.getLang().log.error(I18n.GET, "数据库链接", "未初始化 SQL 链接池.");
                plugin.logger.error("获取值 数据库链接 时遇到错误(未初始化 SQL 链接池.).")
                return null
            }
            return source!!.connection
        } catch (e: SQLException) {
            // plugin.getLang().log.error(I18n.GET, "数据库链接", e, plugin.getPackageName());
            plugin.logger.error("获取值 数据库链接 时遇到错误($e).", e)
        }
        return null
    }

    fun execute(
        plugin: KotlinPlugin, sql: String,
        args: Function<PreparedStatement?, PreparedStatement?>?
    ) {
        val connection = getConnection(plugin)
        if (Objects.isNull(connection)) {
            return
        }
        var statement: PreparedStatement? = null
        try {
            statement = connection!!.prepareStatement(sql)
            if (Objects.nonNull(args)) {
                statement = args!!.apply(statement)
            }
            if (Objects.isNull(statement)) {
                return
            }
            statement!!.execute()
        } catch (exception: SQLException) {
            // plugin.getLang().log.error(I18n.EXECUTE, "MySQL 语句", exception, plugin.getPackageName());
            plugin.logger.error("执行 MySQL 语句 时遇到错误($exception).", exception)
        } finally {
            close(plugin, connection, statement, null)
        }
    }

    fun <T> query(
        plugin: KotlinPlugin, sql: String,
        getter: (ResultSet) -> T,
        args: Function<PreparedStatement?, PreparedStatement?>?
    ): T? {
        val connection = getConnection(plugin)
        if (Objects.isNull(connection)) {
            return null
        }
        var statement: PreparedStatement? = null
        var result: ResultSet? = null
        var value: T? = null
        try {
            statement = connection!!.prepareStatement(sql)
            if (Objects.nonNull(args)) {
                statement = args!!.apply(statement)
            }
            if (Objects.isNull(statement)) {
                return null
            }
            result = statement!!.executeQuery()
            value = getter.invoke(result)
        } catch (exception: SQLException) {
            // plugin.getLang().log.error(I18n.EXECUTE, "MySQL 语句", exception, plugin.getPackageName());
            plugin.logger.error("执行 MySQL 语句 时遇到错误($exception).", exception)
        } finally {
            close(plugin, connection, statement, result)
        }
        return value
    }

    fun close(
        plugin: KotlinPlugin,
        connection: Connection?,
        statement: PreparedStatement?,
        result: ResultSet?
    ) {
        try {
            if (Objects.nonNull(connection)) {
                connection!!.close()
            }
            if (Objects.nonNull(statement)) {
                statement!!.close()
            }
            if (Objects.nonNull(result)) {
                result!!.close()
            }
        } catch (e: SQLException) {
            // plugin.getLang().log.error("关闭", "数据库链接", e, "serverct");
            plugin.logger.error("关闭 数据库链接 时遇到错误($e).", e)
        }
    }

}