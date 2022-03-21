package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.data.at
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.data.TempStorage
import net.realmidc.niuzi.sql.Dao
import net.realmidc.niuzi.util.Locale.sendLang

class RequestCommand : SubCommand {

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
                                }
                                TempStorage.lovedata.remove(entry.key, entry.value)
                            }
                            group.sendLang("Lover.Get.Request.Agree") {
                                it?.replace("{0}", sender.at().getDisplay(group))
                            }
                        }
                        "不同意" -> {
                            group.sendLang("Lover.Get.Request.DisAgree") {
                                it?.replace("{0}", sender.at().getDisplay(group))
                            }
                        }
                    }
                }
                "分手" -> {
                    if (!TempStorage.lovedata.containsValue(sender.id)) {
                        group.sendLang("NoRequest")
                        return
                    }
                    when (args[1]) {
                        "同意" -> {
                            for (entry in TempStorage.lovedata.entries) {
                                if (entry.value == sender.id) {
                                    Dao.leave(sender.id)
                                    TempStorage.lovedata.remove(entry.key, entry.value)
                                }
                            }
                            group.sendLang("Lover.Leave.Request.Agree") {
                                it?.replace("{0}", sender.at().getDisplay(group))
                            }
                        }
                        "不同意" -> {
                            group.sendLang("Lover.Leave.Request.DisAgree") {
                                it?.replace("{0}", sender.at().getDisplay(group))
                            }
                        }
                    }
                }
            }
        }
    }

}