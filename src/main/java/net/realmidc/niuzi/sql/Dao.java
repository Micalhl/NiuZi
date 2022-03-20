package net.realmidc.niuzi.sql;

import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin;
import net.mamoe.mirai.contact.Member;
import net.realmidc.niuzi.config.Settings;
import net.realmidc.niuzi.entity.NiuZi;
import net.realmidc.niuzi.enums.Sex;
import net.realmidc.niuzi.util.HikariCPUtil;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.Random;

/**
 * @author Ting
 * @date 2022/2/16 8:55 PM
 */
public class Dao {

    private final KotlinPlugin plugin;

    public Dao(KotlinPlugin plugin) {
        this.plugin = plugin;
        plugin.getLogger().info("牛子数据库跑起来了");
    }

    public void connect() {
        HikariCPUtil.setSqlConnectionPool(
                plugin,
                Settings.INSTANCE.getDatabaseAddress(),
                String.valueOf(Settings.INSTANCE.getDatabasePort()),
                Settings.INSTANCE.getDatabaseName(),
                Settings.INSTANCE.getDatabaseUser(),
                Settings.INSTANCE.getDatabasePassword()
        );
        HikariCPUtil.execute(
                plugin,
                "CREATE TABLE IF NOT EXISTS `niuzi_data` (`qq` BIGINT UNIQUE NOT NULL,`name` TEXT, `length` DOUBLE, `sex` INTEGER DEFAULT 0, `level` INT DEFAULT 0, `points` INT DEFAULT 0, PRIMARY KEY (`qq`))",
                null
        );
    }

    /**
     * 领养
     *
     * @param member 群成员
     */
    public void create(Member member) {
        final String name = member.getNameCard() + "的牛子";
        final long qq = member.getId();
        final double random = new Random().nextInt(10);
        final double length = new Random().nextDouble() + random;
        final int temp = new Random().nextInt(2);
        //final Sex sex = temp == 0 ? Sex.MALE : Sex.FEMALE;
        HikariCPUtil.execute(
                plugin,
                "INSERT INTO niuzi_data (qq,name,length,sex,level,points) VALUE(" + qq + ",'" + name + "'," + length + "," + temp + "," + 0 + "," + 0 + ");",
                null
        );
    }

    @Nullable
    public NiuZi getByQQ(long qq) {
        return HikariCPUtil.query(
                plugin,
                "SELECT * FROM `niuzi_data` WHERE `qq` = " + qq,
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            String name = resultSet.getString(2);
                            double length = resultSet.getDouble(3);
                            int temp = resultSet.getInt(4);
                            Sex sex = temp == 0 ? Sex.MALE : Sex.FEMALE;
                            int level = resultSet.getInt(5);
                            int points = resultSet.getInt(6);
                            return new NiuZi(qq, name, length, sex, level, points);
                        }
                    } catch (SQLException exception) {
                        plugin.getLogger().error("执行 MySQL 语句 时遇到错误(" + exception + ").", exception);
                    }
                    return null;
                },
                null
        );
    }

    public void changeName(long qq, String name) {
        HikariCPUtil.execute(
                plugin,
                "UPDATE `niuzi_data` SET `name` = '" + name + "' WHERE `qq` = " + qq,
                null
        );
    }

    public void setLength(long qq, double length) {
        HikariCPUtil.execute(
                plugin,
                "UPDATE `niuzi_data` SET `length` = " + length + " WHERE `qq` = " + qq,
                null
        );
    }

}
