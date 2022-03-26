package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.config.Settings
import net.realmidc.niuzi.data.TempStorage
import net.realmidc.niuzi.sql.Dao
import net.realmidc.niuzi.util.Locale.sendLang
import net.realmidc.niuzi.util.randomDouble
import net.realmidc.niuzi.util.timeToMinute

class DoiCommand : SubCommand {

    override fun describe(): String = "和对象贴贴！"

    override fun usage(): String? = null

    override fun needPerm(): Boolean = false

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        if (!Dao.hasLover(sender.id)) {
            group.sendLang("Lover.Doi.NoLover")
            return
        }
        val targetQQ = Dao.getLover(sender.id)
        val delta = randomDouble(21)
        val source = Dao.getByQQ(sender.id)!!
        val target = Dao.getByQQ(targetQQ)!!
        if (TempStorage.doidata.containsKey(sender.id) || TempStorage.doidata.containsKey(targetQQ)) {
            val time = TempStorage.doidata[sender.id]!!
            val now = System.currentTimeMillis()
            val minute = timeToMinute(time - now)
            group.sendLang("Lover.Doi.Fail") {
                it?.replace("{0}", minute.toString())
            }
            return
        }
        TempStorage.doidata[sender.id] = System.currentTimeMillis() + (Settings.doiCd * 1000)
        TempStorage.doidata[targetQQ] = System.currentTimeMillis() + (Settings.doiCd * 1000)
        Dao.setLength(sender.id, source.length + delta)
        Dao.setLength(targetQQ, target.length + delta)
        group.sendLang("Lover.Doi.Success") {
            it?.replace("{0}", delta.toString())
        }
    }

}