package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.getMember
import net.mamoe.mirai.message.data.at
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.data.TempStorage
import net.realmidc.niuzi.data.sql.Dao
import net.realmidc.niuzi.util.Locale.sendLang

// FIXME: 下次优化一下代码，重复的地方好像有点多
class RequestCommand : SubCommand {

    override fun describe(): String = "管理你的请求"

    override fun usage(): String = "[搞对象/分手] [同意/不同意]"

    override fun needPerm(): Boolean = false

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        if (args.size == 2) {
            when (args[0]) {
                "搞对象" -> {
                    if (!TempStorage.lovedata.containsValue(sender.id)) {
                        group.sendLang("NoRequest")
                        return
                    }
                    when (args[1]) {
                        "同意" -> {
                            for (entry in TempStorage.lovedata.entries) {
                                if (entry.value == sender.id) {
                                    Dao.love(entry.key, sender.id)
                                    TempStorage.lovedata.remove(entry.key, entry.value)
                                    group.sendLang("Lover.Get.Request.Agree", group.getMember(entry.key)!!.at().getDisplay(group))
                                }
                            }
                        }
                        "不同意" -> {
                            for (entry in TempStorage.lovedata.entries) {
                                if (entry.value == sender.id) {
                                    TempStorage.lovedata.remove(entry.key, entry.value)
                                    group.sendLang("Lover.Get.Request.DisAgree", group.getMember(entry.key)!!.at().getDisplay(group))
                                }
                            }
                        }
                    }
                }
                "分手" -> {
                    if (!TempStorage.leavedata.containsValue(sender.id)) {
                        group.sendLang("NoRequest")
                        return
                    }
                    when (args[1]) {
                        "同意" -> {
                            for (entry in TempStorage.leavedata.entries) {
                                if (entry.value == sender.id) {
                                    Dao.leave(sender.id)
                                    TempStorage.leavedata.remove(entry.key, entry.value)
                                    group.sendLang("Lover.Leave.Request.Agree", group.getMember(entry.key)!!.at().getDisplay(group))
                                }
                            }
                        }
                        "不同意" -> {
                            for (entry in TempStorage.leavedata.entries) {
                                if (entry.value == sender.id) {
                                    TempStorage.leavedata.remove(entry.key, entry.value)
                                    group.sendLang("Lover.Leave.Request.DisAgree", group.getMember(entry.key)!!.at().getDisplay(group))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}