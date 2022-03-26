package net.realmidc.niuzi.command

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member

interface SubCommand {

    fun describe(): String? {
        return "没有介绍"
    }

    fun usage(): String?

    fun needPerm(): Boolean

    suspend fun execute(sender: Member, group: Group, args: List<String>)

}