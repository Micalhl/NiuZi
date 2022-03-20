package net.realmidc.niuzi

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.getMember
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.event.subscribeGroupMessages
import net.realmidc.niuzi.config.CumData
import net.realmidc.niuzi.config.HongZhongData
import net.realmidc.niuzi.config.Settings
import net.realmidc.niuzi.sql.Dao
import net.realmidc.niuzi.util.randomDouble
import java.util.*
import kotlin.concurrent.schedule
import kotlin.random.Random

/**
 * @author Ting
 * @date 2022/2/16 4:53 PM
 */
object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "net.realmidc.niuzi",
        version = "1.0-SNAPSHOT"
    ) {
        name("NiuZi")
        author("Mical")
    }
) {

    private lateinit var dao: Dao
    private const val PREFIX = "---== 牛子系统 ==---\n"

    private val admins = arrayListOf(
        3332366064L
    )

    // val dataMap0 = hashMapOf<Long, Int>()
    //val times = hashMapOf<Long, Long>()

    override fun onEnable() {
        Settings.reload()
        if (Settings.firstEnable) {
            for (i in 1..5) {
                logger.warning("检测到你第一次运行插件，请去配置文件修改数据库信息，并把 firstEnable 改为 false。插件将不会继续加载")
            }
            return
        }

        dao = Dao(this)
        dao.connect()

        HongZhongData.load()
        CumData.load()

        globalEventChannel().subscribeGroupMessages {
            startsWith("", removePrefix = true) {
                val command = it.split(" ")
                val root = command[0]
                val args = command.drop(1)

                when (root) {
                    "牛子系统" -> {
                        group.sendMessage(
                            "牛子养成系统(未开发完成\n" +
                                    "命令：领养牛子\n" +
                                    "命令：我的牛子\n" +
                                    "命令：改牛子名 [名字]\n" +
                                    "命令：比划比划 [@对方]\n" +
                                    "注意：后两个命令两个参数中间要有空格"

                        )
                    }
                    "领养牛子" -> {
                        if (dao.getByQQ(sender.id) != null) {
                            group.sendMessage(
                                PREFIX +
                                        "？你有了你还领，有病"
                            )
                            return@startsWith
                        }
                        dao.create(sender)
                        group.sendMessage(
                            PREFIX +
                                    "领养了，输入「我的牛子」查看你的牛子信息"
                        )
                    }
                    /*
                    "爹的牛子",
                    "爸的牛子",
                    "老爹的牛子",
                    "爷爷的牛子",
                    "爷的牛子",
                    "你爷爷的牛子",
                    "你爷的牛子",
                    "你爸爸的牛子",
                    "你爸的牛子",
                    "父亲的牛子",
                    "老父亲的牛子",
                    "你父亲的牛子",
                    "你爹的牛子",
                    "👴的牛子",
                    "👨的牛子",
                    "🐎的牛子",
                    "🐴的牛子" -> {
                        group.sendMessage("你狂什么你狂？")
                        if (dataMap0.containsKey(sender.id)) {
                            dataMap0[sender.id] = dataMap0[sender.id]!! + 1
                            if (dataMap0[sender.id]!! >= 3) {
                                sender.mute(120)
                                group.sendMessage("我真服了")
                                return@startsWith
                            }
                        }
                        dataMap0[sender.id] = 1
                    }
                     */

                    "我的牛子" -> {
                        val niuzi = dao.getByQQ(sender.id)
                        if (niuzi == null) {
                            group.sendMessage(
                                PREFIX +
                                        "你没有牛子你查什么查滚"
                            )
                            return@startsWith
                        }
                        group.sendMessage(
                            PREFIX +
                                    "主人：${sender.nameCard}(${sender.id})\n" +
                                    "名称：${niuzi.name}\n" +
                                    "性别：${niuzi.sex.toChinese()}\n" +
                                    "长度：${niuzi.length}厘米"
                        )
                    }
                    "改牛子名" -> {
                        if (dao.getByQQ(sender.id) == null) {
                            group.sendMessage(
                                PREFIX +
                                        "你有牛子吗你就要改名？"
                            )
                            return@startsWith
                        }
                        if (args.isEmpty()) {
                            group.sendMessage(
                                PREFIX +
                                        "你牛子要改的名字忘给了，我怎么改？"
                            )
                            return@startsWith
                        }
                        val name = args[0]
                        dao.changeName(sender.id, name)
                        group.sendMessage(
                            PREFIX +
                                    "行了行了行了"
                        )
                    }
                    "爽一下" -> {
                        if (dao.getByQQ(sender.id) == null) {
                            group.sendMessage(
                                PREFIX +
                                        "你没有牛子你在这你想干什么啊"
                            )
                            return@startsWith
                        }
                        if (CumData.dataMap.containsKey(sender.id)) {
                            group.sendMessage(
                                PREFIX +
                                        "你已经爽过了，注意身体"
                            )
                            return@startsWith
                        }
                        CumData.dataMap[sender.id] = System.currentTimeMillis() + (1000 * 60 * 60 * 24)
                        val niuzi = dao.getByQQ(sender.id)!!
                        val length = randomDouble(10)
                        dao.setLength(sender.id, niuzi.length + length)
                        group.sendMessage(
                            PREFIX +
                                    "爽死你了吧骚货，本次射出 $length 厘米"
                        )
                    }
                    "比划比划" -> {
                        if (dao.getByQQ(sender.id) == null) {
                            group.sendMessage(
                                PREFIX +
                                        "你没有牛子你在这你想干什么啊"
                            )
                            return@startsWith
                        }
                        if (args.isEmpty()) {
                            group.sendMessage(
                                PREFIX +
                                        "不艾特人家我怎么知道你想跟谁比划？"
                            )
                            return@startsWith
                        }
                        val at = args[0]
                        if (!at.contains("@")) {
                            group.sendMessage(
                                PREFIX +
                                        "想跟谁比划比划就直接@人家就可以"
                            )
                            return@startsWith
                        }
                        val data = at.replace("@", "")
                        val target: Long?
                        try {
                            target = data.toLong()
                        } catch (e: NumberFormatException) {
                            group.sendMessage(
                                PREFIX +
                                        "你发的什么东西，你没@对方"
                            )
                            return@startsWith
                        }
                        if (!group.contains(target)) {
                            group.sendMessage(
                                PREFIX +
                                        "群里都没这人你比划什么？"
                            )
                            return@startsWith
                        }
                        if (target == sender.id) {
                            group.sendMessage(
                                PREFIX +
                                        "你跟自己比划什么？"
                            )
                            return@startsWith
                        }
                        if (dao.getByQQ(target) == null) {
                            group.sendMessage(
                                PREFIX +
                                        "真可惜！你选的比划对象人家没有牛子"
                            )
                            return@startsWith
                        }
                        val rInt = Random.nextInt(10)
                        val rDouble = Random.nextDouble()
                        val delta = rInt + rDouble
                        val s = dao.getByQQ(sender.id)!!
                        val t = dao.getByQQ(target)!!
                        if (HongZhongData.dataMap.containsKey(sender.id)) {
                            val time = HongZhongData.dataMap[sender.id]!!
                            val now = System.currentTimeMillis()
                            val delta0 = time - now
                            val minute = ((delta0 / 1000) / 60).toInt()
                            group.sendMessage(
                                PREFIX +
                                        "你牛子红肿了，等 $minute 分钟。"
                            )
                            return@startsWith
                        }
                        if (HongZhongData.dataMap.containsKey(target)) {
                            val time = HongZhongData.dataMap[target]!!
                            val now = System.currentTimeMillis()
                            val delta0 = time - now
                            val minute = ((delta0 / 1000) / 60).toInt()
                            group.sendMessage(
                                PREFIX +
                                        "对方牛子红肿了，等 $minute 分钟。"
                            )
                            return@startsWith
                        }
                        HongZhongData.dataMap[sender.id] = System.currentTimeMillis() + (Settings.pkCd * 1000)
                        HongZhongData.dataMap[target] = System.currentTimeMillis() + (Settings.pkCd * 1000)
                        // 生成一个0-100的随机的数字
                        val success = Random.nextInt(101)
                        // 如果这个数字小于40，就是输
                        if (success < 40) {
                            dao.setLength(sender.id, s.length - delta)
                            dao.setLength(target, t.length + delta)
                            group.sendMessage(
                                PREFIX +
                                        "${sender.nameCard} 和 ${group.getMember(target)!!.nameCard} 开始比划牛子，输了 $delta 厘米。"
                            )
                            // 如果大于60就是赢
                        } else if (success > 60) {
                            dao.setLength(sender.id, s.length + delta)
                            dao.setLength(target, t.length - delta)
                            group.sendMessage(
                                PREFIX +
                                        "${sender.nameCard} 和 ${group.getMember(target)!!.nameCard} 开始比划牛子，赢到了 $delta 厘米。"
                            )
                            // 如果大于40小于60就是断掉
                        } else {
                            dao.setLength(sender.id, s.length - delta)
                            dao.setLength(target, t.length - delta)
                            group.sendMessage(
                                PREFIX +
                                        "${sender.nameCard} 和 ${group.getMember(target)!!.nameCard} 开始比划牛子，不小心缠住了，两人都断了 $delta 厘米。"
                            )
                        }
                    }
                    "管理命令" -> {
                        if (admins.contains(sender.id)) {
                            if (args.isNotEmpty()) {
                                val cmd0 = args[0]
                                val temp0 = args.drop(1)
                                when (cmd0) {
                                    "牛子系统" -> {
                                        if (temp0.isNotEmpty()) {
                                            val cmd1 = temp0[0]
                                            val temp1 = temp0.drop(1)
                                            when (cmd1) {
                                                "更改长度" -> {
                                                    if (temp1.isEmpty()) {
                                                        group.sendMessage(PREFIX + "不给要改的人和长度我怎么改？")
                                                        return@startsWith
                                                    }
                                                    if (temp1.size == 1) {
                                                        group.sendMessage(PREFIX + "不给长度我怎么改？")
                                                        return@startsWith
                                                    }
                                                    if (temp1.size == 2) {
                                                        val at = temp1[0]
                                                        if (!at.contains("@")) {
                                                            group.sendMessage(
                                                                PREFIX +
                                                                        "想改谁长度就直接@人家就可以"
                                                            )
                                                            return@startsWith
                                                        }
                                                        val data = at.replace("@", "")
                                                        val target: Long?
                                                        try {
                                                            target = data.toLong()
                                                        } catch (e: NumberFormatException) {
                                                            group.sendMessage(
                                                                PREFIX +
                                                                        "你发的什么东西，你没@对方"
                                                            )
                                                            return@startsWith
                                                        }
                                                        if (!group.contains(target)) {
                                                            group.sendMessage(
                                                                PREFIX +
                                                                        "群里都没这人你瞎改什么？"
                                                            )
                                                            return@startsWith
                                                        }
                                                        val length = temp1[1]
                                                        if (length == "随机长度") {
                                                            val random = java.util.Random().nextInt(10).toDouble()
                                                            val length = java.util.Random().nextDouble() + random
                                                            dao.setLength(target, length)
                                                            group.sendMessage(PREFIX + "行了行了行了")
                                                        } else {
                                                            var l: Double? = null
                                                            try {
                                                                l = length.toDouble()
                                                            } catch (e: NumberFormatException) {
                                                                group.sendMessage(PREFIX + "你看看你发的什么东西，是数字吗？")
                                                                return@startsWith
                                                            }
                                                            dao.setLength(target, l)
                                                            group.sendMessage(PREFIX + "行了行了行了")
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            group.sendMessage(PREFIX + "你没有权限执行此命令。")
                            return@startsWith
                        }
                    }
                }
            }
        }
        Timer().schedule(0L, 20L) {
            val delete = arrayListOf<Long>()
            HongZhongData.dataMap.forEach { (qq, time) ->
                if (System.currentTimeMillis() >= time) {
                    delete.add(qq)
                }
            }
            delete.forEach { HongZhongData.dataMap.remove(it) }
        }
        Timer().schedule(0L, 20L) {
            val delete = arrayListOf<Long>()
            CumData.dataMap.forEach { (qq, time) ->
                if (System.currentTimeMillis() >= time) {
                    delete.add(qq)
                }
            }
            delete.forEach { CumData.dataMap.remove(it) }
        }
        logger.info("达达没牛子")
    }

    override fun onDisable() {
        HongZhongData.save()
        CumData.save()
        logger.info("达达牛子被吃了")
    }

}