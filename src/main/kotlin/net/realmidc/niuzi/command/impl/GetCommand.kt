package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.sql.Dao
import net.realmidc.niuzi.util.Locale.sendLang

class GetCommand : SubCommand {

    override fun describe(): String = "领养一只牛子"

    override fun usage(): String? = null

    override fun needPerm(): Boolean = false

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        if (Dao.getByQQ(sender.id) != null) {
            group.sendLang("Get.HasNiuZi")
            return
        }
        Dao.create(sender)
        group.sendLang("Get.Success")
    }

}