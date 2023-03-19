package net.realmidc.niuzi.data.sql

import net.mamoe.mirai.contact.Group
import net.realmidc.niuzi.PluginMain
import net.realmidc.niuzi.ConfigReader.databaseAddress
import net.realmidc.niuzi.ConfigReader.databaseName
import net.realmidc.niuzi.ConfigReader.databasePassword
import net.realmidc.niuzi.ConfigReader.databasePort
import net.realmidc.niuzi.ConfigReader.databaseUser
import net.realmidc.niuzi.entity.NiuZi
import net.realmidc.niuzi.enums.Sex
import net.realmidc.niuzi.util.HikariCP
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

object Dao {

    fun connect() {
        HikariCP.setSqlConnectionPool(
            PluginMain,
            databaseAddress, databasePort.toString(),
            databaseName,
            databaseUser,
            databasePassword
        )
        HikariCP.execute(
            PluginMain,
            "CREATE TABLE IF NOT EXISTS `niuzi_data` (`qq` BIGINT UNIQUE NOT NULL,`name` TEXT, `length` DOUBLE, `sex` INTEGER DEFAULT 0, `level` INT DEFAULT 0, `points` INT DEFAULT 0, PRIMARY KEY (`qq`))",
            null
        )
        HikariCP.execute(
            PluginMain,
            "CREATE TABLE IF NOT EXISTS `lovers_data` (`qq` BIGINT UNIQUE NOT NULL,`target` BIGINT)",
            null
        )
    }

    /**
     * 领养
     *
     * @param member 群成员
     */
    fun create(qq: Long) {
        val random = Random().nextInt(10).toDouble()
        val length = Random().nextDouble() + random
        val temp = Random().nextInt(2)
        HikariCP.execute(
            PluginMain,
            "INSERT INTO niuzi_data (qq,name,length,sex,level,points) VALUE($qq,'牛子',$length,$temp,0,0);",
            null
        )
    }

    fun getByQQ(qq: Long): NiuZi? {
        return HikariCP.query(
            PluginMain,
            "SELECT * FROM `niuzi_data` WHERE `qq` = $qq",
            { resultSet: ResultSet ->
                try {
                    if (resultSet.next()) {
                        val name = resultSet.getString(2)
                        val length = resultSet.getDouble(3)
                        val temp = resultSet.getInt(4)
                        val sex =
                            if (temp == 0) Sex.MALE else Sex.FEMALE
                        val level = resultSet.getInt(5)
                        val points = resultSet.getInt(6)
                        return@query NiuZi(qq, name, length, sex, level, points)
                    }
                } catch (exception: SQLException) {
                    PluginMain.logger.error("执行 MySQL 语句 时遇到错误($exception).", exception)
                }
                null
            },
            null
        )
    }

    fun getAll(group: Group): List<NiuZi> {
        val result = arrayListOf<NiuZi>()
        HikariCP.query(
            PluginMain,
            "SELECT * FROM `niuzi_data`",
            { resultSet ->
                if (resultSet.next()) {
                    do {
                        val qq = resultSet.getLong(1)
                        val name = resultSet.getString(2)
                        val length = resultSet.getDouble(3)
                        val temp = resultSet.getInt(4)
                        val sex =
                            if (temp == 0) Sex.MALE else Sex.FEMALE
                        val level = resultSet.getInt(5)
                        val points = resultSet.getInt(6)
                        result.add(NiuZi(qq, name, length, sex, level, points))
                    } while (resultSet.next())
                }
            },
            null
        )
        return result.filter { group.members.map { it.id }.contains(it.owner) }.sortedBy { it.length }.reversed()
    }

    fun changeSex(qq: Long, sex: Sex) {
        HikariCP.execute(
            PluginMain,
            "UPDATE `niuzi_data` SET `sex` = '${if (sex == Sex.MALE) 0 else 1}' WHERE `qq` = $qq",
            null
        )
    }

    fun changeName(qq: Long, name: String) {
        HikariCP.execute(
            PluginMain,
            "UPDATE `niuzi_data` SET `name` = '$name' WHERE `qq` = $qq",
            null
        )
    }

    fun setLength(qq: Long, length: Double) {
        HikariCP.execute(
            PluginMain,
            "UPDATE `niuzi_data` SET `length` = $length WHERE `qq` = $qq",
            null
        )
    }

    fun hasLover(qq: Long): Boolean {
        val list1: Boolean = HikariCP.query(
            PluginMain,
            "SELECT * FROM `lovers_data` WHERE `qq` = $qq",
            { resultSet ->
                try {
                    return@query resultSet.next()
                } catch (exception: SQLException) {
                    PluginMain.logger.error("执行 MySQL 语句 时遇到错误($exception).", exception)
                }
                return@query false
            },
            null
        ) ?: false
        return if (list1) true else {
            return HikariCP.query(
                PluginMain,
                "SELECT * FROM `lovers_data` WHERE `target` = $qq",
                { resultSet ->
                    try {
                        return@query resultSet.next()
                    } catch (exception: SQLException) {
                        PluginMain.logger.error("执行 MySQL 语句 时遇到错误($exception).", exception)
                    }
                    return@query false
                },
                null
            ) ?: false
        }
    }

    fun love(qq: Long, target: Long) {
        HikariCP.execute(
            PluginMain,
            "INSERT INTO lovers_data (qq,target) VALUE($qq,$target);",
            null
        )
    }

    fun leave(qq: Long) {
        HikariCP.query(
            PluginMain,
            "SELECT * FROM `lovers_data` WHERE `qq` = $qq",
            { resultSet ->
                try {
                    if (resultSet.next()) {
                        HikariCP.execute(PluginMain, "DELETE FROM `lovers_data` WHERE `qq`=$qq", null)
                    } else {
                        HikariCP.execute(PluginMain, "DELETE FROM `lovers_data` WHERE `target`=$qq", null)
                    }
                } catch (exception: SQLException) {
                    PluginMain.logger.error("执行 MySQL 语句 时遇到错误($exception).", exception)
                }
            },
            null
        )
    }

    fun getLover(qq: Long): Long {
        val list1: Long = HikariCP.query(
            PluginMain,
            "SELECT * FROM `lovers_data` WHERE `qq` = $qq",
            { resultSet ->
                try {
                    if (resultSet.next()) {
                        return@query resultSet.getLong(2)
                    }
                } catch (exception: SQLException) {
                    PluginMain.logger.error("执行 MySQL 语句 时遇到错误($exception).", exception)
                }
                return@query -1L
            },
            null
        ) ?: -1L
        return if (list1 != -1L) list1 else {
            return HikariCP.query(
                PluginMain,
                "SELECT * FROM `lovers_data` WHERE `target` = $qq",
                { resultSet ->
                    try {
                        if (resultSet.next()) {
                            return@query resultSet.getLong(1)
                        }
                    } catch (exception: SQLException) {
                        PluginMain.logger.error("执行 MySQL 语句 时遇到错误($exception).", exception)
                    }
                    return@query -1L
                },
                null
            ) ?: -1L
        }
    }
}