package net.realmidc.niuzi.entity

import net.realmidc.niuzi.enums.Sex
import net.realmidc.niuzi.data.sql.Dao

/**
 * @author Ting
 * @date 2022/2/16 4:58 PM
 */
class NiuZi(val owner: Long, var name: String, var length: Double, var sex: Sex, val level: Int, val points: Int) {

    fun sex(sex: Sex) {
        this.sex = sex
        Dao.changeSex(owner, this.sex)
    }

    fun name(name: String) {
        this.name = name
        Dao.changeName(owner, this.name)
    }

    fun add(length: Double) {
        this.length += length
        Dao.setLength(owner, this.length)
    }

    fun take(length: Double) {
        this.length -= length
        Dao.setLength(owner, this.length)
    }

    fun set(length: Double) {
        this.length = length
        Dao.setLength(owner, this.length)
    }
}
