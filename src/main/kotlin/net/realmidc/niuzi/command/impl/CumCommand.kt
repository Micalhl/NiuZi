package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.config.Settings
import net.realmidc.niuzi.data.TempStorage
import net.realmidc.niuzi.sql.Dao
import net.realmidc.niuzi.util.Locale.sendLang
import net.realmidc.niuzi.util.hasNiuZi
import net.realmidc.niuzi.util.randomDouble

class CumCommand : SubCommand {

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        if (Settings.cumEnable) {
            if (hasNiuZi(group, sender.id)) {
                if (TempStorage.cumdata.containsKey(sender.id)) {
                    group.sendLang("Cum.Already")
                    return
                }
                TempStorage.cumdata[sender.id] = System.currentTimeMillis() + (1000 * 60 * 60 * 24)
                val niuzi = Dao.getByQQ(sender.id)!!
                val length = randomDouble(10)
                Dao.setLength(sender.id, niuzi.length + length)
                group.sendLang("Cum.Success") {
                    it?.replace("{0}", length.toString())
                }
            }
        }
    }

}