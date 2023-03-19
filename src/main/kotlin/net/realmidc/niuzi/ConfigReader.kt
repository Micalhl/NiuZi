package net.realmidc.niuzi

import taboolib.library.configuration.ConfigurationSection
import taboolib.module.configuration.Config
import taboolib.module.configuration.ConfigNode
import taboolib.module.configuration.Configuration

/**
 * NiuZi
 * net.realmidc.niuzi.ConfigReader
 *
 * @author mical
 * @since 2023/3/19 11:32 AM
 */
object ConfigReader {

    @Config(autoReload = true)
    lateinit var config: Configuration
        private set

    @ConfigNode
    var language = "zh_CN"

    val database: ConfigurationSection?
        get() = config.getConfigurationSection("database")
}