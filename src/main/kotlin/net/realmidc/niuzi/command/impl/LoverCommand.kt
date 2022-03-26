package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.*
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.sql.Dao
import net.realmidc.niuzi.util.Locale.sendLang

class LoverCommand : SubCommand {

    override fun describe(): String = "查看你的对象的牛子信息"

    override fun usage(): String? = null

    override fun needPerm(): Boolean = false

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        if (Dao.hasLover(sender.id)) {
            val lover = Dao.getLover(sender.id)
            val member = group.getMember(lover) ?: group.bot.getStranger(lover)!!
            val niuzi = Dao.getByQQ(lover)!!
            group.sendLang("Lover.Status") {
                it?.replace("{0}", member.nameCardOrNick)
                    ?.replace("{1}", member.id.toString())
                    ?.replace("{2}", niuzi.name)
                    ?.replace("{3}", niuzi.sex.toChinese())
                    ?.replace("{4}", niuzi.length.toString())
            }
        } else {
            group.sendLang("Lover.NoLover")
        }
    }

}