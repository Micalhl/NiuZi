package net.realmidc.niuzi

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.realmidc.niuzi.command.CommandHandler
import net.realmidc.niuzi.data.CumData
import net.realmidc.niuzi.data.HongZhongData
import net.realmidc.niuzi.config.Settings
import net.realmidc.niuzi.sql.Dao
import net.realmidc.niuzi.util.AutoRefresher
import net.realmidc.niuzi.util.Locale

/**
 * @author Ting
 * @date 2022/2/16 4:53 PM
 */
object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "net.realmidc.niuzi",
        version = "1.0-SNAPSHOT"
    ) {
        name("NiuZi")
        author("Mical")
    }
) {

    val admins = arrayListOf(
        3332366064L
    )

    override fun onEnable() {
        Settings.reload()
        if (Settings.firstEnable) {
            for (i in 1..5) {
                logger.warning("检测到你第一次运行插件，请去配置文件修改数据库信息，并把 firstEnable 改为 false。插件将不会继续加载")
            }
            return
        }
        Dao.connect()
        HongZhongData.load()
        CumData.load()
        CommandHandler.init()
        AutoRefresher
        logger.info("达达没牛子")
        println(Locale.file)
    }

    override fun onDisable() {
        HongZhongData.save()
        CumData.save()
        logger.info("达达牛子被吃了")
    }

}