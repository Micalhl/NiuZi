package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.getMember
import net.mamoe.mirai.contact.nameCardOrNick
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.data.HongZhongData
import net.realmidc.niuzi.config.Settings
import net.realmidc.niuzi.sql.Dao
import net.realmidc.niuzi.util.*
import net.realmidc.niuzi.util.Locale.sendLang
import kotlin.random.Random

class PKCommand : SubCommand {

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        if (hasNiuZi(group, sender.id)) {
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
                if (HongZhongData.dataMap.containsKey(sender.id)) {
                    val time = HongZhongData.dataMap[sender.id]!!
                    val now = System.currentTimeMillis()
                    val minute = timeToMinute(time - now)
                    group.sendLang("PK.SourceInCD", minute)
                    return
                }
                if (HongZhongData.dataMap.containsKey(targetQQ)) {
                    val time = HongZhongData.dataMap[targetQQ]!!
                    val now = System.currentTimeMillis()
                    val minute = timeToMinute(time - now)
                    group.sendLang("PK.TargetInCD", minute)
                    return
                }
                HongZhongData.dataMap[sender.id] = System.currentTimeMillis() + (Settings.pkCd * 1000)
                HongZhongData.dataMap[targetQQ] = System.currentTimeMillis() + (Settings.pkCd * 1000)
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

}