package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.data.sql.Dao
import net.realmidc.niuzi.util.Locale.sendLang
import net.realmidc.niuzi.util.hasNiuZi

class NameCommand : SubCommand {

    override fun describe(): String = "改你的牛子的名字，支持空格，最长10个字"

    override fun usage(): String = "[要改的名字]"

    override fun needPerm(): Boolean = false

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        if (hasNiuZi(group, sender.id)) {
            if (args.isEmpty()) {
                group.sendLang("Name.NoArgs")
                return
            }
            val name = args[0]
            if (name.length > 10) {
                group.sendLang("NameTooLong")
                return
            }
            Dao.changeName(sender.id, name)
            group.sendLang("Success")
        }
    }
}