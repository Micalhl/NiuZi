package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.realmidc.niuzi.api.NiuziAPI.createNiuZi
import net.realmidc.niuzi.api.NiuziAPI.hasNiuZi
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.util.Locale.sendLang

class GetCommand : SubCommand {

    override fun describe(): String = "领养一只牛子"

    override fun usage(): String? = null

    override fun needPerm(): Boolean = false

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        if (sender.hasNiuZi()) {
            group.sendLang("Get.HasNiuZi")
            return
        }
        sender.createNiuZi()
        group.sendLang("Get.Success")
    }
}