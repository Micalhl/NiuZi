package net.realmidc.niuzi.util

import net.mamoe.mirai.contact.Group
import net.realmidc.niuzi.PluginMain
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.text.MessageFormat

object Locale {

    val file: File by lazy {
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

    suspend fun Group.sendLang(path: String, vararg args: Any) {
        if (config.contains(path)) {
            if (config.isList(path)) {
                sendMessage(MessageFormat.format(config.getStringList(path).joinToString("\n"), args))
            } else {
                sendMessage(MessageFormat.format(config.getString(path)!!, args))
            }
        }
    }

}