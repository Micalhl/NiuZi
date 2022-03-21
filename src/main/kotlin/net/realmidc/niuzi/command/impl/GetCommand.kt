package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.sql.Dao
import net.realmidc.niuzi.util.Locale.sendLang

class GetCommand : SubCommand {

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        if (Dao.getByQQ(sender.id) != null) {
            group.sendLang("Get.HasNiuZi")
            return
        }
        Dao.create(sender)
        group.sendLang("Get.Success")
    }

}