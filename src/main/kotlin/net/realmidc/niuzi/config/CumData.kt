package net.realmidc.niuzi.config

import net.realmidc.niuzi.PluginMain
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object CumData {

    val dataMap = hashMapOf<Long, Long>()

    fun load() {
        val file = File("config/NiuZi/cumdata.yml")
        if (!file.exists()) {
            file.createNewFile()
        }
        val config = YamlConfiguration.loadConfiguration(file)
        config.getKeys(false).forEach {
            val qq = it.toLong()
            val time = config.getLong(it)
            dataMap[qq] = time
        }
        PluginMain.logger.info("已成功从临时文件中扽（dèn）出来了${dataMap.size}条「爽一下」数据")
    }

    fun save() {
        val file = File("config/NiuZi/cumdata.yml")
        if (!file.exists()) {
            file.createNewFile()
        }
        val config = YamlConfiguration.loadConfiguration(file)
        dataMap.forEach { (t, u) -> config.set(t.toString(), u) }
        config.save(file)
        PluginMain.logger.info("已成功往临时文件中怼回去了${dataMap.size}条「爽一下」数据")
    }

}