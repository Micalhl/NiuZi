package net.realmidc.niuzi.command

import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.event.subscribeGroupMessages
import net.realmidc.niuzi.PluginMain
import net.realmidc.niuzi.command.impl.*

object CommandHandler {

    private val commands = hashMapOf<String, SubCommand>()

    fun init() {
        registerCommands("领养牛子", GetCommand())
        registerCommands("我的牛子", StatusCommand())
        registerCommands("改牛子名", NameCommand())
        registerCommands("爽一下", CumCommand())
        registerCommands("比划比划", PKCommand())
        registerCommands("管理命令", AdminCommand())
        registerCommands("我的对象", LoverCommand())
        registerCommands("搞对象", LoveRequestCommand())
        registerCommands("贴贴！", DoiCommand())
        registerCommands("处理请求", RequestCommand())
        registerCommands("分手", LeaveCommand())

        PluginMain.globalEventChannel().subscribeGroupMessages {
            startsWith("", removePrefix = true) {
                val command = it.split(" ")
                val root = command[0]
                val args = command.drop(1)
                commands.forEach { (name, executor) -> if (root == name) executor.execute(sender, group, args) }
            }
        }
    }

    private fun registerCommands(name: String, executor: SubCommand) {
        if (commands.containsKey(name)) {
            PluginMain.logger.error("重复注册子命令：$name")
            return
        }
        commands[name] = executor
    }

}