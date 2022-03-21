package net.realmidc.niuzi.data

import net.realmidc.niuzi.PluginMain
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.FileOutputStream

/**
 * 针对于CD冷却的数据存储
 */
open class TData(name: String, private val type: String, private val dataMap: HashMap<Long, Long>) {

    private val file = File(PluginMain.configFolder, "$name.yml")

    fun load() {
        if (!file.exists()) {
            file.createNewFile()
        }
        val config = YamlConfiguration.loadConfiguration(file)
        config.getKeys(false).forEach {
            val qq = it.toLong()
            val time = config.getLong(it)
            dataMap[qq] = time
        }
        PluginMain.logger.info("已成功从临时文件中扽（dèn）出来了${dataMap.size}条「$type」数据")
    }

    fun save() {
        if (!file.exists()) {
            file.createNewFile()
        }
        val out = FileOutputStream(file, false)
        out.write("".toByteArray(Charsets.UTF_8))
        out.close()
        val config = YamlConfiguration.loadConfiguration(file)
        dataMap.forEach { (t, u) -> config.set(t.toString(), u) }
        config.save(file)
        PluginMain.logger.info("已成功往临时文件中怼回去了${dataMap.size}条「$type」数据")
    }

}
