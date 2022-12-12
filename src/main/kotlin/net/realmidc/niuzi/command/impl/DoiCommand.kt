package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.api.NiuziAPI.getLover
import net.realmidc.niuzi.api.NiuziAPI.getNiuZi
import net.realmidc.niuzi.api.NiuziAPI.hasLover
import net.realmidc.niuzi.data.TempStorage
import net.realmidc.niuzi.util.Locale.sendLang
import net.realmidc.niuzi.util.getTimeLong
import net.realmidc.niuzi.util.randomDouble
import kotlin.random.Random

class DoiCommand : SubCommand {

    override fun describe(): String = "和对象贴贴！"

    override fun usage(): String? = null

    override fun needPerm(): Boolean = false

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        if (!sender.hasLover()) {
            group.sendLang("Lover.Doi.NoLover")
            return
        }
        val target = sender.getLover()
        val length = randomDouble(121)
        val now = System.currentTimeMillis()
        if (TempStorage.doidata.containsKey(sender.id) || TempStorage.doidata.containsKey(target.owner)) {
            val time = TempStorage.doidata[sender.id]!!
            group.sendLang("Lover.Doi.Fail", (time - now).getTimeLong())
            return
        }
        val h = Random.nextInt(13)
        val data = (12 + h + Random.nextDouble().toLong()) * 60 * 60 * 1000L
        TempStorage.doidata[sender.id] = now + data
        TempStorage.doidata[target.owner] = now + data
        sender.getNiuZi().add(length)
        target.add(length)
        val msg = if (h < 6) "" else "但你俩已经虚了，所以你们得等"
        group.sendLang("Lover.Doi.Success", length, msg, data.getTimeLong())
    }
}