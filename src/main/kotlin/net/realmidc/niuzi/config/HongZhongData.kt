package net.realmidc.niuzi.config

import net.realmidc.niuzi.PluginMain
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

/**
 * 牛子红肿了的剩余时间保存到临时数据文件里，
 * 要不然重启机器人就可以继续比划了。
 * 我太菜了不会用 mirai 的配置文件，
 * 所以我用了 bukkit 的。
 * @author Ting
 * @date 2022/3/6 11:09 AM
 */
object HongZhongData {

    val dataMap = hashMapOf<Long, Long>()

    fun load() {
        val file = File("config/NiuZi/hongzhongdata.yml")
        if (!file.exists()) {
            file.createNewFile()
        }
        val config = YamlConfiguration.loadConfiguration(file)
        config.getKeys(false).forEach {
            val qq = it.toLong()
            val time = config.getLong(it)
            dataMap[qq] = time
        }
        PluginMain.logger.info("已成功从临时文件中扽（dèn）出来了${dataMap.size}条红肿数据")
    }

    fun save() {
        val file = File("config/NiuZi/hongzhongdata.yml")
        if (!file.exists()) {
            file.createNewFile()
        }
        val config = YamlConfiguration.loadConfiguration(file)
        dataMap.forEach { (t, u) -> config.set(t.toString(), u) }
        config.save(file)
        PluginMain.logger.info("已成功往临时文件中怼回去了${dataMap.size}条红肿数据")
    }

}