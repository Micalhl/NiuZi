package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.getMember
import net.mamoe.mirai.contact.nameCardOrNick
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.ConfigReader
import net.realmidc.niuzi.api.NiuziAPI.hasNiuZi
import net.realmidc.niuzi.data.TempStorage
import net.realmidc.niuzi.data.sql.Dao
import net.realmidc.niuzi.util.*
import net.realmidc.niuzi.util.Locale.sendLang
import kotlin.random.Random

class PKCommand : SubCommand {

    override fun describe(): String = "比划一下，赢加长度输减长度，断掉双方都减长度"

    override fun usage(): String = "[@对方]"

    override fun needPerm(): Boolean = false

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        if (!sender.hasNiuZi()) {
            group.sendLang("NoNiuZi")
            return
        }
        if (args.isEmpty()) {
            group.sendLang("PK.NoArgs")
            return
        }
        val targetQQ = getAt(group, args[0], true)
        if (targetQQ != -1L) {
            if (targetQQ == sender.id) {
                group.sendLang("PK.Same")
                return
            }
            if (Dao.getByQQ(targetQQ) == null) {
                group.sendLang("PK.TargetNoNiuZi")
                return
            }
            val targetMember = group.getMember(targetQQ)!!
            val delta = randomDouble(10)
            val source = Dao.getByQQ(sender.id)!!
            val target = Dao.getByQQ(targetQQ)!!
            if (TempStorage.hongzhongdata.containsKey(sender.id)) {
                val time = TempStorage.hongzhongdata[sender.id]!!
                val now = System.currentTimeMillis()
                val minute = time -  now
                group.sendLang("PK.SourceInCD", minute.getTimeLong())
                return
            }
            if (TempStorage.hongzhongdata.containsKey(targetQQ)) {
                val time = TempStorage.hongzhongdata[targetQQ]!!
                val now = System.currentTimeMillis()
                val minute = time - now
                group.sendLang("PK.TargetInCD", minute.getTimeLong())
                return
            }
            TempStorage.hongzhongdata[sender.id] = System.currentTimeMillis() + (ConfigReader.pkCd * 1000)
            TempStorage.hongzhongdata[targetQQ] = System.currentTimeMillis() + (ConfigReader.pkCd * 1000)
            val success = Random.nextInt(101)
            if (success < 40) {
                pk(source, target, delta, false)
                group.sendLang("PK.Lost", sender.nameCardOrNick, targetMember.nameCardOrNick, delta)
            } else if (success > 60) {
                pk(source, target, delta, true)
                group.sendLang("PK.Win", sender.nameCardOrNick, targetMember.nameCardOrNick, delta)
            } else {
                pk(source, target, delta)
                group.sendLang("PK.BothLost", sender.nameCardOrNick, targetMember.nameCardOrNick, delta)
            }
        }
    }
}