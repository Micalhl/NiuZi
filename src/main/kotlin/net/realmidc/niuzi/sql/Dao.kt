package net.realmidc.niuzi.sql

import net.mamoe.mirai.contact.Member
import net.realmidc.niuzi.PluginMain
import net.realmidc.niuzi.config.Settings.databaseAddress
import net.realmidc.niuzi.config.Settings.databaseName
import net.realmidc.niuzi.config.Settings.databasePassword
import net.realmidc.niuzi.config.Settings.databasePort
import net.realmidc.niuzi.config.Settings.databaseUser
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
    }

    /**
     * 领养
     *
     * @param member 群成员
     */
    fun create(member: Member) {
        val name = member.nameCard + "的牛子"
        val qq = member.id
        val random = Random().nextInt(10).toDouble()
        val length = Random().nextDouble() + random
        val temp = Random().nextInt(2)
        //final Sex sex = temp == 0 ? Sex.MALE : Sex.FEMALE;
        HikariCP.execute(
            PluginMain,
            "INSERT INTO niuzi_data (qq,name,length,sex,level,points) VALUE($qq,'$name',$length,$temp,0,0);",
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

}