package net.realmidc.niuzi.command

import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.event.subscribeGroupMessages
import net.realmidc.niuzi.PluginMain
import net.realmidc.niuzi.command.impl.*
import net.realmidc.niuzi.util.Locale

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
        registerCommands("我要分手", LeaveCommand())

        PluginMain.globalEventChannel().subscribeGroupMessages {
            startsWith("", removePrefix = true) {
                val command = it.split(" ")
                val root = command[0]
                val args = command.drop(1)
                if (root == "牛子系统") {
                    val builder = StringBuilder()
                    builder.append(Locale.getLang("CommandHeader") + "\n")
                    commands.forEach { (name, executor) ->
                        if (executor.needPerm()) {
                            if (!PluginMain.admins.contains(sender.id)) {
                                return@forEach
                            }
                        }
                        builder.append(Locale.getLang("CommandHelper") { str ->
                            str?.replace("{0}", name)
                                ?.replace("{1}", executor.usage() ?: "")
                                ?.replace("{2}", executor.describe() ?: "")
                        } + "\n")
                    }
                    group.sendMessage(builder.toString())
                }
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