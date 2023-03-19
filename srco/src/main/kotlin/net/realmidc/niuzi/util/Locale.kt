package net.realmidc.niuzi.util

import net.mamoe.mirai.contact.Group
import net.realmidc.niuzi.PluginMain
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.function.Function

object Locale {

    private val file: File by lazy {
        val file = File(PluginMain.configFolder, "locale.yml")
        if (!file.exists()) {
            file.createNewFile()
            val content = PluginMain.getResource("locale.yml")
            if (content != null) {
                val config = YamlConfiguration()
                config.loadFromString(content)
                config.save(file)
            }
        }
        file
    }

    private val config = YamlConfiguration.loadConfiguration(file)

    fun getLang(path: String, vararg args: Any): String {
        if (config.contains(path)) {
            return if (config.isList(path)) {
                config.getStringList(path).joinToString("\n").replaceWithOrder(*args)
            } else {
                config.getString(path)!!.replaceWithOrder(*args)
            }
        }
        return ""
    }

    suspend fun Group.sendLang(path: String, vararg args: Any) {
        sendMessage(getLang(path, *args))
    }

}