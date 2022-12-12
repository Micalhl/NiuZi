package net.realmidc.niuzi

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

/**
 * @author Ting
 * @date 2022/2/17 3:25 PM
 */
object ConfigReader : AutoSavePluginConfig("settings") {

    val firstEnable: Boolean by value(true)
    val pkCd: Int by value(3600)
    val databaseAddress: String by value("localhost")
    val databasePort: Int by value(3306)
    val databaseName: String by value("laoshu")
    val databaseUser: String by value("laoshu")
    val databasePassword: String by value("password")
}