package net.realmidc.niuzi.command

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member

interface SubCommand {

    suspend fun execute(sender: Member, group: Group, args: List<String>)

}