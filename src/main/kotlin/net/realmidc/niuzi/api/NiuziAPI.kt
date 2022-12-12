package net.realmidc.niuzi.api

import net.mamoe.mirai.contact.User
import net.mamoe.mirai.contact.nameCardOrNick
import net.realmidc.niuzi.entity.NiuZi
import net.realmidc.niuzi.data.sql.Dao

/**
 * NiuZi
 * net.realmidc.niuzi.api.NiuziAPI
 *
 * @author xiaomu
 * @since 2022/12/1 2:48 PM
 */
object NiuziAPI {

    fun User.hasNiuZi(): Boolean {
        return Dao.getByQQ(id) != null
    }

    fun User.getNiuZi(): NiuZi {
        return if (hasNiuZi()) {
            Dao.getByQQ(id)!!
        } else {
            error("user $nameCardOrNick($id) doesn't have niuzi.")
        }
    }

    fun User.createNiuZi() {
        Dao.create(id)
    }

    fun User.hasLover(): Boolean {
        return Dao.hasLover(id)
    }

    fun User.getLover(): NiuZi {
        return Dao.getByQQ(Dao.getLover(id)) ?: error("user $nameCardOrNick($id) doesn't have lover.")
    }
}