package net.realmidc.niuzi

import net.mamoe.mirai.console.extension.PluginComponentStorage
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.realmidc.niuzi.util.sendLang
import taboolib.common.TabooLibCommon
import taboolib.common.platform.function.info
import taboolib.platform.AppIO

/**
 * NiuZi
 * net.realmidc.niuzi.NiuZi
 *
 * @author mical
 * @since 2022/2/16 4:53 PM
 */
object NiuZi : KotlinPlugin(
    JvmPluginDescription(
        id = "net.realmidc.niuzi",
        version = "2.0.0"
    ) {
        name("NiuZi")
        author("Mical")
    }
) {

    override fun PluginComponentStorage.onLoad() {
        // 设置数据目录
        AppIO.nativeDataFolder = configFolder
        // 初始化 TabooLib
        TabooLibCommon.testSetup()
    }

    override fun onEnable() {
        info("Successfully running ExamplePlugin!")
        info(ConfigReader.config.getString("test"))
        sendLang("test", "运行在 Mirai 上!")
    }
}