package net.realmidc.niuzi.util

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.permission.PermissionService.Companion.hasPermission
import net.mamoe.mirai.contact.Group
import net.realmidc.niuzi.PluginMain
import net.realmidc.niuzi.entity.NiuZi
import net.realmidc.niuzi.data.sql.Dao
import net.realmidc.niuzi.util.Locale.sendLang
import java.util.*
import java.util.concurrent.ThreadLocalRandom

fun randomDouble(nextInt: Int): Double = Random().nextDouble() + Random().nextInt(nextInt)

suspend fun checkNumber(group: Group, input: String): Boolean {
    return try {
        input.toDouble()
        true
    } catch (e: NumberFormatException) {
        group.sendLang("NumberError")
        Random().nextGaussian()
        ThreadLocalRandom.current().nextInt()
        false
    }
}

suspend fun getAt(group: Group, at: String, checkInGroup: Boolean): Long {
    if (!at.contains("@")) {
        group.sendLang("NoAtArg")
        return -1
    }
    val data = at.replace("@", "")
    val target: Long?
    try {
        target = data.toLong()
    } catch (e: NumberFormatException) {
        group.sendLang("NoAt")
        return -1
    }
    if (checkInGroup && !group.contains(target)) {
        group.sendLang("MemberNotFound")
        return -1
    }
    return target
}

suspend fun hasNiuZi(group: Group, qq: Long): Boolean {
    return if (Dao.getByQQ(qq) == null) {
        group.sendLang("NoNiuZi")
        false
    } else true
}

fun timeToMinute(time: Long): Int = ((time / 1000) / 60).toInt()

/**
 * 比划比划的结果
 * @param source
 * @param target
 * @param delta
 * @param win 发起者是否胜利
 */
fun pk(source: NiuZi, target: NiuZi, delta: Double, win: Boolean) {
    Dao.setLength(source.owner, if (win) source.length + delta else source.length - delta)
    Dao.setLength(target.owner, if (win) target.length - delta else target.length + delta)
}

/**
 * 比划比划的结果（牛子都断掉）
 * @param source
 * @param target
 * @param delta
 */
fun pk(source: NiuZi, target: NiuZi, delta: Double) {
    Dao.setLength(source.owner, source.length - delta)
    Dao.setLength(target.owner, target.length - delta)
}

suspend fun CommandSender.permitted(): Boolean {
    return hasPermission(PluginMain.PERMISSION_ADMIN).also {
        if (it) {
            return@also
        }
        sendMessage("I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.")
    }
}