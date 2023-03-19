package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.getMember
import net.mamoe.mirai.message.data.at
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.data.TempStorage
import net.realmidc.niuzi.data.sql.Dao
import net.realmidc.niuzi.util.Locale.sendLang

class LeaveCommand : SubCommand {

    override fun describe(): String = "和你的对象分手"

    override fun usage(): String? = null

    override fun needPerm(): Boolean = false

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        if (!Dao.hasLover(sender.id)) {
            group.sendLang("Lover.Leave.NoLover")
            return
        }
        val target = Dao.getLover(sender.id)
        TempStorage.leavedata[sender.id] = target
        group.sendLang("Lover.Leave.Request.Send", group.getMember(target)!!.at().getDisplay(group), sender.at().getDisplay(group))
    }
}