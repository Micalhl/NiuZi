package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.sql.Dao
import net.realmidc.niuzi.util.Locale.sendLang

class StatusCommand : SubCommand {

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        val niuzi = Dao.getByQQ(sender.id)
        if (niuzi == null) {
            group.sendLang("Status.NoNiuZi")
            return
        }
        group.sendLang("Status.Status", sender.nameCard, sender.id, niuzi.name, niuzi.sex.toChinese(), niuzi.length)
    }

}