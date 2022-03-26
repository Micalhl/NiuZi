package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.nameCardOrNick
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.sql.Dao
import net.realmidc.niuzi.util.Locale.sendLang

class StatusCommand : SubCommand {

    override fun describe(): String = "查看你的牛子"

    override fun usage(): String? = null

    override fun needPerm(): Boolean = false

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        get(group, sender)
    }

    companion object {
        suspend fun get(group: Group, member: Member) {
            val niuzi = Dao.getByQQ(member.id)
            if (niuzi == null) {
                group.sendLang("Status.NoNiuZi")
                return
            }
            group.sendLang("Status.Status") {
                it?.replace("{0}", member.nameCardOrNick)
                    ?.replace("{1}", member.id.toString())
                    ?.replace("{2}", niuzi.name)
                    ?.replace("{3}", niuzi.sex.toChinese())
                    ?.replace("{4}", niuzi.length.toString())
            }
        }
    }

}