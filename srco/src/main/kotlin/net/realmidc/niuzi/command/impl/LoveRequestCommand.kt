package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.getMember
import net.mamoe.mirai.message.data.at
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.data.TempStorage
import net.realmidc.niuzi.data.sql.Dao
import net.realmidc.niuzi.util.Locale.sendLang
import net.realmidc.niuzi.util.getAt
import net.realmidc.niuzi.util.hasNiuZi

class LoveRequestCommand : SubCommand {

    override fun describe(): String = "和别人搞对象"

    override fun usage(): String = "[@对方]"

    override fun needPerm(): Boolean = false

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        if (hasNiuZi(group, sender.id)) {
            if (Dao.hasLover(sender.id)) {
                group.sendLang("Lover.Get.HasLover")
                return
            }
            if (args.isNotEmpty()) {
                val target = getAt(group, args[0], true)
                if (target != -1L) {
                    if (target == sender.id) {
                        group.sendLang("Lover.Get.Self")
                        return
                    }
                    if (Dao.hasLover(target)) {
                        group.sendLang("Lover.Get.Fail")
                        return
                    }
                    if (Dao.getByQQ(target) == null) {
                        group.sendLang("Lover.Get.TargetNoNiuzi")
                        return
                    }
                    if (TempStorage.lovedata.containsValue(target)) {
                        group.sendLang("Lover.Get.Request.Exists")
                    }
                    TempStorage.lovedata[sender.id] = target
                    group.sendLang("Lover.Get.Request.Send", group.getMember(target)!!.at().getDisplay(group), sender.at().getDisplay(group))
                }
            }
        }
    }
}