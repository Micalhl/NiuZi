package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.realmidc.niuzi.api.NiuziAPI.getNiuZi
import net.realmidc.niuzi.api.NiuziAPI.hasNiuZi
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.enums.Sex
import net.realmidc.niuzi.util.Locale.sendLang

/**
 * NiuZi
 * net.realmidc.niuzi.command.impl.ChangeToWomanCommand
 *
 * @author xiaomu
 * @since 2022/12/1 2:21 PM
 */
class ChangeToWomanCommand : SubCommand {

    override fun usage(): String? = null

    override fun needPerm(): Boolean = false

    override fun describe(): String = "转变为女性，扣除50厘米"

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        if (!sender.hasNiuZi()) {
            group.sendLang("NoNiuZi")
            return
        }
        if (sender.getNiuZi().sex == Sex.FEMALE) {
            group.sendLang("ChangeSex.AlreadyWoman")
            return
        }
        sender.getNiuZi().take(50.0)
        sender.getNiuZi().sex(Sex.FEMALE)
        group.sendLang("ChangeSex.Success")
    }
}