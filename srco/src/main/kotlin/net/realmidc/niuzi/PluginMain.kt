package net.realmidc.niuzi

import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.realmidc.niuzi.command.CommandHandler
import net.realmidc.niuzi.data.TDataManager
import net.realmidc.niuzi.data.sql.Dao
import net.realmidc.niuzi.util.AutoRefresher

/**
 * @author Ting
 * @date 2022/2/16 4:53 PM
 */
object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "net.realmidc.niuzi",
        version = "1.1-SNAPSHOT"
    ) {
        name("NiuZi")
        author("Mical")
    }
) {

    val PERMISSION_ADMIN by lazy {
        PermissionService.INSTANCE.register(
            permissionId("net.realmidc.niuzi.admin"),
            "NiuZi 管理员权限"
        )
    }

    override fun onEnable() {
        ConfigReader.reload()
        if (ConfigReader.firstEnable) {
            for (i in 1..5) {
                logger.warning("检测到你第一次运行插件，请去配置文件修改数据库信息，并把 firstEnable 改为 false。插件将不会继续加载")
            }
            return
        }
        Dao.connect()
        TDataManager.init()
        CommandHandler.init()
        AutoRefresher
        logger.info("达达没牛子")
    }

    override fun onDisable() {
        TDataManager.disable()
        logger.info("达达牛子被吃了")
    }

}