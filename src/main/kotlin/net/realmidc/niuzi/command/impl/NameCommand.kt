package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.sql.Dao
import net.realmidc.niuzi.util.Locale.sendLang
import net.realmidc.niuzi.util.hasNiuZi

class NameCommand : SubCommand {

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        if (hasNiuZi(group, sender.id)) {
            if (args.isEmpty()) {
                group.sendLang("Name.NoArgs")
                return
            }
            val name = args[0]
            Dao.changeName(sender.id, name)
            group.sendLang("Success")
        }
    }

}