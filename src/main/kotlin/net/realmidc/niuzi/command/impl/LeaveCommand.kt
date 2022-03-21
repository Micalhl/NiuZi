package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.getMember
import net.mamoe.mirai.message.data.at
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.data.TempStorage
import net.realmidc.niuzi.sql.Dao
import net.realmidc.niuzi.util.Locale.sendLang

class LeaveCommand : SubCommand {

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        if (!Dao.hasLover(sender.id)) {
            group.sendLang("Lover.Leave.NoLover")
            return
        }
        val target = Dao.getLover(sender.id)
        TempStorage.leavedata[sender.id] = target
        group.sendLang("Lover.Leave.Request.Send") {
            it?.replace("{0}", group.getMember(target)!!.at().getDisplay(group))
                ?.replace("{1}", sender.at().getDisplay(group))
        }
    }

}