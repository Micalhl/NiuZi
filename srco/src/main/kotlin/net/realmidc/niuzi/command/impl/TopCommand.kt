package net.realmidc.niuzi.command.impl

import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.getMember
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.message.data.ForwardMessage
import net.mamoe.mirai.message.data.PlainText
import net.realmidc.niuzi.command.SubCommand
import net.realmidc.niuzi.data.sql.Dao

/**
 * NiuZi
 * net.realmidc.niuzi.command.impl.TopCommand
 *
 * @author xiaomu
 * @since 2022/11/30 8:19 PM
 */
class TopCommand : SubCommand {

    override fun usage(): String? = null

    override fun needPerm(): Boolean = false

    override fun describe(): String = "查看牛子排行榜"

    override suspend fun execute(sender: Member, group: Group, args: List<String>) {
        val time = System.currentTimeMillis()
        val nodeList = arrayListOf<ForwardMessage.Node>()
        Dao.getAll(group).forEachIndexed { index, niuZi ->
            nodeList.add(
                ForwardMessage.Node(
                    senderId = group.bot.id,
                    time = ((time / 1000) + 1).toInt(),
                    senderName = group.bot.nameCardOrNick,
                    message = PlainText(
                        "${index + 1}.${niuZi.name}[主人:${group.getMember(niuZi.owner)!!.nameCardOrNick}(${niuZi.owner})]: ${niuZi.length}厘米"
                    )
                )
            )
        }
        if (nodeList.isEmpty()) {
            nodeList.add(
                ForwardMessage.Node(
                    senderId = group.bot.id,
                    time = ((time / 1000) + 1).toInt(),
                    senderName = group.bot.nameCardOrNick,
                    message = PlainText("太可惜了，本群还没有人领养过牛子")
                )
            )
        }
        val forwardMessage = ForwardMessage(
            brief = "[聊天记录]",
            preview = listOf("木子:我要和群主谈恋爱", "绕道走:畜生,我才13岁啊", "木子:我不管,你130岁我也要和你谈", "绕道走:6"),
            source = "聊天记录",
            summary = "查看7条转发消息",
            title = "群聊的聊天记录",
            nodeList = nodeList
        )
        group.sendMessage(forwardMessage)
    }
}