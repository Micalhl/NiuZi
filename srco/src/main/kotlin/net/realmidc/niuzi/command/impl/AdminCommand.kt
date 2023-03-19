package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.console.command.CommandSender.Companion.asMemberCommandSender
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.getMember
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.data.sql.Dao
import net.realmidc.niuzi.util.Locale.sendLang
import net.realmidc.niuzi.util.checkNumber
import net.realmidc.niuzi.util.getAt
import net.realmidc.niuzi.util.permitted
import net.realmidc.niuzi.util.randomDouble

@Deprecated("等待重构")
class AdminCommand : SubCommand {

    override fun describe(): String = "管理员命令"

    override fun usage(): String? = null

    override fun needPerm(): Boolean = true

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        if (sender.asMemberCommandSender().permitted()) {
            if (args.isNotEmpty()) {
                val cmd0 = args[0]
                val temp0 = args.drop(1)
                when (cmd0) {
                    "牛子系统" -> {
                        if (temp0.isNotEmpty()) {
                            val cmd1 = temp0[0]
                            val temp1 = temp0.drop(1)
                            when (cmd1) {
                                "查看牛子" -> {
                                    if (temp1.isEmpty()) {
                                        group.sendLang("Admin.View.NoArgs")
                                        return
                                    }
                                    if (temp1.size == 1) {
                                        val target = getAt(group, temp1[0], true)
                                        if (target != -1L) {
                                            StatusCommand.get(group, group.getMember(target)!!)
                                        }
                                    }
                                }
                                "更改长度" -> {
                                    if (temp1.isEmpty()) {
                                        group.sendLang("Admin.Change.NoArgs")
                                        return
                                    }
                                    if (temp1.size == 1) {
                                        group.sendLang("Admin.Change.NoLength")
                                        return
                                    }
                                    if (temp1.size == 2) {
                                        val target = getAt(group, temp1[0], true)
                                        if (target != -1L) {
                                            val length = temp1[1]
                                            if (length == "随机长度") {
                                                Dao.setLength(target, randomDouble(10))
                                                group.sendLang("Success")
                                            } else {
                                                if (checkNumber(group, length)) {
                                                    Dao.setLength(target, length.toDouble())
                                                    group.sendLang("Success")
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}